package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.NotImplementedException;
import com.runtimeverification.rvmonitor.java.rvj.output.EnableSet;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMemberField;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeFormatters;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.RuntimeMonitorType;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.*;

import java.util.ArrayList;
import java.util.HashMap;

public class IndexingDeclNew {
	RVMonitorSpec mopSpec;
	RVMParameters specParam;
	HashMap<RVMParameters, IndexingTreeNew> indexingTrees = new HashMap<RVMParameters, IndexingTreeNew>();
	HashMap<RVMonitorParameterPair, IndexingTreeNew> indexingTreesForCopy = new HashMap<RVMonitorParameterPair, IndexingTreeNew>();

	HashMap<EventDefinition, ArrayList<RVMonitorParameterPair>> mapEventToCopyParams = new HashMap<EventDefinition, ArrayList<RVMonitorParameterPair>>();

	HashMap<String, RefTree> refTrees;

	MonitorSet monitorSet;
	SuffixMonitor monitor;

	public RVMParameters endObjectParameters = new RVMParameters();

	public IndexingDeclNew(RVMonitorSpec mopSpec, MonitorSet monitorSet, SuffixMonitor monitor, EnableSet enableSet, HashMap<String, RefTree> refTrees) throws RVMException {
		this.mopSpec = mopSpec;
		this.specParam = mopSpec.getParameters();
		this.refTrees = refTrees;

		RVMParameterSet indexingParameterSet = new RVMParameterSet();
		RVMParameterPairSet indexingRestrictedParameterSet = new RVMParameterPairSet();

		for (EventDefinition event : mopSpec.getEvents()) {
			if (event.isEndObject() && event.getRVMParameters().size() != 0)
				endObjectParameters.addAll(event.getRVMParameters());
		}

		for (EventDefinition event : mopSpec.getEvents()) {
			RVMParameters param = event.getRVMParametersOnSpec();

			indexingParameterSet.add(param);

			if (event.isEndObject()) {
				RVMParameter endParam = param.getParam(event.getEndObjectVar());
				RVMParameters endParams = new RVMParameters();
				if (endParam != null) {
					endParams.add(endParam);
				}
				indexingParameterSet.add(endParams);
			}
		}

		if(mopSpec.isGeneral()){
			for (EventDefinition event : mopSpec.getEvents()) {
				ArrayList<RVMonitorParameterPair> pairs = new ArrayList<RVMonitorParameterPair>();
	
				RVMParameters param = event.getRVMParametersOnSpec();
				RVMParameterSet enable = enableSet.getEnable(event.getId());
	
				for (RVMParameters enableEntity : enable) {
					if (enableEntity.size() == 0 && !mopSpec.hasNoParamEvent()) {
						continue;
					}
	
					RVMParameters unionOfEnableEntityAndParam = RVMParameters.unionSet(enableEntity, param);
					unionOfEnableEntityAndParam = specParam.sortParam(unionOfEnableEntityAndParam);
	
					if (!enableEntity.contains(param)) {
						RVMParameters intersectionOfEnableEntityAndParam = RVMParameters.intersectionSet(enableEntity, param);
						intersectionOfEnableEntityAndParam = specParam.sortParam(intersectionOfEnableEntityAndParam);
	
						RVMonitorParameterPair paramPair = new RVMonitorParameterPair(intersectionOfEnableEntityAndParam, enableEntity);
						if (!param.contains(enableEntity)) {
							indexingRestrictedParameterSet.add(paramPair);
							indexingParameterSet.add(unionOfEnableEntityAndParam);
						} else {
							if (!indexingParameterSet.contains(enableEntity)) {
								indexingRestrictedParameterSet.add(paramPair);
							}
						}
						pairs.add(paramPair);
					}
				}
	
				mapEventToCopyParams.put(event, pairs);
			}
		}

		if (mopSpec.isCentralized()) {
			for (RVMParameters param : indexingParameterSet) {
				if (param.size() == 1 && this.endObjectParameters.getParam(param.get(0).getName()) != null) {
					throw new NotImplementedException();
					/*
					IndexingTree indexingTree = DecentralizedIndexingTree.defineIndexingTree(mopSpec.getName(), param, null, specParam, monitorSet, monitor, refTrees,
							mopSpec.isPerThread(), mopSpec.isGeneral());
					indexingTrees.put(param, indexingTree);
					*/
				} else {
					IndexingTreeNew.EventKind evttype = this.calculateEventType(param);
					boolean needsTimeTracking = mopSpec.isGeneral();
					IndexingTreeNew indexingTree = new IndexingTreeNew(mopSpec.getName(), specParam, param, null, monitorSet, monitor, evttype, needsTimeTracking);
					/*
					IndexingTree indexingTree = CentralizedIndexingTree.defineIndexingTree(mopSpec.getName(), param, null, specParam, monitorSet, monitor, refTrees,
							mopSpec.isPerThread(), mopSpec.isGeneral());
							*/
					indexingTrees.put(param, indexingTree);
				}
			}
			
			if (mopSpec.isGeneral()) {
				for (RVMonitorParameterPair paramPair : indexingRestrictedParameterSet) {
					/*
					indexingTreesForCopy.put(paramPair, CentralizedIndexingTree.defineIndexingTree(mopSpec.getName(), paramPair.getParam1(), paramPair.getParam2(), specParam,
							monitorSet, monitor, refTrees, mopSpec.isPerThread(), mopSpec.isGeneral()));
							*/
					IndexingTreeNew.EventKind evttype = IndexingTreeNew.EventKind.AlwaysCreate;
					boolean needsTimeTracking = mopSpec.isGeneral();
					IndexingTreeNew indexingTree = new IndexingTreeNew(mopSpec.getName(), specParam, paramPair.getParam1(), paramPair.getParam2(), monitorSet, monitor, evttype, needsTimeTracking);
					indexingTreesForCopy.put(paramPair, indexingTree);
				}
			}

//			combineCentralIndexingTrees();
			
//			combineRefTreesIntoIndexingTrees();
		} else {
			/* TODO: Decentralized RefTree which does not require any mapping.
			
			for (RVMParameters param : indexingParameterSet) {
				IndexingTree indexingTree = DecentralizedIndexingTree.defineIndexingTree(mopSpec.getName(), param, null, specParam, monitorSet, monitor, refTrees,
						mopSpec.isPerThread(), mopSpec.isGeneral());

				indexingTrees.put(param, indexingTree);
			}
			if (mopSpec.isGeneral()) {
				for (RVMonitorParameterPair paramPair : indexingRestrictedParameterSet) {
					IndexingTree indexingTree = DecentralizedIndexingTree.defineIndexingTree(mopSpec.getName(), paramPair.getParam1(), paramPair.getParam2(), specParam,
							monitorSet, monitor, refTrees, mopSpec.isPerThread(), mopSpec.isGeneral());

					indexingTreesForCopy.put(paramPair, indexingTree);
				}
			}
			*/
			throw new NotImplementedException();
		}
	}
	
