package rvmonitor.output.combinedaspect.event;

import java.util.ArrayList;

import rvmonitor.RVMException;
import rvmonitor.output.RVMVariable;
import rvmonitor.output.combinedaspect.CombinedAspect;
import rvmonitor.output.combinedaspect.event.advice.AdviceBody;
import rvmonitor.output.combinedaspect.event.advice.GeneralAdviceBody;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;

public class EndProgram {
	RVMVariable hookName = null;
	String className;

	ArrayList<EndThread> endThreadEvents = new ArrayList<EndThread>();
	ArrayList<AdviceBody> eventBodies = new ArrayList<AdviceBody>();

	public EndProgram(String name) {
		this.hookName = new RVMVariable(name + "_DummyHookThread");
		this.className = name + "RuntimeMonitor";
	}

	public void addEndProgramEvent(RVMonitorSpec mopSpec, EventDefinition event, CombinedAspect combinedAspect) throws RVMException {
		if (!event.isEndProgram())
			throw new RVMException("EndProgram should be defined only for an endProgram pointcut.");

		this.eventBodies.add(new GeneralAdviceBody(mopSpec, event, combinedAspect));
	}

	public void registerEndThreadEvents(ArrayList<EndThread> endThreadEvents) {
		this.endThreadEvents.addAll(endThreadEvents);
	}

	public String printAddStatement() {
		String ret = "";
		
		if(eventBodies.size() == 0 && endThreadEvents.size() == 0)
			return ret;

		ret += "Runtime.getRuntime().addShutdownHook( (new " + className + "())" + ".new " + hookName + "());\n";

		return ret;
	}

	public String printHookThread() {
		String ret = "";

		if(eventBodies.size() == 0 && endThreadEvents.size() == 0)
			return ret;

		ret += "class " + hookName + " extends Thread {\n";
		ret += "public void run(){\n";

		if (endThreadEvents != null && endThreadEvents.size() > 0) {
			for (EndThread endThread : endThreadEvents) {
				ret += endThread.printAdviceBodyAtEndProgram();
			}
		}

		for (AdviceBody eventBody : eventBodies) {
			if (eventBodies.size() > 1) {
				ret += "{\n";
			}

			ret += eventBody;

			if (eventBodies.size() > 1) {
				ret += "}\n";
			}
		}

		ret += "}\n";
		ret += "}\n";

		return ret;
	}
}
