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
import com.runtimeverification.rvmonitor.logicrepository.LogicRepositoryData;
import com.runtimeverification.rvmonitor.logicrepository.LogicException;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.PropertyType;
import com.runtimeverification.rvmonitor.logicrepository.plugins.*;
import com.runtimeverification.rvmonitor.logicpluginshells.*;

import com.runtimeverification.rvmonitor.c.rvc.parser.RVCParser;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import java.util.Scanner;

public class Main {
  public static boolean isJarFile = false;
  public static String jarFilePath = null;
  public static String basePath = null;

  static public void main(String[] args) {
     
    try{
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
   
      basePath = cmgPath;
    
      if(args.length < 1){
        System.err.println("usage is:  RVC <spec_file>\n  Please specify a spec file");
        System.exit(1);
      }


      String logicPluginDirPath = polishPath(readLogicPluginDir(basePath));
      File dirLogicPlugin = new File(logicPluginDirPath);
      if(!dirLogicPlugin.exists()){
        throw new LogicException(
      "Unrecoverable error: please place plugins in the default plugins directory:plugins");
      }

      // Parse Input
      FileInputStream fio = new FileInputStream(new File(args[0]));
      Scanner sc = new Scanner(fio);
      StringBuilder buf = new StringBuilder();
      while(sc.hasNextLine()) buf.append(sc.nextLine());
      RVCParser rvcParser = RVCParser.parse(buf.toString()); 
      
      // Get Logic Name and Client Name
      String logicName = rvcParser.getFormalism();
      if (logicName == null || logicName.length() == 0) {
        throw new LogicException("no logic names");
      }

      LogicRepositoryType cmgXMLIn = new LogicRepositoryType();
      PropertyType logicProperty = new PropertyType();
      
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
      
      LogicRepositoryData cmgDataOut = new LogicRepositoryData(logicPluginResultStream);

      // Error check
      if (logicPluginResultStream == null || logicPluginResultStream.size() == 0) {
        throw new LogicException("Unknown Error from Logic Plugins");
      }
      
      // Outputting
       
      LogicRepositoryType logicOutputXML = cmgDataOut.getXML();
      if(logicOutputXML.getProperty().getLogic().toLowerCase().compareTo("fsm") != 0){
        throw new Exception("Only finite logics are currently supported");
      } 
      CFSM cfsm = new CFSM();
      LogicPluginShellResult sr = cfsm.process(logicOutputXML, logicOutputXML.getEvents());

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

      cos.println(rvcParser.getIncludes());
      cos.println(sr.properties.get("state declaration"));
      cos.println(rvcParser.getDeclarations());
      cos.println(sr.properties.get("monitored events"));
      cos.println(sr.properties.get("categories"));
      cos.println(sr.properties.get("reset"));
      hos.println("void");
      String resetName = rvcPrefix + specName + "reset";
      hos.println(resetName + "(void);"); 
      cos.println(sr.properties.get("monitoring body"));
      for(String eventName : rvcParser.getEvents().keySet()){
        hos.println("void");
        cos.println("void");
        String funcDecl = rvcPrefix + specName + eventName + rvcParser.getParameters().get(eventName);
        hos.println(funcDecl + ";");
        cos.println(funcDecl);
        cos.println("{");
        cos.println(rvcParser.getEvents().get(eventName) + "\n"); 
        cos.println(rvcPrefix + specName + "monitor(" + rvcPrefix + constSpecName + eventName.toUpperCase() + ");"); 
        for(String category : rvcParser.getHandlers().keySet()){
          cos.println("if(" + rvcPrefix + specName + category + ")\n{");
          cos.println(rvcParser.getHandlers().get(category).replaceAll("__RESET", resetName + "()"));
          cos.println("}");
        }
        cos.println("}\n");
      }
      hos.println("#endif");
      System.out.println(hFile + " and " + cFile + " have been generated.");
    } catch (Exception e) {
      e.printStackTrace();
      //System.out.println(e);
    }
  }

 // Read Logic Plugin Directory
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


}
