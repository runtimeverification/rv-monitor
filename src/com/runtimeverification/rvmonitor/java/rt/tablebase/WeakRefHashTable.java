package com.runtimeverification.rvmonitor.java.rt.tablebase;

import java.util.ArrayList;

import com.runtimeverification.rvmonitor.java.rt.RuntimeOption;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;

class WeakRefHashTableCleanerConfig {
	final int DEFAULT_CAPACITY;
	final float DEFAULT_LOAD_FACTOR;
	final int MAXIMUM_CAPACITY;
	final int DEFAULT_CLEANUP_THRESHOLD;
	final int NUM_PARTIAL_CLEANUP_UNIT;
	
	final static WeakRefHashTableCleanerConfig forIndexingTree;
	final static WeakRefHashTableCleanerConfig forPureWeakRefTable;
	
	static {
		forIndexingTree = new WeakRefHashTableCleanerConfig(16, 1 << 10, 5);
		forPureWeakRefTable = new WeakRefHashTableCleanerConfig(128, 1 << 8, 16);
	}
	
	private WeakRefHashTableCleanerConfig(int defaultCapacity, int cleanupThreshold, int numPartialCleanupUnit) {
		this.DEFAULT_CAPACITY = defaultCapacity;
		this.DEFAULT_LOAD_FACTOR = 0.75f;
		this.MAXIMUM_CAPACITY = 1 << 30;
		this.DEFAULT_CLEANUP_THRESHOLD = 512;
		this.NUM_PARTIAL_CLEANUP_UNIT = numPartialCleanupUnit;
	}
}

class WeakRefHashTableCleaner {
	private final WeakRefHashTableCleanerConfig config;
	
	private long numElements;
	private int partialCleanUpCursor;
	
	private long numElementsThreshold;
	
	public int getDefaultCapacity() {
		return this.config.DEFAULT_CAPACITY;
	}
	
	public long getNumElements() {
		return this.numElements;
	}
	
	private WeakRefHashTableCleaner(WeakRefHashTableCleanerConfig config) {
		this.config = config;
		this.numElements = 0;
		this.partialCleanUpCursor = -1;
		this.updateThreshold(this.config.DEFAULT_CAPACITY);
	}
	
	public static WeakRefHashTableCleaner forIndexingTree() {
		return new WeakRefHashTableCleaner(WeakRefHashTableCleanerConfig.forIndexingTree);
	}
	
	public static WeakRefHashTableCleaner forPureWeakRefTable() {
		return new WeakRefHashTableCleaner(WeakRefHashTableCleanerConfig.forPureWeakRefTable);
	}
	
	public <TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> void onElementAdded(WeakRefHashTable<TWeakRef, TValue> table, int bucketsize) {
		this.numElements++;
		this.adjustBucketSize(table, bucketsize);
	}
	
	public void onElementDeleted(int num) {
		this.numElements -= num;
	}
	
	private <TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> void adjustBucketSize(WeakRefHashTable<TWeakRef, TValue> table, int bucketsize) {
		if (this.numElements >= this.numElementsThreshold) {
			table.cleanUpUnnecessaryMappings(false, 0, 0);
			if (this.numElements >= bucketsize / 2) {
				int newsize = Math.min(this.config.MAXIMUM_CAPACITY, bucketsize * 2);
				table.setBucketSize(newsize);
				this.updateThreshold(newsize);
			}
		}
		else if (this.numElements >= this.config.DEFAULT_CLEANUP_THRESHOLD && this.numElements >= bucketsize / 8) {
			this.partialCleanUpCursor = table.cleanUpUnnecessaryMappings(true, this.config.NUM_PARTIAL_CLEANUP_UNIT, this.partialCleanUpCursor);
		}
	}
	
	private void updateThreshold(int bucketsize) {
		this.numElementsThreshold = (long)(bucketsize * this.config.DEFAULT_LOAD_FACTOR);
	}
}
 
abstract class WeakRefHashTable<TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> {
	private final TupleTrait<TValue> valueTrait;
	private final WeakRefHashTableCleaner cleaner;

