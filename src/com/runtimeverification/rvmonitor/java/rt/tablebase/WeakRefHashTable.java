package com.runtimeverification.rvmonitor.java.rt.tablebase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import com.runtimeverification.rvmonitor.java.rt.RuntimeOption;
import com.runtimeverification.rvmonitor.java.rt.observable.IObservableObject;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;

/**
 * This class holds predefined configurations for indexing tree cleaners.
 *
 * @author Choonghwan Lee <clee83@illinois.edu>
 */
final class WeakRefHashTableCleanerConfig {
	final int INITIAL_CAPACITY;
	final int SECONDSTEP_CAPACITY;
	
	final static WeakRefHashTableCleanerConfig forIndexingTree;
	final static WeakRefHashTableCleanerConfig forPureWeakRefTable;
	
	static {
		forIndexingTree = new WeakRefHashTableCleanerConfig(2, 32);
		forPureWeakRefTable = new WeakRefHashTableCleanerConfig(16, 64);
	}
	
	private WeakRefHashTableCleanerConfig(int initialCapacity, int secondStepCapacity) {
		this.INITIAL_CAPACITY = initialCapacity;
		this.SECONDSTEP_CAPACITY = secondStepCapacity;
	}
}

/**
 * This class is used to hold configurations for cleaning indexing trees and GWRTs.
 * Although it is now simple, this had implemented various things that have been finally dropped.
 *
 * @author Choonghwan Lee <clee83@illinois.edu>
 */
final class WeakRefHashTableCleaner {
	private final WeakRefHashTableCleanerConfig config;
	
	public int getInitialCapacity() {
		return this.config.INITIAL_CAPACITY;
	}
	
	public int getSecondStepCapacity() {
		return this.config.SECONDSTEP_CAPACITY;
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

	public <TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> void onSaturated(WeakRefHashTable<TWeakRef, TValue> table) {
		table.increaseBucket();
	}
}
 
/**
 * This class is the base of one level of an indexing tree or global weak reference table (GWRT).
 * It implements all the necessary features; its subclasses simply qualify types to prevent the
 * generated code from wrong uses.
 * 
 * Each level in an indexing tree is a map, where the key being a weak reference and the value
 * being one of the followings:
 * 1. a nested level
 * 2. a set of monitors
 * 3. a monitor
 * 4. a Tuple2 instance for holding two of 1. 2. and 3.
 * 5. a Tuple3 instance for holding all of 1. 2. and 3.
 * 
 * Like a map, this implementation supports get and put operations. More specifically,
 * 1. putNode() or putNodeUnconditional() to add a new entry to this map
 * 2. putNodeAdditive() to update an existing entry in this map
 * 3. getNode() to retrieve an entry using a weak reference
 * 4. getNodeEquivalent() to retrieve an entry using a weak reference
 * 5. getNodeWithStrongRef() to retrieve an entry using a strong reference
 * 
 * The difference between 3. and 4. is that 3. assumes that weak reference interning is enabled and,
 * consequently, it is correct to check the equivalence of two weak references using reference equality.
 * Unlike 3., 4. does not assume that, and checks whether their referents are the same.
 * 
 * Additionally, this class implements GWRT, where one can retrieve weak references by invoking
 * findOrCreateWeakRefInternal().
 * 
 * The implementation is similar to HashMap, in the sense that it has multiple buckets and an entry
 * is distributed according to its hash value. A bucket is represented by an instance of Bucket.
 * The number of buckets is adjusted, at runtime, when it turns out that a bucket contains too many entries.
 *
 * @author Choonghwan Lee <clee83@illinois.edu>
 * @see Bucket
 *
 * @param <TWeakRef> type of the key in this level
 * @param <TValue> type of the value in this level
 */
public abstract class WeakRefHashTable<TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> implements INodeOperation<TWeakRef, TValue> {
	protected final int treeid;
	private final TupleTrait<TValue> valueTrait;
	private final WeakRefHashTableCleaner cleaner;

	private final AtomicInteger revision;
	private int defaultBucketCapacity;
	private ArrayList<Bucket<TWeakRef, TValue>> buckets;
	private final CacheEntry<TWeakRef, TValue> cacheWeakRef;
	
