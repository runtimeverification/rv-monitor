# Overview

RV-Monitor is a software development and analysis framework aiming at reducing the 
gap between formal specification and implementation by allowing them together to form 
a system. With RV-Monitor, runtime monitoring is supported and encouraged as a
fundamental principle for building reliable software: monitors are  synthesized from 
specified properties and integrated into the original system to check its dynamic 
behaviors during execution. When a specification is violated or validated at runtime, 
user-defined actions will be triggered, which can be any code from information logging 
to runtime recovery. One can understand RV-Monitor from at least three perspectives: 
as a discipline allowing one to improve safety, reliability and dependability of a 
system by monitoring its requirements against its implementation at runtime; as an
extension of programming languages with logics (one can add logical statements
anywhere in the program, referring to past or future states); and as a
lightweight formal method.

At the heart of RV-Monitor is tool for generating monitoring code.  RV-Monitor for 
Java takes as input one or more specification files and generates Java classes that 
implement the monitoring functionality.  

Each specification defines a number of *events*, which represent abstractions
of certain points in programs, e.g., a call to the `hasNext()` method in Java,
or closing a file.  With these event abstractions in mind, a user can define
one or more properties over the events, taking the events as either atoms in
logical formulae or as symbols in formal language descriptions.  For example,
one may use these events as symbols in a regular expression or as atoms in a
linear temporal logic formula. In the generated Java class, each event becomes
a method that can be either called manually by a user or by using some means of
instrumentation, such as AspectJ.  

Each specification also has a number of handlers associated with each property
that run when their associated property matches some specific conditions.  For
instance, when a regular expression pattern matches, we run a handler
designated with the keyword `@match`, and when an LTL formula is violated, we
run a handler designated with the keyword `@violation`. 

As mentioned, handlers are run by the Java class as needed, however, we also export
a number of boolean variables that allow for external testing of the different 
*categories* (e.g., match or violation) that a property may flag.  Additionally,
RV-Monitor has the capability to generate monitors that enforce a given property
by delaying threads in multi-threaded programs. 


# Writing a Property, Instrumenting a Program, and Running It

## RV-Monitor event methods can be manually called from code.  
This allows for fine grain use of RV-Monitor monitors as a programming paradigm.

For example, we can directly call the event methods generated for the
HasNext.rvm property shown below:

	package rvm;
	
	HasNext(Iterator i) {
	  event hasnext(Iterator i) {}
	  event next(Iterator i) {}
	
	  ere : (hasnext hasnext* next)*
	
	  @fail {
	    System.out.println(
	             "! hasNext() has not been called"
	           + " before calling next() for an" 
	           + " iterator");
	          __RESET;
	  }
	}
	
The generated Java class named HasNextRuntimeMonitor for this property has two methods, 
one for each event, with the following signatures:

	public void hasnextEvent(Iterator i)

and

	public void nextEvent(Iterator i)

By calling the methods directly, rather than using some sort of automatic instrumentor,
like AspectJ, we can choose exactly which methods we wish to monitor.  For instance,
we can add a wrapper class for Iterator that has versions of hasNext and next that
call our monitoring code, and only use them in places where it is crucial to be correct.
The class could be defined as follows:

	  public class SafeIterator<E> implements java.util.Iterator<E> {
	  private java.util.Iterator<E> it;
	
	  public SafeIterator(java.util.Iterator it){
	    this.it = it;
	  }
	
	  public boolean hasNext(){
	    rvm.HasNextRuntimeMonitor.hasnextEvent(it);
	    return it.hasNext();
	  } 
	
	  public E next(){
	    rvm.HasNextRuntimeMonitor.nextEvent(it);
	    return it.next();
	  }
	
	  public  void remove(){
	    it.remove();
	  }
	}

Now our program can distinguish between monitored and unmonitored Iterators
by simply creating SafeIterators from Iterators.

For example, consider the following program:

	public class Test {
	  public static void main(String[] args){
	    Vector<Integer> v = new Vector<Integer>();
	
	    v.add(1);
	    v.add(2);
	    v.add(4);
	    v.add(8);
	
	    Iterator it = v.iterator();
	    SafeIterator i = new SafeIterator(it);
	    int sum = 0;
	
	    if(i.hasNext()){
	      sum += (Integer)i.next();
	      sum += (Integer)i.next();
	      sum += (Integer)i.next();
	      sum += (Integer)i.next();
	    }
	
	    System.out.println("sum: " + sum);
	  }
	}
	
