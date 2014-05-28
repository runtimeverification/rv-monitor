package com.runtimeverification.rvmonitor.logicrepository.ere;

import java.util.ArrayList;
import java.util.Arrays;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Or;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Or ERE element.
 */
public class OrTest {
	
	/**
	 * Test that equivalent Or elements compare equal.
	 */
	@Test
	public void testEquality() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		
		ERE or = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		ERE or_again = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		ERE or_reverse = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		
		assertEquals(or, or_again);
		assertEquals(0, or.compareTo(or_again));
		assertEquals(or, or_reverse);
		assertEquals(0, or.compareTo(or_reverse));
	}
	
	/**
	 * Test that different Or elements compare inequal.
	 */
	@Test
	public void testInequality() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		Symbol c = Symbol.get("c");
		
		ERE aOrb = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		ERE aOrc = Or.get(new ArrayList<ERE>(Arrays.asList(a, c)));
		
		assertFalse(aOrb.equals(aOrc));
		assertFalse(0 == aOrb.compareTo(aOrc));
		assertFalse(aOrb.equals(a));
		assertFalse(0 == aOrb.compareTo(a));
	}
	
	/**
	 * Test that copied Or elements compare equal.
	 */
	@Test
	public void testCopy() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		
		ERE or = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		ERE copy = or.copy();
		assertEquals(or, copy);
	}
	
	/**
	 * Test that Or elements have the correct EREType.
	 */
	@Test
	public void testEREType() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		
		ERE or = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		assertEquals(EREType.OR, or.getEREType());
	}
	
	/**
	 * Test that Or elements produce the correct strings.
	 */
	@Test
	public void testString() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		
		ERE aOrb = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		assertEquals("(a | b)", aOrb.toString());
		
		Symbol c = Symbol.get("c");
		ERE aOrbOrc = Or.get(new ArrayList<ERE>(Arrays.asList(a, b, c)));
		assertEquals("(a | b | c)", aOrbOrc.toString());
	}
	
	/**
	 * Test that Or elements contain an epsilon if they have a member that contains an epsilon.
	 */
	@Test
	public void testContainsEpsilon() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		
		ERE aOrb = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		assertFalse(aOrb.containsEpsilon());
		
		Epsilon epsilon = Epsilon.get();
		ERE aOrepsilon = Or.get(new ArrayList<ERE>(Arrays.asList(a, epsilon)));
		assertTrue(aOrepsilon.containsEpsilon());
	}
	
	/**
	 * Test that Or elements can derive to any of the members they contain and not anything else.
	 */
	@Test
	public void testDerive() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		Symbol c = Symbol.get("c");
		
		Epsilon epsilon = Epsilon.get();
		Empty empty = Empty.get();
		
		ERE aOrb = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		
		assertEquals(epsilon, aOrb.derive(a));
		assertEquals(epsilon, aOrb.derive(b));
		assertEquals(empty, aOrb.derive(c));
	}
	
	/**
	 * Test that Or elements are flattened and simplified on creation.
	 */
	@Test
	public void testSimplify() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		Symbol c = Symbol.get("c");
		
		Epsilon epsilon = Epsilon.get();
		
		ERE aOrb = Or.get(new ArrayList<ERE>(Arrays.asList(a, b)));
		assertEquals(aOrb, Or.get(new ArrayList<ERE>(Arrays.asList(a, b, a, b, a, b, a, b, a))));
		assertEquals(Or.get(new ArrayList<ERE>(Arrays.asList(a, b, c))), Or.get(new ArrayList<ERE>(Arrays.asList(aOrb, c))));
	}
}