package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;

public class CodeMemberField extends CodeMember {
	private final CodeType type;
	private final CodeExpr init;
	
	public final CodeType getType() {
		return this.type;
	}

	public CodeMemberField(String name, boolean statik, boolean publik, CodeType type) {
		this(name, statik, publik, type, null);
	}
	
	public CodeMemberField(String name, boolean statik, boolean publik, CodeType type, CodeExpr init) {
		super(name, statik, publik);
		
		this.type = type;
		this.init = init;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		fmt.keyword(this.publik ? "public" : "private");
		if (this.statik)
			fmt.keyword("static");
		fmt.type(this.type);
		fmt.identifier(this.name);
		if (this.init != null) {
			fmt.operator("=");
			this.init.getCode(fmt);
		}
		fmt.endOfStatement();
	}
}
