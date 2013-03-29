package rvmonitor.output.monitor;

import rvmonitor.RVMException;
import rvmonitor.output.RVMVariable;
import rvmonitor.output.OptimizedCoenableSet;
import rvmonitor.output.Util;
import rvmonitor.output.combinedaspect.GlobalLock;
import rvmonitor.output.combinedaspect.RVMonitorStatistics;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMParameter;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.mopspec.PropertyAndHandlers;
import rvmonitor.parser.ast.stmt.BlockStmt;

import java.util.HashMap;
import java.util.Set;

public abstract class Monitor {
	RVMVariable monitorName;

	boolean isDefined;
	boolean isOutermost;

	boolean has__LOC;
	boolean has__ACTIVITY;
	boolean has__STATICSIG;
	boolean has__SKIP;
	boolean handlersHave__SKIP;
	boolean hasThisJoinPoint;

	String defaultMessage =  "\"Specification ";

	public String getDefaultMessage() {
		return defaultMessage;
	}

	OptimizedCoenableSet coenableSet;

	MonitorTermination monitorTermination = null;
	MonitorInfo monitorInfo = null;

	RVMonitorStatistics stat;

	VarInOutermostMonitor varInOutermostMonitor = null;

	HashMap<String, RVMVariable> mopRefs = new HashMap<String, RVMVariable>();

	HashMap<String, RefTree> refTrees;

	public Monitor(String name, RVMonitorSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost) throws RVMException {
		this.isOutermost = isOutermost;

		this.has__LOC = mopSpec.has__LOC();
		this.has__ACTIVITY = mopSpec.has__ACTIVITY();
		this.has__STATICSIG = mopSpec.has__STATICSIG();
		this.has__SKIP = mopSpec.has__SKIP();
		this.hasThisJoinPoint = mopSpec.hasThisJoinPoint();

		this.handlersHave__SKIP = false;
		for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
			for (BlockStmt handler : prop.getHandlers().values()) {
				if (handler.toString().indexOf("__SKIP") != -1){
					this.handlersHave__SKIP = true;
				}
			}
		}

		this.coenableSet = coenableSet;

		this.stat = new RVMonitorStatistics(name, mopSpec);

		this.defaultMessage += name + " has been violated on line \" + "
				+ "__LOC" +" + \". Documentation for this property can be found at "
				+ Util.packageAndNameToUrl(mopSpec.getPackage(), name) + "\"";

		for (RVMParameter p : mopSpec.getParameters()) {
			mopRefs.put(p.getName(), new RVMVariable("RVMRef_" + p.getName()));
		}

	}

	public RVMVariable getRVMonitorRef(RVMParameter p){
		return mopRefs.get(p.getName());
	}

	RVMVariable activity = new RVMVariable("RVM_activity");
	String activityCode() {
		String ret = "";
		ret += "static android.app.Activity " + activity + ";\n";
		//activity
		ret += "static void setActivity(android.app.Activity a) {\n";
		ret += this.activity + " = a;\n";
		ret += "}\n";
		ret += "\n";
		return ret;
	}

	public boolean has__ACTIVITY() {
		return has__ACTIVITY;
	}

	public RVMVariable getActivityName() {
		return activity;
	}

	public abstract void setRefTrees(HashMap<String, RefTree> refTrees);

	public abstract RVMVariable getOutermostName();

	public abstract Set<String> getNames();

	public abstract Set<RVMVariable> getCategoryVars();

	public abstract String Monitoring(RVMVariable monitorVar, EventDefinition event, RVMVariable loc, RVMVariable staticsig, GlobalLock l, String aspectName, boolean inMonitorSet);

	public abstract MonitorInfo getMonitorInfo();

	public abstract String toString();
}
