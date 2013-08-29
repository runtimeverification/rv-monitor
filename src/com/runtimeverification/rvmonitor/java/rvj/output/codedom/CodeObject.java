package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeFormatters;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeGenerator;

public abstract class CodeObject implements ICodeGenerator, ICodeVisitable {
	@Override
	public String toString() {
		ICodeFormatter fmt = CodeFormatters.getDefault();
		this.getCode(fmt);
		return fmt.getCode();
	}
}
