package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import java.util.ArrayList;
import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.OptimizedCoenableSet;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BlockStmt;

/***
 * 
 * Wrapper monitor class for enforcing properties
 * 
 */
public class EnforceMonitor extends BaseMonitor {

	/**
	 * Deadlock handler code for enforcement monitor
	 * */
	private BlockStmt deadlockHandler = null;

	public EnforceMonitor(String name, RVMonitorSpec mopSpec,
			OptimizedCoenableSet coenableSet, boolean isOutermost)
			throws RVMException {
		super(name, mopSpec, coenableSet, isOutermost, "Enforcement");
		for (PropertyAndHandlers prop : props) {
			HashMap<String, BlockStmt> handlerBodies = prop.getHandlers();
			BlockStmt handlerBody = handlerBodies.get("deadlock");
			if (handlerBody != null) {
				// For now we assume there's only one deadlock handler.
				this.deadlockHandler = handlerBody;
				break;
			}
		}
	}

	/**
	 * 
	 * Print callback class declaration
	 * 
	 * */
	@Override
	public String printExtraDeclMethods() {
		String ret = "";

		// Callback class declaration
		ret += "static public class " + this.monitorName
				+ "DeadlockCallback implements com.runtimeverification.rvmonitor.java.rt.RVMCallBack { \n";
		ret += "public void apply() {\n";
		if (this.deadlockHandler != null) {
			ret += this.deadlockHandler;
		}
		ret += "\n";
		ret += "}\n";
		ret += "}\n\n";
		return ret;
	}

	/**
	 * 
	 * notify all the other waiting threads after an event was executed
	 * 
	 * */
	@Override
	public String afterEventMethod(RVMVariable monitor,
			PropertyAndHandlers prop, EventDefinition event, GlobalLock lock,
			String aspectName) {
		String ret = "";
		if (lock != null) {
			ret += lock.getName() + "_cond.signalAll();\n";
		}
		return ret;
	}

	/**
	 * 
	 * Clone the main monitor, and check whether executing current event on the
	 * cloned monitor will incur failure or not
	 * 
	 * */
	@Override
	public String beforeEventMethod(RVMVariable monitor,
			PropertyAndHandlers prop, EventDefinition event, GlobalLock lock,
			String aspectName, boolean inMonitorSet) {

		// If it is a blocking event, then it cannot be enforced.
		// TODO
//		if (event.isBlockingEvent()) {
//			return "";
//		}
		
		String ret = "";
		PropMonitor propMonitor = propMonitors.get(prop);
		String methodName = propMonitor.eventMethods.get(event.getId())
				.toString();
		ArrayList<String> blockedThreads = event.getThreadBlockedVar();
		ret += "try {\n";
		ret += "do {\n";
		RVMVariable clonedMonitor = new RVMVariable("clonedMonitor");
		ret += this.monitorName + " " + clonedMonitor + " = ("
				+ this.monitorName + ")" + monitor + ".clone();\n";

		RVMVariable enforceCategory = (RVMVariable) propMonitor.categoryVars
				.values().toArray()[0];
		ret += "cloned_monitor_condition_satisfied = " + clonedMonitor + "."
				+ methodName + "("
				// Because of CL's change
				+ /*event.getRVMParameters().parameterInvokeString() +*/ ");\n";

		ret += "if (!cloned_monitor_condition_satisfied) {\n";
		ret += "break;\n";
		ret += "}\n";

		ret += "if (!" + clonedMonitor + "." + enforceCategory + ") {\n";
		if (lock != null)
			ret += lock.getName() + "_cond.await();\n";
		ret += "}\n";
		ret += "else {\n";
		ret += "break;\n";
		ret += "}\n";
		ret += "} while (true);\n\n";

		// If there is blocking event point cut, wait for the thread to be
		// blocked
		
		// we don't need that in rv-monitor because we have separate blockingEvent

//		if (blockedThreads != null) {
//			ret += "if (!cloned_monitor_condition_satisfied){\n";
//			for (String var : blockedThreads) {
//
//				if (!(var.startsWith("\"") && var.endsWith("\"")))
//					var = monitor + "." + var;
//				ret += "while (!" + aspectName + ".containsBlockedThread("
//						+ var + ")) {\n";
//				ret += "if (!" + aspectName + ".containsThread(" + var
//						+ ")) {\n";
//				if (lock != null)
//					ret += lock.getName() + "_cond.await();\n";
//				ret += "}\n";
//				if (lock != null)
//					ret += lock.getName()
//							+ "_cond.await(50L, TimeUnit.MILLISECONDS);\n";
//				ret += "}\n";
//			}
//			ret += "}\n";
//		}

		ret += "} catch (Exception e) {\n";
		ret += "e.printStackTrace();\n";
		ret += "}\n";
		return ret;
	}

}
