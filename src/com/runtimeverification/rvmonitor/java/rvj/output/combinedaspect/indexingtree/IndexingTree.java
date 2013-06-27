package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree;

import java.util.ArrayList;
import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
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
	
	protected IndexingTreeType getTreeType() {
		if (this.parentTree != null)
			return this.parentTree.getTreeType();
		return this.inferTreeType(this.queryParam, this.isFullParam);
	}
	
	protected IndexingTreeType getMonitorType() {
		return IndexingTreeType.fromSimpleType(this.monitorClass.getOutermostName().toString());
	}
	
	protected IndexingTreeType getMonitorSetType() {
		return IndexingTreeType.fromSimpleType(this.monitorSet.getName().toString());
	}
	
	private IndexingTreeType inferTreeType(RVMParameters params, boolean full) {
		IndexingTreeType type = new IndexingTreeType();
		
		boolean m = false, s = false, l = false;
		
		if (this.isGeneral) {
			if (params.size() == 1) {
				l = true;
				if (!full)
					s = true;
			}
			else {
				m = true;
				s = true;
				l = true;
			}
		}
		else {
			if (params.size() == 1) {
				if (full)
					l = true;
				else
					s = true;
			}
			else {
				m = true;
				s = true;
			}
		}
		
		if (m) {
			IndexingTreeType nested = this.inferTreeType(params.tail(), full);
			type.enableMap(nested);
		}
		if (s)
			type.enableSet(this.monitorSet.getName().toString());
		if (l)
			type.enableLeaf(this.monitorClass.getOutermostName().toString());
		
		if (this.parasiticRefTree == null)
			type.setRefType(-1);
		else
			type.setRefType(this.parasiticRefTree.generalProperties.size());
	
		type.fix();
		return type;
	}
	
	public static enum IndexingTreeInterface {
		Map,
		Set,
		Leaf,
	}
	
	public static class IndexingTreeType {
		private int weakreftag = -1;
		private String clsname;
		private IndexingTreeType map;
		private IndexingTreeType set;
		private IndexingTreeType leaf;
		
		/*
		public IndexingTreeType getNestedMap() {
			return this.map;
		}
		
		public IndexingTreeType getNestedSet() {
			return this.set;
		}
		
		public IndexingTreeType getNestedLeaf() {
			return this.leaf;
		}
		*/
		
		public IndexingTreeType getPart(IndexingTreeInterface itf) {
			switch (itf) {
			case Map:
				return this.map;
			case Set:
				return this.set;
			case Leaf:
				return this.leaf;
			}
			return null;
		}
		
		public String inferClassName() {
			String str = "";
//			str += "com.runtimeverification.rvmonitor.java.rt.table.";
			
			switch (this.weakreftag) {
			case -1:
				break;
			case 0:
				str += "BasicRef";
				break;
			case 1:
				str += "TagRef";
				break;
			default:
				str += "MultiTagRef";
				break;
			}
			str += "MapOf";
			
			boolean m = this.map != null;
			boolean s = this.set != null;
			boolean l = this.leaf != null;
			if (m && s && l)
				str += "All";
			else if (m && s)
				str += "MapSet";
			else if (s && l)
				str += "SetMonitor";
			else if (m)
				str += "Map";
			else if (s)
				str += "Set";
			else if (l)
				str += "Monitor";
			else
				return null;
			return str;
		}
		
		public void setRefType(int weakreftag) {
			this.weakreftag = weakreftag;
		}
		
		public void enableMap(IndexingTreeType nested) {
			this.map = nested;
		}
		
		public void enableSet(String settype) {
			this.set = fromSimpleType(settype);
		}
		
		public void enableLeaf(String leaftype) {
			this.leaf = fromSimpleType(leaftype);
		}
		
		private static IndexingTreeType fromSimpleType(String clsname) {
			IndexingTreeType type = new IndexingTreeType();
			type.clsname = clsname;
			return type;
		}
		
		public void fix() {
			String name = this.inferClassName();
			if (name != null)
				this.clsname = name;
		}
		
		@Override
		public String toString() {
			String str = this.clsname;
			
			boolean m = this.map != null;
			boolean s = this.set != null;
			boolean l = this.leaf != null;
			if (m && s && l)
				str += "<" + this.map + ", " + this.set + ", " + this.leaf + ">";
			else if (m && s)
				str += "<" + this.map + ", " + this.set + ">";
			else if (s && l)
				str += "<" + this.set + ", " + this.leaf + ">";
			else if (m)
				str += "<" + this.map + ">";
			else if (s)
				str += "<" + this.set + ">";
			else if (l)
				str += "<" + this.leaf + ">";
			return str;
		}
	}
}
