package com.runtimeverification.rvmonitor.java.rt.tablebase;

public abstract class AbstractMonitor implements IMonitor {
	protected boolean terminated;
	protected int lastevent = -1;
	
	protected abstract void terminateInternal(int treeid);
	
	@Override
	public final void terminate(int treeid) {
		if (!this.terminated) {
			this.terminateInternal(treeid);
			this.terminated = true;
		}
	}
	
	@Override
	public final boolean checkTerminatedWhileCleaningUp() {
		return this.terminated;
	}
	
	@Override
	public boolean isTerminated() {
		return this.terminated;
	}
}
