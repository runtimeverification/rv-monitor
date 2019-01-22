CC=gcc
all : test
.PHONY: all clean

SHELL:=/bin/bash
MONBASE:=__RVC_level_limit_Monitor
RV_MONITOR_DIR:=$(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))/../../../../
RV_MONITOR_BIN:=$(RV_MONITOR_DIR)/bin

aspect.map : $(RV_MONITOR_DIR)/.git level_limit.rvm waterTankSimulation.h
	$(RV_MONITOR_BIN)/rv-monitor -c level_limit.rvm
	clang-6.0 -g -emit-llvm -c $(MONBASE).c

test : aspect.map waterTankSimulation.c waterTankSimulation.h
	$(RV_MONITOR_BIN)/rvm-clang -g waterTankSimulation.c $(MONBASE).bc -o test

clean :
	rm -f test out aspect.map $(MONBASE).c $(MONBASE).h $(MONBASE).bc $(MONBASE).o

