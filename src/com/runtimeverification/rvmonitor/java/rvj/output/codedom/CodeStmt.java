package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitor;
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

	@Override
	public void accept(ICodeVisitor visitor) {
		// Ideally, one can parse the legacy code and mark referred variables.
		// Given that legacy code should disappear in the future, however, it
		// seems it's waste of time to implement that. The current implementation
		// returns as if the legacy code does not refer to any variable.
	}
}