Detailed Documentation
----------------------

For detailed information on RV-Monitor and its use, please see
http://runtimeverification.com/monitor/docs (also included in text form in the docs/
subfolder of this install).


Command Line Basics
----------------------

To call rv-monitor with ease, please add RV-Monitor/bin to your PATH!

The rv-monitor script has the following usage:
	
	 rv-monitor [-v] [-d <target directory>] <specification file or dir>

    	-v option is verbose mode 
    	-d option is used to specify the target directory
        	where the resulting java code will be saved. It is optional.
    
For more options, type rv-monitor or rv-monitor -h


Running Examples
----------------------

Before trying any examples, please ensure you’ve added the RV-Monitor/bin directory to your PATH 
and RV-Monitor/lib/rv-monitor-rt.jar:. to the beginning of your CLASSPATH (with ; as a separator
on Windows, making the CLASSPATH  RV-Monitor/lib/rv-monitor-rt.jar;. on Windows).

This ensures easy use of 1) rv-monitor to generate monitoring libraries, 2) javac to compile 
the libraries together with your instrumented program, and 3) java to run the code in all the examples.

To get started, here’s an example of monitoring via context free grammar.

In RV-Monitor/examples/java/CFG/HasNext, we call:
$ rv-monitor rvm/HasNext.rvm
$ javac rvm/HasNextRuntimeMonitor.java HasNext_1/HasNext_1.java
$ java HasNext_1.HasNext_1

For more information on running examples, please see the web documentation at 
https://runtimeverification.com/monitor/1.3/docs/runningexamples also included in text form in the docs/
subfolder of this install.
