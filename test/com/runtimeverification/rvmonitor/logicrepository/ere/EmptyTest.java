package com.runtimeverification.rvmonitor.logicrepository.ere;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the Empty ERE class.
 * @author A. Cody Schuffelen
 */
public class EmptyTest {
	
	/**
	 * Test two instances of the class retrieved through the standard method are equal.
	 */
	@Test
	public void testEquality() {
		Empty one = Empty.get();
		Empty two = Empty.get();
		assertEquals(one, two);
		assertEquals(0, one.compareTo(two));
	}
	
	/**
	 * Test Empty doesn't compare equal with other classes.
	 */
	@Test
	public void testInequality() {
		Empty one = Empty.get();
		Epsilon two = Epsilon.get();
		assertFalse(one.equals(two));
		assertFalse(0 == one.compareTo(two));
	}
	
	/**
	 * Test the EREType of the Empty element is correct.
	 */
	@Test
	public void testType() {
		Empty empty = Empty.get();
		assertEquals(empty.getEREType(), EREType.EMP);
	}
	
	/**
	 * Ensure the Empty instances convert to the expected string.
	 */
	@Test
	public void testString() {
		Empty empty = Empty.get();
		assertEquals("empty", empty.toString());
	}
	
	/**
	 * Ensure that Empty instances derive to themselves.
	 */
	@Test
	public void testDerive() {
		Empty empty = Empty.get();
		ERE derived = empty.derive(Symbol.get("test"));
		assertEquals(empty, derived);
	}
	
	/**
	 * Ensure that Empty instances don't contain epsilons.
	 */
	@Test
	public void testContainsEpsilon() {
		Empty empty = Empty.get();
		assertFalse(empty.containsEpsilon());
	}
	
	/**
	 * Ensure Empty instances copy to themselves.
	 */
	@Test
	public void testCopy() {
		Empty empty = Empty.get();
		ERE copy = empty.copy();
		assertEquals(empty, copy);
	}
}