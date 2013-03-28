package rvmonitortestsuite;

import rvmonitor.util.Tool;

public class TestCaseSpec {
	TestCase testcase;
	String name;
	
	String spec_filename;
	String monitor_filename;
	String err_filename;

	boolean hasErrorFile = false;
	
	public TestCaseSpec(TestCase parent, String specFileName) throws Exception{
		if(!specFileName.endsWith(Tool.getSpecFileExt()))
			throw new Exception("A specification file does not have the extension " + Tool.getSpecFileExt());

		this.testcase = parent;
		this.name = specFileName.substring(0, specFileName.length() - 4);

		this.spec_filename = this.name + Tool.getSpecFileExt();
		this.err_filename = this.name + ".err";
		this.monitor_filename = this.name + "RuntimeMonitor.java";
	}
	
	
}
