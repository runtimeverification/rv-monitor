//////////
// Info //
//////////

  The following project Monitored shows how we can use RV-Monitor to find errors
in Android Apps.  We just have one MOP property, to start.  It is the
equivalent of the Develope09ForMusicPlayer app property, but stated in a
simple, short JavaMOP file.

////////////////////////
//Installation Steps. //
////////////////////////

Note: for each step, make sure the installed binaries are visible in
your PATH (POSIX) or %PATH% (Windows) variable.

--Install the Android SDK

--Install Ant

--Install Aspectj

--Copy aspectjtools.jar from <ASPECTJ_HOME>/lib to <ANT_HOME>/lib
  This allows for running ajc from ant scripts.
  For me the ant libraries go in /usr/share/ant/lib/


////////////////////////
//Compiling Steps.    //
////////////////////////

Make sure to set sdk.dir in local.properties to the correct installation
for the Android SDK.

Open a terminal of some sort and cd to this directory
run the following commands:

$ant debug

//This will build the app in debug mode

$adb install bin/MonitoredApp-debug.apk

//This installs the app.  I use an actual phone, which should work
//as automatically in OS X when plugged in with the USB data cable.
//For Windows I believe you must install a driver.  The other option
//is to use the Android emulator manager to create a running emulator

Run the app from the phone/emulator.  

////////////////////
// Explanation    //
////////////////////

The property can be found in mop_properties and is called 
SetOnPreparedListener.mop.  We compile this to a java file
by issuing the command ../../../bin/rv-monitor SetOnPreparedListener.mop
in the mop_properties direcotry.  The result java file is copied
to src/com/example/myapp/.  We have an aspect that calls the
methods of this generated class place in the aspects directory.
Anything in the aspects directory is automatically weaved.  Alternatively,
we could call the methods manually in the MonitoredApp.java file.

Any property file which contains the keyword __ACTIVITY automatically
generates a method called onCreateActivity, to which the aspect or
manual instrumentation must pass the current activity so that we
may correctly create dialog boxes from the property.  When
this file is run, a dialog box will pop up telling us that
we have recovered from an error. 

////////////////////////////////////////////////////////
// Creating a new App Project for use with RV-Monitor //
////////////////////////////////////////////////////////

First we issue the following command:

$ android create project --target 1 --name MyFirstApp \
--path MyFirstApp --activity MyFirstActivity \
--package com.example.myapp

Where MyFirstApp and MyFirstActivity and com.example.myapp are replaced as 
desired

copy custom_rules.xml to the new app directory.  This is what correctly runs
the aspectj weaver

Make a directory called aspects.  This is where we will put aspects for weaving
in calls to our event methods.  You may also put properties there if you wish.  Ajc is smart enough
to only compile .aj files.

RV-Monitor properties must be hand compiled using the rv-monitor command line tool.
The easiest way to do this is just to issue the command:

$rv-monitor <file>.mop

From the directory this readme is in, rv-monitor can be found as
../../bin/rv-monitor

Which will generate a .java file in the same directory as the mop file.  Make
sure to copy the .java file to the src directory in the proper package
path.  Unfortunately, there is no good way to automate this process.
Any aj files in the aspect directory will be weaved.  In general, 
rather than having a file to turn off monitors, we prefer to use selective weaving, 
for performance reasons.

I may add a rv-monitor ant task, but, in general, it is not necessary to
regenerate aj files all that often, while the weaving step must happen
every time.

