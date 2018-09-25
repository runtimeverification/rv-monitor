all: test

.PHONY: check

#CC=clang -Xclang -load -Xclang ../../build/lib/LLVMAOP.so
CC=rvmcc
CFLAGS=-flto=thin -O3

aspect.o:
	clang $(CFLAGS) -c aspect.c

test_instr.o: aspect.map
	$(CC) $(CFLAGS) -c test.c -o test_instr.o

test_instr.o.ll: test_instr.o
	llvm-dis test_instr.o

test: aspect.o test_instr.o
	clang -flto aspect.o test_instr.o -o test

check: test
	./test || true

clean:
	rm -f *.bc *.o test_instr.o.ll test
