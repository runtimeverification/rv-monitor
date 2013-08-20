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
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeNew.Access;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeNew.Entry;
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

	@Override
	public CodeStmtCollection visitPreNode(Entry entry, CodeExpr parentref, CodeVarRefExpr weakref, CodeExpr entryref, RVMParameter nextparam) {
		CodeExpr ifnull = CodeBinOpExpr.isNull(entryref);

		CodeStmtCollection extrastmts = new CodeStmtCollection();
		CodeNewExpr create;
		{
			if (nextparam == null) { 
				// The above condition tells that this is the last map; i.e., below this entry,
				// there is no map. Creating such a non-intermediate node requires careful manipulation.
				// Currently, the rule is to create the entry or a set, but not a monitor, because
				// creating a monitor should be done conditionally, whereas an entry or a set needs
				// to be created unconditionally.
				Entry nextentry = entry.getMap().getValue();
				if (nextentry == null)
					throw new NotImplementedException();
				boolean m = nextentry.getMap() != null;
				boolean s = nextentry.getSet() != null;
				boolean l = nextentry.getLeaf() != null;
				if (!m && !s && l)
					create = null;
				else {
					create = new CodeNewExpr(nextentry.getCodeType());
					if (s && (m || l)) {
						// The above statement creates the entry, and the following creates a set
						// instance and assigns it to one field of the entry.
						CodeExpr createdset = new CodeNewExpr(nextentry.getSet());
						extrastmts.add(nextentry.generateFieldSetCode(entryref, Access.Set, createdset));
					}
				}
			}
			else {
				CodeExpr arg = null;
				if (this.specParams != null) {
					int id = this.specParams.getIdnum(nextparam);
					arg = CodeLiteralExpr.integer(id);	
				}
				if (arg == null)
					create = new CodeNewExpr(entryref.getType());
				else
					create = new CodeNewExpr(entryref.getType(), arg);
			}
		}
		
		if (create == null)
			return null;
		
		CodeStmtCollection stmts = new CodeStmtCollection();
		CodeAssignStmt assign = new CodeAssignStmt(entryref, create);
		stmts.add(assign);

		CodeMethodInvokeExpr insert = new CodeMethodInvokeExpr(CodeType.foid(), parentref, "putNode", weakref, entryref);
		CodeExprStmt insertstmt = new CodeExprStmt(insert);
		stmts.add(insertstmt);
		
		stmts.add(extrastmts);
		return new CodeStmtCollection(new CodeConditionStmt(ifnull, stmts));
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
