@echo off

set SRC_ROOT=%~dp0..

java -cp "%SRC_ROOT%\lib\testsuite.jar;%SRC_ROOT%\lib\rvmonitor.jar;%SRC_ROOT%\lib\logicrepository.jar;%SRC_ROOT%\lib\rt.jar" com.runtimeverification.rvmonitor.java.testsuite.Main -m "%SRC_ROOT%\build" -local "%SRC_ROOT%/examples/java" %*

