package rvmonitor.output.combinedaspect.indexingtree;

import java.util.HashMap;

import rvmonitor.output.RVMVariable;
import rvmonitor.output.combinedaspect.event.advice.LocalVariables;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.RVMParameter;
import rvmonitor.parser.ast.mopspec.RVMParameters;

public class IndexingCache {
	RVMParameters param;
	boolean perthread = false;
	boolean isGeneral = false;
	SuffixMonitor monitor;

	public boolean hasSet = false;
	public boolean hasNode = false;
	RVMVariable setType;
	RVMVariable set;

	RVMVariable nodeType;
	RVMVariable node;

	HashMap<String, RVMVariable> keys = new HashMap<String, RVMVariable>();
	HashMap<String, RefTree> refTrees;

	public IndexingCache(RVMVariable name, RVMParameters param, RVMParameters fullParam, SuffixMonitor monitor, MonitorSet monitorSet, HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) {
		this.param = param;
		this.perthread = perthread;
		this.monitor = monitor;
		this.isGeneral = isGeneral;

		for (int i = 0; i < param.size(); i++) {
			this.keys.put(param.get(i).getName(), new RVMVariable(name + "_cachekey_" + fullParam.getIdnum(param.get(i))));
		}

		this.hasSet = !fullParam.equals(param);
		this.hasNode = !hasSet || isGeneral;
		
		this.setType = monitorSet.getName();
		this.set = new RVMVariable(name + "_cacheset");
		this.nodeType = monitor.getOutermostName();
		this.node = new RVMVariable(name + "_cachenode");
		this.refTrees = refTrees;
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
				ret += param.get(i).getName() + " == " + "((" + getKeyType(param.get(i)) + ")" + getKey(i) + ".get()).get()";
			} else {
				ret += param.get(i).getName() + " == " + getKey(i) + ".get()";
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
				ret += "(" + getKeyType(p) + ")" + getKey(p) + ".get();\n";
			} else {
				ret += getKey(p) + ";\n";
			}
		}

		return ret;
	}

	public String getCacheSet(RVMVariable obj) {
		String ret = "";

		if (!hasSet)
			return ret;

		if (perthread) {
			ret += obj + " = " + "(" + setType + ")" + set + ".get();\n";
		} else {
			ret += obj + " = " + set + ";\n";
		}

		return ret;
	}

	public String getCacheNode(RVMVariable obj) {
		String ret = "";

		if (perthread) {
			ret += obj + " = " + "(" + nodeType + ")" + node + ".get();\n";
		} else {
			ret += obj + " = " + node + ";\n";
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
				ret += getKey(p) + " = " + tempRef + ";\n";
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
			ret += set + " = " + obj + ";\n";
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
			ret += node + " = " + obj + ";\n";
		}

		return ret;
	}
	
	public String init(){
		return "";
	}

	public String toString() {
		String ret = "";

		if (perthread) {
			for (RVMParameter p : param) {
				RVMVariable key = keys.get(p.getName());

				ret += "static final ThreadLocal " + key + " = new ThreadLocal() {\n";
				ret += "protected " + getKeyType(p) + " initialValue(){\n";
				ret += "return " + getTreeType(p) + ".NULRef;\n";
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
				ret += "static " + getKeyType(p) + " " + key + " = " + getTreeType(p) + ".NULRef;\n";
			}
			if (hasSet) {
				ret += "static " + setType + " " + set + " = null;\n";
			}
			if (hasNode) {
				ret += "static " + nodeType + " " + node + " = null;\n";
			}
		}

		return ret;
	}

	public String reset() {
		String ret = "";

		if (perthread) {
			for (RVMParameter p : param) {
				RVMVariable key = keys.get(p.getName());

				ret += key + " = new ThreadLocal() {\n";
				ret += "protected " + getKeyType(p) + " initialValue(){\n";
				ret += "return " + getTreeType(p) + ".NULRef;\n";
				ret += "}\n";
				ret += "};\n";
			}
			
			if (hasSet) {
				ret += set + " = new ThreadLocal() {\n";
				ret += "protected " + setType + " initialValue(){\n";
				ret += "return null;\n";
				ret += "}\n";
				ret += "};\n";
			}

			if (hasNode) {
				ret += node + " = new ThreadLocal() {\n";
				ret += "protected " + nodeType + " initialValue(){\n";
				ret += "return null;\n";
				ret += "}\n";
				ret += "};\n";
			}
		} else {
			for (RVMParameter p : param) {
				RVMVariable key = keys.get(p.getName());
				ret += key + " = " + getTreeType(p) + ".NULRef;\n";
			}
			if (hasSet) {
				ret += set + " = null;\n";
			}
			if (hasNode) {
				ret += node + " = null;\n";
			}
		}

		return ret;
	}

}
