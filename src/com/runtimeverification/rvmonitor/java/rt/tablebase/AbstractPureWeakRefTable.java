package com.runtimeverification.rvmonitor.java.rt.tablebase;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;

public abstract class AbstractPureWeakRefTable<TWeakRef extends CachedWeakReference> extends WeakRefHashTable<TWeakRef, Tuple0> implements IWeakRefTableOperation<TWeakRef> {
	protected AbstractPureWeakRefTable() {
		super(null, WeakRefHashTableCleaner.forPureWeakRefTable());
	}
	
	@Override
	protected int cleanUpUnnecessaryMappingsInBucket(Bucket<TWeakRef, Tuple0> bucket) {
		return bucket.cleanUpUnnecessaryMappings(false, 0);
	}
}

