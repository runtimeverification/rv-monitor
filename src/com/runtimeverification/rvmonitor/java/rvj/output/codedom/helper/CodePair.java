package com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeObject;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;

public class CodePair<T extends CodeObject> {
	private final CodeStmtCollection generated;
	private final T logicalReturn;
	
	public final CodeStmtCollection getGeneratedCode() {
		return this.generated;
	}
	
	public final T getLogicalReturn() {
		return this.logicalReturn;
	}
	
	public CodePair(T logret) {
		this.generated = null;
		this.logicalReturn = logret;

		this.validate();
	}
	
	public CodePair(CodeStmt stmt, T logret) {
		this(new CodeStmtCollection(stmt), logret);
	}

	public CodePair(CodeStmtCollection gen, T logret) {
		this.generated = gen;
		this.logicalReturn = logret;

		this.validate();
	}
	
	private void validate() {
		// this.generated can be null.
		// this.logicalReturn can be null.
	}
}
