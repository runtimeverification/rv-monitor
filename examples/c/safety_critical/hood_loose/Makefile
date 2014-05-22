all: test1

__RVC_HoodLoose_Monitor.c: hood_loose.rvm
	../../../../bin/rv-monitor -c hood_loose.rvm

test1: __RVC_HoodLoose_Monitor.c
	gcc test_1/hood_loose.c __RVC_HoodLoose_Monitor.c -o test1
	
clean:
	rm -f test1 __RVC_* 

test: all
	-@./test1



