package com.runtimeverification.rvmonitor.java.rvj.util;

public class RVMonitorStack<Elt> {
	static int CAPACITY = 100; 
	int curr_index = 0;
	Elt[] elements;
	int capacity;
	public RVMonitorStack(){
		elements = (Elt[])new Object[CAPACITY];
		capacity = CAPACITY;
	}
	public RVMonitorStack(int initial_capacity){
		elements = (Elt[])new Object[initial_capacity];
		capacity = initial_capacity;
	}
	public Elt peek(){
	   return elements[curr_index - 1];
	}
	public void pop(int num){
		curr_index -= num;
	}
	public void push(Elt elt){
		elements[curr_index++] = elt;
	}
	public void clear(){
		for (int i = 0; i < curr_index; i ++)
			elements[i] = null;
		curr_index = 0;
	}
	public RVMonitorStack<Elt> fclone(){
		RVMonitorStack<Elt> ret = new RVMonitorStack<Elt>(capacity);
		ret.curr_index = curr_index;
		for (int i = 0; i < curr_index; i ++)
			ret.elements[i] = elements[i];		
		return ret;
	}
}
