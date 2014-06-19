package com.runtimeverification.rvmonitor.logicrepository.ltl;

import com.runtimeverification.rvmonitor.logicrepository.PluginHelper;

import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.PropertyType;

import org.junit.Test;
import static org.junit.Assert.*;

/*

    Tests invocation of LTL Logic Repository plugin.

*/

public class PluginTest {

    public void testCompletePlugin() throws Exception {
        LogicRepositoryType input = new LogicRepositoryType();
        input.setClient("testing");
        input.setProperty(new PropertyType());
        input.getProperty().setLogic("ltl");
        input.getProperty().setFormula("a & b");
        
        LogicRepositoryType output = PluginHelper.runLogicPlugin("ltl", input);
    
        System.out.println(output.getClient);
        assertEquals("testing", output.getClient());
        
        System.out.println(output.getProperty.getLogic());
        assertEquals("ltl pseudo-code", output.getProperty.getLogic());





    }
