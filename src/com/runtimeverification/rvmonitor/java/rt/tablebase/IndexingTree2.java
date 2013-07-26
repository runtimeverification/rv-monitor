package com.runtimeverification.rvmonitor.java.rt.tablebase;

import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple2;

public abstract class IndexingTree2<TWeakRef extends CachedWeakReference, TValue1 extends IIndexingTreeValue, TValue2 extends IIndexingTreeValue> extends AbstractIndexingTree<TWeakRef, Tuple2<TValue1, TValue2>> {
	protected IndexingTree2(int treeid) {
		super(new Tuple2Trait<TValue1, TValue2>(), treeid);
	}
	
	protected final TValue1 get1(TWeakRef key) {
		BucketNode<TWeakRef, Tuple2<TValue1, TValue2>> node = this.getNode(key);
		if (node == null) return null;
		return node.getValue().getValue1();
	}
	
	protected final void put1(TWeakRef key, TValue1 value1) {
		Tuple2<TValue1, TValue2> tuple = new Tuple2<TValue1, TValue2>(value1, null);
		this.putNodeAdditive(key, tuple, 1);
	}
	
	protected final TValue2 get2(TWeakRef key) {
		BucketNode<TWeakRef, Tuple2<TValue1, TValue2>> node = this.getNode(key);
		if (node == null) return null;
		return node.getValue().getValue2();
	}
	
	protected final void put2(TWeakRef key, TValue2 value2) {
		Tuple2<TValue1, TValue2> tuple = new Tuple2<TValue1, TValue2>(null, value2);
		this.putNodeAdditive(key, tuple, 2);
	}
}