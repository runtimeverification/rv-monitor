package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.CombinedAspect;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.RVMonitorStatistics;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.itf.EventMethodBody;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingDeclNew;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeNew;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;
import com.runtimeverification.rvmonitor.util.RVMException;

import java.util.HashMap;

public abstract class AdviceBody {
	protected RVMonitorSpec mopSpec;
	protected EventDefinition event;
	protected MonitorSet monitorSet;
	protected SuffixMonitor monitorClass;
	protected RVMVariable monitorName;
	protected HashMap<RVMParameters, IndexingTreeNew> indexingTrees;
	protected IndexingDeclNew indexingDecl;
	protected HashMap<String, RefTree> refTrees;
	
	protected RVMonitorStatistics stat;
	
	protected boolean isGeneral;
	protected RVMParameters eventParams;

	protected boolean isFullParam;
	protected CombinedAspect aspect;
	
	public RVMParameters getEventParameters() {
		return this.eventParams;
	}
	
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
	public abstract void generateCode();
	
	public static AdviceBody createAdviceBody(RVMonitorSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {
//		return new GeneralAdviceBody(mopSpec, event, combinedAspect);
		return new EventMethodBody(mopSpec, event, combinedAspect);
	}
}
