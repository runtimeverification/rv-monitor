package com.runtimeverification.rvmonitor.java.rt;

import com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMap;
import com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMonitor;
import com.runtimeverification.rvmonitor.java.rt.map.hashentry.RVMHashEntry;
import com.runtimeverification.rvmonitor.java.rt.ref.RVMWeakReference;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

class StopWatch {
	protected long runTime;
	protected long startTime, stopTime;

	StopWatch() {
		runTime = 0;
		startTime = 0;
		stopTime = 0;
	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void stop() {
		stopTime = System.currentTimeMillis();
		runTime += (stopTime - startTime);
	}

	public long getRunTime() {
		return runTime;
	}

}

public class Test {
	static final int NUM_DUMMY = 1000000;

	static public void memoryClean() {
		Integer[] integers = new Integer[NUM_DUMMY];

		for (int i = 0; i < NUM_DUMMY; i++) {
			integers[i] = new Integer(i);
		}

		System.gc();
	}

	private static final int SZ_REF = 4;

	private static int size_prim(Class t) {
		if (t == Boolean.TYPE)
			return 1;
		else if (t == Byte.TYPE)
			return 1;
		else if (t == Character.TYPE)
			return 2;
		else if (t == Short.TYPE)
			return 2;
		else if (t == Integer.TYPE)
			return 4;
		else if (t == Long.TYPE)
			return 8;
		else if (t == Float.TYPE)
			return 4;
		else if (t == Double.TYPE)
			return 8;
		else if (t == Void.TYPE)
			return 0;
		else
			return SZ_REF;
	}

	private static int size_inst(Class c) {
		Field flds[] = c.getDeclaredFields();
		int sz = 0;

		for (int i = 0; i < flds.length; i++) {
			Field f = flds[i];
			if (!c.isInterface() && (f.getModifiers() & Modifier.STATIC) != 0)
				continue;
			sz += size_prim(f.getType());
		}

		if (c.getSuperclass() != null)
			sz += size_inst(c.getSuperclass());

		Class cv[] = c.getInterfaces();
		for (int i = 0; i < cv.length; i++)
			sz += size_inst(cv[i]);

		return sz;
	}

	public static int sizeof(Object obj) {
		if (obj == null)
			return 0;

		Class c = obj.getClass();

		return size_inst(c);
	}

	static public void testHashSet() {
		// setting up
		HashSet set = new HashSet();

		Integer[] integers = new Integer[1000000];

		for (int i = 0; i < 1000000; i++) {
			integers[i] = new Integer(i);
			set.add(integers[i]);
		}

		StopWatch timer = new StopWatch();

		timer.start();
		// testing
		long sum = 0;
		for (Integer e : (HashSet<Integer>) set) {
			if (e != null && e >= 0)
				sum++;
		}
		timer.stop();

		System.out.println("== total execution time of HashSet iteration==");
		System.out.println(timer.getRunTime() + "ms");
	}

	static public void main(String[] args) {
		System.out.println(sizeof(new RVMMapOfMap(0)));
		System.out.println(sizeof(new RVMMapOfMonitor(0)));
		System.out.println(sizeof(new RVMHashEntry(null, null)));

		System.out.println(sizeof(new RVMWeakReference(null)));

		System.exit(0);
	}
}
