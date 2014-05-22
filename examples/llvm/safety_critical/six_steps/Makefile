.PHONY: all test clean

all: test_1.bc test_2.bc

__RVC_SixSteps_Monitor.bc: six_steps.rvm
	../../../../bin/rv-monitor -llvm six_steps.rvm

six_steps_1.bc: test_1/six_steps.c
	clang test_1/six_steps.c -c -emit-llvm -o six_steps_1.bc

test_1.bc: __RVC_SixSteps_Monitor.bc six_steps_1.bc
	llvm-link __RVC_SixSteps_Monitor.bc six_steps_1.bc -o test_1.bc

six_steps_2.bc: test_2/six_steps.c
	clang test_2/six_steps.c -c -emit-llvm -o six_steps_2.bc

test_2.bc: __RVC_SixSteps_Monitor.bc six_steps_2.bc
	llvm-link __RVC_SixSteps_Monitor.bc six_steps_2.bc -o test_2.bc

test: test_1.bc test_2.bc
	-@lli test_1.bc
	-@lli test_2.bc

clean:
	rm -f test_1.bc test_2.bc
	rm -f __RVC_SixSteps_Monitor.bc
	rm -f six_steps_1.bc
	rm -f six_steps_2.bc