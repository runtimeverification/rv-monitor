package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import java.util.Arrays;
import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;

public class CodeMethodInvokeExpr extends CodeExpr {
	private final CodeExpr target;
	private final String methodname;
	private final List<CodeExpr> arguments;
	
	public CodeMethodInvokeExpr(CodeType type, CodeExpr target, String methodname, CodeExpr ... args) {
		this(type, target, methodname, Arrays.asList(args));
	}

	public CodeMethodInvokeExpr(CodeType type, CodeExpr target, String methodname, List<CodeExpr> args) {
		super(type);

		this.target = target;
		this.methodname = methodname;
		this.arguments = args;
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		if (this.target != null) {
			this.target.getCode(fmt);
			fmt.operator(".");
		}
		fmt.identifier(this.methodname);
		fmt.operator("(");
		{
			boolean first = true;
			for (CodeExpr arg : this.arguments) {
				if (first) first = false;
				else fmt.operator(",");
				arg.getCode(fmt);
			}
		}
		fmt.operator(")");
	}
}
