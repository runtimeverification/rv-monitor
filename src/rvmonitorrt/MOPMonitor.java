package rvmonitorrt;

public abstract class MOPMonitor implements rvmonitorrt.MOPObject {
	public abstract void endObject(int idnum);
	public boolean MOP_terminated = false;
	public int MOP_lastevent = -1;
}
