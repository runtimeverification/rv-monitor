package com.runtimeverification.rvmonitor.logicrepository.srs;

import com.runtimeverification.rvmonitor.logicrepository.PluginHelper;

import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.PropertyType;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests invocation of the complete SRS Logic Repository plugin.
 * Verifies that the complete SRS plugin can be invoked through the normal plugin mechanism
 * and that it produces reasonable output.
 * @author A. Cody Schuffelen
 */
public class PluginTest {
    
    /**
     * Tests code generation for SRS code containing a HasNext property.
     */
    @Test
    public void testCompletePluginHasNext() throws Exception {
        LogicRepositoryType input = new LogicRepositoryType();
        input.setClient("testing");
        input.setCategories("fail");
        input.setEvents("hasnexttrue hasnextfalse next");
        input.setProperty(new PropertyType());
        input.getProperty().setLogic("srs");
        input.getProperty().setFormula(
            "hasnexttrue hasnexttrue -> hasnexttrue ." +
            "hasnexttrue next        -> #epsilon . " +
            "next                    -> #fail ."
        );
        
        LogicRepositoryType output = PluginHelper.runLogicPlugin("srs", input);
        
        assertEquals("testing", output.getClient());
        assertEquals("done", output.getMessage().get(output.getMessage().size() - 1));
        
        String formula = output.getProperty().getFormula();
        
        /**
         * These get reordered and the order isn't important, so i just test for the lines separately.
         */
        
        String fiveAtZero = formula.substring(formula.indexOf("\n<5 @ 0"), formula.indexOf("\n<6 @ 1"));
        
        assertTrue(fiveAtZero.contains("hasnexttrue -> [0] <6 @ 1>"));
        assertTrue(fiveAtZero.contains("hasnextfalse -> [0] <5 @ 0>"));
        assertTrue(fiveAtZero.contains("next -> [0] <9 @ 1 matches next  -> #fail>"));
        
        String sixAtOne = formula.substring(formula.indexOf("\n<6 @ 1"), formula.indexOf("\n<7 @ 2"));
        
        assertTrue(sixAtOne.contains("hasnexttrue -> [0] <7 @ 2 matches hasnexttrue hasnexttrue  -> hasnexttrue"));
        assertTrue(sixAtOne.contains("hasnextfalse -> [1] <5 @ 0>"));
        assertTrue(sixAtOne.contains("next -> [0] <8 @ 2 matches hasnexttrue next  -> #epsilon"));
        
        String sevenAtTwo = formula.substring(formula.indexOf("\n<7 @ 2"), formula.indexOf("\n<8 @ 2"));
        
        assertTrue(sevenAtTwo.contains("matches hasnexttrue hasnexttrue  -> hasnexttrue"));
        assertTrue(sevenAtTwo.contains("hasnexttrue -> [1] <6 @ 1>"));
        assertTrue(sevenAtTwo.contains("hasnextfalse -> [1] <6 @ 1>"));
        assertTrue(sevenAtTwo.contains("next -> [1] <6 @ 1>"));
        
        String eightAtTwo = formula.substring(formula.indexOf("\n<8 @ 2"), formula.indexOf("\n<9 @ 1"));
        
        assertTrue(eightAtTwo.contains("matches hasnexttrue next  -> #epsilon"));
        assertTrue(eightAtTwo.contains("hasnexttrue -> [1] <9 @ 1 matches next  -> #fail>"));
        assertTrue(eightAtTwo.contains("hasnextfalse -> [1] <9 @ 1 matches next  -> #fail>"));
        assertTrue(eightAtTwo.contains("next -> [1] <9 @ 1 matches next  -> #fail>"));
        
        String nineAtOne = formula.substring(formula.indexOf("\n<9 @ 1"));
        
        assertTrue(nineAtOne.contains("matches next  -> #fail"));
        assertTrue(nineAtOne.contains("hasnexttrue -> [1] <5 @ 0>"));
        assertTrue(nineAtOne.contains("hasnextfalse -> [1] <5 @ 0>"));
        assertTrue(nineAtOne.contains("next -> [1] <5 @ 0>"));
    }
}