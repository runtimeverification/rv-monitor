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

public class PartialParamIndexingTree extends IndexingTree {

	final static int NODEONLY = 0;
	final static int SETONLY = 1;
	final static int NODEANDSET = 2;

	public PartialParamIndexingTree(String aspectName, MOPParameters queryParam, MOPParameters contentParam, MOPParameters fullParam, MonitorSet monitorSet, SuffixMonitor monitor,
			HashMap<String, RefTree> refTrees, boolean perthread, boolean isGeneral) throws RVMException {
		super(aspectName, queryParam, contentParam, fullParam, monitorSet, monitor, refTrees, perthread, isGeneral);

		if (isFullParam)
			throw new RVMException("PartialParamIndexingTree can be created only when queryParam does not equal to fullParam.");

		if (queryParam.size() == 0)
			throw new RVMException("PartialParamIndexingTree should contain at least one parameter.");

		if (anycontent) {
			this.name = new MOPVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "_Map");
		} else {
			if (!contentParam.contains(queryParam))
				throw new RVMException("[Internal] contentParam should contain queryParam");
			if (contentParam.size() <= queryParam.size())
				throw new RVMException("[Internal] contentParam should be larger than queryParam");

			this.name = new MOPVariable(aspectName + "_" + queryParam.parameterStringUnderscore() + "__To__" + contentParam.parameterStringUnderscore() + "_Map");
		}

