package com.runtimeverification.rvmonitor.parser;

import com.runtimeverification.rvmonitor.core.ast.MonitorFile;
import com.runtimeverification.rvmonitor.core.parser.RVParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private File fromResources(Path filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(filePath.toString()).getFile());
    }

    @Test
    public void testUseFunctionContainsEvent() {
        try {
            File input = fromResources(Paths.get("parser", "UserFunctionContainsEvent.rvm"));
            BufferedReader inputReader = new BufferedReader(new FileReader(input));
            MonitorFile spec = RVParser.parse(inputReader);
            assertNotNull("Parser neither parsed file nor threw exception", spec);
        } catch(FileNotFoundException e) {
            fail("Test input not found: " + e.getMessage());
        } catch(Exception e) {
            fail("Unexpected Parse Exception: " + e.getMessage());
        }
    }


}
