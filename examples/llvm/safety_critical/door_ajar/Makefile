.PHONY: all test clean

all: test_1.bc test_2.bc

__RVC_DoorAjar_Monitor.bc: door_ajar.rvm
	../../../../bin/rv-monitor -llvm door_ajar.rvm

door_ajar_1.bc: test_1/door_ajar.c
	clang test_1/door_ajar.c -c -emit-llvm -o door_ajar_1.bc

test_1.bc: __RVC_DoorAjar_Monitor.bc door_ajar_1.bc
	llvm-link __RVC_DoorAjar_Monitor.bc door_ajar_1.bc -o test_1.bc

door_ajar_2.bc: test_2/door_ajar.c
	clang test_2/door_ajar.c -c -emit-llvm -o door_ajar_2.bc

test_2.bc: __RVC_DoorAjar_Monitor.bc door_ajar_2.bc
	llvm-link __RVC_DoorAjar_Monitor.bc door_ajar_2.bc -o test_2.bc

test: test_1.bc test_2.bc
	-@lli test_1.bc
	-@lli test_2.bc

clean:
	rm -f test_1.bc test_2.bc
	rm -f __RVC_DoorAjar_Monitor.bc
	rm -f door_ajar_1.bc
	rm -f door_ajar_2.bc