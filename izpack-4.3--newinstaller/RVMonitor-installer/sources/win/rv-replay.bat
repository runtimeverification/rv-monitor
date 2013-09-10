@echo off
echo.
SET batchpath=%~dp0
SET batchpath=%batchpath%rv-replayer.jar
java -Xmx1g -cp %CD%\tmp\replay;%batchpath% edu.uiuc.run.Main %*