package rvmonitor.output.combinedaspect.indexingtree.decentralized;

import java.util.HashMap;

import rvmonitor.RVMException;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.RVMParameters;

public class NoParamIndexingTree extends rvmonitor.output.combinedaspect.indexingtree.centralized.NoParamIndexingTree {
	public NoParamIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);
	}
	
	
	//purely depends on NoParamIndexingTree in the centralized indexing tree package
}
