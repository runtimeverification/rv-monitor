package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitor;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeVarDeclStmt extends CodeStmt {
	private final CodeVariable var;
	private final CodeExpr init;
	
	public final CodeVariable getVariable() {
		return this.var;
	}
	
	public CodeVarDeclStmt(CodeVariable var) {
		this(var, null);
	}
	
	public CodeVarDeclStmt(CodeVariable var, CodeExpr init) {
		this.var = var;
		this.init = init;
	
		this.validate();
	}
	
	private void validate() {
		if (this.var == null)
			throw new IllegalArgumentException();
	}
	
	@Override
	public boolean isBlock() {
		return false;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		if (fmt.needsPrintVariableDescription()) {
			String desc = this.var.getDescription();
			if (desc != null)
				fmt.comment(desc);
		}
		fmt.type(this.var.getType());
		fmt.identifier(this.var.getName());
		if (this.init != null) {
			fmt.operator("=");
			this.init.getCode(fmt);
		}
	}

	@Override
	public void accept(ICodeVisitor visitor) {
		if (this.init != null)
			this.init.accept(visitor);
		
		visitor.declareVariable(this);
	}
}
