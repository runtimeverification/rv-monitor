package com.runtimeverification.rvmonitor.logicpluginshells.dl

import org.scalatest.FunSuite
import edu.cmu.cs.ls.keymaerax.parser.{KeYmaeraXArchiveParser, KeYmaeraXArchivePrinter}
import com.runtimeverification.rvmonitor.logicpluginshells.dl.ModelPlexConnector._
import edu.cmu.cs.ls.keymaerax.core.BaseVariable
import edu.cmu.cs.ls.keymaerax.launcher.KeYmaeraX
import java.io.File

import com.runtimeverification.rvmonitor.logicpluginshells.dl.CPrinters.Param

import scala.io.Source
import scala.Array

class ModelPlexConnectorTest extends FunSuite {

  test("extract WaterTank  variables") {
    val modelStr = Source.fromResource("WaterTank.key").mkString
    val variables = getStateVarsFromModel(modelStr)
    assert(variables.mkString(",") == "c,l,f")
  }
  test("remove includes") {
    val badC = Source.fromResource("with_includes.c").mkString
    val expectedC = Source.fromResource("without_includes.c").mkString
    assert(expectedC.trim() == stripIncludes(badC))
  }

}
