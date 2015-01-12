package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.EnableSet;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeFormatters;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.EventManager;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTreeManager;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingDeclNew;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeInterface;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.BaseMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.Monitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class CombinedAspect {
	private final String name;
	public final TreeMap<RVMonitorSpec, MonitorSet> monitorSets;
	public final TreeMap<RVMonitorSpec, SuffixMonitor> monitors;
	public final TreeMap<RVMonitorSpec, EnableSet> enableSets;
	public HashMap<RVMonitorSpec, HashSet<RVMParameter>> setOfParametersForDisable;

	final RVMVariable mapManager;
	final boolean versionedStack;
	private final RuntimeServiceManager runtimeServiceManager;

	final List<RVMonitorSpec> specs;
	public final RVMonitorStatManager statManager;
	public final LockManager lockManager;
	public final TimestampManager timestampManager;
	public final ActivatorManager activatorsManager;
	public final IndexingTreeManager indexingTreeManager;
	public final EventManager eventManager;

	private boolean has__ACTIVITY = false;
	private boolean isCodeGenerated = false;
	
	public InternalBehaviorObservableCodeGenerator getInternalBehaviorObservableGenerator() {
		return this.runtimeServiceManager.getObserver();
	}

	public CombinedAspect(String name, RVMSpecFile rvmSpecFile, TreeMap<RVMonitorSpec, MonitorSet> monitorSets, TreeMap<RVMonitorSpec, SuffixMonitor> monitors,
			TreeMap<RVMonitorSpec, EnableSet> enableSets, boolean versionedStack) throws RVMException {
		this.name = name + "RuntimeMonitor";
		this.monitorSets = monitorSets;
		this.monitors = monitors;
		this.enableSets = enableSets;
		this.versionedStack = versionedStack;
		this.runtimeServiceManager = new RuntimeServiceManager();

		this.specs = rvmSpecFile.getSpecs();
		for (RVMonitorSpec spec : specs) {
			if (spec.has__ACTIVITY()) has__ACTIVITY = true;
		}
		this.statManager = new RVMonitorStatManager(name, this.specs);
		this.lockManager = new LockManager(name, this.specs);
		this.timestampManager = new TimestampManager(name, this.specs);
		this.activatorsManager = new ActivatorManager(name, this.specs);
		this.indexingTreeManager = new IndexingTreeManager(name, this.specs, this.monitorSets, this.monitors, this.enableSets);

		collectDisableParameters(rvmSpecFile.getSpecs());

		this.eventManager = new EventManager(name, this.specs, this);

		this.mapManager = new RVMVariable(name + "MapManager");
	}
	
	public void collectDisableParameters(List<RVMonitorSpec> specs){
		this.setOfParametersForDisable = new HashMap<RVMonitorSpec, HashSet<RVMParameter>>();
		for(RVMonitorSpec spec : specs){
			HashSet<RVMParameter> parametersForDisable = new HashSet<RVMParameter>();
			
			for(EventDefinition event : spec.getEvents()){
				RVMParameters eventParams = event.getRVMParametersOnSpec();
				RVMParameterSet enable = enableSets.get(spec).getEnable(event.getId());
				
				for (RVMParameters enableEntity : enable) {
					if (enableEntity.size() == 0 && !spec.hasNoParamEvent())
						continue;
					if (enableEntity.contains(eventParams))
						continue;
					
					RVMParameters unionOfEnableEntityAndParam = RVMParameters.unionSet(enableEntity, eventParams);
					
					for (RVMParameter p : unionOfEnableEntityAndParam){
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
	
	/* This method is never used.
	public String constructor(){
		String ret = "";
		
		HashMap<String, RefTree> refTrees = indexingTreeManager.refTrees;
		
		for(RVMonitorSpec spec : specs){
			IndexingDeclNew indexDecl = indexingTreeManager.getIndexingDecl(spec);
			
			for(IndexingTreeInterface indexTree : indexDecl.getIndexingTrees().values()){
				RVMParameters param = indexTree.getQueryParams();
				
				if(param.size() == 0)
					continue;
				
				RefTree refTree = refTrees.get(param.get(0).getType().toString());
				
				if(refTree.hostIndexingTree != indexTree)
					ret += refTree.getName() + ".addCleaningChain(" + indexTree.getName() + ");\n";
			}
			
		}
		
		return ret;
	}
	*/

	public String initCache(){
		String ret = "";
		
		for(RVMonitorSpec spec : specs){
			IndexingDeclNew decl = indexingTreeManager.getIndexingDecl(spec);
		
			for(IndexingTreeInterface tree : decl.getIndexingTrees().values()){
				if(tree.getCache()!= null){
					// The following is no longer needed.
//					ret += tree.getCache().init();
				}
			}
		}
		
		
		return ret;
	}


	public String categoryVarsDecl() {
		boolean skipEvent = false;
		Set<RVMVariable> categoryVars = new HashSet<RVMVariable>();
		for (RVMonitorSpec mopSpec : this.specs) {
			if (mopSpec.has__SKIP()) {
				skipEvent = true;
			}
			MonitorSet monitorSet =  monitorSets.get(mopSpec);
			Monitor monitorClass = monitors.get(mopSpec);
			categoryVars.addAll(monitorSet.getCategoryVars());
			categoryVars.addAll(monitorClass.getCategoryVars());
		}
		String ret = "";
		if (!Main.eliminatePresumablyRemnantCode) {
			for (RVMVariable variable : categoryVars) {
				ret += "private static boolean " +
						BaseMonitor.getNiceVariable(variable) + " = " +
						"false;\n";
			}
		}
		if (skipEvent) {
			ret += "public static boolean " + BaseMonitor.skipEvent + " = false;" +
					"\n";
		}
		return ret;
	}

	public void generateCode() {
		if (!this.isCodeGenerated) {
			this.eventManager.generateCode();
		}
	
		this.isCodeGenerated = true;
	}

	public String toString() {
		this.generateCode();
	
		String ret = "";

		ret += this.statManager.statClass();
		
		ret += "public final class " + this.name + " implements com.runtimeverification.rvmonitor.java.rt.RVMObject {\n";

		ret += categoryVarsDecl();

		ret += "private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager " + mapManager + ";\n";

		ret += this.statManager.fieldDecl2();

		// constructor
		ret += "static {\n";
		
		ret += this.eventManager.printConstructor();
		
		ret += mapManager + " = " + "new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();\n";
		ret += mapManager + ".start();\n";

		ret += this.statManager.constructor();
		
		//ret += constructor();
		//ret += initCache();
		
		ret += "}\n";
		ret += "\n";

		//ret += this.statManager.fieldDecl();

		ret += this.lockManager.decl();

		ret += this.timestampManager.decl();

		ret += this.activatorsManager.decl();

		ret += this.indexingTreeManager.decl();
		
		{
			ICodeFormatter fmt = CodeFormatters.getDefault();
			this.runtimeServiceManager.getCode(fmt);
			ret += fmt.getCode();
		}
		
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

		ret += "}\n";

		return ret;
	}
}