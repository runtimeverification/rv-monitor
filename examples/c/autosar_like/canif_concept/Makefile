all: canif_test_1 canif_test_2 canif_test_3

__RVC_Can_If_Monitor.c: can_if.rvm
	../../../../bin/rv-monitor -c can_if.rvm

canif_test_1: __RVC_Can_If_Monitor.c canif_1/test_1.c
	gcc canif_1/test_1.c __RVC_Can_If_Monitor.c CanIf.c -o canif_test_1

canif_test_2: __RVC_Can_If_Monitor.c canif_2/test_2.c
	gcc canif_2/test_2.c __RVC_Can_If_Monitor.c CanIf.c -o canif_test_2

canif_test_3: __RVC_Can_If_Monitor.c canif_3/test_3.c
	gcc canif_3/test_3.c __RVC_Can_If_Monitor.c CanIf.c -o canif_test_3

clean:
	rm -f canif_test_1 canif_test_2 canif_test_3 __RVC_*


test: all
	-@./canif_test_1
	-@./canif_test_2
	-@./canif_test_3



