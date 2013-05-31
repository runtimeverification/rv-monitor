package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.centralized;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.java.rvj.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class CentralizedIndexingTree {

	static public IndexingTree defineIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {

		if(queryParam.size() == 0)
			return new NoParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
		
		if(queryParam.equals(fullParam) || queryParam.equals(contentParam))
			return new FullParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
		
		return new PartialParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
	}

}
