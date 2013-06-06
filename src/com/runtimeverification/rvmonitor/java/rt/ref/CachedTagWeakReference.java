package com.runtimeverification.rvmonitor.java.rt.ref;


public class CachedTagWeakReference extends CachedWeakReference {
  	private long disabled = -1;
	private long tau = -1;
	
	public long isDiabled() {
		return this.disabled;
	}
	
	public void setDisabled(long d) {
		this.disabled = d;
	}
	
	public long getTau() {
		return this.tau;
	}
	
	public void setTau(long t) {
		this.tau = t;
	}
	
	public CachedTagWeakReference(Object ref, int hashval) {
		super(ref, hashval);
	}
}