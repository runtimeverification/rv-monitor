package com.runtimeverification.rvmonitor.core.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.io.*;

import com.runtimeverification.rvmonitor.core.ast.Event;
import com.runtimeverification.rvmonitor.core.ast.Property;
import com.runtimeverification.rvmonitor.core.ast.PropertyHandler;
import com.runtimeverification.rvmonitor.core.ast.Specification;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testHasNext() throws FileNotFoundException {
        Specification spec = RVParser.parse(new InputStreamReader(new FileInputStream(
            "examples/newsyntax/HasNext.rvm")));
        
        assertTrue(spec.getPreDeclarations().contains("package mop;"));
        assertTrue(spec.getPreDeclarations().contains("import java.io.*;"));
        assertTrue(spec.getPreDeclarations().contains("import java.util.*;"));
        
        assertEquals(0, spec.getLanguageModifiers().size());
        assertEquals("HasNext", spec.getName());
        assertEquals(1, spec.getProperties().size());
        
        Property ereProp = spec.getProperties().get(0);
        assertEquals("(hasnext hasnext* next)*", ereProp.getSyntax());
        assertEquals(1, ereProp.getHandlers().size());
        
        PropertyHandler failHandler = ereProp.getHandlers().get(0);
        assertEquals("fail", failHandler.getState());
    }
    
}