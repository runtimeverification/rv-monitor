package com.runtimeverification.rvmonitor.examples.llvmmop;

import com.runtimeverification.rvmonitor.c.rvc.Main;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Base class for llvmmop examples JUnit tests
 * @author TraianSF
 */
public class TestHelper {
    public static final String RVC = "__RVC_";
    public static final String MONITOR_BC = "_Monitor.bc";
    protected String specPath;
    protected String specName;
    File specFile;
    private FileSystem fileSystem;
    private Path specPathParent;

    @Before
    public void setUp() throws Exception {
        fileSystem = FileSystems.getDefault();
        specPathParent = fileSystem.getPath(specPath).getParent();
        specFile = specPathParent.toFile();

    }


    @Test
    public void testMonitor() throws Exception {
        if (Files.exists(fileSystem.getPath(specPathParent.toString(), RVC + specName + MONITOR_BC))) {
            return;
        }
        deleteFiles(false,
                RVC + specName + MONITOR_BC,
                "Makefile",
                "Makefile.instrument",
                "aspect.map"
        );
        Main.main(new String[]{"-llvm", specPath});
        relocateFiles(
                RVC + specName + MONITOR_BC,
                "Makefile.instrument",
                "Makefile.new",
                "aspect.map"
        );

        Files.move(
                fileSystem.getPath(specPathParent.toString(), "Makefile.new"),
                fileSystem.getPath(specPathParent.toString(), "Makefile")
        );
    }

    @Test
    public void testMake() throws Exception {
        testCommand("make");
    }

    @Test
    public void testMakeInstrument() throws Exception {
        testCommand("make", "instrument");
    }

    @Test
    public void testMakeUnInstrument() throws Exception {
        testCommand("make", "uninstrument");
    }

    @Test
    public void testMakeClean() throws Exception {
        testCommand("make", "clean");
    }

    private void testCommand(String... command) throws Exception {
        testMonitor();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(specFile);
        Process process = processBuilder.start();
        int returnCode = process.waitFor();
        Assert.assertEquals("Expected no error during" + Arrays.toString(command) + ".", 0, returnCode);
    }

    private void relocateFiles(String... files) throws IOException {
        for (String s : files) {
            Path path = fileSystem.getPath(specPathParent.toString(), s);
            Files.move(
                    fileSystem.getPath(s),
                    path
            );
        }
    }

    private void deleteFiles(boolean fail, String... files) throws IOException {
        for (String s : files) {
            Path toDelete = fileSystem.getPath(specPathParent.toString(), s);
            if (fail) {
                Files.delete(toDelete);
            } else {
                Files.deleteIfExists(toDelete);
            }
        }
    }
}
