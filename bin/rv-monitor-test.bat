@echo off

set SRC_ROOT=%~dp0..

java -cp "%SRC_ROOT%/lib/testsuite.jar;%SRC_ROOT%/lib/rvmonitor.jar;%SRC_ROOT%/lib/logicrepository.jar:%SRC_ROOT%/lib/*.jar" com.runtimeverification.rvmonitor.java.testsuite.Main -local "%SRC_ROOT%/examples/java" %*

