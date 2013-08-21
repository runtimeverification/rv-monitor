/*
 * entry class for RVC - based somewhat off the MOP logic repository code,
 * but bare bones and easier to maintain
 * 
 * author: Patrick Meredith
 *
 * previous author : Dongyun Jin 
 */

package com.runtimeverification.rvmonitor.c.rvc;

import com.runtimeverification.rvmonitor.logicpluginshells.fsm.CFSM;
import com.runtimeverification.rvmonitor.logicpluginshells.cfg.CCFG;
import com.runtimeverification.rvmonitor.logicrepository.LogicRepositoryData;
import com.runtimeverification.rvmonitor.logicrepository.LogicException;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.PropertyType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.*;
import com.runtimeverification.rvmonitor.logicpluginshells.*;

import com.runtimeverification.rvmonitor.c.rvc.parser.RVCParser;

import com.runtimeverification.rvmonitor.util.RVMException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import java.util.Scanner;

public class Main {
  public static boolean isJarFile = false;
  public static String jarFilePath = null;
  public static String basePath = null;

  private static boolean parametric = false;

  private static RVCParser rvcParser;

  static public void main(String[] args) {
     
    try{
   
      basePath = getBasePath();
    
      handleArgs(args);

      String logicPluginDirPath = polishPath(readLogicPluginDir(basePath));
      
      File dirLogicPlugin = new File(logicPluginDirPath);
      
      if(!dirLogicPlugin.exists()){
        throw new LogicException(
      "Unrecoverable error: please place plugins in the default plugins directory:plugins");
      }

      if(args[0].equals("-p")){
        parametric = true; 
        rvcParser = parseInput(args[1]);
      }
      else {
        rvcParser = parseInput(args[0]);
      }

      //send Spec to logic repository to get the logic result 
      LogicRepositoryData cmgDataOut = sendToLogicRepository(logicPluginDirPath);
 
      // Outputting the logic result
      outputCode(cmgDataOut);    
     
    } catch (Exception e) {
      e.printStackTrace();
      //System.out.println(e);
    }
  }

 //put any arg handling code here
 static private void handleArgs(String[] args){
   if(args.length < 1){
     System.err.println("usage is:  rv-monitor -c <spec_file>\n  Please specify a spec file");
     System.exit(1);
   }
 }

 //Finds the base path from which this class was invoked
 static private String getBasePath(){

      ClassLoader loader = Main.class.getClassLoader();
      String mainClassPath = loader.getResource("com/runtimeverification/rvmonitor/c/rvc/Main.class").toString();
      String cmgPath = null;
      if (mainClassPath.endsWith(".jar!/com/runtimeverification/rvmonitor/c/rvc/Main.class") && mainClassPath.startsWith("jar:")) {
        cmgPath = mainClassPath.substring("jar:file:".length(), mainClassPath.length()
                   - "rvmonitor.jar!/com/runtimeverification/rvmonitor/c/rvc/Main.class".length());
        cmgPath = polishPath(cmgPath);
        isJarFile = true;
        jarFilePath = mainClassPath.substring("jar:file:".length(), mainClassPath.length()
                       - "!/com/runtimeverification/rvmonitor/c/rvc/Main.class".length());
        jarFilePath = polishPath(jarFilePath);
       }
       else {
         cmgPath = Main.class.getResource(".").getFile();
       }
   return cmgPath;
 }

 // Parses rv-monitor C input and produces a RVCParser object
 // from which we can grab important data
 static private RVCParser parseInput(String fileName) 
   throws FileNotFoundException
 {
      FileInputStream fio = new FileInputStream(new File(fileName));
      Scanner sc = new Scanner(fio);
      StringBuilder buf = new StringBuilder();
      while(sc.hasNextLine()) buf.append(sc.nextLine());
      RVCParser ret = RVCParser.parse(buf.toString());
      return ret; 
 }

  // Generates the proper name for the logic plugin directory
  static public String readLogicPluginDir(String basePath) {
    String logicPluginDirPath = System.getenv("LOGICPLUGINPATH");
    if (logicPluginDirPath == null || logicPluginDirPath.length() == 0) {
      if (basePath.charAt(basePath.length() - 1) == '/')
        logicPluginDirPath = basePath + "plugins";
      else
        logicPluginDirPath = basePath + "/plugins";
    }

    return logicPluginDirPath;
  }

