package com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.decentralized;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.event.advice.LocalVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class OneFullParamIndexingTree extends IndexingTree {
	private final RVMParameter firstKey;

	public OneFullParamIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		this.name = new RVMVariable(aspectName + "_Monitor");
		this.firstKey = queryParam.get(0);
	}

	public boolean containsSet() {
		return false;
	}

	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative, String monitorType) {
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);
		ret += monitor + " = " + retrieveTree() + ";\n";

		return ret;
	}

	public String lookupSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
		String ret = "";

		// do nothing

		return ret;
	}

	public String lookupNodeAndSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative, String monitorType) {
		return lookupNode(localVars, monitorStr, lastMapStr, lastSetStr, creative, monitorType);
	}

	public String attachNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) {
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);

		ret += retrieveTree() + " = " + monitor + ";\n";

		return ret;
	}

	public String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) {
		String ret = "";

		// do nothing

		return ret;
	}

	public String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr) {
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);
		ret += retrieveTree() + " = " + monitor + ";\n";

		return ret;
	}

	public String retrieveTree() {
		return firstKey.getName() + "." + name.toString();
	}

	public String getRefTreeType() {
		String ret = "";

		if (parentTree != null)
			return parentTree.getRefTreeType();

		return ret;
	}

	public String toString() {
		String ret = "";

		ret += monitorClass.getOutermostName() + " " + firstKey.getType() + "." + name + " = null;\n";

		if (cache != null)
			ret += cache;

		return ret;
	}

	public String reset() {
		String ret = "";

		ret += firstKey.getType() + "." + name + " = null;\n";

		if (cache != null)
			ret += cache.reset();

		return ret;
	}

}
