package rvmonitor.output;

import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMParameterSet;
import rvmonitor.parser.ast.mopspec.RVMParameters;
import rvmonitor.util.RVMBooleanSimplifier;

public class OptimizedCoenableSet extends CoEnableSet{
	RVMParameterSet parameterGroups = new RVMParameterSet();
	
	public OptimizedCoenableSet(CoEnableSet coenableSet){
		super(coenableSet.events, coenableSet.specParameters);
		this.contents = coenableSet.contents;
		optimize();
	}
	
	private void optimize(){
		for (EventDefinition event : this.events) {
			RVMParameterSet enables = contents.get(event.getId());
			
			if(enables == null)
				enables = getFullEnable();
			
			RVMParameterSet simplifiedDNF = RVMBooleanSimplifier.simplify(enables, this.specParameters);

			for(RVMParameters param : simplifiedDNF){
				if(param.size() > 0)
					parameterGroups.add(param);
			}
				
			contents.put(event.getId(), simplifiedDNF);
		}
	}

	public RVMParameterSet getParameterGroups(){
		return parameterGroups;
	}
}
