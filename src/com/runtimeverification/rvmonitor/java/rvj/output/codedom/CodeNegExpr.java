package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeNegExpr extends CodeExpr {
	private final CodeExpr nested;
	
	public CodeNegExpr(CodeExpr nested) {
		super(nested.getType());
		
		this.nested = nested;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		fmt.operator("!");
		this.nested.getCode(fmt);
	}
}
