package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface ILeafOperation<TWeakRef, TNode> {
	public TNode getLeaf(TWeakRef key);
	public void putLeaf(TWeakRef key, TNode value);
}
