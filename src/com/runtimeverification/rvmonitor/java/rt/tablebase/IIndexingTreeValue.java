package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface IIndexingTreeValue {
	public void terminate(int treeid);
	public boolean checkTerminatedWhileCleaningUp();
}
