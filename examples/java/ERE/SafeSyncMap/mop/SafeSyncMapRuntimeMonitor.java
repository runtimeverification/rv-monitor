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

class SafeSyncMapMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeSyncMapMonitor> {
	boolean matchProp1;

	SafeSyncMapMonitor_Set(){
		this.size = 0;
		this.elements = new SafeSyncMapMonitor[4];
	}

	final void event_sync(Map syncMap) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_sync(syncMap);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(syncMap, null, null);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_createSet(Map syncMap, Set mapSet) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_createSet(syncMap, mapSet);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(syncMap, mapSet, null);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_syncCreateIter(Set mapSet, Iterator iter) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_syncCreateIter(mapSet, iter);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(null, mapSet, iter);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_asyncCreateIter(Set mapSet, Iterator iter) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_asyncCreateIter(mapSet, iter);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(null, mapSet, iter);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_accessIter(Iterator iter) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_accessIter(iter);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(null, null, iter);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
}

class SafeSyncMapMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	long tau = -1;
	protected Object clone() {
		try {
			SafeSyncMapMonitor ret = (SafeSyncMapMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	Map c;

	int Prop_1_state;
	static final int Prop_1_transition_sync[] = {4, 5, 5, 5, 5, 5};;
	static final int Prop_1_transition_createSet[] = {5, 5, 5, 5, 3, 5};;
	static final int Prop_1_transition_syncCreateIter[] = {5, 5, 5, 2, 5, 5};;
	static final int Prop_1_transition_asyncCreateIter[] = {5, 5, 5, 1, 5, 5};;
	static final int Prop_1_transition_accessIter[] = {5, 5, 1, 5, 5, 5};;

	boolean Prop_1_Category_match = false;

	SafeSyncMapMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_sync(Map syncMap) {
		{
			this.c = syncMap;
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_sync[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 1;
		return true;
	}

	final boolean Prop_1_event_createSet(Map syncMap, Set mapSet) {
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_createSet[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 1;
		return true;
	}

	final boolean Prop_1_event_syncCreateIter(Set mapSet, Iterator iter) {
		{
			if (!Thread.holdsLock(c)) return true;
		}
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_syncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 1;
		return true;
	}

	final boolean Prop_1_event_asyncCreateIter(Set mapSet, Iterator iter) {
		{
			if (Thread.holdsLock(c)) return true;
		}
		RVM_lastevent = 3;

		Prop_1_state = Prop_1_transition_asyncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 1;
		return true;
	}

	final boolean Prop_1_event_accessIter(Iterator iter) {
		{
			if (Thread.holdsLock(c)) return true;
		}
		RVM_lastevent = 4;

		Prop_1_state = Prop_1_transition_accessIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 1;
		return true;
	}

	final void Prop_1_handler_match (Map syncMap, Set mapSet, Iterator iter){
		{
			System.out.println("synchronized collection accessed in non threadsafe manner!");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	CachedTagWeakReference RVMRef_syncMap;
	CachedTagWeakReference RVMRef_mapSet;
	CachedTagWeakReference RVMRef_iter;

	//alive_parameters_0 = [Map syncMap, Set mapSet, Iterator iter]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Set mapSet, Iterator iter]
	boolean alive_parameters_1 = true;
	//alive_parameters_2 = [Iterator iter]
	boolean alive_parameters_2 = true;

	@Override
	protected final void terminateInternal(int idnum) {
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
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
			//sync
			//alive_syncMap && alive_mapSet && alive_iter
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//createSet
			//alive_mapSet && alive_iter
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//syncCreateIter
			//alive_iter
			if(!(alive_parameters_2)){
				RVM_terminated = true;
				return;
			}
			break;

			case 3:
			//asyncCreateIter
			return;
			case 4:
			//accessIter
			return;
		}
		return;
	}

}

public class SafeSyncMapRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean matchProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeSyncMapMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		SafeSyncMapMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeSyncMapMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock SafeSyncMap_RVMLock = new ReentrantLock();
	static Condition SafeSyncMap_RVMLock_cond = SafeSyncMap_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeSyncMap_timestamp = 1;

	private static boolean SafeSyncMap_activated = false;

	// Declarations for Indexing Trees
	static MapOfAll<MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> SafeSyncMap_mapSet_iter_Map = new MapOfAll<MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(1);
	static CachedTagWeakReference SafeSyncMap_mapSet_iter_Map_cachekey_1 = null;
	static CachedTagWeakReference SafeSyncMap_mapSet_iter_Map_cachekey_2 = null;
	static SafeSyncMapMonitor_Set SafeSyncMap_mapSet_iter_Map_cacheset = null;
	static SafeSyncMapMonitor SafeSyncMap_mapSet_iter_Map_cachenode = null;
	static CachedTagWeakReference SafeSyncMap_syncMap_Map_cachekey_0 = null;
	static SafeSyncMapMonitor_Set SafeSyncMap_syncMap_Map_cacheset = null;
	static SafeSyncMapMonitor SafeSyncMap_syncMap_Map_cachenode = null;
	static MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> SafeSyncMap_syncMap_mapSet_iter_Map = new MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(0);
	static CachedTagWeakReference SafeSyncMap_syncMap_mapSet_iter_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeSyncMap_syncMap_mapSet_iter_Map_cachekey_1 = null;
	static CachedTagWeakReference SafeSyncMap_syncMap_mapSet_iter_Map_cachekey_2 = null;
	static SafeSyncMapMonitor SafeSyncMap_syncMap_mapSet_iter_Map_cachenode = null;
	static CachedTagWeakReference SafeSyncMap_syncMap_mapSet_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeSyncMap_syncMap_mapSet_Map_cachekey_1 = null;
	static SafeSyncMapMonitor_Set SafeSyncMap_syncMap_mapSet_Map_cacheset = null;
	static SafeSyncMapMonitor SafeSyncMap_syncMap_mapSet_Map_cachenode = null;
	static MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> SafeSyncMap_iter_Map = new MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(2);
	static CachedTagWeakReference SafeSyncMap_iter_Map_cachekey_2 = null;
	static SafeSyncMapMonitor_Set SafeSyncMap_iter_Map_cacheset = null;
	static SafeSyncMapMonitor SafeSyncMap_iter_Map_cachenode = null;
	static MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> SafeSyncMap_mapSet__To__syncMap_mapSet_Map = new MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(1);

	// Trees for References
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeSyncMap_Iterator_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeSyncMap_Map_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeSyncMap_Set_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

	public static void syncEvent(Map syncMap) {
		SafeSyncMap_activated = true;
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeSyncMapMonitor mainMonitor = null;
		SafeSyncMapMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_syncMap;

		// Cache Retrieval
		if (SafeSyncMap_syncMap_Map_cachekey_0 != null && syncMap == SafeSyncMap_syncMap_Map_cachekey_0.get()) {
			TempRef_syncMap = SafeSyncMap_syncMap_Map_cachekey_0;

			mainSet = SafeSyncMap_syncMap_Map_cacheset;
			mainMonitor = SafeSyncMap_syncMap_Map_cachenode;
		} else {
			TempRef_syncMap = SafeSyncMap_Map_RefMap.findOrCreateWeakRef(syncMap);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> mainMap = SafeSyncMap_syncMap_mapSet_iter_Map;
			mainMonitor = mainMap.getLeaf(TempRef_syncMap);
			mainSet = mainMap.getSet(TempRef_syncMap);
			if (mainSet == null){
				mainSet = new SafeSyncMapMonitor_Set();
				mainMap.putSet(TempRef_syncMap, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new SafeSyncMapMonitor();

				mainMonitor.RVMRef_syncMap = TempRef_syncMap;

				SafeSyncMap_syncMap_mapSet_iter_Map.putLeaf(TempRef_syncMap, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = SafeSyncMap_timestamp;
				if (TempRef_syncMap.getTau() == -1){
					TempRef_syncMap.setTau(SafeSyncMap_timestamp);
				}
				SafeSyncMap_timestamp++;
			}

			SafeSyncMap_syncMap_Map_cachekey_0 = TempRef_syncMap;
			SafeSyncMap_syncMap_Map_cacheset = mainSet;
			SafeSyncMap_syncMap_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_sync(syncMap);
			matchProp1 = mainSet.matchProp1;
		}
		SafeSyncMap_RVMLock.unlock();
	}

	public static void createSetEvent(Map syncMap, Set mapSet) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}
		if (SafeSyncMap_activated) {
			SafeSyncMapMonitor mainMonitor = null;
			SafeSyncMapMonitor origMonitor = null;
			SafeSyncMapMonitor_Set mainSet = null;
			SafeSyncMapMonitor_Set monitors = null;
			CachedTagWeakReference TempRef_syncMap;
			CachedTagWeakReference TempRef_mapSet;

			// Cache Retrieval
			if (SafeSyncMap_syncMap_mapSet_Map_cachekey_0 != null && syncMap == SafeSyncMap_syncMap_mapSet_Map_cachekey_0.get() && SafeSyncMap_syncMap_mapSet_Map_cachekey_1 != null && mapSet == SafeSyncMap_syncMap_mapSet_Map_cachekey_1.get()) {
				TempRef_syncMap = SafeSyncMap_syncMap_mapSet_Map_cachekey_0;
				TempRef_mapSet = SafeSyncMap_syncMap_mapSet_Map_cachekey_1;

				mainSet = SafeSyncMap_syncMap_mapSet_Map_cacheset;
				mainMonitor = SafeSyncMap_syncMap_mapSet_Map_cachenode;
			} else {
				TempRef_syncMap = SafeSyncMap_Map_RefMap.findOrCreateWeakRef(syncMap);
				TempRef_mapSet = SafeSyncMap_Set_RefMap.findOrCreateWeakRef(mapSet);
			}

			if (mainSet == null || mainMonitor == null) {
				MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap1 = SafeSyncMap_syncMap_mapSet_iter_Map;
				MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj1 = tempMap1.getMap(TempRef_syncMap);
				if (obj1 != null) {
					MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> mainMap = obj1;
					mainMonitor = mainMap.getLeaf(TempRef_mapSet);
					mainSet = mainMap.getSet(TempRef_mapSet);
				}

				if (mainMonitor == null) {
					if (mainMonitor == null) {
						MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> origMap1 = SafeSyncMap_syncMap_mapSet_iter_Map;
						origMonitor = origMap1.getLeaf(TempRef_syncMap);
						if (origMonitor != null) {
							boolean timeCheck = true;

							if (TempRef_mapSet.getDisabled() > origMonitor.tau) {
								timeCheck = false;
							}

							if (timeCheck) {
								mainMonitor = (SafeSyncMapMonitor)origMonitor.clone();
								mainMonitor.RVMRef_mapSet = TempRef_mapSet;
								if (TempRef_mapSet.getTau() == -1){
									TempRef_mapSet.setTau(origMonitor.tau);
								}
								MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap2 = SafeSyncMap_syncMap_mapSet_iter_Map;
								MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj2 = tempMap2.getMap(TempRef_syncMap);
								if (obj2 == null) {
									obj2 = new MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(0);
									tempMap2.putMap(TempRef_syncMap, obj2);
								}
								MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap3 = obj2;
								SafeSyncMapMonitor_Set obj3 = tempMap3.getSet(TempRef_mapSet);
								mainSet = obj3;
								if (mainSet == null) {
									mainSet = new SafeSyncMapMonitor_Set();
									tempMap3.putSet(TempRef_mapSet, mainSet);
								}
								mainSet.add(mainMonitor);

								MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap4 = SafeSyncMap_syncMap_mapSet_iter_Map;
								SafeSyncMapMonitor_Set obj4 = tempMap4.getSet(TempRef_syncMap);
								monitors = obj4;
								if (monitors == null) {
									monitors = new SafeSyncMapMonitor_Set();
									tempMap4.putSet(TempRef_syncMap, monitors);
								}
								monitors.add(mainMonitor);

								MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap5 = SafeSyncMap_mapSet__To__syncMap_mapSet_Map;
								SafeSyncMapMonitor_Set obj5 = tempMap5.getSet(TempRef_mapSet);
								monitors = obj5;
								if (monitors == null) {
									monitors = new SafeSyncMapMonitor_Set();
									tempMap5.putSet(TempRef_mapSet, monitors);
								}
								monitors.add(mainMonitor);
							}
						}
					}

					TempRef_mapSet.setDisabled(SafeSyncMap_timestamp);
					SafeSyncMap_timestamp++;
				}

				SafeSyncMap_syncMap_mapSet_Map_cachekey_0 = TempRef_syncMap;
				SafeSyncMap_syncMap_mapSet_Map_cachekey_1 = TempRef_mapSet;
				SafeSyncMap_syncMap_mapSet_Map_cacheset = mainSet;
				SafeSyncMap_syncMap_mapSet_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_createSet(syncMap, mapSet);
				matchProp1 = mainSet.matchProp1;
			}
		}
		SafeSyncMap_RVMLock.unlock();
	}

	public static void syncCreateIterEvent(Set mapSet, Iterator iter) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}
		if (SafeSyncMap_activated) {
			SafeSyncMapMonitor mainMonitor = null;
			SafeSyncMapMonitor origMonitor = null;
			SafeSyncMapMonitor lastMonitor = null;
			SafeSyncMapMonitor_Set mainSet = null;
			SafeSyncMapMonitor_Set origSet = null;
			SafeSyncMapMonitor_Set monitors = null;
			CachedTagWeakReference TempRef_syncMap;
			CachedTagWeakReference TempRef_mapSet;
			CachedTagWeakReference TempRef_iter;

			// Cache Retrieval
			if (SafeSyncMap_mapSet_iter_Map_cachekey_1 != null && mapSet == SafeSyncMap_mapSet_iter_Map_cachekey_1.get() && SafeSyncMap_mapSet_iter_Map_cachekey_2 != null && iter == SafeSyncMap_mapSet_iter_Map_cachekey_2.get()) {
				TempRef_mapSet = SafeSyncMap_mapSet_iter_Map_cachekey_1;
				TempRef_iter = SafeSyncMap_mapSet_iter_Map_cachekey_2;

				mainSet = SafeSyncMap_mapSet_iter_Map_cacheset;
				mainMonitor = SafeSyncMap_mapSet_iter_Map_cachenode;
			} else {
				TempRef_mapSet = SafeSyncMap_Set_RefMap.findOrCreateWeakRef(mapSet);
				TempRef_iter = SafeSyncMap_Iterator_RefMap.findOrCreateWeakRef(iter);
			}

			if (mainSet == null || mainMonitor == null) {
				MapOfAll<MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap1 = SafeSyncMap_mapSet_iter_Map;
				MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj1 = tempMap1.getMap(TempRef_mapSet);
				if (obj1 != null) {
					MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> mainMap = obj1;
					mainMonitor = mainMap.getLeaf(TempRef_iter);
					mainSet = mainMap.getSet(TempRef_iter);
				}

				if (mainMonitor == null) {
					MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> origMap1 = SafeSyncMap_mapSet__To__syncMap_mapSet_Map;
					origSet = origMap1.getSet(TempRef_mapSet);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.getSize(); i++) {
							origMonitor = origSet.get(i);
							Map syncMap = (Map)origMonitor.RVMRef_syncMap.get();
							if (!origMonitor.isTerminated() && syncMap != null) {
								origSet.set(numAlive, origMonitor);
								numAlive++;

								TempRef_syncMap = origMonitor.RVMRef_syncMap;

								MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap2 = SafeSyncMap_syncMap_mapSet_iter_Map;
								MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj2 = tempMap2.getMap(TempRef_syncMap);
								if (obj2 == null) {
									obj2 = new MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(1);
									tempMap2.putMap(TempRef_syncMap, obj2);
								}
								MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap3 = obj2;
								MapOfMonitor<SafeSyncMapMonitor> obj3 = tempMap3.getMap(TempRef_mapSet);
								if (obj3 == null) {
									obj3 = new MapOfMonitor<SafeSyncMapMonitor>(2);
									tempMap3.putMap(TempRef_mapSet, obj3);
								}
								MapOfMonitor<SafeSyncMapMonitor> lastMap1 = obj3;
								lastMonitor = lastMap1.getLeaf(TempRef_iter);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_iter.getDisabled() > origMonitor.tau|| (TempRef_iter.getTau() > 0 && TempRef_iter.getTau() < origMonitor.tau)) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (SafeSyncMapMonitor)origMonitor.clone();
										lastMonitor.RVMRef_iter = TempRef_iter;
										if (TempRef_iter.getTau() == -1){
											TempRef_iter.setTau(origMonitor.tau);
										}
										lastMap1.putLeaf(TempRef_iter, lastMonitor);

										MapOfAll<MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap4 = SafeSyncMap_mapSet_iter_Map;
										MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj4 = tempMap4.getMap(TempRef_mapSet);
										if (obj4 == null) {
											obj4 = new MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(1);
											tempMap4.putMap(TempRef_mapSet, obj4);
										}
										MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap5 = obj4;
										SafeSyncMapMonitor_Set obj5 = tempMap5.getSet(TempRef_iter);
										mainSet = obj5;
										if (mainSet == null) {
											mainSet = new SafeSyncMapMonitor_Set();
											tempMap5.putSet(TempRef_iter, mainSet);
										}
										mainSet.add(lastMonitor);

										MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap6 = SafeSyncMap_syncMap_mapSet_iter_Map;
										SafeSyncMapMonitor_Set obj6 = tempMap6.getSet(TempRef_syncMap);
										monitors = obj6;
										if (monitors == null) {
											monitors = new SafeSyncMapMonitor_Set();
											tempMap6.putSet(TempRef_syncMap, monitors);
										}
										monitors.add(lastMonitor);

										MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap7 = SafeSyncMap_syncMap_mapSet_iter_Map;
										MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj7 = tempMap7.getMap(TempRef_syncMap);
										if (obj7 == null) {
											obj7 = new MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(0);
											tempMap7.putMap(TempRef_syncMap, obj7);
										}
										MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap8 = obj7;
										SafeSyncMapMonitor_Set obj8 = tempMap8.getSet(TempRef_mapSet);
										monitors = obj8;
										if (monitors == null) {
											monitors = new SafeSyncMapMonitor_Set();
											tempMap8.putSet(TempRef_mapSet, monitors);
										}
										monitors.add(lastMonitor);

										MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap9 = SafeSyncMap_iter_Map;
										SafeSyncMapMonitor_Set obj9 = tempMap9.getSet(TempRef_iter);
										monitors = obj9;
										if (monitors == null) {
											monitors = new SafeSyncMapMonitor_Set();
											tempMap9.putSet(TempRef_iter, monitors);
										}
										monitors.add(lastMonitor);
									}
								}
							}
						}

						origSet.eraseRange(numAlive);
					}

					TempRef_mapSet.setDisabled(SafeSyncMap_timestamp);
					TempRef_iter.setDisabled(SafeSyncMap_timestamp);
					SafeSyncMap_timestamp++;
				}

				SafeSyncMap_mapSet_iter_Map_cachekey_1 = TempRef_mapSet;
				SafeSyncMap_mapSet_iter_Map_cachekey_2 = TempRef_iter;
				SafeSyncMap_mapSet_iter_Map_cacheset = mainSet;
				SafeSyncMap_mapSet_iter_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_syncCreateIter(mapSet, iter);
				matchProp1 = mainSet.matchProp1;
			}
		}
		SafeSyncMap_RVMLock.unlock();
	}

	public static void asyncCreateIterEvent(Set mapSet, Iterator iter) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}
		if (SafeSyncMap_activated) {
			SafeSyncMapMonitor mainMonitor = null;
			SafeSyncMapMonitor origMonitor = null;
			SafeSyncMapMonitor lastMonitor = null;
			SafeSyncMapMonitor_Set mainSet = null;
			SafeSyncMapMonitor_Set origSet = null;
			SafeSyncMapMonitor_Set monitors = null;
			CachedTagWeakReference TempRef_syncMap;
			CachedTagWeakReference TempRef_mapSet;
			CachedTagWeakReference TempRef_iter;

			// Cache Retrieval
			if (SafeSyncMap_mapSet_iter_Map_cachekey_1 != null && mapSet == SafeSyncMap_mapSet_iter_Map_cachekey_1.get() && SafeSyncMap_mapSet_iter_Map_cachekey_2 != null && iter == SafeSyncMap_mapSet_iter_Map_cachekey_2.get()) {
				TempRef_mapSet = SafeSyncMap_mapSet_iter_Map_cachekey_1;
				TempRef_iter = SafeSyncMap_mapSet_iter_Map_cachekey_2;

				mainSet = SafeSyncMap_mapSet_iter_Map_cacheset;
				mainMonitor = SafeSyncMap_mapSet_iter_Map_cachenode;
			} else {
				TempRef_mapSet = SafeSyncMap_Set_RefMap.findOrCreateWeakRef(mapSet);
				TempRef_iter = SafeSyncMap_Iterator_RefMap.findOrCreateWeakRef(iter);
			}

			if (mainSet == null || mainMonitor == null) {
				MapOfAll<MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap1 = SafeSyncMap_mapSet_iter_Map;
				MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj1 = tempMap1.getMap(TempRef_mapSet);
				if (obj1 != null) {
					MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> mainMap = obj1;
					mainMonitor = mainMap.getLeaf(TempRef_iter);
					mainSet = mainMap.getSet(TempRef_iter);
				}

				if (mainMonitor == null) {
					MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> origMap1 = SafeSyncMap_mapSet__To__syncMap_mapSet_Map;
					origSet = origMap1.getSet(TempRef_mapSet);
					if (origSet!= null) {
						int numAlive = 0;
						for(int i = 0; i < origSet.getSize(); i++) {
							origMonitor = origSet.get(i);
							Map syncMap = (Map)origMonitor.RVMRef_syncMap.get();
							if (!origMonitor.isTerminated() && syncMap != null) {
								origSet.set(numAlive, origMonitor);
								numAlive++;

								TempRef_syncMap = origMonitor.RVMRef_syncMap;

								MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap2 = SafeSyncMap_syncMap_mapSet_iter_Map;
								MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj2 = tempMap2.getMap(TempRef_syncMap);
								if (obj2 == null) {
									obj2 = new MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(1);
									tempMap2.putMap(TempRef_syncMap, obj2);
								}
								MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap3 = obj2;
								MapOfMonitor<SafeSyncMapMonitor> obj3 = tempMap3.getMap(TempRef_mapSet);
								if (obj3 == null) {
									obj3 = new MapOfMonitor<SafeSyncMapMonitor>(2);
									tempMap3.putMap(TempRef_mapSet, obj3);
								}
								MapOfMonitor<SafeSyncMapMonitor> lastMap1 = obj3;
								lastMonitor = lastMap1.getLeaf(TempRef_iter);
								if (lastMonitor == null) {
									boolean timeCheck = true;

									if (TempRef_iter.getDisabled() > origMonitor.tau|| (TempRef_iter.getTau() > 0 && TempRef_iter.getTau() < origMonitor.tau)) {
										timeCheck = false;
									}

									if (timeCheck) {
										lastMonitor = (SafeSyncMapMonitor)origMonitor.clone();
										lastMonitor.RVMRef_iter = TempRef_iter;
										if (TempRef_iter.getTau() == -1){
											TempRef_iter.setTau(origMonitor.tau);
										}
										lastMap1.putLeaf(TempRef_iter, lastMonitor);

										MapOfAll<MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap4 = SafeSyncMap_mapSet_iter_Map;
										MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj4 = tempMap4.getMap(TempRef_mapSet);
										if (obj4 == null) {
											obj4 = new MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(1);
											tempMap4.putMap(TempRef_mapSet, obj4);
										}
										MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap5 = obj4;
										SafeSyncMapMonitor_Set obj5 = tempMap5.getSet(TempRef_iter);
										mainSet = obj5;
										if (mainSet == null) {
											mainSet = new SafeSyncMapMonitor_Set();
											tempMap5.putSet(TempRef_iter, mainSet);
										}
										mainSet.add(lastMonitor);

										MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap6 = SafeSyncMap_syncMap_mapSet_iter_Map;
										SafeSyncMapMonitor_Set obj6 = tempMap6.getSet(TempRef_syncMap);
										monitors = obj6;
										if (monitors == null) {
											monitors = new SafeSyncMapMonitor_Set();
											tempMap6.putSet(TempRef_syncMap, monitors);
										}
										monitors.add(lastMonitor);

										MapOfAll<MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap7 = SafeSyncMap_syncMap_mapSet_iter_Map;
										MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> obj7 = tempMap7.getMap(TempRef_syncMap);
										if (obj7 == null) {
											obj7 = new MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor>(0);
											tempMap7.putMap(TempRef_syncMap, obj7);
										}
										MapOfAll<MapOfMonitor<SafeSyncMapMonitor>, SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap8 = obj7;
										SafeSyncMapMonitor_Set obj8 = tempMap8.getSet(TempRef_mapSet);
										monitors = obj8;
										if (monitors == null) {
											monitors = new SafeSyncMapMonitor_Set();
											tempMap8.putSet(TempRef_mapSet, monitors);
										}
										monitors.add(lastMonitor);

										MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> tempMap9 = SafeSyncMap_iter_Map;
										SafeSyncMapMonitor_Set obj9 = tempMap9.getSet(TempRef_iter);
										monitors = obj9;
										if (monitors == null) {
											monitors = new SafeSyncMapMonitor_Set();
											tempMap9.putSet(TempRef_iter, monitors);
										}
										monitors.add(lastMonitor);
									}
								}
							}
						}

						origSet.eraseRange(numAlive);
					}

					TempRef_mapSet.setDisabled(SafeSyncMap_timestamp);
					TempRef_iter.setDisabled(SafeSyncMap_timestamp);
					SafeSyncMap_timestamp++;
				}

				SafeSyncMap_mapSet_iter_Map_cachekey_1 = TempRef_mapSet;
				SafeSyncMap_mapSet_iter_Map_cachekey_2 = TempRef_iter;
				SafeSyncMap_mapSet_iter_Map_cacheset = mainSet;
				SafeSyncMap_mapSet_iter_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_asyncCreateIter(mapSet, iter);
				matchProp1 = mainSet.matchProp1;
			}
		}
		SafeSyncMap_RVMLock.unlock();
	}

	public static void accessIterEvent(Iterator iter) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}
		if (SafeSyncMap_activated) {
			SafeSyncMapMonitor mainMonitor = null;
			SafeSyncMapMonitor_Set mainSet = null;
			CachedTagWeakReference TempRef_iter;

			// Cache Retrieval
			if (SafeSyncMap_iter_Map_cachekey_2 != null && iter == SafeSyncMap_iter_Map_cachekey_2.get()) {
				TempRef_iter = SafeSyncMap_iter_Map_cachekey_2;

				mainSet = SafeSyncMap_iter_Map_cacheset;
				mainMonitor = SafeSyncMap_iter_Map_cachenode;
			} else {
				TempRef_iter = SafeSyncMap_Iterator_RefMap.findOrCreateWeakRef(iter);
			}

			if (mainSet == null || mainMonitor == null) {
				MapOfSetMonitor<SafeSyncMapMonitor_Set, SafeSyncMapMonitor> mainMap = SafeSyncMap_iter_Map;
				mainMonitor = mainMap.getLeaf(TempRef_iter);
				mainSet = mainMap.getSet(TempRef_iter);

				if (mainMonitor == null) {

					TempRef_iter.setDisabled(SafeSyncMap_timestamp);
					SafeSyncMap_timestamp++;
				}

				SafeSyncMap_iter_Map_cachekey_2 = TempRef_iter;
				SafeSyncMap_iter_Map_cacheset = mainSet;
				SafeSyncMap_iter_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_accessIter(iter);
				matchProp1 = mainSet.matchProp1;
			}
		}
		SafeSyncMap_RVMLock.unlock();
	}

}
