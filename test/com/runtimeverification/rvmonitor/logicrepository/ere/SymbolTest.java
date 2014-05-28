package com.runtimeverification.rvmonitor.logicrepository.ere;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Symbol ERE class.
 * @author A. Cody Schuffelen
 */
public class SymbolTest {
	
	/**
	 * Test that symbols with the same string are equal.
	 */
	@Test
	public void testEquality() {
		Symbol one = Symbol.get("one");
		Symbol one_again = Symbol.get("one");
		
		assertEquals(one, one_again);
		assertEquals(0, one.compareTo(one_again));
	}
	
	/**
	 * Test that symbols with different strings are inequal.
	 */
	@Test
	public void testInequality() {
		Symbol one = Symbol.get("one");
		Symbol two = Symbol.get("two");
		
		assertFalse(one.equals(two));
		assertFalse(0 == one.compareTo(two));
		
		Empty empty = Empty.get();
		Epsilon epsilon = Epsilon.get();
		
		assertFalse(one.equals(empty));
		assertFalse(one.equals(epsilon));
		
		assertFalse(0 == one.compareTo(empty));
		assertFalse(0 == one.compareTo(epsilon));
	}
	
	/**
	 * Test that copied symbols are equal.
	 */
	@Test
	public void testCopy() {
		Symbol one = Symbol.get("one");
		ERE copy = one.copy();
		assertEquals(one, copy);
	}
	
	/**
	 * Test that the EREType returned is correct.
	 */
	@Test
	public void testEREType() {
		Symbol one = Symbol.get("one");
		EREType type = one.getEREType();
		
		assertEquals(EREType.S, type);
	}
	
	/**
	 * Test that symbols cannot contain epsilons.
	 */
	@Test
	public void testContainsEpsilon() {
		Symbol one = Symbol.get("one");
		
		assertFalse(one.containsEpsilon());
	}
	
	/**
	 * Test that symbols derive to epsilon with respect to themselves and
	 * empty with respect to other symbols.
	 */
	@Test
	public void testDerive() {
		Symbol one = Symbol.get("one");
		Symbol two = Symbol.get("two");
		
		Empty empty = Empty.get();
		Epsilon epsilon = Epsilon.get();
		
		assertEquals(epsilon, one.derive(one));
		assertEquals(empty, one.derive(two));
		assertEquals(epsilon, two.derive(two));
		assertEquals(empty, two.derive(one));
	}
	
	/**
	 * Test that symbols convert to strings with their names.
	 */
	@Test
	public void testString() {
		Symbol one = Symbol.get("one");
		Symbol two = Symbol.get("two");
		
		assertEquals("one", one.toString());
		assertEquals("two", two.toString());
	}
}