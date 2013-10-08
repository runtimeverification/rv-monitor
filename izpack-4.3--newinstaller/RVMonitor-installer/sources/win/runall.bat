@echo off
call rv-instrument %1
call rv-record %1
call rv-predict %1
call rv-replay %1 1
