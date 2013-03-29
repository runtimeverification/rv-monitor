package rvmonitor.parser;

import java.io.File;

import rvmonitor.parser.ast.RVMSpecFile;
import rvmonitor.parser.astex.RVMSpecFileExt;
import rvmonitor.parser.main_parser.RVMonitorParser;

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
