package com.runtimeverification.rvmonitor.java.rvj.output.monitor;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.OptimizedCoenableSet;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodePhantomStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeRVType;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BlockStmt;

import java.util.*;

public class SuffixMonitor extends Monitor {
	RVMVariable activity = new RVMVariable("RVM_activity");
	RVMVariable loc = new RVMVariable("RVM_loc");
	RVMVariable staticsig = new RVMVariable("RVM_staticsig");
	RVMVariable lastevent = new RVMVariable("RVM_lastevent");
	RVMVariable thisJoinPoint = new RVMVariable("thisJoinPoint");

	List<EventDefinition> events;

	Monitor innerMonitor = null;

	ArrayList<String> categories;
	RVMVariable monitorList = new RVMVariable("monitorList");
	boolean existSkip = false;
	String aspectName;
	
	@Override
	public MonitorFeatures getFeatures() {
		// The inner monitor thing looks quirky to me, but, to deal with the quirkiness,
		// it forwards the request.
		return this.innerMonitor.getFeatures();
	}
	
	public SuffixMonitor(String name, RVMonitorSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost) throws RVMException {
		super(name, mopSpec, coenableSet, isOutermost);

		this.isDefined = mopSpec.isSuffixMatching();

		if (this.isDefined) {
			monitorName = new RVMVariable(mopSpec.getName() + "SuffixMonitor");

			if (isOutermost) {
				varInOutermostMonitor = new VarInOutermostMonitor(name, mopSpec, mopSpec.getEvents());
				monitorTermination = new MonitorTermination(name, mopSpec, mopSpec.getEvents(), coenableSet);
			}
			
			if (mopSpec.isEnforce())
			{
				// TODO Do we need raw monitor for enforcing properties?
				innerMonitor = new EnforceMonitor(name, mopSpec, coenableSet, false);
				for (PropertyAndHandlers p : mopSpec.getPropertiesAndHandlers()) {
					int totalHandlers = p.getHandlers().size();
					if (p.getHandlers().containsKey("deadlock"))
						totalHandlers--;
					// We only allow one handler (except deadlock handler) when enforcing a property
					if (totalHandlers > 1)
						throw new RVMException("Only one handler (except deadlock handler) is allowed when enforcing a property");
				}

			}
			else
			{
				if (mopSpec.getPropertiesAndHandlers().size() == 0)
					innerMonitor = new RawMonitor(name, mopSpec, coenableSet, false);
				else		
					innerMonitor = new BaseMonitor(name, mopSpec, coenableSet, false);
			}
			events = mopSpec.getEvents();
			
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
				if (event.has__SKIP()){
					existSkip = true;
					break;
				}
			}
		} else {
			if (mopSpec.isEnforce())
			{
				// TODO Do we need raw monitor for enforcing properties?
				innerMonitor = new EnforceMonitor(name, mopSpec, coenableSet, isOutermost);
				for (PropertyAndHandlers p : mopSpec.getPropertiesAndHandlers()) {
					int totalHandlers = p.getHandlers().size();
					if (p.getHandlers().containsKey("deadlock"))
						totalHandlers--;
					// We only allow one handler (except deadlock handler) when enforcing a property
					if (totalHandlers > 1)
						throw new RVMException("Only one handler (except deadlock handler) is allowed when enforcing a property");
				}
			}
			else
			{
				if (mopSpec.getPropertiesAndHandlers().size() == 0)
					innerMonitor = new RawMonitor(name, mopSpec, coenableSet, isOutermost);
				else		
					innerMonitor = new BaseMonitor(name, mopSpec, coenableSet, isOutermost);
			}
		}

