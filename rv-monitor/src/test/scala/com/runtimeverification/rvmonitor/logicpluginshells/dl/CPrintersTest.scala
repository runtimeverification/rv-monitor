package com.runtimeverification.rvmonitor.logicpluginshells.dl

import com.runtimeverification.rvmonitor.logicpluginshells.dl.CPrinters.{CFunction, Param}
import org.scalatest.FunSuite

class CPrintersTest extends FunSuite{

  test("Parameter Printer") {
    val params = Param("bool", "a") :: Param("AType *", "ptr") :: Nil
    assert(params.mkString(", ") == "bool a, AType * ptr")
  }

  test("Function Printer") {
    val params = Param("bool", "a") :: Param("AType *", "ptr") :: Nil
    val cFunc = CFunction(Some("static"), "void", "test", params, "printf(test);")
    val expected = "static void test(" + params.mkString(", ") + ")\n{\tprintf(test);\n}"
    assert(cFunc.toString == expected)
  }

}
