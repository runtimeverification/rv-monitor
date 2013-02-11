package rvmonitor.output;

import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.MOPParameterSet;
import rvmonitor.parser.ast.mopspec.MOPParameters;
import rvmonitor.util.MOPBooleanSimplifier;

public class OptimizedCoenableSet extends CoEnableSet{
	MOPParameterSet parameterGroups = new MOPParameterSet();
	
	public OptimizedCoenableSet(CoEnableSet coenableSet){
		super(coenableSet.events, coenableSet.specParameters);
		this.contents = coenableSet.contents;
		optimize();
	}
	
	private void optimize(){
		for (EventDefinition event : this.events) {
			MOPParameterSet enables = contents.get(event.getId());
			
			if(enables == null)
				enables = getFullEnable();
			
			MOPParameterSet simplifiedDNF = MOPBooleanSimplifier.simplify(enables, this.specParameters);

			for(MOPParameters param : simplifiedDNF){
				if(param.size() > 0)
					parameterGroups.add(param);
			}
				
			contents.put(event.getId(), simplifiedDNF);
		}
	}

	public MOPParameterSet getParameterGroups(){
		return parameterGroups;
	}
}
