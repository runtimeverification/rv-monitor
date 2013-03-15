package com.example.myapp;
import android.media.MediaPlayer;
import android.util.Log;
import android.media.MediaPlayer.OnPreparedListener;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;
import rvmonitorrt.*;
import java.lang.ref.*;

class SetOnPreparedListenerMonitor_Set extends rvmonitorrt.MOPSet {
	protected SetOnPreparedListenerMonitor[] elementData;
	boolean skipEvent = false;
	boolean violationProp1;

	SetOnPreparedListenerMonitor_Set(){
		this.size = 0;
		this.elementData = new SetOnPreparedListenerMonitor[4];
	}

	public final int size(){
		while(size > 0 && elementData[size-1].MOP_terminated) {
			elementData[--size] = null;
		}
		return size;
	}

	public final boolean add(MOPMonitor e){
		ensureCapacity();
		elementData[size++] = (SetOnPreparedListenerMonitor)e;
		return true;
	}

	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			SetOnPreparedListenerMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				monitor.endObject(idnum);
			}
			if(!monitor.MOP_terminated){
				elementData[numAlive++] = monitor;
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	public final boolean alive(){
		for(int i = 0; i < size; i++){
			MOPMonitor monitor = elementData[i];
			if(!monitor.MOP_terminated){
				return true;
			}
		}
		return false;
	}

	public final void endObjectAndClean(int idnum){
		int size = this.size;
		this.size = 0;
		for(int i = size - 1; i >= 0; i--){
			MOPMonitor monitor = elementData[i];
			if(monitor != null && !monitor.MOP_terminated){
				monitor.endObject(idnum);
			}
			elementData[i] = null;
		}
		elementData = null;
	}

	public final void ensureCapacity() {
		int oldCapacity = elementData.length;
		if (size + 1 > oldCapacity) {
			cleanup();
		}
		if (size + 1 > oldCapacity) {
			SetOnPreparedListenerMonitor[] oldData = elementData;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < size + 1){
				newCapacity = size + 1;
			}
			elementData = Arrays.copyOf(oldData, newCapacity);
		}
	}

	final void cleanup() {
		int numAlive = 0 ;
		for(int i = 0; i < size; i++){
			SetOnPreparedListenerMonitor monitor = (SetOnPreparedListenerMonitor)elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;
			}
		}
		for(int i = numAlive; i < size; i++){
			elementData[i] = null;
		}
		size = numAlive;
	}

	final void event_setOnPreparedListener() {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SetOnPreparedListenerMonitor monitor = (SetOnPreparedListenerMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_setOnPreparedListener();
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation();
				}
				skipEvent |= monitor.skipEvent;
				monitor.skipEvent = false;
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}

	final void event_start(MediaPlayer m) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SetOnPreparedListenerMonitor monitor = (SetOnPreparedListenerMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_start(m);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation();
				}
				skipEvent |= monitor.skipEvent;
				monitor.skipEvent = false;
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}
}

