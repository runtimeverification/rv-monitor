package com.runtimeverification.rvmonitor.java.rt.tablebase;

import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple3;

public abstract class IndexingTree3<TWeakRef extends CachedWeakReference, TValue1 extends IIndexingTreeValue, TValue2 extends IIndexingTreeValue, TValue3 extends IIndexingTreeValue> extends AbstractIndexingTree<TWeakRef, Tuple3<TValue1, TValue2, TValue3>> {
	protected IndexingTree3(int treeid) {
		super(new Tuple3Trait<TValue1, TValue2, TValue3>(), treeid);
	}
	
	protected final TValue1 get1(TWeakRef key) {
		BucketNode<TWeakRef, Tuple3<TValue1, TValue2, TValue3>> node = this.getNode(key);
		if (node == null) return null;
		return node.getValue().getValue1();
	}
	
	protected final void put1(TWeakRef key, TValue1 value1) {
		Tuple3<TValue1, TValue2, TValue3> tuple = new Tuple3<TValue1, TValue2, TValue3>(value1, null, null);
		this.putNodeAdditive(key, tuple, 1);
	}
	
	protected final TValue2 get2(TWeakRef key) {
		BucketNode<TWeakRef, Tuple3<TValue1, TValue2, TValue3>> node = this.getNode(key);
		if (node == null) return null;
		return node.getValue().getValue2();
	}
	
	protected final void put2(TWeakRef key, TValue2 value2) {
		Tuple3<TValue1, TValue2, TValue3> tuple = new Tuple3<TValue1, TValue2, TValue3>(null, value2, null);
		this.putNodeAdditive(key, tuple, 2);
	}
	
	protected final TValue3 get3(TWeakRef key) {
		BucketNode<TWeakRef, Tuple3<TValue1, TValue2, TValue3>> node = this.getNode(key);
		if (node == null) return null;
		return node.getValue().getValue3();
	}
	
	protected final void put3(TWeakRef key, TValue3 value3) {
		Tuple3<TValue1, TValue2, TValue3> tuple = new Tuple3<TValue1, TValue2, TValue3>(null, null, value3);
		this.putNodeAdditive(key, tuple, 3);
	}
}