We must compile `SafeIterator.java`, `Test.java`, and `HasNextRuntimeMonitor.java` and run
`Test.java`.

The javac and java commands need `rvmonitorrt.jar` and the monitor directory on your `CLASSPATH`.

    javac -cp [rvmonitorrt.jar]:[monitor directory] InstrumentedProgram(s) MonitorLibrary
    java -cp [rvmonitorrt.jar]:[monitor directory] TestWithMain

This is why we ask you to add `rv-monitor/lib/rvmonitorrt.jar:.` to your `CLASSPATH`.
It makes calling javac and java from the directory above the /rvm directory in all Java examples 
simpler. More on that in the Examples section.

    javac InstrumentedProgram(s) MonitorLibrary
    java TestWithMain

For instance, considering the existence of an rvm folder that would house HasNext.rvm and its 
generated property library, a command to compile would be:

    javac Test.java SafeIterator.java rvm/HasNextRuntimeMonitor.java

We will successfully catch all of the bad calls to `next()`:

    java Test

        ! hasNext() has not been called before calling next() for an iterator
        ! hasNext() has not been called before calling next() for an iterator
        ! hasNext() has not been called before calling next() for an iterator
        sum: 15

## RV-Monitor event methods can be referred to as form instrumentation.
For instance, one can call them using AspectJ.  It is possible to write an AspectJ file that
calls a given event method every place that event method needs to occur.

Rather than including our calls to the HasNextRuntimeMonitor events in
Java code, we can create an AspectJ aspect that calls them for all instances
of `next()` and `hasNext()` in the program.  This aspect can then be weaved throughout
any program to make *all* uses of Iterators safe.  What follows is an example of
an aspect that can achieve this effect.

	aspect HasNextAspect {
	  after(Iterator i) : call(* Iterator.hasNext()) && target(i) {
	    rvm.HasNextRuntimeMonitor.hasnextEvent(i);
	  }
	
	  after(): before(Iterator i) : call(* Iterator.next()) && target(i) {
	    rvm.HasNextRuntimeMonitor.nextEvent(it);
	  }
	}

**RV-Monitor can access a whole data base of properties that may be run against
a program as a large scale dynamic property checker.

## RV-Monitor can be used as a testing framework.
That is, RV-Monitor can use properties to enforce a desired thread schedule.  

Any time an event occurs which could violate a property, that thread is put to sleep until 
such time that it may continue. A special deadlock handler is able to run when all threads 
become deadlocked due to an infeasible schedule.

For example, we can modify our previous HasNext property as follows:
	
	enforce HasNext(Iterator i) {
		event hasnext(Iterator i) {}
		event next(Iterator i) {}
	
		ere : (hasnext hasnext* next)*
	
		@deadlock {
		  System.out.println(
	             "! hasNext() has not been called"
	           + " before calling next() for an" 
	           + " iterator");
	          __RESET;
		}
	}

By adding the keyword enforce, we can actually delay threads when a violation
of the specified ere is found.  As a more concrete example we can use the following
property to ensure mutual exclusion between calls to `hashcode()` and list modification:

	enforce SafeListCFG(List l) {
	  
	  event beforehashcode(List l) {}
	  event afterhashcode(List l) {}
	  event beforemodify(List l) {}
	  event aftermodify(List l) {}
	
	  cfg :
	    S −> A S | B S | epsilon,
	    A −> A beforehashcode A afterhashcode | epsilon,
	    B −> B beforemodify B aftermodify | epsilon
	
	@nonfail {}
	
	@deadlock { System.out.println(”Deadlock detected!”); }
	
	}
	
The property is parametric in the list, so operations on different list
instances will not interfere with each other. There are four types of events in
this property: `beforehashcode` and `afterhashcode` indicate the start and end
of the execution of `hashCode`, and `beforemodify` and `aftermodify` represent
the start and end of all the modification methods on `ArrayList`.  The property
is defined using a CFG, which allows us to pair the start and the end events of
the execution of +hashCode+ or of modification methods.  While the execution of
`hashCode` is in progress (event `afterhashcode` has not been encountered), the
execution of any modification methods is not allowed (event `beforemodify` is
not allowed).

