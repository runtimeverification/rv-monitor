package rvmonitorrt.map.hashentry;

import rvmonitorrt.ref.RVMWeakReference;

public class RVMHashDualEntry {
	public RVMHashDualEntry next;
	
	public RVMWeakReference key;
	
	public Object value1 = null;
	public Object value2 = null;

	public RVMHashDualEntry(RVMHashDualEntry next, RVMWeakReference keyref) {
		this.next = next;
		this.key = keyref;
	}

}
