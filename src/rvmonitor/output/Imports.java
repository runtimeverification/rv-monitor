package rvmonitor.output;

import java.util.ArrayList;

import rvmonitor.parser.ast.ImportDeclaration;
import rvmonitor.parser.ast.MOPSpecFile;

public class Imports {
	ArrayList<String> imports;
	String[] required = {"java.util.concurrent.*", "java.util.concurrent.locks.*", "java.util.*", "rvmonitorrt.*", "java.lang.ref.*", "org.aspectj.lang.*" };

	public Imports(MOPSpecFile mopSpecFile) {
		imports = new ArrayList<String>();

		for (ImportDeclaration imp : mopSpecFile.getImports()) {
			String n = "";
			if (imp.isStatic())
				n += "static ";
			n += imp.getName().toString().trim(); 
			if (imp.isAsterisk())
				n += ".*";

			if(!imports.contains(n))
				imports.add(n);
		}
		
		for (String req : required) {
			if(!imports.contains(req))
				imports.add(req);
		}
		
	}

	public String toString() {
		String ret = "";

		for (String imp : imports)
			ret += "import " + imp + ";\n";

		return ret;
	}

}
