package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.*;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RawMonitor extends Monitor{

	RVMVariable loc = new RVMVariable("RVM_loc");
	RVMVariable activity = new RVMVariable("RVM_activity");
	RVMVariable staticsig = new RVMVariable("RVM_staticsig");
	RVMVariable lastevent = new RVMVariable("RVM_lastevent");
	RVMVariable thisJoinPoint = new RVMVariable("thisJoinPoint");
	
	RVMonitorSpec mopSpec;
	List<EventDefinition> events;
	
	boolean isGeneral;
	
	UserJavaCode monitorDeclaration;

	public RawMonitor(String name, RVMonitorSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost) throws RVMException {
		super(name, mopSpec, coenableSet, isOutermost);
		
		this.isDefined = true;

		this.mopSpec = mopSpec;
		this.isGeneral = mopSpec.isGeneral();

		this.monitorName = new RVMVariable(mopSpec.getName() + "RawMonitor");

		if (isOutermost) {
			varInOutermostMonitor = new VarInOutermostMonitor(name, mopSpec, mopSpec.getEvents());
			monitorTermination = new MonitorTermination(name, mopSpec, mopSpec.getEvents(), coenableSet);
		}

		monitorDeclaration = new UserJavaCode(mopSpec.getDeclarationsStr());

		events = mopSpec.getEvents();
		
		if (this.isDefined && mopSpec.isGeneral()){
			if(mopSpec.isFullBinding() || mopSpec.isConnected())
				monitorInfo = new MonitorInfo(mopSpec);
		}
	}

	public void setRefTrees(HashMap<String, RefTree> refTrees){
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

		if (event.getAction() != null && event.getAction().getStmts() != null && event.getAction().getStmts().size() != 0) {
			String eventActionStr = event.getAction().toString();

			eventActionStr = eventActionStr.replaceAll("return;", "return true;");
			eventActionStr = eventActionStr.replaceAll("__RESET", "this.reset()");
 			eventActionStr = eventActionStr.replaceAll("__DEFAULT_MESSAGE", defaultMessage);
      //__DEFAULT_MESSAGE may contain __LOC, make sure to sub in __DEFAULT_MESSAGE first
      // -P
			eventActionStr = eventActionStr.replaceAll("__LOC", Util.defaultLocation);
			eventActionStr = eventActionStr.replaceAll("__ACTIVITY", "this." + activity);
			eventActionStr = eventActionStr.replaceAll("__STATICSIG", "this." + staticsig);
			eventActionStr = eventActionStr.replaceAll("__SKIP",
					BaseMonitor.skipEvent + " = true");

			eventAction = new RVMJavaCode(eventActionStr);
		}

			ret += "final boolean event_" + event.getId() + "(" + event.getRVMParameters().parameterDeclString() + ") {\n";

		if ( has__SKIP)
			ret += "boolean " + BaseMonitor.skipEvent + " = false;\n";

		if (!condition.isEmpty()) {
			ret += "if (!(" + condition + ")) {\n";
			ret += "return false;\n";
			ret += "}\n";
		}

		if (isOutermost) {
			ret += lastevent + " = " + idnum + ";\n";
		}

		if(eventAction != null)
			ret += eventAction;

		ret += "return true;\n";
		ret += "}\n";

		return ret;
	}

	public String Monitoring(RVMVariable monitorVar, EventDefinition event, RVMVariable loc, RVMVariable staticsig, GlobalLock l, String aspectName, boolean inMonitorSet) {
		String ret = "";

//		if (has__LOC) {
//			if(loc != null)
//				ret += monitorVar + "." + this.loc + " = " + loc + ";\n";
//			else
//				ret += monitorVar + "." + this.loc + " = " +
//						"Thread.currentThread().getStackTrace()[2].toString()"
//					+ ";\n";
//				ret += monitorVar + "." + this.loc + " = " + "thisJoinPoint.getSourceLocation().toString()" + ";\n";
//		}
		
		if (has__STATICSIG) {
			if(staticsig != null)
				ret += monitorVar + "." + this.staticsig + " = " + staticsig + ";\n";
			else
				ret += monitorVar + "." + this.staticsig + " = " + "thisJoinPoint.getStaticPart().getSignature()" + ";\n";
		}

		
		if (this.hasThisJoinPoint){
			ret += monitorVar + "." + this.thisJoinPoint + " = " + this.thisJoinPoint + ";\n";
		}

		ret += monitorVar + ".event_" + event.getId() + "(";
		ret += event.getRVMParameters().parameterString();
		ret += ");\n";
		
		if (this.hasThisJoinPoint){
			ret += monitorVar + "." + this.thisJoinPoint + " = null;\n";
		}
		
		return ret;
	}
	
	public MonitorInfo getMonitorInfo(){
		return monitorInfo;
	}

	public String toString() {
		String ret = "";
	
		ret += "class " + monitorName;
		if (isOutermost)
			ret += " extends com.runtimeverification.rvmonitor.java.rt.RVMMonitor";
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
		if (this.has__STATICSIG)
			ret += "org.aspectj.lang.Signature " + staticsig + ";\n";

		if (this.hasThisJoinPoint)
			ret += "org.aspectj.lang.JoinPoint " + thisJoinPoint + " = null;\n";
		
		// events
		for (EventDefinition event : this.events) {
			ret += this.doEvent(event) + "\n";
		}
		
		//reset
		ret += "final void reset() {\n";
		if (isOutermost) {
			ret += lastevent + " = -1;\n";
		}
		ret += "}\n";
		ret += "\n";
		
		//endObject and some declarations
		if (isOutermost) {
			ret += monitorTermination + "\n";
		}
		
		if (monitorInfo != null)
			ret += monitorInfo.monitorDecl();

		ret += "}\n";
		
		return ret;
	}
}
