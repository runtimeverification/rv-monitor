package rvmonitor.output.combinedaspect.event.advice;

import rvmonitor.RVMException;
import rvmonitor.Main;
import rvmonitor.output.MOPVariable;
import rvmonitor.output.combinedaspect.*;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.JavaMOPSpec;
import rvmonitor.parser.ast.mopspec.MOPParameter;
import rvmonitor.parser.ast.mopspec.MOPParameters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class Advice {
	public MOPStatManager statManager;
	public ActivatorManager activatorsManager;

	MOPVariable inlineFuncName;
	MOPParameters inlineParameters;

	MOPVariable pointcutName;
	MOPParameters parameters;

	boolean hasThisJoinPoint;
	public boolean beCounted = false;
	public MOPParameters threadVars = new MOPParameters();
	GlobalLock globalLock;
	boolean isSync;

	LinkedList<EventDefinition> events = new LinkedList<EventDefinition>();
	HashSet<JavaMOPSpec> specsForActivation = new HashSet<JavaMOPSpec>();
	HashSet<JavaMOPSpec> specsForChecking = new HashSet<JavaMOPSpec>();
	
	HashMap<EventDefinition, AdviceBody> advices = new HashMap<EventDefinition, AdviceBody>();

	public Advice(JavaMOPSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {
		this.hasThisJoinPoint = mopSpec.hasThisJoinPoint();

		this.pointcutName = new MOPVariable(event.getId() + "Event");
		this.inlineFuncName = new MOPVariable("MOPInline" + mopSpec.getName() + "_" + event.getUniqueId());
		this.parameters = event.getParametersWithoutThreadVar();
		this.inlineParameters = event.getMOPParametersWithoutThreadVar();

		if (event.getThreadVar() != null && event.getThreadVar().length() != 0) {
			if (event.getParameters().getParam(event.getThreadVar()) == null)
				throw new RVMException("thread variable is not included in the event definition.");

			this.threadVars.add(event.getParameters().getParam(event.getThreadVar()));
		}

		this.statManager = combinedAspect.statManager;
		
		this.activatorsManager = combinedAspect.activatorsManager;
		
		this.globalLock = combinedAspect.lockManager.getLock();
		this.isSync = mopSpec.isSync();

		this.advices.put(event, new GeneralAdviceBody(mopSpec, event, combinedAspect));

		this.events.add(event);
		if (event.getCountCond() != null && event.getCountCond().length() != 0) {
			this.beCounted = true;
		}

		if(event.isStartEvent())
			specsForActivation.add(mopSpec);
		else
			specsForChecking.add(mopSpec);
	}

	public String getPointCutName() {
		return pointcutName.getVarName();
	}
	
	public boolean addEvent(JavaMOPSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {

		// Parameter Conflict Check
		for(MOPParameter param : event.getParametersWithoutThreadVar()){
			MOPParameter param2 = parameters.getParam(param.getName());
			
			if(param2 == null)
				continue;
			
			if(!param.getType().equals(param2.getType())){
				return false;
			}
		}
		
		parameters.addAll(event.getParametersWithoutThreadVar());

		if (event.getThreadVar() != null && event.getThreadVar().length() != 0) {
			if (event.getParameters().getParam(event.getThreadVar()) == null)
				throw new RVMException("thread variable is not included in the event definition.");

			this.threadVars.add(event.getParameters().getParam(event.getThreadVar()));
		}

		// add an advice body.
		this.advices.put(event, new GeneralAdviceBody(mopSpec, event, combinedAspect));
		
		this.events.add(event);
		if (event.getCountCond() != null && event.getCountCond().length() != 0) {
			this.beCounted = true;
		}
		if(event.isStartEvent())
			specsForActivation.add(mopSpec);
		else
			specsForChecking.add(mopSpec);
		return true;
	}
	
	protected String adviceBody(){
		String ret = "";

		if(Main.empty_advicebody){
			ret += "System.out.print(\"\");\n";

			Iterator<EventDefinition> iter;
			iter = this.events.iterator();
			
			if (this.beCounted) {
				ret += "++" + this.pointcutName + "_count;\n";
			}
			
			while(iter.hasNext()){
				EventDefinition event = iter.next(); 
						
				AdviceBody advice = advices.get(event);
	
				if(advices.size() > 1){
					ret += "//" + advice.mopSpec.getName() + "_" + event.getUniqueId() + "\n";
				}
			}
		} else {
			for (MOPParameter threadVar : threadVars) {
				ret += "Thread " + threadVar.getName() + " = Thread.currentThread();\n";
			}

			for(JavaMOPSpec spec : specsForActivation){
				ret += activatorsManager.getActivator(spec) + " = true;\n";
			}

			if (isSync) {
				ret += "while (!" + globalLock.getName() + ".tryLock()) {\n";
				ret += "Thread.yield();\n";
				ret += "}\n";
			}

			Iterator<EventDefinition> iter;
			iter = this.events.iterator();
			
			if (this.beCounted) {
				ret += "++" + this.pointcutName + "_count;\n";
			}

			while(iter.hasNext()){
				EventDefinition event = iter.next();

				AdviceBody advice = advices.get(event);

				ret += this.statManager.incEvent(advice.mopSpec, event);

				if(specsForChecking.contains(advice.mopSpec)){
					if(advices.size() > 1){
						ret += "//" + advice.mopSpec.getName() + "_" + event.getUniqueId() + "\n";
					}

					ret += "if (" + activatorsManager.getActivator(advice.mopSpec) + ") {\n";
				} else {
					if(advices.size() > 1){
						ret += "//" + advice.mopSpec.getName() + "_" + event.getUniqueId() + "\n";
						ret += "{\n";
					}
				}

				if (Main.statistics) {
					MOPStatistics stat = this.statManager.getStat(advice.mopSpec);
					
					ret += stat.eventInc(event.getId());
	
					for (MOPParameter param : event.getMOPParametersOnSpec()) {
						ret += stat.paramInc(param);
					}
	
					ret += "\n";
				}

				// add check count condition here
				String countCond = event.getCountCond();
				
				if (countCond != null && countCond.length() != 0) {
					countCond = countCond.replaceAll("count", this.pointcutName + "_count");
					ret += "if (" + countCond + ") {\n";
				}
				ret += advice;

				if (countCond != null && countCond.length() != 0) {
					ret += "}\n";
				}
				
				if(specsForChecking.contains(advice.mopSpec)){
					ret += "}\n";
				} else {
					if(advices.size() > 1){
						ret += "}\n";
					}
				}
			}

			if (isSync) {
				ret += globalLock.getName() + ".unlock();\n";
			}

		}
		
		return ret;
	}

	public String toString() {
		String ret = "";

		if(Main.inline){
			ret += "void " + inlineFuncName + "(" + inlineParameters.parameterDeclString();
			if(hasThisJoinPoint){
				if(inlineParameters.size() > 0) 
					ret += ", ";
				ret += "JoinPoint thisJoinPoint";
			}
			ret += ") {\n";

			ret += adviceBody();
			
			ret += "}\n";
		}


		ret += "public static void " + pointcutName;
		ret += "(";
		ret += parameters.parameterDeclString();
		ret += ")";
		ret += " {\n";

		if(Main.inline){
			ret += inlineFuncName + "(" + inlineParameters.parameterString();
			if(hasThisJoinPoint){
				if(inlineParameters.size() > 0) 
					ret += ", ";
				ret += "thisJoinPoint";
			}
			ret += ");\n";
		} else {
			ret += adviceBody();
		}

		ret += "}\n";

		return ret;
	}
}