	protected WeakRefHashTable(int treeid, TupleTrait<TValue> valuetrait, WeakRefHashTableCleaner cleaner) {
		this.treeid = treeid;
		this.valueTrait = valuetrait;
		this.cleaner = cleaner;

		this.revision = new AtomicInteger();
		this.defaultBucketCapacity = 2;
		this.setBucketSize(true, this.getNextBucketSize());
		if (RuntimeOption.isFineGrainedLockEnabled())
			this.cacheWeakRef = new ThreadLocalCacheEntry<TWeakRef, TValue>();
		else
			this.cacheWeakRef = new OrdinaryCacheEntry<TWeakRef, TValue>();
	}
	
	private int getNextBucketSize() {
		if (this.buckets == null)
			return this.cleaner.getInitialCapacity();
		else if (this.buckets.size() == this.cleaner.getInitialCapacity())
			return this.cleaner.getSecondStepCapacity();
		else
			return this.buckets.size() * 2;
	}
	
	@Override
	public final synchronized TValue getNode(TWeakRef key) {
		Bucket<TWeakRef, TValue> bucket = this.getBucket(key);
		if (bucket == null)
			return null;
		return bucket.findByWeakRef(key);
	}
	
	private final synchronized TValue getNodeEquivalentInternal(int hashval, Object key) {
		Bucket<TWeakRef, TValue> bucket = this.getBucket(hashval);
		if (bucket == null)
			return null;
		return bucket.findByStrongRef(key);
	}
	
	@Override
	public final TValue getNodeEquivalent(TWeakRef key) {
		int hashval = key.hashCode();
		return this.getNodeEquivalentInternal(hashval, key.get());
	}
	
	@Override
	public final TValue getNodeWithStrongRef(Object key) {
		int hashval = System.identityHashCode(key);
		return this.getNodeEquivalentInternal(hashval, key);
	}

	private final synchronized void putNodeInternal(TWeakRef key, TValue value, boolean additive, int valueflag) {
		Bucket<TWeakRef, TValue> bucket = this.getOrCreateBucket(key);
		if (bucket.updateOrAdd(this.valueTrait, key, value, additive, valueflag)) {
			if (bucket.isSaturated())
				this.cleaner.onSaturated(this);
		}
	}
	
	protected final void putNodeUnconditional(TWeakRef key, TValue value) {
		this.putNodeInternal(key, value, false, 0);
	}
	
	protected final void putNodeAdditive(TWeakRef key, TValue value, int valueflag) {
		this.putNodeInternal(key, value, true, valueflag);
	}

	@Override
	public final void putNode(TWeakRef key, TValue value) {
		this.putNodeInternal(key, value, false, 0);
	}
	
	/**
	 * Finds or creates (only if 'create' is true) the weak reference that corresponds to
	 * the given key.
	 * @param key the strong reference
	 * @param create true if the weak reference should be created when there is none
	 * @return found or created weak reference
	 */
	protected final TWeakRef findOrCreateWeakRefInternal(Object key, boolean create) {
		TWeakRef weakref = this.cacheWeakRef.getWeakRef(key);
		if (weakref != null)
			return weakref;

		int hashval = System.identityHashCode(key);
		
		// The new bucket node inserted below may be lost if another thread is
		// resizing the buckets. Unlike putNodeInternal(), where losing the inserted
		// node may result in an error, losing a weak reference here does not result
		// in such catastrophic error; the weak reference will be added next time.
		Bucket<TWeakRef, TValue> bucket = this.getOrCreateBucket(hashval);
		synchronized (bucket) {
			weakref = bucket.findWeakRef(key);
			if (weakref != null) {
				this.cacheWeakRef.set(key, weakref);
				return weakref;
			}
			
			if (!create) {
				this.cacheWeakRef.invalidate();
				return null;
			}
			
			weakref = this.createWeakRef(key, hashval);
			bucket.add(weakref, null);
		}

		if (bucket.isSaturated())
			this.cleaner.onSaturated(this);
		
		this.cacheWeakRef.set(key, weakref);
		return weakref;
	}

	protected abstract TWeakRef createWeakRef(Object key, int hashval);
	
	private final Bucket<TWeakRef, TValue> getBucket(TWeakRef key) {
		return this.getBucket(this.buckets, key.hashCode());
	}
	
	private final Bucket<TWeakRef, TValue> getBucket(int hashval) {
		return this.getBucket(this.buckets, hashval);
	}
	
