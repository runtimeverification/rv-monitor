
import android.media.MediaPlayer;
import android.util.Log;
import java.util.*;
import javamoprt.*;
import java.lang.ref.*;
import org.aspectj.lang.*;

class SetOnPreparedListenerMonitor_Set extends javamoprt.MOPSet {
	protected SetOnPreparedListenerMonitor[] elementData;

	public SetOnPreparedListenerMonitor_Set(){
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

	public final void cleanup() {
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

	public final void event_setOnPreparedListener() {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SetOnPreparedListenerMonitor monitor = (SetOnPreparedListenerMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_setOnPreparedListener();
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_start() {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SetOnPreparedListenerMonitor monitor = (SetOnPreparedListenerMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_start();
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}
}

class SetOnPreparedListenerMonitor extends javamoprt.MOPMonitor implements Cloneable, javamoprt.MOPObject {
	public Object clone() {
		try {
			SetOnPreparedListenerMonitor ret = (SetOnPreparedListenerMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_setOnPreparedListener[] = {1, 1, 3, 3};;
	static final int Prop_1_transition_start[] = {2, 0, 3, 3};;

	boolean Prop_1_Category_violation = false;

	public SetOnPreparedListenerMonitor () {
		Prop_1_state = 0;

	}

	public final void Prop_1_event_setOnPreparedListener() {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_setOnPreparedListener[Prop_1_state];
		Prop_1_Category_violation = Prop_1_state == 2;
	}

	public final void Prop_1_event_start() {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_start[Prop_1_state];
		Prop_1_Category_violation = Prop_1_state == 2;
	}

	public final void Prop_1_handler_violation (){
		{
			for (int i = 0; i < 40; ++i) {
				Log.e("SetOnPreparedListener", "setOnPreparedListener was not called before media player was started!");
			}
		}

	}

	public final void reset() {
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

public aspect SetOnPreparedListenerMonitorAspect implements javamoprt.MOPObject {
	javamoprt.map.MOPMapManager SetOnPreparedListenerMapManager;
	public SetOnPreparedListenerMonitorAspect(){
		SetOnPreparedListenerMapManager = new javamoprt.map.MOPMapManager();
		SetOnPreparedListenerMapManager.start();
	}

	// Declarations for the Lock
	static Object SetOnPreparedListener_MOPLock = new Object();

	static boolean SetOnPreparedListener_activated = false;

	// Declarations for Indexing Trees
	static SetOnPreparedListenerMonitor SetOnPreparedListener_Monitor = new SetOnPreparedListenerMonitor();

	// Trees for References

	pointcut MOP_CommonPointCut() : !within(javamoprt.MOPObject+) && !adviceexecution();
	pointcut SetOnPreparedListener_setOnPreparedListener() : (call(void MediaPlayer.setOnPreparedListener(..))) && MOP_CommonPointCut();
	after () : SetOnPreparedListener_setOnPreparedListener() {
		SetOnPreparedListener_activated = true;
		synchronized(SetOnPreparedListener_MOPLock) {
			SetOnPreparedListenerMonitor mainMonitor = null;

			mainMonitor = SetOnPreparedListener_Monitor;

			if (mainMonitor == null) {
				mainMonitor = new SetOnPreparedListenerMonitor();

				SetOnPreparedListener_Monitor = mainMonitor;
			}

			mainMonitor.Prop_1_event_setOnPreparedListener();
			if(mainMonitor.Prop_1_Category_violation) {
				mainMonitor.Prop_1_handler_violation();
			}
		}
	}

	pointcut SetOnPreparedListener_start() : (call(void MediaPlayer.start())) && MOP_CommonPointCut();
	before () : SetOnPreparedListener_start() {
		SetOnPreparedListener_activated = true;
		synchronized(SetOnPreparedListener_MOPLock) {
			SetOnPreparedListenerMonitor mainMonitor = null;

			mainMonitor = SetOnPreparedListener_Monitor;

			if (mainMonitor == null) {
				mainMonitor = new SetOnPreparedListenerMonitor();

				SetOnPreparedListener_Monitor = mainMonitor;
			}

			mainMonitor.Prop_1_event_start();
			if(mainMonitor.Prop_1_Category_violation) {
				mainMonitor.Prop_1_handler_violation();
			}
		}
	}

}
