package com.runtimeverification.rvmonitor.java.rvj.output;

public class UserJavaCode {
	String code;

	public UserJavaCode(String code) {
		this.code = code;
	}

	public String toString() {
		String ret = "";

		if (code != null)
			ret += code;

		return ret;
	}
}
