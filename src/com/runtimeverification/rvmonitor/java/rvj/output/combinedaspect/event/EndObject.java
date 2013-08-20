package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.CombinedAspect;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice.AdviceBody;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingDeclNew;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeNew;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.TypePattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class EndObject {
	RVMonitorSpec mopSpec;
	EventDefinition event;
	MonitorSet monitorSet;
	SuffixMonitor monitorClass;
	IndexingDeclNew indexingDecl;
	HashMap<RVMParameters, IndexingTreeNew> indexingTrees;
	GlobalLock globalLock;

	String endObjectVar;
	TypePattern endObjectType;
	IndexingTreeNew indexingTree;
	
	boolean isStart;
	AdviceBody eventBody = null;
	
	RVMVariable endObjectSupportType;

	public EndObject(RVMonitorSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {
		if (!event.isEndObject())
			throw new RVMException("EndObject should be defined only for endObject pointcut.");

		this.mopSpec = mopSpec;
		this.event = event;
		this.monitorSet = combinedAspect.monitorSets.get(mopSpec);
		this.monitorClass = combinedAspect.monitors.get(mopSpec);
		this.indexingDecl = combinedAspect.indexingTreeManager.getIndexingDecl(mopSpec);
		this.indexingTrees = indexingDecl.getIndexingTrees();
		this.globalLock = combinedAspect.lockManager.getLock();

		this.endObjectType = event.getEndObjectType();
		this.endObjectVar = event.getEndObjectVar();
		if (this.endObjectVar == null || this.endObjectVar.length() == 0)
			throw new RVMException("The variable for an endObject pointcut is not defined.");
		this.endObjectSupportType = new RVMVariable(endObjectType.toString() + "RVMFinalized");
		
		this.isStart = event.isStartEvent();

		RVMParameter endParam = event.getRVMParametersOnSpec().getParam(event.getEndObjectVar());
		RVMParameters endParams = new RVMParameters();
		if (endParam != null)
			endParams.add(endParam);

		for (RVMParameters params : indexingTrees.keySet()) {
			if (endParams.equals(params))
				this.indexingTree = indexingTrees.get(params);
		}
		
		this.eventBody = AdviceBody.createAdviceBody(mopSpec, event, combinedAspect);
	}

	public String printDecl() {
		String ret = "";

		ret += "public static abstract class " + endObjectSupportType + "{\n";
		ret += "protected void finalize() throws Throwable{\n";
		ret += "try {\n";
		ret += endObjectType + " " + endObjectVar + " = (" + endObjectType + ")this;\n";
		ret += eventBody;
		ret += "} finally {\n";
		ret += "super.finalize();\n";
		ret += "}\n";
		ret += "}\n"; //method
		ret += "}\n"; //abstract class
		ret += "\n";
		
		ret += "declare parents : " + endObjectType + " extends " + endObjectSupportType + ";\n";
		ret += "\n";
		
		ret += "after(" + endObjectType + " " + endObjectVar + ") : execution(void " + endObjectType + ".finalize()) && this(" + endObjectVar + "){\n";
		ret += eventBody;
		ret += "}\n";

		return ret;
	}


}