	private ArrayList<Bucket<TWeakRef, TValue>> buckets;
	private final CacheEntry<TWeakRef, TValue> cacheWeakRef;
	
	protected long getNumElements() {
		return this.cleaner.getNumElements();
	}

	protected WeakRefHashTable(TupleTrait<TValue> valuetrait, WeakRefHashTableCleaner cleaner) {
		this.valueTrait = valuetrait;
		this.cleaner = cleaner;

		this.setBucketSize(this.cleaner.getDefaultCapacity());
		if (RuntimeOption.isFineGrainedLockEnabled())
			this.cacheWeakRef = new ThreadLocalCacheEntry<TWeakRef, TValue>();
		else
			this.cacheWeakRef = new OrdinaryCacheEntry<TWeakRef, TValue>();
	}
	
	protected synchronized final BucketNode<TWeakRef, TValue> getNode(TWeakRef key) {
		Bucket<TWeakRef, TValue> bucket = this.getBucket(key);
		for (BucketNode<TWeakRef, TValue> node = bucket.getHead(); node != null; node = node.getNext()) {
			TWeakRef wref = node.getKey();
			if (key == wref)
				return node;
		}
		return null;
	}
	
	private synchronized final void putNodeInternal(TWeakRef key, TValue value, boolean additive, int valueflag) {
		Bucket<TWeakRef, TValue> bucket = this.getBucket(key);
		for (BucketNode<TWeakRef, TValue> node = bucket.getHead(); node != null; node = node.getNext()) {
			TWeakRef wref = node.getKey();
			if (key == wref) {
				if (additive)
					this.valueTrait.set(node.getValue(), value, valueflag);
				else
					node.setValue(value);
				return;
			}
		}

		bucket.add(key, value);
		this.cleaner.onElementAdded(this, this.buckets.size());		
	}
	
	protected final void putNodeUnconditional(TWeakRef key, TValue value) {
		this.putNodeInternal(key, value, false, 0);
	}
	
	protected final void putNodeAdditive(TWeakRef key, TValue value, int valueflag) {
		this.putNodeInternal(key, value, true, valueflag);
	}
	
	protected final TWeakRef findOrCreateWeakRefInternal(Object key, boolean create) {
		TWeakRef weakref = this.cacheWeakRef.getWeakRef(key);
		if (weakref != null)
			return weakref;

		int hashval = System.identityHashCode(key);
		
		synchronized (this) {
			Bucket<TWeakRef, TValue> bucket = this.getBucket(hashval);
			for (BucketNode<TWeakRef, TValue> node = bucket.getHead(); node != null; node = node.getNext()) {
				TWeakRef wref = node.getKey();
				if (key == wref.get()) {
					this.cacheWeakRef.set(key, node);
					return node.getKey();
				}
			}
			
			if (!create) {
				this.cacheWeakRef.invalidate();
				return null;
			}
			
			weakref = this.createWeakRef(key, hashval);
			BucketNode<TWeakRef, TValue> node = bucket.add(weakref, null);
			this.cleaner.onElementAdded(this, this.buckets.size());
		
			this.cacheWeakRef.set(key, node);
			return weakref;
		}
	}
	
	protected abstract TWeakRef createWeakRef(Object key, int hashval);
	
	private Bucket<TWeakRef, TValue> getBucket(TWeakRef key) {
		return this.getBucket(key.hashCode());
	}
	
	private Bucket<TWeakRef, TValue> getBucket(int hashval) {
		return this.getBucket(this.buckets, hashval);
	}
	
	private Bucket<TWeakRef, TValue> getBucket(ArrayList<Bucket<TWeakRef, TValue>> buckets, TWeakRef key) {
		return this.getBucket(buckets, key.hashCode());
	}
	
	private Bucket<TWeakRef, TValue> getBucket(ArrayList<Bucket<TWeakRef, TValue>> buckets, int hashval) {
		int index = hashval & (buckets.size() - 1);
		return buckets.get(index);
	}
	
