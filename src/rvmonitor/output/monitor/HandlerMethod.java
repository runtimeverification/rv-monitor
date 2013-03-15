package rvmonitor.output.monitor;

import rvmonitor.Main;
import rvmonitor.output.MOPJavaCode;
import rvmonitor.output.MOPVariable;
import rvmonitor.output.Util;
import rvmonitor.parser.ast.mopspec.MOPParameter;
import rvmonitor.parser.ast.mopspec.MOPParameters;
import rvmonitor.parser.ast.mopspec.PropertyAndHandlers;
import rvmonitor.parser.ast.stmt.BlockStmt;

import java.util.HashMap;

public class HandlerMethod {
	PropertyAndHandlers prop;
	MOPVariable methodName;
	MOPJavaCode handlerCode = null;
	MOPParameters specParam;
	MOPVariable categoryVar;
	String category;
	Monitor monitor;

	MOPParameters varsToRestore;
	HashMap<MOPParameter, MOPVariable> savedParams;

	// local variables for now
	MOPVariable loc = new MOPVariable("MOP_loc");
	MOPVariable staticsig = new MOPVariable("MOP_staticsig");

	private boolean has__SKIP = false;

	public HandlerMethod(PropertyAndHandlers prop, String category, MOPParameters specParam, MOPParameters commonParamInEvents,
			HashMap<MOPParameter, MOPVariable> savedParams, BlockStmt body, MOPVariable categoryVar, Monitor monitor) {
		this.prop = prop;
		this.category = category;
		this.methodName = new MOPVariable("Prop_" + prop.getPropertyId() + "_handler_" + category);
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
			
			this.handlerCode = new MOPJavaCode(handlerBody);
		}
		this.specParam = specParam;
		this.categoryVar = categoryVar;
		this.monitor = monitor;
		this.varsToRestore = new MOPParameters();
		this.savedParams = savedParams;

		for (MOPParameter p : prop.getUsedParametersIn(category, specParam)) {
			if (!commonParamInEvents.contains(p)) {
				this.varsToRestore.add(p);
			}
		}
	}

	public boolean has__SKIP() {
		return has__SKIP;
	}

	public MOPVariable getMethodName() {
		return methodName;
	}

	public String toString() {
		String ret = "";

		ret += "final ";

		// if we want a handler to return some value, change it.
		ret += "void ";

		ret += methodName + " (" + this.specParam.parameterDeclString() + "){\n";

		if (Main.statistics) {
			ret += "if(" + categoryVar + ") {\n";
			ret += monitor.stat.categoryInc(prop, category);
			ret += "}\n";
		}

		for (MOPParameter p : this.varsToRestore) {
			MOPVariable v = this.savedParams.get(p);

			ret += "if(" + p.getName() + " == null && " + v + " != null){\n";
			ret += p.getName() + " = (" + p.getType() + ")" + v + ".get();\n";
			ret += "}\n";
		}

		ret += handlerCode + "\n";

		ret += "}\n";

		return ret;
	}

}
