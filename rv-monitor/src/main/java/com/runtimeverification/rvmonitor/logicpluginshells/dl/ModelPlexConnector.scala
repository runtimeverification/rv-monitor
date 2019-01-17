package com.runtimeverification.rvmonitor.logicpluginshells.dl

import edu.cmu.cs.ls.keymaerax.parser.KeYmaeraXArchiveParser.{Declaration, ParsedArchiveEntry}
import edu.cmu.cs.ls.keymaerax.parser._
import edu.cmu.cs.ls.keymaerax.Configuration
import edu.cmu.cs.ls.keymaerax.bellerophon.parser.{BelleParser, BellePrettyPrinter}
import edu.cmu.cs.ls.keymaerax.bellerophon._
import edu.cmu.cs.ls.keymaerax.btactics.ExpressionTraversal.{ExpressionTraversalFunction, StopTraversal}
import edu.cmu.cs.ls.keymaerax.btactics.ModelPlex.createMonitorSpecificationConjecture
import edu.cmu.cs.ls.keymaerax.btactics.{ModelPlex, SimplifierV3, _}
import edu.cmu.cs.ls.keymaerax.btactics.TactixLibrary._
import edu.cmu.cs.ls.keymaerax.btactics.SimplifierV3._
import edu.cmu.cs.ls.keymaerax.core.{BaseVariable, _}
import edu.cmu.cs.ls.keymaerax.parser.KeYmaeraXArchiveParser.{Declaration, ParsedArchiveEntry}
import edu.cmu.cs.ls.keymaerax.parser.StringConverter._
import edu.cmu.cs.ls.keymaerax.parser._
import edu.cmu.cs.ls.keymaerax.pt.ProvableSig
import edu.cmu.cs.ls.keymaerax.core._
import edu.cmu.cs.ls.keymaerax.codegen._

import scala.io.Source
import edu.cmu.cs.ls.keymaerax.launcher.DefaultConfiguration

/**
  * Connects to ModelPlex for dL monitoring C code synthesis
  */
object ModelPlexConnector {
  def init():Unit = {
    BelleInterpreter.setInterpreter(ExhaustiveSequentialInterpreter())
    PrettyPrinter.setPrinter(KeYmaeraXPrettyPrinter.pp)

    val generator = new ConfigurableGenerator[Formula]()
    KeYmaeraXParser.setAnnotationListener((p: Program, inv: Formula) =>
      generator.products += (p->(generator.products.getOrElse(p, Nil) :+ inv)))
    TactixLibrary.invGenerator = generator
  }

  def getMathematica():MathematicaToolProvider = {
    val mathLinkTcp = System.getProperty(Configuration.Keys.MATH_LINK_TCPIP, "true") // JVM parameter -DMATH_LINK_TCPIP=[true,false]
    Configuration.set(Configuration.Keys.MATH_LINK_TCPIP, mathLinkTcp, saveToFile = false)
    Configuration.set(Configuration.Keys.QE_TOOL, "mathematica", saveToFile=false)
    val provider = new MathematicaToolProvider(DefaultConfiguration.currentMathematicaConfig)
    ToolProvider.setProvider(provider)
    return provider
  }
  def shutdown():Unit = {
    ToolProvider.shutdown()
    ToolProvider.setProvider(new NoneToolProvider())
    TactixLibrary.invGenerator = FixedGenerator(Nil)
  }

  init()
  val in = getClass.getResourceAsStream("/waterTank.key")
  val model = KeYmaeraXArchiveParser.parseAsProblemOrFormula(io.Source.fromInputStream(in).mkString)
  val Imply(_, Box(prg, _)) = model
  val stateVars = ("f"::"l"::"c"::Nil).map(_.asVariable.asInstanceOf[BaseVariable])
  val (modelplexInput, assumptions) = createMonitorSpecificationConjecture(model, stateVars:_*)
  val simplifier = SimplifierV3.simpTac(taxs=composeIndex(groundEqualityIndex,defaultTaxs))
  val provider = getMathematica()
  val tactic = ModelPlex.modelMonitorByChase(1) & ModelPlex.optimizationOneWithSearch(Some(provider.tool), assumptions)(1) & simplifier(1)
  val result = proveBy(modelplexInput, tactic)
  println(result.subgoals);
  val inputs = CGenerator.getInputs(prg)
  CPrettyPrinter.printer = new CExpressionPlainPrettyPrinter()
  val code = (new CMonitorGenerator())(result.subgoals.head.succ.head, stateVars.toSet, inputs, "Monitor")
  print(code)
  shutdown()

}
