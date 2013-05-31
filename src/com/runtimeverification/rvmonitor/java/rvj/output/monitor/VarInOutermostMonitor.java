package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

import java.util.List;

public class VarInOutermostMonitor {
	RVMParameters parameters;
	List<EventDefinition> events;

	boolean isGeneral = false;
	
	RVMVariable tau = new RVMVariable("tau");

	public VarInOutermostMonitor(String name, RVMonitorSpec mopSpec, List<EventDefinition> events){
		this.parameters = mopSpec.getParameters();
		this.events = events;
		this.isGeneral = mopSpec.isGeneral();
	}
	
	public String toString(){
		String ret = "";

		if(isGeneral){
			ret += "long " + tau + " = -1;\n";
		}
		
		return ret;
	}		
}
