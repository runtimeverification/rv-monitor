package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.advice;

import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.*;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.EventDefinition;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

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
	
	private final InternalBehaviorObservableCodeGenerator internalBehaviorObservableGenerator;

	private boolean isCodeGenerated = false;

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

		this.advices.put(event, AdviceBody.createAdviceBody(mopSpec, event, combinedAspect));

		this.events.add(event);
		if (event.getCountCond() != null && event.getCountCond().length() != 0) {
			this.beCounted = true;
		}

		if(event.isStartEvent())
			specsForActivation.add(mopSpec);
		else
			specsForChecking.add(mopSpec);
		
		this.internalBehaviorObservableGenerator = combinedAspect.getInternalBehaviorObservableGenerator();
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
		this.advices.put(event, AdviceBody.createAdviceBody(mopSpec, event, combinedAspect));
		
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

			if (Main.useFineGrainedLock) {
				if (!Main.suppressActivator) {
					for (RVMonitorSpec spec : specsForActivation) {
						ret += this.activatorsManager.setValue(spec, true);
						ret += ";\n";
					}
				}
			}
			else {
				for(RVMonitorSpec spec : specsForActivation){
					ret += activatorsManager.setValue(spec, true);
					ret += ";\n";
				}
				if (isSync)
					ret += this.globalLock.getAcquireCode();
			}

			Iterator<EventDefinition> iter;
			iter = this.events.iterator();
			
			if (this.beCounted) {
				ret += "++" + this.pointcutName + "_count;\n";
			}

			while(iter.hasNext()){
				EventDefinition event = iter.next();

				AdviceBody advice = advices.get(event);

				ret += this.internalBehaviorObservableGenerator.generateEventMethodEnterCode(event);
				ret += this.statManager.incEvent(advice.mopSpec, event);

				if(specsForChecking.contains(advice.mopSpec)){
					if(advices.size() > 1){
						ret += "//" + advice.mopSpec.getName() + "_" + event.getUniqueId() + "\n";
					}

					if (Main.suppressActivator)
						ret += "{\n";
					else
						ret += "if (" + activatorsManager.getValue(advice.mopSpec) + ") {\n";
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
				ret += this.internalBehaviorObservableGenerator.generateEventMethodLeaveCode(event);
			}

			if (!Main.useFineGrainedLock) {
				if (isSync)
					ret += this.globalLock.getReleaseCode();
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
			ret += "com.runtimeverification.rvmonitor.java.rt.concurrent.BlockingEventThread " + threadName + " = new com.runtimeverification.rvmonitor.java.rt.concurrent.BlockingEventThread(\"" + eventName + "\") {\n";
			
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
			
			// Set name of the blocking event method thread to be the same name
			ret += threadName + ".setName(Thread.currentThread().getName());\n";
			// Start the blocking event method thread
			ret += threadName + ".start();\n";
			ret += " }\n";
			ret += "\n";
		}
		
		if (this.blockingMethodName != null) {
			ret += "private static final void " + pointcutName;
		}
		else {
			ret += "public static final void " + pointcutName;
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
	
	public void generateCode() {
		if (!this.isCodeGenerated) {
//			this.eventManager.generateCode();
			Iterator<EventDefinition> iter;
			iter = this.events.iterator();
			
			while (iter.hasNext()) {
				EventDefinition event = iter.next();
				AdviceBody advice = advices.get(event);
				advice.generateCode();
			}
		}
	
		this.isCodeGenerated = true;
	}
}
