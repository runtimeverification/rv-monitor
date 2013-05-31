package com.runtimeverification.rvmonitor.java.rvj;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;

import java.util.HashMap;

public class RVMErrorChecker {

	public static void verify(RVMonitorSpec rvMonitorSpec) throws RVMException {
		for (PropertyAndHandlers prop : rvMonitorSpec.getPropertiesAndHandlers()) {
			verifyHandlers(prop);
		}
		for (EventDefinition event : rvMonitorSpec.getEvents()) {
			//endProgram cannot have any parameter.
			verifyEndProgramParam(event);

			//endThread cannot have any parameter except one from thread pointcut.
			verifyEndThreadParam(event);
		}
		
		//there should be only one endProgram event
		verifyUniqueEndProgram(rvMonitorSpec);
		
		verifySameEventName(rvMonitorSpec);
		verifyGeneralParametric(rvMonitorSpec);
		
		//check if two endObject pointcuts share the same parameter, which should not happen
		
		
	}

	public static void verifyHandlers(PropertyAndHandlers prop) throws RVMException {
		for (String handlerName : prop.getHandlers().keySet()) {
			if (prop.getLogicProperty(handlerName + " condition") == null) {
				throw new RVMException(handlerName + " is not a supported state in this logic, " + prop.getProperty().getType() + ".");
			}
		}
	}

	public static void verifySameEventName(RVMonitorSpec mopSpec) throws RVMException {
		HashMap<String, RVMParameters> nameToParam = new HashMap<String, RVMParameters>();
		
		for(EventDefinition event : mopSpec.getEvents()){
			if(nameToParam.get(event.getId()) != null){
				if(event.getParameters().equals(nameToParam.get(event.getId()))){
					String prettyname = mopSpec.getName() + "." + event.getId();
					throw new RVMException("An event that has the same name and signature has been already defined: " + prettyname);
				}
			} else {
				nameToParam.put(event.getId(), event.getParameters());
			}
		}
	}
	
	public static void verifyUniqueEndProgram(RVMonitorSpec mopSpec) throws RVMException {
		boolean found = false;
		
		for(EventDefinition event : mopSpec.getEvents()){
			if(event.isEndProgram()){
				if(found)
					throw new RVMException("There can be only one endProgram event");
				else
					found = true;
			}
		}
	}
	
	public static void verifyGeneralParametric(RVMonitorSpec mopSpec) throws RVMException {
		if(mopSpec.isGeneral() && mopSpec.getParameters().size() == 0)
			throw new RVMException("[Internal Error] It cannot use general parameteric algorithm when there is no parameter");
	}
	
	public static void verifyEndProgramParam(EventDefinition event) throws RVMException {
		if(event.isEndProgram() && event.getParameters().size() >0)
			throw new RVMException("A endProgram pointcut cannot have any parameter.");
	}

	public static void verifyEndThreadParam(EventDefinition event) throws RVMException {
		if(event.isEndThread())
			if(event.getParametersWithoutThreadVar().size() >0)
			throw new RVMException("A endThread pointcut cannot have any parameter except one from thread pointcut.");
	}

}
