package com.runtimeverification.rvmonitor.java.rt.tablebase;

import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;

abstract class AbstractIndexingTree<TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> extends WeakRefHashTable<TWeakRef, TValue> implements IIndexingTree {
	private final int treeid;
	
	@Override
	public int getTreeId() {
		return this.treeid;
	}
	
	protected AbstractIndexingTree(TupleTrait<TValue> valuetrait, int treeid) {
		super(valuetrait, WeakRefHashTableCleaner.forIndexingTree());
		this.treeid = treeid;
	}

	@Override
	public final void terminate(int treeid) {
		this.terminateValues(treeid);
	}
	
	@Override
	public final boolean checkTerminatedWhileCleaningUp() {
		return this.getNumElements() == 0;
	}
	
	@Override
	protected int cleanUpUnnecessaryMappingsInBucket(Bucket<TWeakRef, TValue> bucket) {
		int removed = bucket.cleanUpUnnecessaryMappings(true, this.treeid);
		return removed;
	}
}
