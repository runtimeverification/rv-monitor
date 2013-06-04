// Handle the creation of java code to be used as a library.
// Mostly modified from AspectJCode.java

package com.runtimeverification.rvmonitor.java.rvj.output;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;

import java.util.HashMap;

public class JavaLibCode {
	Package packageDecl;
	Imports imports;
	HashMap<RVMonitorSpec, MonitorSet> monitorSets = new HashMap<RVMonitorSpec, MonitorSet>();
	HashMap<RVMonitorSpec, SuffixMonitor> monitors = new HashMap<RVMonitorSpec, SuffixMonitor>();
	HashMap<RVMonitorSpec, EnableSet> enableSets = new HashMap<RVMonitorSpec, EnableSet>();
	HashMap<RVMonitorSpec, CoEnableSet> coenableSets = new HashMap<RVMonitorSpec, CoEnableSet>();

	public JavaLibCode(String name, RVMSpecFile rvmSpecFile) throws RVMException {
		packageDecl = new Package(rvmSpecFile);
		imports = new Imports(rvmSpecFile);

		for (RVMonitorSpec mopSpec : rvmSpecFile.getSpecs()) {
			EnableSet enableSet = new EnableSet(mopSpec.getEvents(), mopSpec.getParameters());
			CoEnableSet coenableSet = new CoEnableSet(mopSpec.getEvents(), mopSpec.getParameters());

			for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
				enableSet.add(new EnableSet(prop, mopSpec.getEvents(), mopSpec.getParameters()));
				coenableSet.add(new CoEnableSet(prop, mopSpec.getEvents(), mopSpec.getParameters()));
			}

			OptimizedCoenableSet optimizedCoenableSet = new OptimizedCoenableSet(coenableSet);

			enableSets.put(mopSpec, enableSet);
			coenableSets.put(mopSpec, optimizedCoenableSet);

			SuffixMonitor monitor = new SuffixMonitor(name, mopSpec, optimizedCoenableSet, true);

			monitors.put(mopSpec, monitor);

			monitorSets.put(mopSpec, new MonitorSet(name, mopSpec, monitor));

		}
	}

	public String toString() {
		String ret = "";

		ret += packageDecl;
		ret += imports;
		ret += "\n";

		for (MonitorSet monitorSet : this.monitorSets.values())
			ret += monitorSet;
		ret += "\n";

		for (SuffixMonitor monitor : this.monitors.values())
			ret += monitor;
		ret += "\n";

		return ret;
	}
}
