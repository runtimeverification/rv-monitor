package com.runtimeverification.rvmonitor.java.rt.ref;

import java.lang.ref.WeakReference;

public class CachedWeakReference extends WeakReference<Object> {
	private final int hashval;
	
	public CachedWeakReference(Object ref, int hashval) {
		super(ref);
		this.hashval = hashval;
	}
	
	@Override
	public int hashCode() {
		return this.hashval;
	}
}