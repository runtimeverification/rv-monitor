package rvmonitorrt.map.hashentry;

import rvmonitorrt.ref.MOPWeakReference;

public class MOPHashRefEntry {
	public MOPHashRefEntry next;
	public MOPWeakReference key;

	public MOPHashRefEntry(MOPHashRefEntry next, MOPWeakReference keyref) {
		this.next = next;
		this.key = keyref;
	}

}