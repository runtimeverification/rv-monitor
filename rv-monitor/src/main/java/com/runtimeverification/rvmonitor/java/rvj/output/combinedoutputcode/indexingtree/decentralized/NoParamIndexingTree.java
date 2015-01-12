package com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.decentralized;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class NoParamIndexingTree extends com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.centralized.NoParamIndexingTree {
	public NoParamIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
	}
	
	
	//purely depends on NoParamIndexingTree in the centralized indexing tree package
}
