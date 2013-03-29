package rvmonitor.output.combinedaspect.event.advice;

import rvmonitor.output.RVMVariable;
import rvmonitor.output.combinedaspect.CombinedAspect;
import rvmonitor.output.combinedaspect.RVMonitorStatistics;
import rvmonitor.output.combinedaspect.indexingtree.IndexingDecl;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTree;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.mopspec.RVMParameters;

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
