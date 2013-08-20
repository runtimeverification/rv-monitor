package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.java.rvj.RVMNameSpace;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.OptimizedCoenableSet;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.RVMonitorStatistics;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.*;

import java.util.HashMap;
import java.util.List;

public class MonitorTermination {

	RVMParameters parameters;
	List<EventDefinition> events;
	OptimizedCoenableSet coenableSet;

	RVMonitorStatistics stat;
	
	HashMap<RVMParameter, RVMVariable> references = new HashMap<RVMParameter, RVMVariable>();
	HashMap<String, RefTree> refTrees;

	public MonitorTermination(String name, RVMonitorSpec mopSpec, List<EventDefinition> events, OptimizedCoenableSet coenableSet){
		this.parameters = mopSpec.getParameters();
		this.events = events;
		this.coenableSet = coenableSet;
		
		this.stat = new RVMonitorStatistics(name, mopSpec);
	}
	
	public String getRefType(RVMParameter p){
		if (refTrees != null) {
			RefTree refTree = refTrees.get(p.getType().toString());
			return refTree.getResultType();
		}
		return p.getType().toString();
	}
	
	public void setRefTrees(HashMap<String, RefTree> refTrees){
		this.refTrees = refTrees;

		for (RVMParameter param : parameters) {
			references.put(param, new RVMVariable("RVMRef_" + param.getName()));
		}
	}
	
	public String copyAliveParameters(RVMVariable toMonitor, RVMVariable fromMonitor){
		String ret = "";
		
		for (int j = 0; j < coenableSet.getParameterGroups().size(); j++) {
			RVMVariable alive_parameter = new RVMVariable("alive_parameters_" + j);
			
			ret += toMonitor + "." + alive_parameter + " = " + fromMonitor + "." + alive_parameter + ";\n";
		}
		
		return ret;
	}
	
	public String getCode(MonitorFeatures features) {
		String synch = Main.useFineGrainedLock ? " synchronized " : " ";
		int step = 0;
		String ret = "";

		{
			boolean generalcase = features.isNonFinalWeakRefsInMonitorNeeded();
			RVMParameters needed = features.getRememberedParameters();
			for (RVMParameter param : parameters) {
				if (generalcase || needed.contains(param)) {
					if (!generalcase)
						ret += "final ";
					ret += getRefType(param) + " " + references.get(param) + ";";
				}
				else
					ret += "// " + references.get(param) + " was suppressed to reduce memory overhead";
				ret += "\n";
			}
		}
		ret += "\n";

		for (int j = 0; j < coenableSet.getParameterGroups().size(); j++) {
			ret += "//alive_parameters_" + j + " = " + coenableSet.getParameterGroups().get(j) + "\n";
			ret += "boolean " + new RVMVariable("alive_parameters_" + j) + " = true;\n";
		}
		ret += "\n";

		ret += "@Override\n";
		ret += "protected" + synch + "final void terminateInternal(int idnum) {\n";

		ret += "switch(idnum){\n";
		for (int i = 0; i < parameters.size(); i++) {
			ret += "case " + i + ":\n";

			for (int j = 0; j < coenableSet.getParameterGroups().size(); j++) {
				if (coenableSet.getParameterGroups().get(j).contains(parameters.get(i)))
					ret += RVMNameSpace.getRVMVar("alive_parameters_" + j) + " = false;\n";
			}

			ret += "break;\n";
		}
		ret += "}\n";

		// do endObject event
		ret += "switch(RVM_lastevent) {\n";
		ret += "case -1:\n";
		ret += "return;\n";
		for (EventDefinition event : this.events) {
			ret += "case " + event.getIdNum() + ":\n";
			ret += "//" + event.getId() + "\n";

			RVMParameterSet simplifiedDNF = coenableSet.getEnable(event.getId());
			if (simplifiedDNF.size() == 1 && simplifiedDNF.get(0).size() == 0) {
				ret += "return;\n";
			} else {
				boolean firstFlag = true;

				ret += "//";
				for (RVMParameters param : simplifiedDNF) {
					if (firstFlag) {
						firstFlag = false;
					} else {
						ret += " || ";
					}
					boolean firstFlag2 = true;
					for (RVMParameter s : param) {
						if (firstFlag2) {
							firstFlag2 = false;
						} else {
							ret += " && ";
						}

						ret += "alive_" + s.getName();
					}
				}
				ret += "\n";

				ret += "if(!(";
				firstFlag = true;
				for (RVMParameters param : simplifiedDNF) {
					if (firstFlag) {
						firstFlag = false;
					} else {
						ret += " || ";
					}
					ret += "alive_parameters_" + coenableSet.getParameterGroups().getIdnum(param);
				}
				ret += ")){\n";
				ret += "RVM_terminated = true;\n";

				if (Main.statistics) {
					ret += stat.incTerminatedMonitor();
				}
				
				ret += "return;\n";
				ret += "}\n";
				ret += "break;\n";
				ret += "\n";
			}
		}
		ret += "}\n";

		ret += "return;\n";
		
		ret += "}\n";
		ret += "\n";

		if (Main.statistics) {
			ret += "protected void finalize() throws Throwable {\n";
			ret += "try {\n";
			ret += 	stat.incCollectedMonitor();
			ret += "} finally {\n";
			ret += "super.finalize();\n";
			ret += "}\n";
			ret += "}\n";
		}

		return ret;
	}
}
