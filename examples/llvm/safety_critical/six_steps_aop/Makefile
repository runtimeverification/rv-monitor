all: test_1.bc test_2.bc

__RVC_SixSteps_Monitor.bc: six_steps.rvm
	../../../../bin/rv-monitor -llvm six_steps.rvm

lib/six_steps.bc: lib/six_steps.c lib/six_steps.h
	clang lib/six_steps.c -o lib/six_steps.bc -c -emit-llvm

lib/six_steps_instr.bc: lib/six_steps.bc aspect.map
	opt -load LLVMAOP.so -aop < lib/six_steps.bc >lib/six_steps_instr.bc

test_2/test_2.bc: test_2/test_2.c
	clang test_2/test_2.c -o test_2/test_2.bc -c -emit-llvm

test_2.bc: __RVC_SixSteps_Monitor.bc lib/six_steps_instr.bc test_2/test_2.bc
	llvm-link  __RVC_SixSteps_Monitor.bc lib/six_steps_instr.bc test_2/test_2.bc -o test_2.bc
	
test_1/test_1.bc: test_1/test_1.c
	clang test_1/test_1.c -o test_1/test_1.bc -c -emit-llvm

test_1.bc: __RVC_SixSteps_Monitor.bc lib/six_steps_instr.bc test_1/test_1.bc
	llvm-link  __RVC_SixSteps_Monitor.bc lib/six_steps_instr.bc test_1/test_1.bc -o test_1.bc
	
clean:
	rm -f *.bc lib/*.bc test_1/*.bc test_2/*.bc
