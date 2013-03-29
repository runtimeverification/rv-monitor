package rvmonitor.output.monitor;

import rvmonitor.output.RVMVariable;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.mopspec.RVMParameters;

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
