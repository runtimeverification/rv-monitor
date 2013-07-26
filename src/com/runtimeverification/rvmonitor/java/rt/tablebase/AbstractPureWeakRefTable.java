package com.runtimeverification.rvmonitor.java.rt.tablebase;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple0;

public abstract class AbstractPureWeakRefTable<TWeakRef extends CachedWeakReference> extends WeakRefHashTable<TWeakRef, Tuple0> implements IWeakRefTableOperation<TWeakRef> {
	protected AbstractPureWeakRefTable() {
		super(null, WeakRefHashTableCleaner.forPureWeakRefTable());
	}
	
	@Override
	protected int cleanUpUnnecessaryMappingsInBucket(Bucket<TWeakRef, Tuple0> bucket) {
		int removed = bucket.cleanUpUnnecessaryMappings(false, 0);
		return removed;
	}
}