		if (anycontent){
			this.cache = new IndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
			//this.cache = new LocalityIndexingCache(this.name, this.queryParam, this.fullParam, this.monitorClass, this.monitorSet, refTrees, perthread, isGeneral);
		}
	}

	public MOPParameter getLastParam() {
		return queryParam.get(queryParam.size() - 1);
	}

	protected String lookupIntermediateCreative(LocalVariables localVars, MOPVariable monitor, MOPVariable lastMap, MOPVariable lastSet, int i, int target) {
		String ret = "";

		MOPVariable obj = localVars.get("obj");
		MOPVariable tempMap = localVars.get("tempMap");

		MOPParameter p = queryParam.get(i);
		MOPVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		MOPParameter nextP = queryParam.get(i + 1);

		ret += "if (" + obj + " == null) {\n";

		if(isGeneral){
			if (i == queryParam.size() - 2)
				ret += obj + " = new rvmonitorrt.map.MOPMapOfSetMon(" + fullParam.getIdnum(nextP) + ");\n";
			else
				ret += obj + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(nextP) + ");\n";
		} else {
			if (i == queryParam.size() - 2)
				ret += obj + " = new rvmonitorrt.map.MOPMapOfSet(" + fullParam.getIdnum(nextP) + ");\n";
			else
				ret += obj + " = new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(nextP) + ");\n";
		}

		ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
		ret += "}\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			if (target == NODEONLY)
				ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, true);
			else if (target == SETONLY)
				ret += lookupSetLast(localVars, monitor, lastMap, lastSet, i + 1, true);
			else if (target == NODEANDSET)
				ret += lookupNodeAndSetLast(localVars, monitor, lastMap, lastSet, i + 1, true);
		} else {
			ret += tempMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateCreative(localVars, monitor, lastMap, lastSet, i + 1, target);
		}

		return ret;
	}

	protected String lookupIntermediateNonCreative(LocalVariables localVars, MOPVariable monitor, MOPVariable lastMap, MOPVariable lastSet, int i, int target) {
		String ret = "";

		MOPVariable obj = localVars.get("obj");
		MOPVariable tempMap = localVars.get("tempMap");

		MOPParameter p = queryParam.get(i);
		MOPVariable tempRef = localVars.getTempRef(p);

		ret += obj + " = " + tempMap + ".getMap(" + tempRef + ");\n";

		ret += "if (" + obj + " != null) {\n";

		if (i == queryParam.size() - 2) {
			ret += lastMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			if (target == NODEONLY)
				ret += lookupNodeLast(localVars, monitor, lastMap, lastSet, i + 1, false);
			else if (target == SETONLY)
				ret += lookupSetLast(localVars, monitor, lastMap, lastSet, i + 1, false);
			else if (target == NODEANDSET)
				ret += lookupNodeAndSetLast(localVars, monitor, lastMap, lastSet, i + 1, false);
		} else {
			ret += tempMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
			ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, lastSet, i + 1, target);
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

	public String lookupNode(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr, boolean creative) {
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
				ret += lookupIntermediateCreative(localVars, monitor, lastMap, null, 0, NODEONLY);
			} else {
				ret += lookupIntermediateNonCreative(localVars, monitor, lastMap, null, 0, NODEONLY);
			}
		}

		return ret;
	}

	protected String lookupSetLast(LocalVariables localVars, MOPVariable monitor, MOPVariable lastMap, MOPVariable lastSet, int i, boolean creative) {
		String ret = "";

		MOPParameter p = queryParam.get(i);
		MOPVariable tempRef = localVars.getTempRef(p);

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

		MOPVariable lastMap = localVars.get(lastMapStr);
		MOPVariable lastSet = localVars.get(lastSetStr);

		if (queryParam.size() == 1) {
			ret += lastMap + " = " + retrieveTree() + ";\n";
			ret += lookupSetLast(localVars, null, lastMap, lastSet, 0, creative);
		} else {
			MOPVariable tempMap = localVars.get("tempMap");
			ret += tempMap + " = " + retrieveTree() + ";\n";

			if (creative) {
				ret += lookupIntermediateCreative(localVars, null, lastMap, lastSet, 0, SETONLY);
			} else {
				ret += lookupIntermediateNonCreative(localVars, null, lastMap, lastSet, 0, SETONLY);
			}
		}

		return ret;
	}

	protected String lookupNodeAndSetLast(LocalVariables localVars, MOPVariable monitor, MOPVariable lastMap, MOPVariable lastSet, int i, boolean creative) {
		String ret = "";

		MOPParameter p = queryParam.get(i);
		MOPVariable tempRef = localVars.getTempRef(p);

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

		MOPVariable monitor = localVars.get(monitorStr);
		MOPVariable lastMap = localVars.get(lastMapStr);
		MOPVariable lastSet = localVars.get(lastSetStr);

		if (queryParam.size() == 1) {
			ret += lastMap + " = " + retrieveTree() + ";\n";
			ret += lookupNodeAndSetLast(localVars, monitor, lastMap, lastSet, 0, creative);
		} else {
			MOPVariable tempMap = localVars.get("tempMap");
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

		MOPVariable monitor = localVars.get(monitorStr);
		MOPVariable lastSet = localVars.get(lastSetStr);

		MOPVariable tempRef = localVars.getTempRef(getLastParam());

		if (queryParam.size() == 1) {
			ret += retrieveTree() + ".putNode(" + tempRef + ", " + monitor + ");\n";
		} else {
			MOPVariable lastMap = localVars.get(lastMapStr);

			ret += lastMap + ".putNode(" + tempRef + ", " + monitor + ");\n";
		}

		ret += lastSet + ".add(" + monitor + ");\n";

		return ret;
	}

	public String attachSet(LocalVariables localVars, String monitorStr, String lastMapStr, String lastSetStr) {
		String ret = "";

		MOPVariable lastSet = localVars.get(lastSetStr);

		MOPVariable tempRef = localVars.getTempRef(getLastParam());

		if (queryParam.size() == 1) {
			ret += retrieveTree() + ".putSet(" + tempRef + ", " + lastSet + ");\n";
		} else {
			MOPVariable lastMap = localVars.get(lastMapStr);

			ret += lastMap + ".putSet(" + tempRef + ", " + lastSet + ");\n";
		}

		return ret;
	}

	public String addMonitor(LocalVariables localVars, String monitorStr, String tempMapStr, String tempSetStr) {
		String ret = "";

		MOPVariable obj = localVars.get("obj");
		MOPVariable tempMap = localVars.get(tempMapStr);
		MOPVariable monitor = localVars.get(monitorStr);
		MOPVariable monitors = localVars.get(tempSetStr);

		ret += tempMap + " = " + retrieveTree() + ";\n";

		MOPParameter p = queryParam.get(0);
		MOPVariable tempRef = localVars.getTempRef(p);

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
						ret += obj + " = new rvmonitorrt.map.MOPMapOfSetMon(" + fullParam.getIdnum(p) + ");\n";
					} else
						ret += obj + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(p) + ");\n";
				} else {
					if (i == queryParam.size() - 1) {
						ret += obj + " = new rvmonitorrt.map.MOPMapOfSet(" + fullParam.getIdnum(p) + ");\n";
					} else
						ret += obj + " = new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(p) + ");\n";
				}

				ret += tempMap + ".putMap(" + tempRef + ", " + obj + ");\n";
				ret += "}\n";

				ret += tempMap + " = (rvmonitorrt.map.MOPAbstractMap)" + obj + ";\n";
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
					ret = "rvmonitorrt.map.MOPBasicRefMapOfSetMon";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.MOPTagRefMapOfSetMon";
				} else {
					ret = "rvmonitorrt.map.MOPMultiTagRefMapOfSetMon";
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
					ret = "rvmonitorrt.map.MOPBasicRefMapOfSet";
				} else if (parasiticRefTree.generalProperties.size() == 1) {
					ret = "rvmonitorrt.map.MOPTagRefMapOfSet";
				} else {
					ret = "rvmonitorrt.map.MOPMultiTagRefMapOfSet";
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

				if(isGeneral){
					if (queryParam.size() == 1) {
						ret += "new rvmonitorrt.map.MOPMapOfSetMon(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					} else {
						ret += "new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					}
				} else {
					if (queryParam.size() == 1) {
						ret += "new rvmonitorrt.map.MOPMapOfSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					} else {
						ret += "new rvmonitorrt.map.MOPMapOfMapSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
					}
				}
	
				ret += "}\n";
				ret += "};\n";
			} else {
				if(parasiticRefTree == null){
					if(isGeneral){
						if (queryParam.size() == 1) {
							ret += "static rvmonitorrt.map.MOPAbstractMap " + name + " = new rvmonitorrt.map.MOPMapOfSetMon(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += "static rvmonitorrt.map.MOPAbstractMap " + name + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += "static rvmonitorrt.map.MOPAbstractMap " + name + " = new rvmonitorrt.map.MOPMapOfSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
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
							ret += name + " = new rvmonitorrt.map.MOPMapOfSetMon(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						} else {
							ret += name + " = new rvmonitorrt.map.MOPMapOfAll(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
						}
					} else {
						if (queryParam.size() == 1) {
							ret += name + " = new rvmonitorrt.map.MOPMapOfSet(" + fullParam.getIdnum(queryParam.get(0)) + ");\n";
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
