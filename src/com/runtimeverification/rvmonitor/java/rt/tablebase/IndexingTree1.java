package com.runtimeverification.rvmonitor.java.rt.tablebase;

import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;

public abstract class IndexingTree1<TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> extends AbstractIndexingTree<TWeakRef, TValue> {
	protected IndexingTree1(int treeid) {
		super(null, treeid);
	}
	
	protected final TValue get1(TWeakRef key) {
		BucketNode<TWeakRef, TValue> node = this.getNode(key);
		if (node == null) return null;
		return node.getValue();
	}
	
	protected final void put1(TWeakRef key, TValue value) {
		this.putNodeUnconditional(key, value);
	}
}