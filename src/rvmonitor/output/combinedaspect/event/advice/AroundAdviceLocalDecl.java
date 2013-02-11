package rvmonitor.output.combinedaspect.event.advice;

import rvmonitor.output.MOPVariable;

public class AroundAdviceLocalDecl {

	MOPVariable skipAroundAdvice;
	
	public AroundAdviceLocalDecl(){
		skipAroundAdvice = new MOPVariable("MOP_skipAroundAdvice");	
	}
	
	
	public String toString(){
		String ret = "";
		
		ret += "boolean " + skipAroundAdvice + " = false;\n";
		
		return ret;
	}
	
}
