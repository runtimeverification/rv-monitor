package com.runtimeverification.rvmonitor.java.rt;

public interface JavaLibInterface {
	enum Category { Match, Fail, Unknown };
	boolean isCoreachable();
	void process(String s);
	Category getCategory();
}
