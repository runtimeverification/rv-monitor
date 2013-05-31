package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.CombinedAspect;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.RVMonitorStatistics;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingDecl;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

import java.util.HashMap;

public abstract class AdviceBody {
	RVMonitorSpec mopSpec;
	public EventDefinition event;
	public MonitorSet monitorSet;
	public SuffixMonitor monitorClass;
	public RVMVariable monitorName;
	public HashMap<RVMParameters, IndexingTree> indexingTrees;
	public IndexingDecl indexingDecl;
	public HashMap<String, RefTree> refTrees;
	
	public RVMonitorStatistics stat;
	
	public boolean isGeneral;
	RVMParameters eventParams;

	public boolean isFullParam;
	CombinedAspect aspect;
	
	public AdviceBody(RVMonitorSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) {
		this.mopSpec = mopSpec;
		this.aspect = combinedAspect;
		this.event = event;
		this.eventParams = event.getRVMParametersOnSpec();
		this.monitorSet = combinedAspect.monitorSets.get(mopSpec);
		this.monitorClass = combinedAspect.monitors.get(mopSpec);
		this.monitorClass.setAspectName(combinedAspect.getAspectName());
		this.monitorName = monitorClass.getOutermostName();
		this.indexingDecl = combinedAspect.indexingTreeManager.getIndexingDecl(mopSpec);
		this.indexingTrees = indexingDecl.getIndexingTrees();
		this.stat = combinedAspect.statManager.getStat(mopSpec);
		this.refTrees = combinedAspect.indexingTreeManager.refTrees;
		this.isGeneral = mopSpec.isGeneral();
		this.isFullParam = eventParams.equals(mopSpec.getParameters());
	}

	public abstract String toString();
}
