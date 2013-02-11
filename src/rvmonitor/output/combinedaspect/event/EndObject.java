package rvmonitor.output.combinedaspect.event;

import java.util.HashMap;

import rvmonitor.MOPException;
import rvmonitor.output.MOPVariable;
import rvmonitor.output.combinedaspect.CombinedAspect;
import rvmonitor.output.combinedaspect.GlobalLock;
import rvmonitor.output.combinedaspect.event.advice.AdviceBody;
import rvmonitor.output.combinedaspect.event.advice.GeneralAdviceBody;
import rvmonitor.output.combinedaspect.indexingtree.IndexingDecl;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.aspectj.TypePattern;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.JavaMOPSpec;
import rvmonitor.parser.ast.mopspec.MOPParameter;
import rvmonitor.parser.ast.mopspec.MOPParameters;

public class EndObject {
	JavaMOPSpec mopSpec;
	EventDefinition event;
	MonitorSet monitorSet;
	SuffixMonitor monitorClass;
	IndexingDecl indexingDecl;
	HashMap<MOPParameters, IndexingTree> indexingTrees;
	GlobalLock globalLock;

	String endObjectVar;
	TypePattern endObjectType;
	IndexingTree indexingTree;
	
	boolean isStart;
	AdviceBody eventBody = null;
	
	MOPVariable endObjectSupportType;

	public EndObject(JavaMOPSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws MOPException {
		if (!event.isEndObject())
			throw new MOPException("EndObject should be defined only for endObject pointcut.");

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
			throw new MOPException("The variable for an endObject pointcut is not defined.");
		this.endObjectSupportType = new MOPVariable(endObjectType.toString() + "MOPFinalized"); 
		
		this.isStart = event.isStartEvent();

		MOPParameter endParam = event.getMOPParametersOnSpec().getParam(event.getEndObjectVar());
		MOPParameters endParams = new MOPParameters();
		if (endParam != null)
			endParams.add(endParam);

		for (MOPParameters params : indexingTrees.keySet()) {
			if (endParams.equals(params))
				this.indexingTree = indexingTrees.get(params);
		}
		
		this.eventBody = new GeneralAdviceBody(mopSpec, event, combinedAspect);
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
