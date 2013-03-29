package rvmonitor.output;

import rvmonitor.RVMNameSpace;

public class RVMVariable {
	RVMVariable pred;
	String varName;
	
	public RVMVariable(String varName){
		this.varName = varName;
	}

	public RVMVariable(RVMVariable pred, String varName){
		this.pred = pred;
		this.varName = varName;
	}

	public String getVarName(){
		return varName;
	}
	
	public String toString(){
		if(pred != null)
			return pred.toString() + "." + RVMNameSpace.getRVMVar(varName);
		else
			return RVMNameSpace.getRVMVar(varName);
	}
	
}
