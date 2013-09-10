@echo off
echo.
echo detecting races in %1
echo.
SET batchpath=%~dp0
SET batchpath=%batchpath%rv-predictor.jar
java -Xmx1g -cp %batchpath% NewRVPredict %1
