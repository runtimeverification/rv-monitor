package com.runtimeverification.rvmonitor.logicpluginshells.fsm;

import java.io.ByteArrayInputStream;
import java.util.*;

import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShell;
import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShellResult;
import com.runtimeverification.rvmonitor.logicpluginshells.fsm.parser.*;
import com.runtimeverification.rvmonitor.logicpluginshells.fsm.ast.*;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.util.FileUtils;

import com.runtimeverification.rvmonitor.c.rvc.parser.RVCParser;

public class CFSM extends LogicPluginShell {
  private RVCParser rvcParser;
  private boolean parametric;

  public CFSM() {
    super();
    monitorType = "FSM";
    outputLanguage = "C";
    this.rvcParser = null;
  }

  public CFSM(RVCParser rvcParser, boolean parametric) {
    super();
    monitorType = "FSM";
    outputLanguage = "C";
    this.rvcParser = rvcParser;
    this.parametric = parametric;
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
    if(parametric){
      return getParametricMonitorCode(logicOutput);
    }
    else {
      return getNonParametricMonitorCode(logicOutput);
    }
  }
  

  private Properties getNonParametricMonitorCode(LogicRepositoryType logicOutput) throws RVMException {
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

    List<String> monitoredEvents;
    Map<String, String> eventArrays = new HashMap<String, String>(allEvents.size());
    monitoredEvents = allEvents;
    
    Map<String, Integer> EventNum = new HashMap<String, Integer>();
    int countEvent = 1;

    Map<String, String> constEventNames = new HashMap<String, String>(allEvents.size());   
 
    for(String event: monitoredEvents){
      EventNum.put(event, new Integer(countEvent));
      String constEventName = rvcPrefix + constSpecName + event.toUpperCase(); 
      eventArrays.put(event, "static int " + constEventName + "[] = {");
      constEventNames.put(event, constEventName);     
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
    
    
    result.put("reset", "void\n" + rvcPrefix + specName + "reset(void)\n{\n  __RVC_state = 0;\n }\n");
    
    

    for(FSMItem i : fsmInput.getItems()){
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
      }
    }

    for(String event : monitoredEvents){
       String arrayString = eventArrays.get(event);
       eventArrays.put(event, arrayString + "};\n");
    }

    String monitoringbodyString = "" ;

    for(String eventArray : eventArrays.values()){
       monitoringbodyString += eventArray;
    }

    addStateDeclaration(result);

    //figure out which categories we actually care about
    HashSet<String> cats = new HashSet<String>();
    for(String category : logicOutput.getCategories().split("\\s+")){
      cats.add(category); 
    }
    String catString = "";

    String condString = "";
    //add fail if necessary
    if(cats.contains("fail")){
      catString = "int " + rvcPrefix + specName + "fail = 0;\n"; 
      condString += "  " + rvcPrefix + specName + "fail = __RVC_state == -1;\n"; 
    }

    for(String stateName : stateNum.keySet()){ 
      if(!cats.contains(stateName)) continue;
      String catName = rvcPrefix + specName + stateName;
      condString += "  " + catName + " = __RVC_state == " + stateNum.get(stateName) + ";\n";  
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
            conditionStr += "__RVC_state == " + stateNum.get(state);
            firstFlag = false;
          } else{
            conditionStr += " || __RVC_state == " + stateNum.get(state);
          }
        }
        String catName = rvcPrefix + specName + stateName;
        condString += "  " + catName + " = " + conditionStr + ";\n";  
        catString += "int " + catName + " = 0;\n"; 
      }
    }
   
    //TODO: fixme
    if(catString.equals("")){
      throw new RuntimeException("Invalid handlers specified.  Make sure your logic supports your specified handlers");
    }

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
    result.put("monitoring body", monitoringbodyString);

    result.put("categories", catString);

    return result;
  }

  private Properties getParametricMonitorCode(LogicRepositoryType logicOutput) throws RVMException {
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

    List<String> monitoredEvents;
    Map<String, String> eventArrays = new HashMap<String, String>(allEvents.size());
    monitoredEvents = allEvents;
    
    Map<String, Integer> EventNum = new HashMap<String, Integer>();
    int countEvent = 1;

    Map<String, String> constEventNames = new HashMap<String, String>(allEvents.size());   
 
    for(String event: monitoredEvents){
      EventNum.put(event, new Integer(countEvent));
      String constEventName = rvcPrefix + constSpecName + event.toUpperCase(); 
      eventArrays.put(event, "static int " + constEventName + "[] = {");
      constEventNames.put(event, constEventName);     
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
    
    
    result.put("reset", "void\n" + rvcPrefix + specName + "reset(void *key)\n{\n__RV_monitor * temp = __RV_find(list,key);\nif(temp != NULL) {\ntemp->__RVC_state = 0;}\n }\n");
    
    
    
    for(FSMItem i : fsmInput.getItems()){
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
      }
    }

    for(String event : monitoredEvents){
       String arrayString = eventArrays.get(event);
       eventArrays.put(event, arrayString + "};\n");
    }

    String monitoringbodyString = "" ;

    for(String eventArray : eventArrays.values()){
       monitoringbodyString += eventArray;
    }


    //figure out which categories we actually care about
    HashSet<String> cats = new HashSet<String>();
    for(String category : logicOutput.getCategories().split("\\s+")){
      cats.add(category); 
    }

    addParametricStateDeclaration(result, cats);
    String catString = "";

    String condString = "";
    //add fail if necessary
    if(cats.contains("fail")){
      condString += "  mon->" + rvcPrefix + specName + "fail = mon->__RVC_state == -1;\n"; 
    }

    for(String stateName : stateNum.keySet()){ 
      if(!cats.contains(stateName)) continue;
      String catName = rvcPrefix + specName + stateName;
      condString += "  mon->" + catName + " = mon->__RVC_state == " + stateNum.get(stateName) + ";\n";  
    }

    if(fsmInput.getAliases() != null){
      for(FSMAlias a : fsmInput.getAliases()){
        String stateName = a.getGroupName();
        if(!cats.contains(stateName)) continue;
        String conditionStr = "";
        boolean firstFlag = true;
        for(String state : a.getStates()){
          if(firstFlag){
            conditionStr += "mon->__RVC_state == " + stateNum.get(state);
            firstFlag = false;
          } else{
            conditionStr += " || mon->__RVC_state == " + stateNum.get(state);
          }
        }
        String catName = rvcPrefix + specName + stateName;
        condString += "  mon->" + catName + " = " + conditionStr + ";\n";  
      }
    }

    StringBuilder headerDecs = new StringBuilder();
    StringBuilder eventFuncs = new StringBuilder();

    String resetName = rvcPrefix + specName + "reset";

    headerDecs.append("void\n");
    headerDecs.append(resetName + "(void *key);\n"); 


    for(String eventName : monitoredEvents){
      headerDecs.append("void\n");
      eventFuncs.append("void\n");
      String funcDecl = rvcPrefix + specName + eventName + rvcParser.getPParameters().get(eventName);
      headerDecs.append(funcDecl + ";\n");
      eventFuncs.append(funcDecl + "\n");
      eventFuncs.append("{\n");
      eventFuncs.append(rvcParser.getEvents().get(eventName) + "\n"); 
      eventFuncs.append("if(list == NULL){\n");
      eventFuncs.append("  list = __RV_new_RV_list(10);\n");
      eventFuncs.append("}\n");
      eventFuncs.append("__RV_monitor *mon = __RV_find(list, key);\n");
      eventFuncs.append("if(mon == NULL){\n");
      eventFuncs.append("  __RV_tag_monitor *tm = __RV_new_RV_tag_monitor(key,0);\n");
      eventFuncs.append("  mon = tm->monitor;\n");
      eventFuncs.append("  __RV_append(list,tm);\n");
      eventFuncs.append("}\n");
      eventFuncs.append("mon->__RVC_state = " + constEventNames.get(eventName) + "[mon->__RVC_state];\n"); 
      eventFuncs.append(condString);
      for(String category : rvcParser.getHandlers().keySet()){
        eventFuncs.append("if(mon->" + rvcPrefix + specName + category + ")\n{\n");
        eventFuncs.append(rvcParser.getHandlers().get(category).replaceAll("__RESET", resetName + "()\n"));
        eventFuncs.append("}\n");
      }
      eventFuncs.append("}\n\n");
    }

    result.put("header declarations", headerDecs.toString());
    result.put("event functions", eventFuncs.toString());
    result.put("monitoring body", monitoringbodyString);

    result.put("categories", catString);

    return result;
  }



  static private void addStateDeclaration(Properties p){
    String stateDecl = "static int __RVC_state = 0; \n";
    p.put("state declaration", stateDecl);
  }  

  static private void addParametricStateDeclaration(Properties p, HashSet<String> cats){
    String specName = (String) p.get("specName");
    String rvcPrefix = (String) p.get("rvcPrefix");
    String ret = "typedef struct monitor {\n"
    + "  int __RVC_state;\n";
    for(String cat : cats){
      ret += "int " + rvcPrefix + specName + cat + ";\n"; 
    }
    ret += "} __RV_monitor;\n"
      + "\n";
    ret += FileUtils.extractFileFromJar(CFSM.class, "cfg_monitor.h");
    p.put("state declaration", ret);
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