	private final Bucket<TWeakRef, TValue> getBucket(ArrayList<Bucket<TWeakRef, TValue>> buckets, int hashval) {
		int index = this.getBucketIndex(buckets, hashval);
		return buckets.get(index);
	}

	private final synchronized Bucket<TWeakRef, TValue> getOrCreateBucket(TWeakRef key) {
		return this.getOrCreateBucket(this.buckets, key.hashCode());
	}

	private final synchronized Bucket<TWeakRef, TValue> getOrCreateBucket(int hashval) {
		return this.getOrCreateBucket(this.buckets, hashval);
	}

	private final Bucket<TWeakRef, TValue> getOrCreateBucket(ArrayList<Bucket<TWeakRef, TValue>> buckets, TWeakRef key) {
		return this.getOrCreateBucket(buckets, key.hashCode());
	}
	
	private final Bucket<TWeakRef, TValue> getOrCreateBucket(ArrayList<Bucket<TWeakRef, TValue>> buckets, int hashval) {
		int index = this.getBucketIndex(buckets, hashval);
		Bucket<TWeakRef, TValue> bucket = buckets.get(index);
		if (bucket != null)
			return bucket;
		
		bucket = new Bucket<TWeakRef, TValue>(this.treeid, this.defaultBucketCapacity);
		buckets.set(index, bucket);
		return bucket;
	}
	
	private final int getBucketIndex(ArrayList<Bucket<TWeakRef, TValue>> buckets, int hashval) {
		return hashval & (buckets.size() - 1);
	}
	
	public void increaseBucket() {
		int newsize = this.getNextBucketSize();
		this.setBucketSize(false, newsize);
	}
	
	private final void updateDefaultBucketCapacity(ArrayList<Bucket<TWeakRef, TValue>> oldbuckets) {
		// Since the number of buckets is doubled below, the number of elements
		// that each new bucket holds would be close to half. It simply picks the
		// first few buckets and see their sizes for the purpose of getting statistics.
		int denom = 0;
		int sum = 0;
		for (Bucket<TWeakRef, TValue> bucket : oldbuckets) {
			if (bucket == null)
				continue;
			denom++;
			sum += bucket.size();
			if (denom == 4)
				break;
		}
		
		if (denom == 0)
			return;

		int capacity = sum / denom;
	
		// The capacity should be a power of 2.
		int safecapacity = 1;
		for (int c = capacity >> 1; c != 0; c >>= 1)
			safecapacity <<= 1;
		if (safecapacity < capacity)
			safecapacity <<= 1;	

		this.defaultBucketCapacity = safecapacity;
	}
	
	private final synchronized void setBucketSize(boolean initial, int newsize) {
		ArrayList<Bucket<TWeakRef, TValue>> oldbuckets = this.buckets; 
		
		if (!initial)
			this.updateDefaultBucketCapacity(oldbuckets);

		ArrayList<Bucket<TWeakRef, TValue>> newbuckets = new ArrayList<Bucket<TWeakRef, TValue>>(newsize);
		for (int i = 0; i < newsize; ++i) {
			// Let's create buckets lazily.
			newbuckets.add(null);
		}

		if (oldbuckets != null) {
			for (Bucket<TWeakRef, TValue> bucket : oldbuckets) {
				if (bucket == null)
					continue;
				synchronized (bucket) {
					Bucket<TWeakRef, TValue>.PairIterator it = bucket.iterator();
					while (it.moveNext()) {
						if (!initial) {
							if (bucket.terminateIfReclaimed(it.getKey(), it.getValue()))
								continue;
						}
						Bucket<TWeakRef, TValue> target = this.getOrCreateBucket(newbuckets, it.getKey());
						target.add(it.getKey(), it.getValue());
					}
				}
			}
		}
		
		synchronized (this) {
			this.buckets = newbuckets;
			this.revision.incrementAndGet();
		}
	}
	
	public synchronized final int cleanUpUnnecessaryMappings() {
		int removed = 0;
		for (Bucket<TWeakRef, TValue> bucket : this.buckets) {
			if (bucket == null)
				continue;
			int n = bucket.cleanUpUnnecessaryMappings();
			removed += n;
		}
		return removed;
	}
	
