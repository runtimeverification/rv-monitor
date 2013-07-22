package com.runtimeverification.rvmonitor.logicpluginshells.fsm;

import java.io.ByteArrayInputStream;
import java.util.*;

import com.runtimeverification.rvmonitor.logicpluginshells.fsm.visitor.HasDefaultVisitor;
import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShell;
import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShellResult;
import com.runtimeverification.rvmonitor.logicpluginshells.fsm.parser.*;
import com.runtimeverification.rvmonitor.logicpluginshells.fsm.ast.*;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.util.RVMException;

public class CFSM extends LogicPluginShell {
  public CFSM() {
    super();
    monitorType = "FSM";
    outputLanguage = "C";
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

    FSMInput fsmInput = null;
    try {
      fsmInput = FSMParser.parse(new ByteArrayInputStream(monitor.getBytes()));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RVMException("FSM to C LogicPluginShell cannot parse FSM formula");
    }

    if (fsmInput == null)
      throw new RVMException("FSM to C LogicPluginShell cannot parse FSM formula");

    HasDefaultVisitor hasDefaultVisitor = new HasDefaultVisitor();
    boolean[] hasDefault = fsmInput.accept(hasDefaultVisitor, null);

    List<String> monitoredEvents;
    Map<String, String> eventArrays = new HashMap<String, String>(allEvents.size());
    monitoredEvents = allEvents;
    
    Map<String, Integer> EventNum = new HashMap<String, Integer>();
    int countEvent = 1;
    
    String monitoredEventsStr = "";
    
    for(String event: monitoredEvents){
      EventNum.put(event, new Integer(countEvent));
      eventArrays.put(event, "static int " + event + "[] = {");
      
      monitoredEventsStr += "static const int " + rvcPrefix + constSpecName + event.toUpperCase() 
                         + " = " + countEvent + ";\n";
      
      ++countEvent;
    }
    
    Map<String, Integer> stateNum = new HashMap<String, Integer>();
    int countState = 0;
    
    if(fsmInput.getItems() != null){
      for(FSMItem i : fsmInput.getItems()){
        String stateName = i.getState();
        stateNum.put(stateName, new Integer(countState));
        countState++;
      }
    }
    
    result.put("monitored events", monitoredEventsStr);

   // result.put("state declaration", "static int state = 0;\n");
    
    String stateDecl = "static int state = 0; \n";
    
    result.put("reset", "void\n" + rvcPrefix + specName + "reset(void)\n{\n  state = 0;\n }\n");
    //result.put("initialization", "state = 0;\nevent = -1;\n");
    
    String monitoringbodyStr = "void\n" + rvcPrefix + specName + "monitor(int event)\n{\n";
    
   
    for(FSMItem i : fsmInput.getItems()){
      int num = stateNum.get(i.getState());
      System.out.println(num);
      Set<String> unseenEvents = new HashSet<String>(monitoredEvents);
      int defaultState = -1;
      for(FSMTransition t : i.getTransitions()){
        if(t.isDefaultFlag()){
           defaultState = stateNum.get(t.getStateName());
        }
        else{
          String eventName = t.getEventName();
          unseenEvents.remove(eventName);
          String arrayString = eventArrays.get(eventName);
          eventArrays.put(eventName, arrayString + stateNum.get(t.getStateName()) + ", ");
        }
      }
      for(String event : unseenEvents){
        String arrayString = eventArrays.get(event);
        eventArrays.put(event, arrayString + defaultState + ",");
        System.out.println(num+ ":" + event);
      }
    }

    for(String event : monitoredEvents){
       String arrayString = eventArrays.get(event);
       eventArrays.put(event, arrayString + "};\n");
    }

    System.out.println(eventArrays);

    monitoringbodyStr += "  switch(state) {\n";
    if(fsmInput.getItems() != null){
      for(FSMItem i : fsmInput.getItems()){
        boolean doneDefault = false;
        String stateName = i.getState();
        monitoringbodyStr += "  case " + stateNum.get(stateName) + ":\n";
        if(i.getTransitions() != null){
          monitoringbodyStr += "  switch(event) {\n";
          for(FSMTransition t : i.getTransitions()){
            if(t.isDefaultFlag() && !doneDefault){
              if(stateNum.get(t.getStateName()) == null)
                throw new RVMException("Incorrect Monitor");
                
              monitoringbodyStr += "   default : state = " + stateNum.get(t.getStateName()) + "; break;\n";
              doneDefault = true;
            } else{
              monitoringbodyStr += "    case " + EventNum.get(t.getEventName()) + " : state = " + stateNum.get(t.getStateName()) + "; break;\n";
            }
          }
          if(!doneDefault){
            monitoringbodyStr += "    default : state = -1; break;\n";
          }
          monitoringbodyStr += "  }\n";
        }
        monitoringbodyStr += "  break;\n";
      }
    }
    monitoringbodyStr += "  default : state = -1;\n";
    monitoringbodyStr += "  }\n";

    System.out.println(monitoringbodyStr);


    result.put("state declaration", stateDecl);

    //figure out which categories we actually care about
    HashSet<String> cats = new HashSet();
    for(String category : logicOutput.getCategories().split("\\s+")){
      cats.add(category); 
    }
    String catString = "";
    //add fail if necessary
    if(cats.contains("fail")){
      catString = "int " + rvcPrefix + specName + "fail = 0;\n"; 
      monitoringbodyStr += "  " + rvcPrefix + specName + "fail = state == -1;\n"; 
    }

    for(String stateName : stateNum.keySet()){ 
      if(!cats.contains(stateName)) continue;
      String catName = rvcPrefix + specName + stateName;
      monitoringbodyStr += "  " + catName + " = state == " + stateNum.get(stateName) + ";\n";  
      catString += "int " + catName + " = 0;\n"; 
    }

    if(fsmInput.getAliases() != null){
      for(FSMAlias a : fsmInput.getAliases()){
        String stateName = a.getGroupName();
        if(!cats.contains(stateName)) continue;
        String conditionStr = "";
        boolean firstFlag = true;
        for(String state : a.getStates()){
          if(firstFlag){
            conditionStr += "state == " + stateNum.get(state);
            firstFlag = false;
          } else{
            conditionStr += " || state == " + stateNum.get(state);
          }
        }
        String catName = rvcPrefix + specName + stateName;
        monitoringbodyStr += "  " + catName + " = " + conditionStr + ";\n";  
        catString += "int " + catName + " = 0;\n"; 
      }
    }
   
    monitoringbodyStr += "}\n\n"; 
    result.put("monitoring body", monitoringbodyStr);
    //TODO: fixme
    if(catString.equals("")){
      throw new RuntimeException("Invalid handlers specified.  Make sure your logic supports your specified handlers");
    }
    result.put("categories", catString);
    return result;
  }

  @Override
  public LogicPluginShellResult process(LogicRepositoryType logicOutputXML, String events) throws RVMException {
    if (logicOutputXML.getProperty().getLogic().toLowerCase().compareTo(monitorType.toLowerCase()) != 0)
      throw new RVMException("Wrong type of monitor is given to FSM Monitor.");
    allEvents = getEvents(events);

    LogicPluginShellResult logicShellResult = new LogicPluginShellResult();
    logicShellResult.startEvents = getEvents(logicOutputXML.getCreationEvents());
    //logicShellResult.startEvents = allEvents;
    logicShellResult.properties = getMonitorCode(logicOutputXML);
    logicShellResult.properties = addEnableSets(logicShellResult.properties, logicOutputXML);

    return logicShellResult;
  }
}
