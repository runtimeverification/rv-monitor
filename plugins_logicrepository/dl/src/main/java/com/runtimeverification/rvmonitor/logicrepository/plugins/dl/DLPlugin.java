package com.runtimeverification.rvmonitor.logicrepository.plugins.dl;

import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.LogicPlugin;

public class DLPlugin extends LogicPlugin {
    
    /**
     * Apply the Finite State Machine plugin to some input.
     * @param logicInputXML The bare Finite State Machine input.
     * @return The annotated and minimized Finite State Machine output.
     */
    public LogicRepositoryType process(LogicRepositoryType logicInputXML) {
        LogicRepositoryType logicOutputXML = logicInputXML;
        logicOutputXML.getMessage().add("done");

        return logicOutputXML;
    }
    
}
