package RVC.plugins;

import java.io.*;
import java.util.jar.*;
import java.util.*;
import java.net.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import RVC.*;
import RVC.RVCsyntax.*;

public class LogicPluginFactory {

  static int numtry = 0;

  static public LogicPlugin findLogicPlugin(String logicPluginDirPath, String logicName) {
    String pluginName = logicName.toLowerCase() + "plugin";
    ArrayList<Class<?>> logicPlugins = null;
    try {
      /* this should return only subclasses of LogicPlugins */
      logicPlugins = getClassesFromPath(logicPluginDirPath);
      if (logicPlugins != null) {
        for (Class c : logicPlugins) {
          if (c.getSimpleName().toLowerCase().compareTo(pluginName) == 0) {
            LogicPlugin plugin = (LogicPlugin) (c.newInstance());
            return plugin;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

  static public LogicPlugin findLogicPluginFromJar(String jarPath, String logicName) {
    String pluginName = logicName.toLowerCase() + "plugin";
    ArrayList<Class<?>> logicPlugins = null;
    try {
      /* this should return only subclasses of LogicPlugins */
      logicPlugins = getClassesFromJar(jarPath);

      if (logicPlugins != null) {
        for (Class c : logicPlugins) {
          if (c.getSimpleName().toLowerCase().compareTo(pluginName) == 0) {
            LogicPlugin plugin = (LogicPlugin) (c.newInstance());
            return plugin;
          }
        }
      }
    } catch (Exception e) {
    }
    return null;
  }

  static private ArrayList<Class<?>> getClassesFromJar(String jarPath) throws LogicException {
    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

    try {
      JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath));
      JarEntry jarEntry;

      while (true) {
        jarEntry = jarFile.getNextJarEntry();
        if (jarEntry == null) {
          break;
        }
        if (jarEntry.getName().endsWith(".class")) {
          String className = jarEntry.getName().replaceAll("/", "\\.");
          className = className.substring(0, className.length() - ".class".length());
          Class<?> clazz = Class.forName(className);

          if (!clazz.isInterface()) {
            Class<?> superClass = clazz.getSuperclass();
            while (superClass != null) {
              if (superClass.getName() == "RVC.plugins.LogicPlugin") {
                classes.add(clazz);
                break;
              }
              superClass = superClass.getSuperclass();
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return classes;
  }
  
  static private ArrayList<Class<?>> getClassesFromPath(String packagePath) throws LogicException {
    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
    String path = packagePath;

    // WINDOWS HACK
    if (path.indexOf("%20") > 0)
      path = path.replaceAll("%20", " ");

    if (!(path.indexOf("!") > 0) && !(path.indexOf(".jar") > 0)) {
      try {
        classes.addAll(getFromDirectory(new File(path), "RVC.plugins"));
      } catch (Exception e) {
        throw new LogicException("cannot load logic plugins");
      }
    }
    return classes;
  }

  static private ArrayList<Class<?>> getFromDirectory(File directory, String packageName) throws Exception {
    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

    if (directory.exists()) {
      for (File file : directory.listFiles()) {
        if (file.getName().endsWith(".class")) {
          String name = packageName + '.' + stripFilenameExtension(file.getName());
          Class<?> clazz = null;
          try {
            clazz = Class.forName(name);
          } catch (Error e) {
            continue;
          }

          if (!clazz.isInterface()) {
            Class superClass = clazz.getSuperclass();
            while (superClass != null) {
              if (superClass.getName() == "RVC.plugins.LogicPlugin") {
                classes.add(clazz);
                break;
              }
              superClass = superClass.getSuperclass();
            }
          }
        } else if (file.list() != null) {
          classes.addAll(getFromDirectory(file, packageName + "." + stripFilenameExtension(file.getName())));
        }
      }
    }
    return classes;
  }

  static private String stripFilenameExtension(String path) {
    if (path == null) {
      return null;
    }
    int sepIndex = path.lastIndexOf(".");
    return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
  }

  static public ByteArrayOutputStream executeProgram(String[] cmdarray, String path, ByteArrayInputStream input)
      throws LogicException {
    Process child;
    String output = "";
    try {
      child = Runtime.getRuntime().exec(cmdarray, null, new File(path));
      OutputStream stdin = child.getOutputStream();

      StreamGobbler errorGobbler = new StreamGobbler(child.getErrorStream());
      StreamGobbler outputGobbler = new StreamGobbler(child.getInputStream());

      outputGobbler.start();
      errorGobbler.start();

      byte[] b = new byte[input.available()];
      input.read(b);

      stdin.write(b);
      stdin.flush();
      stdin.close();

      child.waitFor();
      outputGobbler.join();
      errorGobbler.join();

      output = outputGobbler.text + errorGobbler.text;

      ByteArrayOutputStream logicOutput = new ByteArrayOutputStream();
      logicOutput.write(output.getBytes());

      return logicOutput;
    } catch (Exception e) {
      if (cmdarray.length > 0)
        throw new LogicException("Cannot execute the logic plugin: " + cmdarray[0]);
      else
        throw new LogicException("Cannot execute the logic plugin: ");
    }
  }

  static public String[] getSuffixes() {
    String[] suffixes;
    String os = System.getProperty("os.name");
    if (os.toLowerCase().contains("windows")) {
      String[] suffixes_windows = { ".bat", ".exe", ".pl" };
      suffixes = suffixes_windows;
    } else {
      String[] suffxies_unix = { "", ".sh", ".pl" };
      suffixes = suffxies_unix;
    }

    return suffixes;
  }

  static public ByteArrayOutputStream process(String logicPluginDirPath, String logicName,
      CMonGenData cMonGenData) throws LogicException {
    ByteArrayOutputStream ret = null;

    ++LogicPluginFactory.numtry;

    // 1. LogicPlugin Class
    LogicPlugin plugin = findLogicPlugin(logicPluginDirPath, logicName);

    if (plugin != null)
      ret = plugin.process(cMonGenData.getInputStream());

    // 2. LogicPlugin Class from Jar file
    if (ret == null) {
      boolean logicJarExists = false;
      File logicPluginDir = new File(logicPluginDirPath);
      String logicJarPath = null;
      if (logicPluginDir.exists()) {
        for (File file : logicPluginDir.listFiles()) {
          if (file.getName().toLowerCase().compareTo(logicName.toLowerCase() + ".jar") == 0) {
            if (file.exists() && !file.isDirectory()) {
              logicJarExists = true;
              logicJarPath = logicPluginDirPath + "/" + file.getName();
            }
          }
        }
      }
      plugin = findLogicPluginFromJar(logicJarPath, logicName);

      if (plugin != null)
        ret = plugin.process(cMonGenData.getInputStream());
    }
    
    // Used to allow arbitrary programs as plugins, going to avoid that from now on

    // 3. Transitive Processing
    if (ret != null) {
      CMonGenData logicOutputData = new CMonGenData(ret);
      CMonGenType logicOutputXML = logicOutputData.getXML();

      boolean done = false;
      for (String msg : logicOutputXML.getMessage()) {
        if (msg.compareTo("done") == 0)
          done = true;
      }

      if (done) {
        return logicOutputData.getOutputStream();
      } else {
        if (logicOutputXML.getProperty() == null)
          throw new LogicException("Wrong Logic Plugin Result from " + logicName + " Logic Plugin");
        String logic = logicOutputXML.getProperty().getLogic();

        return process(logicPluginDirPath, logic, logicOutputData);
      }
    }

    throw new LogicException("Logic Plugin Not Found");
  }

}
