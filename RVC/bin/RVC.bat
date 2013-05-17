@echo off

set SRC_ROOT=%~dp0..

java -cp "%SRC_ROOT%\lib\RVC.jar;%SRC_ROOT%\lib\scala-library.jar;%SRC_ROOT%\lib\plugins\*.jar" rvmonitor.Main %*



