package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface ISetOperation<TWeakRef, TSet> {
	public TSet getSet(TWeakRef key);
	public void putSet(TWeakRef key, TSet value);
}
