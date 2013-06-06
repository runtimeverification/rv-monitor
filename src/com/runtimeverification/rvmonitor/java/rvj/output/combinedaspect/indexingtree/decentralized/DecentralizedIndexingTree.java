package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.decentralized;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class DecentralizedIndexingTree {

	static public IndexingTree defineIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet,
			SuffixMonitor monitor, HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {

		if (perthread)
			throw new RVMException("decentralized perthread specification is not supported");

		if (queryParam.size() == 0)
			return new NoParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		if (queryParam.size() == 1) {
			if (fullParam.size() == 1)
				return new OneFullParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
			else
				return new OnePartialParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
		}

		if (queryParam.equals(fullParam) || queryParam.equals(contentParam))
			return new FullParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		return new PartialParamIndexingTree(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
	}
}
