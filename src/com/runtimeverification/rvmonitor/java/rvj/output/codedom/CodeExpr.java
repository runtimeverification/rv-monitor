package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitor;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;

public abstract class CodeExpr extends CodeObject {
	protected final CodeType type;
	
	public CodeType getType() {
		return this.type;
	}
	
	protected CodeExpr(CodeType type) {
		this.type = type;
	}
	
	public static CodeExpr fromLegacy(CodeType type, String rawexpr) {
		return new CodeLegacyExpr(type, rawexpr);
	}
}

class CodeLegacyExpr extends CodeExpr {
	private final String rawexpr;
	
	CodeLegacyExpr(CodeType type, String rawexpr) {
		super(type);
	
		this.rawexpr = rawexpr;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		fmt.legacyExpr(this.rawexpr);
	}

	@Override
	public void accept(ICodeVisitor visitor) {
		// Ideally, one can parse the legacy code and mark referred variables.
		// Given that legacy code should disappear in the future, however, it
		// seems it's waste of time to implement that. The current implementation
		// returns as if the legacy code does not refer to any variable.
	}
}
