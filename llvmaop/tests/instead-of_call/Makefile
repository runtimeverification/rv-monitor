all: test.bc

.PHONY: check

aspect.bc: 
	clang-6.0 aspect.c -o aspect.bc -c -emit-llvm

test0.bc: 
	clang-6.0 test.c -o test0.bc -c -emit-llvm

test_instr.bc: test0.bc aspect.map
	opt-6.0 -load ../../build/lib/LLVMAOP.so -aop < test0.bc >test_instr.bc

test.bc: aspect.bc test_instr.bc 
	llvm-link-6.0  aspect.bc test_instr.bc -o test.bc
	
check: test.bc
	lli-6.0 test.bc || true

clean:
	rm -f *.bc 
