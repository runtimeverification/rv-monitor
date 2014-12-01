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

Before trying any examples, please ensure you’ve added the RV-Monitor/bin directory to 
your PATH and the RV-Monitor Runtime Library, as well as your current directory in 
your Java CLASSPATH. 

The RV-Monitor Runtime Library is provided in this package in the rv-monitor/lib directory.
A typical value of this is:

In Windows,
  rv-monitor\lib\rv-monitor-rt.jar

In Linux and Mac,
  rv-monitor/lib/rv-monitor-rt.jar

Add this to the left end of the CLASSPATH followed by `;.` (in Windows) or `:.`
(in Linux and Mac). The second part (`:.` i.e. *this* directory) is for ease
of use.

This ensures easy use of 1) rv-monitor to generate monitoring libraries, 2) javac to compile the 
libraries together with your instrumented program, and 3) java to run the code in all the examples.

Note: for setting the classpath to already existing classpath you may want to use 
%CLASSPATH% (in Windows) or $CLASSPATH (in Linux and Mac). Similarly for PATH.
e.g. CLASSPATH=$CLASSPATH:rv-monitor/lib/rv-monitor-rt.jar:.



To get started, here’s an example of monitoring via context free grammar.

In RV-Monitor/examples/java/CFG/HasNext, we call:
$ rv-monitor rvm/HasNext.rvm
$ javac rvm/HasNextRuntimeMonitor.java HasNext_1/HasNext_1.java
$ java HasNext_1.HasNext_1

For more information on running examples, please see the web documentation on 
running examples at https://runtimeverification.com/monitor/docs (also included
in text form in the docs/ subfolder of this install).
