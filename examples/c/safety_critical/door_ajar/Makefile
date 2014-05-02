all: test1 test2 test1_aspc test2_aspc

__RVC_DoorAjar_Monitor.c: door_ajar.rvm
	../../../../bin/rv-monitor -c door_ajar.rvm

lib_aspc/door_ajar.c: lib_aspc/door_ajar.mc lib_aspc/door_ajar_acc.acc
	acc lib_aspc/door_ajar.mc lib_aspc/door_ajar_acc.acc

lib_aspc/door_ajar_acc.c: lib_aspc/door_ajar.mc lib_aspc/door_ajar_acc.acc
	acc lib_aspc/door_ajar.mc lib_aspc/door_ajar_acc.acc

test1: __RVC_DoorAjar_Monitor.c
	gcc test_1/door_ajar.c __RVC_DoorAjar_Monitor.c -o test1
	
test2: __RVC_DoorAjar_Monitor.c
	gcc test_2/door_ajar.c __RVC_DoorAjar_Monitor.c -o test2


test1_aspc: __RVC_DoorAjar_Monitor.c lib_aspc/door_ajar.c lib_aspc/door_ajar_acc.c
	gcc test_1_aspc/door_ajar.c lib_aspc/door_ajar.c lib_aspc/door_ajar_acc.c __RVC_DoorAjar_Monitor.c -o test1_aspc

test2_aspc: __RVC_DoorAjar_Monitor.c lib_aspc/door_ajar.c lib_aspc/door_ajar_acc.c
	gcc test_2_aspc/door_ajar.c lib_aspc/door_ajar.c lib_aspc/door_ajar_acc.c __RVC_DoorAjar_Monitor.c -o test2_aspc
	
clean:
	rm -f test1 test2 test1_aspc test2_aspc __RVC_* lib_aspc/*.c

test: all
	@echo "Test 1" 
	@echo "Test 1"  >>/dev/stderr
	-@./test1
	@echo "Test 2" 
	@echo "Test 2"  >>/dev/stderr
	-@./test2
	@echo "Test 1 ASPC" 
	@echo "Test 1 ASPC"  >>/dev/stderr
	-@./test1_aspc
	@echo "Test 2 ASPC" 
	@echo "Test 2 ASPC"  >>/dev/stderr
	-@./test2_aspc



