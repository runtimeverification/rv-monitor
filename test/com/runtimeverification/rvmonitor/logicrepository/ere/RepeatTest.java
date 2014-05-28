package com.runtimeverification.rvmonitor.logicrepository.ere;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Repeat;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test ERE repeating elements.
 * @author A. Cody Schuffelen
 */
public class RepeatTest {
	
	/**
	 * Test that two equivalent repeat elements are equal.
	 */
	@Test
	public void equalityTest() {
		Symbol a = Symbol.get("a");
		ERE a_x5 = Repeat.get(a, 5);
		ERE a_x5_again = Repeat.get(a, 5);
		
		assertEquals(a_x5, a_x5_again);
		assertEquals(0, a_x5.compareTo(a_x5_again));
	}
	
	/**
	 * Test two different repeat elements are inequal.
	 */
	@Test
	public void inequalityTest() {
		Symbol a = Symbol.get("a");
		ERE a_x5 = Repeat.get(a, 5);
		ERE a_x10 = Repeat.get(a, 10);
		Symbol b = Symbol.get("b");
		ERE b_x5 = Repeat.get(b, 5);
		
		assertFalse(a_x5.equals(a_x10));
		assertFalse(0 == a_x5.compareTo(a_x10));
		assertFalse(a_x5.equals(b_x5));
		assertFalse(0 == a_x5.compareTo(b_x5));
	}
	
	/**
	 * Test that repeat elements derive the repeated element the required number of times, and not other elements.
	 */
	@Test
	public void deriveTest() {
		Symbol a = Symbol.get("a");
		ERE a_x3 = Repeat.get(a, 3);
		Epsilon epsilon = Epsilon.get();
		
		assertEquals(epsilon, a_x3.derive(a).derive(a).derive(a));
		
		Empty empty = Empty.get();
		assertEquals(empty, a_x3.derive(a).derive(a).derive(a).derive(a));
		
		Symbol b = Symbol.get("b");
		assertEquals(empty, a_x3.derive(b));
	}
	
	/**
	 * Test that repeat elements do not contain epsilons.
	 */
	@Test
	public void containsEpsilonTest() {
		Symbol a = Symbol.get("a");
		ERE a_x3 = Repeat.get(a, 3);
		assertFalse(a_x3.containsEpsilon());
	}
	
	/**
	 * Test that copied repeat elements compare equal.
	 */
	@Test
	public void testCopy() {
		Symbol a = Symbol.get("a");
		ERE a_x3 = Repeat.get(a, 3);
		ERE copy = a_x3.copy();
		assertEquals(a_x3, copy);
	}
}