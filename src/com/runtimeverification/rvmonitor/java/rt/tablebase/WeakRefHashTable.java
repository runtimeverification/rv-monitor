package com.runtimeverification.rvmonitor.java.rt.tablebase;

import java.util.ArrayList;

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
	private int partialCleanUpCursor = -1;
	
	private long numElementsThreshold;
	
	public int getDefaultCapacity() {
		return this.config.DEFAULT_CAPACITY;
	}
	
	public long getNumElements() {
		return this.numElements;
	}
	
	private WeakRefHashTableCleaner(WeakRefHashTableCleanerConfig config) {
		this.config = config;
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
	
				this.numElementsThreshold = (long)(newsize * this.config.DEFAULT_LOAD_FACTOR);
			}
		}
		else if (this.numElements >= this.config.DEFAULT_CLEANUP_THRESHOLD && this.numElements >= bucketsize / 8) {
			this.partialCleanUpCursor = table.cleanUpUnnecessaryMappings(true, this.config.NUM_PARTIAL_CLEANUP_UNIT, this.partialCleanUpCursor);
		}
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
		this.cacheWeakRef = new CacheEntry<TWeakRef, TValue>();
	}
	
	protected final BucketNode<TWeakRef, TValue> getNode(TWeakRef key) {
		Bucket<TWeakRef, TValue> bucket = this.getBucket(key);
		for (BucketNode<TWeakRef, TValue> node = bucket.getHead(); node != null; node = node.getNext()) {
			if (key == node.getKey())
				return node;
		}
		return null;
	}
	
	protected final void putNodeUnconditional(TWeakRef key, TValue value) {
		Bucket<TWeakRef, TValue> bucket = this.getBucket(key);

		bucket.add(key, value);
		this.cleaner.onElementAdded(this, this.buckets.size());
	}
	
	protected final void putNodeAdditive(TWeakRef key, TValue value, int valueflag) {
		Bucket<TWeakRef, TValue> bucket = this.getBucket(key);
		for (BucketNode<TWeakRef, TValue> node = bucket.getHead(); node != null; node = node.getNext()) {
			if (key == node.getKey()) {
				this.valueTrait.set(node.getValue(), value, valueflag);
				return;
			}
		}

		bucket.add(key, value);
		this.cleaner.onElementAdded(this, this.buckets.size());
	}
	
	protected final TWeakRef findOrCreateWeakRefInternal(Object key, boolean create) {
		TWeakRef weakref = this.cacheWeakRef.getWeakRef(key);
		if (weakref != null)
			return weakref;

		int hashval = System.identityHashCode(key);
		Bucket<TWeakRef, TValue> bucket = this.getBucket(hashval);
		for (BucketNode<TWeakRef, TValue> node = bucket.getHead(); node != null; node = node.getNext()) {
			if (key == node.getKey()) {
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
	
	protected abstract int cleanUpUnnecessaryMappingsInBucket(Bucket<TWeakRef, TValue> bucket);
	
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
	
	protected void terminateValues(int treeid) {
		for (Bucket<TWeakRef, TValue> bucket : this.buckets)
			bucket.terminateValues(treeid);
	}
}

class CacheEntry<TWeakRef extends CachedWeakReference, TValue> {
	private Object ref;
	private TWeakRef weakref;
	
	public Object getRef() {
		return this.ref;
	}
	
	public TWeakRef getWeakRef(Object key) {
		if (key == this.ref)
			return this.weakref;
		return null;
	}
	
	public void set(Object ref, BucketNode<TWeakRef, TValue> node) {
		this.ref = ref;
		this.weakref = node.getKey();
	}
	
	public void invalidate() {
		this.ref = null;
		this.weakref = null;
	}
}
