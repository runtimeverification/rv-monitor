all: test

.PHONY: check

aspect.bc: 
	clang-6.0 -flto -fvisibility=hidden aspect.c -o aspect.bc -c -emit-llvm

test0.bc: 
	clang-6.0 -flto -fvisibility=hidden test.c -o test0.bc -c -emit-llvm

test_instr.bc: test0.bc aspect.map
	opt-6.0 -load ../../build/lib/LLVMAOP.so -aop < test0.bc >test_instr.bc

test.bc : aspect.bc test_instr.bc
	llvm-link-6.0 aspect.bc test_instr.bc | opt -internalize -internalize-public-api-list=main -Os -o test.bc

test: aspect.bc test_instr.bc
	clang-6.0 -flto -Xlinker -emain aspect.bc test_instr.bc -o test

check: test
	./test || true

clean:
	rm -f *.bc test
