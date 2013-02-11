package rvmonitorrt.map.hashentry;

import rvmonitorrt.ref.MOPWeakReference;

public class MOPHashAllEntry{
	public MOPHashAllEntry next;
	
	public MOPWeakReference key;
	
	public Object node = null;
	public Object set = null;
	public Object map = null;

	public MOPHashAllEntry(MOPHashAllEntry next, MOPWeakReference keyref) {
		this.next = next;
		this.key = keyref;
	}
}
