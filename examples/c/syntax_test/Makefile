all:

clean:
	rm -f __RVC_* 

test: all
	../../../bin/rv-monitor -c ere_test.rvm
	../../../bin/rv-monitor -c ltl_test.rvm
	../../../bin/rv-monitor -c ptltl_test.rvm


