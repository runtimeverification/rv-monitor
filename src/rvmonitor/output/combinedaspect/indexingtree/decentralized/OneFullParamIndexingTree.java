package rvmonitor.output.combinedaspect.indexingtree.decentralized;

import java.util.HashMap;

import rvmonitor.RVMException;
import rvmonitor.output.RVMVariable;
import rvmonitor.output.combinedaspect.event.advice.LocalVariables;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTree;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.RVMParameter;
import rvmonitor.parser.ast.mopspec.RVMParameters;

public class OneFullParamIndexingTree extends IndexingTree {
	RVMParameter firstKey;

	public OneFullParamIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		this.name = new RVMVariable(aspectName + "_Monitor");
		this.firstKey = queryParam.get(0);
	}

	public boolean containsSet() {
		return false;
	}

	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
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

	public String lookupNodeAndSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
		return lookupNode(localVars, monitorStr, lastMapStr, lastSetStr, creative);
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
