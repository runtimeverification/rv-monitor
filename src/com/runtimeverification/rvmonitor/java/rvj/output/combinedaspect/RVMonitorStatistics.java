package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect;

import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;

import java.util.HashMap;

public class RVMonitorStatistics {
	String aspectName;

	RVMVariable numMonitor;
	RVMVariable collectedMonitor;
	RVMVariable terminatedMonitor;
	HashMap<String, RVMVariable> eventVars = new HashMap<String, RVMVariable>();
	HashMap<PropertyAndHandlers, HashMap<String, RVMVariable>> categoryVars = new HashMap<PropertyAndHandlers, HashMap<String, RVMVariable>>();
	HashMap<RVMParameter, RVMVariable> paramVars = new HashMap<RVMParameter, RVMVariable>();

	String specName;
	
	public RVMonitorStatistics(String name, RVMonitorSpec mopSpec) {
		this.aspectName = name + "MonitorAspect";
		this.specName = mopSpec.getName();
		this.numMonitor = new RVMVariable(mopSpec.getName() + "_Monitor_num");
		this.collectedMonitor = new RVMVariable(mopSpec.getName() + "_CollectedMonitor_num");
		this.terminatedMonitor = new RVMVariable(mopSpec.getName() + "_TerminatedMonitor_num");

		for (EventDefinition event : mopSpec.getEvents()) {
			RVMVariable eventVar = new RVMVariable(mopSpec.getName() + "_" + event.getId() + "_num");
			this.eventVars.put(event.getId(), eventVar);
		}

		for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
			HashMap<String, RVMVariable> categoryVarsforProp = new HashMap<String, RVMVariable>();
			for (String key : prop.getHandlers().keySet()) {
				RVMVariable categoryVar = new RVMVariable(mopSpec.getName() + "_" + prop.getPropertyId() + "_" + key + "_num");
				categoryVarsforProp.put(key, categoryVar);
			}
			this.categoryVars.put(prop, categoryVarsforProp);
		}

		for (RVMParameter param : mopSpec.getParameters()) {
			RVMVariable paramVar = new RVMVariable(mopSpec.getName() + "_" + param.getName() + "_set");
			this.paramVars.put(param, paramVar);
		}
	}

	public String fieldDecl() {
		String ret = "";
		if (!Main.statistics)
			return ret;

		ret += "static long " + numMonitor + " = 0;\n";
		ret += "static long " + collectedMonitor + " = 0;\n";
		ret += "static long " + terminatedMonitor + " = 0;\n";

		for (RVMVariable eventVar : eventVars.values()) {
			ret += "static long " + eventVar + " = 0;\n";
		}

		for (HashMap<String, RVMVariable> categoryVarsforProp : categoryVars.values()) {
			for (RVMVariable categoryVar : categoryVarsforProp.values()) {
				ret += "static long " + categoryVar + " = 0;\n";
			}
		}

		/* removed for buggy behavior */
		// for(RVMVariable paramVar : paramVars.values()){
		// ret += "static HashSet " + paramVar + " = new HashSet();\n";
		// }

		return ret;
	}

	public String paramInc(RVMParameter param) {
		String ret = "";
		if (!Main.statistics)
			return ret;

		/* removed for buggy behavior */
		// RVMVariable paramVar = null;
		//		
		// for(RVMParameter p : paramVars.keySet()){
		// if(p.getName().equals(param.getName()))
		// paramVar = paramVars.get(p);
		// }
		//		
		// if(paramVar != null)
		// ret += paramVar + ".add(" + param.getName() + ");\n";

		return ret;
	}

	public String eventInc(String eventName) {
		String ret = "";
		if (!Main.statistics)
			return ret;

		RVMVariable eventVar = eventVars.get(eventName);

		ret += eventVar + "++;\n";

		return ret;
	}

	public String categoryInc(PropertyAndHandlers prop, String category) {
		String ret = "";
		if (!Main.statistics)
			return ret;

		RVMVariable categoryVar = categoryVars.get(prop).get(category);

		ret += aspectName + "." + categoryVar + "++;\n";

		return ret;
	}

	public String incNumMonitor() {
		String ret = "";
		if (!Main.statistics)
			return ret;

		ret += aspectName + "." + numMonitor + "++;\n";

		return ret;
	}

	public String incCollectedMonitor() {
		String ret = "";
		if (!Main.statistics)
			return ret;

		ret += aspectName + "." + collectedMonitor + "++;\n";

		return ret;
	}

	public String incTerminatedMonitor() {
		String ret = "";
		if (!Main.statistics)
			return ret;

		ret += aspectName + "." + terminatedMonitor + "++;\n";

		return ret;
	}

	public String advice() {
		String ret = "";
		if (!Main.statistics)
			return ret;

		ret += "after () : execution(* *.main(..)) {\n";

		ret += "System.err.println(\"== " + this.specName + " ==\");\n";
		ret += "System.err.println(\"#monitors: \" + " + numMonitor + ");\n";

		for (String eventName : eventVars.keySet()) {
			RVMVariable eventVar = eventVars.get(eventName);
			ret += "System.err.println(\"#event - " + eventName + ": \" + " + eventVar + ");\n";
		}

		for (PropertyAndHandlers prop : categoryVars.keySet()) {
			HashMap<String, RVMVariable> categoryVarsforProp = categoryVars.get(prop);
			for (String categoryName : categoryVarsforProp.keySet()) {
				RVMVariable categoryVar = categoryVarsforProp.get(categoryName);
				ret += "System.err.println(\"#category - prop " + prop.getPropertyId() + " - " + categoryName + ": \" + " + categoryVar + ");\n";
			}
		}

		// for(RVMParameter param : paramVars.keySet()){
		// RVMVariable paramVar = paramVars.get(param);
		// ret += "System.err.println(\"#parameter - " + param.getName() +
		// ": \" + " + paramVar + ".size()" + ");\n";
		// }

		ret += "}\n";

		return ret;
	}

}
