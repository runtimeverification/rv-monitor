.PHONY: all test clean
all: hood_loose.bc

__RVC_HoodLoose_Monitor.bc: hood_loose.rvm
	../../../../bin/rv-monitor -llvm -p hood_loose.rvm

hood_loose_1.bc: test_1/hood_loose.c
	clang -c -emit-llvm -o hood_loose_1.bc test_1/hood_loose.c

hood_loose.bc: hood_loose_1.bc __RVC_HoodLoose_Monitor.bc
	llvm-link __RVC_HoodLoose_Monitor.bc hood_loose_1.bc -o hood_loose.bc

test: hood_loose.bc
	-@lli hood_loose.bc

clean:
	rm -f __RVC_HoodLoose_Monitor.bc
	rm -f hood_loose_1.bc
	rm -f hood_loose.bc