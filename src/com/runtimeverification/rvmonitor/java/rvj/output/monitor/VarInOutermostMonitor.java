package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.java.rvj.Main;
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
	private final RVMVariable disable;

	public VarInOutermostMonitor(String name, RVMonitorSpec mopSpec, List<EventDefinition> events){
		this.parameters = mopSpec.getParameters();
		this.events = events;
		this.isGeneral = mopSpec.isGeneral();
		
		// If weak-reference interning is disabled, we cannot use weak-reference's
		// 'disable' flag because there can be multiple weak references for the same
		// monitor. For that reason, the 'disable' field is added to the monitor.
		this.disable = Main.useWeakRefInterning ? null : new RVMVariable("disable");
	}
	
	public String toString(){
		String ret = "";

		if(isGeneral){
			ret += "long " + tau + " = -1;\n";
			if (this.disable != null)
				ret += "long " + this.disable + " = -1;\n";
		}
		
		return ret;
	}		
}
