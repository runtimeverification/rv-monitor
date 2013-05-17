package RVC.plugins.ltl;

import java.io.*;
import java.util.*;
import RVC.LogicException;
import RVC.CMonGenData;
import RVC.Main;
import RVC.RVCsyntax.*;
import RVC.plugins.*;

public class LTLPlugin extends LogicPlugin {
  
  public CMonGenType process(CMonGenType logicInputXML) throws LogicException {
    String logicStr = logicInputXML.getProperty().getFormula();
    String eventsStr = logicInputXML.getEvents();
    eventsStr.replaceAll("\\s+", " ");
    String[] eventStrings = eventsStr.split(" ");

    HashSet<Atom> events = new HashSet();
    for (int i = 0; i < eventStrings.length; ++i) {
      events.add(Atom.get(eventStrings[i]));
    }

    LTLParser ltlParser = LTLParser.parse(logicStr);
    LTLFormula ltl = ltlParser.getFormula();

    ltl = ltl.simplify();

    for(Atom a : ltl.atoms()){
      if(!events.contains(a)){
        throw new LogicException("event " + a + " is used but never defined");
      }
    }
    
    AAutomaton aa = new AAutomaton(ltl);    
    GBA gba = new GBA(aa);
    BA ba = new BA(gba);
    NFA nfa = new NFA(ba);
    DFA dfa = new DFA(nfa);

    String logic = "fsm";
    String formula = dfa.toString();

    CMonGenType logicOutputXML = logicInputXML;
    logicOutputXML.getProperty().setLogic(logic);
    logicOutputXML.getProperty().setFormula(formula);

    return logicOutputXML;
  }
  
  static protected LTLPlugin plugin = new LTLPlugin();
  
  public static void main(String[] args) {

    try {
      // Parse Input
      CMonGenData logicInputData = new CMonGenData(System.in);

      // use plugin main function
      if(plugin == null){
        throw new LogicException("Each plugin should initiate plugin field.");
      }
      CMonGenType logicOutputXML = plugin.process(logicInputData.getXML());

      if (logicOutputXML == null) {
        throw new LogicException("no output from the plugin.");
      }

      ByteArrayOutputStream logicOutput = new CMonGenData(logicOutputXML).getOutputStream();
      System.out.println(logicOutput);
    } catch (LogicException e) {
      System.out.println(e);
    }

  }
}
