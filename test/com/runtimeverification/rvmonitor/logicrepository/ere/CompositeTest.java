package com.runtimeverification.rvmonitor.logicrepository.ere;

import java.util.ArrayList;
import java.util.Arrays;

import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Concat;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Empty;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Epsilon;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.ERE;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.EREType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Kleene;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Or;
import com.runtimeverification.rvmonitor.logicrepository.plugins.ere.Symbol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test some composite/complex examples with many elements.
 */
public class CompositeTest {
	
	/**
	 * Test that (ab)^3 | (abab)* matches abab, abababab, but not ab.
	 */
	@Test
	public void testRepeatOrKleene() {
		Symbol a = Symbol.get("a");
		Symbol b = Symbol.get("b");
		
		ERE ab = Concat.get(a, b);
		ERE abab = Concat.get(ab, ab);
		ERE abab_star = Kleene.get(abab);
		ERE ababab = Concat.get(abab, ab);
		ERE or = Or.get(new ArrayList<ERE>(Arrays.asList(abab_star, ababab)));
		
		assertTrue(or.derive(a).derive(b).derive(a).derive(b).derive(a).derive(b).containsEpsilon());
		assertTrue(or.derive(a).derive(b).derive(a).derive(b).containsEpsilon());
		
		assertFalse(or.derive(a).derive(b).containsEpsilon());
	}
}