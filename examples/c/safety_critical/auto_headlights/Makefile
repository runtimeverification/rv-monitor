all: test1 test2 test1_aspc test2_aspc

__RVC_AutoHeadlights_Monitor.c: auto_headlights.rvm
	../../../../bin/rv-monitor -c auto_headlights.rvm

test_1_aspc/auto_headlights.c: test_1_aspc/auto_headlights.mc test_1_aspc/auto_headlights_acc.acc
	acc test_1_aspc/auto_headlights.mc test_1_aspc/auto_headlights_acc.acc

test_2_aspc/auto_headlights.c: test_2_aspc/auto_headlights.mc test_2_aspc/auto_headlights_acc.acc
	acc test_2_aspc/auto_headlights.mc test_2_aspc/auto_headlights_acc.acc

test1: __RVC_AutoHeadlights_Monitor.c
	gcc test_1/auto_headlights.c __RVC_AutoHeadlights_Monitor.c -o test1
	
test2: __RVC_AutoHeadlights_Monitor.c
	gcc test_2/auto_headlights.c __RVC_AutoHeadlights_Monitor.c -o test2


test1_aspc: __RVC_AutoHeadlights_Monitor.c test_1_aspc/auto_headlights.c test_1_aspc/auto_headlights_acc.c
	gcc test_1_aspc/auto_headlights.c test_1_aspc/auto_headlights_acc.c __RVC_AutoHeadlights_Monitor.c -o test1_aspc

test2_aspc: __RVC_AutoHeadlights_Monitor.c test_2_aspc/auto_headlights.c test_2_aspc/auto_headlights_acc.c
	gcc test_2_aspc/auto_headlights.c test_2_aspc/auto_headlights_acc.c __RVC_AutoHeadlights_Monitor.c -o test2_aspc
	
clean:
	rm -f test1 test2 test1_aspc test2_aspc __RVC_* test_*_aspc/auto_headlights*.c

test: all
	-@./test1
	-@./test2
	-@./test1_aspc
	-@./test2_aspc



