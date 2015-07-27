package com.runtimeverification.rvmonitor.java.rvj.output;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.CombinedOutput;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.MonitorFeatures;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.rvmspec.PropertyAndHandlers;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.rvmspec.RVMonitorSpec;

import java.util.TreeMap;

public class RVMOutputCode {
	private final String name;
	
	private final Package packageDecl;
	private final Imports imports;
	private final TreeMap<RVMonitorSpec, MonitorSet> monitorSets = new TreeMap<RVMonitorSpec, MonitorSet>();
	private final TreeMap<RVMonitorSpec, SuffixMonitor> monitors = new TreeMap<RVMonitorSpec, SuffixMonitor>();
	private final CombinedOutput output;
	private final TreeMap<RVMonitorSpec, EnableSet> enableSets = new TreeMap<RVMonitorSpec, EnableSet>();
	private final TreeMap<RVMonitorSpec, CoEnableSet> coenableSets = new TreeMap<RVMonitorSpec, CoEnableSet>();
	private boolean isCodeGenerated = false;
	
	public RVMOutputCode(String name, RVMSpecFile rvmSpecFile) throws RVMException {
		this.name = name;
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

			SuffixMonitor monitor = new SuffixMonitor(mopSpec.getName(), mopSpec, optimizedCoenableSet, true);

			monitors.put(mopSpec, monitor);

			monitorSets.put(mopSpec, new MonitorSet(mopSpec.getName(), mopSpec, monitor));
		}

		output = new CombinedOutput(name, rvmSpecFile, monitorSets, monitors, enableSets);

		// Set monitor lock for each monitor set
		for (MonitorSet monitorSet : monitorSets.values()) {
			monitorSet.setMonitorLock(output.getName() + "." + output.lockManager.getLock().getName());
			monitorSet.setIndexingTreeManager(output.indexingTreeManager);
		}

		TreeMap<String, RefTree> refTrees = output.indexingTreeManager.refTrees;

		for(SuffixMonitor monitor : monitors.values()){
			monitor.setRefTrees(refTrees);
		}
	}
	
	public void generateCode() {
		if (!this.isCodeGenerated) {
			this.output.generateCode();
		}
		
		this.postprocessMonitorFeatures();
	
		this.isCodeGenerated = true;
	}

	private void postprocessMonitorFeatures() {
		for (SuffixMonitor monitor : this.monitors.values()) {
			MonitorFeatures features = monitor.getFeatures();
			features.onCodeGenerationPass1Completed();
		}
	}

	public String toString() {
		// Some non-trivial and potentially side-effect generating works should be done.
		// Since toString() is not a very nice place to do such things, I created a
		// method and let is invoked. I hope such changes are made in many other places.
		this.generateCode();

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

		ret += output;

		return ret;
	}
}
