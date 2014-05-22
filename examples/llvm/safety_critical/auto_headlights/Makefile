.PHONY: all test clean

all: test_1.bc test_2.bc

__RVC_AutoHeadlights_Monitor.bc: auto_headlights.rvm
	../../../../bin/rv-monitor -llvm auto_headlights.rvm

auto_headlights_1.bc: test_1/auto_headlights.c
	clang test_1/auto_headlights.c -c -emit-llvm -o auto_headlights_1.bc

test_1.bc: __RVC_AutoHeadlights_Monitor.bc auto_headlights_1.bc
	llvm-link __RVC_AutoHeadlights_Monitor.bc auto_headlights_1.bc -o test_1.bc

auto_headlights_2.bc: test_2/auto_headlights.c
	clang test_2/auto_headlights.c -c -emit-llvm -o auto_headlights_2.bc

test_2.bc: __RVC_AutoHeadlights_Monitor.bc auto_headlights_2.bc
	llvm-link __RVC_AutoHeadlights_Monitor.bc auto_headlights_2.bc -o test_2.bc

test: test_1.bc test_2.bc
	-@lli test_1.bc
	-@lli test_2.bc

clean:
	rm -f test_1.bc test_2.bc
	rm -f __RVC_AutoHeadlights_Monitor.bc
	rm -f auto_headlights_1.bc
	rm -f auto_headlights_2.bc