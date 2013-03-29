package rvmonitor.output.combinedaspect;

import rvmonitor.output.RVMVariable;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;

import java.util.List;
import java.util.TreeMap;

public class ActivatorManager {

	List<RVMonitorSpec> specs;
	TreeMap<RVMonitorSpec, RVMVariable> activators = new TreeMap<RVMonitorSpec, RVMVariable>();

	public ActivatorManager(String name, List<RVMonitorSpec> specs) {
		this.specs = specs;
		for (RVMonitorSpec spec : specs) {
			activators.put(spec, new RVMVariable(spec.getName() + "_activated"));
		}
	}

	public RVMVariable getActivator(RVMonitorSpec spec) {
		return activators.get(spec);
	}

	public String decl() {
		String ret = "";

		for (RVMVariable activator : activators.values()) {
			ret += "private static boolean " + activator + " = false;\n";
		}

		if (activators.size() > 0)
			ret += "\n";

		return ret;
	}

	public String reset() {
		String ret = "";

		for (RVMVariable activator : activators.values()) {
			ret += activator + " = false;\n";
		}

		if (activators.size() > 0)
			ret += "\n";

		return ret;
	}

}
