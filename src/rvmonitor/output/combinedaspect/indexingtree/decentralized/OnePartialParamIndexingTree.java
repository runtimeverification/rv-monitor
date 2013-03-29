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

public class OnePartialParamIndexingTree extends IndexingTree {
	RVMParameter firstKey;
	public RVMVariable oneParamNode;

	public OnePartialParamIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet,
			SuffixMonitor monitor, HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		if (anycontent) {
			this.name = new RVMVariable(aspectName + "_Set");
			if (isGeneral)
				this.oneParamNode = new RVMVariable(aspectName + "_Monitor");
		} else {
			this.name = new RVMVariable(aspectName + "__To__" + contentParam.parameterStringUnderscore() + "_Set");
		}

		this.firstKey = queryParam.get(0);
	}

	public boolean containsSet() {
		return true;
	}

	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
		String ret = "";

		if(oneParamNode != null){
			RVMVariable monitor = localVars.get(monitorStr);
			ret += monitor + " = " + retrieveOneParamMonitor() + ";\n";
		}

		return ret;
	}

	public String lookupSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
		String ret = "";

		RVMVariable lastSet = localVars.get(lastSetStr);

		if (creative){
			ret += createTree();
		}
		
		ret += lastSet + " = " + retrieveTree() + ";\n";

		return ret;
	}

	public String lookupNodeAndSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
		String ret = "";
		
		RVMVariable lastSet = localVars.get(lastSetStr);
		
		if (creative){
			ret += createTree();
		}

		ret += lastSet + " = " + retrieveTree() + ";\n";
		
		if(oneParamNode != null){
			RVMVariable monitor = localVars.get(monitorStr);
			ret += monitor + " = " + retrieveOneParamMonitor() + ";\n";
		}
		
		return ret;
	}

	public String attachNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) {
		String ret = "";

		if(oneParamNode != null){
			RVMVariable monitor = localVars.get(monitorStr);
			ret += retrieveOneParamMonitor() + " = " + monitor + ";\n";
		}

		return ret;
	}

	public String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) {
		String ret = "";

		RVMVariable lastSet = localVars.get(lastSetStr);
		ret += retrieveTree() + " = " + lastSet + ";\n";

		return ret;
	}

	public String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr) {
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);
		
		ret += createTree();
		ret += retrieveTree() + ".add(" + monitor + ");\n";

		return ret;
	}

	public String retrieveTree() {
		return firstKey.getName() + "." + name.toString();
	}

	public String retrieveOneParamMonitor() {
		if (oneParamNode == null)
			return "";

		return firstKey.getName() + "." + oneParamNode.toString();
	}
	
	protected String createTree() {
		String ret = "";
		
		ret += "if (" + retrieveTree() + " == null) {\n";
		ret += retrieveTree() + " = " + "new " + monitorSet.getName() + "()" + ";\n";
		ret += "}\n";

		return ret;
	}

	public String getRefTreeType() {
		String ret = "";

		if (parentTree != null)
			return parentTree.getRefTreeType();

		return ret;
	}

	public String toString() {
		String ret = "";

		ret += monitorSet.getName() + " " + firstKey.getType() + "." + name + " = null;\n";
		if (oneParamNode != null) {
			ret += monitorClass.getOutermostName() + " " + firstKey.getType() + "." + oneParamNode + " = null;\n";
		}

		if (cache != null)
			ret += cache;

		return ret;
	}

	public String reset() {
		String ret = "";

		ret += firstKey.getType() + "." + name + " = null;\n";
		if (oneParamNode != null) {
			ret += firstKey.getType() + "." + oneParamNode + " = null;\n";
		}

		if (cache != null)
			ret += cache.reset();

		return ret;
	}
}
