package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;

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

		ret += "static ReentrantLock " + lock + " = new ReentrantLock();\n";
		ret += "static Condition " + lock + "_cond = " + lock + ".newCondition();\n";

		return ret;
	}

}
