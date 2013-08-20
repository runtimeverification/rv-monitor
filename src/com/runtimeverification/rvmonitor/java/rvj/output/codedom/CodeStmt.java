package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public abstract class CodeStmt extends CodeObject {
	public abstract boolean isBlock();
	
	public static CodeStmt fromLegacy(String rawstmt) {
		return new CodeLegacyStmt(rawstmt);
	}
}

class CodeLegacyStmt extends CodeStmt {
	private final String rawstmt;
	
	CodeLegacyStmt(String rawstmt) {
		this.rawstmt = rawstmt;
	}
	
	@Override
	public boolean isBlock() {
		return true;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		fmt.legacyStmt(this.rawstmt);
	}
}