package rvmonitor.output.combinedaspect.event.advice;

import rvmonitor.RVMException;
import rvmonitor.Main;
import rvmonitor.output.RVMVariable;
import rvmonitor.output.combinedaspect.*;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMParameter;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.mopspec.RVMParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Advice {
	public RVMonitorStatManager statManager;
	public ActivatorManager activatorsManager;

	RVMVariable inlineFuncName;
	RVMParameters inlineParameters;

	RVMVariable pointcutName;
	RVMVariable blockingMethodName;
	RVMParameters parameters;

	boolean hasThisJoinPoint;
	public boolean beCounted = false;
	public RVMParameters threadVars = new RVMParameters();
	GlobalLock globalLock;
	boolean isSync;

	LinkedList<EventDefinition> events = new LinkedList<EventDefinition>();
	HashSet<RVMonitorSpec> specsForActivation = new HashSet<RVMonitorSpec>();
	HashSet<RVMonitorSpec> specsForChecking = new HashSet<RVMonitorSpec>();
	
	HashMap<EventDefinition, AdviceBody> advices = new HashMap<EventDefinition, AdviceBody>();

	public Advice(RVMonitorSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {
		this.hasThisJoinPoint = mopSpec.hasThisJoinPoint();

		String prefix = Main.merge ? mopSpec.getName() + "_" : "";
		this.pointcutName = new RVMVariable(prefix + event.getId() + "Event");
		if (event.isBlockingEvent()) {
			this.blockingMethodName = new RVMVariable(event.getId() + "BlockingEvent");
		}
		this.inlineFuncName = new RVMVariable("RVMInline" + mopSpec.getName() + "_" + event.getUniqueId());
		this.parameters = event.getParametersWithoutThreadVar();
		this.inlineParameters = event.getRVMParametersWithoutThreadVar();

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
	
	public boolean addEvent(RVMonitorSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {

		// Parameter Conflict Check
		for(RVMParameter param : event.getParametersWithoutThreadVar()){
			RVMParameter param2 = parameters.getParam(param.getName());
			
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
			for (RVMParameter threadVar : threadVars) {
				ret += "Thread " + threadVar.getName() + " = Thread.currentThread();\n";
			}

			for(RVMonitorSpec spec : specsForActivation){
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
					RVMonitorStatistics stat = this.statManager.getStat(advice.mopSpec);
					
					ret += stat.eventInc(event.getId());
	
					for (RVMParameter param : event.getRVMParametersOnSpec()) {
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

		if (this.blockingMethodName != null) {
			ret += "public static void " + blockingMethodName;
			ret += "(";
			ret += parameters.parameterDeclString();
			ret += ")";
			ret += " {\n";
			
			
//			BlockingEventThread b = new BlockingEventThread() {
//				public void execEvent() {
//					blocked();
//				}
//				
//			};
			
			// Copy parameters to final variables
			List<String> finalParameters = new ArrayList<String>();
			for (RVMParameter p : parameters) {
				ret += "final " + p.getType() + " " + p.getName() + "_final = " + p.getName() + ";\n";
				finalParameters.add(p.getName() + "_final");
			}
			String threadName = this.blockingMethodName + "_thread";
			String eventName = this.pointcutName.toString().substring(0, this.pointcutName.toString().length() - 5);
			ret += "rvmonitorrt.concurrent.BlockingEventThread " + threadName + " = new rvmonitorrt.concurrent.BlockingEventThread(\"" + eventName + "\") {\n";
			
			ret += "public void execEvent() {\n";
			
			// Call the real event method
			ret += pointcutName + "(";
			
			String finalParameter = "";
			for (String p : finalParameters) {
				finalParameter += ", " + p;
			}
			if (finalParameter.length() != 0) {
				finalParameter = finalParameter.substring(2);
			}
			ret += finalParameter;
			ret += ");\n";
			
			ret += " }\n";
			ret += " };\n";
			
			// Start the blocking event method thread
			ret += threadName + ".start();\n";
			ret += " }\n";
			ret += "\n";
		}
		
		if (this.blockingMethodName != null) {
			ret += "private static void " + pointcutName;
		}
		else {
			ret += "public static void " + pointcutName;
		}
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
