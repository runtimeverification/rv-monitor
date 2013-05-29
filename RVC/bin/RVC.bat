@echo off

set SRC_ROOT=%~dp0..

set RV_ROOT=%SRC_ROOT%\..

set PLUGINS=%RV_ROOT%\lib\plugins
set CP=%SRC_ROOT%\lib\rvc.jar;%RV_ROOT%\lib\logicrepository.jar;%SRC_ROOT%\lib\scala-library.jar
for /f %%a IN ('dir /b /s "%PLUGINS%\*.jar"') do call :concat %%a

set LOGICPLUGINPATH=%PLUGINS%

java -cp "%CP%" com.runtimeverification.rvmonitor.c.rvc.Main %*
goto :eof

:concat
set CP=%CP%;%1
goto :eof
