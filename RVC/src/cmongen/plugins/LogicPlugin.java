package cmongen.plugins;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cmongen.*;
import cmongen.cmongensyntax.*;

import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.*;
import java.lang.reflect.*;

public abstract class LogicPlugin {

  abstract public CMonGenType process(CMonGenType logicInput) throws LogicException;

  public ByteArrayOutputStream process(
      ByteArrayInputStream logicPluginInputStream) throws LogicException {

    // Parse Input
    CMonGenData logicData = new CMonGenData(logicPluginInputStream);

    CMonGenType logicOutputXML = process(logicData.getXML());

    ByteArrayOutputStream logicOutput = new CMonGenData(logicOutputXML).getOutputStream();
    return logicOutput;
  }

  static protected LogicPlugin plugin = null;
  
  public static void main(String[] args) {

    try {
      // Parse Input
      CMonGenData logicInputData = new CMonGenData(System.in);

      // use plugin main function
      if(plugin == null){
        throw new LogicException("Each plugin should initiate plugin field.");
      }
      CMonGenType logicOutputXML = plugin.process(logicInputData.getXML());

      if (logicOutputXML == null) {
        throw new LogicException("no output from the plugin.");
      }

      ByteArrayOutputStream logicOutput = new CMonGenData(logicOutputXML).getOutputStream();
      System.out.println(new ByteArrayInputStream(logicOutput.toByteArray()));
    } catch (LogicException e) {
      System.out.println(e);
    }

  }

}
