//////////
// Info //
//////////

  The following project Monitored shows how we can use JavaMOP to find errors
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

--Install JavaMOP if you wish to compile mop properties yourself (not
necessary for this example)

////////////////////////
//Compiling Steps.    //
////////////////////////

Open a terminal of some sort
cd to MyFirstApp and run the following commands:

$ant debug

//This will build the app in debug mode

$adb install bin/MyFirstApp-debug.apk

//This installs the app.  I use an actual phone, which should work
//as automatically in OS X when plugged in with the USB data cable.
//For Windows I believe you must install a driver.  The other option
//is to use the Android emulator manager to create a running emulator

$adb logcat '*:E'

//This starts logcat in a terminal, and sets it to show only errors.

Run the app from the phone/emulator.  In logcat you should see

E/SetOnPreparedListener(18637): setOnPreparedListener was not called before media player was started!

40 times.  I have it set to print that many simply to make it easier to see.
We could also kill the app, or attempt to recover from the error.

/////////////////////////////////////////////////////
// Creating a new App Project for use with JavaMOP //
/////////////////////////////////////////////////////

First we issue the following command:

$ android create project --target 1 --name MyFirstApp \
--path MyFirstApp --activity MyFirstActivity \
--package com.example.myapp

Where MyFirstApp and MyFirstActivity and com.example.myapp are replaced as 
desired

copy custom_rules.xml to the new app directory.  This is what correctly runs
the aspectj weaver

Make a directory called aspects.  This is where we will put JavaMOP compiled
aspects.  You may also put properties there if you wish.  Ajc is smart enough
to only compile .aj files.

JavaMOP properties must be hand compiled using the rvmonitor command line tool.
The easiest way to do this is just to issue the command:

$rvmonitor <file>.mop

Which will generate an .aj file in the same directory as the mop file.  Make
sure to copy the .aj file to the aspect directory.  Any aj files in the aspect
directory will be weaved.  In general, rather than having a file to turn off
monitors, we prefer to use selective weaving, for performance reasons.

I may add a rvmonitor ant task, but, in general, it is not necessary to
regenerate aj files all that often, while the weaving step must happen
every time.

