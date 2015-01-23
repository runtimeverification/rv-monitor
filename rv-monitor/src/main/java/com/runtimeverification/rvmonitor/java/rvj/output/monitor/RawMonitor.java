package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.*;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class RawMonitor extends Monitor{

	private final RVMVariable activity = new RVMVariable("RVM_activity");
	private final RVMVariable lastevent = new RVMVariable("RVM_lastevent");	
	private final List<EventDefinition> events;
	
	private final UserJavaCode monitorDeclaration;

	public RawMonitor(String outputName, RVMonitorSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost) throws RVMException {
		super(outputName, mopSpec, coenableSet, isOutermost);
		
		this.isDefined = true;

		this.monitorName = new RVMVariable(mopSpec.getName() + "RawMonitor");

		if (isOutermost) {
			varInOutermostMonitor = new VarInOutermostMonitor(outputName, mopSpec, mopSpec.getEvents());
			monitorTermination = new MonitorTermination(outputName, mopSpec, mopSpec.getEvents(), coenableSet);
		}

		monitorDeclaration = new UserJavaCode(mopSpec.getDeclarationsStr());

		events = mopSpec.getEvents();
		
		if (this.isDefined && mopSpec.isGeneral()){
			if(mopSpec.isFullBinding() || mopSpec.isConnected())
				monitorInfo = new MonitorInfo(mopSpec);
		}
	}

	public void setRefTrees(TreeMap<String, RefTree> refTrees){
		this.refTrees = refTrees;
		
		if(monitorTermination != null)
			monitorTermination.setRefTrees(refTrees);
	}

	public RVMVariable getOutermostName() {
		return monitorName;
	}

	public Set<String> getNames(){
		Set<String> ret = new HashSet<String>();
		
		ret.add(monitorName.toString());
		return ret;
	}
	
	public Set<RVMVariable> getCategoryVars(){
		HashSet<RVMVariable> ret = new HashSet<RVMVariable>();
		return ret;
	}

	public String doEvent(EventDefinition event){
		String ret = "";

		int idnum = event.getIdNum();
		RVMJavaCode condition = new RVMJavaCode(event.getCondition(), monitorName);
		RVMJavaCode eventAction = null;

		if (event.getAction() != null) {
			String eventActionStr = event.getAction();

			if (!Main.generateVoidMethods) {
				eventActionStr = eventActionStr.replaceAll("return;", "return true;");
			}
			eventActionStr = eventActionStr.replaceAll("__RESET", "this.reset()");
 			eventActionStr = eventActionStr.replaceAll("__DEFAULT_MESSAGE", defaultMessage);
      //__DEFAULT_MESSAGE may contain __LOC, make sure to sub in __DEFAULT_MESSAGE first
      // -P
			eventActionStr = eventActionStr.replaceAll("__LOC", Util.defaultLocation);
			eventActionStr = eventActionStr.replaceAll("__ACTIVITY", "this." + activity);
			eventActionStr = eventActionStr.replaceAll("__SKIP",
					BaseMonitor.skipEvent + " = true");

			eventAction = new RVMJavaCode(eventActionStr);
		}

		boolean retbool = !Main.generateVoidMethods;
		String synch = Main.useFineGrainedLock ? " synchronized " : " ";
		ret += "final" + synch + (retbool ? "boolean" : "void") + " event_" + event.getId() + "(";
		{
			RVMParameters params;
			if (Main.stripUnusedParameterInMonitor)
				params = event.getReferredParameters(event.getRVMParameters());
			else
				params = event.getRVMParameters();
			ret += params.parameterDeclString();
		}
		ret += ") {\n";

		if ( has__SKIP)
			ret += "boolean " + BaseMonitor.skipEvent + " = false;\n";

		if (!condition.isEmpty()) {
			ret += "if (!(" + condition + ")) {\n";
			if (Main.generateVoidMethods)
				ret += "return;\n";
			else
				ret += "return false;\n";
			ret += "}\n";
		}

		if (isOutermost) {
			ret += lastevent + " = " + idnum + ";\n";
		}

		if(eventAction != null)
			ret += eventAction;

		if (!Main.generateVoidMethods)
			ret += "return true;\n";
		ret += "}\n";

		return ret;
	}

	public String Monitoring(RVMVariable monitorVar, EventDefinition event, RVMVariable loc, GlobalLock l, String outputName, boolean inMonitorSet) {
		String ret = "";

//		if (has__LOC) {
//			if(loc != null)
//				ret += monitorVar + "." + this.loc + " = " + loc + ";\n";
//			else
//				ret += monitorVar + "." + this.loc + " = " +
//						"Thread.currentThread().getStackTrace()[2].toString()"
//					+ ";\n";
//		}
		
		ret += monitorVar + ".event_" + event.getId() + "(";
		{
			RVMParameters params;
			if (Main.stripUnusedParameterInMonitor)
				params = event.getReferredParameters(event.getRVMParameters());
			else
				params = event.getRVMParameters();
			ret += params.parameterString();
		}
		ret += ");\n";
		
		return ret;
	}
	
	public MonitorInfo getMonitorInfo(){
		return monitorInfo;
	}

	public String toString() {
		String synch = Main.useFineGrainedLock ? " synchronized " : " ";		
		String ret = "";
	
		ret += "class " + monitorName;
		if (isOutermost)
			ret += " extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractSynchronizedMonitor";	
		ret += " implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {\n";
	
		if(varInOutermostMonitor != null)
			ret += varInOutermostMonitor;

		//clone()
		ret += "protected Object clone() {\n";
		ret += "try {\n";
		ret += monitorName + " ret = (" + monitorName + ") super.clone();\n";
		if (monitorInfo != null)
			ret += monitorInfo.copy("ret", "this");
		ret += "return ret;\n";
		ret += "}\n";
		ret += "catch (CloneNotSupportedException e) {\n";
		ret += "throw new InternalError(e.toString());\n";
		ret += "}\n";
		ret += "}\n";

		ret += monitorDeclaration + "\n";
		if (this.has__ACTIVITY)
			ret += activityCode();
//		if (this.has__LOC)
//			ret += "String " + loc + ";\n";
		// implements getState(), which returns -1
		{
			ret += "@Override\n";
			ret += "public final int getState() {\n";
			ret += "return -1;\n";
			ret += "}\n\n";
		}

		// events
		for (EventDefinition event : this.events) {
			ret += this.doEvent(event) + "\n";
		}
		
		//reset
		ret += "final" + synch + "void reset() {\n";
		if (isOutermost) {
			ret += lastevent + " = -1;\n";
		}
		ret += "}\n";
		ret += "\n";
		
		//endObject and some declarations
		if (isOutermost) {
			ret += monitorTermination.getCode(this.getFeatures(), null, null);
			ret += "\n";
		}
		
		if (monitorInfo != null)
			ret += monitorInfo.monitorDecl();

		ret += "}\n";
		
		return ret;
	}
}
