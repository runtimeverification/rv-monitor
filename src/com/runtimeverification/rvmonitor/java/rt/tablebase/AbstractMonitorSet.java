package com.runtimeverification.rvmonitor.java.rt.tablebase;

import java.util.Arrays;

public abstract class AbstractMonitorSet<TMonitor extends IMonitor> implements IMonitorSet {
	protected int size = 0;
	protected TMonitor[] elements;
	
	@Override
	public final void terminate(int treeid) {
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
	public final boolean checkTerminatedWhileCleaningUp() {
		this.removeTerminated();
		return this.size == 0;
	}
	
	public final void add(TMonitor e) {
		this.ensureCapacity();
		this.elements[this.size++] = e;
	}
	
	private void ensureCapacity() {
		int oldCapacity = this.elements.length;
		
		if (this.size + 1 > oldCapacity)
			this.removeTerminated();

		if (this.size + 1 > oldCapacity) {
			TMonitor[] oldData = this.elements;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < this.size + 1){
				newCapacity = this.size + 1;
			}
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

		for(int i = numAlive; i < this.size; ++i)
			this.elements[i] = null;
		this.size = numAlive;
	}
}
