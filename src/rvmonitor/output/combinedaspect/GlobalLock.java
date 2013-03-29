package rvmonitor.output.combinedaspect;

import rvmonitor.output.RVMVariable;

public class GlobalLock {
	RVMVariable lock;

	public GlobalLock(RVMVariable lock) {
		this.lock = lock;
	}

	public RVMVariable getName(){
		return lock;
	}
	
	public String toString() {
		String ret = "";

		ret += "private static ReentrantLock " + lock + " = new ReentrantLock();\n";
		ret += "private static Condition " + lock + "_cond = " + lock + ".newCondition();\n";

		return ret;
	}

}
