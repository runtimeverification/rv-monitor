@echo off

set SRC_ROOT=%~dp0..

set PLUGINS=%SRC_ROOT%\lib\plugins
set CP=%SRC_ROOT%\lib\RVC.jar;%SRC_ROOT%\lib\scala-library.jar
for /f %%a IN ('dir /b /s "%PLUGINS%\*.jar"') do call :concat %%a

java -cp "%CP%" RVC.Main %*
goto :eof

:concat
set CP=%CP%;%1
goto :eof
