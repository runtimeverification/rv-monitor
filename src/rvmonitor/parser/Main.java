package rvmonitor.parser;

import java.io.File;

import rvmonitor.parser.ast.MOPSpecFile;
import rvmonitor.parser.astex.MOPSpecFileExt;
import rvmonitor.parser.main_parser.JavaMOPParser;

public class Main {

	public static void main(String[] args) {
		try {
			MOPSpecFileExt f = JavaMOPParser.parse(new File(args[0]));
			MOPSpecFile o = JavaMOPExtender.translateMopSpecFile(f);
			
			System.out.print(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
