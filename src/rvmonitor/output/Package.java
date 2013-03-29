package rvmonitor.output;

import rvmonitor.parser.ast.RVMSpecFile;

public class Package {
	String packageString;

	public Package(RVMSpecFile rvmSpecFile) {
		if (rvmSpecFile.getPakage() != null)
			packageString = rvmSpecFile.getPakage().toString();
		else
			packageString = "";
		packageString = packageString.trim();
	}
	
	public String toString(){
		String ret = "";
		
		ret += packageString + "\n";
		
		return ret;
	}

}
