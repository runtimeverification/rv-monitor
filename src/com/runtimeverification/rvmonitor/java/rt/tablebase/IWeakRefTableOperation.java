package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface IWeakRefTableOperation<TWeakRef> {
	public TWeakRef findWeakRef(Object key);
	public TWeakRef findOrCreateWeakRef(Object key);
}
