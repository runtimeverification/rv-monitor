package com.runtimeverification.rvmonitor.logicpluginshells.dl

import edu.cmu.cs.ls.keymaerax.Configuration
import edu.cmu.cs.ls.keymaerax.bellerophon.{BelleInterpreter, ExhaustiveSequentialInterpreter}
import edu.cmu.cs.ls.keymaerax.btactics.ModelPlex.createMonitorSpecificationConjecture
import edu.cmu.cs.ls.keymaerax.btactics.SimplifierV3.{composeIndex, defaultTaxs, groundEqualityIndex}
import edu.cmu.cs.ls.keymaerax.btactics.TactixLibrary.proveBy
import edu.cmu.cs.ls.keymaerax.btactics._
import edu.cmu.cs.ls.keymaerax.codegen.{CExpressionPlainPrettyPrinter, CGenerator, CMonitorGenerator, CPrettyPrinter}
import edu.cmu.cs.ls.keymaerax.core.{BaseVariable, Box, Formula, Imply, PrettyPrinter, Program}
import edu.cmu.cs.ls.keymaerax.launcher.DefaultConfiguration
import edu.cmu.cs.ls.keymaerax.parser.{KeYmaeraXArchiveParser, KeYmaeraXParser, KeYmaeraXPrettyPrinter}

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

  def synthesizeCFromFile(fileName:String):String = {
    init()
    val model = KeYmaeraXArchiveParser.parseAsProblemOrFormula(io.Source.fromFile(fileName).mkString)

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
    shutdown()
    return code._2
  }

}
