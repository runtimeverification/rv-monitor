package com.runtimeverification.rvmonitor.examples.llvmmop.safety_critical;

import com.runtimeverification.rvmonitor.examples.llvmmop.TestHelper;
import org.junit.Before;

/**
 * Test class for the llvmmop/safety_critical/door_ajar example
 * @author TraianSF
 */
public class DoorAjarTest extends TestHelper {
    @Override
    @Before
    public void setUp() throws Exception {
        specName = "DoorAjar";
        specPath = "examples/llvmmop/safety_critical/door_ajar/door_ajar.rvm";
        super.setUp();
    }


}
