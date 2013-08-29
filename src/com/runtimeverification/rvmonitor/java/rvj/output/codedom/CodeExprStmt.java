package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitor;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeExprStmt extends CodeStmt {
	private final CodeExpr expr;
	
	public CodeExprStmt(CodeExpr expr) {
		this.expr = expr;
		
		this.validate();
	}
	
	private void validate() {
		if (this.expr == null)
			throw new IllegalArgumentException();
	}

	@Override
	public boolean isBlock() {
		return false;
	}
	
	@Override
	public void getCode(ICodeFormatter fmt) {
		this.expr.getCode(fmt);
	}

	@Override
	public void accept(ICodeVisitor visitor) {
		this.expr.accept(visitor);
	}
}
