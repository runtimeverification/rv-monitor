package com.runtimeverification.rvmonitor.java.rt.tablebase;

public abstract class AbstractMonitor implements IMonitor {
	protected boolean RVM_terminated;
	protected int RVM_lastevent = -1;
	
	protected abstract void terminateInternal(int treeid);
	
	@Override
	public final void terminate(int treeid) {
		if (!this.RVM_terminated) {
			this.terminateInternal(treeid);
			this.RVM_terminated = true;
		}
	}
	
	@Override
	public final boolean checkTerminatedWhileCleaningUp() {
		return this.RVM_terminated;
	}
	
	@Override
	public boolean isTerminated() {
		return this.RVM_terminated;
	}
	
	@Override
	public String toString() {
		String r = this.getClass().getSimpleName();
		r += "#";
		r += String.format("%03x", this.hashCode() & 0xFFF);
		return r;
	}
}
