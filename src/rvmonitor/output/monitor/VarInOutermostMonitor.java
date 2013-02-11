package rvmonitor.output.monitor;

import java.util.List;

import rvmonitor.output.MOPVariable;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.JavaMOPSpec;
import rvmonitor.parser.ast.mopspec.MOPParameters;

public class VarInOutermostMonitor {
	MOPParameters parameters;
	List<EventDefinition> events;

	boolean isGeneral = false;
	
	MOPVariable tau = new MOPVariable("tau");

	public VarInOutermostMonitor(String name, JavaMOPSpec mopSpec, List<EventDefinition> events){
		this.parameters = mopSpec.getParameters();
		this.events = events;
		this.isGeneral = mopSpec.isGeneral();
	}
	
	public String toString(){
		String ret = "";

		if(isGeneral){
			ret += "public long " + tau + " = -1;\n";
		}
		
		return ret;
	}		
}
