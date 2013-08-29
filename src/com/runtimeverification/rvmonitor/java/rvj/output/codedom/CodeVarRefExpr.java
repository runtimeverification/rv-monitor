package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitor;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeVarRefExpr extends CodeExpr {
	private final CodeVariable var;
	
	public final CodeVariable getVariable() {
		return this.var;
	}
	
	public CodeVarRefExpr(CodeVariable var) {
		super(var.getType());
		
		this.var = var;
		
		this.validate();
	}
	
	private void validate() {
		if (this.var == null)
			throw new IllegalArgumentException();
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		fmt.identifier(this.var.getName());
	}

	@Override
	public void accept(ICodeVisitor visitor) {
		visitor.referVariable(this.var);
	}
}
