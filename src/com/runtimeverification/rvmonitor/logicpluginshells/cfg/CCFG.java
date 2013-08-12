package com.runtimeverification.rvmonitor.logicpluginshells.cfg;

import java.io.ByteArrayInputStream;
import java.util.*;

import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShell;
import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShellResult;
import com.runtimeverification.rvmonitor.logicpluginshells.cfg.parser.*;

import com.runtimeverification.rvmonitor.logicpluginshells.cfg.util.CFG;
import com.runtimeverification.rvmonitor.logicpluginshells.cfg.util.GLRGen;
import com.runtimeverification.rvmonitor.logicpluginshells.cfg.util.LR;
import com.runtimeverification.rvmonitor.logicpluginshells.cfg.util.Terminal;

import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.util.RVMException;

import com.runtimeverification.rvmonitor.c.rvc.parser.RVCParser;

public class CCFG extends LogicPluginShell {
  private RVCParser rvcParser;

  public CCFG(RVCParser rvcParser) {
    super();
    monitorType = "CFG";
    outputLanguage = "C";
    this.rvcParser = rvcParser;
  }

  ArrayList<String> allEvents;

  private ArrayList<String> getEvents(String eventStr) {
    ArrayList<String> events = new ArrayList<String>();

    for (String event : eventStr.trim().split(" ")) {
      if (event.trim().length() != 0)
        events.add(event.trim());
    }

    return events;
  }

  private Properties getMonitorCode(LogicRepositoryType logicOutput) throws RVMException {
    String rvcPrefix = "__RVC_";
    Properties result = new Properties();

    String monitor = logicOutput.getProperty().getFormula();
    String specName = logicOutput.getSpecName() + "_";
    String constSpecName = specName.toUpperCase();    

    result.put("rvcPrefix", rvcPrefix);
    result.put("specName", specName);
    result.put("constSpecName", constSpecName);

		CFG g = null;
		try {
			g = CFGParser.parse(new ByteArrayInputStream(monitor.getBytes())).getCFG();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RVMException("CCFG Shell cannot parse CFG formula");
		}

		if (g == null)
			throw new RVMException("CCFG Shell cannot parse CFG formula");
		g.simplify();

		List<String> monitoredEvents;
		monitoredEvents = allEvents;

     Map<String, String> constEventNames = new HashMap<String, String>();

   for(String event: monitoredEvents){
     String constEventName = rvcPrefix + constSpecName + event.toUpperCase(); 
      constEventNames.put(event, constEventName);     
   }


		// NB EOF will always be 0
		Map<String, Integer> tsmap = new HashMap<String, Integer>();
		int tnum = 1;

		String monitoredEventsStr = "";

		for (String event : monitoredEvents) {
			tsmap.put(event, new Integer(tnum));

			monitoredEventsStr += event + ":{\n  $event$ = " + tnum + ";\n}\n\n";

			tnum++;
		}

		HashMap<Terminal, Integer> tmap = new HashMap<Terminal, Integer>();
		for (String event : monitoredEvents) {
			tmap.put(new Terminal(event), tsmap.get(event));
		}

		LR lr = new LR(g, tmap);

		if (!(tmap.keySet().containsAll(g.terminals())))
			throw new RVMException("Terminals in CFG differ from declared events");

	//	result.put("monitored events", monitoredEventsStr);



    result.put("state declaration", GLRGen.state(lr) + "\n" + GLRGen.intstack);
    result.put("reset", GLRGen.reset(lr));
    result.put("initialization", GLRGen.init(lr));

    result.put("monitoring body", GLRGen.body());

    String catString  = "int " + rvcPrefix + specName + "match = 0;\n"
                      + "int " + rvcPrefix + specName + "fail = 0;\n";

    String condString = rvcPrefix + specName + "match = $cat$ == 0;\n"  
                      + rvcPrefix + specName + "fail = $cat$ == 2;\n";

    result.put("categories", catString);

	//	result.put("match condition", "$cat$ == 0\n");
	//	result.put("fail condition", "$cat$ == 2\n");
		
	//	result.put("nonfail condition", "$cat$ != 2\n");


    StringBuilder headerDecs = new StringBuilder();
    StringBuilder eventFuncs = new StringBuilder();

    String resetName = rvcPrefix + specName + "reset";
  
    headerDecs.append("void\n");
    headerDecs.append(resetName + "(void);\n");
  for(String eventName : monitoredEvents){
      headerDecs.append("void\n");
      eventFuncs.append("void\n");
      String funcDecl = rvcPrefix + specName + eventName + rvcParser.getParameters().get(eventName);
      headerDecs.append(funcDecl + ";\n");
      eventFuncs.append(funcDecl + "\n");
      eventFuncs.append("{\n");
      eventFuncs.append(rvcParser.getEvents().get(eventName) + "\n"); 
      eventFuncs.append("__RVC_state = " + constEventNames.get(eventName) + "[__RVC_state];\n"); 
      eventFuncs.append(condString);
      for(String category : rvcParser.getHandlers().keySet()){
        eventFuncs.append("if(" + rvcPrefix + specName + category + ")\n{\n");
        eventFuncs.append(rvcParser.getHandlers().get(category).replaceAll("__RESET", resetName + "()\n"));
        eventFuncs.append("}\n");
      }
      eventFuncs.append("}\n\n");
    }

    result.put("header declarations", headerDecs.toString());
    result.put("event functions", eventFuncs.toString());

    return result;
  }
  
  @Override
  public LogicPluginShellResult process(LogicRepositoryType logicOutputXML, String events) throws RVMException {
    if (logicOutputXML.getProperty().getLogic().toLowerCase().compareTo(monitorType.toLowerCase()) != 0)
      throw new RVMException("Wrong type of monitor is given to CFG Monitor.");
    allEvents = getEvents(events);

    LogicPluginShellResult logicShellResult = new LogicPluginShellResult();
    logicShellResult.startEvents = getEvents(logicOutputXML.getCreationEvents());
    //logicShellResult.startEvents = allEvents;
    logicShellResult.properties = getMonitorCode(logicOutputXML);
    logicShellResult.properties = addEnableSets(logicShellResult.properties, logicOutputXML);

    return logicShellResult;
  }
}
