package rvmonitor.output.combinedaspect.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rvmonitor.MOPException;
import rvmonitor.Main;
import rvmonitor.output.EnableSet;
import rvmonitor.output.MOPVariable;
import rvmonitor.output.combinedaspect.CombinedAspect;
import rvmonitor.output.combinedaspect.event.advice.AdviceAndPointCut;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.aspectj.PointCut;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.JavaMOPSpec;
import rvmonitor.parser.ast.visitor.ConvertPointcutToCNFVisitor;

public class EventManager {

	public ArrayList<AdviceAndPointCut> advices = new ArrayList<AdviceAndPointCut>();
	public ArrayList<EndObject> endObjectEvents = new ArrayList<EndObject>();
	public ArrayList<EndThread> endThreadEvents = new ArrayList<EndThread>();
	public ArrayList<StartThread> startThreadEvents = new ArrayList<StartThread>();
	public EndProgram endProgramEvent = null;

	public HashMap<JavaMOPSpec, MonitorSet> monitorSets;
	public HashMap<JavaMOPSpec, SuffixMonitor> monitors;
	public HashMap<JavaMOPSpec, EnableSet> enableSets;
	
	MOPVariable commonPointcut = new MOPVariable("MOP_CommonPointCut");

	public EventManager(String name, List<JavaMOPSpec> specs, CombinedAspect combinedAspect) throws MOPException {
		this.monitorSets = combinedAspect.monitorSets;
		this.monitors = combinedAspect.monitors;
		this.enableSets = combinedAspect.enableSets;

		this.endProgramEvent = new EndProgram(name);

		for (JavaMOPSpec spec : specs) {
			if (spec.isEnforce()) {
				endThreadEvents.add(new ThreadStatusMonitor(spec, combinedAspect));
			}
			for (EventDefinition event : spec.getEvents()) {
				// normal event
				if (!event.isEndObject() && !event.isEndProgram() && !event.isEndThread() && !event.isStartThread()) {
					boolean added = false;
					for (AdviceAndPointCut advice : advices) {
						if (advice.isAround != event.getPos().equals("around"))
							continue;
						if (advice.isAround) {
							if (!advice.retType.equals(event.getRetType().toString()))
								continue;
						}
						if (!advice.pos.equals(event.getPos()))
							continue;
						if (!advice.retVal.equals(event.getRetVal()))
							continue;
						if (!advice.throwVal.equals(event.getThrowVal()))
							continue;

						PointcutComparator comparator = new PointcutComparator();
						PointCut p1 = event.getPointCut().accept(new ConvertPointcutToCNFVisitor(), null);
						PointCut p2 = advice.getPointCut().accept(new ConvertPointcutToCNFVisitor(), null);
						
						if (comparator.compare(p1, p2)) {
							added = advice.addEvent(spec, event, combinedAspect);
							if(added)
								break;
						}
					}

					if (!added) {
						advices.add(new AdviceAndPointCut(spec, event, combinedAspect));
					}
				}

				// endObject
				if (event.isEndObject()) {
					endObjectEvents.add(new EndObject(spec, event, combinedAspect));
				}

				// endThread
				if (event.isEndThread()) {
					endThreadEvents.add(new EndThread(spec, event, combinedAspect));
				}

				// startThread
				if (event.isStartThread()) {
					startThreadEvents.add(new StartThread(spec, event, combinedAspect));
				}

				// endProgram
				if (event.isEndProgram()) {
					endProgramEvent.addEndProgramEvent(spec, event, combinedAspect);
				}

			} // end of for event

		} // end of for spec

		endProgramEvent.registerEndThreadEvents(endThreadEvents);

	}

	public MonitorSet getMonitorSet(JavaMOPSpec spec) {
		return monitorSets.get(spec);
	}

	public SuffixMonitor getMonitor(JavaMOPSpec spec) {
		return monitors.get(spec);
	}

	public EnableSet getEnableSet(JavaMOPSpec spec) {
		return enableSets.get(spec);
	}

	public String printConstructor() {
		String ret = "";

		if (endProgramEvent != null) {
			ret += endProgramEvent.printAddStatement();
		}

		return ret;
	}

	public String advices() {
		String ret = "";

		ret += "pointcut " + commonPointcut + "() : ";
		if(Main.dacapo){
			ret += "!within(rvmonitorrt.MOPObject+) && !adviceexecution() && BaseAspect.notwithin();\n";
		} else {
			ret += "!within(rvmonitorrt.MOPObject+) && !adviceexecution();\n";
		}
		
		int numAdvice = 1;
		for (AdviceAndPointCut advice : advices) {
			if(Main.empty_advicebody){
				ret += "// " + numAdvice++ + "\n";
			}

			ret += advice;
			ret += "\n";
			if (advice.beCounted) {
				ret += "\n";
				ret += "// Declaration of the count variable for above pointcut\n";
				ret += "static int " + advice.getPointCutName() + "_count = 0;";
				ret += "\n\n\n";
			}
		}

		for (EndObject endObject : endObjectEvents) {
			ret += endObject;
			ret += "\n";
		}

		for (EndThread endThread : endThreadEvents) {
			ret += endThread.printAdvices();
			ret += "\n";
		}

		for (StartThread startThread : startThreadEvents) {
			ret += startThread.printAdvices();
			ret += "\n";
		}

		ret += endProgramEvent.printHookThread();

		return ret;
	}

}