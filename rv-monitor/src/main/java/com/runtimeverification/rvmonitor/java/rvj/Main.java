/**
 * @author fengchen, Dongyun Jin, Patrick Meredith, Michael Ilseman
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package com.runtimeverification.rvmonitor.java.rvj;

import com.runtimeverification.rvmonitor.java.rvj.logicclient.LogicRepositoryConnector;
import com.runtimeverification.rvmonitor.java.rvj.output.CodeGenerationOption;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;
import com.runtimeverification.rvmonitor.util.RVMException;
import com.runtimeverification.rvmonitor.util.Tool;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;

class JavaFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.endsWith(".java");
    }
}

class RVMFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.endsWith(Tool.getSpecFileDotExt());
    }
}

public class Main {
    
    static File outputDir = null;
    public static boolean debug = false;
    public static boolean noopt1 = false;
    public static boolean toJavaLib = false;
    public static boolean statistics = false;
    public static boolean statistics2 = false;
    public static String aspectname = null;
    public static boolean isJarFile = false;
    public static String jarFilePath = null;
    
    public static boolean dacapo = false;
    public static boolean dacapo2 = false;
    public static boolean silent = false;
    public static boolean empty_advicebody = false;
    
    public static boolean merge = false;
    public static boolean inline = false;
    
    public static boolean internalBehaviorObserving = false;
    
    public static boolean useFineGrainedLock = false;
    public static boolean useWeakRefInterning = false;
    
    public static boolean generateVoidMethods = false;
    public static boolean stripUnusedParameterInMonitor = true;
    public static boolean eliminatePresumablyRemnantCode = true;
    public static boolean suppressActivator = false;
    public static boolean usePartitionedSet = false;
    public static boolean useAtomicMonitor = true;
    
    /**
     * The target directory for outputting the results produced from some specification files.
     * If {@link outputDir} is already set, use that. If the input files are all in the same
     * directory, return that directory. Otherwise, return the current directory.
     * @param specFiles The specification files used in the input.
     * @return The place to put the output files.
     */
    static private File getTargetDir(ArrayList<File> specFiles) throws RVMException {
        if(Main.outputDir != null) {
            return outputDir;
        }
        
        boolean sameDir = true;
        File parentFile = null;
        
        for(File file : specFiles) {
            if(parentFile == null) {
                parentFile = file.getAbsoluteFile().getParentFile();
            } else {
                if(file.getAbsoluteFile().getParentFile().equals(parentFile)) {
                    continue;
                } else {
                    sameDir = false;
                    break;
                }
            }
        }
        
        if(sameDir) {
            return parentFile;
        } else {
            return new File(".");
        }
    }
    
    
    /**
     * Process a java file including mop annotations to generate a runtime
     * monitor file. The file argument should be an initialized file object.
     * The location argument should contain the original file name, but it
     * may have a different directory.
     * 
     * @param file
     *            a File object containing the specification file
     * @param location
     *            an absolute path for result file
     */
    public static void processJavaFile(File file, String location) throws RVMException {
        RVMNameSpace.init();
        String specStr = SpecExtractor.process(file);
        RVMSpecFile spec =  SpecExtractor.parse(specStr);
        
        RVMProcessor processor = new RVMProcessor(Main.aspectname == null ? Tool.getFileName(file.getAbsolutePath()) : Main.aspectname);
        
        String aspect = processor.process(spec);
        writeAspectFile(aspect, location);
    }
    
    /**
     * Process a specification file to generate a runtime monitor file.
     * The file argument should be an initialized file object. The location
     * argument should contain the original file name, But it may have a
     * different directory.
     *
     * @param file
     *            a File object containing the specification file
     * @param location
     *            an absolute path for result file
     */
    public static void processSpecFile(File file, String location) throws RVMException {
        RVMNameSpace.init();
        String specStr = SpecExtractor.process(file);
        RVMSpecFile spec =  SpecExtractor.parse(specStr);
        
        RVMProcessor processor = new RVMProcessor(Main.aspectname == null ? Tool.getFileName(file.getAbsolutePath()) : Main.aspectname);
        
        String output = processor.process(spec);
        
        if (toJavaLib) {
            writeJavaLibFile(output, location);
        } else {
            writeAspectFile(output, location);
        }
    }
    
    /**
     * Aggregate and process multiple specification files to generate a runtime monitor file.
     * @param specFiles All the file objects used to construct the monitor object.
     */
    public static void processMultipleFiles(ArrayList<File> specFiles) throws RVMException {
        String aspectName;
        
        if(outputDir == null) {
            outputDir = getTargetDir(specFiles);
        }
        
        if(Main.aspectname != null) {
            aspectName = Main.aspectname;
        } else {
            if(specFiles.size() == 1) {
                aspectName = Tool.getFileName(specFiles.get(0).getAbsolutePath());
            } else {
                int suffixNumber = 0;
                // generate auto name like 'MultiMonitorApsect.aj'
                
                File aspectFile;
                do{
                    suffixNumber++;
                    aspectFile = new File(outputDir.getAbsolutePath() + File.separator + "MultiSpec_" + suffixNumber + "RuntimeMonitor.java");
                } while(aspectFile.exists());
                
                aspectName = "MultiSpec_" + suffixNumber;
            }
        }
        
        RVMNameSpace.init();
        ArrayList<RVMSpecFile> specs = new ArrayList<RVMSpecFile>();
        for(File file : specFiles) {
            String specStr = SpecExtractor.process(file);
            RVMSpecFile spec =  SpecExtractor.parse(specStr);
            
            specs.add(spec);
        }
        RVMSpecFile combinedSpec = SpecCombiner.process(specs);
        
        RVMProcessor processor = new RVMProcessor(aspectName);
        String output = processor.process(combinedSpec);
        
        writeCombinedAspectFile(output, aspectName);
    }
    
    /**
     * Output some text into a Java file.
     * @param javaContent The Java code to write to the file.
     * @param location The place to write the Java code into.
     */
    protected static void writeJavaFile(String javaContent, String location) throws RVMException {
        if ((javaContent == null) || (javaContent.length() == 0))
            throw new RVMException("Nothing to write as a java file");
        if (!Tool.isJavaFile(location))
            throw new RVMException(location + "should be a Java file!");
        
        try {
            FileWriter f = new FileWriter(location);
            f.write(javaContent);
            f.close();
        } catch (Exception e) {
            throw new RVMException(e.getMessage());
        }
    }
    
    /**
     * Write an aspect file with the given content and name.
     * @param aspectContent The text to write into the file.
     * @param aspectName The name of the aspect being written.
     */
    protected static void writeCombinedAspectFile(String aspectContent, String aspectName) throws RVMException {
        if (aspectContent == null || aspectContent.length() == 0)
            return;
        
        try {
            FileWriter f = new FileWriter(outputDir.getAbsolutePath() + File.separator + aspectName + "RuntimeMonitor.java");
            f.write(aspectContent);
            f.close();
        } catch (Exception e) {
            throw new RVMException(e.getMessage());
        }
        System.out.println(" " + aspectName + "RuntimeMonitor.java is generated");
    }
    
    /**
     * Write an aspect file targeting a particular destination file.
     * @param aspectContent The text of the file to write.
     * @param location The place to write the file to.
     */
    protected static void writeAspectFile(String aspectContent, String location) throws RVMException {
        if (aspectContent == null || aspectContent.length() == 0)
            return;
        String name = (Main.aspectname == null?Tool.getFileName(location):Main.aspectname); 
        
        int i = location.lastIndexOf(File.separator);
        try {
            FileWriter f = new FileWriter(location.substring(0, i + 1) + name + "RuntimeMonitor.java");
            f.write(aspectContent);
            f.close();
        } catch (Exception e) {
            throw new RVMException(e.getMessage());
        }
        System.out.println(" " + name + "RuntimeMonitor.java is generated");
    }
    
    /**
     * Write the code needed to produce a monitor as a java library. to a file.
     * @param javaLibContent The text of the file to write.
     * @param location The place to write the file to.
     */
    protected static void writeJavaLibFile(String javaLibContent, String location) throws RVMException {
        if (javaLibContent == null || javaLibContent.length() == 0)
            return;
        
        int i = location.lastIndexOf(File.separator);
        try {
            FileWriter f = new FileWriter(location.substring(0, i + 1) + Tool.getFileName(location) + "JavaLibMonitor.java");
            f.write(javaLibContent);
            f.close();
        } catch (Exception e) {
            throw new RVMException(e.getMessage());
        }
        System.out.println(" " + Tool.getFileName(location) + "JavaLibMonitor.java is generated");
    }
    
    /**
     * Write the output of a plugin to a file.
     * @param pluginOutput The output of the plugin files.
     * @param location The place to write the plugin output to.
     */
    protected static void writePluginOutputFile(String pluginOutput, String location) throws RVMException {
        int i = location.lastIndexOf(File.separator);
        
        try {
            FileWriter f = new FileWriter(location.substring(0, i + 1) + Tool.getFileName(location) + "PluginOutput.txt");
            f.write(pluginOutput);
            f.close();
        } catch (Exception e) {
            throw new RVMException(e.getMessage());
        }
        System.out.println(" " + Tool.getFileName(location) + "PluginOutput.txt is generated");
    }
    
    /**
     * Reformat a path to deal with platform-specific oddities.
     * @param path The path to clean up.
     * @return A cleaned up path.
     */
    public static String polishPath(String path) {
        if (path.indexOf("%20") > 0)
            path = path.replaceAll("%20", " ");
        
        return path;
    }
    
    /**
     * Filter to Java and RVM files and construct their full paths.
     * @param files An array of files that might be involved.
     * @param path The directory to look for the files in.
     * @return A filtered list of relevant files with full paths.
     */
    public static ArrayList<File> collectFiles(String[] files, String path) throws RVMException {
        ArrayList<File> ret = new ArrayList<File>();
        
        for (String file : files) {
            String fPath = path.length() == 0 ? file : path + File.separator + file;
            File f = new File(fPath);
            
            if (!f.exists()) {
                throw new RVMException("[Error] Target file, " + file + ", doesn't exsit!");
            } else if (f.isDirectory()) {
                ret.addAll(collectFiles(f.list(), f.getAbsolutePath()));
            } else {
                if (Tool.isSpecFile(file)) {
                    ret.add(f);
                } else if (Tool.isJavaFile(file)) {
                    ret.add(f);
                } else {
                    // Just ignore it, so we can go arbitrary deep with directories.
                    /*throw new RVMException("Unrecognized file type! The RV " +
                    "Monitor specification file should have .rvm as" +
                    " the extension."); */
                }
            }
        }
        
        return ret;
    }
    
    /**
     * Process an array of files at a base path.
     * @param files All the files to consider when producing output.
     * @param path The base path of the files.
     */
    public static void process(String[] files, String path) throws RVMException {
        ArrayList<File> specFiles = collectFiles(files, path);
        
        if(Main.aspectname != null && files.length > 1) {
            Main.merge = true;
        }
        
        if (Main.merge) {
            System.out.println("-Processing " + specFiles.size() + " specification(s)");
            processMultipleFiles(specFiles);
        } else {
            for (File file : specFiles) {
                String location = outputDir == null ? file.getAbsolutePath() : outputDir.getAbsolutePath() + File.separator + file.getName();
                
                System.out.println("-Processing " + file.getPath());
                if (Tool.isSpecFile(file.getName())) {
                    processSpecFile(file, location);
                } else if (Tool.isJavaFile(file.getName())) {
                    processJavaFile(file, location);
                }
            }
        }
    }
    
    /**
     * Process a semicolon-separated list of files.
     * @param arg A list of files, separated by semicolons.
     */
    public static void process(String arg) throws RVMException {
        if(outputDir != null && !outputDir.exists())
            throw new RVMException("The output directory, " + outputDir.getPath() + " does not exist.");
        
        process(arg.split(";"), "");
    }
    
    /**
     * Print the command line options usable with Java RV-Monitor.
     */
    public static void print_help() {
        System.out.println("Usage: java [-cp rv_monitor_classpath] com.runtimeverification.rvmonitor.java.rvj.Main [-options] files");
        System.out.println("");
        System.out.println("where options include:");
        System.out.println(" Options enabled by default are prefixed with \'+\'");
        System.out.println("    -h -help\t\t\t  print this help message");
        System.out.println("    -version\t\t\t  display RV-Monitor version information");
        System.out.println("    -v | -verbose\t\t  enable verbose output");
        System.out.println("    -debug\t\t\t  enable verbose error message");
        System.out.println();
        
        System.out.println("    -local\t\t\t+ use local logic engine");
        System.out.println("    -remote\t\t\t  use default remote logic engine");
        System.out.println("\t\t\t\t  " + Configuration.getServerAddr());
        System.out.println("\t\t\t\t  (You can change the default address");
        System.out.println("\t\t\t\t   in com/runtimeverification/rvmonitor/java/rvj/config/remote_server_addr.properties)");
        System.out.println("    -remote:<server address>\t  use remote logic engine");
        System.out.println();
        
        System.out.println("    -d <output path>\t\t  select directory to store output files");
        System.out.println("    -n <name>\t\t\t  use the given class name instead of source code name");
        System.out.println();
        
        System.out.println("    -s | -statistics\t\t  generate monitor with statistics");
        System.out.println("    -noopt1\t\t\t  don't use the enable set optimization");
        System.out.println();
        
        System.out.println("    -finegrainedlock\t\t  use fine-grained lock for internal data structure");
        System.out.println("    -weakrefinterning\t\t  use WeakReference interning in indexing trees");
        System.out.println();
        
    }
    
    /**
     * Run Java RV-Monitor on some files.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        ClassLoader loader = Main.class.getClassLoader();
        String mainClassPath = loader.getResource("com/runtimeverification/rvmonitor/java/rvj/Main.class").toString();
        if (mainClassPath.endsWith(".jar!/com/runtimeverification/rvmonitor/java/rvj/Main.class") && mainClassPath.startsWith("jar:")) {
            isJarFile = true;
            
            jarFilePath = mainClassPath.substring("jar:file:".length(), mainClassPath.length() - "!/com/runtimeverification/rvmonitor/java/rvj/Main.class".length());
            jarFilePath = polishPath(jarFilePath);
        }
        
        int i = 0;
        String files = "";
        
        while (i < args.length) {
            if ("-h".equals(args[i]) || "-help".equals(args[i])) {
                print_help();
                return;
            }
            
            if ("-d".equals(args[i])) {
                i++;
                outputDir = new File(args[i]);
            } else if ("-local".equals(args[i])) {
                LogicRepositoryConnector.serverName = "local";
            } else if ("-remote".equals(args[i])) {
                LogicRepositoryConnector.serverName = "default";
            } else if (args[i].startsWith("-remote:")) {
                LogicRepositoryConnector.serverName = args[i].substring(8);
            } else if ("-v".equals(args[i]) || "-verbose".equals(args[i])) {
                LogicRepositoryConnector.verbose = true;
                RVMProcessor.verbose = true;
            } else if ("-javalib".equals(args[i])) {
                toJavaLib = true;
            } else if ("-debug".equals(args[i])) {
                Main.debug = true;
            } else if ("-noopt1".equals(args[i])) {
                Main.noopt1 = true;
            } else if ("-s".equals(args[i]) || "-statistics".equals(args[i])) {
                Main.statistics = true;
            } else if ("-s2".equals(args[i]) || "-statistics2".equals(args[i])) {
                Main.statistics2 = true;
            } else if ("-n".equals(args[i]) || "-aspectname".equals(args[i])) {
                i++;
                Main.aspectname = args[i];
            } else if ("-dacapo".equals(args[i])) {
                Main.dacapo = true;
            } else if ("-dacapo2".equals(args[i])) {
                Main.dacapo2 = true;
            } else if ("-silent".equals(args[i])) {
                Main.silent = true;
            } else if ("-merge".equals(args[i])) {
                Main.merge = true;
            } else if ("-inline".equals(args[i])) {
                Main.inline = true;
            } else if ("-noadvicebody".equals(args[i])) {
                Main.empty_advicebody = true;
            } else if ("-internalbehavior".equals(args[i])) {
                Main.internalBehaviorObserving = true;
            } else if ("-finegrainedlock".equals(args[i])) {
                Main.useFineGrainedLock = true;
            } else if ("-nofinegrainedlock".equals(args[i])) {
                Main.useFineGrainedLock = false;
            } else if ("-weakrefinterning".equals(args[i])) {
                Main.useWeakRefInterning = true;
            } else if ("-noweakrefinterning".equals(args[i])) {
                Main.useWeakRefInterning = false;
            } else if ("-partitionedset".equals(args[i])) {
                Main.usePartitionedSet = true;
            } else if ("-nopartitionedset".equals(args[i])) {
                Main.usePartitionedSet = false;
            } else if ("-atomicmonitor".equals(args[i])) {
                Main.useAtomicMonitor = true;
            } else if ("-noatomicmonitor".equals(args[i])) {
                Main.useAtomicMonitor = false;
            } else if ("-version".equals(args[i])) {
                Tool.printVersionMessage();
                return;
            } else {
                if (files.length() != 0)
                    files += ";";
                files += args[i];
            }
            ++i;
        }
        
        if (files.length() == 0) {
            print_help();
            return;
        }
        
        CodeGenerationOption.initialize();
        
        try {
            process(files);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if (Main.debug)
                e.printStackTrace();
        }
    }
}
