package RVC.plugins.ptltl;

import RVC.plugins.ptltl.*;
import RVC.*; 
import RVC.LogicException.*;
import RVC.Main.*;
import RVC.RVCsyntax.*;
import RVC.plugins.*;

public class PTLTLPlugin extends LogicPlugin {

  public CMonGenType process(CMonGenType logicInputXML) {
    String logicStr = logicInputXML.getProperty().getFormula().trim();

    CMonGenType ret = logicInputXML;
    ret.getProperty().setLogic("fsm");
	//System.out.println(PTLTL.mkFSM(logicStr));
    ret.getProperty().setFormula(RVC.plugins.ptltl.PTLTL.mkFSM(logicStr));

    return ret;
  }

}

