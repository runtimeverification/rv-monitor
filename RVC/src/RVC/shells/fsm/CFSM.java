package RVC.shells.fsm;

import java.io.ByteArrayInputStream;
import java.util.*;

import RVC.RVCsyntax.*;
import RVC.shells.Shell;
import RVC.shells.ShellResult;
import RVC.shells.fsm.fsmparser.*;
import RVC.shells.fsm.fsmparser.ast.*;

public class CFSM extends Shell {
  public CFSM() {
    super();
    monitorType = "FSM";
  }

  ArrayList<String> allEvents;

  private ArrayList<String> getEvents(String eventStr) throws Exception {
    ArrayList<String> events = new ArrayList<String>();

    for (String event : eventStr.trim().split(" ")) {
      if (event.trim().length() != 0)
        events.add(event.trim());
    }

    return events;
  }

  private Properties getMonitorCode(CMonGenType logicOutput) throws Exception {
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
      throw new Exception("FSM to C Shell cannot parse FSM formula");
    }

    if (fsmInput == null)
      throw new Exception("FSM to C Shell cannot parse FSM formula");

    HasDefaultVisitor hasDefaultVisitor = new HasDefaultVisitor();
    boolean[] hasDefault = fsmInput.accept(hasDefaultVisitor, null);

    List<String> monitoredEvents;
    monitoredEvents = allEvents;
    
    Map<String, Integer> EventNum = new HashMap<String, Integer>();
    int countEvent = 1;
    
    String monitoredEventsStr = "";
    
    for(String event: monitoredEvents){
      EventNum.put(event, new Integer(countEvent));
      
      monitoredEventsStr += "static const int " + rvcPrefix + constSpecName + event.toUpperCase() 
                         + " = " + countEvent + ";\n";
      
      ++countEvent;
    }
    
    Map<String, Integer> StateNum = new HashMap<String, Integer>();
    int countState = 0;
    
    if(fsmInput.getItems() != null){
      for(FSMItem i : fsmInput.getItems()){
        String stateName = i.getState();
        StateNum.put(stateName, new Integer(countState));
        countState++;
      }
    }
    
    result.put("monitored events", monitoredEventsStr);

    result.put("state declaration", "static int state = 0;\n");
    result.put("reset", "void\n" + rvcPrefix + specName + "reset(void)\n{\n  state = 0;\n }\n");
    //result.put("initialization", "state = 0;\nevent = -1;\n");
    
    String monitoringbodyStr = "void\n" + rvcPrefix + specName + "monitor(int event)\n{\n";
    
    monitoringbodyStr += "  switch(state) {\n";
    if(fsmInput.getItems() != null){
      for(FSMItem i : fsmInput.getItems()){
        boolean doneDefault = false;
        String stateName = i.getState();
        monitoringbodyStr += "  case " + StateNum.get(stateName) + ":\n";
        if(i.getTransitions() != null){
          monitoringbodyStr += "  switch(event) {\n";
          for(FSMTransition t : i.getTransitions()){
            if(t.isDefaultFlag() && !doneDefault){
              if(StateNum.get(t.getStateName()) == null)
                throw new Exception("Incorrect Monitor");
                
              monitoringbodyStr += "   default : state = " + StateNum.get(t.getStateName()) + "; break;\n";
              doneDefault = true;
            } else{
              monitoringbodyStr += "    case " + EventNum.get(t.getEventName()) + " : state = " + StateNum.get(t.getStateName()) + "; break;\n";
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

    for(String stateName : StateNum.keySet()){ 
      if(!cats.contains(stateName)) continue;
      String catName = rvcPrefix + specName + stateName;
      monitoringbodyStr += "  " + catName + " = state == " + StateNum.get(stateName) + ";\n";  
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
            conditionStr += "state == " + StateNum.get(state);
            firstFlag = false;
          } else{
            conditionStr += " || state == " + StateNum.get(state);
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

  public ShellResult process(CMonGenType logicOutputXML, String events) throws Exception {
    if (logicOutputXML.getProperty().getLogic().toLowerCase().compareTo(monitorType.toLowerCase()) != 0)
      throw new Exception("Wrong type of monitor is given to FSM Monitor.");
    allEvents = getEvents(events);

    ShellResult logicShellResult = new ShellResult();
    logicShellResult.startEvents = getEvents(logicOutputXML.getCreationEvents());
    //logicShellResult.startEvents = allEvents;
    logicShellResult.properties = getMonitorCode(logicOutputXML);
    logicShellResult.properties = addEnableSets(logicShellResult.properties, logicOutputXML);

    return logicShellResult;
  }
}
