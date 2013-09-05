package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree;

import java.util.Map;
import java.util.TreeMap;

import com.runtimeverification.rvmonitor.java.rvj.output.CodeGenerationOption;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeAssignStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeBinOpExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeConditionStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeFieldRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMemberField;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMethodInvokeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeFormatters;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeHelper;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeGenerator;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.itf.WeakReferenceVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeImplementation.Entry;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;

public class IndexingCacheNew implements ICodeGenerator {
	private final TreeMap<RVMParameter, CodeMemberField> keys;
	private final IndexingTreeImplementation.Entry valueEntry;
	private final CodeMemberField valueField;
	
	public CodeMemberField getValueField() {
		return this.valueField;
	}
	
	private IndexingCacheNew(TreeMap<RVMParameter, CodeMemberField> keys, Entry valueEntry, CodeMemberField valueField) {
		this.keys = keys;
		this.valueEntry = valueEntry;
		this.valueField = valueField;
		
		this.validate();
	}
	
	private void validate() {
		if (this.keys == null)
			throw new IllegalArgumentException();
		if (this.valueEntry == null)
			throw new IllegalArgumentException();
		if (this.valueField == null)
			throw new IllegalArgumentException();
	}

	public static IndexingCacheNew fromTree(String treename, IndexingTreeInterface itf) {
		TreeMap<RVMParameter, CodeMemberField> keys = new TreeMap<RVMParameter, CodeMemberField>();
		for (RVMParameter key : itf.getQueryParams()) {
			CodeType keytype;
			if (CodeGenerationOption.isCacheKeyWeakReference())
				keytype = CodeHelper.RuntimeType.getWeakReference();
			else {
				// The legacy code does not provide type information of parameters.
				// Also, it does not enforce a specific type; everything is treated
				// as an Object object.
				keytype = CodeType.object();
			}
			String fieldname = CodeHelper.VariableName.getIndexingTreeCacheKeyName(treename, key);
			CodeMemberField field = new CodeMemberField(fieldname, true, false, keytype);
			keys.put(key, field);
		}
		
		if (keys.size() == 0) {
			// If this indexing tree does not have any keys, a cache is meaningless.
			return null;
		}
		
		Entry lastentry = itf.lookupEntry(itf.getQueryParams());

		String fieldname = CodeHelper.VariableName.getIndexingTreeCacheValueName(treename);
		CodeMemberField field = new CodeMemberField(fieldname, true, false, lastentry.getCodeType());
		
		return new IndexingCacheNew(keys, lastentry, field);
	}
	
	public CodeExpr getKeyCompareCode() {
		CodeExpr prev = null;
		for (Map.Entry<RVMParameter, CodeMemberField> entry : this.keys.entrySet()) {
			RVMParameter param = entry.getKey();
			CodeMemberField field = entry.getValue();

			CodeVarRefExpr lhs;
			{
				// The legacy code does not provide any type information of parameters.
				CodeType type = CodeType.object();
				CodeVariable paramvar = new CodeVariable(type, param.getName());
				lhs = new CodeVarRefExpr(paramvar);
			}

			CodeExpr rhs = new CodeFieldRefExpr(field);
			if (CodeGenerationOption.isCacheKeyWeakReference())
				rhs = new CodeMethodInvokeExpr(CodeType.object(), rhs, "get");

			CodeExpr eqexpr = CodeBinOpExpr.identical(lhs, rhs);
			if (prev == null)
				prev = eqexpr;
			else
				prev = CodeBinOpExpr.logicalAnd(prev, eqexpr);
		}
		return prev;
	}
	
	/**
	 * Generates code for retrieving the cached value.
	 * @param weakrefs weak references
	 * @param destref reference to the variable for holding the cached value
	 * @return the generated code
	 */
	public CodeStmtCollection getCacheRetrievalCode(WeakReferenceVariables weakrefs, CodeVarRefExpr destref) {
		CodeStmtCollection stmts = new CodeStmtCollection();

		{
			CodeFieldRefExpr fieldref = new CodeFieldRefExpr(this.valueField);
			CodeAssignStmt assign = new CodeAssignStmt(destref, fieldref);
			stmts.add(assign);
		}
		
		if (CodeGenerationOption.isCacheKeyWeakReference()) {
			// wr_p = cacheKey_p
			for (Map.Entry<RVMParameter, CodeMemberField> entry : this.keys.entrySet()) {
				RVMParameter param = entry.getKey();
				CodeMemberField field = entry.getValue();
				CodeVariable weakref = weakrefs.getWeakRef(param);
				
				CodeAssignStmt assign = new CodeAssignStmt(
					new CodeVarRefExpr(weakref),
					new CodeFieldRefExpr(field));
				stmts.add(assign);
			}
		}
	
		return stmts;
	}
	
	/**
	 * Generates code that stores the keys and the value of the cache.
	 * The generated code guarantees that the stored value is always non-null.
	 * @param valueref the reference to the value to be stored
	 * @return the generated code
	 */
	public CodeStmtCollection getCacheUpdateCode(CodeVarRefExpr valueref) {
		CodeStmtCollection stmts = new CodeStmtCollection();
		
		CodeExpr ifnonnull = CodeBinOpExpr.isNotNull(valueref);
		CodeStmtCollection ifbody = new CodeStmtCollection();
		stmts.add(new CodeConditionStmt(ifnonnull, ifbody));
		
		for (Map.Entry<RVMParameter, CodeMemberField> entry : this.keys.entrySet()) {
			CodeMemberField field = entry.getValue();
			CodeFieldRefExpr fieldref = new CodeFieldRefExpr(field);
	
			RVMParameter param = entry.getKey();
			CodeVariable paramvar = new CodeVariable(field.getType(), param.getName());
			CodeVarRefExpr keyref = new CodeVarRefExpr(paramvar);

			CodeAssignStmt assign = new CodeAssignStmt(fieldref, keyref);
			ifbody.add(assign);
		}
	
		{
			CodeFieldRefExpr fieldref = new CodeFieldRefExpr(this.valueField);
			CodeAssignStmt assign = new CodeAssignStmt(fieldref, valueref);
			ifbody.add(assign);
		}

		return stmts;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		for (CodeMemberField field : this.keys.values())
			field.getCode(fmt);
		
		this.valueField.getCode(fmt);
	}
	
	@Override
	public String toString() {
		ICodeFormatter fmt = CodeFormatters.getDefault();
		this.getCode(fmt);
		return fmt.getCode();
	}
}
