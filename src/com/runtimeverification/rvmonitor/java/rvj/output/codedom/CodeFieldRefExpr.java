package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeFieldRefExpr extends CodeExpr {
	private final CodeExpr target;
	private final CodeMemberField field;
	
	public CodeFieldRefExpr(CodeMemberField field) {
		this(null, field);
	}
	
	public CodeFieldRefExpr(CodeExpr target, CodeMemberField field) {
		super(field.getType());
		
		this.target = target;
		this.field = field;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		if (this.target != null) {
			this.target.getCode(fmt);
			fmt.operator(".");
		}
		fmt.identifier(this.field.getName());
	}
}
