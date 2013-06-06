package com.runtimeverification.rvmonitor.java.rt.tablebase;

public interface IMapOperation<TWeakRef, TMap> {
	public TMap getMap(TWeakRef key);
	public void putMap(TWeakRef key, TMap value);
}
