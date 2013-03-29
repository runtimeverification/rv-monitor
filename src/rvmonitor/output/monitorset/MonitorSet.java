package rvmonitor.output.monitorset;

import rvmonitor.output.RVMVariable;
import rvmonitor.output.combinedaspect.GlobalLock;
import rvmonitor.output.combinedaspect.RVMonitorStatistics;
import rvmonitor.output.monitor.BaseMonitor;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.mopspec.RVMParameters;
import rvmonitor.parser.ast.mopspec.PropertyAndHandlers;
import rvmonitor.parser.ast.stmt.BlockStmt;

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

		ret += "class " + setName + " extends rvmonitorrt.RVMSet {\n";
		ret += "protected " + monitorName + "[] elementData;\n";
//		ret += "int size;\n";

//		if (has__LOC)
//			ret += "String " + loc + " = null;\n";
		if (this.has__STATICSIG)
			ret += "org.aspectj.lang.Signature " + staticsig + ";\n";

		if (existSkip)
			ret += "boolean " + BaseMonitor.skipEvent + " = false;\n";

		if (hasThisJoinPoint)
			ret += "JoinPoint " + this.thisJoinPoint + " = null;\n";

		for (RVMVariable var : this.monitor.getCategoryVars()) {
			ret += "boolean " + BaseMonitor.getNiceVariable(var) + ";\n";
		}


		ret += "\n";

		ret += setName + "(){\n";
		ret += "this.size = 0;\n";
		ret += "this.elementData = new " + monitorName + "[4];\n";
		ret += "}\n";
		ret += "\n";

		ret += "public final int size(){\n";
		ret += "while(size > 0 && elementData[size-1].RVM_terminated) {\n";
		ret += "elementData[--size] = null;\n";
		ret += "}\n";
		ret += "return size;\n";
		ret += "}\n";
		ret += "\n";

		ret += "public final boolean add(RVMMonitor e){\n";
		ret += "ensureCapacity();\n";
		ret += "elementData[size++] = (" + monitorName + ")e;\n";
		ret += "return true;\n";
		ret += "}\n";
		ret += "\n";

		ret += "public final void endObject(int idnum){\n";
		ret += "int numAlive = 0;\n";
		ret += "for(int i = 0; i < size; i++){\n";
		ret += monitorName + " monitor = elementData[i];\n";
		ret += "if(!monitor.RVM_terminated){\n";
		ret += "monitor.endObject(idnum);\n";
		ret += "}\n";
		ret += "if(!monitor.RVM_terminated){\n";
		ret += "elementData[numAlive++] = monitor;\n";
		ret += "}\n";
		ret += "}\n";
		ret += "for(int i = numAlive; i < size; i++){\n";
		ret += "elementData[i] = null;\n";
		ret += "}\n";
		ret += "size = numAlive;\n";
		ret += "}\n";
		ret += "\n";

		ret += "public final boolean alive(){\n";
		ret += "for(int i = 0; i < size; i++){\n";
		ret += "RVMMonitor monitor = elementData[i];\n";
		ret += "if(!monitor.RVM_terminated){\n";
		ret += "return true;\n";
		ret += "}\n";
		ret += "}\n";
		ret += "return false;\n";
		ret += "}\n";
		ret += "\n";

		ret += "public final void endObjectAndClean(int idnum){\n";
		ret += "int size = this.size;\n";
		ret += "this.size = 0;\n";
		ret += "for(int i = size - 1; i >= 0; i--){\n";
		ret += "RVMMonitor monitor = elementData[i];\n";
		ret += "if(monitor != null && !monitor.RVM_terminated){\n";
		ret += "monitor.endObject(idnum);\n";
		ret += "}\n";
		ret += "elementData[i] = null;\n";
		ret += "}\n";
		ret += "elementData = null;\n";
		ret += "}\n";
		ret += "\n";

		ret += "public final void ensureCapacity() {\n";
		ret += "int oldCapacity = elementData.length;\n";
		ret += "if (size + 1 > oldCapacity) {\n";
		ret += "cleanup();\n";
		ret += "}\n";
		ret += "if (size + 1 > oldCapacity) {\n";
		ret += monitorName + "[] oldData = elementData;\n";
		ret += "int newCapacity = (oldCapacity * 3) / 2 + 1;\n";
		ret += "if (newCapacity < size + 1){\n";
		ret += "newCapacity = size + 1;\n";
		ret += "}\n";
		ret += "elementData = Arrays.copyOf(oldData, newCapacity);\n";
		ret += "}\n";
		ret += "}\n";
		ret += "\n";

//		ret += "final void cleanup() {\n";
//		ret += "int num_terminated_monitors = 0 ;\n";
//		ret += "for(int i = 0; i + num_terminated_monitors < size; i ++){\n";
//		ret += monitorName + " monitor = ";
//		ret += "(" + monitorName + ")elementData[i + num_terminated_monitors];\n";
//		ret += "if(monitor.RVM_terminated){\n";
//		ret += "if(i + num_terminated_monitors + 1 < size){\n";
//		ret += "do{\n";
//		ret += "monitor = (" + monitorName + ")elementData[i + (++num_terminated_monitors)];\n";
//		ret += "} while(monitor.RVM_terminated && i + num_terminated_monitors + 1 < size);\n";
//		ret += "if(monitor.RVM_terminated){\n";
//		ret += "num_terminated_monitors++;\n";
//		ret += "break;\n";
//		ret += "}\n";
//		ret += "} else {\n";
//		ret += "num_terminated_monitors++;\n";
//		ret += "break;\n";
//		ret += "}\n";
//		ret += "}\n";
//		ret += "if(num_terminated_monitors != 0){\n";
//		ret += "elementData[i] = monitor;\n";
//		ret += "}\n";
//		ret += "}\n";
//		ret += "if(num_terminated_monitors != 0){\n";
//		ret += "size -= num_terminated_monitors;\n";
//		ret += "for(int i = size; i < size + num_terminated_monitors ; i++){\n";
//		ret += "elementData[i] = null;\n";
//		ret += "}\n";
//		ret += "}\n";
//		ret += "}\n";

		ret += "final void cleanup() {\n";
		ret += "int numAlive = 0 ;\n";
		ret += "for(int i = 0; i < size; i++){\n";
		ret += monitorName + " monitor = ";
		ret += "(" + monitorName + ")elementData[i];\n";
		ret += "if(!monitor.RVM_terminated){\n";
		ret += "elementData[numAlive] = monitor;\n";
		ret += "numAlive++;\n";
		ret += "}\n";
		ret += "}\n";
		ret += "for(int i = numAlive; i < size; i++){\n";
		ret += "elementData[i] = null;\n";
		ret += "}\n";
		ret += "size = numAlive;\n";
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
			ret += monitorName + " " + monitor + " = (" + monitorName + ")this.elementData[" + i + "];\n";
			ret += "if(!" + monitor + ".RVM_terminated){\n";
			ret += "elementData[" + numAlive + "] = " + monitor + ";\n";
			ret += numAlive + "++;\n";
			ret += "\n";
			ret += this.monitor.Monitoring(monitor, event, loc, staticsig, this.monitorLock, this.monitor.getAspectName(), true);
			ret += "}\n";
			ret += "}\n";

			ret += "for(int " + i + " = " + numAlive + "; " + i + " < this.size; " + i + "++){\n";
			ret += "this.elementData[" + i + "] = null;\n";
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
