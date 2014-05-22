all: test1 test2

__RVC_SixSteps_Monitor.c: six_steps.rvm
	../../../../bin/rv-monitor -c six_steps.rvm

test1: __RVC_SixSteps_Monitor.c
	gcc test_1/six_steps.c __RVC_SixSteps_Monitor.c -o test1
	
test2: __RVC_SixSteps_Monitor.c
	gcc test_2/six_steps.c __RVC_SixSteps_Monitor.c -o test2
	
clean:
	rm -f test1 test2 __RVC_* 

test: all
	-@./test1
	-@./test2
