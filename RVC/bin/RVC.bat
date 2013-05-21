@echo off

set SRC_ROOT=%~dp0..

set PLUGINS=%SRC_ROOT%\lib\plugins

java -cp "%SRC_ROOT%\lib\RVC.jar;%SRC_ROOT%\lib\scala-library.jar;%PLUGINS%\ERE.jar;%PLUGINS%\FSM.jar;%PLUGINS\LTL.jar;%PLUGINS\PTLTL.jar" RVC.Main %*



