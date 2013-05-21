@echo off

set SRC_ROOT=%~dp0..

if "%1"=="-c" (
  "%SRC_ROOT%\RVC\bin\RVC.bat" %2
) else (
  echo "ala bala"
)

java -cp "%SRC_ROOT%\lib\rvmonitor.jar;%SRC_ROOT%\lib\logicrepository.jar;%SRC_ROOT%\lib\plugins\*.jar;%SRC_ROOT%\lib\mysql-connector-java-3.0.9-stable-bin.jar" rvmonitor.Main %*