# RV-Monitor Language

We introduce the RV-Monitor input language through the below BNF grammar,
which is extended with {p} for zero or more and [p] for zero or one p's:
	
	<RV-Monitor Specification>
        		::= {<Modifier>} <Id> ["(" <Java Parameters> ")"] "{"
                       		<Java Declarations>
                        	{<Event>}
                        	{<Property>
                        	{"@" <LOGIC State>  "{" <Java Statements> "}"}}
                        "}"
	<Modifier>          ::= "unsynchronized" | "decentralized" | "perthread" | "suffix"
	<Event>             ::= ["creation"] "event" <Id> "(" <Java Parameters> ")" "{" [ <Java Statements> ] "}"
   	<Property>          ::= <LOGIC Name> ":" <LOGIC Syntax>
   	<Java Declarations> ::= ... <!-- syntax of declarations in Java -->
	<Java Parameters>   ::= ... <!-- syntax of method parameter list in Java -->
	<Java Statements>   ::= ... <!-- slightly extended syntax of statements in Java --> 

`<Modifier>`
The modifier unsynchronized tells RV-Monitor that the monitor state need not be
protected against concurrent accesses; the default is synchronized. The
unsynchronized monitor is faster, but may suffer from races on its state
updates if the monitored program has multiple threads. The decentralized
modifier refers to decentralized monitor indexing. The default indexing is
centralized, meaning that the indexing trees needed to quickly access and
garbage-collect monitor instances are stored in a common place; decentralized
indexing means that the indexing trees are scattered all over the code as
additional fields of objects of interest. Decentralized indexing typically
yields lower runtime overhead, though it may not always work for all settings.
Our OOPSLA'07 paper explains how centralized and decentralized indexing work.

`<Java Parameters> and <Java Declarations>`
These are ordinary Java parameters (as used in methods) and Java declarations.
The former are the parameters of the RV-Monitor specification and the latter
are additional monitor variables that one can access and modify in both event
actions and property handlers (see below).

`<Event>`
The event declaration code allows for the definition of events which may then
be referred to in the property. As part of its defining AspectJ advice, an
event can also have arbitrary code associated with it, called an event action,
which is run when the event is observed; an event action can modify the program
or the monitor state. The event action is represented, in the grammar, by the
optional `<Java Statements>` within the braces at the end of the event
definition.

`<Property>`
Properties are optional in RV-Monitor. A property consists of a named formalism
(`<LOGIC Name>`), followed by a colon, followed by a property specification using
the named formalism (`<LOGIC Syntax>`) and usually referring to the declared
events. RV-Monitor is not bound to any particular property specification
formalism. New formalisms can be added to a RV-Monitor installation by means of
logic plugins. Each logic plugin comes with the following syntactic categories
that are documented on each logic plugin page: `<LOGIC Name>` is the name of the
logic, e.g., ere for extended regular expressions; `<LOGIC Syntax>` is the syntax
that the named logic provides to express properties; `<LOGIC State>` names the
states of monitors generated for the named logic to which one can associate
handlers (see below). The current version of RV-Monitor provides the following
plugins:

`FSM`     -- Finite State Machines <br>
`ERE`     -- Extended Regular Expressions <br>
`CFG`     -- Context Free Grammars <br>
`PTLTL`   -- Past Time Linear Temporal Logic <br>
`LTL`     -- Linear Temporal Logic <br>
`PTCARET` -- Past Time LTL with Calls and Returns <br> 
`SRS`     -- String Rewriting Systems <br>
 
If the property is missing, then the RV-Monitor specification is called raw.
Raw specifications are useful when no existing logic plugin is powerful or
efficient enough to specify the desired property; in that case, one embeds the
custom monitoring code manually within the event generation code.

`"@"<LOGIC State>`
This syntax allows us to define property handlers, which consist of arbitrary
Java code that will be invoked whenever a certain state is reached in the
generated monitor (e.g., validation or violation in linear temporal logic
specifications, or a particular state in a finite state machine description).
At least one handler is required anytime there is a property (i.e., anytime we
are not using a raw monitor).

