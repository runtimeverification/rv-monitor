package rvmonitorrt.concurrent;

public class RVMNameStone {
	Thread t;
	public boolean tag;
	RVMNameStone(){
		t = Thread.currentThread();
		tag = false;
	}
}