
                          RV-Monitor README

==1. Overview==

RV-Monitor is a commercial product derived from earlier work on
Monitoring-Oriented Programming, abbreviated MOP.  It is a software development
and analysis framework aiming at reducing the gap between formal specification
and implementation by allowing them together to form a system. In MOP, and by
extension RV-Monitor, runtime monitoring is supported and encouraged as a
fundamental principle for building reliable software: monitors are
automatically synthesized from specified properties and integrated into the
original system to check its dynamic behaviors during execution. When a
specification is violated or validated at runtime, user-defined actions will be
triggered, which can be any code from information logging to runtime recovery.
One can understand RV-Monitor from at least three perspectives: as a discipline
allowing one to improve safety, reliability and dependability of a system by
monitoring its requirements against its implementation at runtime; as an
extension of programming languages with logics (one can add logical statements
anywhere in the program, referring to past or future states); and as a
lightweight formal method. 

==2. Usage==

=2.1 Linux and Mac (POSIX)

The 'rvmonitor' script has the following usage:

Usage) rvmonitor [-v] [-d <target directory>] <specification file or dir>

-v option is verbose mode -d option is used to specify the target directory
where the resulting java code will be saved. Specification files must have
The .mop file extension.

Example) rvmonitor -d examples/FSM/ examples/FSM/HasNext.mop

For more options, type 'rvmonitor' or 'rvmonitor -h'

=2.2 Windows

The script, 'rvmonitor.bat' has the following usage.

Usage) rvmonitor.bat [-v] [-d <target directory>] <specification file or dir>

The -v option is used to specify verbose mode. The -d option is used to specify
the target directory where the resulting aspectj code will be saved.
Specification files must have The .mop file extension.

Example) rvmonitor.bat -d examples\FSM examples\FSM\HasNext.mop

For more options, type 'rvmonitor.bat' or 'rvmonitor.bat -h'

==3. Additional Features of the JavaMOP Distribution==

=3.1 Executing a Monitored Program

When you execute a monitored program, you need to include the RV-Monitor Runtime 
Library in your class path. The RV-Monitor Runtime Library is provided in this package 
in the rvmonitor2.3/lib directory. A typical value of this is:

In Windows,
  rv-monitor\lib\rvmonitorrt.jar

In Linux and Mac,
  rv-monitor/lib/rvmonitorrt.jar

Add this to the left end of the "CLASSPATH" followed by ";" (in Windows) or ":"
(in Linux and Mac). For more information, refer to Section 5 from the following
URL http://java.sun.com/j2se/1.4.2/install-windows.html