`<Java Statement>`
The Java code used in RV-Monitor specifications slightly extends Java with
two special variables:
__RESET: a special expression (evaluates to void) that resets the monitor to its initial state;
__LOC: a string variable that evaluates to the line number generating the current event;


# Command Line Basics

To call rv-monitor with ease, please add `rv-monitor/bin` to your `PATH`!

*ALL* specification files must have the .rvm file extension.   

## Linux and Mac (POSIX)

The 'rv-monitor' script has the following usage:
	
	 rv-monitor [-v] [-d <target directory>] <specification file or dir>

    	-v option is verbose mode 
    	-d option is used to specify the target directory
        	where the resulting java code will be saved. It is optional.
    
For more options, type 'rv-monitor' or 'rv-monitor -h'

## Windows

The script, 'rv-monitor.bat' has the following usage:

	rv-monitor.bat [-v] [-d <target directory>] <specification file or dir>

For more options, type `rv-monitor.bat` or `rv-monitor.bat -h`


## Executing a Monitored Program

When you execute a monitored program with Java, you need to include the RV-Monitor Runtime 
Library, as well as your current directory, in your class path. The RV-Monitor 
Runtime Library is provided in this package in the rv-monitor/lib directory. 
A typical value of this is:

In Windows,
  rv-monitor\lib\rvmonitorrt.jar

In Linux and Mac,
  rv-monitor/lib/rvmonitorrt.jar

Add this to the left end of the `CLASSPATH` followed by `;.` (in Windows) or `:.`
(in Linux and Mac). The inclusion of the second part (i.e. *this* directory) is for ease of use. It
identifies the directory which houses the monitor, or rvm, directory.
We assume you will call java and javac from the directory which contains the rvm folder. 
This is for uniformity and ease of use in trying the examples.

*Optionally,* you can choose to include `-cp [rvmonitor.jar]:[monitor directory with .rvm and generated libraries]` when you compile with javac and run with java.

## Command Line Options

    -h | -help			  print this help message

    -d <output path>		  select directory to store output files

    -n <name>	                  use the given class name instead of specification name in the
                                  specification file

    -v | -verbose		  enable verbose output

    -debug			  enable verbose error message

    -s | -statistics		  generate monitor with statistics

    -noopt1			  don't use the enable set optimization

    -finegrainedlock		  use fine-grained lock for internal data structure

    -weakrefinterning		  use WeakReference interning in indexing trees


# Examples

Remember, before trying any examples, please ensure you've added the `rv-monitor/bin` directory to your PATH
and `rv-monitor/lib/rvmonitorrt.jar:.` to your `CLASSPATH`.

This ensures easy use of 1) rv-monitor to generate monitoring libraries, 2) javac to compile
the libraries together with your instrumented program, and 3) java to run the code in all the 
examples.

To get started, here's an example of monitoring via context free grammar.

In CFG/HasNext, we call
	
	rv-monitor rvm/HasNext.rvm
	javac rvm/HasNextRuntimeMonitor.java HasNext_1/HasNext_1.java
        java HasNext_1.HasNext_1

HasNext_1 demonstrates RV-Monitor used to detect unsafe programming practice.

> In HasNext_1.java, a programmer calls next() on an Iterator without first
> calling hasNext() to check if there is another item available. RV-Monitor
> warns the user every time this is done.

Now that we've already compiled the monitor, we must only call

        javac HasNext_2/HasNext_2.java
        java HasNext_2.HasNext_2

HasNext_2 demonstrates RV-Monitor used to enforce safe programming practice.

> In HasNext_2.java, a programmer correctly calls hasNext() on an Iterator
> before calling next(). RV-Monitor does not display any warnings in the
> terminal.

* If you navigate to the directory above the rvm folder in each example and compile & run the code there, 
you will be able to use the *same exact structure* as the commands in this example. That is,
you will not have to manually tell javac and java the directory in which your rvm directory dwells! 
The modification you have already made to your CLASSPATH environment variable will accomodate this. *

Each directory in rv-monitor/examples/java demonstrates different logic types used to enforce 
our monitoring.
