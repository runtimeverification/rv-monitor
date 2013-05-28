package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree;

import java.util.ArrayList;
import java.util.HashMap;

import com.runtimeverification.rvmonitor.java.rvj.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice.LocalVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public abstract class IndexingTree {
	public RVMVariable name;
	
	public RVMParameters fullParam;
	public RVMParameters queryParam;
	public RVMParameters contentParam;
	
	public MonitorSet monitorSet;
	public SuffixMonitor monitorClass;
	
	public IndexingCache cache = null;
	
	public boolean anycontent = true;
	public boolean perthread = false;
	public boolean isFullParam = false;
	public boolean isGeneral = false;
	
	HashMap<String, RefTree> refTrees;
	
	public RefTree parasiticRefTree = null;
	
	public IndexingTree parentTree = null;
	public ArrayList<IndexingTree> childTrees = new ArrayList<IndexingTree>();

	public IndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor, HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) {
		this.queryParam = queryParam;
		this.contentParam = contentParam;
		this.fullParam = fullParam;
		this.monitorSet = monitorSet;
		this.monitorClass = monitor;

		if (contentParam == null) {
			anycontent = true;
		} else {
			anycontent = false;
		}
		
		if (queryParam != null && fullParam != null && queryParam.equals(fullParam))
			isFullParam = true;
		if (queryParam != null && contentParam != null && queryParam.equals(contentParam))
			isFullParam = true;

		
		this.perthread = perthread;
		this.isGeneral = isGeneral;
	}

	public RVMVariable getName() {
		return name;
	}

	public boolean hasCache() {
		return cache != null;
	}
	
	public IndexingCache getCache(){
		return cache;
	}

	public abstract boolean containsSet();

	/*
	 * lookupNode, lookupSet, lookupNodeAndSet retrieve data from indexing tree
	 * They can use the following local variables if necessary: obj, m, and tempRef_*. 
	 */
	public abstract String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) throws RVMException;

	public abstract String lookupSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) throws RVMException;

	public abstract String lookupNodeAndSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) throws RVMException;

	public abstract String attachNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) throws RVMException;

	public abstract String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) throws RVMException;

	public String addMonitor(LocalVariables localVars, String monitorStr) throws RVMException {
		return addMonitor(localVars, monitorStr, "tempMap", "monitors");
	}

	public abstract String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr) throws RVMException;

	public abstract String retrieveTree();

	public abstract String getRefTreeType();

	public abstract String toString();

	public abstract String reset();

	////////////////////////
	/*
	public abstract String retrieveTree();
	public abstract String addMonitor(RVMVariable map, RVMVariable obj, RVMVariable monitors, HashMap<String, RVMVariable> mopRefs, RVMVariable monitor);
	public abstract String getWeakReferenceAfterLookup(RVMVariable map, RVMVariable monitor, HashMap<String, RVMVariable> mopRefs);
	public abstract String addMonitorAfterLookup(RVMVariable map, RVMVariable monitor, HashMap<String, RVMVariable> mopRefs);
	public abstract String addExactWrapper(RVMVariable wrapper, RVMVariable lastMap, RVMVariable set, HashMap<String, RVMVariable> mopRefs);
	public abstract String addWrapper(RVMVariable wrapper, RVMVariable lastMap, RVMVariable set, HashMap<String, RVMVariable> mopRefs);
	public abstract String lookup(RVMVariable map, RVMVariable obj, HashMap<String, RVMVariable> tempRefs, boolean creative);
	public abstract String lookupExactMonitor(RVMVariable wrapper, RVMVariable lastMap, RVMVariable set, RVMVariable map, RVMVariable obj,
			HashMap<String, RVMVariable> tempRefs, boolean creative);
	 */
}
