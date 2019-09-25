package com.runtimeverification.rvmonitor.logicpluginshells.dl
import scala.collection.JavaConverters._

object CPrinters {

  case class Param(pType: String, pName: String) {
    override def toString: String = s"""$pType $pName"""
  }

  case class CFunction(modifier: Option[String], returnType: String, name:String, params: Seq[Param], content: String) {

    val paramsList = params.mkString(", ")

    override def toString: String = {
      val template =
      s"""$returnType $name($paramsList)""" + "\n" +
      "{\t" + s"""$content""" + "\n}"

      modifier match {
        case None => template
        case Some(mod) => mod + " " + template
      }
    }
  }

  def generateDlStateUpdate(param: Param): String = {
    val Param(_, field) = param
    val template = s"""
         |    if(monitorState.prevStateExists()) {
         |        monitorState.currState.$field = static_cast<long double>($field);
         |        monitorState.currStateMap["$field"] = true;
         |    } else {
         |        monitorState.prevState.$field = static_cast<long double>($field);
         |        monitorState.prevStateMap["$field"] = true;
         |    }
         |""".stripMargin

    CFunction(None, "void", "update_" + param.pName, param :: Nil, template).toString
  }

  def generateDlStateUpdatesFromModel(model:String, javaParamList: java.util.List[Param]): String = {
    val paramsList = javaParamList.asScala
    val logicalVariables: Set[String] = ModelPlexConnector.getStateVarsFromModel(model).map(_.toString()).toSet
    val variables: List[Param] = paramsList.filter(param => logicalVariables.contains(param.pName)).toList
    variables.map(generateDlStateUpdate).mkString("\n")
  }

}
