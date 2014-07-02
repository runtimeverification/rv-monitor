@echo off

set SRC_ROOT=%~dp0..

set MAINCLASS=com.runtimeverification.rvmonitor.java.rvj.Main

if "%1"=="-c" (
  SHIFT
  set MAINCLASS=com.runtimeverification.rvmonitor.c.rvc.Main
) 

set PLUGINS=%SRC_ROOT%\lib\plugins
set CP=%SRC_ROOT%\lib\rvmonitorrt.jar;%SRC_ROOT%\lib\rvmonitor.jar;%SRC_ROOT%\lib\logicrepository.jar;%SRC_ROOT%\lib\scala-library.jar;%SRC_ROOT%\lib\mysql-connector-java-3.0.9-stable-bin.jar
for /f %%a IN ('dir /b /s "%PLUGINS%\*.jar"') do call :concat %%a

java -cp "%CP%"  %MAINCLASS% %1 %2 %3 %4 %5 %6 %7 %8 %9
goto :eof

:concat
set CP=%CP%;%1
goto :eof
