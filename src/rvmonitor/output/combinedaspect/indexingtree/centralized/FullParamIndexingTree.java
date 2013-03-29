package rvmonitor.output.combinedaspect.indexingtree.centralized;

import java.util.HashMap;

import rvmonitor.RVMException;
import rvmonitor.output.RVMVariable;
import rvmonitor.output.combinedaspect.event.advice.LocalVariables;
import rvmonitor.output.combinedaspect.indexingtree.IndexingCache;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTree;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.RVMParameter;
import rvmonitor.parser.ast.mopspec.RVMParameters;

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

	protected String lookupIntermediateCreative(LocalVariables localVars, RVMVariable monitor, RVMVariable lastMap, RVMVariable lastSet, int i) {
		String ret = "";

		RVMVariable obj = localVars.get("obj");
		RVMVariable tempMap = localVars.get("tempMap");

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		RVMParameter nextP = queryParam.get(i + 1);

		ret += "if (" + obj + " == null) {\n";

		if (i == queryParam.size() - 2){
			ret += obj + " = new rvmonitorrt.map.RVMMapOfMonitor(" + fullParam.getIdnum(nextP) + ");\n";
		} else {
			if(isGeneral){
				ret += obj + " = new rvmonitorrt.map.RVMMapOfAll(" + fullParam.getIdnum(nextP) + ");\n";
			} else {
				ret += obj + " = new rvmonitorrt.map.RVMMapOfMapSet(" + fullParam.getIdnum(nextP) + ");\n";
			}
		}
		
		ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
		ret += "}\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (rvmonitorrt.map.RVMAbstractMap)" + obj + ";\n";
			ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, true);
		} else {
			ret += tempMap + " = (rvmonitorrt.map.RVMAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateCreative(localVars, monitor, lastMap, lastSet, i + 1);
		}

		return ret;
	}
	
	protected String lookupIntermediateNonCreative(LocalVariables localVars, RVMVariable monitor, RVMVariable lastMap, RVMVariable lastSet, int i) {
		String ret = "";

		RVMVariable obj = localVars.get("obj");
		RVMVariable tempMap = localVars.get("tempMap");

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		ret += "if (" + obj + " != null) {\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (rvmonitorrt.map.RVMAbstractMap)" + obj + ";\n";
			ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, false);
		} else {
			ret += tempMap + " = (rvmonitorrt.map.RVMAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, lastSet, i + 1);
		}

		ret += "}\n";

		return ret;
	}
	
	protected String lookupNodeLast(LocalVariables localVars, RVMVariable monitor, RVMVariable lastMap, RVMVariable lastSet, int i, boolean creative) {
		String ret = "";

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += monitor + " = " + "(" + monitorClass.getOutermostName() + ")" + lastMap + ".getNode(" + tempRef + ");\n";

		return ret;
	}
	
	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative){
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);
		RVMVariable lastMap = localVars.get(lastMapStr);

		if (queryParam.size() == 1) {
			ret += lastMap + " = " + retrieveTree() + ";\n";
			ret += lookupNodeLast(localVars, monitor, lastMap, null, 0, creative);
		} else {
			RVMVariable tempMap = localVars.get("tempMap");
			ret += tempMap + " = " + retrieveTree() + ";\n";

			if (creative) {
				ret += lookupIntermediateCreative(localVars, monitor, lastMap, null, 0);
			} else {
				ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, null, 0);
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
			ret += retrieveTree() + ".putNode(" + tempRef + ", " + monitor + ");\n";
		} else {
			RVMVariable lastMap = localVars.get(lastMapStr);

			ret += lastMap + ".putNode(" + tempRef + ", " + monitor + ");\n";
		}

		return ret;
	}

	public String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr){
		return "";
	}
	
	public String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr){
		String ret = "";

		RVMVariable obj = localVars.get("obj");
		RVMVariable tempMap = localVars.get(tempMapStr);
		RVMVariable monitor = localVars.get(monitorStr);

		ret += tempMap + " = " + retrieveTree() + ";\n";

		for (int i = 0; i < queryParam.size() - 1; i++) {
			RVMParameter p = queryParam.get(i);
			RVMParameter nextp = queryParam.get(i + 1);
			RVMVariable tempRef = localVars.getTempRef(p);

			ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

			ret += "if (" + obj + " == null) {\n";

			if (i == queryParam.size() - 2) {
				ret += obj + " = new rvmonitorrt.map.RVMMapOfMonitor(" + fullParam.getIdnum(nextp) + ");\n";
			} else {
				if(isGeneral)
					ret += obj + " = new rvmonitorrt.map.RVMMapOfAll(" + fullParam.getIdnum(nextp) + ");\n";
				else
					ret += obj + " = new rvmonitorrt.map.RVMMapOfMapSet(" + fullParam.getIdnum(nextp) + ");\n";
			}

			ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
			ret += "}\n";

			ret += tempMap + " = (rvmonitorrt.map.RVMAbstractMap)" + obj + ";\n";
 		}

		RVMParameter p = getLastParam();
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += tempMap + ".putNode(" + tempRef + ", " + monitor + ");\n";

//		if(cache != null){
//			ret += cache.setCacheKeys(localVars);
//			
//			if (cache.hasNode){
//				ret += cache.setCacheNode(monitor);
//			}
//		}
		
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

			ret += "(";
			ret += "(rvmonitorrt.map.RVMAbstractMap)";
			ret += name + ".get()";
			ret += ")";

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
		
		if(isGeneral){
			if (queryParam.size() == 1) {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "rvmonitorrt.map.RVMBasicRefMapOfMonitor";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.RVMTagRefMapOfMonitor";
				} else {
					ret = "rvmonitorrt.map.RVMMultiTagRefMapOfMonitor";
				}
			} else {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "rvmonitorrt.map.RVMBasicRefMapOfAll";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.RVMTagRefMapOfAll";
				} else {
					ret = "rvmonitorrt.map.RVMMultiTagRefMapOfAll";
				}
			}
		} else {
			if (queryParam.size() == 1) {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "rvmonitorrt.map.RVMBasicRefMapOfMonitor";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.RVMTagRefMapOfMonitor";
				} else {
					ret = "rvmonitorrt.map.RVMMultiTagRefMapOfMonitor";
				}
			} else {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "rvmonitorrt.map.RVMBasicRefMapOfMapSet";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.RVMTagRefMapOfMapSet";
				} else {
					ret = "rvmonitorrt.map.RVMMultiTagRefMapOfMapSet";
				}
			}
		}
		
		return ret;
	}

	public String toString() {
		String ret = "";

		if(parentTree == null){
			if (perthread) {
				ret += "static final ThreadLocal " + name + " = new ThreadLocal() {\n";
				ret += "protected Object initialValue(){\n";
				ret += "return ";
	
				if (queryParam.size() == 1) {
					ret += "new rvmonitorrt.map.RVMMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
				} else {
					if(isGeneral)
						ret += "new rvmonitorrt.map.RVMMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					else
						ret += "new rvmonitorrt.map.RVMMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
				}
				
				ret += "}\n";
				ret += "};\n";
			} else {
				if(parasiticRefTree == null){
					if(isGeneral){
						if (queryParam.size() == 1) {
							ret += "static rvmonitorrt.map.RVMAbstractMap " + name + " = new rvmonitorrt.map.RVMMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += "static rvmonitorrt.map.RVMAbstractMap " + name + " = new rvmonitorrt.map.RVMMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += "static rvmonitorrt.map.RVMAbstractMap " + name + " = new rvmonitorrt.map.RVMMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += "static rvmonitorrt.map.RVMAbstractMap " + name + " = new rvmonitorrt.map.RVMMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					}
				} else {
					if(parasiticRefTree.generalProperties.size() <= 1){
						ret += "static " + getRefTreeType() + " " + name + " = new " + getRefTreeType() + "(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					} else {
						ret += "static " + getRefTreeType() + " " + name + " = new " + getRefTreeType() + "(" + fullParam.getIdnum(queryParam.get(0)) + ", " + parasiticRefTree.generalProperties.size() + ");\n";
					}
				}
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
							ret += name + " = new rvmonitorrt.map.RVMMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new rvmonitorrt.map.RVMMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += name + " = new rvmonitorrt.map.RVMMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new rvmonitorrt.map.RVMMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
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
