all: test1 test2 test3 test4

__RVC_SeatBelt_Monitor.c: seatbelt.rvm
	../../../../bin/rv-monitor -c seatbelt.rvm

test1: __RVC_SeatBelt_Monitor.c
	gcc test_1/seatbelt.c __RVC_SeatBelt_Monitor.c -o test1
	
test2: __RVC_SeatBelt_Monitor.c
	gcc test_2/seatbelt.c __RVC_SeatBelt_Monitor.c -o test2
	
test3: __RVC_SeatBelt_Monitor.c
	gcc test_3/seatbelt.c __RVC_SeatBelt_Monitor.c -o test3
	
test4: __RVC_SeatBelt_Monitor.c
	gcc test_4/seatbelt.c __RVC_SeatBelt_Monitor.c -o test4
	
clean:
	rm -f test1 test2 test3 test4 __RVC_* 

test: all
	-@./test1
	-@./test2
