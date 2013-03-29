package rvmonitor.output.combinedaspect.indexingtree;

import java.util.HashMap;
import java.util.List;

import rvmonitor.RVMException;
import rvmonitor.output.EnableSet;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.JavaMOPSpec;
import rvmonitor.parser.ast.mopspec.MOPParameter;

public class IndexingTreeManager {

	HashMap<JavaMOPSpec, IndexingDecl> trees = new HashMap<JavaMOPSpec, IndexingDecl>();
	
	public HashMap<String, RefTree> refTrees = new HashMap<String, RefTree>();

	public IndexingTreeManager(String name, List<JavaMOPSpec> specs, HashMap<JavaMOPSpec, MonitorSet> monitorSets, HashMap<JavaMOPSpec, SuffixMonitor> monitors,
			HashMap<JavaMOPSpec, EnableSet> enableSets) throws RVMException {
		getRefTrees(name, specs);
		
		for (JavaMOPSpec spec : specs) {
			MonitorSet monitorSet = monitorSets.get(spec);
			SuffixMonitor monitor = monitors.get(spec);
			EnableSet enableSet = enableSets.get(spec);

			trees.put(spec, new IndexingDecl(spec, monitorSet, monitor, enableSet, refTrees));
		}
	}
	
	protected void getRefTrees(String name, List<JavaMOPSpec> specs) throws RVMException {
		for (JavaMOPSpec spec : specs){
			for(MOPParameter param : spec.getParameters()){
				RefTree refTree = refTrees.get(param.getType().toString());
				
				if(refTree == null){
					refTree = new RefTree(name, param);

					refTrees.put(param.getType().toString(), refTree);
				}
				
				refTree.addProperty(spec);
			}
		}
	}
	
	public IndexingDecl getIndexingDecl(JavaMOPSpec spec) {
		return trees.get(spec);
	}

	public String decl() {
		String ret = "";

		if (trees.size() <= 0)
			return ret;

//		int count = 0;
//		for (IndexingDecl indexDecl : trees.values()) {
//			for(IndexingTree tree : indexDecl.indexingTrees.values()){
//				//if(tree.parentTree == null && tree.queryParam.size() > 0)
//				if(tree.queryParam.size() > 0)
//					count++;
//			}
//			
//			for(IndexingTree tree : indexDecl.indexingTreesForCopy.values()){
//				//if(tree.parentTree == null && tree.queryParam.size() > 0)
//				if(tree.queryParam.size() > 0)
//					count++;
//			}
//		}
//		System.out.println(count);

		ret += "// Declarations for Indexing Trees \n";
		for (IndexingDecl indexDecl : trees.values()) {
			ret += indexDecl;
		}
		ret += "\n";

		ret += "// Trees for References\n";
		for (RefTree refTree : refTrees.values()){
			ret += refTree;
		}
		ret += "\n";

		return ret;
	}

	public String reset() {
		String ret = "";

		if (trees.size() <= 0)
			return ret;

		for (IndexingDecl indexDecl : trees.values()) {
			ret += indexDecl.reset();
		}
		ret += "\n";

		for (RefTree refTree : refTrees.values()){
			ret += refTree.reset();
		}
		ret += "\n";

		return ret;
	}

}
