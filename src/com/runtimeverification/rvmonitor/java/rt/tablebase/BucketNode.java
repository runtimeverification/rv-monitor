package com.runtimeverification.rvmonitor.java.rt.tablebase;

import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;

public class BucketNode<TWeakRef extends CachedWeakReference, TValue> {
	private final TWeakRef key;
	private TValue value;
	private BucketNode<TWeakRef, TValue> next;
	
	public TWeakRef getKey() {
		return this.key;
	}
	
	public boolean isKeyReclaimed() {
		return this.key.get() == null;
	}
	
	public TValue getValue() {
		return this.value;
	}
	
	public void setValue(TValue value) {
		this.value = value;
	}
	
	public BucketNode<TWeakRef, TValue> getNext() {
		return this.next;
	}
	
	public void setNext(BucketNode<TWeakRef, TValue> next) {
		this.next = next;
	}
	
	BucketNode(TWeakRef key, BucketNode<TWeakRef, TValue> next, TValue value) {
		this.key = key;
		this.next = next;
		this.value = value;
	}
	
	@Override
	public String toString() {
		String r = "";
		if (this.key != null) {
			r += "<";
			r += this.key;
			r += ":";
			r += this.value;
			r += ">";
			if (this.next != null) {
				r += " -> ";
				r += this.next.toString();
			}
		}
		return r;
	}
}

class Bucket<TWeakRef extends CachedWeakReference, TValue extends IIndexingTreeValue> {
	private BucketNode<TWeakRef, TValue> head;
	
	public BucketNode<TWeakRef, TValue> getHead() {
		return this.head;
	}
	
	public final BucketNode<TWeakRef, TValue> add(TWeakRef key, TValue value) {
		BucketNode<TWeakRef, TValue> newhead = new BucketNode<TWeakRef, TValue>(key, this.head, value);
		this.head = newhead;
		return newhead;
	}
	
	public final int cleanUpUnnecessaryMappings(boolean terminateValue, int treeid) {
		int removed = 0;
		BucketNode<TWeakRef, TValue> prev = null;
		for (BucketNode<TWeakRef, TValue> node = this.head; node != null; node = node.getNext()) {
			if (node.isKeyReclaimed()) {
				if (prev == null)
					this.head = node.getNext();
				else
					prev.setNext(node.getNext());
				
				if (terminateValue)
					this.terminateValue(node, treeid);
	
				++removed;
			}
			else
				prev = node;
		}
		return removed;
	}
	
	public final void terminateValues(int treeid) {
		for (BucketNode<TWeakRef, TValue> node = this.head; node != null; node = node.getNext()) {
			this.terminateValue(node, treeid);
		}
	}
	
	private void terminateValue(BucketNode<TWeakRef, TValue> node, int treeid) {
		TValue val = node.getValue();
		if (val != null)
			val.terminate(treeid);
	}
	
	@Override
	public String toString() {
		return this.head == null ? "<empty>" : this.head.toString();
	}
}
