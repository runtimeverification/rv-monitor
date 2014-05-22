.PHONY: all test clean
all: headlights.bc

__RVC_headlights_Monitor.bc: headlights.rvm
	../../../../bin/rv-monitor -llvm -p headlights.rvm

headlights_1.bc: headlights_1/headlights_1.c
	clang -c -emit-llvm -o headlights_1.bc headlights_1/headlights_1.c

headlights.bc: headlights_1.bc __RVC_headlights_Monitor.bc
	llvm-link __RVC_headlights_Monitor.bc headlights_1.bc -o headlights.bc

test: headlights.bc
	-@lli headlights.bc

clean:
	rm -f __RVC_headlights_Monitor.bc
	rm -f headlights_1.bc
	rm -f headlights.bc