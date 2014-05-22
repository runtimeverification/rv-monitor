all: test_1_aspc

__RVC_Basic_Car_Monitor.c: basic_car.rvm
	../../../../bin/rv-monitor -c -p basic_car.rvm

basic_car_1_aspc/basic_car.c: basic_car_1_aspc/basic_car.mc basic_car_1_aspc/basic_car_acc.acc
	acc basic_car_1_aspc/basic_car.mc basic_car_1_aspc/basic_car_acc.acc

test_1_aspc: __RVC_Basic_Car_Monitor.c basic_car_1_aspc/basic_car.c basic_car_1_aspc/basic_car_acc.c
	gcc basic_car_1_aspc/basic_car.c basic_car_1_aspc/basic_car_acc.c __RVC_Basic_Car_Monitor.c -o test_1_aspc
	
clean:
	rm -f test_1_aspc __RVC_* basic_car_1_aspc/*.c

test: all
	-@./test_1_aspc	
