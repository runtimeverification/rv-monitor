package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public abstract class CodeLazyStmt extends CodeStmt {
	protected abstract void getLazyCode(ICodeFormatter fmt);
	
	@Override
	public final void getCode(ICodeFormatter fmt) {
		this.getLazyCode(fmt);
	}

	@Override
	public boolean isBlock() {
		// It can be changed, but let's assume it is at this moment.
		return true;
	}
}
