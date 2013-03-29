package rvmonitorrt.map.hashentry;

import rvmonitorrt.ref.RVMWeakReference;

public class RVMHashEntry {
	public RVMHashEntry next;
	public RVMWeakReference key;
	public Object value;

	public RVMHashEntry(RVMHashEntry next, RVMWeakReference keyref) {
		this.next = next;
		this.key = keyref;
		this.value = null;
	}

	public RVMHashEntry(RVMHashEntry next, RVMWeakReference keyref, Object value) {
		this.next = next;
		this.key = keyref;
		this.value = value;
	}

}
