package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.*;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.*;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BlockStmt;

import java.util.*;
import java.util.Map.Entry;

class PropMonitor {
	RVMJavaCode cloneCode;
	RVMJavaCode localDeclaration;
	RVMJavaCode stateDeclaration;
	RVMJavaCode resetCode;
	RVMJavaCode hashcodeCode;
	RVMJavaCode equalsCode;
	RVMJavaCode initilization;
	
	RVMVariable hashcodeMethod = null;

	HashMap<String, RVMVariable> categoryVars = new HashMap<String, RVMVariable>();
	HashMap<String, HandlerMethod> handlerMethods = new HashMap<String, HandlerMethod>();
	HashMap<String, RVMVariable> eventMethods = new HashMap<String, RVMVariable>();
}

public class BaseMonitor extends Monitor {
	// fields
	RVMVariable loc = new RVMVariable("RVM_loc");
	RVMVariable staticsig = new RVMVariable("RVM_staticsig");
	RVMVariable lastevent = new RVMVariable("RVM_lastevent");
	public static RVMVariable skipEvent = new RVMVariable("skipEvent");
	RVMVariable conditionFail = new RVMVariable("RVM_conditionFail");
	RVMVariable thisJoinPoint = new RVMVariable("thisJoinPoint");

	// methods
	RVMVariable reset = new RVMVariable("reset");

	// info about spec
	List<PropertyAndHandlers> props;
	List<EventDefinition> events;
	RVMParameters specParam;
	UserJavaCode monitorDeclaration;
	String systemAspectName;
	boolean existCondition = false;
	boolean existSkip = false;
	HashMap<RVMParameter, RVMVariable> varsToSave;

	HashMap<PropertyAndHandlers, PropMonitor> propMonitors = new HashMap<PropertyAndHandlers, PropMonitor>();

	public BaseMonitor(String name, RVMonitorSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost) throws RVMException {
		super(name, mopSpec, coenableSet, isOutermost);
		this.initialize(name, mopSpec, coenableSet, isOutermost, "");
	}
	
	public BaseMonitor(String name, RVMonitorSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost, String monitorNameSuffix) throws RVMException {
		super(name, mopSpec, coenableSet, isOutermost);
		this.initialize(name, mopSpec, coenableSet, isOutermost, monitorNameSuffix);
	}
	
	public void initialize(String name, RVMonitorSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost, String monitorNameSuffix) {
		this.isDefined = true;
		this.monitorName = new RVMVariable(mopSpec.getName() + monitorNameSuffix + "Monitor");
		this.systemAspectName = name + "SystemAspect";
		this.events = mopSpec.getEvents();
		this.props = mopSpec.getPropertiesAndHandlers();
		this.monitorDeclaration = new UserJavaCode(mopSpec.getDeclarationsStr());
		this.specParam = mopSpec.getParameters();

		if (isOutermost) {
			varInOutermostMonitor = new VarInOutermostMonitor(name, mopSpec, mopSpec.getEvents());
			monitorTermination = new MonitorTermination(name, mopSpec, mopSpec.getEvents(), coenableSet);
		}
		
		String prefix = Main.merge ? this.monitorName + "_" : "";

		for (PropertyAndHandlers prop : props) {
			PropMonitor propMonitor = new PropMonitor();

			HashSet<String> cloneLocal = new HashSet<String>();
			cloneLocal.add("ret");
			
			propMonitor.cloneCode = new RVMJavaCode(prop, prop.getLogicProperty("clone"), monitorName, cloneLocal);
			propMonitor.localDeclaration = new RVMJavaCode(prop, prop.getLogicProperty("local declaration"), monitorName);
			propMonitor.stateDeclaration = new RVMJavaCode(prop, prop.getLogicProperty("state declaration"), monitorName);
			propMonitor.resetCode = new RVMJavaCode(prop, prop.getLogicProperty("reset"), monitorName);
			propMonitor.hashcodeCode = new RVMJavaCode(prop, prop.getLogicProperty("hashcode"), monitorName);
			propMonitor.equalsCode = new RVMJavaCode(prop, prop.getLogicProperty("equals"), monitorName);
			propMonitor.initilization = new RVMJavaCode(prop, prop.getLogicProperty("initialization"), monitorName);

			HashMap<String, BlockStmt> handlerBodies = prop.getHandlers();
			for (String category : prop.getHandlers().keySet()) {
				if (category.equals("deadlock"))
					continue;
				RVMVariable categoryVar = new RVMVariable(prefix + "Prop_" + prop.getPropertyId() + "_Category_" + category);
				propMonitor.categoryVars.put(category, categoryVar);

				BlockStmt handlerBody = handlerBodies.get(category);

				if (handlerBody.toString().length() != 0) {
					propMonitor.handlerMethods.put(category, new HandlerMethod(prop, category, specParam, mopSpec.getCommonParamInEvents(), varsToSave, handlerBody, categoryVar, this));
			
				}
			}
			for(EventDefinition event : events){
				RVMVariable eventMethod = new RVMVariable("Prop_" + prop.getPropertyId() + "_event_" + event.getId());
				
				propMonitor.eventMethods.put(event.getId(), eventMethod);
			}			

			propMonitors.put(prop, propMonitor);
		}

		varsToSave = new HashMap<RVMParameter, RVMVariable>();
		for (RVMParameter p : mopSpec.getVarsToSave()) {
			varsToSave.put(p, new RVMVariable("Ref_" + p.getName()));
		}

		if (this.isDefined && mopSpec.isGeneral()) {
			if (mopSpec.isFullBinding() || mopSpec.isConnected())
				monitorInfo = new MonitorInfo(mopSpec);
		}
		
		for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
			if(!existSkip){
				for (BlockStmt handler : prop.getHandlers().values()) {
					if (handler.toString().indexOf("__SKIP") != -1){
						existSkip = true;
						break;
					}
				}
			}
		}

