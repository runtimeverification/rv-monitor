package com.runtimeverification.rvmonitor.java.rvj.output;

import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.ImportDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;

import java.util.ArrayList;

public class Imports {
	ArrayList<String> imports;
	String[] required = {"java.util.concurrent.*", "java.util.concurrent.locks.*", "java.util.*", "java.lang.ref.*",
		"com.runtimeverification.rvmonitor.java.rt.*",
		"com.runtimeverification.rvmonitor.java.rt.ref.*",
		"com.runtimeverification.rvmonitor.java.rt.table.*",
		"com.runtimeverification.rvmonitor.java.rt.tablebase.IBucketNode",
		"com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple2",
		"com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple3",
	};

	public Imports(RVMSpecFile rvmSpecFile) {
		imports = new ArrayList<String>();

		for (ImportDeclaration imp : rvmSpecFile.getImports()) {
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
		
		if (Main.useFineGrainedLock)
			imports.add("java.util.concurrent.atomic.AtomicBoolean");
	}

	public String toString() {
		String ret = "";

		for (String imp : imports)
			ret += "import " + imp + ";\n";

		return ret;
	}

}
