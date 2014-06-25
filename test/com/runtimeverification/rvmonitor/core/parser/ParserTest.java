package com.runtimeverification.rvmonitor.core.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.io.*;

import com.runtimeverification.rvmonitor.core.ast.Event;
import com.runtimeverification.rvmonitor.core.ast.MonitorFile;
import com.runtimeverification.rvmonitor.core.ast.Property;
import com.runtimeverification.rvmonitor.core.ast.PropertyHandler;
import com.runtimeverification.rvmonitor.core.ast.Specification;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test that the language-independent parser produces reasonable AST structures.
 * @author A. Cody Schuffelen
 */
public class ParserTest {
    
    /**
     * Test the parser on a HasNext property.
     */
    @Test
    public void testHasNext() throws FileNotFoundException {
        final MonitorFile file = RVParser.parse(new InputStreamReader(new FileInputStream(
            "examples/newsyntax/HasNext.rvm")));
        
        assertTrue(file.getPreamble().contains("package mop;"));
        assertTrue(file.getPreamble().contains("import java.io.*;"));
        assertTrue(file.getPreamble().contains("import java.util.*;"));
        
        final Specification spec = file.getSpecifications().get(0);
        
        assertTrue(spec.getLanguageDeclarations().contains("int i;"));
        
        assertEquals(1, spec.getLanguageModifiers().size());
        assertEquals("fullbinding", spec.getLanguageModifiers().get(0));
        assertEquals("HasNext", spec.getName());
        assertEquals(1, spec.getProperties().size());
        
        final Property ereProp = spec.getProperties().get(0);
        assertEquals("(hasnext hasnext* next)*", ereProp.getSyntax().trim());
        assertEquals(1, ereProp.getHandlers().size());
        
        final PropertyHandler failHandler = ereProp.getHandlers().get(0);
        assertEquals("fail", failHandler.getState());
    }
    
}