package com.runtimeverification.rvmonitor.logicrepository.plugins.dl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import com.runtimeverification.rvmonitor.logicrepository.LogicException;
import com.runtimeverification.rvmonitor.logicrepository.LogicRepositoryData;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.LogicPlugin;

public class DLPlugin extends LogicPlugin {
    
    /**
     * Apply the Finite State Machine plugin to some input.
     * @param logicInputXML The bare Finite State Machine input.
     * @return The annotated and minimized Finite State Machine output.
     */
    public LogicRepositoryType process(LogicRepositoryType logicInputXML) throws LogicException {
        
        String logic = logicInputXML.getProperty().getLogic();
        logic = logic.toUpperCase();

	System.out.println(logic); 

        for (String event : (logicInputXML.getEvents().trim()).split("\\s+")){
            System.out.println(event);
        }
        
        for (String category : (logicInputXML.getCategories().trim()).split("\\s+")){
            System.out.println(category);
        }
        
        String formula = logicInputXML.getProperty().getFormula();
	System.out.println("formula: " + formula);
        
        LogicRepositoryType logicOutputXML = logicInputXML;
        logicOutputXML.getMessage().add("done");
        
        return logicOutputXML;
        
    }
    
}
