package com.runtimeverification.rvmonitor.examples;

import com.runtimeverification.rvmonitor.util.Tool;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @author TraianSF
 */
public class TestHelper {

    private final FileSystem fileSystem;
    private final File basePathFile;
    private final Path basePath;

    public TestHelper(String filePath)   {
        fileSystem = FileSystems.getDefault();
        this.basePath = fileSystem.getPath(filePath).getParent();
        basePathFile = basePath.toFile();

    }

    /**
     * Execute command, tests return code and potentially checks standard and error output against expected content
     * in files if {@code expectedFilePrefix} not null.
     * @param expectedFilePrefix the prefix for the expected files, or null if output is not checked.
     * @param command  list of arguments describing the system command to be executed.
     * @throws Exception
     */
    public void testCommand(String expectedFilePrefix, String... command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command).inheritIO();
        processBuilder.directory(basePathFile);
        String actualOutFile = null;
        String testsPrefix;
        String actualErrFile = null;
        String expectedOutFile = null;
        String expectedErrFile = null;
        if (expectedFilePrefix != null) {
            testsPrefix = basePath.toString() + "/" + expectedFilePrefix;
            actualOutFile = testsPrefix + ".actual.out";
            actualErrFile = testsPrefix + ".actual.err";
            expectedOutFile = testsPrefix + ".expected.out";
            expectedErrFile = testsPrefix + ".expected.err";
            processBuilder.redirectError(new File(actualErrFile));
            processBuilder.redirectOutput(new File(actualOutFile));
        }
        Process process = processBuilder.start();
        int returnCode = process.waitFor();
        Assert.assertEquals("Expected no error during" + Arrays.toString(command) + ".", 0, returnCode);
        if (expectedFilePrefix != null) {
            Assert.assertEquals(actualOutFile + " should match " + expectedOutFile, Tool.convertFileToString(expectedOutFile),
                    Tool.convertFileToString(actualOutFile));
            Assert.assertEquals(actualErrFile + "should match " + expectedErrFile, Tool.convertFileToString(expectedErrFile),
                    Tool.convertFileToString(actualErrFile));
        }
    }

    public void relocateFiles(String... files) throws IOException {
        for (String s : files) {
            Path path = fileSystem.getPath(basePath.toString(), s);
            Files.move(
                    fileSystem.getPath(s),
                    path
            );
        }
    }

    public void deleteFiles(boolean fail, String... files) throws IOException {
        for (String s : files) {
            Path toDelete = fileSystem.getPath(basePath.toString(), s);
            if (fail) {
                Files.delete(toDelete);
            } else {
                Files.deleteIfExists(toDelete);
            }
        }
    }

    public Path getPath(String path) {
        return fileSystem.getPath(basePath.toString(), path);
    }

}