		if (this.isDefined && mopSpec.isGeneral()){
			if(mopSpec.isFullBinding() || mopSpec.isConnected())
				monitorInfo = new MonitorInfo(mopSpec);
		}
	}
	
	public void setRefTrees(TreeMap<String, RefTree> refTrees){
		this.refTrees = refTrees;
		innerMonitor.setRefTrees(refTrees);
		
		if(monitorTermination != null)
			monitorTermination.setRefTrees(refTrees);
	}
	
	public void setAspectName(String name) {
		this.aspectName = name;
	}
	
	public String getAspectName() {
		return this.aspectName;
	}

	public RVMVariable getOutermostName() {
		if (isDefined)
			return monitorName;
		else
			return innerMonitor.getOutermostName();
	}

	public Set<String> getNames() {
		Set<String> ret = innerMonitor.getNames();
		if (isDefined)
			ret.add(monitorName.toString());
		return ret;
	}

	public Set<RVMVariable> getCategoryVars() {
		return innerMonitor.getCategoryVars();
	}

	public String doEvent(EventDefinition event) {
		String synch = Main.useFineGrainedLock ? " synchronized " : " ";
		String ret = "";

		int idnum = event.getIdNum();

		RVMVariable monitor = new RVMVariable("monitor");
		RVMVariable monitorSet = new RVMVariable("monitorSet");
		RVMVariable newMonitor = new RVMVariable("newMonitor");
		RVMVariable it = new RVMVariable("it");
		HashSet<RVMVariable> categoryVars = new HashSet<RVMVariable>();

		categoryVars.addAll(innerMonitor.getCategoryVars());

		ret += "final" + synch + "void event_" + event.getId() + "(";
		{
			RVMParameters params;
			if (Main.stripUnusedParameterInMonitor)
				params = event.getReferredParameters(event.getRVMParameters());
			else
				params = event.getRVMParameters();
			ret += params.parameterDeclString();
		}
		ret += ") {\n";

		for (RVMVariable var : getCategoryVars()) {
			ret += BaseMonitor.getNiceVariable(var) + " = false;\n";
		}

		if (isOutermost) {
			ret += lastevent + " = " + idnum + ";\n";
		}

		ret += "HashSet " + monitorSet + " = new HashSet();\n";

		if (event.isStartEvent()) {
			ret += innerMonitor.getOutermostName() + " " + newMonitor + " = new " + innerMonitor.getOutermostName() + "();\n";
			if (monitorInfo != null){
				ret += monitorInfo.copy(newMonitor);
			}
			ret += monitorList + ".add(" + newMonitor + ");\n";
		}

		ret += "Iterator " + it + " = " + monitorList + ".iterator();\n";
		ret += "while (" + it + ".hasNext()){\n";
		ret += innerMonitor.getOutermostName() + " " + monitor + " = (" + innerMonitor.getOutermostName() + ")" + it + ".next();\n";

		ret += innerMonitor.Monitoring(monitor, event, loc, staticsig, null, this.aspectName, false);

		ret += "if(" + monitorSet + ".contains(" + monitor + ")";
		for (RVMVariable categoryVar : categoryVars) {
			ret += " || " + monitor + "." + categoryVar;
		}
		ret += " ) {\n";
		ret += it + ".remove();\n";
		ret += "} else {\n";
		ret += monitorSet + ".add(" + monitor + ");\n";
		ret += "}\n";

		ret += "}\n";

		ret += "}\n";

		return ret;
	}

	public String Monitoring(RVMVariable monitorVar, EventDefinition event, RVMVariable loc, RVMVariable staticsig, GlobalLock l, String aspectName, boolean inMonitorSet) {
		String ret = "";

		if (!isDefined)
			return innerMonitor.Monitoring(monitorVar, event, loc, staticsig, l, aspectName, inMonitorSet);

//		if (has__LOC) {
//			if(loc != null)
//				ret += monitorVar + "." + this.loc + " = " + loc + ";\n";
//			else
//				ret += monitorVar + "." + this.loc + " = " +
//						"Thread.currentThread().getStackTrace()[2].toString()"
//						+ ";\n";
//			else
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
		{
			RVMParameters passing;
			if (Main.stripUnusedParameterInMonitor)
				passing = event.getReferredParameters(event.getRVMParameters());
			else
				passing = event.getRVMParameters();
			ret += passing.parameterString();
		}
		ret += ");\n";

		if (!Main.eliminatePresumablyRemnantCode) {
			for (RVMVariable var : getCategoryVars()) {
				ret += BaseMonitor.getNiceVariable(var) + " |= " +
						monitorVar + "." + BaseMonitor.getNiceVariable(var) + ";" +
						"\n";
			}
		}
		
		if (existSkip) {
			ret += BaseMonitor.skipEvent + " |= " +
					monitorVar + "." + BaseMonitor.skipEvent + ";\n";
			ret += monitorVar + "." + BaseMonitor.skipEvent + " = false;\n";
		}
		if (this.hasThisJoinPoint) {
			ret += monitorVar + "." + this.thisJoinPoint + " = null;\n";
		}

		return ret;
	}

	public MonitorInfo getMonitorInfo(){
		if (isDefined)
			return monitorInfo;
		else
			return innerMonitor.getMonitorInfo();
		
	}

	public String toString() {
		String ret = "";

		RVMVariable monitor = new RVMVariable("monitor");
		RVMVariable newMonitor = new RVMVariable("newMonitor");

		if (isDefined) {
			ret += "class " + monitorName;
			if (isOutermost)
				ret += " extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor";
			ret += " implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {\n";

			for (RVMVariable var : getCategoryVars()) {
			ret += "boolean " + BaseMonitor.getNiceVariable(var) + ";\n";
			}

			if(varInOutermostMonitor != null)
				ret += varInOutermostMonitor;
			
			ret += "Vector<" + innerMonitor.getOutermostName() + "> " + monitorList + " = new Vector<" + innerMonitor.getOutermostName() + ">();\n";

			if (this.has__ACTIVITY)
				ret += activityCode();
//			if (this.has__LOC)
//				ret += "String " + loc + ";\n";
			if (this.has__STATICSIG)
				ret += "org.aspectj.lang.Signature " + staticsig + ";\n";
			if (this.hasThisJoinPoint)
				ret += "org.aspectj.lang.JoinPoint " + thisJoinPoint + " = null;\n";
			if (existSkip)
				ret += "boolean " + BaseMonitor.skipEvent + " = false;\n";

			// clone()
			ret += "protected Object clone() {\n";
			ret += "try {\n";
			ret += monitorName + " ret = (" + monitorName + ") super.clone();\n";
			if (monitorInfo != null)
				ret += monitorInfo.copy("ret", "this");
			ret += "ret." + monitorList + " = new Vector<" + innerMonitor.getOutermostName() + ">();\n";
			ret += "for(" + innerMonitor.getOutermostName() + " " + monitor + " : this." + monitorList + "){\n";
			ret += innerMonitor.getOutermostName() + " " + newMonitor + " = ";
			ret += "(" + innerMonitor.getOutermostName() + ")" + monitor + ".clone()" + ";\n";
			if (monitorInfo != null)
				ret += monitorInfo.copy(newMonitor, monitor);
			ret += "ret." + monitorList + ".add(" + newMonitor + ");\n";
			ret += "}\n";
			ret += "return ret;\n";
			ret += "}\n";
			ret += "catch (CloneNotSupportedException e) {\n";
			ret += "throw new InternalError(e.toString());\n";
			ret += "}\n";
			ret += "}\n";
			ret += "\n";
			
			// implements getState()
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

			// endObject and some declarations
			if (isOutermost && monitorTermination != null) {
				ret += monitorTermination.getCode(this.getFeatures());
			}

			if (monitorInfo != null){
				ret += monitorInfo.monitorDecl();
			}

			ret += "}\n";
			ret += "\n";
		}

		ret += this.innerMonitor;

		return ret;
	}

	public CodeStmtCollection generateMonitorTransitionedCode(CodeVarRefExpr affectedref, EventDefinition event, GlobalLock enforcelock) {
		CodeStmtCollection stmts = new CodeStmtCollection();
		
		// Modernizing the monitoring code takes too much effort. Maybe later.
		RVMVariable monitorvar = affectedref.getVariable().toLegacy();
		String mntcode = this.Monitoring(monitorvar, event, null, null, enforcelock, this.getAspectName(), false);
		stmts.add(CodeStmtCollection.fromLegacy(mntcode));

		// The referred variable is marked so that the dead-code elimination step
		// won't remove the definition of the variable.
		stmts.add(new CodePhantomStmt(affectedref.getVariable()));
		return stmts;
	}
	
	public CodeRVType.Monitor getRuntimeType() {
		CodeType type = new CodeType(this.getOutermostName().toString());
		return CodeRVType.forMonitor(type);
	}
}
