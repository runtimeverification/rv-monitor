package mop;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rvmonitorrt.*;
import java.lang.ref.*;
import org.aspectj.lang.*;

class SafeEnumMonitor_Set extends rvmonitorrt.MOPSet {
	protected SafeEnumMonitor[] elementData;
	String MOP_loc = null;

	public SafeEnumMonitor_Set(){
		this.size = 0;
		this.elementData = new SafeEnumMonitor[4];
	}

	public final int size(){
		while(size > 0 && elementData[size-1].MOP_terminated) {
			elementData[--size] = null;
		}
		return size;
	}

	public final boolean add(MOPMonitor e){
		ensureCapacity();
		elementData[size++] = (SafeEnumMonitor)e;
		return true;
	}

	public final void endObject(int idnum){
		int numAlive = 0;
		for(int i = 0; i < size; i++){
			SafeEnumMonitor monitor = elementData[i];
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
			SafeEnumMonitor[] oldData = elementData;
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
			SafeEnumMonitor monitor = (SafeEnumMonitor)elementData[i];
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

	public final void event_create(Vector v, Enumeration e) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeEnumMonitor monitor = (SafeEnumMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.MOP_loc = MOP_loc;
				monitor.Prop_1_event_create(v, e);
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(v, e);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_updatesource(Vector v) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeEnumMonitor monitor = (SafeEnumMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.MOP_loc = MOP_loc;
				monitor.Prop_1_event_updatesource(v);
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(v, null);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}

	public final void event_next(Enumeration e) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeEnumMonitor monitor = (SafeEnumMonitor)this.elementData[i];
			if(!monitor.MOP_terminated){
				elementData[numAlive] = monitor;
				numAlive++;

				monitor.MOP_loc = MOP_loc;
				monitor.Prop_1_event_next(e);
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(null, e);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elementData[i] = null;
		}
		size = numAlive;
	}
}

class SafeEnumMonitor extends rvmonitorrt.MOPMonitor implements Cloneable, rvmonitorrt.MOPObject {
	public long tau = -1;
	public Object clone() {
		try {
			SafeEnumMonitor ret = (SafeEnumMonitor) super.clone();
			ret.monitorInfo = (rvmonitorrt.MOPMonitorInfo)this.monitorInfo.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	String MOP_loc;
	int Prop_1_state;
	static final int Prop_1_transition_create[] = {1, 1, 3, 3};;
	static final int Prop_1_transition_updatesource[] = {0, 0, 3, 3};;
	static final int Prop_1_transition_next[] = {2, 1, 3, 3};;

	boolean Prop_1_Category_violation = false;

	public SafeEnumMonitor () {
		Prop_1_state = 0;

	}

	public final void Prop_1_event_create(Vector v, Enumeration e) {
		MOP_lastevent = 0;

		Prop_1_state = Prop_1_transition_create[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
	}

	public final void Prop_1_event_updatesource(Vector v) {
		MOP_lastevent = 1;

		Prop_1_state = Prop_1_transition_updatesource[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
	}

	public final void Prop_1_event_next(Enumeration e) {
		MOP_lastevent = 2;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
	}

	public final void Prop_1_handler_violation (Vector v, Enumeration e){
		{
			System.out.println("improper enumeration usage at " + this.MOP_loc);
			this.reset();
		}

	}

	public final void reset() {
		MOP_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_violation = false;
	}

	public rvmonitorrt.ref.MOPTagWeakReference MOPRef_v;
	public rvmonitorrt.ref.MOPTagWeakReference MOPRef_e;

	//alive_parameters_0 = [Vector v, Enumeration e]
	public boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Enumeration e]
	public boolean alive_parameters_1 = true;

	public final void endObject(int idnum){
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
		}
		switch(MOP_lastevent) {
			case -1:
			return;
			case 0:
			//create
			//alive_v && alive_e
			if(!(alive_parameters_0)){
				MOP_terminated = true;
				return;
			}
			break;

			case 1:
			//updatesource
			//alive_e
			if(!(alive_parameters_1)){
				MOP_terminated = true;
				return;
			}
			break;

			case 2:
			//next
			//alive_v && alive_e
			if(!(alive_parameters_0)){
				MOP_terminated = true;
				return;
			}
			break;

		}
		return;
	}

	rvmonitorrt.MOPMonitorInfo monitorInfo;
}

public aspect SafeEnumMonitorAspect implements rvmonitorrt.MOPObject {
	rvmonitorrt.map.MOPMapManager SafeEnumMapManager;
	public SafeEnumMonitorAspect(){
		SafeEnumMapManager = new rvmonitorrt.map.MOPMapManager();
		SafeEnumMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock SafeEnum_MOPLock = new ReentrantLock();
	static Condition SafeEnum_MOPLock_cond = SafeEnum_MOPLock.newCondition();

	// Declarations for Timestamps
	static long SafeEnum_timestamp = 1;

	static boolean SafeEnum_activated = false;

	// Declarations for Indexing Trees
	static rvmonitorrt.ref.MOPTagWeakReference SafeEnum_v_Map_cachekey_0 = rvmonitorrt.map.MOPTagRefMap.NULRef;
	static SafeEnumMonitor_Set SafeEnum_v_Map_cacheset = null;
	static SafeEnumMonitor SafeEnum_v_Map_cachenode = null;
	static rvmonitorrt.map.MOPAbstractMap SafeEnum_v_e_Map = new rvmonitorrt.map.MOPMapOfAll(0);
	static rvmonitorrt.ref.MOPTagWeakReference SafeEnum_v_e_Map_cachekey_0 = rvmonitorrt.map.MOPTagRefMap.NULRef;
	static rvmonitorrt.ref.MOPTagWeakReference SafeEnum_v_e_Map_cachekey_1 = rvmonitorrt.map.MOPTagRefMap.NULRef;
	static SafeEnumMonitor SafeEnum_v_e_Map_cachenode = null;
	static rvmonitorrt.map.MOPAbstractMap SafeEnum_e_Map = new rvmonitorrt.map.MOPMapOfSetMon(1);
	static rvmonitorrt.ref.MOPTagWeakReference SafeEnum_e_Map_cachekey_1 = rvmonitorrt.map.MOPTagRefMap.NULRef;
	static SafeEnumMonitor_Set SafeEnum_e_Map_cacheset = null;
	static SafeEnumMonitor SafeEnum_e_Map_cachenode = null;
	static SafeEnumMonitor_Set SafeEnum__To__v_Set = new SafeEnumMonitor_Set();

	// Trees for References
	static rvmonitorrt.map.MOPRefMap SafeEnum_Enumeration_RefMap = new rvmonitorrt.map.MOPTagRefMap();
	static rvmonitorrt.map.MOPRefMap SafeEnum_Vector_RefMap = new rvmonitorrt.map.MOPTagRefMap();

	pointcut MOP_CommonPointCut() : !within(rvmonitorrt.MOPObject+) && !adviceexecution();
	pointcut SafeEnum_create(Vector v) : (call(Enumeration Vector+.elements()) && target(v)) && MOP_CommonPointCut();
	after (Vector v) returning (Enumeration e) : SafeEnum_create(v) {
		SafeEnum_activated = true;
		while (!SafeEnum_MOPLock.tryLock()) {
			Thread.yield();
		}
		Object obj;
		rvmonitorrt.map.MOPMap tempMap;
		SafeEnumMonitor mainMonitor = null;
		SafeEnumMonitor origMonitor = null;
		rvmonitorrt.map.MOPMap mainMap = null;
		rvmonitorrt.map.MOPMap origMap = null;
		SafeEnumMonitor_Set monitors = null;
		rvmonitorrt.ref.MOPTagWeakReference TempRef_v;
		rvmonitorrt.ref.MOPTagWeakReference TempRef_e;

		// Cache Retrieval
		if (v == SafeEnum_v_e_Map_cachekey_0.get() && e == SafeEnum_v_e_Map_cachekey_1.get()) {
			TempRef_v = SafeEnum_v_e_Map_cachekey_0;
			TempRef_e = SafeEnum_v_e_Map_cachekey_1;

			mainMonitor = SafeEnum_v_e_Map_cachenode;
		} else {
			TempRef_v = SafeEnum_Vector_RefMap.getTagRef(v);
			TempRef_e = SafeEnum_Enumeration_RefMap.getTagRef(e);
		}

		if (mainMonitor == null) {
			tempMap = SafeEnum_v_e_Map;
			obj = tempMap.getMap(TempRef_v);
			if (obj == null) {
				obj = new rvmonitorrt.map.MOPMapOfMonitor(1);
				tempMap.putMap(TempRef_v, obj);
			}
			mainMap = (rvmonitorrt.map.MOPAbstractMap)obj;
			mainMonitor = (SafeEnumMonitor)mainMap.getNode(TempRef_e);

			if (mainMonitor == null) {
				if (mainMonitor == null) {
					origMap = SafeEnum_v_e_Map;
					origMonitor = (SafeEnumMonitor)origMap.getNode(TempRef_v);
					if (origMonitor != null) {
						boolean timeCheck = true;

						if (TempRef_e.disable > origMonitor.tau) {
							timeCheck = false;
						}

						if (timeCheck) {
							mainMonitor = (SafeEnumMonitor)origMonitor.clone();
							mainMonitor.MOPRef_e = TempRef_e;
							if (TempRef_e.tau == -1){
								TempRef_e.tau = origMonitor.tau;
							}
							mainMap.putNode(TempRef_e, mainMonitor);
							mainMonitor.monitorInfo.isFullParam = true;

							tempMap = SafeEnum_v_e_Map;
							obj = tempMap.getSet(TempRef_v);
							monitors = (SafeEnumMonitor_Set)obj;
							if (monitors == null) {
								monitors = new SafeEnumMonitor_Set();
								tempMap.putSet(TempRef_v, monitors);
							}
							monitors.add(mainMonitor);

							tempMap = SafeEnum_e_Map;
							obj = tempMap.getSet(TempRef_e);
							monitors = (SafeEnumMonitor_Set)obj;
							if (monitors == null) {
								monitors = new SafeEnumMonitor_Set();
								tempMap.putSet(TempRef_e, monitors);
							}
							monitors.add(mainMonitor);
						}
					}
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeEnumMonitor();
					mainMonitor.monitorInfo = new rvmonitorrt.MOPMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = true;

					mainMonitor.MOPRef_v = TempRef_v;
					mainMonitor.MOPRef_e = TempRef_e;

					mainMap.putNode(TempRef_e, mainMonitor);
					mainMonitor.tau = SafeEnum_timestamp;
					if (TempRef_v.tau == -1){
						TempRef_v.tau = SafeEnum_timestamp;
					}
					if (TempRef_e.tau == -1){
						TempRef_e.tau = SafeEnum_timestamp;
					}
					SafeEnum_timestamp++;

					tempMap = SafeEnum_v_e_Map;
					obj = tempMap.getSet(TempRef_v);
					monitors = (SafeEnumMonitor_Set)obj;
					if (monitors == null) {
						monitors = new SafeEnumMonitor_Set();
						tempMap.putSet(TempRef_v, monitors);
					}
					monitors.add(mainMonitor);

					tempMap = SafeEnum_e_Map;
					obj = tempMap.getSet(TempRef_e);
					monitors = (SafeEnumMonitor_Set)obj;
					if (monitors == null) {
						monitors = new SafeEnumMonitor_Set();
						tempMap.putSet(TempRef_e, monitors);
					}
					monitors.add(mainMonitor);
				}

				TempRef_e.disable = SafeEnum_timestamp;
				SafeEnum_timestamp++;
			}

			SafeEnum_v_e_Map_cachekey_0 = TempRef_v;
			SafeEnum_v_e_Map_cachekey_1 = TempRef_e;
			SafeEnum_v_e_Map_cachenode = mainMonitor;
		}

		mainMonitor.MOP_loc = thisJoinPoint.getSourceLocation().toString();
		mainMonitor.Prop_1_event_create(v, e);
		if(mainMonitor.Prop_1_Category_violation) {
			mainMonitor.Prop_1_handler_violation(v, e);
		}
		SafeEnum_MOPLock.unlock();
	}

	pointcut SafeEnum_updatesource(Vector v) : ((call(* Vector+.remove*(..)) || call(* Vector+.add*(..)) || call(* Vector+.clear(..)) || call(* Vector+.insertElementAt(..)) || call(* Vector+.set*(..)) || call(* Vector+.retainAll(..))) && target(v)) && MOP_CommonPointCut();
	after (Vector v) : SafeEnum_updatesource(v) {
		SafeEnum_activated = true;
		while (!SafeEnum_MOPLock.tryLock()) {
			Thread.yield();
		}
		SafeEnumMonitor mainMonitor = null;
		rvmonitorrt.map.MOPMap mainMap = null;
		SafeEnumMonitor_Set mainSet = null;
		rvmonitorrt.ref.MOPTagWeakReference TempRef_v;

		// Cache Retrieval
		if (v == SafeEnum_v_Map_cachekey_0.get()) {
			TempRef_v = SafeEnum_v_Map_cachekey_0;

			mainSet = SafeEnum_v_Map_cacheset;
			mainMonitor = SafeEnum_v_Map_cachenode;
		} else {
			TempRef_v = SafeEnum_Vector_RefMap.getTagRef(v);
		}

		if (mainSet == null || mainMonitor == null) {
			mainMap = SafeEnum_v_e_Map;
			mainMonitor = (SafeEnumMonitor)mainMap.getNode(TempRef_v);
			mainSet = (SafeEnumMonitor_Set)mainMap.getSet(TempRef_v);
			if (mainSet == null){
				mainSet = new SafeEnumMonitor_Set();
				mainMap.putSet(TempRef_v, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new SafeEnumMonitor();
				mainMonitor.monitorInfo = new rvmonitorrt.MOPMonitorInfo();
				mainMonitor.monitorInfo.isFullParam = false;

				mainMonitor.MOPRef_v = TempRef_v;

				SafeEnum_v_e_Map.putNode(TempRef_v, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = SafeEnum_timestamp;
				if (TempRef_v.tau == -1){
					TempRef_v.tau = SafeEnum_timestamp;
				}
				SafeEnum_timestamp++;

				SafeEnum__To__v_Set.add(mainMonitor);
			}

			SafeEnum_v_Map_cachekey_0 = TempRef_v;
			SafeEnum_v_Map_cacheset = mainSet;
			SafeEnum_v_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.MOP_loc = thisJoinPoint.getSourceLocation().toString();
			mainSet.event_updatesource(v);
		}
		SafeEnum_MOPLock.unlock();
	}

	pointcut SafeEnum_next(Enumeration e) : (call(* Enumeration+.nextElement()) && target(e)) && MOP_CommonPointCut();
	before (Enumeration e) : SafeEnum_next(e) {
		SafeEnum_activated = true;
		while (!SafeEnum_MOPLock.tryLock()) {
			Thread.yield();
		}
		Object obj;
		rvmonitorrt.map.MOPMap tempMap;
		SafeEnumMonitor mainMonitor = null;
		SafeEnumMonitor origMonitor = null;
		SafeEnumMonitor lastMonitor = null;
		rvmonitorrt.map.MOPMap mainMap = null;
		rvmonitorrt.map.MOPMap lastMap = null;
		SafeEnumMonitor_Set mainSet = null;
		SafeEnumMonitor_Set origSet = null;
		SafeEnumMonitor_Set monitors = null;
		rvmonitorrt.ref.MOPTagWeakReference TempRef_v;
		rvmonitorrt.ref.MOPTagWeakReference TempRef_e;

		// Cache Retrieval
		if (e == SafeEnum_e_Map_cachekey_1.get()) {
			TempRef_e = SafeEnum_e_Map_cachekey_1;

			mainSet = SafeEnum_e_Map_cacheset;
			mainMonitor = SafeEnum_e_Map_cachenode;
		} else {
			TempRef_e = SafeEnum_Enumeration_RefMap.getTagRef(e);
		}

		if (mainSet == null || mainMonitor == null) {
			mainMap = SafeEnum_e_Map;
			mainMonitor = (SafeEnumMonitor)mainMap.getNode(TempRef_e);
			mainSet = (SafeEnumMonitor_Set)mainMap.getSet(TempRef_e);
			if (mainSet == null){
				mainSet = new SafeEnumMonitor_Set();
				mainMap.putSet(TempRef_e, mainSet);
			}

			if (mainMonitor == null) {
				origSet = SafeEnum__To__v_Set;
				if (origSet!= null) {
					int numAlive = 0;
					for(int i = 0; i < origSet.size; i++) {
						origMonitor = origSet.elementData[i];
						Vector v = (Vector)origMonitor.MOPRef_v.get();
						if (!origMonitor.MOP_terminated && v != null) {
							origSet.elementData[numAlive] = origMonitor;
							numAlive++;

							TempRef_v = origMonitor.MOPRef_v;

							tempMap = SafeEnum_v_e_Map;
							obj = tempMap.getMap(TempRef_v);
							if (obj == null) {
								obj = new rvmonitorrt.map.MOPMapOfMonitor(1);
								tempMap.putMap(TempRef_v, obj);
							}
							lastMap = (rvmonitorrt.map.MOPAbstractMap)obj;
							lastMonitor = (SafeEnumMonitor)lastMap.getNode(TempRef_e);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_e.disable > origMonitor.tau|| (TempRef_e.tau > 0 && TempRef_e.tau < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeEnumMonitor)origMonitor.clone();
									lastMonitor.MOPRef_e = TempRef_e;
									if (TempRef_e.tau == -1){
										TempRef_e.tau = origMonitor.tau;
									}
									lastMap.putNode(TempRef_e, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									tempMap = SafeEnum_v_e_Map;
									obj = tempMap.getSet(TempRef_v);
									monitors = (SafeEnumMonitor_Set)obj;
									if (monitors == null) {
										monitors = new SafeEnumMonitor_Set();
										tempMap.putSet(TempRef_v, monitors);
									}
									monitors.add(lastMonitor);

									mainMap = SafeEnum_e_Map;
									obj = mainMap.getSet(TempRef_e);
									mainSet = (SafeEnumMonitor_Set)obj;
									if (mainSet == null) {
										mainSet = new SafeEnumMonitor_Set();
										mainMap.putSet(TempRef_e, mainSet);
									}
									mainSet.add(lastMonitor);
								}
							}
						}
					}

					for(int i = numAlive; i < origSet.size; i++) {
						origSet.elementData[i] = null;
					}
					origSet.size = numAlive;
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeEnumMonitor();
					mainMonitor.monitorInfo = new rvmonitorrt.MOPMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.MOPRef_e = TempRef_e;

					SafeEnum_e_Map.putNode(TempRef_e, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = SafeEnum_timestamp;
					if (TempRef_e.tau == -1){
						TempRef_e.tau = SafeEnum_timestamp;
					}
					SafeEnum_timestamp++;
				}

				TempRef_e.disable = SafeEnum_timestamp;
				SafeEnum_timestamp++;
			}

			SafeEnum_e_Map_cachekey_1 = TempRef_e;
			SafeEnum_e_Map_cacheset = mainSet;
			SafeEnum_e_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.MOP_loc = thisJoinPoint.getSourceLocation().toString();
			mainSet.event_next(e);
		}
		SafeEnum_MOPLock.unlock();
	}

}