  // Polishing directory path for windows
  static public String polishPath(String path) {
    if (path.indexOf("%20") > 0)
      path = path.replaceAll("%20", " ");
    return path;
  }

  //sends spec to logic repository
  static public LogicRepositoryData sendToLogicRepository(String logicPluginDirPath)
    throws LogicException {
    LogicRepositoryType cmgXMLIn = new LogicRepositoryType();
    PropertyType logicProperty = new PropertyType();
   
    // Get Logic Name and Client Name
    String logicName = rvcParser.getFormalism();
    if (logicName == null || logicName.length() == 0) {
      throw new LogicException("no logic names");
    }
 
    cmgXMLIn.setSpecName(rvcParser.getSpecName());

    logicProperty.setFormula(rvcParser.getFormula());
    logicProperty.setLogic(logicName);

    cmgXMLIn.setClient("CMonGen");
    StringBuilder events = new StringBuilder();
    for(String event : rvcParser.getEvents().keySet()){
      events.append(event);
      events.append(" ");
    }
    cmgXMLIn.setEvents(events.toString().trim());

    StringBuilder categories = new StringBuilder();
    for(String category : rvcParser.getHandlers().keySet()){
      categories.append(category);
      categories.append(" ");
    }
    cmgXMLIn.setCategories(categories.toString().trim());

    PropertyType prop = new PropertyType();
    prop.setLogic(rvcParser.getFormalism());
    prop.setFormula(rvcParser.getFormula());

    cmgXMLIn.setProperty(prop);

    LogicRepositoryData cmgDataIn = new LogicRepositoryData(cmgXMLIn);

    // Find a logic plugin and apply it
    ByteArrayOutputStream logicPluginResultStream 
      = LogicPluginFactory.process(logicPluginDirPath, logicName, cmgDataIn);

    // Error check
    if (logicPluginResultStream == null || logicPluginResultStream.size() == 0) {
      throw new LogicException("Unknown Error from Logic Plugins");
    }
    return new LogicRepositoryData(logicPluginResultStream);
  }

  //Output code for the monitor.  Creates a C and a H file
  static private void outputCode(LogicRepositoryData cmgDataOut)
    throws LogicException, RVMException, FileNotFoundException {
      LogicRepositoryType logicOutputXML = cmgDataOut.getXML();

      LogicPluginShellResult sr;
      //TODO: make this reflective instead of using a switch over type
      if(logicOutputXML.getProperty().getLogic().toLowerCase().compareTo("fsm") == 0){
        CFSM cfsm = new CFSM(rvcParser, parametric);
        sr = cfsm.process(logicOutputXML, logicOutputXML.getEvents());
      }
      else if(logicOutputXML.getProperty().getLogic().toLowerCase().compareTo("cfg") == 0){
        CCFG ccfg = new CCFG(rvcParser, parametric);
        sr = ccfg.process(logicOutputXML, logicOutputXML.getEvents());
      }
      else {
        throw new LogicException("Only finite logics and CFG are currently supported");
      } 

      String rvcPrefix = (String) sr.properties.get("rvcPrefix");
      String specName = (String) sr.properties.get("specName");
      String constSpecName = (String) sr.properties.get("constSpecName");

      String cFile = rvcPrefix + specName + "Monitor.c";
      String hFile = rvcPrefix + specName + "Monitor.h";
      String hDef = rvcPrefix + constSpecName + "MONITOR_H";

      FileOutputStream cfos = new FileOutputStream(new File(cFile));
      PrintStream cos = new PrintStream(cfos);
      FileOutputStream hfos = new FileOutputStream(new File(hFile));
      PrintStream hos = new PrintStream(hfos);
      
      hos.println("#ifndef " + hDef);
      hos.println("#define " + hDef);

      if(parametric){

      }
      else {
        cos.println(rvcParser.getIncludes());
        cos.println(sr.properties.get("state declaration"));
        cos.println(rvcParser.getDeclarations());
        cos.println(sr.properties.get("categories"));
        cos.println(sr.properties.get("reset"));
        cos.println(sr.properties.get("monitoring body"));
        cos.println(sr.properties.get("event functions"));
        hos.println(sr.properties.get("header declarations"));
      }
      hos.println("#endif");
      System.out.println(hFile + " and " + cFile + " have been generated.");
  }

}
