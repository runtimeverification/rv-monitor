all: original

original:
	make -f Makefile.original

instrument: original door_ajar.rvm
	../../../../bin/rv-monitor -llvm door_ajar.rvm
	make -f Makefile.Instrument

uninstrument: .instrumented
	make -f Makefile.Instrument uninstrument
	make -f Makefile.original

clean:
	make -f Makefile.Instrument clean
	make -f Makefile.original clean
	rm -f aspect.map *.bc


.PHONY: original instrument uninstrument
