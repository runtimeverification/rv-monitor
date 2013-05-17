/*
 * entry class for RVC - based somewhat off the MOP logic repository code,
 * but bare bones and easier to maintain
 * 
 * author: Patrick Meredith
 *
 * previous author : Dongyun Jin 
 */

package RVC;

import RVC.plugins.*;
import RVC.shells.*;
import RVC.shells.fsm.*;

import RVC.RVCsyntax.*;

import RVC.parser.RVCParser;


import java.io.File;
import java.io.ByteArrayOutputStream;

import java.util.Scanner;

public class Main {
  public static boolean isJarFile = false;
  public static String jarFilePath = null;
  public static String basePath = null;

  static public void main(String[] args) {
     
    try{
      ClassLoader loader = Main.class.getClassLoader();
      String mainClassPath = loader.getResource("RVC/Main.class").toString();
      String cmgPath = null;
      if (mainClassPath.endsWith(".jar!/RVC/Main.class") && mainClassPath.startsWith("jar:")) {
        cmgPath = mainClassPath.substring("jar:file:".length(), mainClassPath.length()
                   - "RVC.jar!/RVC/Main.class".length());
        cmgPath = polishPath(cmgPath);
        isJarFile = true;
        jarFilePath = mainClassPath.substring("jar:file:".length(), mainClassPath.length()
                       - "!/RVC/Main.class".length());
        jarFilePath = polishPath(jarFilePath);
       }
       else {
         cmgPath = Main.class.getResource(".").getFile();
       }
   
      basePath = cmgPath;
  
      String logicPluginDirPath = polishPath(readLogicPluginDir(basePath));
      File dirLogicPlugin = new File(logicPluginDirPath);
      if(!dirLogicPlugin.exists()){
        throw new LogicException(
      "Unrecoverable error: please place plugins in the default plugins directory:plugins");
      }

      // Parse Input
      Scanner sc = new Scanner(System.in);
      StringBuilder buf = new StringBuilder();
      while(sc.hasNextLine()) buf.append(sc.nextLine());
      RVCParser rvcParser = RVCParser.parse(buf.toString()); 
      
      // Get Logic Name and Client Name
      String logicName = rvcParser.getFormalism();
      if (logicName == null || logicName.length() == 0) {
        throw new LogicException("no logic names");
      }

      CMonGenType cmgXMLIn = new CMonGenType();
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


      CMonGenData cmgDataIn = new CMonGenData(cmgXMLIn);

      // Find a logic plugin and apply it
      ByteArrayOutputStream logicPluginResultStream 
        = LogicPluginFactory.process(logicPluginDirPath, logicName, cmgDataIn);
      
      CMonGenData cmgDataOut = new CMonGenData(logicPluginResultStream);

      // Error check
      if (logicPluginResultStream == null || logicPluginResultStream.size() == 0) {
        throw new LogicException("Unknown Error from Logic Plugins");
      }
      
      // Outputting
       
      CMonGenType logicOutputXML = cmgDataOut.getXML();
      if(logicOutputXML.getProperty().getLogic().toLowerCase().compareTo("fsm") != 0){
        throw new Exception("Only finite logics are currently supported");
      } 
      CFSM cfsm = new CFSM(); 
      ShellResult sr = cfsm.process(logicOutputXML, logicOutputXML.getEvents()); 
      System.out.println(rvcParser.getIncludes());
      System.out.println(sr.properties.get("state declaration"));
      System.out.println(rvcParser.getDeclarations());
      System.out.println(sr.properties.get("monitored events"));
      System.out.println(sr.properties.get("categories"));
      System.out.println(sr.properties.get("reset"));
      System.out.println(sr.properties.get("monitoring body"));
      String rvcPrefix = (String) sr.properties.get("rvcPrefix");
      String specName = (String) sr.properties.get("specName");
      String constSpecName = (String) sr.properties.get("constSpecName");
      for(String eventName : rvcParser.getEvents().keySet()){
        System.out.println("void");
        System.out.println(rvcPrefix + specName + eventName + rvcParser.getParameters().get(eventName));
        System.out.println("{");
        System.out.println(rvcParser.getEvents().get(eventName) + "\n"); 
        System.out.println(rvcPrefix + specName + "monitor(" + rvcPrefix + constSpecName + eventName.toUpperCase() + ");"); 
        for(String category : rvcParser.getHandlers().keySet()){
          System.out.println("if(" + rvcPrefix + specName + category + ")\n{");
          System.out.println(rvcParser.getHandlers().get(category));
          System.out.println("}");
        }
        System.out.println("}\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e);
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
