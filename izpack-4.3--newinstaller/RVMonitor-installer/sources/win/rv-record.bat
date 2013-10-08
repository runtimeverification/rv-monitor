@echo off
echo.
echo recording %1
echo.
SET batchpath=%~dp0
SET batchpath=%batchpath%rv-recorder.jar
java -Xmx1g -cp %CD%\tmp\record;%batchpath% edu.uiuc.run.Main %1