	public void printStatistics() {
		String name = this.getClass().toString();
		if (this instanceof IObservableObject) {
			IObservableObject obs = (IObservableObject)this;
			name = obs.getObservableObjectDescription();
		}
		System.out.println("=== " + name + " ===");
		StringBuilder s = new StringBuilder();
		s.append("# buckets: " + this.buckets.size());
		System.out.println(s);
		for (int i = 0; i < this.buckets.size(); ++i) {
			System.out.print(this.buckets.get(i).getCapacity());
			System.out.print(':');
			System.out.print(this.buckets.get(i).size());
			if (i % 16 == 15)
				System.out.println();
			else
				System.out.print(", ");
		}
		System.out.println();
	}
	
	/**
	 * Terminates all the values contained in this map. This will
	 * eventually terminate all the monitors reached by this map.
	 * @param treeid tree id
	 */
	protected final void terminateValues(int treeid) {
		for (Bucket<TWeakRef, TValue> bucket : this.buckets) {
			if (bucket == null)
				continue;
			bucket.terminateValues();
		}
	}
	
	public PairIterator iterator() {
		return new PairIterator();
	}
	
	public class PairIterator {
		private final Iterator<Bucket<TWeakRef, TValue>> iterbucket;
		private Bucket<TWeakRef, TValue>.PairIterator iternode;
		
		public PairIterator() {
			this.iterbucket = WeakRefHashTable.this.buckets.iterator();
		}

		public boolean moveNext() {
			if (this.iternode != null && this.iternode.moveNext())
				return true;

			while (this.iterbucket.hasNext()) {
				Bucket<TWeakRef, TValue> bucket = this.iterbucket.next();
				if (bucket == null)
					continue;
				this.iternode = bucket.iterator();
				if (this.iternode.moveNext())
					return true;
			}

			return false;
		}
		
		public TWeakRef getKey() {
			return this.iternode.getKey();
		}
		
		public TValue getValue() {
			return this.iternode.getValue();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		int i = 0;
		for (Bucket<TWeakRef, TValue> bucket : this.buckets) {
			r.append("[");
			r.append(i);
			r.append("] ");
			if (bucket != null)
				r.append(bucket.toString());
			r.append("\n");
			
			++i;
		}
		return r.toString();
	}
}

/**
 * This class represents the GWRT cache.
 * Two different implementations are available: one for ordinary cache, the other for
 * thread-specific cache. The latter was introduced not to reduce concurrency.
 *
 * @author Choonghwan Lee <clee83@illinois.edu>
 *
 * @param <TWeakRef> type of the key
 * @param <TValue> type of the value
 */
abstract class CacheEntry<TWeakRef extends CachedWeakReference, TValue> {
	public abstract Object getRef();
	public abstract TWeakRef getWeakRef(Object key);
	
	public abstract void set(Object ref, TWeakRef weakref);
	public abstract void invalidate();
	
	protected CacheEntry() {
	}
}

final class OrdinaryCacheEntry<TWeakRef extends CachedWeakReference, TValue> extends CacheEntry<TWeakRef, TValue> {
	private Object ref;
	private TWeakRef weakref;
	
	@Override
	public final Object getRef() {
		return this.ref;
	}
	
	@Override
	public final TWeakRef getWeakRef(Object key) {
		if (key == this.ref)
			return this.weakref;
		return null;
	}
	
	@Override
	public final void set(Object ref, TWeakRef weakref) {
		this.ref = ref;
		this.weakref = weakref;
	}
	
	@Override
	public final void invalidate() {
		this.ref = null;
		this.weakref = null;
	}
}

final class ThreadLocalCacheEntry<TWeakRef extends CachedWeakReference, TValue> extends CacheEntry<TWeakRef, TValue> {
	protected final ThreadLocal<OrdinaryCacheEntry<TWeakRef, TValue>> tls = new ThreadLocal<OrdinaryCacheEntry<TWeakRef, TValue>>() {
		@Override protected OrdinaryCacheEntry<TWeakRef, TValue> initialValue() {
			return new OrdinaryCacheEntry<TWeakRef, TValue>();
		}
	};
	
	@Override
	public final Object getRef() {
		return this.tls.get().getRef();
	}
	
	@Override
	public final TWeakRef getWeakRef(Object key) {
		return this.tls.get().getWeakRef(key);
	}
	
	@Override
	public final void set(Object ref, TWeakRef weakref) {
		this.tls.get().set(ref, weakref);
	}
	
	@Override
	public final void invalidate() {
		this.tls.get().invalidate();
	}
}
