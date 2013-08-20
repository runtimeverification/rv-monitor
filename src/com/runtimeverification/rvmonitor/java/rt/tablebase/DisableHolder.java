package com.runtimeverification.rvmonitor.java.rt.tablebase;

public class DisableHolder implements IIndexingTreeValue, IDisableHolder {
	private long disable = -1;
	private final long tau;
	
	protected DisableHolder(long tau) {
		this.tau = tau;
	}

	@Override
	public long getTau() {
		return this.tau;
	}
	
	@Override
	public long getDisable() {
		return this.disable;
	}

	@Override
	public void setDisable(long value) {
		this.disable = value;
	}

	@Override
	public void terminate(int treeid) {
	}

	@Override
	public boolean checkTerminatedWhileCleaningUp() {
		return false;
	}
}
