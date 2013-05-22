@echo off

set SRC_ROOT=%~dp0..

if "%1"=="-c" (
  "%SRC_ROOT%\RVC\bin\RVC.bat" %2
) 

set PLUGINS=%SRC_ROOT%\lib\plugins
set CP=%SRC_ROOT%\lib\rvmonitor.jar;%SRC_ROOT%\lib\logicrepository.jar;%SRC_ROOT%\lib\scala-library.jar;%SRC_ROOT%\lib\mysql-connector-java-3.0.9-stable-bin.jar
for /f %%a IN ('dir /b /s "%PLUGINS%\*.jar"') do call :concat %%a

java -cp "%CP%"  rvmonitor.Main %*
goto :eof

:concat
set CP=%CP%;%1
goto :eof


java -cp "%SRC_ROOT%\lib\rvmonitor.jar;%SRC_ROOT%\lib\logicrepository.jar;%SRC_ROOT%\lib\plugins\*.jar;%SRC_ROOT%\lib\mysql-connector-java-3.0.9-stable-bin.jar" rvmonitor.Main %*



