package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMJavaCode;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.Util;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BlockStmt;

import java.util.HashMap;

public class HandlerMethod {
	PropertyAndHandlers prop;
	RVMVariable methodName;
	RVMJavaCode handlerCode = null;
	RVMParameters specParam;
	RVMVariable categoryVar;
	String category;
	Monitor monitor;

	RVMParameters varsToRestore;
	HashMap<RVMParameter, RVMVariable> savedParams;

	// local variables for now
	RVMVariable loc = new RVMVariable("RVM_loc");
	RVMVariable staticsig = new RVMVariable("RVM_staticsig");

	private boolean has__SKIP = false;

	public HandlerMethod(PropertyAndHandlers prop, String category, RVMParameters specParam, RVMParameters commonParamInEvents,
			HashMap<RVMParameter, RVMVariable> savedParams, BlockStmt body, RVMVariable categoryVar, Monitor monitor) {
		this.prop = prop;
		this.category = category;
		this.methodName = new RVMVariable("Prop_" + prop.getPropertyId() + "_handler_" + category);
		if(body != null){
			String handlerBody = body.toString();

			if (handlerBody.indexOf("__SKIP") != -1) {
				has__SKIP = true;
			}

			handlerBody = handlerBody.replaceAll("__RESET", "this.reset()");
      handlerBody = handlerBody.replaceAll("__DEFAULT_MESSAGE", monitor.getDefaultMessage());
      //__DEFAULT_MESSAGE may contain __LOC, make sure to sub in __DEFAULT_MESSAGE first
      // -P
			handlerBody = handlerBody.replaceAll("__LOC", Util.defaultLocation);
			handlerBody = handlerBody.replaceAll("__STATICSIG", "this." + staticsig);
			handlerBody = handlerBody.replaceAll("__SKIP",
					BaseMonitor.skipEvent + " = true");
			
			this.handlerCode = new RVMJavaCode(handlerBody);
		}
		this.specParam = specParam;
		this.categoryVar = categoryVar;
		this.monitor = monitor;
		this.varsToRestore = new RVMParameters();
		this.savedParams = savedParams;

		for (RVMParameter p : prop.getUsedParametersIn(category, specParam)) {
			if (!commonParamInEvents.contains(p)) {
				this.varsToRestore.add(p);
			}
		}
	}

	public boolean has__SKIP() {
		return has__SKIP;
	}

	public RVMVariable getMethodName() {
		return methodName;
	}

	public String toString() {
		String synch = Main.useFineGrainedLock ? "synchronized " : "";
		String ret = "";

		ret += "final ";
		ret += synch;

		// if we want a handler to return some value, change it.
		ret += "void ";

		ret += methodName + " (" + this.specParam.parameterDeclString() + "){\n";

		if (Main.statistics) {
			ret += "if(" + categoryVar + ") {\n";
			ret += monitor.stat.categoryInc(prop, category);
			ret += "}\n";
		}

		for (RVMParameter p : this.varsToRestore) {
			RVMVariable v = this.savedParams.get(p);

			ret += "if(" + p.getName() + " == null && " + v + " != null){\n";
			ret += p.getName() + " = (" + p.getType() + ")" + v + ".get();\n";
			ret += "}\n";
		}

		ret += handlerCode + "\n";

		ret += "}\n";

		return ret;
	}

}
