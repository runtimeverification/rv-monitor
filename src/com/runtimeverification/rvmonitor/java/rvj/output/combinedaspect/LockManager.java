package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect;

import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;

public class LockManager {

	//HashMap<RVMonitorSpec, GlobalLock> locks = new HashMap<RVMonitorSpec, GlobalLock>();
	
	GlobalLock lock;

	public LockManager(String name, List<RVMonitorSpec> specs) throws RVMException {
//		for (RVMonitorSpec spec : specs) {
//			if (spec.isSync())
//				locks.put(spec, new GlobalLock(new RVMVariable(spec.getName() + "_RVMLock")));
//		}
		
		lock = new GlobalLock(new RVMVariable(name + "_RVMLock"));
	}

/*	public GlobalLock getLock(RVMonitorSpec spec){
		return locks.get(spec);
	}
*/
	public GlobalLock getLock(){
		return lock;
	}

	public String decl() {
		String ret = "";

/*		if (locks.size() <= 0)
			return ret;
*/
/*		ret += "// Declarations for Locks \n";
		for (GlobalLock lock : locks.values()) {
			ret += lock;
		}
		ret += "\n";
*/
		ret += "// Declarations for the Lock \n";
		ret += lock;
		ret += "\n";
		
		return ret;
	}

}
