package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitor;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public abstract class CodeLazyStmt extends CodeStmt {
	protected abstract CodeStmtCollection evaluate();
	
	@Override
	public final void getCode(ICodeFormatter fmt) {
		CodeStmtCollection stmts = this.evaluate();
		stmts.getCode(fmt);
	}

	@Override
	public boolean isBlock() {
		// It can be changed, but let's assume it is at this moment.
		return true;
	}

	@Override
	public void accept(ICodeVisitor visitor) {
		CodeStmtCollection stmts = this.evaluate();
		if (stmts != null)
			stmts.accept(visitor);
	}
}
