package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.centralized;

import java.util.HashMap;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice.LocalVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingCache;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class PartialParamIndexingTree extends IndexingTree {

	final static int NODEONLY = 0;
	final static int SETONLY = 1;
	final static int NODEANDSET = 2;

	public PartialParamIndexingTree(String aspectName, RVMParameters queryParam, RVMParameters contentParam, RVMParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		if (isFullParam)
			throw new RVMException("PartialParamIndexingTree can be created only when queryParam does not equal to fullParam.");

		if (queryParam.size() == 0)
			throw new RVMException("PartialParamIndexingTree should contain at least one parameter.");

		if (anycontent) {
			this.name = new RVMVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "_Map");
		} else {
			if (!contentParam.contains(queryParam))
				throw new RVMException("[Internal] contentParam should contain queryParam");
			if (contentParam.size() <= queryParam.size())
				throw new RVMException("[Internal] contentParam should be larger than queryParam");

			this.name = new RVMVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "__To__" + contentParam.parameterStringUnderscore() + "_Map");
		}

		if (anycontent){
			this.cache = new IndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
			//this.cache = new LocalityIndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
		}
	}

	public RVMParameter getLastParam() {
		return queryParam.get(queryParam.size() - 1);
	}

	protected String lookupIntermediateCreative(LocalVariables localVars, RVMVariable monitor, RVMVariable lastMap, RVMVariable lastSet, int i, int target) {
		String ret = "";

		RVMVariable obj = localVars.get("obj");
		RVMVariable tempMap = localVars.get("tempMap");

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		RVMParameter nextP = queryParam.get(i + 1);

		ret += "if (" + obj + " == null) {\n";

		if(isGeneral){
			if (i == queryParam.size() - 2)
				ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSetMon(" + fullParam.getIdnum(nextP) + ");\n";
			else
				ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfAll(" + fullParam.getIdnum(nextP) + ");\n";
		} else {
			if (i == queryParam.size() - 2)
				ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSet(" + fullParam.getIdnum(nextP) + ");\n";
			else
				ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMapSet(" + fullParam.getIdnum(nextP) + ");\n";
		}

		ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
		ret += "}\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap)" + obj + ";\n";
			if (target == NODEONLY)
				ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, true);
			else if (target == SETONLY)
				ret += lookupSetLast(localVars, monitor, lastMap, lastSet, i + 1, true);
			else if (target == NODEANDSET)
				ret += lookupNodeAndSetLast(localVars, monitor, lastMap, lastSet, i + 1, true);
		} else {
			ret += tempMap + " = (com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateCreative(localVars, monitor, lastMap, lastSet, i + 1, target);
		}

		return ret;
	}

	protected String lookupIntermediateNonCreative(LocalVariables localVars, RVMVariable monitor, RVMVariable lastMap, RVMVariable lastSet, int i, int target) {
		String ret = "";

		RVMVariable obj = localVars.get("obj");
		RVMVariable tempMap = localVars.get("tempMap");

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		ret += "if (" + obj + " != null) {\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap)" + obj + ";\n";
			if (target == NODEONLY)
				ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, false);
			else if (target == SETONLY)
				ret += lookupSetLast(localVars, monitor, lastMap, lastSet, i + 1, false);
			else if (target == NODEANDSET)
				ret += lookupNodeAndSetLast(localVars, monitor, lastMap, lastSet, i + 1, false);
		} else {
			ret += tempMap + " = (com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, lastSet, i + 1, target);
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

	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
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
				ret += lookupIntermediateCreative(localVars, monitor, lastMap, null, 0, NODEONLY);
			} else {
				ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, null, 0, NODEONLY);
			}
		}

		return ret;
	}

	protected String lookupSetLast(LocalVariables localVars, RVMVariable monitor, RVMVariable lastMap, RVMVariable lastSet, int i, boolean creative) {
		String ret = "";

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += lastSet + " = " + "(" + monitorSet.getName() + ")" + lastMap + ".getSet(" + tempRef + ");\n";

		if (creative) {
			ret += "if (" + lastSet + " == null){\n";
			ret += lastSet + " = new " + monitorSet.getName() + "();\n";
			ret += lastMap + ".putSet(" + tempRef + ", " + lastSet + ");\n";
			ret += "}\n";
		}

		return ret;
	}

	public String lookupSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
		String ret = "";

		RVMVariable lastMap = localVars.get(lastMapStr);
		RVMVariable lastSet = localVars.get(lastSetStr);

		if (queryParam.size() == 1) {
			ret += lastMap + " = " + retrieveTree() + ";\n";
			ret += lookupSetLast(localVars, null, lastMap, lastSet, 0, creative);
		} else {
			RVMVariable tempMap = localVars.get("tempMap");
			ret += tempMap + " = " + retrieveTree() + ";\n";

			if (creative) {
				ret += lookupIntermediateCreative(localVars, null, lastMap, lastSet, 0, SETONLY);
			} else {
				ret += lookupIntermediateNonCreative(localVars, null, lastMap, lastSet, 0, SETONLY);
			}
		}

		return ret;
	}

	protected String lookupNodeAndSetLast(LocalVariables localVars, RVMVariable monitor, RVMVariable lastMap, RVMVariable lastSet, int i, boolean creative) {
		String ret = "";

		RVMParameter p = queryParam.get(i);
		RVMVariable tempRef = localVars.getTempRef(p);

		ret += monitor + " = " + "(" + monitorClass.getOutermostName() + ")" + lastMap + ".getNode(" + tempRef + ");\n";
		ret += lastSet + " = " + "(" + monitorSet.getName() + ")" + lastMap + ".getSet(" + tempRef + ");\n";

		if (creative) {
			ret += "if (" + lastSet + " == null){\n";
			ret += lastSet + " = new " + monitorSet.getName() + "();\n";
			ret += lastMap + ".putSet(" + tempRef + ", " + lastSet + ");\n";
			ret += "}\n";
		}

		return ret;
	}

	public String lookupNodeAndSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);
		RVMVariable lastMap = localVars.get(lastMapStr);
		RVMVariable lastSet = localVars.get(lastSetStr);

		if (queryParam.size() == 1) {
			ret += lastMap + " = " + retrieveTree() + ";\n";
			ret += lookupNodeAndSetLast(localVars, monitor, lastMap, lastSet, 0, creative);
		} else {
			RVMVariable tempMap = localVars.get("tempMap");
			ret += tempMap + " = " + retrieveTree() + ";\n";

			if (creative) {
				ret += lookupIntermediateCreative(localVars, monitor, lastMap, lastSet, 0, NODEANDSET);
			} else {
				ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, lastSet, 0, NODEANDSET);
			}
		}

		return ret;
	}

	public String attachNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) {
		String ret = "";

		RVMVariable monitor = localVars.get(monitorStr);
		RVMVariable lastSet = localVars.get(lastSetStr);

		RVMVariable tempRef = localVars.getTempRef(getLastParam());

		if (queryParam.size() == 1) {
			ret += retrieveTree() + ".putNode(" + tempRef + ", " + monitor + ");\n";
		} else {
			RVMVariable lastMap = localVars.get(lastMapStr);

			ret += lastMap + ".putNode(" + tempRef + ", " + monitor + ");\n";
		}

		ret += lastSet + ".add(" + monitor + ");\n";

		return ret;
	}

	public String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) {
		String ret = "";

		RVMVariable lastSet = localVars.get(lastSetStr);

		RVMVariable tempRef = localVars.getTempRef(getLastParam());

		if (queryParam.size() == 1) {
			ret += retrieveTree() + ".putSet(" + tempRef + ", " + lastSet + ");\n";
		} else {
			RVMVariable lastMap = localVars.get(lastMapStr);

			ret += lastMap + ".putSet(" + tempRef + ", " + lastSet + ");\n";
		}

		return ret;
	}

	public String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr) {
		String ret = "";

		RVMVariable obj = localVars.get("obj");
		RVMVariable tempMap = localVars.get(tempMapStr);
		RVMVariable monitor = localVars.get(monitorStr);
		RVMVariable monitors = localVars.get(tempSetStr);

		ret += tempMap + " = " + retrieveTree() + ";\n";

		RVMParameter p = queryParam.get(0);
		RVMVariable tempRef = localVars.getTempRef(p);

    if(queryParam.size() == 1){
			ret += obj + " = " + tempMap + ".getSet(" + tempRef + ");\n";
    }
    else {
			ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";
    }
		
    for (int i = 1; i < queryParam.size(); i++) {

			//if (i != 0) {

				ret += "if (" + obj + " == null) {\n";

				if(isGeneral){
					if (i == queryParam.size() - 1) {
						ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSetMon(" + fullParam.getIdnum(p) + ");\n";
					} else
						ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfAll(" + fullParam.getIdnum(p) + ");\n";
				} else {
					if (i == queryParam.size() - 1) {
						ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSet(" + fullParam.getIdnum(p) + ");\n";
					} else
						ret += obj + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMapSet(" + fullParam.getIdnum(p) + ");\n";
				}

				ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
				ret += "}\n";

				ret += tempMap + " = (com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap)" + obj + ";\n";
			//}

		  p = queryParam.get(i);
		  tempRef = localVars.getTempRef(p);

			if(i != queryParam.size() - 1)
				ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";
			else
				ret += obj + " = " + tempMap + ".getSet(" + tempRef + ");\n";

		}
		ret += monitors + " = ";
		ret += "(" + monitorSet.getName() + ")" + obj + ";\n";
		ret += "if (" + monitors + " == null) {\n";
		ret += monitors + " = new " + monitorSet.getName() + "();\n";
		ret += tempMap + ".putSet(" + localVars.getTempRef(getLastParam()) + ", " + monitors + ");\n";
		ret += "}\n";

		ret += monitors + ".add(" + monitor + ");\n";

		
//		if(cache != null){
//			ret += cache.setCacheKeys(localVars);
//			
//			if (containsSet()) {
//				ret += cache.setCacheSet(monitors);
//			}
//
//			if (cache.hasNode){
//				ret += cache.setCacheNode(null);
//			}
//		}

		return ret;
	}

	public boolean containsSet() {
		return true;
	}

	public String retrieveTree() {
		if(parentTree != null)
			return parentTree.retrieveTree();
		
		if (perthread) {
			String ret = "";

			ret += "(";
			ret += "(com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap)";
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
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMBasicRefMapOfSetMon";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMTagRefMapOfSetMon";
				} else {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMMultiTagRefMapOfSetMon";
				}
			} else {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMBasicRefMapOfAll";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMTagRefMapOfAll";
				} else {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMMultiTagRefMapOfAll";
				}
			}
		} else {
			if (queryParam.size() == 1) {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMBasicRefMapOfSet";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMTagRefMapOfSet";
				} else {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMMultiTagRefMapOfSet";
				}
			} else {
				if (parasiticRefTree.generalProperties.size() == 0) {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMBasicRefMapOfMapSet";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMTagRefMapOfMapSet";
				} else {
					ret = "com.runtimeverification.rvmonitor.java.rt.map.RVMMultiTagRefMapOfMapSet";
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

				if(isGeneral){
					if (queryParam.size() == 1) {
						ret += "new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSetMon(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					} else {
						ret += "new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					}
				} else {
					if (queryParam.size() == 1) {
						ret += "new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					} else {
						ret += "new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					}
				}
	
				ret += "}\n";
				ret += "};\n";
			} else {
				if(parasiticRefTree == null){
					if(isGeneral){
						if (queryParam.size() == 1) {
							ret += "static com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap " + name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSetMon(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += "static com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap " + name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += "static com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap " + name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += "static com.runtimeverification.rvmonitor.java.rt.map.RVMAbstractMap " + name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
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
							ret += name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSetMon(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += name + " = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapOfSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
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
