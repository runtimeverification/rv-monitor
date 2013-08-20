package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeBlockStmt extends CodeStmt {
	private final CodeStmtCollection body;
	
	public CodeStmtCollection getBody() {
		return this.body;
	}
	
	public CodeBlockStmt(CodeStmtCollection body) {
		this.body = body;
		
		this.validate();
	}
	
	private void validate() {
		if (this.body == null)
			throw new IllegalArgumentException();
	}
	
	@Override
	public boolean isBlock() {
		return true;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		fmt.openBlock();
		this.body.getCode(fmt);
		fmt.closeBlock();
	}
}
