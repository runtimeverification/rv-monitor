package cmongen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cmongen.cmongensyntax.*;

public class CMonGenData {

  private CMonGenType xmlData = null;
  private ByteArrayOutputStream outputStreamData = null;
  private ByteArrayInputStream inputStreamData = null;
  
  public CMonGenData(CMonGenType xmlData) throws LogicException{
    this.xmlData = xmlData;
  }
  
  public CMonGenData(ByteArrayOutputStream outputStreamData) throws LogicException{
    this.outputStreamData = outputStreamData;
  }

  public CMonGenData(ByteArrayInputStream inputStreamData) throws LogicException{
    this.inputStreamData = inputStreamData;
  }

  public CMonGenData(InputStream inputStreamData) throws LogicException{
    this.xmlData = transInputStreamToXML(inputStreamData);
  }
  
  public CMonGenType getXML() throws LogicException{
    if(xmlData != null)
      return xmlData;
    if(outputStreamData != null){
      xmlData = transOutputStreamToXML(outputStreamData);
      return xmlData;
    }
    if(inputStreamData != null){
      xmlData = transInputStreamToXML(inputStreamData);
      return xmlData;
    }
    throw new LogicException("Logic Repository Data Error");
  }
  
  public ByteArrayOutputStream getOutputStream() throws LogicException{
    if(outputStreamData != null){
      return outputStreamData;
    }
    if(xmlData != null){
      outputStreamData = transXMLToOutputStream(xmlData);
      return outputStreamData;
    }
    if(inputStreamData != null){
      xmlData = transInputStreamToXML(inputStreamData);
      outputStreamData = transXMLToOutputStream(xmlData);
      return outputStreamData;
    }
    throw new LogicException("Logic Repository Data Error");
  }
  
  public ByteArrayInputStream getInputStream() throws LogicException{
    if(inputStreamData != null){
      inputStreamData.reset();
      return inputStreamData;
    }
    if(xmlData != null){
      inputStreamData = transXMLToInputStream(xmlData);
      inputStreamData.reset();
      return inputStreamData;
    }
    if(outputStreamData != null){
      xmlData = transOutputStreamToXML(outputStreamData);
      inputStreamData = transXMLToInputStream(xmlData);
      inputStreamData.reset();
      return inputStreamData;
    }
    throw new LogicException("Logic Repository Data Error");
  }
  
  public void updateXML(){
    outputStreamData = null;
    inputStreamData = null;
  }
  
  public ByteArrayOutputStream transXMLToOutputStream(CMonGenType xmlData) throws LogicException {
    JAXBContext logicData;
    ByteArrayOutputStream outputStream;
    try {
      logicData = JAXBContext.newInstance("cmongen.cmongensyntax");
      Marshaller marshaller = logicData.createMarshaller();
      cmongen.cmongensyntax.ObjectFactory factory = new cmongen.cmongensyntax.ObjectFactory();
      JAXBElement<CMonGenType> logicData2 = factory.createMop(xmlData);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.marshal(logicData2, os);
      outputStream = os;
    } catch (Exception e) {
      throw new LogicException(e.getMessage());
    }

    return outputStream;
  }

  public ByteArrayInputStream transXMLToInputStream(CMonGenType xmlData) throws LogicException {
    ByteArrayOutputStream os = transXMLToOutputStream(xmlData); 
    return new ByteArrayInputStream(os.toByteArray());
  }

  public CMonGenType transOutputStreamToXML(ByteArrayOutputStream outputStream) throws LogicException {
    CMonGenType xmlData;
    ByteArrayInputStream parserInput = new ByteArrayInputStream(outputStream.toByteArray());

    return transInputStreamToXML(parserInput);
  }

  public CMonGenType transInputStreamToXML(InputStream inputStream) throws LogicException {
    CMonGenType xmlData;
    InputStream parserInput = inputStream;

    JAXBContext logicRequest;
    try {
      logicRequest = JAXBContext.newInstance("cmongen.cmongensyntax");
      Unmarshaller unmarshaller = logicRequest.createUnmarshaller();
      xmlData = ((JAXBElement<CMonGenType>) unmarshaller.unmarshal(parserInput)).getValue();
    } catch (Exception e) {
      throw new LogicException(e.getMessage());
    }

    return xmlData;
  }

  
}
