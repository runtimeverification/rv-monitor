package com.runtimeverification.rvmonitor.java.rvj.output.monitorset;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.RVMonitorStatistics;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.BaseMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BlockStmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MonitorSet {
	RVMVariable setName;
	RVMVariable monitorName;
	SuffixMonitor monitor;

	ArrayList<EventDefinition> events;
	List<PropertyAndHandlers> properties;
	boolean has__LOC;
	boolean has__STATICSIG;
	boolean hasThisJoinPoint;
	boolean existSkip = false;

	RVMVariable loc = new RVMVariable("RVM_loc");
	RVMVariable staticsig = new RVMVariable("RVM_staticsig");
	RVMVariable thisJoinPoint = new RVMVariable("thisJoinPoint");

	RVMonitorStatistics stat;
	
	GlobalLock monitorLock;

	public MonitorSet(String name, RVMonitorSpec mopSpec, SuffixMonitor monitor) {
		this.monitorName = monitor.getOutermostName();
		this.monitor = monitor;
		this.setName = new RVMVariable(monitorName + "_Set");
		this.events = new ArrayList<EventDefinition>(mopSpec.getEvents());
		this.properties = mopSpec.getPropertiesAndHandlers();

		this.has__LOC = mopSpec.has__LOC();
		this.has__STATICSIG = mopSpec.has__STATICSIG();
		this.hasThisJoinPoint = mopSpec.hasThisJoinPoint();

		for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
			for (BlockStmt handler : prop.getHandlers().values()) {
				if (handler.toString().indexOf("__SKIP") != -1) {
					existSkip = true;
				}
			}
		}
		for (EventDefinition event : events) {
			if (event.has__SKIP()) {
				existSkip = true;
				break;
			}
		}

		this.stat = new RVMonitorStatistics(name, mopSpec);
	}

	public RVMVariable getName() {
		return setName;
	}
	
	public void setMonitorLock(String lockName) {
		this.monitorLock = new GlobalLock(new RVMVariable(lockName));
	}

	public String Monitoring(RVMVariable monitorSetVar, EventDefinition event, RVMVariable loc, RVMVariable staticsig, GlobalLock lock) {
		this.monitorLock = lock;
		String ret = "";

		ret += "if(" + monitorSetVar + " != null) {\n";

//		if (has__LOC) {
//			if (loc != null)
//				ret += monitorSetVar + "." + this.loc + " = " + loc + ";\n";
//			else
//				ret += monitorSetVar + "." + this.loc + " = " +
//						"Thread.currentThread().getStackTrace()[2].toString()"
//						+ ";\n";
//			else
//				ret += monitorSetVar + "." + this.loc + " = " + "thisJoinPoint.getSourceLocation().toString()" + ";\n";
//		}
		
		if (has__STATICSIG) {
			if(staticsig != null)
				ret +=  monitorSetVar + "." + this.staticsig + " = " + staticsig + ";\n";
			else
				ret +=  monitorSetVar + "." + this.staticsig + " = " + "thisJoinPoint.getStaticPart().getSignature()" + ";\n";
		}

		if (this.hasThisJoinPoint) {
			ret += monitorSetVar + "." + this.thisJoinPoint + " = " + this.thisJoinPoint + ";\n";
		}

		ret += monitorSetVar + ".event_" + event.getId() + "(";
		ret += event.getRVMParameters().parameterString();
		ret += ");\n";

		for (RVMVariable var : monitor.getCategoryVars()) {
			ret += BaseMonitor.getNiceVariable(var) + " = " + monitorSetVar +
					"." + BaseMonitor.getNiceVariable(var) + ";\n";
		}

		ret += "}\n";

		if (this.hasThisJoinPoint) {
			ret += monitorSetVar + "." + this.thisJoinPoint + " = null;\n";
		}
		
		return ret;
	}

	public String toString() {
		String ret = "";

		RVMVariable monitor = new RVMVariable("monitor");
//		RVMVariable num_terminated_monitors = new RVMVariable("num_terminated_monitors");
		RVMVariable numAlive = new RVMVariable("numAlive");
		RVMVariable i = new RVMVariable("i");
		// elementData and size are safe since they will be accessed by the prefix "this.".

		ret += "class " + setName + " extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<" + monitorName + "> {\n";

//		if (has__LOC)
//			ret += "String " + loc + " = null;\n";
		if (this.has__STATICSIG)
			ret += "org.aspectj.lang.Signature " + staticsig + ";\n";

		if (existSkip)
			ret += "boolean " + BaseMonitor.skipEvent + " = false;\n";

		if (hasThisJoinPoint)
			ret += "org.aspectj.lang.JoinPoint " + this.thisJoinPoint + " = null;\n";

		for (RVMVariable var : this.monitor.getCategoryVars()) {
			ret += "boolean " + BaseMonitor.getNiceVariable(var) + ";\n";
		}


		ret += "\n";

		ret += setName + "(){\n";
		ret += "this.size = 0;\n";
		ret += "this.elements = new " + monitorName + "[4];\n";
		ret += "}\n";

		for (EventDefinition event : this.events) {
			String eventName = event.getId();
			RVMParameters parameters = event.getRVMParameters();

			ret += "\n";

			ret += "final void event_" + eventName + "(";
			ret += parameters.parameterDeclString();
			ret += ") {\n";


			for (RVMVariable var : this.monitor.getCategoryVars()) {
				ret += "this." + BaseMonitor.getNiceVariable(var) + " = " +
						"false;\n";
			}

			ret += "int " + numAlive + " = 0 ;\n";
			ret += "for(int " + i + " = 0; " + i + " < this.size; " + i + "++){\n";
			ret += monitorName + " " + monitor + " = this.elements[" + i + "];\n";
			ret += "if(!" + monitor + ".isTerminated()){\n";
			ret += "elements[" + numAlive + "] = " + monitor + ";\n";
			ret += numAlive + "++;\n";
			ret += "\n";
			ret += this.monitor.Monitoring(monitor, event, loc, staticsig, this.monitorLock, this.monitor.getAspectName(), true);
			ret += "}\n";
			ret += "}\n";

			ret += "for(int " + i + " = " + numAlive + "; " + i + " < this.size; " + i + "++){\n";
			ret += "this.elements[" + i + "] = null;\n";
			ret += "}\n";
			ret += "size = numAlive;\n";
			ret += "}\n";
		}

		ret += "}\n";

		return ret;
	}

	public Set<RVMVariable> getCategoryVars() {
		return this.monitor.getCategoryVars();
	}
}
