package com.runtimeverification.rvmonitor.logicrepository.plugins.dl;

import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.LogicPlugin;

public class DLPlugin extends LogicPlugin {

    public LogicRepositoryType process(LogicRepositoryType logicInputXML) {
        LogicRepositoryType logicOutputXML = logicInputXML;
        logicOutputXML.getMessage().add("done");

        return logicOutputXML;
    }

}
