package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import java.util.ArrayList;
import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis.ICodeVisitor;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;

public class CodeNewExpr extends CodeExpr {
	private final List<CodeExpr> arguments;
	
	public CodeNewExpr(CodeType type, CodeExpr ... args) {
		super(type);
		
		this.arguments = new ArrayList<CodeExpr>();
		for (CodeExpr arg : args)
			this.arguments.add(arg);
	}
	
	public CodeNewExpr(CodeType type, List<CodeExpr> args) {
		super(type);
		
		this.arguments = args;
	}
	
	@Override
	public void getCode(ICodeFormatter fmt) {
		fmt.keyword("new");
		fmt.type(this.type);
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

	@Override
	public void accept(ICodeVisitor visitor) {
		for (CodeExpr arg : this.arguments)
			arg.accept(visitor);
	}
}
