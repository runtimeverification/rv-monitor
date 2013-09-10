@echo off
SET classname=%1
SET /a count=%2
SET /a index=1

SET current=%CD%
for %%F in ("%current%") do set DIR=%%~dpF

:loopstart
echo run %index%
java -cp %DIR% %classname%
SET /a index=%index%+1
if %index% LEQ %count% ( goto loopstart )