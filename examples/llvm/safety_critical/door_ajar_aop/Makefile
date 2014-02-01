all: test_1.bc test_2.bc

__RVC_DoorAjar_Monitor.bc: door_ajar.rvm door_ajar_dispatch.c
	../../../../bin/rv-monitor -llvm door_ajar.rvm

door_ajar_dispatch.bc: 
	clang door_ajar_dispatch.c -o door_ajar_dispatch.bc -c -emit-llvm

lib/door_ajar.bc: lib/door_ajar.c lib/door_ajar.h
	clang lib/door_ajar.c -o lib/door_ajar.bc -c -emit-llvm

lib/door_ajar_instr.bc: lib/door_ajar.bc aspect.map
	opt -load LLVMAOP.so -aop < lib/door_ajar.bc >lib/door_ajar_instr.bc

test_2/test_2.bc: test_2/test_2.c
	clang test_2/test_2.c -o test_2/test_2.bc -c -emit-llvm

test_2.bc: __RVC_DoorAjar_Monitor.bc door_ajar_dispatch.bc lib/door_ajar_instr.bc test_2/test_2.bc
	llvm-link  __RVC_DoorAjar_Monitor.bc door_ajar_dispatch.bc lib/door_ajar_instr.bc test_2/test_2.bc -o test_2.bc
	
test_1/test_1.bc: test_1/test_1.c
	clang test_1/test_1.c -o test_1/test_1.bc -c -emit-llvm

test_1.bc: __RVC_DoorAjar_Monitor.bc door_ajar_dispatch.bc lib/door_ajar_instr.bc test_1/test_1.bc
	llvm-link  __RVC_DoorAjar_Monitor.bc door_ajar_dispatch.bc lib/door_ajar_instr.bc test_1/test_1.bc -o test_1.bc
	
clean:
	rm -f *.bc lib/*.bc test_1/*.bc test_2/*.bc
