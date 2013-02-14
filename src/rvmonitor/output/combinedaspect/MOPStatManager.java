package rvmonitor.output.combinedaspect;

import rvmonitor.MOPException;
import rvmonitor.Main;
import rvmonitor.output.MOPVariable;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.JavaMOPSpec;

import java.util.HashMap;
import java.util.List;

public class MOPStatManager {

	HashMap<JavaMOPSpec, MOPStatistics> stats = new HashMap<JavaMOPSpec, MOPStatistics>();

	MOPVariable statClass;
	MOPVariable statObject;
	
	public MOPStatManager(String name, List<JavaMOPSpec> specs) throws MOPException {
		for (JavaMOPSpec spec : specs) {
			stats.put(spec, new MOPStatistics(name, spec));
		}
		
		
		statClass = new MOPVariable(name + "_Statistics");
		statObject = new MOPVariable(name + "_StatisticsInstance");
	}

	public MOPStatistics getStat(JavaMOPSpec spec){
		return stats.get(spec);
	}
	
	public String statClass() {
		String ret = "";

		if (!Main.statistics2)
			return ret;
		
		ret = "class " + statClass + " extends Thread implements rvmonitorrt.MOPObject {\n";
		
		ret += "static public long numTotalEvents = 0;\n"; 
		ret += "static public long numTotalMonitors = 0;\n"; 
		
		ret += "public void run() {\n";
		{
			ret += "System.err.println(\"# of total events: \" + " + statClass + ".numTotalEvents);\n";
			ret += "System.err.println(\"# of total monitors: \" + " + statClass + ".numTotalMonitors);\n";
		}
		ret += "}\n";
		
		ret += "}\n";
		
		return ret;
	}
	
	public String incEvent(JavaMOPSpec spec, EventDefinition event){
		String ret = "";

		if (!Main.statistics2)
			return ret;

		ret += statClass + ".numTotalEvents++;\n";
		
		return ret;
	}
	
	public String incMonitor(JavaMOPSpec spec){
		String ret = "";

		if (!Main.statistics2)
			return ret;
		
		ret += statClass + ".numTotalMonitors++;\n";
		
		return ret;
	}
	
	public String fieldDecl2() {
		String ret = "";

		if (!Main.statistics2)
			return ret;

		ret += "private static " + statClass + " " + statObject + ";\n";
		
		return ret;
	}

	public String constructor() {
		String ret = "";

		if (!Main.statistics2)
			return ret;

		ret += statObject + " = new " + statClass + "();\n";
		ret += "Runtime.getRuntime().addShutdownHook(" + statObject + ");\n";
		
		return ret;
	}
	
	
	
	public String fieldDecl() {
		String ret = "";

		if (!Main.statistics)
			return ret;

		ret += "// Declarations for Statistics \n";
		for (MOPStatistics stat : stats.values()) {
			ret += stat.fieldDecl();
		}
		ret += "\n";

		return ret;
	}

	public String advice() {
		String ret = "";

		if (!Main.statistics)
			return ret;

		ret += "\n";
		ret += "// advices for Statistics \n";
		for (MOPStatistics stat : stats.values()) {
			ret += stat.advice();
		}

		return ret;
	}

}
