package com.runtimeverification.rvmonitor.logicrepository.plugins.ere;

import java.util.ArrayList;
import java.util.Arrays;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Concat;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.FSM;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Kleene;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Negation;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Or;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.parser.EREParser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test generating deterministic finite automata from extended regular expressions.
 */
public class FSMTest {
    
    private Symbol a;
    private Symbol b;
    
    @Before
    public void setUp() {
        a = Symbol.get("a");
        b = Symbol.get("b");
    }
    
    /**
     * Construct the string representation of a DFA matching the given expression.
     * @param expr The expression to print the DFA/FSM from.
     * @param symbols The symbols present in the expression.
     * @return A string representing the DFA/FSM.
     */
    private String printFSM(ERE expr, Symbol... symbols) {
        FSM dfa = FSM.get(expr, symbols);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        dfa.print(ps);
        
        return os.toString();
    }
    
    /**
     * Test generating the DFA for a Concat element of two Symbols.
     */
    @Test
    public void testConcat() {
        ERE ab = Concat.get(a, b);
        
        String fsm = "s0 [\n   a -> s1\n]\n" +
        "s1 [\n   b -> s2\n]\n" +
        "s2 [\n]\n" +
        "alias match = s2 \n";
        assertEquals(fsm, printFSM(ab, a, b));
    }
    
    /**
     * Test generating the DFA for an Or element of two Symbols.
     */
    @Test
    public void testOr() {
        ERE or = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
        
        String fsm = "s0 [\n   a -> s1\n   b -> s1\n]\n"+
        "s1 [\n]\n" +
        "alias match = s1 \n";
        assertEquals(fsm, printFSM(or, a, b));
    }
    
    /**
     * Test generating the DFA for a Kleene element of one symbol.
     */
    @Test
    public void testKleene() {
        ERE aStar = Kleene.get(a);
        
        String fsm = "s0 [\n   a -> s0\n]\n" +
        "alias match = s0 \n";
        assertEquals(fsm, printFSM(aStar, a));
    }
}