package com.runtimeverification.rvmonitor.logicpluginshells.dl

import java.beans.EventHandler

import edu.cmu.cs.ls.keymaerax.Configuration
import edu.cmu.cs.ls.keymaerax.bellerophon._
import edu.cmu.cs.ls.keymaerax.btactics.ModelPlex.createMonitorSpecificationConjecture
import edu.cmu.cs.ls.keymaerax.btactics.{ModelPlex, SimplifierV3, _}
import edu.cmu.cs.ls.keymaerax.btactics.TactixLibrary._
import edu.cmu.cs.ls.keymaerax.btactics.SimplifierV3._
import edu.cmu.cs.ls.keymaerax.core.Variable
import edu.cmu.cs.ls.keymaerax.parser.StringConverter._
import edu.cmu.cs.ls.keymaerax.parser._
import edu.cmu.cs.ls.keymaerax.core._
import edu.cmu.cs.ls.keymaerax.codegen._
import edu.cmu.cs.ls.keymaerax.launcher.{DefaultConfiguration, KeYmaeraX}
import CGenerator._
import java.io.{File, PrintWriter}

import com.runtimeverification.rvmonitor.core.ast.Event

import scala.Array
import scala.io.Source
/**
  * Connects to ModelPlex for dL monitoring C code synthesis
  */
object ModelPlexConnector {

  def genCControllerMonitor(modelStr: String): String = {
    val tempModelFile = File.createTempFile("model", ".key")
    val tempConditionFile = File.createTempFile("condition", ".kyx")

    val writer = new PrintWriter(tempModelFile)
    writer.write(modelStr)
    writer.close()

    val variables: List[Variable] = getStateVarsFromModel(modelStr)
    KeYmaeraX.main(Array( "-tool"     , "mathematica"
                        , "-modelplex", tempModelFile.getAbsolutePath
                        , "-vars"     , variables.mkString(",")
                        , "-monitor"  , "ctrl"
                        , "-out"      , tempConditionFile.getAbsolutePath))

    val tempCFile = File.createTempFile("gen_monitor", ".c")
    KeYmaeraX.main(Array( "-interval"
                        , "-tool"        , "mathematica"
                        , "-codegen"     , tempConditionFile.getAbsolutePath
                        , "-vars", variables.mkString(",")
                        , "-quantitative", "ctrl"
                        , "-out"         , tempCFile.getAbsolutePath))

    tempModelFile.delete()
    tempConditionFile.delete()

    val cMonitorSource = Source.fromFile(tempCFile)
    val cMonitorStr = cMonitorSource.mkString
    cMonitorSource.close()
    tempCFile.delete()
    sanitizeC(cMonitorStr)
  }


  // PV (Program Variables) and End are KeYmaeraX keywords for declaring variables
  abstract class State
  case class BeforePVSeen()    extends State
  case class PVSeenEndUnSeen() extends State
  case class EndAfterPVSeen()  extends State

  def getVariablesFromModelKyx(modelKyxLines:List[String]):List[String] = {
    val variableLineRegex = "R\\s+([A-Za-z0-9]+)\\s*\\.".r

    def getVariableFromLine(str: String):Option[List[String]] =
      str match {
        case variableLineRegex(v) => Some(List(v))
        case _ => None
      }

    modelKyxLines.foldLeft(Tuple2(BeforePVSeen().asInstanceOf[State], List[String]()))({
      (stateTup, line) =>
        val trimmedLine = line.trim()
        stateTup._1 match {
          case BeforePVSeen()    => if(trimmedLine.startsWith("ProgramVariables")) {
                                      Tuple2(PVSeenEndUnSeen(), stateTup._2)
                                    } else {
                                      stateTup
                                    }
          case PVSeenEndUnSeen() => if(trimmedLine.startsWith("R")) {
                                      Tuple2(PVSeenEndUnSeen()
                                              ,getVariableFromLine(trimmedLine).getOrElse(List()) ::: stateTup._2)
                                    } else {
                                      stateTup
                                    }
          case EndAfterPVSeen()  => stateTup
        }
    })._2
  }

  def stripIncludes(cString: String): String = {
    cString.split("\n").filter(!_.contains("#include")).mkString("\n")
  }

  def stripComments(str: String) = {
    str.split("\n").filterNot(line => (line.trim().startsWith("/")) | line.trim().startsWith("*")).mkString("\n")
  }

  /**
    * The LLVM instrumentation fails due to this function.
    * Since it's unused during monitoring, it's removed from the synthesized monitoring code.
    */

  def sanitizeC(unsanitizedInput:String):String = {
    stripComments(stripIncludes(unsanitizedInput))
      .split("(?<=\\n)")
      .foldLeft(Tuple2(false, ""))( { (tup2, line) => if(tup2._1 && line.contains("}")) {
                                                        Tuple2(false, tup2._2)
                                                     } else if(!tup2._1 && line.contains("Run controller")) {
                                                        Tuple2(true, tup2._2)
                                                     } else if(!tup2._1) {
                                                        Tuple2(false, tup2._2 + line)
                                                     } else {
                                                        tup2
                                                     }
                                    })._2
  }

  def getStateVarsFromModel(keymaeraxModel:String):List[BaseVariable] = {
    getVariablesFromModelKyx(keymaeraxModel.split("\n")
                      .toList.filter(!_.equals("")))
                      .map(_.asVariable.asInstanceOf[BaseVariable])
    }

  // Todo: Create an implicit object with better formatting
  def formattedSetter(structName:String, structType:String, fieldName: String, fieldType:String): String =
  s"""void updateState_$fieldName($structType * $structName, $fieldType param) {
         $structName->$fieldName = param;
}"""


}
