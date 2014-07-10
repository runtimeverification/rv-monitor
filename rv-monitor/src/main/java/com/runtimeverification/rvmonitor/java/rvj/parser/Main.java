package com.runtimeverification.rvmonitor.java.rvj.parser;

import java.io.File;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.RVMSpecFileExt;
import com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.RVMonitorParser;

public class Main {

	public static void main(String[] args) {
		try {
			RVMSpecFileExt f = RVMonitorParser.parse(new File(args[0]));
			RVMSpecFile o = RVMonitorExtender.translateMopSpecFile(f);
			
			System.out.print(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
