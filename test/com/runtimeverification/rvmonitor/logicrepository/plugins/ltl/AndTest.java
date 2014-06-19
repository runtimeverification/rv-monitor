package com.runtimeverification.rvmonitor.logicrepository.plugins.ltl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Set;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AndTest {

    LinkedHashSet<LinkedHashSet<LTLFormula>> one 
     = new LinkedHashSet<LinkedHashSet<LTLFormula>> ();
    LinkedHashSet<LinkedHashSet<LTLFormula>> two 
     = new LinkedHashSet<LinkedHashSet<LTLFormula>> ();

     LinkedHashSet<LTLFormula> in1 = new LinkedHashSet<LTLFormula>();
     LinkedHashSet<LTLFormula> in2 = new LinkedHashSet<LTLFormula>();
     LinkedHashSet<LTLFormula> in3 = new LinkedHashSet<LTLFormula>();
     LinkedHashSet<LTLFormula> in4 = new LinkedHashSet<LTLFormula>();
    


    @Test
    public void testAnd() {
     // see if get properly adds atoms
     //in1.add(Atom.get("1"));
     //in2.add(Atom.get("1"));
     assertTrue(in1.equals(in2));

     System.out.println(one);
     System.out.println(two);
   }


}
