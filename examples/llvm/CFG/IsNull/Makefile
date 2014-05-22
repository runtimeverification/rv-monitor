.PHONY: all test clean
all: IsNullTest1.bc IsNullTest2.bc

__RVC_IsNull_Monitor.bc: IsNull.rvm
	../../../../bin/rv-monitor -llvm IsNull.rvm

IsNull_1.bc: IsNull_1/IsNull_1.c
	clang IsNull_1/IsNull_1.c -o IsNull_1.bc -c -emit-llvm

IsNullTest1.bc: IsNull_1.bc __RVC_IsNull_Monitor.bc
	llvm-link -o IsNullTest1.bc IsNull_1.bc __RVC_IsNull_Monitor.bc

IsNull_2.bc: IsNull_2/IsNull_2.c
	clang IsNull_2/IsNull_2.c -o IsNull_2.bc -c -emit-llvm

IsNullTest2.bc: IsNull_2.bc __RVC_IsNull_Monitor.bc
	llvm-link -o IsNullTest2.bc IsNull_2.bc __RVC_IsNull_Monitor.bc

test: IsNullTest1.bc IsNullTest2.bc
	-@lli IsNullTest1.bc
	-@lli IsNullTest2.bc

clean:
	rm -f __RVC_IsNull_Monitor.bc
	rm -f IsNull_1.bc IsNullTest1.bc
	rm -f IsNull_2.bc IsNullTest2.bc