package com.runtimeverification.rvmonitor.java.rt.observable;

public interface IObservable<T> {
	public void subscribe(T observer);
	public void unsubscribe(T observer);
}
