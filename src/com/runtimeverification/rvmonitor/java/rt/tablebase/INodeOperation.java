package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface INodeOperation<TWeakRef, TValue> {
	public TValue getNode(TWeakRef key);
	public TValue getNodeWithStrongRef(Object key);
	public void putNode(TWeakRef key, TValue value);
}
