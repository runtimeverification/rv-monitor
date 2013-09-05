package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree;

import com.runtimeverification.rvmonitor.java.rvj.output.NotImplementedException;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeAssignStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeBinOpExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeConditionStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeExprStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMethodInvokeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeNewExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeObject;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeImplementation.Access;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeImplementation.Entry;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public interface IIndexingTreeVisitor<T extends CodeObject> {
	public CodeStmtCollection visitPreNode(Entry entry, CodeExpr parentref, CodeVarRefExpr weakref, CodeExpr entryref, RVMParameter nextparam);
	public CodeStmtCollection visitPostNode(Entry entry, CodeVarRefExpr entryref, RVMParameter nextparam, CodeStmtCollection nested);
	public CodeStmtCollection visitSecondLast(Entry entry, CodeExpr entryref);
	public CodeStmtCollection visitLast(Entry entry, CodeExpr entryref);
}

/**
 * This visitor is for generative traversal.
 * The resulting code will look like the following:
 * <code>
 *   if (... == null) {
 *     create a node
 *     insert the node into the map (or set)
 *   }
 *   if (... == null) {
 *     create a node
 *     insert the node into the map (or set)
 *   }
 * </code>
 * That is, the resulting code will not have nesting structure.
 * 
 * @author Choonghwan Lee <clee83@illinois.edu>
 *
 */
abstract class GenerativeIndexingTreeVisitor implements IIndexingTreeVisitor<CodeVarRefExpr> {
	private final RVMParameters specParams;
	
	protected GenerativeIndexingTreeVisitor(RVMParameters specParams) {
		this.specParams = specParams;
	}
	
	private CodeNewExpr createSet(Entry nextentry) {
		return new CodeNewExpr(nextentry.getSet());
	}
	
	private CodeNewExpr createMap(Entry nextentry, RVMParameter nextparam) {
		CodeExpr arg = null;
		if (this.specParams != null) {
			int id = this.specParams.getIdnum(nextparam);
			arg = CodeLiteralExpr.integer(id);	
		}
		if (arg == null)
			return new CodeNewExpr(nextentry.getMap().getCodeType());
		else
			return new CodeNewExpr(nextentry.getMap().getCodeType(), arg);
	}

	@Override
	public CodeStmtCollection visitPreNode(Entry entry, CodeExpr parentref, CodeVarRefExpr weakref, CodeExpr entryref, RVMParameter nextparam) {
		CodeExpr ifnull = CodeBinOpExpr.isNull(entryref);
		CodeStmtCollection ifbody = new CodeStmtCollection();

		Entry nextentry = entry.getMap().getValue();
		if (nextentry == null)
			throw new NotImplementedException();
		boolean m = nextentry.getMap() != null;
		boolean s = nextentry.getSet() != null;
		boolean l = nextentry.getLeaf() != null;
		boolean tuple = (m && s) || (s && l) || (l && m);
	
		CodeNewExpr createentry;
		if (tuple) {
			// A tuple is created when multiple fields need to be stored.
			createentry = new CodeNewExpr(nextentry.getCodeType());
		}
		else {
			if (m)
				createentry = this.createMap(nextentry, nextparam);
			else if (s)
				createentry = this.createSet(nextentry);
			else {
				// A monitor cannot be simply created. This should be and will be
				// created by the caller after some computation.
				createentry = null;
			}
		}
		
		if (createentry == null)
			return null;

		CodeAssignStmt assign = new CodeAssignStmt(entryref, createentry);
		ifbody.add(assign);

		CodeExpr mapref = entry.generateFieldGetInlinedCode(parentref, Access.Map);
		CodeMethodInvokeExpr insert = new CodeMethodInvokeExpr(CodeType.foid(), mapref, "putNode", weakref, entryref);
		CodeExprStmt insertstmt = new CodeExprStmt(insert);
		ifbody.add(insertstmt);
		
		if (tuple) {
			// The current policy is that, whenever a tuple is created, its map and set are
			// instantiated.
			if (m) {
				CodeExpr createdmap = this.createMap(nextentry, nextparam);
				ifbody.add(nextentry.generateFieldSetCode(entryref, Access.Map, createdmap));
			}
			if (s) {
				CodeExpr createdset = this.createSet(nextentry);
				ifbody.add(nextentry.generateFieldSetCode(entryref, Access.Set, createdset));
			}
		}
	
		return new CodeStmtCollection(new CodeConditionStmt(ifnull, ifbody));
	}

	@Override
	public CodeStmtCollection visitPostNode(Entry entry, CodeVarRefExpr entryref, RVMParameter nextparam, CodeStmtCollection nested) {
		return nested;
	}
}

/**
 * This visitor is for non-generative traversal.
 * The resulting code will look like the following:
 * <code>
 *   if (... != null) {
 *     ...
 *     if (... != null) {
 *       ...
 *     }
 *   }
 * </code>
 * That is, the resulting code will have nesting structure.
 *
 * @author Choonghwan Lee <clee83@illinois.edu>
 */
abstract class NonGenerativeIndexingTreeVisitor implements IIndexingTreeVisitor<CodeVarRefExpr> {
	private final boolean suppressLastNullCheck;
	
	protected NonGenerativeIndexingTreeVisitor(boolean suppressLastNullCheck) {
		this.suppressLastNullCheck = suppressLastNullCheck;
	}
	
	@Override
	public CodeStmtCollection visitPreNode(Entry entry, CodeExpr parentref, CodeVarRefExpr weakref, CodeExpr entryref, RVMParameter nextparam) {
		return null;
	}

	@Override
	public CodeStmtCollection visitPostNode(Entry entry, CodeVarRefExpr entryref, RVMParameter nextparam, CodeStmtCollection nested) {
		// If this is the last node and null-check is unnecessary, do not touch it.
		if (nextparam == null && this.suppressLastNullCheck)
			return nested;
		
		CodeExpr ifnull = CodeBinOpExpr.isNotNull(entryref);
		CodeConditionStmt cond = new CodeConditionStmt(ifnull, nested);
		return new CodeStmtCollection(cond);
	}	
}
