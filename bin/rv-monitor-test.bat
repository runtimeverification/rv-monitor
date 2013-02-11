@echo off

set SRC_ROOT=%~dp0..

java -cp "%SRC_ROOT%/lib/rvmonitortestsuite.jar;%SRC_ROOT%/lib/rvmonitor.jar;%SRC_ROOT%/lib/logicrepository.jar:%SRC_ROOT%/lib/*.jar" rvmonitortestsuite.Main -local "%SRC_ROOT%/examples" %*

