@echo off
echo.
echo instrumenting %1
echo.
SET batchpath=%~dp0
SET batchpath=%batchpath%rv-instrumentor.jar
SET current=%CD%
for %%F in ("%current%") do set DIR=%%~dpF
java -Xmx1g -cp %DIR%;%batchpath% rvpredict.instrumentation.Main %1
