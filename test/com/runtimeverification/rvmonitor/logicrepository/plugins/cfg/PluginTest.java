package com.runtimeverification.rvmonitor.logicrepository.plugins.cfg;

import com.runtimeverification.rvmonitor.logicrepository.PluginHelper;

import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.PropertyType;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests invocation of the complete CFG Logic Repository plugin.
 * Verifies that the complete CFG plugin can be invoked through the normal plugin mechanism
 * and that it produces reasonable output.
 * @author A. Cody Schuffelen
 */
public class PluginTest {
    
    /**
     * Tests code generation for CFG code containing a Safe File property.
     */
    @Test
    public void testCompletePluginSafeFile() throws Exception {
        LogicRepositoryType input = new LogicRepositoryType();
        input.setClient("testing");
        input.setEvents("beginCall endCall open close");
        input.setCategories("fail");
        input.setProperty(new PropertyType());
        input.getProperty().setLogic("cfg");
        input.getProperty().setFormula(
            "S -> A S | epsilon,\n" +
            "A -> A beginCall A endCall | A open A close | epsilon\n"
        );
        
        LogicRepositoryType output = PluginHelper.runLogicPlugin("cfg", input);
        
        assertEquals("testing", output.getClient());
        assertEquals("done", output.getMessage().get(output.getMessage().size() - 1));
        assertEquals("cfg", output.getProperty().getLogic());
        
        String powerSet = "[[open, close, beginCall], [], [endCall, close], [close, beginCall], " +
        "[endCall], [endCall, beginCall, close], [close], [endCall, open], [open], " +
        "[open, endCall, close, beginCall], [close, open], [beginCall, open], " +
        "[open, endCall, close], [open, endCall, beginCall], [beginCall], [endCall, beginCall]]";
        
        assertTrue(output.getEnableSets().contains(powerSet));
        assertEquals(input.getEvents(), output.getCreationEvents());
    }
    
    /**
     * Test code generation for CFG code containing a HasNext property.
     */
    @Test
    public void testCompletePluginHasNext() throws Exception {
        LogicRepositoryType input = new LogicRepositoryType();
        input.setClient("testing");
        input.setEvents("hasnext next");
        input.setCategories("fail");
        input.setProperty(new PropertyType());
        input.getProperty().setLogic("cfg");
        input.getProperty().setFormula("S -> next next");
        
        LogicRepositoryType output = PluginHelper.runLogicPlugin("cfg", input);
        
        assertEquals("testing", output.getClient());
        assertEquals("done", output.getMessage().get(output.getMessage().size() - 1));
        assertEquals("cfg", output.getProperty().getLogic());
        
        assertTrue(output.getEnableSets().contains("{next=[[], [next]]}"));
        assertEquals(input.getEvents(), output.getCreationEvents());
    }
}