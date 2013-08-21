package mop;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.lang.ref.*;
import com.runtimeverification.rvmonitor.java.rt.*;
import com.runtimeverification.rvmonitor.java.rt.ref.*;
import com.runtimeverification.rvmonitor.java.rt.table.*;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IBucketNode;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple2;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple3;

class UnsafeMapIteratorMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<UnsafeMapIteratorMonitor> {
	boolean matchProp1;

	UnsafeMapIteratorMonitor_Set(){
		this.size = 0;
		this.elements = new UnsafeMapIteratorMonitor[4];
	}

	final void event_createColl(Map map, Collection c) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_createColl(map, c);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(map, c, null);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_createIter(Collection c, Iterator i) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_createIter(c, i);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(null, c, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_useIter(Iterator i) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_useIter(i);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(null, null, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_updateMap(Map map) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_updateMap(map);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(map, null, null);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
}

class UnsafeMapIteratorMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	long tau = -1;
	protected Object clone() {
		try {
			UnsafeMapIteratorMonitor ret = (UnsafeMapIteratorMonitor) super.clone();
			ret.monitorInfo = (com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo)this.monitorInfo.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_createColl[] = {2, 5, 5, 5, 5, 5};;
	static final int Prop_1_transition_createIter[] = {5, 5, 4, 5, 5, 5};;
	static final int Prop_1_transition_useIter[] = {5, 5, 5, 1, 4, 5};;
	static final int Prop_1_transition_updateMap[] = {5, 5, 2, 3, 3, 5};;

	boolean Prop_1_Category_match = false;

	UnsafeMapIteratorMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_createColl(Map map, Collection c) {
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_createColl[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 1;
		}
		return true;
	}

	final boolean Prop_1_event_createIter(Collection c, Iterator i) {
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_createIter[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 1;
		}
		return true;
	}

	final boolean Prop_1_event_useIter(Iterator i) {
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_useIter[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 1;
		}
		return true;
	}

	final boolean Prop_1_event_updateMap(Map map) {
		RVM_lastevent = 3;

		Prop_1_state = Prop_1_transition_updateMap[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 1;
		}
		return true;
	}

	final void Prop_1_handler_match (Map map, Collection c, Iterator i){
		{
			System.out.println("unsafe iterator usage!");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	CachedTagWeakReference RVMRef_map;
	CachedTagWeakReference RVMRef_c;
	CachedTagWeakReference RVMRef_i;

	//alive_parameters_0 = [Map map, Collection c, Iterator i]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Map map, Iterator i]
	boolean alive_parameters_1 = true;
	//alive_parameters_2 = [Iterator i]
	boolean alive_parameters_2 = true;

	@Override
	protected final void terminateInternal(int idnum) {
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			break;
			case 2:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			alive_parameters_2 = false;
			break;
		}
		switch(RVM_lastevent) {
			case -1:
			return;
			case 0:
			//createColl
			//alive_map && alive_c && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//createIter
			//alive_map && alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//useIter
			//alive_map && alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 3:
			//updateMap
			//alive_i
			if(!(alive_parameters_2)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

	com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo monitorInfo;
}

public class UnsafeMapIteratorRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean matchProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager UnsafeMapIteratorMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		UnsafeMapIteratorMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		UnsafeMapIteratorMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock UnsafeMapIterator_RVMLock = new ReentrantLock();
	static Condition UnsafeMapIterator_RVMLock_cond = UnsafeMapIterator_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long UnsafeMapIterator_timestamp = 1;

	private static boolean UnsafeMapIterator_activated = false;

	// Declarations for Indexing Trees
	static MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> UnsafeMapIterator_map_c_i_Map = new MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(0);
	static CachedTagWeakReference UnsafeMapIterator_map_c_i_Map_cachekey_0 = null;
	static CachedTagWeakReference UnsafeMapIterator_map_c_i_Map_cachekey_1 = null;
	static CachedTagWeakReference UnsafeMapIterator_map_c_i_Map_cachekey_2 = null;
	static UnsafeMapIteratorMonitor UnsafeMapIterator_map_c_i_Map_cachenode = null;
	static CachedTagWeakReference UnsafeMapIterator_map_Map_cachekey_0 = null;
	static UnsafeMapIteratorMonitor_Set UnsafeMapIterator_map_Map_cacheset = null;
	static UnsafeMapIteratorMonitor UnsafeMapIterator_map_Map_cachenode = null;
	static MapOfAll<MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> UnsafeMapIterator_c_i_Map = new MapOfAll<MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(1);
	static CachedTagWeakReference UnsafeMapIterator_c_i_Map_cachekey_1 = null;
	static CachedTagWeakReference UnsafeMapIterator_c_i_Map_cachekey_2 = null;
	static UnsafeMapIteratorMonitor_Set UnsafeMapIterator_c_i_Map_cacheset = null;
	static UnsafeMapIteratorMonitor UnsafeMapIterator_c_i_Map_cachenode = null;
	static CachedTagWeakReference UnsafeMapIterator_map_c_Map_cachekey_0 = null;
	static CachedTagWeakReference UnsafeMapIterator_map_c_Map_cachekey_1 = null;
	static UnsafeMapIteratorMonitor_Set UnsafeMapIterator_map_c_Map_cacheset = null;
	static UnsafeMapIteratorMonitor UnsafeMapIterator_map_c_Map_cachenode = null;
	static MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> UnsafeMapIterator_i_Map = new MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(2);
	static CachedTagWeakReference UnsafeMapIterator_i_Map_cachekey_2 = null;
	static UnsafeMapIteratorMonitor_Set UnsafeMapIterator_i_Map_cacheset = null;
	static UnsafeMapIteratorMonitor UnsafeMapIterator_i_Map_cachenode = null;
	static MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> UnsafeMapIterator_c__To__map_c_Map = new MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(1);

	// Trees for References
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap UnsafeMapIterator_Collection_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap UnsafeMapIterator_Iterator_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap UnsafeMapIterator_Map_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

	public static void createCollEvent(Map map, Collection c) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		UnsafeMapIteratorMonitor mainMonitor = null;
		UnsafeMapIteratorMonitor_Set mainSet = null;
		UnsafeMapIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_map;
		CachedTagWeakReference TempRef_c;

		// Cache Retrieval
		if (UnsafeMapIterator_map_c_Map_cachekey_0 != null && map == UnsafeMapIterator_map_c_Map_cachekey_0.get() && UnsafeMapIterator_map_c_Map_cachekey_1 != null && c == UnsafeMapIterator_map_c_Map_cachekey_1.get()) {
			TempRef_map = UnsafeMapIterator_map_c_Map_cachekey_0;
			TempRef_c = UnsafeMapIterator_map_c_Map_cachekey_1;

			mainSet = UnsafeMapIterator_map_c_Map_cacheset;
			mainMonitor = UnsafeMapIterator_map_c_Map_cachenode;
		} else {
			TempRef_map = UnsafeMapIterator_Map_RefMap.findOrCreateWeakRef(map);
			TempRef_c = UnsafeMapIterator_Collection_RefMap.findOrCreateWeakRef(c);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap1 = UnsafeMapIterator_map_c_i_Map;
			MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> obj1 = tempMap1.getMap(TempRef_map);
			if (obj1 == null) {
				obj1 = new MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(1);
				tempMap1.putMap(TempRef_map, obj1);
			}
			MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> mainMap = obj1;
			mainMonitor = mainMap.getLeaf(TempRef_c);
			mainSet = mainMap.getSet(TempRef_c);
			if (mainSet == null){
				mainSet = new UnsafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_c, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new UnsafeMapIteratorMonitor();
				mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				mainMonitor.monitorInfo.isFullParam = false;

				mainMonitor.RVMRef_map = TempRef_map;
				mainMonitor.RVMRef_c = TempRef_c;

				mainMap.putLeaf(TempRef_c, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = UnsafeMapIterator_timestamp;
				if (TempRef_map.getTau() == -1){
					TempRef_map.setTau(UnsafeMapIterator_timestamp);
				}
				if (TempRef_c.getTau() == -1){
					TempRef_c.setTau(UnsafeMapIterator_timestamp);
				}
				UnsafeMapIterator_timestamp++;

				MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap2 = UnsafeMapIterator_map_c_i_Map;
				UnsafeMapIteratorMonitor_Set obj2 = tempMap2.getSet(TempRef_map);
				monitors = obj2;
				if (monitors == null) {
					monitors = new UnsafeMapIteratorMonitor_Set();
					tempMap2.putSet(TempRef_map, monitors);
				}
				monitors.add(mainMonitor);

				MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap3 = UnsafeMapIterator_c__To__map_c_Map;
				UnsafeMapIteratorMonitor_Set obj3 = tempMap3.getSet(TempRef_c);
				monitors = obj3;
				if (monitors == null) {
					monitors = new UnsafeMapIteratorMonitor_Set();
					tempMap3.putSet(TempRef_c, monitors);
				}
				monitors.add(mainMonitor);
			}

			UnsafeMapIterator_map_c_Map_cachekey_0 = TempRef_map;
			UnsafeMapIterator_map_c_Map_cachekey_1 = TempRef_c;
			UnsafeMapIterator_map_c_Map_cacheset = mainSet;
			UnsafeMapIterator_map_c_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_createColl(map, c);
			matchProp1 = mainSet.matchProp1;
		}
		UnsafeMapIterator_RVMLock.unlock();
	}

	public static void createIterEvent(Collection c, Iterator i) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		UnsafeMapIteratorMonitor mainMonitor = null;
		UnsafeMapIteratorMonitor origMonitor = null;
		UnsafeMapIteratorMonitor lastMonitor = null;
		UnsafeMapIteratorMonitor_Set mainSet = null;
		UnsafeMapIteratorMonitor_Set origSet = null;
		UnsafeMapIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_map;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (UnsafeMapIterator_c_i_Map_cachekey_1 != null && c == UnsafeMapIterator_c_i_Map_cachekey_1.get() && UnsafeMapIterator_c_i_Map_cachekey_2 != null && i == UnsafeMapIterator_c_i_Map_cachekey_2.get()) {
			TempRef_c = UnsafeMapIterator_c_i_Map_cachekey_1;
			TempRef_i = UnsafeMapIterator_c_i_Map_cachekey_2;

			mainSet = UnsafeMapIterator_c_i_Map_cacheset;
			mainMonitor = UnsafeMapIterator_c_i_Map_cachenode;
		} else {
			TempRef_c = UnsafeMapIterator_Collection_RefMap.findOrCreateWeakRef(c);
			TempRef_i = UnsafeMapIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap1 = UnsafeMapIterator_c_i_Map;
			MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> obj1 = tempMap1.getMap(TempRef_c);
			if (obj1 == null) {
				obj1 = new MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(2);
				tempMap1.putMap(TempRef_c, obj1);
			}
			MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> mainMap = obj1;
			mainMonitor = mainMap.getLeaf(TempRef_i);
			mainSet = mainMap.getSet(TempRef_i);
			if (mainSet == null){
				mainSet = new UnsafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_i, mainSet);
			}

			if (mainMonitor == null) {
				MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> origMap1 = UnsafeMapIterator_c__To__map_c_Map;
				origSet = origMap1.getSet(TempRef_c);
				if (origSet!= null) {
					int numAlive = 0;
					for(int i_1 = 0; i_1 < origSet.getSize(); i_1++) {
						origMonitor = origSet.get(i_1);
						Map map = (Map)origMonitor.RVMRef_map.get();
						if (!origMonitor.isTerminated() && map != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_map = origMonitor.RVMRef_map;

							MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap2 = UnsafeMapIterator_map_c_i_Map;
							MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> obj2 = tempMap2.getMap(TempRef_map);
							if (obj2 == null) {
								obj2 = new MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(1);
								tempMap2.putMap(TempRef_map, obj2);
							}
							MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap3 = obj2;
							MapOfMonitor<UnsafeMapIteratorMonitor> obj3 = tempMap3.getMap(TempRef_c);
							if (obj3 == null) {
								obj3 = new MapOfMonitor<UnsafeMapIteratorMonitor>(2);
								tempMap3.putMap(TempRef_c, obj3);
							}
							MapOfMonitor<UnsafeMapIteratorMonitor> lastMap1 = obj3;
							lastMonitor = lastMap1.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_i.getDisabled() > origMonitor.tau|| (TempRef_i.getTau() > 0 && TempRef_i.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (UnsafeMapIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_i = TempRef_i;
									if (TempRef_i.getTau() == -1){
										TempRef_i.setTau(origMonitor.tau);
									}
									lastMap1.putLeaf(TempRef_i, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap4 = UnsafeMapIterator_map_c_i_Map;
									UnsafeMapIteratorMonitor_Set obj4 = tempMap4.getSet(TempRef_map);
									monitors = obj4;
									if (monitors == null) {
										monitors = new UnsafeMapIteratorMonitor_Set();
										tempMap4.putSet(TempRef_map, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap5 = UnsafeMapIterator_c_i_Map;
									MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> obj5 = tempMap5.getMap(TempRef_c);
									if (obj5 == null) {
										obj5 = new MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(1);
										tempMap5.putMap(TempRef_c, obj5);
									}
									MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap6 = obj5;
									UnsafeMapIteratorMonitor_Set obj6 = tempMap6.getSet(TempRef_i);
									mainSet = obj6;
									if (mainSet == null) {
										mainSet = new UnsafeMapIteratorMonitor_Set();
										tempMap6.putSet(TempRef_i, mainSet);
									}
									mainSet.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap7 = UnsafeMapIterator_map_c_i_Map;
									MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> obj7 = tempMap7.getMap(TempRef_map);
									if (obj7 == null) {
										obj7 = new MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>(0);
										tempMap7.putMap(TempRef_map, obj7);
									}
									MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap8 = obj7;
									UnsafeMapIteratorMonitor_Set obj8 = tempMap8.getSet(TempRef_c);
									monitors = obj8;
									if (monitors == null) {
										monitors = new UnsafeMapIteratorMonitor_Set();
										tempMap8.putSet(TempRef_c, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap9 = UnsafeMapIterator_i_Map;
									UnsafeMapIteratorMonitor_Set obj9 = tempMap9.getSet(TempRef_i);
									monitors = obj9;
									if (monitors == null) {
										monitors = new UnsafeMapIteratorMonitor_Set();
										tempMap9.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				if (mainMonitor == null) {
					mainMonitor = new UnsafeMapIteratorMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.RVMRef_c = TempRef_c;
					mainMonitor.RVMRef_i = TempRef_i;

					mainMap.putLeaf(TempRef_i, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = UnsafeMapIterator_timestamp;
					if (TempRef_c.getTau() == -1){
						TempRef_c.setTau(UnsafeMapIterator_timestamp);
					}
					if (TempRef_i.getTau() == -1){
						TempRef_i.setTau(UnsafeMapIterator_timestamp);
					}
					UnsafeMapIterator_timestamp++;

					MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> tempMap10 = UnsafeMapIterator_i_Map;
					UnsafeMapIteratorMonitor_Set obj10 = tempMap10.getSet(TempRef_i);
					monitors = obj10;
					if (monitors == null) {
						monitors = new UnsafeMapIteratorMonitor_Set();
						tempMap10.putSet(TempRef_i, monitors);
					}
					monitors.add(mainMonitor);
				}

				TempRef_i.setDisabled(UnsafeMapIterator_timestamp);
				UnsafeMapIterator_timestamp++;
			}

			UnsafeMapIterator_c_i_Map_cachekey_1 = TempRef_c;
			UnsafeMapIterator_c_i_Map_cachekey_2 = TempRef_i;
			UnsafeMapIterator_c_i_Map_cacheset = mainSet;
			UnsafeMapIterator_c_i_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_createIter(c, i);
			matchProp1 = mainSet.matchProp1;
		}
		UnsafeMapIterator_RVMLock.unlock();
	}

	public static void useIterEvent(Iterator i) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		UnsafeMapIteratorMonitor mainMonitor = null;
		UnsafeMapIteratorMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (UnsafeMapIterator_i_Map_cachekey_2 != null && i == UnsafeMapIterator_i_Map_cachekey_2.get()) {
			TempRef_i = UnsafeMapIterator_i_Map_cachekey_2;

			mainSet = UnsafeMapIterator_i_Map_cacheset;
			mainMonitor = UnsafeMapIterator_i_Map_cachenode;
		} else {
			TempRef_i = UnsafeMapIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfSetMonitor<UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> mainMap = UnsafeMapIterator_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);
			mainSet = mainMap.getSet(TempRef_i);
			if (mainSet == null){
				mainSet = new UnsafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_i, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new UnsafeMapIteratorMonitor();
				mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				mainMonitor.monitorInfo.isFullParam = false;

				mainMonitor.RVMRef_i = TempRef_i;

				UnsafeMapIterator_i_Map.putLeaf(TempRef_i, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = UnsafeMapIterator_timestamp;
				if (TempRef_i.getTau() == -1){
					TempRef_i.setTau(UnsafeMapIterator_timestamp);
				}
				UnsafeMapIterator_timestamp++;

				TempRef_i.setDisabled(UnsafeMapIterator_timestamp);
				UnsafeMapIterator_timestamp++;
			}

			UnsafeMapIterator_i_Map_cachekey_2 = TempRef_i;
			UnsafeMapIterator_i_Map_cacheset = mainSet;
			UnsafeMapIterator_i_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_useIter(i);
			matchProp1 = mainSet.matchProp1;
		}
		UnsafeMapIterator_RVMLock.unlock();
	}

	public static void updateMapEvent(Map map) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		UnsafeMapIteratorMonitor mainMonitor = null;
		UnsafeMapIteratorMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_map;

		// Cache Retrieval
		if (UnsafeMapIterator_map_Map_cachekey_0 != null && map == UnsafeMapIterator_map_Map_cachekey_0.get()) {
			TempRef_map = UnsafeMapIterator_map_Map_cachekey_0;

			mainSet = UnsafeMapIterator_map_Map_cacheset;
			mainMonitor = UnsafeMapIterator_map_Map_cachenode;
		} else {
			TempRef_map = UnsafeMapIterator_Map_RefMap.findOrCreateWeakRef(map);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfAll<MapOfMonitor<UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor>, UnsafeMapIteratorMonitor_Set, UnsafeMapIteratorMonitor> mainMap = UnsafeMapIterator_map_c_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_map);
			mainSet = mainMap.getSet(TempRef_map);
			if (mainSet == null){
				mainSet = new UnsafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_map, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new UnsafeMapIteratorMonitor();
				mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				mainMonitor.monitorInfo.isFullParam = false;

				mainMonitor.RVMRef_map = TempRef_map;

				UnsafeMapIterator_map_c_i_Map.putLeaf(TempRef_map, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = UnsafeMapIterator_timestamp;
				if (TempRef_map.getTau() == -1){
					TempRef_map.setTau(UnsafeMapIterator_timestamp);
				}
				UnsafeMapIterator_timestamp++;
			}

			UnsafeMapIterator_map_Map_cachekey_0 = TempRef_map;
			UnsafeMapIterator_map_Map_cacheset = mainSet;
			UnsafeMapIterator_map_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_updateMap(map);
			matchProp1 = mainSet.matchProp1;
		}
		UnsafeMapIterator_RVMLock.unlock();
	}

}
