package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.centralized;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMTypedVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice.LocalVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingCache;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class FullParamIndexingTree extends IndexingTree {

	public FullParamIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		if (!isFullParam)
			throw new RVMException("FullParamIndexingTree can be created only when queryParam equals to fullParam.");

		if (queryParam.size() == 0)
			throw new RVMException("FullParamIndexingTree should contain at least one parameter.");

		if (anycontent) {
			this.name = new RVMVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "_Map");
		} else {
			if (!contentParam.contains(queryParam))
				throw new RVMException("[Internal] contentParam should contain queryParam");

			this.name = new RVMVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "__To__" + contentParam.parameterStringUnderscore() + "_Map");
		}
	
		if (anycontent){
			this.cache = new IndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
			//this.cache = new LocalityIndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
		}
	}

	public RVMParameter getLastParam(){
		return queryParam.get(queryParam.size() - 1);
	}

	protected String lookupIntermediateCreative(LocalVariables localVars, RVMVariable monitor, String lastMapStr, RVMVariable lastSet, int i) {
		String ret = "";

		RVMTypedVariable tempMap = localVars.getTempMap();
		RVMTypedVariable obj = localVars.createObj(tempMap, IndexingTreeInterface.Map);

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += obj.getType() + " " + obj.getName() + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		RVMParameter nextP = queryParam.get(i + 1);

		ret += "if (" + obj + " == null) {\n";

		ret += obj + " = new " + obj.getType() + "(" + fullParam.getIdnum(nextP) + ");\n";
		
		ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
		ret += "}\n";

		if (i == queryParam.size() - 2) {
			RVMTypedVariable.Pair pair = localVars.createOrFindMap(lastMapStr, obj.getType());
			if (pair.isCreated())
				ret += pair.getVariable().getType() + " ";	
			ret += pair.getVariable() + " = " + obj + ";\n";	
			ret += lookupNodeLast(localVars, monitor, lastMapStr, lastSet, i + 1, true);
		} else {
			RVMTypedVariable var = localVars.createTempMap(obj.getType());
			ret += var.getType() + " " + var.getName() + " = " + obj + ";\n";
			ret += lookupIntermediateCreative(localVars, monitor, lastMapStr, lastSet, i + 1);
		}

		return ret;
	}
	
	protected String lookupIntermediateNonCreative(LocalVariables localVars, RVMVariable monitor, String lastMapStr, RVMVariable lastSet, int i) {
		String ret = "";

		RVMTypedVariable tempMap = localVars.getTempMap();
		RVMTypedVariable obj = localVars.createObj(tempMap, IndexingTreeInterface.Map);

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += obj.getType() + " " + obj.getName() + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		ret += "if (" + obj + " != null) {\n";

		if (i == queryParam.size() - 2) {
			RVMTypedVariable.Pair pair = localVars.createOrFindMap(lastMapStr, obj.getType());
			if (pair.isCreated())
				ret += pair.getVariable().getType() + " ";	
			ret += pair.getVariable() + " = " + obj + ";\n";			
			ret += lookupNodeLast(localVars, monitor, lastMapStr, lastSet, i + 1, false);
		} else {
			RVMTypedVariable var = localVars.createTempMap(obj.getType());
			ret += var.getType() + " " + var.getName() + " = " + obj + ";\n";
			ret += lookupIntermediateNonCreative(localVars, monitor, lastMapStr, lastSet, i + 1);
		}

		ret += "}\n";

		return ret;
	}
	
	protected String lookupNodeLast(LocalVariables localVars, RVMVariable monitor, String lastMapStr, RVMVariable lastSet, int i, boolean creative) {
		String ret = "";

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);
		RVMTypedVariable lastMap = localVars.getMap(lastMapStr);

		ret += monitor + " = " + lastMap + ".getLeaf(" + tempRef + ");\n";

		return ret;
	}
	
	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative){
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);

		if (queryParam.size() == 1) {
			RVMTypedVariable.Pair pair = localVars.createOrFindMap(lastMapStr, this.getTreeType());
			if (pair.isCreated())
				ret += pair.getVariable().getType() + " ";
			ret += pair.getVariable() + " = " + retrieveTree() + ";\n";
			ret += lookupNodeLast(localVars, monitor, lastMapStr, null, 0, creative);
		} else {
			RVMTypedVariable tempMap = localVars.createTempMap(this.getTreeType());
			ret += tempMap.getType().toString() + " " + tempMap.getName() + " = " + retrieveTree() + ";\n";

			if (creative) {
				ret += lookupIntermediateCreative(localVars, monitor, lastMapStr, null, 0);
			} else {
				ret += lookupIntermediateNonCreative(localVars, monitor, lastMapStr, null, 0);
			}
		}

		return ret;
	}

	public String lookupSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative){
		return "";
	}
	
	public String lookupNodeAndSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative){
		return lookupNode(localVars, monitorStr, lastMapStr, lastSetStr, creative);
	}

	public String attachNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr){
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);

		RVMVariable tempRef = localVars.getTempRef(getLastParam());

		if (queryParam.size() == 1) {
			ret += retrieveTree() + ".putLeaf(" + tempRef + ", " + monitor + ");\n";
		} else {
			RVMTypedVariable var = localVars.getMap(lastMapStr);
			ret += var + ".putLeaf(" + tempRef + ", " + monitor + ");\n";
		}

		return ret;
	}

	public String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr){
		return "";
	}
	
	public String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr){
		String ret = "";

		RVMTypedVariable tempMap = localVars.createTempMap(this.getTreeType());

		RVMVariable monitor = localVars.get(monitorStr);

		ret += tempMap.getType() + " " + tempMap.getName() + " = " + retrieveTree() + ";\n";

		for (int i = 0; i < queryParam.size() - 1; i++) {
			RVMParameter p = queryParam.get(i);
			RVMParameter nextp = queryParam.get(i + 1);
			RVMVariable tempRef = localVars.getTempRef(p);

			RVMTypedVariable obj = localVars.createObj(tempMap, IndexingTreeInterface.Map);
			ret += obj.getType() + " " + obj.getName() + " = " + tempMap + ".getMap(" + tempRef + ");\n";

			ret += "if (" + obj + " == null) {\n";

			ret += obj + " = new " + obj.getType() + "(" + fullParam.getIdnum(nextp) + ");\n";
			ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
			ret += "}\n";

			tempMap = localVars.createTempMap(obj.getType());
			ret += tempMap.getType() + " " + tempMap.getName() + " = " + obj.getName() + ";\n";
		}

		RVMParameter p = getLastParam();
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += tempMap + ".putLeaf(" + tempRef + ", " + monitor + ");\n";

		return ret;
	}


	public boolean containsSet() {
		return false;
	}

	public String retrieveTree(){
		if(parentTree != null)
			return parentTree.retrieveTree();
		
		if (perthread) {
			String ret = "";
			ret += name + ".get()";
			return ret;

		} else {
			return name.toString();
		}
	}

	public String getRefTreeType(){
		String ret = "";
		
		if(parentTree != null)
			return parentTree.getRefTreeType();

		if(parasiticRefTree == null)
			return ret;
		
		ret = this.getTreeType().toString();
		
		return ret;
	}

	public String toString() {
		String ret = "";

		if(parentTree == null){
			if (perthread) {
				String type = this.getTreeType().toString();
				ret += "static final ThreadLocal<" + type + ">" + name + " = new ThreadLocal<" + type + ">() {\n";
				ret += "protected " + type + " initialValue(){\n";
				ret += "return new " + type + "(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
				
				ret += "}\n";
				ret += "};\n";
			} else {
				String type = this.getTreeType().toString();
				ret += "static " + type + " " + name + " = new " + type + "(" + fullParam.getIdnum(queryParam.get(0));
				if (parasiticRefTree != null && parasiticRefTree.generalProperties.size() > 1)
					ret += ", " + parasiticRefTree.generalProperties.size();
				ret += ");\n";
			}
		}
		
		if (cache != null)
			ret += cache;

		return ret;
	}
	
	public String reset() {
		String ret = "";

		if(parentTree == null){
			if (perthread) {
			} else {
				//ret += "System.err.println(\""+ name + " size: \" + (" + name + ".addedMappings - " + name + ".deletedMappings" + "));\n";
				
				if(parasiticRefTree == null){
					if(isGeneral){
						if (queryParam.size() == 1) {
							ret += name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					}
				} else {
					if(parasiticRefTree.generalProperties.size() <= 1){
						ret += name + " = new " + getRefTreeType() + "(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					} else {
						ret += name + " = new " + getRefTreeType() + "(" + fullParam.getIdnum(queryParam.get(0)) + ", " + parasiticRefTree.generalProperties.size() + ");\n";
					}
				}
			}
		}
		
		if (cache != null)
			ret += cache.reset();

		return ret;
	}

}
