package rvmonitor.output.combinedaspect.event;

import rvmonitor.RVMException;
import rvmonitor.Main;
import rvmonitor.output.EnableSet;
import rvmonitor.output.combinedaspect.CombinedAspect;
import rvmonitor.output.combinedaspect.event.advice.Advice;
import rvmonitor.output.monitor.SuffixMonitor;
import rvmonitor.output.monitorset.MonitorSet;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {

	public ArrayList<Advice> advices = new ArrayList<Advice>();
	public ArrayList<EndObject> endObjectEvents = new ArrayList<EndObject>();
	public ArrayList<EndThread> endThreadEvents = new ArrayList<EndThread>();
	public ArrayList<StartThread> startThreadEvents = new ArrayList<StartThread>();
	public EndProgram endProgramEvent = null;

	public HashMap<RVMonitorSpec, MonitorSet> monitorSets;
	public HashMap<RVMonitorSpec, SuffixMonitor> monitors;
	public HashMap<RVMonitorSpec, EnableSet> enableSets;
	
	public EventManager(String name, List<RVMonitorSpec> specs, CombinedAspect combinedAspect) throws RVMException {
		this.monitorSets = combinedAspect.monitorSets;
		this.monitors = combinedAspect.monitors;
		this.enableSets = combinedAspect.enableSets;

		this.endProgramEvent = new EndProgram(name);

		for (RVMonitorSpec spec : specs) {
			if (spec.isEnforce()) {
				endThreadEvents.add(new ThreadStatusMonitor(spec, combinedAspect));
			}
			for (EventDefinition event : spec.getEvents()) {
				// normal event
				if (!event.isEndObject() && !event.isEndProgram() && !event.isEndThread() && !event.isStartThread()) {
					advices.add(new Advice(spec, event, combinedAspect));
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

	public String printConstructor() {
		String ret = "";

		if (endProgramEvent != null) {
			ret += endProgramEvent.printAddStatement();
		}

		return ret;
	}

	public String advices() {
		String ret = "";

		int numAdvice = 1;
		for (Advice advice : advices) {
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
