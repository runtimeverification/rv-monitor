package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;

import java.util.HashMap;
import java.util.List;

public class TimestampManager {

	HashMap<RVMonitorSpec, RVMVariable> timestamps = new HashMap<RVMonitorSpec, RVMVariable>();

	public TimestampManager(String name, List<RVMonitorSpec> specs) throws RVMException {
		for (RVMonitorSpec spec : specs) {
			if (spec.isGeneral())
				timestamps.put(spec, new RVMVariable(spec.getName() + "_timestamp"));
		}
	}
	
	public RVMVariable getTimestamp(RVMonitorSpec spec){
		return timestamps.get(spec);
	}

	public String decl() {
		String ret = "";

		if (timestamps.size() <= 0)
			return ret;

		ret += "// Declarations for Timestamps \n";
		for (RVMVariable timestamp : timestamps.values()) {
			ret += "private static long " + timestamp + " = 1;\n";
		}
		ret += "\n";

		return ret;
	}
	
	public String reset() {
		String ret = "";

		if (timestamps.size() <= 0)
			return ret;

		for (RVMVariable timestamp : timestamps.values()) {
			ret += timestamp + " = 1;\n";
		}
		ret += "\n";

		return ret;
	}


}
