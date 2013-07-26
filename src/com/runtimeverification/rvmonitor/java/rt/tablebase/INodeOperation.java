package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface INodeOperation<TWeakRef, TValue> {
	public IBucketNode<TWeakRef, TValue> getNodeWithStrongRef(Object key);
}
