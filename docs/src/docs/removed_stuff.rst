Testing with RV-Monitor
-----------------------
There are several ways to use RV-Monitor.
One is to insert code to recovery from safety errors in your monitoring
library, increasing the safety of your program and providing lightweight
formal guarantees.
RV-Monitor can also be used as a debugger, logging errors when they
occur and giving a developer increased insight as to the execution of
their program and the order of their events.

For example, we can use the following property to ensure mutual
exclusion between calls to ``hashcode()`` and list modification::

	enforce SafeListCFG(List l) {
	  
	  event beforehashcode(List l) {}
	  event afterhashcode(List l) {}
	  event beforemodify(List l) {}
	  event aftermodify(List l) {}
	
	  cfg :
	    S -> A S | B S | epsilon,
	    A -> A beforehashcode A afterhashcode | epsilon,
	    B -> B beforemodify B aftermodify | epsilon
	
	@nonfail {}
	
	@deadlock { System.out.println(”Deadlock detected!”); }
	
	}
	
The property is parametric in the list, so operations on different list
instances will not interfere with each other.
There are four types of events in this property: ``beforehashcode`` and 
``afterhashcode`` indicate the start and end of the execution of ``hashCode``,
and ``beforemodify`` and ``aftermodify`` represent the start and end of all
the modification methods on ``ArrayList``.
The property is defined using a CFG, which allows us to pair the start and
the end events of the execution of ``hashCode`` or of modification methods.
While the execution of ``hashCode`` is in progress (event ``afterhashcode``
has not been encountered), the execution of any modification methods is not
allowed (event ``beforemodify`` is not allowed).

If a violation of this property occurs, an error is reported to the developer.

Specification Language
----------------------

We introduce the RV-Monitor input language through the below BNF grammar,
which is extended with {p} for zero or more and [p] for zero or one p's:

.. code-block:: none
	
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
Our `OOPSLA'07 paper <http://fsl.cs.illinois.edu/index.php/MOP:_An_Efficient_and_Generic_Runtime_Verification_Framework>`_ 
explains how centralized and decentralized indexing work.

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

- **FSM**     -- Finite State Machines
- **ERE**     -- Extended Regular Expressions
- **CFG**     -- Context Free Grammars
- **PTLTL**   -- Past Time Linear Temporal Logic
- **LTL**     -- Linear Temporal Logic
- **PTCARET** -- Past Time LTL with Calls and Returns
- **SRS**     -- String Rewriting Systems
 
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
__MONITOR: a special variable that evaluates to the current monitor object, so that one can read/write monitor variables.