	private IndexingTreeNew.EventKind calculateEventType(RVMParameters treeParams) {
		int numCreationEvent = 0;
		int numNonCreationEvent = 0;
	
		for (EventDefinition event : this.mopSpec.getEvents()) {
			RVMParameters param = event.getRVMParametersOnSpec();
			if (param.equals(treeParams)) {
				if (event.isStartEvent())
					numCreationEvent++;
				else
					numNonCreationEvent++;
			}
		}
		
		if (numCreationEvent > 0) {
			if (numNonCreationEvent == 0)
				return IndexingTreeNew.EventKind.AlwaysCreate;
			else
				return IndexingTreeNew.EventKind.MayCreate;
		}
		return IndexingTreeNew.EventKind.NeverCreate;
	}

	public HashMap<RVMParameters, IndexingTreeNew> getIndexingTrees() {
		return indexingTrees;
	}

	public HashMap<RVMonitorParameterPair, IndexingTreeNew> getIndexingTreesForCopy() {
		return indexingTreesForCopy;
	}

	public ArrayList<RVMonitorParameterPair> getCopyParamForEvent(EventDefinition e) {
		return mapEventToCopyParams.get(e);
	}

	/*
	protected void combineCentralIndexingTrees() {
		if (!mopSpec.isCentralized())
			return;

		for (IndexingTreeNew indexingTree : indexingTrees.values()) {
			if (indexingTree.parentTree == null) {
				RVMParameters sortedParam = specParam.sortParam(indexingTree.queryParam);

				treeSearch: for (IndexingTreeNew indexingTree2 : indexingTrees.values()) {
					if (indexingTree == indexingTree2)
						continue;

					if (!indexingTree2.queryParam.contains(indexingTree.queryParam))
						continue;

					RVMParameters sortedParam2 = specParam.sortParam(indexingTree2.queryParam);

					for (int i = 0; i < sortedParam.size(); i++) {
						if (!sortedParam.get(i).equals(sortedParam2.get(i)))
							continue treeSearch;
					}

					// System.out.println(sortedParam + " -> " + sortedParam2);

					IndexingTreeNew host = indexingTree2;
					while (host.parentTree != null) {
						host = host.parentTree;
					}

					indexingTree.parentTree = host;
					if (indexingTree.parasiticRefTree != null) {
						host.parasiticRefTree = indexingTree.parasiticRefTree;
						host.parasiticRefTree.setHost(host);
						indexingTree.parasiticRefTree = null;
					}

					if (indexingTree.childTrees.size() > 0) {
						host.childTrees.addAll(indexingTree.childTrees);
						indexingTree.childTrees = new ArrayList<IndexingTree>();
					}

					break;
				}
			}
		}
	}
	
	protected void combineRefTreesIntoIndexingTrees(){
		if(mopSpec.isPerThread())
			return;
		
		if(mopSpec.isGeneral())
			return;
		
		for (RVMParameters param : indexingTrees.keySet()) {
			if (param.size() == 1 && this.endObjectParameters.getParam(param.get(0).getName()) != null)
				continue;
		
			IndexingTreeNew indexingTree = indexingTrees.get(param);
			
			if (indexingTree.parentTree == null && param.size() == 1) {
				RVMParameter p = param.get(0);
				RefTree refTree = refTrees.get(p.getType().toString());
				
				if (refTree.generalProperties.size() == 0 && refTree.hostIndexingTree == null) {
					refTree.setHost(indexingTree);
					indexingTree.parasiticRefTree = refTree;
				}
			}
		}
	}
	*/

	public String toString() {
		ICodeFormatter fmt = CodeFormatters.getDefault();

		for (IndexingTreeNew indexingTree : indexingTrees.values())
			indexingTree.getCode(fmt);

		for (IndexingTreeNew indexingTree : indexingTreesForCopy.values())
			indexingTree.getCode(fmt);

		return fmt.getCode();
	}

	public String reset() {
		return "";
	}
	
	private String getDescriptionCode(IndexingTreeNew tree) {
		String ret = "";
		
		CodeType type = tree.getCodeType();
		if (type instanceof RuntimeMonitorType.IndexingTree) {
			CodeMemberField field = tree.getField();
			ret += field.getName();
			ret += ".";
			ret += "setObservableObjectDescription(\"";
			ret += tree.getPrettyName();
			ret += "\");\n";
		}
		return ret;
	}

	public String getObservableObjectDescriptionSetCode() {
		String ret = "";
		for (IndexingTreeNew indexingTree : indexingTrees.values())
			ret += this.getDescriptionCode(indexingTree);

		for (IndexingTreeNew indexingTree : indexingTreesForCopy.values())
			ret += this.getDescriptionCode(indexingTree);
		return ret;
	}
}
