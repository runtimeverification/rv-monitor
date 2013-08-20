package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.itf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runtimeverification.rvmonitor.java.rvj.output.NotImplementedException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeFieldRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMemberField;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarDeclStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeHelper;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTreeManager;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class WeakReferenceVariables {
	private final List<RVMParameter> params;
	private final Map<RVMParameter, CodeVariable> mapping;
	
	public List<RVMParameter> getParams() {
		return this.params;
	}
	
	public Map<RVMParameter, CodeVariable> getMapping() {
		return this.mapping;
	}
	
	private WeakReferenceVariables(List<RVMParameter> params, Map<RVMParameter, CodeVariable> mapping) {
		this.params = params;
		this.mapping = mapping;
	}
	
	WeakReferenceVariables(IndexingTreeManager trees, RVMParameters params) {
		this.params = new ArrayList<RVMParameter>();
		this.mapping = new HashMap<RVMParameter, CodeVariable>();
		
		for (RVMParameter param : params) {
			RefTree gwrt = trees.refTrees.get(param.getType().toString());
			CodeType type = gwrt.getResultFQType();
			CodeVariable var = CodeHelper.VariableName.getWeakRef(type, param);
	
			this.params.add(param);
			this.mapping.put(param, var);
		}
	}
	
	public static WeakReferenceVariables merge(WeakReferenceVariables ... lst) {
		List<RVMParameter> params = new ArrayList<RVMParameter>();
		Map<RVMParameter, CodeVariable> mapping = new HashMap<RVMParameter, CodeVariable>();
		
		for (WeakReferenceVariables weakrefs : lst) {
			Set<RVMParameter> setl = mapping.keySet();
			Set<RVMParameter> setr = weakrefs.mapping.keySet();
			if (!Collections.disjoint(setl, setr))
				throw new NotImplementedException();
			
			params.addAll(weakrefs.params);
			mapping.putAll(weakrefs.mapping);
		}
		
		return new WeakReferenceVariables(params, mapping);
	}
	
	public CodeVariable getWeakRef(RVMParameter param) {
		return this.mapping.get(param);
	}
	
	public CodeStmtCollection getDeclarationCode() {
		CodeStmtCollection stmts = new CodeStmtCollection();
		for (Map.Entry<RVMParameter, CodeVariable> entry : this.mapping.entrySet()) {
			CodeVarDeclStmt decl = new CodeVarDeclStmt(entry.getValue(), CodeLiteralExpr.nul());
			stmts.add(decl);
		}
		return stmts;
	}

	public CodeStmtCollection getDeclarationCode(SuffixMonitor monitorClass, CodeVarRefExpr monitorref) {
		CodeStmtCollection stmts = new CodeStmtCollection();
		for (Map.Entry<RVMParameter, CodeVariable> entry : this.mapping.entrySet()) {
			RVMVariable wrfieldname = monitorClass.getRVMonitorRef(entry.getKey());
			CodeMemberField wrfield = new CodeMemberField(wrfieldname.getVarName(), false, false, entry.getValue().getType());
			CodeFieldRefExpr wrfieldref = new CodeFieldRefExpr(monitorref, wrfield);
			
			CodeVarDeclStmt decl = new CodeVarDeclStmt(entry.getValue(), wrfieldref);
			stmts.add(decl);
		}
		return stmts;
	}
}
