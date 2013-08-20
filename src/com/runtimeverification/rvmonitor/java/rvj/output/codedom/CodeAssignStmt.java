package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeAssignStmt extends CodeStmt {
	private final CodeExpr lhs;
	private final CodeExpr rhs;
	
	public CodeAssignStmt(CodeExpr lhs, CodeExpr rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
		
		this.validate();
	}
	
	private void validate() {
		if (this.lhs == null)
			throw new IllegalArgumentException();
		if (this.rhs == null)
			throw new IllegalArgumentException();
	}
	
	@Override
	public boolean isBlock() {
		return false;
	}
	
	@Override
	public void getCode(ICodeFormatter fmt) {
		this.lhs.getCode(fmt);
		fmt.operator("=");
		this.rhs.getCode(fmt);
	}
}
