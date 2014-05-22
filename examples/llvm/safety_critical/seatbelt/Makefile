.PHONY: all test clean

all: test_1.bc test_2.bc

__RVC_SeatBelt_Monitor.bc: seatbelt.rvm
	../../../../bin/rv-monitor -llvm seatbelt.rvm

seatbelt_1.bc: test_1/seatbelt.c
	clang test_1/seatbelt.c -c -emit-llvm -o seatbelt_1.bc

test_1.bc: __RVC_SeatBelt_Monitor.bc seatbelt_1.bc
	llvm-link __RVC_SeatBelt_Monitor.bc seatbelt_1.bc -o test_1.bc

seatbelt_2.bc: test_2/seatbelt.c
	clang test_2/seatbelt.c -c -emit-llvm -o seatbelt_2.bc

test_2.bc: __RVC_SeatBelt_Monitor.bc seatbelt_2.bc
	llvm-link __RVC_SeatBelt_Monitor.bc seatbelt_2.bc -o test_2.bc

test: test_1.bc test_2.bc
	-@lli test_1.bc
	-@lli test_2.bc

clean:
	rm -f test_1.bc test_2.bc
	rm -f __RVC_SeatBelt_Monitor.bc
	rm -f seatbelt_1.bc
	rm -f seatbelt_2.bc