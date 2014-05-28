package com.runtimeverification.rvmonitor.logicrepository.ere;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Concat;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Kleene;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Kleene Star ERE operator.
 * @author A. Cody Schuffelen
 */
public class KleeneTest {
	
	/**
	 * Test that equivalent Kleene operators compare equal.
	 */
	@Test
	public void testEquality() {
		Symbol a = Symbol.get("a");
		ERE aStar = Kleene.get(a);
		ERE aStar_again = Kleene.get(a);
		
		assertEquals(aStar, aStar_again);
		assertEquals(0, aStar.compareTo(aStar_again));
	}
	
	/**
	 * Test that different Kleene operators compare inequal, and Kleene operators compare inequal with other operators.
	 */
	@Test
	public void testInequality() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		
		ERE aStar = Kleene.get(a);
		ERE bStar = Kleene.get(b);
		
		assertFalse(aStar.equals(bStar));
		assertFalse(0 == aStar.compareTo(bStar));
		assertFalse(aStar.equals(a));
		assertFalse(0 == aStar.compareTo(a));
	}
	
	/**
	 * Test that Kleene EREs have the correct EREType.
	 */
	@Test
	public void testEREType() {
		Symbol a = Symbol.get("a");
		ERE aStar = Kleene.get(a);
		
		assertEquals(EREType.STAR, aStar.getEREType());
	}
	
	/**
	 * Test that Kleene operators convert to strings properly.
	 */
	@Test
	public void testString() {
		Symbol a = Symbol.get("a");
		ERE aStar = Kleene.get(a);
		
		assertEquals("a*", aStar.toString());
	}
	
	/**
	 * Test that Kleene stars contain epsilons.
	 */
	@Test
	public void testContainsEpsilon() {
		Symbol a = Symbol.get("a");
		ERE aStar = Kleene.get(a);
		
		assertTrue(aStar.containsEpsilon());
	}
	
	/**
	 * Test that copying Kleene elements produces equivalent ones.
	 */
	@Test
	public void testCopy() {
		Symbol a = Symbol.get("a");
		ERE aStar = Kleene.get(a);
		
		ERE copy = aStar.copy();
		assertEquals(aStar, copy);
	}
	
	/**
	 * Test that Kleene elements derive correctly.
	 */
	@Test
	public void testDerive() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		Epsilon epsilon = Epsilon.get();
		Empty empty = Empty.get();
		
		ERE aStar = Kleene.get(a);
		assertEquals(aStar, aStar.derive(a));
		assertEquals(empty, aStar.derive(b));
		
		ERE ab = Concat.get(a, b);
		ERE ab_Star = Kleene.get(ab);
		assertEquals(ab_Star, ab_Star.derive(a).derive(b));
		assertEquals(empty, ab_Star.derive(b));
	}
}