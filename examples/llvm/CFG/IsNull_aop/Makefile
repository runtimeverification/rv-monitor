all: test_1.bc test_2.bc

__RVC_IsNull_Monitor.bc: is_null.rvm 
	../../../../bin/rv-monitor -llvm is_null.rvm

is_null_aspect.bc: 
	clang is_null_aspect.c -o is_null_aspect.bc -c -emit-llvm

test_1/test_1_instr.bc: test_1/test_1.bc aspect.map
	opt -load LLVMAOP.so -aop < test_1/test_1.bc >test_1/test_1_instr.bc

test_2/test_2_instr.bc: test_2/test_2.bc aspect.map
	opt -load LLVMAOP.so -aop < test_2/test_2.bc >test_2/test_2_instr.bc

test_1/test_1.bc: test_1/test_1.c
	clang test_1/test_1.c -o test_1/test_1.bc -c -emit-llvm

test_2/test_2.bc: test_2/test_2.c
	clang test_2/test_2.c -o test_2/test_2.bc -c -emit-llvm

test_1.bc: __RVC_IsNull_Monitor.bc is_null_aspect.bc test_1/test_1_instr.bc
	llvm-link  __RVC_IsNull_Monitor.bc is_null_aspect.bc test_1/test_1_instr.bc -o test_1.bc
	
test_2.bc: __RVC_IsNull_Monitor.bc is_null_aspect.bc test_2/test_2_instr.bc
	llvm-link  __RVC_IsNull_Monitor.bc is_null_aspect.bc test_2/test_2_instr.bc -o test_2.bc
	
clean:
	rm -f *.bc test_1/*.bc test_2/*.bc
