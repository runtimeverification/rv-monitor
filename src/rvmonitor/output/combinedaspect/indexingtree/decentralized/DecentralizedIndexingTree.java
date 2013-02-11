package rvmonitor.output.combinedaspect.indexingtree.decentralized;

import java.util.HashMap;

import rvmonitor.MOPException;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTree;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.MOPParameters;

public class DecentralizedIndexingTree {

	static public IndexingTree defineIndexingTree(String aspectName, MOPParameters queryParam, MOPParameters contentParam, MOPParameters fullParam, MonitorSet monitorSet,
			SuffixMonitor monitor, HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws MOPException {

		if (perthread)
			throw new MOPException("decentralized perthread specification is not supported");

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
