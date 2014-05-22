all: test1

__RVC_headlights_Monitor.c: headlights.rvm
	../../../../bin/rv-monitor -c -p headlights.rvm

test1: __RVC_headlights_Monitor.c
	gcc headlights_1/headlights_1.c __RVC_headlights_Monitor.c -o test1
	
clean:
	rm -f test1 __RVC_*

test: all
	-@./test1
