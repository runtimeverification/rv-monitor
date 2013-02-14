package rvmonitor.output;

import rvmonitor.MOPException;
import rvmonitor.output.combinedaspect.CombinedAspect;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.MOPSpecFile;
import rvmonitor.parser.ast.mopspec.JavaMOPSpec;
import rvmonitor.parser.ast.mopspec.PropertyAndHandlers;

import java.util.HashMap;

public class AspectJCode {
	String name;
	
	Package packageDecl;
	Imports imports;
	HashMap<JavaMOPSpec, MonitorSet> monitorSets = new HashMap<JavaMOPSpec, MonitorSet>();
	HashMap<JavaMOPSpec, SuffixMonitor> monitors = new HashMap<JavaMOPSpec, SuffixMonitor>();
	//Aspect aspect;
	CombinedAspect aspect;
	HashMap<JavaMOPSpec, EnableSet> enableSets = new HashMap<JavaMOPSpec, EnableSet>();
	HashMap<JavaMOPSpec, CoEnableSet> coenableSets = new HashMap<JavaMOPSpec, CoEnableSet>();
	boolean versionedStack = false;
	SystemAspect systemAspect;
	
	public AspectJCode(String name, MOPSpecFile mopSpecFile) throws MOPException {
		this.name = name;
		packageDecl = new Package(mopSpecFile);
		imports = new Imports(mopSpecFile);

		for (JavaMOPSpec mopSpec : mopSpecFile.getSpecs()) {
			EnableSet enableSet = new EnableSet(mopSpec.getEvents(), mopSpec.getParameters());
			CoEnableSet coenableSet = new CoEnableSet(mopSpec.getEvents(), mopSpec.getParameters());

			for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
				enableSet.add(new EnableSet(prop, mopSpec.getEvents(), mopSpec.getParameters()));
				coenableSet.add(new CoEnableSet(prop, mopSpec.getEvents(), mopSpec.getParameters()));
				versionedStack |= prop.getVersionedStack();
			}

			OptimizedCoenableSet optimizedCoenableSet = new OptimizedCoenableSet(coenableSet);

			enableSets.put(mopSpec, enableSet);
			coenableSets.put(mopSpec, optimizedCoenableSet);

			SuffixMonitor monitor = new SuffixMonitor(name, mopSpec, optimizedCoenableSet, true);

			monitors.put(mopSpec, monitor);

			monitorSets.put(mopSpec, new MonitorSet(name, mopSpec, monitor));

		}

		//aspect = new Aspect(name, mopSpecFile, monitorSets, monitors, enableSets, versionedStack);
		aspect = new CombinedAspect(name, mopSpecFile, monitorSets, monitors, enableSets, versionedStack);

		// Set monitor lock for each monitor set
		for (MonitorSet monitorSet : monitorSets.values()) {
			monitorSet.setMonitorLock(aspect.getAspectName() + "." + aspect.lockManager.getLock().getName());
		}

		HashMap<String, RefTree> refTrees = aspect.indexingTreeManager.refTrees;

		for(SuffixMonitor monitor : monitors.values()){
			monitor.setRefTrees(refTrees);
		}
		
		if(versionedStack)
			systemAspect = new SystemAspect(name);
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


		// The order of these two is really important.
		if(systemAspect != null){
			ret += "aspect " + name + "OrderAspect {\n";
			ret += "declare precedence : ";
			ret += systemAspect.getSystemAspectName() + ""; 
			ret += ", ";
			ret += systemAspect.getSystemAspectName() + "2";
			ret += ", ";
			ret += aspect.getAspectName();
			ret += ";\n";
			
			ret += "}\n";
			ret += "\n";
		}

		ret += aspect;

		if(systemAspect != null)
			ret += "\n" + systemAspect;

		return ret;
	}
}