class SetOnPreparedListenerMonitor extends rvmonitorrt.MOPMonitor implements Cloneable, rvmonitorrt.MOPObject {
	protected Object clone() {
		try {
			SetOnPreparedListenerMonitor ret = (SetOnPreparedListenerMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	MediaPlayer toFix;

	static android.app.Activity MOP_activity;
	static void setActivity(android.app.Activity a) {
		MOP_activity = a;
	}

	boolean skipEvent = false;
	int Prop_1_state;
	static final int Prop_1_transition_setOnPreparedListener[] = {1, 1, 3, 3};;
	static final int Prop_1_transition_start[] = {2, 1, 3, 3};;

	boolean Prop_1_Category_violation = false;

	SetOnPreparedListenerMonitor () {
		Prop_1_state = 0;

	}

	final void Prop_1_event_setOnPreparedListener() {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_setOnPreparedListener[Prop_1_state];
		Prop_1_Category_violation = Prop_1_state == 2;
	}

	final void Prop_1_event_start(MediaPlayer m) {
		{
			toFix = m;
		}
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_start[Prop_1_state];
		Prop_1_Category_violation = Prop_1_state == 2;
	}

	final void Prop_1_handler_violation (){
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this.MOP_activity);
			builder.setMessage("Media player called without setting the prepared listener, setting listener....");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int id) {
					toFix.setOnPreparedListener(new OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							mp.start();
						}
						});
					}
					});
					builder.create().show();
					skipEvent = true;
				}

			}

			final void reset() {
				MOP_lastevent = -1;
				Prop_1_state = 0;
				Prop_1_Category_violation = false;
			}

			public final void endObject(int idnum){
				switch(idnum){
				}
				switch(MOP_lastevent) {
					case -1:
					return;
					case 0:
					//setOnPreparedListener
					return;
					case 1:
					//start
					return;
				}
				return;
			}

		}

		public class SetOnPreparedListenerRuntimeMonitor implements rvmonitorrt.MOPObject {
			public static boolean violationProp1 = false;
			public static boolean skipEvent = false;
			private static rvmonitorrt.map.MOPMapManager SetOnPreparedListenerMapManager;
			static {
				SetOnPreparedListenerMapManager = new rvmonitorrt.map.MOPMapManager();
				SetOnPreparedListenerMapManager.start();
			}

			// Declarations for the Lock
			private static ReentrantLock SetOnPreparedListener_MOPLock = new ReentrantLock();
			private static Condition SetOnPreparedListener_MOPLock_cond = SetOnPreparedListener_MOPLock.newCondition();

			private static boolean SetOnPreparedListener_activated = false;

			// Declarations for Indexing Trees
			static SetOnPreparedListenerMonitor SetOnPreparedListener_Monitor = new SetOnPreparedListenerMonitor();

			// Trees for References

			public static void setOnPreparedListenerEvent() {
				SetOnPreparedListener_activated = true;
				while (!SetOnPreparedListener_MOPLock.tryLock()) {
					Thread.yield();
				}
				SetOnPreparedListenerMonitor mainMonitor = null;

				violationProp1 = false;
				skipEvent = false;

				mainMonitor = SetOnPreparedListener_Monitor;

				if (mainMonitor == null) {
					mainMonitor = new SetOnPreparedListenerMonitor();

					SetOnPreparedListener_Monitor = mainMonitor;
				}

				mainMonitor.Prop_1_event_setOnPreparedListener();
				violationProp1 |= mainMonitor.Prop_1_Category_violation;
				if(mainMonitor.Prop_1_Category_violation) {
					mainMonitor.Prop_1_handler_violation();
				}
				skipEvent |= mainMonitor.skipEvent;
				mainMonitor.skipEvent = false;
				SetOnPreparedListener_MOPLock.unlock();
			}

			public static void startEvent(MediaPlayer m) {
				SetOnPreparedListener_activated = true;
				while (!SetOnPreparedListener_MOPLock.tryLock()) {
					Thread.yield();
				}
				SetOnPreparedListenerMonitor mainMonitor = null;

				violationProp1 = false;
				skipEvent = false;

				mainMonitor = SetOnPreparedListener_Monitor;

				if (mainMonitor == null) {
					mainMonitor = new SetOnPreparedListenerMonitor();

					SetOnPreparedListener_Monitor = mainMonitor;
				}

				mainMonitor.Prop_1_event_start(m);
				violationProp1 |= mainMonitor.Prop_1_Category_violation;
				if(mainMonitor.Prop_1_Category_violation) {
					mainMonitor.Prop_1_handler_violation();
				}
				skipEvent |= mainMonitor.skipEvent;
				mainMonitor.skipEvent = false;
				SetOnPreparedListener_MOPLock.unlock();
			}

			public static void onCreateActivity(Activity a) {
				SetOnPreparedListenerMonitor.MOP_activity = a;
			}

		}
