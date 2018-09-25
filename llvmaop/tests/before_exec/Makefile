all: test

.PHONY: check

aspect.bc: 
	clang -flto -fvisibility=hidden aspect.c -o aspect.bc -c -emit-llvm

test0.bc: 
	clang -flto -fvisibility=hidden test.c -o test0.bc -c -emit-llvm

test_instr.bc: test0.bc aspect.map
	opt -load ../../build/lib/LLVMAOP.so -aop < test0.bc >test_instr.bc

test.bc : aspect.bc test_instr.bc
	llvm-link aspect.bc test_instr.bc | opt -internalize -internalize-public-api-list=main -Os -o test.bc

test: aspect.bc test_instr.bc
	clang -flto -Xlinker -emain aspect.bc test_instr.bc -o test

check: test
	./test || true

clean:
	rm -f *.bc test
