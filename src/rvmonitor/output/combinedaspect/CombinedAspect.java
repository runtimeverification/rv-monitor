package rvmonitor.output.combinedaspect;

import rvmonitor.RVMException;
import rvmonitor.Main;
import rvmonitor.output.EnableSet;
import rvmonitor.output.MOPVariable;
import rvmonitor.output.combinedaspect.event.EventManager;
import rvmonitor.output.combinedaspect.indexingtree.IndexingDecl;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTree;
import rvmonitor.output.combinedaspect.indexingtree.IndexingTreeManager;
import rvmonitor.output.combinedaspect.indexingtree.reftree.RefTree;
import rvmonitor.output.monitor.BaseMonitor;
import rvmonitor.output.monitor.Monitor;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.MOPSpecFile;
import rvmonitor.parser.ast.mopspec.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinedAspect {
	String name;
	public HashMap<JavaMOPSpec, MonitorSet> monitorSets;
	public HashMap<JavaMOPSpec, SuffixMonitor> monitors;
	public HashMap<JavaMOPSpec, EnableSet> enableSets;
	public HashMap<JavaMOPSpec, HashSet<MOPParameter>> setOfParametersForDisable;

	MOPVariable mapManager;
	boolean versionedStack;

	List<JavaMOPSpec> specs;
	public MOPStatManager statManager;
	public LockManager lockManager;
	public TimestampManager timestampManager;
	public ActivatorManager activatorsManager;
	public IndexingTreeManager indexingTreeManager;
	public EventManager eventManager;

	boolean has__ACTIVITY = false;

	public CombinedAspect(String name, MOPSpecFile mopSpecFile, HashMap<JavaMOPSpec, MonitorSet> monitorSets, HashMap<JavaMOPSpec, SuffixMonitor> monitors,
			HashMap<JavaMOPSpec, EnableSet> enableSets, boolean versionedStack) throws RVMException {
		this.name = name + "RuntimeMonitor";
		this.monitorSets = monitorSets;
		this.monitors = monitors;
		this.enableSets = enableSets;
		this.versionedStack = versionedStack;

		this.specs = mopSpecFile.getSpecs();
		for (JavaMOPSpec spec : specs) {
			if (spec.has__ACTIVITY()) has__ACTIVITY = true;
		}
		this.statManager = new MOPStatManager(name, this.specs);
		this.lockManager = new LockManager(name, this.specs);
		this.timestampManager = new TimestampManager(name, this.specs);
		this.activatorsManager = new ActivatorManager(name, this.specs);
		this.indexingTreeManager = new IndexingTreeManager(name, this.specs, this.monitorSets, this.monitors, this.enableSets);

		collectDisableParameters(mopSpecFile.getSpecs());

		this.eventManager = new EventManager(name, this.specs, this);

		this.mapManager = new MOPVariable(name + "MapManager");
	}
	
	public void collectDisableParameters(List<JavaMOPSpec> specs){
		this.setOfParametersForDisable = new HashMap<JavaMOPSpec, HashSet<MOPParameter>>();
		for(JavaMOPSpec spec : specs){
			HashSet<MOPParameter> parametersForDisable = new HashSet<MOPParameter>();
			
			for(EventDefinition event : spec.getEvents()){
				MOPParameters eventParams = event.getMOPParametersOnSpec();
				MOPParameterSet enable = enableSets.get(spec).getEnable(event.getId());
				
				for (MOPParameters enableEntity : enable) {
					if (enableEntity.size() == 0 && !spec.hasNoParamEvent())
						continue;
					if (enableEntity.contains(eventParams))
						continue;
					
					MOPParameters unionOfEnableEntityAndParam = MOPParameters.unionSet(enableEntity, eventParams);
					
					for (MOPParameter p : unionOfEnableEntityAndParam){
						if(!enableEntity.contains(p)){
							parametersForDisable.add(p);
						}
					}
				}
			}
			
			this.setOfParametersForDisable.put(spec, parametersForDisable);
		}
	}

	public String getAspectName() {
		return name;
	}
	
	public String constructor(){
		String ret = "";
		
		HashMap<String, RefTree> refTrees = indexingTreeManager.refTrees;
		
		for(JavaMOPSpec spec : specs){
			IndexingDecl indexDecl = indexingTreeManager.getIndexingDecl(spec);
			
			for(IndexingTree indexTree : indexDecl.getIndexingTrees().values()){
				MOPParameters param = indexTree.queryParam;
				
				if(param.size() == 0)
					continue;
				
				RefTree refTree = refTrees.get(param.get(0).getType().toString());
				
				if(refTree.hostIndexingTree != indexTree)
					ret += refTree.getName() + ".addCleaningChain(" + indexTree.getName() + ");\n";
			}
			
		}
		
		return ret;
	}

	public String initCache(){
		String ret = "";
		
		for(JavaMOPSpec spec : specs){
			IndexingDecl decl = indexingTreeManager.getIndexingDecl(spec);
		
			for(IndexingTree tree : decl.getIndexingTrees().values()){
				if(tree.cache != null){
					ret += tree.cache.init();
				}
			}
		}
		
		
		return ret;
	}


	public String categoryVarsDecl() {
		boolean skipEvent = false;
		Set<MOPVariable> categoryVars = new HashSet<MOPVariable>();
		for (JavaMOPSpec mopSpec : this.specs) {
			if (mopSpec.has__SKIP()) {
				skipEvent = true;
			}
			MonitorSet monitorSet =  monitorSets.get(mopSpec);
			Monitor monitorClass = monitors.get(mopSpec);
			categoryVars.addAll(monitorSet.getCategoryVars());
			categoryVars.addAll(monitorClass.getCategoryVars());
		}
		String ret = "";
		for (MOPVariable variable : categoryVars) {
			ret += "public static boolean " +
					BaseMonitor.getNiceVariable(variable) + " = " +
					"false;\n";
		}
		if (skipEvent) {
			ret += "public static boolean " + BaseMonitor.skipEvent + " = false;" +
					"\n";
		}
		return ret;
	}


	public String toString() {
		String ret = "";

		ret += this.statManager.statClass();
		
		ret += "public class " + this.name + " implements rvmonitorrt.MOPObject {\n";

		ret += categoryVarsDecl();

		ret += "private static rvmonitorrt.map.MOPMapManager " + mapManager + ";\n";

		ret += this.statManager.fieldDecl2();

		// constructor
		ret += "static {\n";

		ret += this.eventManager.printConstructor();
		
		ret += mapManager + " = " + "new rvmonitorrt.map.MOPMapManager();\n";
		ret += mapManager + ".start();\n";

		ret += this.statManager.constructor();
		
		//ret += constructor();
		//ret += initCache();
		
		ret += "}\n";
		ret += "\n";

		ret += this.statManager.fieldDecl();

		ret += this.lockManager.decl();

		ret += this.timestampManager.decl();

		ret += this.activatorsManager.decl();

		ret += this.indexingTreeManager.decl();

		ret += this.eventManager.advices();

		if (this.has__ACTIVITY) {
			ret += "public static void onCreateActivity(Activity a) {\n";
			for (Monitor m : monitors.values()) {
				if (m.has__ACTIVITY()) {
					ret += m.getOutermostName() + "." + m.getActivityName() + " = a;\n";
				}
			}
			ret += "}\n";
			ret += "\n";
		}

		ret += this.statManager.advice();

		if(Main.dacapo2){
			ret += "// after () : (execution(* avrora.Main.main(..)) || call(* dacapo.Benchmark.run(..)) || call(* org.dacapo.harness.Benchmark.run(..))) {\n";

			ret += "public static void resetDaCapo() {\n";

			//ret += "System.err.println(\"reset \" + Thread.currentThread().getName());\n";
			
			ret += this.timestampManager.reset();

			ret += this.activatorsManager.reset();

			ret += this.indexingTreeManager.reset();
			
			ret += "}\n";
		}

		ret += "}\n";

		return ret;
	}
}
