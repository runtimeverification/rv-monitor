package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface ILeafOperation<TWeakRef, TLeaf> {
	public TLeaf getLeaf(TWeakRef key);
	public void putLeaf(TWeakRef key, TLeaf value);
}
