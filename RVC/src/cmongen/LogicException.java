package cmongen;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cmongen.cmongensyntax.*;
import java.util.*;

public class LogicException extends Exception {
  private static final long serialVersionUID = -1L;

  public LogicException(Exception e) {
    super("C Monitor Generation  Exception:" + e.getMessage());
  }

  public LogicException(String str) {
    super(str);
  }

  public String toString() {
    return getMessage();
  }
}
