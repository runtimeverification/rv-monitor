package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice;

/*
import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.CombinedAspect;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.GlobalLock;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.RVMonitorStatManager;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingCache;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeNew;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.BaseMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.MonitorInfo;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GeneralAdviceBody extends AdviceBody {
	public RVMonitorStatManager statManager;
	
	public IndexingTreeNew indexingTree;
	public IndexingCache cache = null;

	public HashMap<RVMonitorParameterPair, IndexingTreeNew> indexingTreesForCopy;
	public ArrayList<RVMonitorParameterPair> paramPairsForCopy;

	RVMVariable timestamp;

	public HashSet<RVMParameter> parametersForDisable;

	boolean isFullBinding;
	boolean isConnected;
	MonitorInfo monitorInfo;

	LocalVariables localVars;
	
	boolean doDisable = false;
	
	GlobalLock lock;
	
	String aspectName;

	// assumes: mopSpec.getParameters().size() != 0
	GeneralAdviceBody(RVMonitorSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {
		super(mopSpec, event, combinedAspect);

		this.isFullBinding = mopSpec.isFullBinding();
		this.isConnected = mopSpec.isConnected();

		this.monitorInfo = monitorClass.getMonitorInfo();

		this.timestamp = combinedAspect.timestampManager.getTimestamp(mopSpec);
		this.indexingTree = indexingTrees.get(eventParams);
		this.cache = this.indexingTree.getCache();
		this.parametersForDisable = combinedAspect.setOfParametersForDisable.get(mopSpec);

		this.indexingTreesForCopy = indexingDecl.getIndexingTreesForCopy();

		this.localVars = new LocalVariables(mopSpec, event, combinedAspect);

		this.paramPairsForCopy = indexingDecl.getCopyParamForEvent(event);
		
		this.doDisable = doDisable();
		
		this.statManager = combinedAspect.statManager;
		this.aspectName = combinedAspect.getAspectName();
		lock = new GlobalLock(new RVMVariable(this.aspectName + "." + combinedAspect.lockManager.getLock().getName()));
	}

	public boolean doDisable(){
		for (RVMParameter p : eventParams) {
			if (parametersForDisable.contains(p))
				return true;
		}

		return false;
	}
	
	// opt done
	public RefTree getRefTree(RVMParameter p) {
		return refTrees.get(p.getType().toString());
	}

	// opt done
	public boolean isUsingMonitor() {
		if (!indexingTree.containsSet())
			return true;

		if (event.isStartEvent())
			return true;

		if (paramPairsForCopy != null && paramPairsForCopy.size() > 0)
			return true;

		if (doDisable)
			return true;
		
		return false;
	}


	// opt done
	public String refRetrievalFromCache() {
		String ret = "";

		if (this.cache == null)
			return ret;

		ret += cache.getCacheKeys(localVars);

		return ret;
	}

	// opt done
	public String refRetrievalFromTree() {
		String ret = "";

		for (RVMParameter p : this.eventParams) {
			RVMVariable tempRef = localVars.getTempRef(p);
			
			if (Main.useWeakRefInterning) {
				if((!isGeneral && !event.isStartEvent()) || (isGeneral && !event.isStartEvent() && paramPairsForCopy.size() == 0 && !doDisable))
					ret += getRefTree(p).getRefNonCreative(tempRef, p);
				else
					ret += getRefTree(p).get(tempRef, p);
			}
			else {
				ret += tempRef + " = null;\n";
			}
		}

		return ret;
	}

	// opt done
	public String cacheRetrieval() {
		String ret = "";

		if (cache == null)
			return ret;

		String keyComparison = cache.getKeyComparison();

		ret += "// Cache Retrieval\n";

		ret += "if (" + keyComparison + ") {\n";
		{
			ret += refRetrievalFromCache();
			ret += "\n";
			if (indexingTree.containsSet()) {
				RVMVariable mainSet = localVars.get("mainSet");
				ret += cache.getCacheSet(mainSet);
			}
			if (isUsingMonitor()) {
				RVMVariable mainMonitor = localVars.get("mainMonitor");
				ret += cache.getCacheNode(mainMonitor);
			}
		}
		ret += "} else {\n";
		{
			ret += refRetrievalFromTree();

//			if((!isGeneral && !event.isStartEvent()) || (isGeneral && !event.isStartEvent() && paramPairsForCopy.size() == 0 && !doDisable)){
//				RVMVariable cacheHit = localVars.get("cacheHit");
//				ret += cacheHit + " = false;\n";
//			}
		}
		ret += "}\n";

		ret += "\n";

		return ret;
	}

	// opt done
	public String retrieveIndexingTree()  throws RVMException {
		String ret = "";

		if (isUsingMonitor()) {
			if (indexingTree.containsSet()) {
				ret += indexingTree.lookupNodeAndSet(localVars, "mainMonitor", "mainMap", "mainSet", event.isStartEvent(), monitorClass.getOutermostName().getVarName());
			} else {
				ret += indexingTree.lookupNode(localVars, "mainMonitor", "mainMap", "mainSet", event.isStartEvent(), monitorClass.getOutermostName().getVarName());
			}
		} else {
			if (indexingTree.containsSet()) {
				ret += indexingTree.lookupSet(localVars, "mainMonitor", "mainMap", "mainSet", event.isStartEvent());
			}
		}

		ret += "\n";

		return ret;
	}

	// opt done
	public String copyStateFromList(RVMonitorParameterPair paramPair, IndexingTree indexingTreeForCopy)  throws RVMException {
		String ret = "";

		IndexingTree targetIndexingTree = null;

		RVMParameters fromParams = paramPair.getParam2();
		RVMParameters toParams = RVMParameters.unionSet(fromParams, eventParams);
		toParams = mopSpec.getParameters().sortParam(toParams);

		for (RVMParameters param : indexingTrees.keySet()) {
			if (param.equals(toParams)) {
				targetIndexingTree = indexingTrees.get(param);
			}
		}
		if (targetIndexingTree == null)
			throw new Error("[Internal] cannot find the indexing tree");

		RVMVariable origSet = localVars.get("origSet");
		RVMVariable origMonitor = localVars.get("origMonitor");

		RVMVariable lastMonitor = localVars.get("lastMonitor");

		RVMVariable numAlive = new RVMVariable("numAlive");
		RVMVariable i = new RVMVariable("i");
		RVMVariable timeCheck = new RVMVariable("timeCheck");

		RVMParameters newParam = new RVMParameters();
		for (RVMParameter p : fromParams) {
			if (eventParams.contains(p))
				continue;
			newParam.add(p);
		}

		ret += "int " + numAlive + " = 0;\n";
		ret += "for(int " + i + " = 0; " + i + " < " + origSet + ".getSize(); " + i + "++) {\n";
		{
			ret += origMonitor + " = " + origSet + ".get(" + i + ");\n";
			for (RVMParameter p : newParam)
				ret += p.getType() + " " + p.getName() + " = " + "(" + p.getType() + ")" + origMonitor + "." + monitorClass.getRVMonitorRef(p) + ".get();\n";

			ret += "if (!" + origMonitor + ".isTerminated()";
			for (RVMParameter p : newParam)
				ret += " && " + p.getName() + " != null";
			ret += ") {\n";
			{
				ret += origSet + ".set(" + numAlive + ", " + origMonitor + ");\n";
				ret += numAlive + "++;\n";
				ret += "\n";

				for (RVMParameter p : newParam) {
					RVMVariable tempRef = localVars.getTempRef(p);
					ret += tempRef + " = " + origMonitor + "." + monitorClass.getRVMonitorRef(p) + ";\n";
				}
				
				ret += "\n";

				ret += targetIndexingTree.lookupNode(localVars, "lastMonitor", "lastMap", "lastSet", true, monitorClass.getOutermostName().getVarName());

				ret += "if (" + lastMonitor + " == null) {\n";
				{
					ret += "boolean " + timeCheck + " = true;\n\n";
					for (RVMParameter p : eventParams) {

						if (!fromParams.contains(p)) {
							// disable(theta''), tau(theta'')
							String disablepp = null, taupp = null;
							String extracheck = null;
							
							if (Main.useWeakRefInterning) {
								disablepp = getTempRefDisable(p);
								taupp = getTempRefTau(p);
							}
							else {
								disablepp = lastMonitor.getVarName() + ".disable";
								taupp = lastMonitor.getVarName() + ".tau";
								extracheck = lastMonitor + " != null";
							}
							
							ret += "if (";
							if (extracheck != null)
								ret += "(" + extracheck + ") && (";
							ret += disablepp + " > " + origMonitor + ".tau";
							ret += "|| (" + taupp + " > 0 && " + taupp + " < " + origMonitor + ".tau)";
							if (extracheck != null)
								ret += ")";
							ret += ") {\n";
							{
								ret += timeCheck + " = false;\n";
							}
							ret += "}\n";
						}
					}
					ret += "\n";

					ret += "if (" + timeCheck + ") {\n";
					{
						ret += statManager.incMonitor(mopSpec);

						ret += lastMonitor + " = " + "(" + monitorClass.getOutermostName() + ")" + origMonitor + ".clone();\n";

						for (RVMParameter p : toParams) {
							if (!fromParams.contains(p)) {
								RVMVariable tempRef = localVars.getTempRef(p);
								
								ret += lastMonitor + "." + monitorClass.getRVMonitorRef(p) + " = " + tempRef + ";\n";
								
								RefTree refTree = getRefTree(p);

								int tagNumber = refTree.getTagNumber(mopSpec);

								if (!refTree.isTagging())
									continue;

								ret += this.getTagInitializationCode(tempRef, tagNumber, origMonitor.getVarName() + ".tau");
							}
						}

						ret += targetIndexingTree.attachNode(localVars, "lastMonitor", "lastMap", "lastSet");

						if (monitorInfo != null)
							ret += monitorInfo.expand(lastMonitor, monitorClass, toParams);

						for (RVMParameters param : indexingTrees.keySet()) {
							if (toParams.contains(param) && !toParams.equals(param)) {
								IndexingTree indexingTree = indexingTrees.get(param);

								ret += "\n";
								if (indexingTree == this.indexingTree)
									ret += indexingTree.addMonitor(localVars, "lastMonitor", "mainMap", "mainSet");
								else
									ret += indexingTree.addMonitor(localVars, "lastMonitor");
							}
						}

						for (RVMonitorParameterPair paramPair2 : indexingTreesForCopy.keySet()) {
							if (paramPair2.getParam2().equals(toParams)) {
								IndexingTree indexingTree = indexingTreesForCopy.get(paramPair2);

								ret += "\n";
								ret += indexingTree.addMonitor(localVars, "lastMonitor");
							}
						}
					}
					ret += "}\n";
				}
				ret += "}\n";
			}
			ret += "}\n";
		}
		ret += "}\n\n";
		ret += origSet + ".eraseRange(" + numAlive + ");\n";
		return ret;
	}

	public String copyStateFromMonitor(RVMonitorParameterPair paramPair, IndexingTree indexingTreeForCopy) throws RVMException {
		String ret = "";

		RVMParameters fromParams = paramPair.getParam2();
		RVMParameters toParams = eventParams;

		RVMVariable origMonitor = localVars.get("origMonitor");
		RVMVariable mainMonitor = localVars.get("mainMonitor");

		RVMVariable timeCheck = new RVMVariable("timeCheck");

		ret += "boolean " + timeCheck + " = true;\n\n";
		for (RVMParameter p : eventParams) {
			if (!fromParams.contains(p)) {
				RVMVariable tau = new RVMVariable("tau");

				ret += "if (" + getTempRefDisable(p) + " > " + origMonitor + "." + tau + ") {\n";
				{
					ret += timeCheck + " = false;\n";
				}
				ret += "}\n";
			}
		}
		ret += "\n";

		ret += "if (" + timeCheck + ") {\n";
		{
			ret += statManager.incMonitor(mopSpec);
			
			ret += mainMonitor + " = " + "(" + monitorClass.getOutermostName() + ")" + origMonitor + ".clone();\n";

			for (RVMParameter p : toParams) {
				if (!fromParams.contains(p)) {
					RVMVariable tempRef = localVars.getTempRef(p);
					ret += mainMonitor + "." + monitorClass.getRVMonitorRef(p) + " = " + tempRef + ";\n";
					
					RefTree refTree = getRefTree(p);

					int tagNumber = refTree.getTagNumber(mopSpec);

					if (!refTree.isTagging())
						continue;

					ret += this.getTagInitializationCode(tempRef, tagNumber, origMonitor.getVarName() + ".tau");
				}
			}

			if (event.isStartEvent())
				ret += indexingTree.attachNode(localVars, "mainMonitor", "mainMap", "mainSet");
			else
				ret += indexingTree.addMonitor(localVars, "mainMonitor", "mainMap", "mainSet");

			if (monitorInfo != null)
				ret += monitorInfo.expand(mainMonitor, monitorClass, toParams);

			for (RVMParameters param : indexingTrees.keySet()) {
				if (!param.equals(toParams) && toParams.contains(param)) {
					IndexingTree indexingTree = indexingTrees.get(param);

					ret += "\n";
					ret += indexingTree.addMonitor(localVars, "mainMonitor");
				}
			}

			for (RVMonitorParameterPair paramPair2 : indexingTreesForCopy.keySet()) {
				if (paramPair2.getParam2().equals(toParams)) {
					IndexingTree indexingTree = indexingTreesForCopy.get(paramPair2);

					ret += "\n";
					ret += indexingTree.addMonitor(localVars, "mainMonitor");
				}
			}
		}
		ret += "}\n";

		return ret;
	}

	public String copyState() throws RVMException {
		String ret = "";
		
		if(!isGeneral)
			return ret;

		for (RVMonitorParameterPair paramPair : paramPairsForCopy) {
			IndexingTree indexingTreeForCopy = null;
			for (RVMonitorParameterPair paramPair2 : indexingTreesForCopy.keySet()){
				if(paramPair.equals(paramPair2)){
					indexingTreeForCopy = indexingTreesForCopy.get(paramPair2);
					break;
				}
			}

			if (!event.getRVMParametersOnSpec().contains(paramPair.getParam2())) {
				RVMVariable origSet = localVars.get("origSet");

				ret += indexingTreeForCopy.lookupSet(localVars, "origMonitor", "origMap", "origSet", false);
				ret += "if (" + origSet + "!= null) {\n";
				{
					ret += copyStateFromList(paramPair, indexingTreeForCopy);
				}
				ret += "}\n";
			} else {
				RVMVariable mainMonitor = localVars.get("mainMonitor");
				RVMVariable origMonitor = localVars.get("origMonitor");

				if (indexingTreeForCopy == null) {
					for (RVMParameters param : indexingTrees.keySet()) {
						if (param.equals(paramPair.getParam2())) {
							indexingTreeForCopy = indexingTrees.get(param);
						}
					}
				}
				
				ret += "if (" + mainMonitor + " == null) {\n";
				{
					ret += indexingTreeForCopy.lookupNode(localVars, "origMonitor", "origMap", "origSet", false, monitorClass.getOutermostName().getVarName());
					ret += "if (" + origMonitor + " != null) {\n";
					{
						ret += copyStateFromMonitor(paramPair, indexingTreeForCopy);
					}
					ret += "}\n";
				}
				ret += "}\n";
			}
		}

		return ret;
	}

	// opt done
	public String setMonitorRefs(RVMVariable monitor) {
		String ret = "";

		for (RVMParameter p : eventParams) {
			RVMVariable tempRef = localVars.getTempRef(p);

			ret += monitor + "." + monitorClass.getRVMonitorRef(p) + " = " + tempRef + ";\n";
		}

		if (ret.length() > 0)
			ret += "\n";

		return ret;
	}

	// opt done
	public String addToCurrentTree() throws RVMException {
		String ret = "";

		if (!event.isStartEvent())
			return ret;

		ret += indexingTree.attachNode(localVars, "mainMonitor", "mainMap", "mainSet");

		return ret;
	}
	
	private String getTagInitializationCode(RVMVariable weakref, int tagNumber, String value) {
		String ret = "";

		if (Main.useWeakRefInterning) {
			if (tagNumber == -1) {
				ret += "if (" + weakref + "." + "getTau() == -1){\n";
				ret += weakref + "." + "setTau(" + value + ");\n";
				ret += "}\n";
			} else {
				ret += "if (" + weakref + "." + "getTau(" + tagNumber + ") == -1){\n";
				ret += weakref + "." + "setTau(" + tagNumber + ", " + value + ");\n";
				ret += "}\n";
			}
		}
		else {
			// Since weak references do not hold tau, nothing needs to be done.
		}

		return ret;
	}

	// opt done
	public String setTau() {
		String ret = "";

		if (!isGeneral)
			return ret;

		RVMVariable mainMonitor = localVars.get("mainMonitor");

		ret += mainMonitor + ".tau = " + timestamp + ";\n";

		for (RVMParameter p : eventParams) {
			RVMVariable tempRef = localVars.getTempRef(p);
			RefTree refTree = getRefTree(p);

			int tagNumber = refTree.getTagNumber(mopSpec);

			if (!refTree.isTagging())
				continue;
			
			ret += this.getTagInitializationCode(tempRef, tagNumber, this.timestamp.getVarName());
		}

		ret += timestamp + "++;\n";

		return ret;
	}

	// opt done
	public String addToAllCompatibleTrees() throws RVMException {
		String ret = "";

		for (RVMParameters param : indexingTrees.keySet()) {
			if (param.equals(eventParams))
				continue;

			if (!eventParams.contains(param))
				continue;

			IndexingTree indexingTree = indexingTrees.get(param);

			ret += "\n";
			ret += indexingTree.addMonitor(localVars, "mainMonitor");
		}

		for (RVMonitorParameterPair paramPair : indexingTreesForCopy.keySet()) {
			if (!paramPair.getParam2().equals(eventParams))
				continue;

			IndexingTree indexingTree = indexingTreesForCopy.get(paramPair);

			ret += "\n";
			ret += indexingTree.addMonitor(localVars, "mainMonitor");
		}

		return ret;
	}

	// opt done
	public String createNewMonitor(boolean doWrap) throws RVMException {
		String ret = "";

		if (!event.isStartEvent())
			return ret;

		RVMVariable mainMonitor = localVars.get("mainMonitor");

		ret += statManager.incMonitor(mopSpec);

		ret += mainMonitor + " = new " + monitorClass.getOutermostName() + "();\n";
		if (monitorInfo != null)
			ret += monitorInfo.newInfo(mainMonitor, eventParams);

		ret += "\n";
		ret += setMonitorRefs(mainMonitor);
		ret += addToCurrentTree();
		ret += setTau();
		ret += addToAllCompatibleTrees();

		if (doWrap) {
			ret = "if (" + mainMonitor + " == null) {\n" + ret + "}\n";
		}

		return ret;
	}
	
	private String generateDisablePropertyAccessCode(RVMParameter p, boolean issetting, String rhs) {
		String ret = "";

		RefTree refTree = getRefTree(p);
		if (!refTree.isTagging())
			return ret;

		if (parametersForDisable.contains(p)) {
			RVMVariable tempRef = localVars.getTempRef(p);

			int tagNumber = refTree.getTagNumber(mopSpec);
			
			ret += tempRef;
			ret += ".";
			if (issetting)
				ret += "setDisabled(";
			else
				ret += "getDisabled(";
			
			if (tagNumber != -1)
				ret += tagNumber;
			
			if (issetting) {
				if (tagNumber != -1)
					ret += ", ";
				ret += rhs;
			}
			
			ret += ")";
		}

		return ret;
	}

	// opt done
	public String getTempRefDisable(RVMParameter p) {
		return this.generateDisablePropertyAccessCode(p, false, null);
	}
	
	public String setTempRefDisable(RVMParameter p, String rhs) {
		return this.generateDisablePropertyAccessCode(p, true, rhs);
	}
	
	private String setMonitorDisable(RVMVariable monitor, String rhs) {
		String ret = "";
		ret += monitor.getVarName() + ".disable = ";
		ret += rhs;
		return ret;
	}
	
	public String getTempRefTau(RVMParameter p) {
		String ret = "";

		RefTree refTree = getRefTree(p);
		if (!refTree.isTagging())
			return ret;

		if (parametersForDisable.contains(p)) {
			RVMVariable tempRef = localVars.getTempRef(p);

			int tagNumber = refTree.getTagNumber(mopSpec);

			if (tagNumber == -1) {
				ret += tempRef + "." + "getTau()";
			} else {
				ret += tempRef + "." + "getTau(" + tagNumber + ")";
			}
		}

		return ret;
	}


	// opt done
	public String setDisable(RVMVariable monitor) {
		String ret = "";

		if (!isGeneral)
			return ret;

		if (Main.useWeakRefInterning) {
			for (RVMParameter p : eventParams) {
				if (parametersForDisable.contains(p)) {
					RefTree refTree = getRefTree(p);
					if (!refTree.isTagging())
						continue;
	
					ret += setTempRefDisable(p, timestamp.getVarName()) + ";\n";
				}
			}
		}
		else {
			ret += setMonitorDisable(monitor, timestamp.getVarName()) + ";\n";
		}

		if (ret.length() > 0) {
			ret = "\n" + ret;
			ret += timestamp + "++;\n";
		}

		return ret;
	}

	// opt done
	public String handleNoMonitor() throws RVMException {
		RVMVariable mainMonitor = localVars.get("mainMonitor");
			
		String ret = "";

		String copyState = copyState();
		String createNewMonitor = createNewMonitor(copyState.length() > 0);
		String setDisable = setDisable(mainMonitor);

		if (copyState.length() > 0 || createNewMonitor.length() > 0 || setDisable.length() > 0) {

			ret += "if (" + mainMonitor + " == null) {\n";
			{
				ret += copyState;
				ret += createNewMonitor;
				ret += setDisable;
			}
			ret += "}\n";
			ret += "\n";
		}

		return ret;
	}

	// opt done
	public String setCache() {
		String ret = "";

		if(cache == null)
			return ret;
		
		ret += cache.setCacheKeys(localVars);
		if (indexingTree.containsSet()) {
			RVMVariable mainSet = localVars.get("mainSet");
			ret += cache.setCacheSet(mainSet);
		}

		if (indexingTree.cache.hasNode){
			RVMVariable mainMonitor = localVars.get("mainMonitor");
			ret += cache.setCacheNode(mainMonitor);
		}

		return ret;
	}

	// opt done
	public String handleCacheMiss() throws RVMException {
		String ret = "";

		ret += retrieveIndexingTree();

		ret += handleNoMonitor();

		ret += setCache();

		return ret;
	}

	// opt done
	public String cacheResultWrap(String handleCacheMiss) {
		String ret = "";
		String cacheResultCondition = "";

		if (!indexingTree.hasCache())
			return handleCacheMiss;

		if (indexingTree.containsSet()) {
			RVMVariable mainSet = localVars.get("mainSet");
			cacheResultCondition += mainSet + " == null";
		}
		if (isUsingMonitor()) {
			RVMVariable mainMonitor = localVars.get("mainMonitor");
			if (cacheResultCondition.length() > 0)
				cacheResultCondition += " || ";
			cacheResultCondition += mainMonitor + " == null";
		}
		
		if((!isGeneral && !event.isStartEvent()) || (isGeneral && !event.isStartEvent() && paramPairsForCopy.size() == 0 && !doDisable)){
//			RVMVariable cacheHit = localVars.get("cacheHit");
			
			if (cacheResultCondition.length() > 0){
				cacheResultCondition = "(" + cacheResultCondition + ")";
			}

//			if (cacheResultCondition.length() > 0)
//				cacheResultCondition = " && " + cacheResultCondition;
//			cacheResultCondition = "!" + cacheHit + cacheResultCondition; 
			
			if (Main.useWeakRefInterning) {
				for (RVMParameter p : this.eventParams) {
					RVMVariable tempRef = localVars.getTempRef(p);
					cacheResultCondition += " && " + tempRef + " != null";
				}
			}
		}

		ret += "if (" + cacheResultCondition + ") {\n";
		{
			ret += handleCacheMiss;
		}
		ret += "}\n";

		ret += "\n";

		return ret;
	}

	// opt done
	public String monitoring() {
		String ret = "";

		if (indexingTree.containsSet()) {
			RVMVariable mainSet = localVars.get("mainSet");

			ret += monitorSet.Monitoring(mainSet, event, null, null, this.lock);
		} else if (event.isStartEvent() && isFullParam) {
			RVMVariable mainMonitor = localVars.get("mainMonitor");
			
			ret += monitorClass.Monitoring(mainMonitor, event, null, null, this.lock, this.aspectName, false);
		} else {
			RVMVariable mainMonitor = localVars.get("mainMonitor");

			ret += "if (" + mainMonitor + " != null " + ") {\n";
			{
				ret += monitorClass.Monitoring(mainMonitor, event, null, null, this.lock, this.aspectName, false);
			}
			ret += "}\n";
		}

		return ret;
	}

	
	public String toString() {
		String ret = "";
		if (!indexingTree.containsSet()) {
			for (RVMVariable variable : monitorClass.getCategoryVars()) {
				ret += BaseMonitor.getNiceVariable(variable) + " = false;\n";
			}
			if (mopSpec.has__SKIP()) {
				ret += BaseMonitor.skipEvent + " = false;\n";
			}
		}

		localVars.init();

		if (indexingTree.hasCache()) {
			ret += cacheRetrieval();
		} else {
			ret += refRetrievalFromTree();
			ret += "\n";
		}

		String handleCacheMiss;

		try{
			handleCacheMiss = handleCacheMiss();
		} catch (RVMException e){
			ret += "*** Error under GeneralAdviceBody ***\n";
			ret += e.getMessage();
			return ret;
		}

		ret += cacheResultWrap(handleCacheMiss);

		ret += monitoring();

		ret = localVars.varDecl() + ret;

		return ret;
	}
}

*/