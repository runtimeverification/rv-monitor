all: test1 test2

__RVC_SeatBelt_Monitor.c: seatbelt.rvm
	../../../../bin/rv-monitor -c seatbelt.rvm

test1: __RVC_SeatBelt_Monitor.c
	gcc test_1/seatbelt.c __RVC_SeatBelt_Monitor.c -o test1
	
test2: __RVC_SeatBelt_Monitor.c
	gcc test_2/seatbelt.c __RVC_SeatBelt_Monitor.c -o test2
	
clean:
	rm -f test1 test2 __RVC_* 

test: all
	@echo "Test 1" 
	@echo "Test 1"  >>/dev/stderr
	-@./test1
	@echo "Test 2" 
	@echo "Test 2"  >>/dev/stderr
	-@./test2
