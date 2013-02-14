package rvmonitor.output.combinedaspect;

import rvmonitor.output.MOPVariable;

public class GlobalLock {
	MOPVariable lock;

	public GlobalLock(MOPVariable lock) {
		this.lock = lock;
	}

	public MOPVariable getName(){
		return lock;
	}
	
	public String toString() {
		String ret = "";

		ret += "private static ReentrantLock " + lock + " = new ReentrantLock();\n";
		ret += "private static Condition " + lock + "_cond = " + lock + ".newCondition();\n";

		return ret;
	}

}
