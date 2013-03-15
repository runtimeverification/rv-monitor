package mop;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rvmonitorrt.*;
import java.lang.ref.*;

class HasNextMonitor_Set extends rvmonitorrt.MOPSet {
	protected HasNextMonitor[] elementData;
	boolean failProp1;

	HasNextMonitor_Set(){
		this.size = 0;
		this.elementData = new HasNextMonitor[4];
	}

	public final int size(){
		while(size > 0 && elementData[size-1].MOP_terminated) {
			elementData[--size] = null;
		}
		return size;
	}

	public final boolean add(MOPMonitor e){
		ensureCapacity();
		elementData[size++] = (HasNextMonitor)e;
		return true;
	}

	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			HasNextMonitor monitor = elementData[i];
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
			HasNextMonitor[] oldData = elementData;
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
			HasNextMonitor monitor = (HasNextMonitor)elementData[i];
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

	final void event_hasnext(Iterator i) {
		this.failProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			HasNextMonitor monitor = (HasNextMonitor)this.elementData[i_1];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_hasnext(i);
				failProp1 |= monitor.Prop_1_Category_fail;
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elementData[i_1] = null;
		}
		size = numAlive;
	}

	final void event_next(Iterator i) {
		this.failProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			HasNextMonitor monitor = (HasNextMonitor)this.elementData[i_1];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_next(i);
				failProp1 |= monitor.Prop_1_Category_fail;
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elementData[i_1] = null;
		}
		size = numAlive;
	}
}

class HasNextMonitor extends rvmonitorrt.MOPMonitor implements Cloneable, rvmonitorrt.MOPObject {
	protected Object clone() {
		try {
			HasNextMonitor ret = (HasNextMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_hasnext[] = {1, 1, 2};;
	static final int Prop_1_transition_next[] = {2, 0, 2};;

	boolean Prop_1_Category_fail = false;

	HasNextMonitor () {
		Prop_1_state = 0;

	}

	final void Prop_1_event_hasnext(Iterator i) {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_hasnext[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 2;
	}

	final void Prop_1_event_next(Iterator i) {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 2;
	}

	final void Prop_1_handler_fail (Iterator i){
		{
			System.err.println("! hasNext() has not been called" + " before calling next() for an" + " iterator");
			this.reset();
		}

	}

	final void reset() {
		MOP_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_fail = false;
	}

	rvmonitorrt.ref.MOPWeakReference MOPRef_i;

	//alive_parameters_0 = [Iterator i]
	boolean alive_parameters_0 = true;

	public final void endObject(int idnum){
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
		}
		switch(MOP_lastevent) {
			case -1:
			return;
			case 0:
			//hasnext
			//alive_i
			if(!(alive_parameters_0)){
				MOP_terminated = true;
				return;
			}
			break;

			case 1:
			//next
			//alive_i
			if(!(alive_parameters_0)){
				MOP_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

public class HasNextRuntimeMonitor implements rvmonitorrt.MOPObject {
	public static boolean failProp1 = false;
	private static rvmonitorrt.map.MOPMapManager HasNextMapManager;
	static {
		HasNextMapManager = new rvmonitorrt.map.MOPMapManager();
		HasNextMapManager.start();
	}

	// Declarations for the Lock
	private static ReentrantLock HasNext_MOPLock = new ReentrantLock();
	private static Condition HasNext_MOPLock_cond = HasNext_MOPLock.newCondition();

	private static boolean HasNext_activated = false;

	// Declarations for Indexing Trees
	static rvmonitorrt.map.MOPBasicRefMapOfMonitor HasNext_i_Map = new rvmonitorrt.map.MOPBasicRefMapOfMonitor(0);
	static rvmonitorrt.ref.MOPWeakReference HasNext_i_Map_cachekey_0 = rvmonitorrt.map.MOPBasicRefMapOfMonitor.NULRef;
	static HasNextMonitor HasNext_i_Map_cachenode = null;

	// Trees for References
	static rvmonitorrt.map.MOPRefMap HasNext_Iterator_RefMap = HasNext_i_Map;

	public static void hasnextEvent(Iterator i) {
		HasNext_activated = true;
		while (!HasNext_MOPLock.tryLock()) {
			Thread.yield();
		}
		HasNextMonitor mainMonitor = null;
		rvmonitorrt.map.MOPMap mainMap = null;
		rvmonitorrt.ref.MOPWeakReference TempRef_i;

		failProp1 = false;
		// Cache Retrieval
		if (i == HasNext_i_Map_cachekey_0.get()) {
			TempRef_i = HasNext_i_Map_cachekey_0;

			mainMonitor = HasNext_i_Map_cachenode;
		} else {
			TempRef_i = HasNext_i_Map.getRef(i);
		}

		if (mainMonitor == null) {
			mainMap = HasNext_i_Map;
			mainMonitor = (HasNextMonitor)mainMap.getNode(TempRef_i);

			if (mainMonitor == null) {
				mainMonitor = new HasNextMonitor();

				mainMonitor.MOPRef_i = TempRef_i;

				HasNext_i_Map.putNode(TempRef_i, mainMonitor);
			}

			HasNext_i_Map_cachekey_0 = TempRef_i;
			HasNext_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_hasnext(i);
		failProp1 |= mainMonitor.Prop_1_Category_fail;
		if(mainMonitor.Prop_1_Category_fail) {
			mainMonitor.Prop_1_handler_fail(i);
		}
		HasNext_MOPLock.unlock();
	}

	public static void nextEvent(Iterator i) {
		HasNext_activated = true;
		while (!HasNext_MOPLock.tryLock()) {
			Thread.yield();
		}
		HasNextMonitor mainMonitor = null;
		rvmonitorrt.map.MOPMap mainMap = null;
		rvmonitorrt.ref.MOPWeakReference TempRef_i;

		failProp1 = false;
		// Cache Retrieval
		if (i == HasNext_i_Map_cachekey_0.get()) {
			TempRef_i = HasNext_i_Map_cachekey_0;

			mainMonitor = HasNext_i_Map_cachenode;
		} else {
			TempRef_i = HasNext_i_Map.getRef(i);
		}

		if (mainMonitor == null) {
			mainMap = HasNext_i_Map;
			mainMonitor = (HasNextMonitor)mainMap.getNode(TempRef_i);

			if (mainMonitor == null) {
				mainMonitor = new HasNextMonitor();

				mainMonitor.MOPRef_i = TempRef_i;

				HasNext_i_Map.putNode(TempRef_i, mainMonitor);
			}

			HasNext_i_Map_cachekey_0 = TempRef_i;
			HasNext_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_next(i);
		failProp1 |= mainMonitor.Prop_1_Category_fail;
		if(mainMonitor.Prop_1_Category_fail) {
			mainMonitor.Prop_1_handler_fail(i);
		}
		HasNext_MOPLock.unlock();
	}

}
