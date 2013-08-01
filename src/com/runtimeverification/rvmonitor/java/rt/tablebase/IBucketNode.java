package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface IBucketNode<TWeakRef, TValue> {
	public TWeakRef getKey();
	public TValue getValue();
}
