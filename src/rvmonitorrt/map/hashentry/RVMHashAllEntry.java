package rvmonitorrt.map.hashentry;

import rvmonitorrt.ref.RVMWeakReference;

public class RVMHashAllEntry {
	public RVMHashAllEntry next;
	
	public RVMWeakReference key;
	
	public Object node = null;
	public Object set = null;
	public Object map = null;

	public RVMHashAllEntry(RVMHashAllEntry next, RVMWeakReference keyref) {
		this.next = next;
		this.key = keyref;
	}
}
