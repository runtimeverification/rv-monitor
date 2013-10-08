@echo off
SET /a count=2
SET classname=%1
SET /a index=1

:loopstart
call rv-replay %classname% %index%
SET /a index=%index%+1
if %index% LEQ %count% ( goto loopstart )