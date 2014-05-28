package com.runtimeverification.rvmonitor.logicrepository.ere;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Epsilon or empty string ERE class.
 * @author A. Cody Schuffelen
 */
public class EpsilonTest {
	
	/**
	 * Test two instances of the class retrieved through the standard method are equal.
	 */
	@Test
	public void testEquality() {
		Epsilon one = Epsilon.get();
		Epsilon two = Epsilon.get();
		assertEquals(one, two);
		assertEquals(0, one.compareTo(two));
	}
	
	/**
	 * Test Epsilon doesn't compare equal with other classes.
	 */
	@Test
	public void testInequality() {
		Epsilon one = Epsilon.get();
		Empty two = Empty.get();
		assertFalse(one.equals(two));
		assertFalse(0 == one.compareTo(two));
	}
	
	/**
	 * Test the EREType of the Epsilon element is correct.
	 */
	@Test
	public void testType() {
		Epsilon epsilon = Epsilon.get();
		assertEquals(epsilon.getEREType(), EREType.EPS);
	}
	
	/**
	 * Ensure the Epsilon instances convert to the expected string.
	 */
	@Test
	public void testString() {
		Epsilon epsilon = Epsilon.get();
		assertEquals("epsilon", epsilon.toString());
	}
	
	/**
	 * Ensure that Epsilon instances derive to empty instances.
	 */
	@Test
	public void testDerive() {
		Epsilon epsilon = Epsilon.get();
		ERE derived = epsilon.derive(Symbol.get("test"));
		Empty empty = Empty.get();
		assertEquals(empty, derived);
	}
	
	/**
	 * Ensure that Epsilon instances contain epsilons.
	 */
	@Test
	public void testContainsEpsilon() {
		Epsilon epsilon = Epsilon.get();
		assertTrue(epsilon.containsEpsilon());
	}
	
	/**
	 * Ensure Empty instances copy to themselves.
	 */
	@Test
	public void testCopy() {
		Epsilon epsilon = Epsilon.get();
		ERE copy = epsilon.copy();
		assertEquals(epsilon, copy);
	}
}