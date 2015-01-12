package com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.event.advice;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMonitorStatistics;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.CombinedOutput;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.event.itf.EventMethodBody;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.newindexingtree.IndexingDeclNew;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedoutputcode.newindexingtree.IndexingTreeInterface;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;
import com.runtimeverification.rvmonitor.util.RVMException;

import java.util.TreeMap;

public abstract class AdviceBody {
	protected final RVMonitorSpec mopSpec;
	protected final EventDefinition event;
	protected final MonitorSet monitorSet;
	protected final SuffixMonitor monitorClass;
	protected final RVMVariable monitorName;
	protected final TreeMap<RVMParameters, IndexingTreeInterface> indexingTrees;
	protected final IndexingDeclNew indexingDecl;
	protected final TreeMap<String, RefTree> refTrees;
	
	protected final RVMonitorStatistics stat;
	
	protected final boolean isGeneral;
	protected final RVMParameters eventParams;

	protected final boolean isFullParam;
	protected final CombinedOutput output;
	
	public RVMParameters getEventParameters() {
		return this.eventParams;
	}
	
	public AdviceBody(RVMonitorSpec mopSpec, EventDefinition event, CombinedOutput combinedOutput) {
		this.mopSpec = mopSpec;
		this.output = combinedOutput;
		this.event = event;
		this.eventParams = event.getRVMParametersOnSpec();
		this.monitorSet = combinedOutput.monitorSets.get(mopSpec);
		this.monitorClass = combinedOutput.monitors.get(mopSpec);
		this.monitorClass.setAspectName(combinedOutput.getName());
		this.monitorName = monitorClass.getOutermostName();
		this.indexingDecl = combinedOutput.indexingTreeManager.getIndexingDecl(mopSpec);
		this.indexingTrees = indexingDecl.getIndexingTrees();
		this.stat = combinedOutput.statManager.getStat(mopSpec);
		this.refTrees = combinedOutput.indexingTreeManager.refTrees;
		this.isGeneral = mopSpec.isGeneral();
		this.isFullParam = eventParams.equals(mopSpec.getParameters());
	}

	public abstract String toString();
	public abstract void generateCode();
	
	public static AdviceBody createAdviceBody(RVMonitorSpec mopSpec, EventDefinition event, CombinedOutput combinedAspect) throws RVMException {
//		return new GeneralAdviceBody(mopSpec, event, combinedAspect);
		return new EventMethodBody(mopSpec, event, combinedAspect);
	}
}
