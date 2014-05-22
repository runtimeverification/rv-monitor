all: test_1 test_2

__RVC_IsNull_Monitor.c: IsNull.rvm
	../../../../bin/rv-monitor -c IsNull.rvm

test_1: __RVC_IsNull_Monitor.c IsNull_1/IsNull_1.c
	gcc IsNull_1/IsNull_1.c __RVC_IsNull_Monitor.c -o test_1

test_2: __RVC_IsNull_Monitor.c IsNull_2/IsNull_2.c
	gcc IsNull_2/IsNull_2.c __RVC_IsNull_Monitor.c -o test_2

clean:
	rm -f test_1 test_2 __RVC_*


test: all
	-@./test_1
	-@./test_2



