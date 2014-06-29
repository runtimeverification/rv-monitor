package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;

import java.util.List;

public class VarInOutermostMonitor {
	
//	RVMVariable tau = new RVMVariable("tau");
//	private final RVMVariable disable;

	public VarInOutermostMonitor(String name, RVMonitorSpec mopSpec, List<EventDefinition> events){
		
		// If weak-reference interning is disabled, we cannot use weak-reference's
		// 'disable' flag because there can be multiple weak references for the same
		// monitor. For that reason, the 'disable' field is added to the monitor.
//		this.disable = Main.useWeakRefInterning ? null : new RVMVariable("disable");
	}
	
	public String toString(){
		return "";
	}
}
