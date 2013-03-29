package rvmonitor.output.combinedaspect.indexingtree.centralized;

import java.util.HashMap;

import rvmonitor.RVMException;
import rvmonitor.output.MOPVariable;
import rvmonitor.output.combinedaspect.event.advice.LocalVariables;
import rvmonitor.output.combinedaspect.indexingtree.IndexingCache;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTree;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.MOPParameter;
import rvmonitor.parser.ast.mopspec.MOPParameters;

public class FullParamIndexingTree extends IndexingTree {

	public FullParamIndexingTree(String aspectName, MOPParameters queryParam, MOPParameters contentParam, MOPParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		if (!isFullParam)
			throw new RVMException("FullParamIndexingTree can be created only when queryParam equals to fullParam.");

		if (queryParam.size() == 0)
			throw new RVMException("FullParamIndexingTree should contain at least one parameter.");

		if (anycontent) {
			this.name = new MOPVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "_Map");
		} else {
			if (!contentParam.contains(queryParam))
				throw new RVMException("[Internal] contentParam should contain queryParam");

			this.name = new MOPVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "__To__" + contentParam.parameterStringUnderscore() + "_Map");
		}
	
		if (anycontent){
			this.cache = new IndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
			//this.cache = new LocalityIndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
		}
	}

	public MOPParameter getLastParam(){
		return queryParam.get(queryParam.size() - 1);
	}

	protected String lookupIntermediateCreative(LocalVariables localVars, MOPVariable monitor, MOPVariable lastMap, MOPVariable lastSet, int i) {
		String ret = "";

		MOPVariable obj = localVars.get("obj");
		MOPVariable tempMap = localVars.get("tempMap");

		MOPParameter p = queryParam.get(i);
		MOPVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		MOPParameter nextP = queryParam.get(i + 1);

		ret += "if (" + obj + " == null) {\n";

		if (i == queryParam.size() - 2){
			ret += obj + " = new rvmonitorrt.map.MOPMapOfMonitor(" + fullParam.getIdnum(nextP) + ");\n";
		} else {
			if(isGeneral){
				ret += obj + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(nextP) + ");\n";
			} else {
				ret += obj + " = new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(nextP) + ");\n";
			}
		}
		
		ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
		ret += "}\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, true);
		} else {
			ret += tempMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateCreative(localVars, monitor, lastMap, lastSet, i + 1);
		}

		return ret;
	}
	
	protected String lookupIntermediateNonCreative(LocalVariables localVars, MOPVariable monitor, MOPVariable lastMap, MOPVariable lastSet, int i) {
		String ret = "";

		MOPVariable obj = localVars.get("obj");
		MOPVariable tempMap = localVars.get("tempMap");

		MOPParameter p = queryParam.get(i);
		MOPVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		ret += "if (" + obj + " != null) {\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, false);
		} else {
			ret += tempMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, lastSet, i + 1);
		}

		ret += "}\n";

		return ret;
	}
	
	protected String lookupNodeLast(LocalVariables localVars, MOPVariable monitor, MOPVariable lastMap, MOPVariable lastSet, int i, boolean creative) {
		String ret = "";

		MOPParameter p = queryParam.get(i);
		MOPVariable tempRef = localVars.getTempRef(p);

		ret += monitor + " = " + "(" + monitorClass.getOutermostName() + ")" + lastMap + ".getNode(" + tempRef + ");\n";

		return ret;
	}
	
	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative){
		String ret = "";

		MOPVariable monitor = localVars.get(monitorStr);
		MOPVariable lastMap = localVars.get(lastMapStr);

		if (queryParam.size() == 1) {
			ret += lastMap + " = " + retrieveTree() + ";\n";
			ret += lookupNodeLast(localVars, monitor, lastMap, null, 0, creative);
		} else {
			MOPVariable tempMap = localVars.get("tempMap");
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

		MOPVariable monitor = localVars.get(monitorStr);

		MOPVariable tempRef = localVars.getTempRef(getLastParam());

		if (queryParam.size() == 1) {
			ret += retrieveTree() + ".putNode(" + tempRef + ", " + monitor + ");\n";
		} else {
			MOPVariable lastMap = localVars.get(lastMapStr);

			ret += lastMap + ".putNode(" + tempRef + ", " + monitor + ");\n";
		}

		return ret;
	}

	public String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr){
		return "";
	}
	
	public String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr){
		String ret = "";

		MOPVariable obj = localVars.get("obj");
		MOPVariable tempMap = localVars.get(tempMapStr);
		MOPVariable monitor = localVars.get(monitorStr);

		ret += tempMap + " = " + retrieveTree() + ";\n";

		for (int i = 0; i < queryParam.size() - 1; i++) {
			MOPParameter p = queryParam.get(i);
			MOPParameter nextp = queryParam.get(i + 1);
			MOPVariable tempRef = localVars.getTempRef(p);

			ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

			ret += "if (" + obj + " == null) {\n";

			if (i == queryParam.size() - 2) {
				ret += obj + " = new rvmonitorrt.map.MOPMapOfMonitor(" + fullParam.getIdnum(nextp) + ");\n";
			} else {
				if(isGeneral)
					ret += obj + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(nextp) + ");\n";
				else
					ret += obj + " = new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(nextp) + ");\n";
			}

			ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
			ret += "}\n";

			ret += tempMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
 		}

		MOPParameter p = getLastParam();
		MOPVariable tempRef = localVars.getTempRef(p);

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
			ret += "(rvmonitorrt.map.MOPAbstractMap)";
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
					ret = "rvmonitorrt.map.MOPBasicRefMapOfMonitor";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.MOPTagRefMapOfMonitor";
				} else {
					ret = "rvmonitorrt.map.MOPMultiTagRefMapOfMonitor";
				}
			} else {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "rvmonitorrt.map.MOPBasicRefMapOfAll";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.MOPTagRefMapOfAll";
				} else {
					ret = "rvmonitorrt.map.MOPMultiTagRefMapOfAll";
				}
			}
		} else {
			if (queryParam.size() == 1) {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "rvmonitorrt.map.MOPBasicRefMapOfMonitor";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.MOPTagRefMapOfMonitor";
				} else {
					ret = "rvmonitorrt.map.MOPMultiTagRefMapOfMonitor";
				}
			} else {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "rvmonitorrt.map.MOPBasicRefMapOfMapSet";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.MOPTagRefMapOfMapSet";
				} else {
					ret = "rvmonitorrt.map.MOPMultiTagRefMapOfMapSet";
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
					ret += "new rvmonitorrt.map.MOPMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
				} else {
					if(isGeneral)
						ret += "new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					else
						ret += "new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
				}
				
				ret += "}\n";
				ret += "};\n";
			} else {
				if(parasiticRefTree == null){
					if(isGeneral){
						if (queryParam.size() == 1) {
							ret += "static rvmonitorrt.map.MOPAbstractMap " + name + " = new rvmonitorrt.map.MOPMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += "static rvmonitorrt.map.MOPAbstractMap " + name + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += "static rvmonitorrt.map.MOPAbstractMap " + name + " = new rvmonitorrt.map.MOPMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += "static rvmonitorrt.map.MOPAbstractMap " + name + " = new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
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
							ret += name + " = new rvmonitorrt.map.MOPMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += name + " = new rvmonitorrt.map.MOPMapOfMonitor(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
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
