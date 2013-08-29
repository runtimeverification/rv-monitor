package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

public abstract class CodeMember {
	protected final String name;
	protected final boolean statik;
	protected final boolean publik;
	
	public String getName() {
		return this.name;
	}
	
	protected CodeMember(String name, boolean statik, boolean publik) {
		this.name = name;
		this.statik = statik;
		this.publik = publik;
	}
}
