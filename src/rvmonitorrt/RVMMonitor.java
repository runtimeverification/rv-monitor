package rvmonitorrt;

public abstract class RVMMonitor implements RVMObject {
	public abstract void endObject(int idnum);
	public boolean RVM_terminated = false;
	public int RVM_lastevent = -1;
}
