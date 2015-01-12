package com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.event.advice.LocalVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;


// it turns out that this cache is inefficient.
// do not use.
public class LocalityIndexingCache extends IndexingCache {
	private final int size = 16;
	
	public LocalityIndexingCache(RVMVariable name, RVMParameters param, RVMParameters fullParam, SuffixMonitor monitor, MonitorSet monitorSet, HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) {
		super(name, param, fullParam, monitor, monitorSet, refTrees, perthread, isGeneral);
	}

	public String getKeyType(RVMParameter p) {
		return refTrees.get(p.getType().toString()).getResultType();
	}

	public String getTreeType(RVMParameter p) {
		return refTrees.get(p.getType().toString()).getType();
	}

	public RVMVariable getKey(RVMParameter p) {
		return keys.get(p.getName());
	}

	public RVMVariable getKey(int i) {
		return keys.get(param.get(i).getName());
	}

	public String getKeyComparison() {
		String ret = "";

		for (int i = 0; i < param.size(); i++) {
			if (i > 0) {
				ret += " && ";
			}
			if (perthread) {
				ret += param.get(i).getName() + " == " + getKey(i) + ".get().get()";
			} else {
				ret += param.get(i).getName() + " == " + getKey(i) + "[thisJoinPoint.getStaticPart().getId() & " + (size - 1) + "].get()";
			}
		}

		return ret;
	}

	public String getCacheKeys(LocalVariables localVars) {
		String ret = "";

		for (RVMParameter p : param) {
			RVMVariable tempRef = localVars.getTempRef(p);

			ret += tempRef + " = ";

			if (perthread) {
				ret += getKey(p) + ".get();\n";
			} else {
				ret += getKey(p) + "[thisJoinPoint.getStaticPart().getId() & " + (size - 1) + "];\n";
			}
		}

		return ret;
	}

	public String getCacheSet(RVMVariable obj) {
		String ret = "";

		if (!hasSet)
			return ret;

		if (perthread) {
			ret += obj + " = " + set + ".get();\n";
		} else {
			ret += obj + " = " + set + "[thisJoinPoint.getStaticPart().getId() & " + (size - 1) + "];\n";
		}

		return ret;
	}

	public String getCacheNode(RVMVariable obj) {
		String ret = "";

		if (perthread) {
			ret += obj + " = " + node + ".get();\n";
		} else {
			ret += obj + " = " + node + "[thisJoinPoint.getStaticPart().getId() & " + (size - 1) + "];\n";
		}

		return ret;
	}

	public String setCacheKeys(LocalVariables localVars) {
		String ret = "";

		for (RVMParameter p : param) {
			RVMVariable tempRef = localVars.getTempRef(p);

			if (perthread) {
				ret += getKey(p) + ".set(" + tempRef + ");\n";
			} else {
				ret += getKey(p) + "[thisJoinPoint.getStaticPart().getId() & " + (size - 1) + "] = " + tempRef + ";\n";
			}
		}

		return ret;
	}

	public String setCacheSet(RVMVariable obj) {
		String ret = "";

		if (!hasSet)
			return ret;

		if (perthread) {
			ret += set + ".set(" + obj + ");\n";
		} else {
			ret += set + "[thisJoinPoint.getStaticPart().getId() & " + (size - 1) + "] = " + obj + ";\n";
		}

		return ret;
	}

	public String setCacheNode(RVMVariable obj) {
		String ret = "";

		if(!hasNode)
			return ret;
		
		if (perthread) {
			ret += node + ".set(" + obj + ");\n";
		} else {
			ret += node + "[thisJoinPoint.getStaticPart().getId() & " + (size - 1) + "] = " + obj + ";\n";
		}

		return ret;
	}

	public String init(){
		String ret = "";

		RVMVariable i = new RVMVariable("i");
		
		if(perthread)
			return ret;

		for (RVMParameter p : param) {
			RVMVariable key = keys.get(p.getName());
			ret += key + " = new " + getKeyType(p) + "[" + size + "];\n";
		}
		if (hasSet) {
			ret += set + " = new " + setType + "[" + size + "];\n";
		}
		if (hasNode) {
			ret += node + " = new " + nodeType + "[16];\n";
		}
		
		ret += "for(int " + i + " = 0; " + i + " < 16; " + i + "++){\n";
		for (RVMParameter p : param) {
			RVMVariable key = keys.get(p.getName());
			ret += key + "[" + i + "] = " + getTreeType(p) + ".NULRef;\n";
		}
		if (hasSet) {
			ret += set + "[" + i + "] " + " = null;\n";
		}
		if (hasNode) {
			ret += node + "[" + i + "] " + " = null;\n";
		}
		ret += "}\n";
		
		return ret;
	}
	
	public String toString() {
		String ret = "";

		if (perthread) {
			for (RVMParameter p : param) {
				RVMVariable key = keys.get(p.getName());

				ret += "static final ThreadLocal " + key + " = new ThreadLocal() {\n";
				ret += "protected " + getKeyType(p) + " initialValue(){\n";
				ret += "return " + getKeyType(p) + ".NULRef;\n";
				ret += "}\n";
				ret += "};\n";
			}
			
			if (hasSet) {
				ret += "static final ThreadLocal " + set + " = new ThreadLocal() {\n";
				ret += "protected " + setType + " initialValue(){\n";
				ret += "return null;\n";
				ret += "}\n";
				ret += "};\n";
			}

			if (hasNode) {
				ret += "static final ThreadLocal " + node + " = new ThreadLocal() {\n";
				ret += "protected " + nodeType + " initialValue(){\n";
				ret += "return null;\n";
				ret += "}\n";
				ret += "};\n";
			}
		} else {
			for (RVMParameter p : param) {
				RVMVariable key = keys.get(p.getName());
				ret += "static " + getKeyType(p) + "[] " + key + ";\n";
			}
			if (hasSet) {
				ret += "static " + setType + "[] " + set + ";\n";
			}
			if (hasNode) {
				ret += "static " + nodeType + "[] " + node + ";\n";
			}
		}

		return ret;
	}

}
