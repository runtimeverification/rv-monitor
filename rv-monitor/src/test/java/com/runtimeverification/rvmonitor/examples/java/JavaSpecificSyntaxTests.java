package com.runtimeverification.rvmonitor.examples.java;

import com.runtimeverification.rvmonitor.java.rvj.JavaParserAdapter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.rvmspec.RVMParameters;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.RVMSpecFileExt;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.rvmspec.EventDefinitionExt;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.rvmspec.RVMonitorSpecExt;
import com.runtimeverification.rvmonitor.util.RVMException;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JavaSpecificSyntaxTests {

    private static final String errIncorrectArgNum = "Unexpected number of parameters";
    private static final String errIncorrectGenericType = "Unexpected number of parameters";

    private File fromResources(Path filePath) {
        ClassLoader classLoader = getClass().getClassLoader();

        return new File(classLoader.getResource(filePath.toString()).getFile());
    }

    private EventDefinitionExt getEvent(String folder, String name) {
        RVMSpecFileExt parsedFile = null;
        try {
            parsedFile = JavaParserAdapter.parse(fromResources(Paths.get(folder, name)));
            assertNotNull("Parser neither parsed file nor threw exception", parsedFile);

        } catch (RVMException e) {
            fail("Unexpected ParseException" + e.getMessage());
        }

        List<RVMonitorSpecExt> specs = parsedFile.getSpecs();
        assertEquals("Unexpected number of Spec ASTs", 1, specs.size());
        RVMonitorSpecExt spec = specs.get(0);

        List<EventDefinitionExt> events = spec.getEvents();
        assertEquals("Unexpected number of Event ASTs", 1, events.size());

        return events.get(0);
    }

    @Test
    public void testGenericsInAdviceParams() {
        EventDefinitionExt testEvent = getEvent("java", "AdviceParamsWithGenerics.rvm");
        RVMParameters adviceParams = testEvent.getParameters();

        assertEquals(errIncorrectArgNum, 1, adviceParams.size());
        assertEquals(errIncorrectGenericType
                , "List<Bar>"
                , adviceParams.get(0).getType().toString());
    }

    @Test
    public void testGenericsInAdviceParamsMultiple() {
        EventDefinitionExt testEvent = getEvent("java", "AdviceParamsWithGenericsMultiple.rvm");
        RVMParameters adviceParams = testEvent.getParameters();

        assertEquals(errIncorrectArgNum, 2, adviceParams.size());
        assertEquals(errIncorrectGenericType
                , "List<Bar>"
                , adviceParams.get(0).getType().toString());

        assertEquals(errIncorrectGenericType
                , "List<Foo>"
                , adviceParams.get(1).getType().toString());
    }


    @Test
    public void testGenericsInAdviceParamsNested() {
        EventDefinitionExt testEvent = getEvent("java", "AdviceParamsWithGenericsNested.rvm");
        RVMParameters adviceParams = testEvent.getParameters();

        assertEquals(errIncorrectArgNum, 1, adviceParams.size());
        assertEquals(errIncorrectGenericType
                , "List<List<Bar>>"
                , adviceParams.get(0).getType().toString());

    }
}