	// The caller should synchronize this method.
	void setBucketSize(int newsize) {
		ArrayList<Bucket<TWeakRef, TValue>> newbuckets = new ArrayList<Bucket<TWeakRef, TValue>>(newsize);
		for (int i = 0; i < newsize; ++i)
			newbuckets.add(new Bucket<TWeakRef, TValue>());
		
		if (this.buckets != null) {
			for (Bucket<TWeakRef, TValue> bucket : this.buckets) {
				for (BucketNode<TWeakRef, TValue> node = bucket.getHead(); node != null; node = node.getNext()) {
					Bucket<TWeakRef, TValue> target = this.getBucket(newbuckets, node.getKey());
					target.add(node.getKey(), node.getValue());
				}
			}
		}
	
		this.buckets = newbuckets;
	}
	
	// The caller should synchronize this method.
	protected abstract int cleanUpUnnecessaryMappingsInBucket(Bucket<TWeakRef, TValue> bucket);
	
	// The caller should synchronize this method.
	int cleanUpUnnecessaryMappings(boolean partial, int limit, int cleancursor) {
		if (partial) {
			if (cleancursor < 0)
				cleancursor = this.buckets.size() - 1;
	
			for (int i = 0; i < limit && cleancursor >= 0; ++i) {
				Bucket<TWeakRef, TValue> bucket = this.buckets.get(cleancursor);
				int removed = this.cleanUpUnnecessaryMappingsInBucket(bucket);
				this.cleaner.onElementDeleted(removed);
				cleancursor--;
			}
			return cleancursor;
		}
		else {
			for (Bucket<TWeakRef, TValue> bucket : this.buckets) {
				int removed = this.cleanUpUnnecessaryMappingsInBucket(bucket);
				this.cleaner.onElementDeleted(removed);
			}
			return 0;
		}
	}
	
	protected synchronized void terminateValues(int treeid) {
		for (Bucket<TWeakRef, TValue> bucket : this.buckets)
			bucket.terminateValues(treeid);
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		int i = 0;
		for (Bucket<TWeakRef, TValue> bucket : this.buckets) {
			r.append("[");
			r.append(i);
			r.append("] ");
			r.append(bucket.toString());
			r.append("\n");
			
			++i;
		}
		return r.toString();
	}
}

abstract class CacheEntry<TWeakRef extends CachedWeakReference, TValue> {
	public abstract Object getRef();
	public abstract TWeakRef getWeakRef(Object key);
	
	public abstract void set(Object ref, BucketNode<TWeakRef, TValue> node);
	public abstract void invalidate();
	
	protected CacheEntry() {
	}
}

class OrdinaryCacheEntry<TWeakRef extends CachedWeakReference, TValue> extends CacheEntry<TWeakRef, TValue> {
	private Object ref;
	private TWeakRef weakref;
	
	@Override
	public Object getRef() {
		return this.ref;
	}
	
	@Override
	public TWeakRef getWeakRef(Object key) {
		if (key == this.ref)
			return this.weakref;
		return null;
	}
	
	@Override
	public void set(Object ref, BucketNode<TWeakRef, TValue> node) {
		this.ref = ref;
		this.weakref = node.getKey();
	}
	
	@Override
	public void invalidate() {
		this.ref = null;
		this.weakref = null;
	}
}

class ThreadLocalCacheEntry<TWeakRef extends CachedWeakReference, TValue> extends CacheEntry<TWeakRef, TValue> {
	protected final ThreadLocal<OrdinaryCacheEntry<TWeakRef, TValue>> tls = new ThreadLocal<OrdinaryCacheEntry<TWeakRef, TValue>>() {
		@Override protected OrdinaryCacheEntry<TWeakRef, TValue> initialValue() {
			return new OrdinaryCacheEntry<TWeakRef, TValue>();
		}
	};
	
	@Override
	public Object getRef() {
		return this.tls.get().getRef();
	}
	
	@Override
	public TWeakRef getWeakRef(Object key) {
		return this.tls.get().getWeakRef(key);
	}
	
	@Override
	public void set(Object ref, BucketNode<TWeakRef, TValue> node) {
		this.tls.get().set(ref, node);
	}
	
	@Override
	public void invalidate() {
		this.tls.get().invalidate();
	}
}