		for (EventDefinition event : events) {
			if (event.getCondition() != null && event.getCondition().length() != 0) {
				existCondition = true;
				break;
			}
			if (event.has__SKIP()){
				existSkip = true;
				break;
			}
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

	public Set<String> getNames() {
		Set<String> ret = new HashSet<String>();

		ret.add(monitorName.toString());
		return ret;
	}

	public Set<RVMVariable> getCategoryVars() {
		HashSet<RVMVariable> ret = new HashSet<RVMVariable>();

		for (PropertyAndHandlers prop : props) {
			ret.addAll(propMonitors.get(prop).categoryVars.values());
		}

		return ret;
	}

	public String printEventMethod(PropertyAndHandlers prop, EventDefinition event, String methodNamePrefix) {
		String ret = "";

		PropMonitor propMonitor = propMonitors.get(prop);
		
		int idnum = event.getIdNum();
		RVMJavaCode condition = new RVMJavaCode(event.getCondition(), monitorName);
		RVMJavaCode eventMonitoringCode = new RVMJavaCode(prop, prop.getEventMonitoringCode(event.getId()), monitorName);
		RVMJavaCode aftereventMonitoringCode = new RVMJavaCode(prop, prop.getAfterEventMonitoringCode(event.getId()), monitorName);
		RVMJavaCode monitoringBody = new RVMJavaCode(prop, prop.getLogicProperty("monitoring body"), monitorName);
		RVMJavaCode stackManage = new RVMJavaCode(prop, prop.getLogicProperty("stack manage"), monitorName);
		HashMap<String, RVMJavaCode> categoryConditions = new HashMap<String, RVMJavaCode>();
		RVMJavaCode eventAction = null;

		for (String handlerName : prop.getHandlers().keySet()) {
			if (handlerName.equals("deadlock"))
				continue;
			String conditionStr = prop.getLogicProperty(handlerName + " condition");
			if (conditionStr.contains(":{")) {
				HashMap<String, String> conditions = new HashMap<String, String>();
				prop.parseMonitoredEvent(conditions, conditionStr);
				conditionStr = conditions.get(event.getId());
			}

			if (conditionStr != null) {
				categoryConditions.put(handlerName, new RVMJavaCodeNoNewLine(prop, conditionStr, monitorName));
			}
		}

		if(prop == props.get(props.size() - 1)){
			if (event.getAction() != null && event.getAction().getStmts() != null && event.getAction().getStmts().size() != 0) {
				String eventActionStr = event.getAction().toString();
	
				eventActionStr = eventActionStr.replaceAll("return;", "return true;");
				eventActionStr = eventActionStr.replaceAll("__RESET", "this.reset()");
				eventActionStr = eventActionStr.replaceAll("__DEFAULT_MESSAGE", defaultMessage);
        //__DEFAULT_MESSAGE may contain __LOC, make sure to sub in __DEFAULT_MESSAGE first
        // -P
				eventActionStr = eventActionStr.replaceAll("__LOC",
						Util.defaultLocation);
//						"this." + loc);
				eventActionStr = eventActionStr.replaceAll("__ACTIVITY", "this." + activity);
				eventActionStr = eventActionStr.replaceAll("__STATICSIG", "this." + staticsig);
				eventActionStr = eventActionStr.replaceAll("__SKIP", "this." + skipEvent + " = true");
	
				eventAction = new RVMJavaCode(eventActionStr);
			}
		}

		// Add return value to events, so we know whether it's because of the condition failure or not
		ret += "final boolean " + methodNamePrefix + propMonitor.eventMethods.get(event.getId()) + "(" + event.getRVMParameters().parameterDeclString() + ") {\n";

		if (prop == props.get(props.size() - 1) && eventAction != null) {
			for (RVMParameter p : event.getUsedParametersIn(specParam)) {
				if (!event.getRVMParametersOnSpec().contains(p)) {
					RVMVariable v = this.varsToSave.get(p);

					ret += p.getType() + " " + p.getName() + " = null;\n";
					ret += "if(" + v + " != null){\n";
					ret += p.getName() + " = (" + p.getType() + ")" + v + ".get();\n";
					ret += "}\n";
				}
			}

			ret += eventAction;
		}

		for (RVMParameter p : varsToSave.keySet()) {
			if (event.getRVMParametersOnSpec().contains(p)) {
				RVMVariable v = varsToSave.get(p);

				ret += "if(" + v + " == null){\n";
				ret += v + " = new WeakReference(" + p.getName() + ");\n";
				ret += "}\n";
			}
		}

		if (isOutermost) {
			ret += lastevent + " = " + idnum + ";\n";
		}

		if (monitorInfo != null)
			ret += monitorInfo.union(event.getRVMParametersOnSpec());

		ret += propMonitors.get(prop).localDeclaration;

		if (prop.getVersionedStack()) {
			RVMVariable global_depth = new RVMVariable("global_depth");
			RVMVariable version = new RVMVariable("version");

			ret += "int[] " + global_depth + " = (int[])(" + systemAspectName + ".t_global_depth.get());\n";
			ret += "int[] " + version + " = (int[])(" + systemAspectName + ".t_version.get());\n";
		}

		ret += stackManage + "\n";

		ret += eventMonitoringCode;
		
		ret += monitoringBody;

		String categoryCode = "";
		for (Entry<String, RVMJavaCode> entry : categoryConditions.entrySet()) {
			categoryCode += propMonitors.get(prop).categoryVars.get(entry.getKey()) + " = " + entry.getValue() + ";\n";
		}

		if (monitorInfo != null)
			ret += monitorInfo.computeCategory(categoryCode);
		else
			ret += categoryCode;

		ret += aftereventMonitoringCode;

		ret += "return true;\n";
		ret += "}\n";

		return ret;
	}
	
	
	public String printEventMethod(PropertyAndHandlers prop, EventDefinition event) {
		return this.printEventMethod(prop, event, "");	
	}
	
	public String Monitoring(RVMVariable monitorVar, EventDefinition event, RVMVariable loc, RVMVariable staticsig, GlobalLock lock, String aspectName, boolean inMonitorSet) {
		String ret = "";

//		if (has__LOC) {
//			if(loc != null)
//				ret += monitorVar + "." + this.loc + " = " + loc + ";\n";
//			else
//				ret += monitorVar + "." + this.loc + " = " +
//						"Thread.currentThread().getStackTrace()[2].toString()"
//					+ ";\n";
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

		for(PropertyAndHandlers prop : props){
			PropMonitor propMonitor = propMonitors.get(prop);
			
			ret += this.beforeEventMethod(monitorVar, prop, event, lock, aspectName, inMonitorSet);
			ret += monitorVar + "." + propMonitor.eventMethods.get(event.getId()) + "(";
			ret += event.getRVMParameters().parameterString();
			ret += ");\n";
			ret += this.afterEventMethod(monitorVar, prop, event, lock, aspectName);

			if (event.getCondition() != null && event.getCondition().length() != 0) {
				ret += "if(" + monitorVar + "." + conditionFail + "){\n";
				ret += monitorVar + "." + conditionFail + " = false;\n";
				ret += "} else {\n";
			}
			
			for (String category : propMonitor.handlerMethods.keySet()) {
				if (category.equals("deadlock"))
					continue;
				HandlerMethod handlerMethod = propMonitor.handlerMethods.get(category);

				final RVMVariable rvmVariable = propMonitor.categoryVars.get(category);
				ret += BaseMonitor.getNiceVariable(rvmVariable)
						+ " |= " + monitorVar + "." + rvmVariable + ";\n";
				ret += "if(" +  monitorVar + "." + rvmVariable + ") {\n";

				ret += monitorVar + "." + handlerMethod.getMethodName() + "(";
				ret += event.getRVMParametersOnSpec().parameterStringIn(specParam);
				ret += ");\n";

				ret += "}\n";
			}
			if (existSkip) {
				ret += skipEvent + " |= " + monitorVar + "." +
						skipEvent + ";\n";
				ret += monitorVar + "." + skipEvent + " = false;\n";
			}

			if (event.getCondition() != null && event.getCondition().length() != 0) {
				ret += "}\n";
			}
		}
		
		if (this.hasThisJoinPoint){
			ret += monitorVar + "." + this.thisJoinPoint + " = null;\n";
		}

		return ret;
	}
	
	public String afterEventMethod(RVMVariable monitor, PropertyAndHandlers prop, EventDefinition event, GlobalLock l, String aspectName) {
		return "";
	}

	public String beforeEventMethod(RVMVariable monitor, PropertyAndHandlers prop, EventDefinition event, GlobalLock l, String aspectName, boolean inMonitorSet) {
		return "";
	}

	public MonitorInfo getMonitorInfo(){
		return monitorInfo;
	}
	
	public String toString() {
		String ret = "";

		ret += "class " + monitorName;
		if (isOutermost)
			ret += " extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor";	
		ret += " implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {\n";
		
		if (isOutermost && varInOutermostMonitor != null)
			ret += varInOutermostMonitor;

		// clone()
		ret += "protected Object clone() {\n";
		if (Main.statistics) {
			ret += stat.incNumMonitor();
		}
		ret += "try {\n";
		ret += monitorName + " ret = (" + monitorName + ") super.clone();\n";
		if (monitorInfo != null)
			ret += monitorInfo.copy("ret", "this");
		for(PropertyAndHandlers prop : props)
			ret += propMonitors.get(prop).cloneCode;
		ret += "return ret;\n";
		ret += "}\n";
		ret += "catch (CloneNotSupportedException e) {\n";
		ret += "throw new InternalError(e.toString());\n";
		ret += "}\n";
		ret += "}\n";

		// monitor variables
		ret += monitorDeclaration + "\n";
		if (this.has__ACTIVITY)
			ret += activityCode();
//		if (this.has__LOC)
//			ret += "String " + loc + ";\n";
		if (this.has__STATICSIG)
			ret += "org.aspectj.lang.Signature " + staticsig + ";\n";
		if (this.hasThisJoinPoint)
			ret += "org.aspectj.lang.JoinPoint " + thisJoinPoint + " = null;\n";

		// references for saved parameters
		for (RVMVariable v : varsToSave.values()) {
			ret += "WeakReference " + v + " = null;\n";
		}

		if (existCondition) {
			ret += "boolean " + conditionFail + " = false;\n";
		}
		if (existSkip){
			ret += "boolean " + skipEvent + " = false;\n";
		}

		// state declaration
		for(PropertyAndHandlers prop : props){
			ret += propMonitors.get(prop).stateDeclaration;
		}
		ret += "\n";

		// category condition
		for(PropertyAndHandlers prop : props){
			PropMonitor propMonitor = propMonitors.get(prop);
			for (String category : propMonitor.categoryVars.keySet()) {
				ret += "boolean " + propMonitor.categoryVars.get(category) + " = false;\n";
			}
		}
		ret += "\n";

		// constructor
		ret += monitorName + " () {\n";
		for(PropertyAndHandlers prop : props){
			if (prop.getVersionedStack()) {
				RVMVariable global_depth = new RVMVariable("global_depth");
				RVMVariable version = new RVMVariable("version");
	
				ret += "int[] " + global_depth + " = (int[])(" + systemAspectName + ".t_global_depth.get());\n";
				ret += "int[] " + version + " = (int[])(" + systemAspectName + ".t_version.get());\n";
				break;
			}
		}
		for(PropertyAndHandlers prop : props){
			PropMonitor propMonitor = propMonitors.get(prop);
			ret += propMonitor.localDeclaration;
			ret += propMonitor.initilization;
			ret += "\n";
		}
		if (Main.statistics) {
			ret += stat.incNumMonitor();
		}
		ret += "}\n";
		ret += "\n";

		// events
		for(PropertyAndHandlers prop : props){
			for (EventDefinition event : this.events) {
				ret += this.printEventMethod(prop, event) + "\n";
			}
		}

		// handlers
		for(PropertyAndHandlers prop : props){
			PropMonitor propMonitor = propMonitors.get(prop);
			for (HandlerMethod handlerMethod : propMonitor.handlerMethods.values()) {
				ret += handlerMethod + "\n";
			}
		}

		// reset
		ret += "final void reset() {\n";
		if (monitorInfo != null)
			ret += monitorInfo.initConnected();
		for(PropertyAndHandlers prop : props){
			if (prop.getVersionedStack()) {
				RVMVariable global_depth = new RVMVariable("global_depth");
				RVMVariable version = new RVMVariable("version");
	
				ret += "int[] " + global_depth + " = (int[])(" + systemAspectName + ".t_global_depth.get());\n";
				ret += "int[] " + version + " = (int[])(" + systemAspectName + ".t_version.get());\n";
			}
		}
		if (isOutermost) {
			ret += lastevent + " = -1;\n";
		}
		for(PropertyAndHandlers prop : props){
			PropMonitor propMonitor = propMonitors.get(prop);

			ret += propMonitor.localDeclaration;
			ret += propMonitor.resetCode;
			for (String category : propMonitor.categoryVars.keySet()) {
				ret += propMonitor.categoryVars.get(category) + " = false;\n";
			}
		}
		ret += "}\n";
		ret += "\n";

		// hashcode
		if(props.size() > 1){
			boolean newHashCode = false;
			for(PropertyAndHandlers prop : props){
				PropMonitor propMonitor = propMonitors.get(prop);
				if (!propMonitor.hashcodeCode.isEmpty()) {
					newHashCode = true;
					
					propMonitor.hashcodeMethod = new RVMVariable("Prop_" + prop.getPropertyId() + "_hashCode");
					
					ret += "final int " + propMonitor.hashcodeMethod + "() {\n";
					ret += propMonitor.hashcodeCode;
					ret += "}\n";
				}
			}
			if(newHashCode){
				ret += "public final int hashCode() {\n";
				ret += "return ";
				boolean first = true;
				for(PropertyAndHandlers prop : props){
					PropMonitor propMonitor = propMonitors.get(prop);
					if(propMonitor.hashcodeMethod != null){
						if(first){
							first = false;
						} else {
							ret += "^";
						}
						
						ret += propMonitor.hashcodeMethod + "()";
					}
				}
				ret += ";\n";
				ret += "}\n";
				ret += "\n";
			}
		} else if(props.size() == 1){
			for(PropertyAndHandlers prop : props){
				PropMonitor propMonitor = propMonitors.get(prop);
				if (!propMonitor.hashcodeCode.isEmpty()) {
					
					ret += "public final int hashCode() {\n";
					ret += propMonitor.hashcodeCode;
					ret += "}\n";
					ret += "\n";
				}
			}
		}

		// equals
		// if there are more than 1 property, there is no state collapsing.
		if(props.size() == 1){
			for(PropertyAndHandlers prop : props){
				PropMonitor propMonitor = propMonitors.get(prop);
				if (!propMonitor.equalsCode.isEmpty()) {
					ret += "public final boolean equals(Object o) {\n";
					ret += propMonitor.equalsCode;
					ret += "}\n";
					ret += "\n";
				}
			}
		}

		// Other declarations/methods for subclasses
		ret += this.printExtraDeclMethods();
		
		// endObject and some declarations
		if (isOutermost) {
			ret += monitorTermination;
		}

		if (monitorInfo != null)
			ret += monitorInfo.monitorDecl();

		ret += "}\n";

		if (has__ACTIVITY) {
			ret = ret.replaceAll("__ACTIVITY", "this." + activity);
		}

		return ret;
	}

	/***
	 * 
	 * Extra methods could be defined in subclasses.
	 * 
	 * @return
	 */
	public String printExtraDeclMethods() {
		return "";
	}

	static Map<RVMVariable,RVMVariable> niceVars =
			new HashMap<RVMVariable, RVMVariable>();
	public static RVMVariable getNiceVariable(RVMVariable var) {
		RVMVariable result = niceVars.get(var);
		if (result != null) return result;
		String v = var.getVarName();
		if (v.contains("_Category_")) {
			String[] parts = v.split("_Category_", 2);
			parts[1] = parts[1].replaceAll("_","");
			parts[0] = parts[0].replaceAll("_","");
			parts[0] = Character.toUpperCase(parts[0].charAt(0)) +
					parts[0].substring(1);
			v = parts[1] + parts[0];
		} else {
			String[] parts = v.split("_");
			v = parts[0];
			for (int i = 1; i < parts.length; i++) {
				v += Character.toUpperCase(parts[0].charAt(0)) +
						parts[0].substring(1);
			}
		}

		result = new RVMVariable(v);
		niceVars.put(var, result);
		return  result;
	}
}
