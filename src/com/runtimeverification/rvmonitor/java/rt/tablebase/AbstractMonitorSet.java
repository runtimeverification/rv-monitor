package com.runtimeverification.rvmonitor.java.rt.tablebase;

import java.util.Arrays;

public abstract class AbstractMonitorSet<TMonitor extends IMonitor> implements IMonitorSet {
	protected int size = 0;
	protected TMonitor[] elements;
	
	@Override
	public synchronized final void terminate(int treeid) {
		for (int i = this.size - 1; i >= 0; --i) {
			TMonitor monitor = this.elements[i];
			if (monitor != null && !monitor.isTerminated())
				monitor.terminate(treeid);
			this.elements[i] = null;
		}
	
		this.elements = null;
		this.size = 0;
	}
	
	@Override
	public synchronized final boolean checkTerminatedWhileCleaningUp() {
		this.removeTerminated();
		return this.size == 0;
	}
	
	public synchronized final void add(TMonitor e) {
		this.ensureCapacity();
		this.elements[this.size++] = e;
	}
	
	public synchronized final TMonitor get(int index) {
		return this.elements[index];
	}
	
	public synchronized final void set(int index, TMonitor monitor) {
		this.elements[index] = monitor;
	}
	
	public synchronized final int getSize() {
		return this.size;
	}
	
	public synchronized final void eraseRange(int from) {
		for (int i = from; i < this.size; ++i)
			this.elements[i] = null;
		this.size = from;
	}
	
	private void ensureCapacity() {
		int oldCapacity = this.elements.length;
		
		if (this.size + 1 > oldCapacity)
			this.removeTerminated();

		if (this.size + 1 > oldCapacity) {
			TMonitor[] oldData = this.elements;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < this.size + 1)
				newCapacity = this.size + 1;
			this.elements = Arrays.copyOf(oldData, newCapacity);
		}
	}
	
	private void removeTerminated() {
		int numAlive = 0;
		for (int i = 0; i < this.size; ++i) {
			TMonitor monitor = this.elements[i];
			if (!monitor.isTerminated()) {
				this.elements[numAlive] = monitor;
				numAlive++;
			}
		}
		
		this.eraseRange(numAlive);
	}

	@Override
	public String toString() {
		String r = this.getClass().getSimpleName();
		r += "#";
		r += String.format("%03x", this.hashCode() & 0xFFF);
		return r;
	}
}
