package com.runtimeverification.rvmonitor.logicpluginshells.dl;

import java.util.Properties;

import com.runtimeverification.rvmonitor.c.rvc.CSpecification;
import com.runtimeverification.rvmonitor.core.ast.Event;
import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShell;
import com.runtimeverification.rvmonitor.logicpluginshells.LogicPluginShellResult;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.util.RVMException;

public class CDL extends LogicPluginShell {
    private CSpecification cSpec;
    private boolean parametric;

    public CDL() {
        super();
        monitorType = "DL";
        outputLanguage = "C";
        this.cSpec = null;
    }

    public CDL(CSpecification cSpec, boolean parametric) {
        super();
        monitorType = "DL";
        outputLanguage = "C";
        this.cSpec = cSpec;
        this.parametric = parametric;
    }

    private Properties getMonitorCode(LogicRepositoryType logicOutput)
            throws RVMException {
        if (parametric) {
            return getParametricMonitorCode(logicOutput);
        } else {
            return getNonParametricMonitorCode(logicOutput);
        }
    }

    private String modelMonitorFromModelPlex(String keymaeraxModel) {
        return ModelPlexConnector$.MODULE$.synthesizeCModelMonitor(keymaeraxModel);
    }

    private String toCFunctionDecl(String fName, Event fEvent) {
        return "void " + fName + fEvent.getDefinition();
    }

    private String toCFunction(String fName, Event fEvent) {
        StringBuffer functionDef = new StringBuffer();
        functionDef.append(toCFunctionDecl(fName, fEvent) + "{ \n");
        functionDef.append("\t" + fEvent.getAction() + "\n");
        functionDef.append("}\n");
        return functionDef.toString();
    }

    private Properties getNonParametricMonitorCode(LogicRepositoryType logicOutput) throws RVMException {
        String rvcPrefix = "__RVC_";
        Properties result = new Properties();

        String specName = logicOutput.getSpecName() + "_";
        String constSpecName = specName.toUpperCase();

        result.setProperty("rvcPrefix", rvcPrefix);
        result.setProperty("specName", specName);
        result.setProperty("constSpecName", constSpecName);

        StringBuilder headerDecs = new StringBuilder();
        StringBuilder eventFuncs = new StringBuilder();


        String[] monitoredEvents = logicOutput.getEvents().trim().split("\\s+");

        for (String eventName : monitoredEvents) {
            Event event = cSpec.getEvents().get(eventName);
            String funcName = rvcPrefix + specName + eventName;
            headerDecs.append(toCFunctionDecl(funcName, event) + ";\n");
            eventFuncs.append(toCFunction(funcName, event));
        }

        result.setProperty("header declarations", headerDecs.toString());
        result.setProperty("event functions", eventFuncs.toString());
        result.setProperty("monitoring body", modelMonitorFromModelPlex(logicOutput.getProperty().getFormula()));

        return result;
    }

    private Properties getParametricMonitorCode(LogicRepositoryType logicOutput)
            throws RVMException {
        throw new RVMException("Parametric DL monitoring not supported");
    }



    @Override
    public LogicPluginShellResult process(LogicRepositoryType logicOutputXML,
                                          String events) throws RVMException {
        if (logicOutputXML.getProperty().getLogic().toLowerCase()
                .compareTo(monitorType.toLowerCase()) != 0)
            throw new RVMException(
                    "Wrong type of monitor is given to DL Monitor.");

        LogicPluginShellResult logicShellResult = new LogicPluginShellResult();
        logicShellResult.properties = getMonitorCode(logicOutputXML);

        return logicShellResult;
    }
}

