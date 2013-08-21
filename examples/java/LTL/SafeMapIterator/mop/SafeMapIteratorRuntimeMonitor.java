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

class SafeMapIteratorMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeMapIteratorMonitor> {
	boolean violationProp1;

	SafeMapIteratorMonitor_Set(){
		this.size = 0;
		this.elements = new SafeMapIteratorMonitor[4];
	}

	final void event_createColl(Map map, Collection c) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			SafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_createColl(map, c);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(map, c, null);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_createIter(Collection c, Iterator i) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			SafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_createIter(c, i);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(null, c, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_useIter(Iterator i) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			SafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_useIter(i);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(null, null, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_updateMap(Map map) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			SafeMapIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_updateMap(map);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(map, null, null);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
}

class SafeMapIteratorMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	long tau = -1;
	protected Object clone() {
		try {
			SafeMapIteratorMonitor ret = (SafeMapIteratorMonitor) super.clone();
			ret.monitorInfo = (com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo)this.monitorInfo.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_createColl[] = {1, 1, 3, 3};;
	static final int Prop_1_transition_createIter[] = {0, 1, 3, 3};;
	static final int Prop_1_transition_useIter[] = {2, 1, 3, 3};;
	static final int Prop_1_transition_updateMap[] = {0, 0, 3, 3};;

	boolean Prop_1_Category_violation = false;

	SafeMapIteratorMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_createColl(Map map, Collection c) {
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_createColl[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
		return true;
	}

	final boolean Prop_1_event_createIter(Collection c, Iterator i) {
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_createIter[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
		return true;
	}

	final boolean Prop_1_event_useIter(Iterator i) {
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_useIter[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
		return true;
	}

	final boolean Prop_1_event_updateMap(Map map) {
		RVM_lastevent = 3;

		Prop_1_state = Prop_1_transition_updateMap[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
		return true;
	}

	final void Prop_1_handler_violation (Map map, Collection c, Iterator i){
		{
			System.out.println("unsafe iterator usage!");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_violation = false;
	}

	CachedTagWeakReference RVMRef_map;
	CachedTagWeakReference RVMRef_c;
	CachedTagWeakReference RVMRef_i;

	//alive_parameters_0 = [Map map, Iterator i]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Iterator i]
	boolean alive_parameters_1 = true;

	@Override
	protected final void terminateInternal(int idnum) {
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
			case 1:
			break;
			case 2:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
		}
		switch(RVM_lastevent) {
			case -1:
			return;
			case 0:
			//createColl
			//alive_map && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//createIter
			//alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//useIter
			//alive_map && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 3:
			//updateMap
			//alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

	com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo monitorInfo;
}

public class SafeMapIteratorRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean violationProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeMapIteratorMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		SafeMapIteratorMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeMapIteratorMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock SafeMapIterator_RVMLock = new ReentrantLock();
	static Condition SafeMapIterator_RVMLock_cond = SafeMapIterator_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeMapIterator_timestamp = 1;

	private static boolean SafeMapIterator_activated = false;

	// Declarations for Indexing Trees
	static MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> SafeMapIterator_map_c_i_Map = new MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
	static CachedTagWeakReference SafeMapIterator_map_c_i_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeMapIterator_map_c_i_Map_cachekey_1 = null;
	static CachedTagWeakReference SafeMapIterator_map_c_i_Map_cachekey_2 = null;
	static SafeMapIteratorMonitor SafeMapIterator_map_c_i_Map_cachenode = null;
	static MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> SafeMapIterator_map_i_Map = new MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
	static CachedTagWeakReference SafeMapIterator_map_i_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeMapIterator_map_i_Map_cachekey_2 = null;
	static SafeMapIteratorMonitor_Set SafeMapIterator_map_i_Map_cacheset = null;
	static SafeMapIteratorMonitor SafeMapIterator_map_i_Map_cachenode = null;
	static CachedTagWeakReference SafeMapIterator_map_Map_cachekey_0 = null;
	static SafeMapIteratorMonitor_Set SafeMapIterator_map_Map_cacheset = null;
	static SafeMapIteratorMonitor SafeMapIterator_map_Map_cachenode = null;
	static MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> SafeMapIterator_c_i_Map = new MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
	static CachedTagWeakReference SafeMapIterator_c_i_Map_cachekey_1 = null;
	static CachedTagWeakReference SafeMapIterator_c_i_Map_cachekey_2 = null;
	static SafeMapIteratorMonitor_Set SafeMapIterator_c_i_Map_cacheset = null;
	static SafeMapIteratorMonitor SafeMapIterator_c_i_Map_cachenode = null;
	static CachedTagWeakReference SafeMapIterator_map_c_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeMapIterator_map_c_Map_cachekey_1 = null;
	static SafeMapIteratorMonitor_Set SafeMapIterator_map_c_Map_cacheset = null;
	static SafeMapIteratorMonitor SafeMapIterator_map_c_Map_cachenode = null;
	static MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> SafeMapIterator_i_Map = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(2);
	static CachedTagWeakReference SafeMapIterator_i_Map_cachekey_2 = null;
	static SafeMapIteratorMonitor_Set SafeMapIterator_i_Map_cacheset = null;
	static SafeMapIteratorMonitor SafeMapIterator_i_Map_cachenode = null;
	static SafeMapIteratorMonitor_Set SafeMapIterator__To__map_Set = new SafeMapIteratorMonitor_Set();
	static MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> SafeMapIterator_c__To__map_c_Map = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
	static SafeMapIteratorMonitor_Set SafeMapIterator__To__c_i_Set = new SafeMapIteratorMonitor_Set();
	static SafeMapIteratorMonitor_Set SafeMapIterator__To__map_c_Set = new SafeMapIteratorMonitor_Set();
	static MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> SafeMapIterator_c__To__c_i_Map = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);

	// Trees for References
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeMapIterator_Collection_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeMapIterator_Iterator_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeMapIterator_Map_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

	public static void createCollEvent(Map map, Collection c) {
		SafeMapIterator_activated = true;
		while (!SafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeMapIteratorMonitor mainMonitor = null;
		SafeMapIteratorMonitor origMonitor = null;
		SafeMapIteratorMonitor lastMonitor = null;
		SafeMapIteratorMonitor_Set mainSet = null;
		SafeMapIteratorMonitor_Set origSet = null;
		SafeMapIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_map;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (SafeMapIterator_map_c_Map_cachekey_0 != null && map == SafeMapIterator_map_c_Map_cachekey_0.get() && SafeMapIterator_map_c_Map_cachekey_1 != null && c == SafeMapIterator_map_c_Map_cachekey_1.get()) {
			TempRef_map = SafeMapIterator_map_c_Map_cachekey_0;
			TempRef_c = SafeMapIterator_map_c_Map_cachekey_1;

			mainSet = SafeMapIterator_map_c_Map_cacheset;
			mainMonitor = SafeMapIterator_map_c_Map_cachenode;
		} else {
			TempRef_map = SafeMapIterator_Map_RefMap.findOrCreateWeakRef(map);
			TempRef_c = SafeMapIterator_Collection_RefMap.findOrCreateWeakRef(c);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap1 = SafeMapIterator_map_c_i_Map;
			MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj1 = tempMap1.getMap(TempRef_map);
			if (obj1 == null) {
				obj1 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
				tempMap1.putMap(TempRef_map, obj1);
			}
			MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> mainMap = obj1;
			mainMonitor = mainMap.getLeaf(TempRef_c);
			mainSet = mainMap.getSet(TempRef_c);
			if (mainSet == null){
				mainSet = new SafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_c, mainSet);
			}

			if (mainMonitor == null) {
				MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> origMap1 = SafeMapIterator_c__To__c_i_Map;
				origSet = origMap1.getSet(TempRef_c);
				if (origSet!= null) {
					int numAlive = 0;
					for(int i_1 = 0; i_1 < origSet.getSize(); i_1++) {
						origMonitor = origSet.get(i_1);
						Iterator i = (Iterator)origMonitor.RVMRef_i.get();
						if (!origMonitor.isTerminated() && i != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_i = origMonitor.RVMRef_i;

							MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap2 = SafeMapIterator_map_c_i_Map;
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj2 = tempMap2.getMap(TempRef_map);
							if (obj2 == null) {
								obj2 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
								tempMap2.putMap(TempRef_map, obj2);
							}
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap3 = obj2;
							MapOfMonitor<SafeMapIteratorMonitor> obj3 = tempMap3.getMap(TempRef_c);
							if (obj3 == null) {
								obj3 = new MapOfMonitor<SafeMapIteratorMonitor>(2);
								tempMap3.putMap(TempRef_c, obj3);
							}
							MapOfMonitor<SafeMapIteratorMonitor> lastMap1 = obj3;
							lastMonitor = lastMap1.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_map.getDisabled() > origMonitor.tau|| (TempRef_map.getTau() > 0 && TempRef_map.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeMapIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_map = TempRef_map;
									if (TempRef_map.getTau() == -1){
										TempRef_map.setTau(origMonitor.tau);
									}
									lastMap1.putLeaf(TempRef_i, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap4 = SafeMapIterator_map_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj4 = tempMap4.getMap(TempRef_map);
									if (obj4 == null) {
										obj4 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap4.putMap(TempRef_map, obj4);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap5 = obj4;
									SafeMapIteratorMonitor_Set obj5 = tempMap5.getSet(TempRef_i);
									monitors = obj5;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap5.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap6 = SafeMapIterator_map_c_i_Map;
									SafeMapIteratorMonitor_Set obj6 = tempMap6.getSet(TempRef_map);
									monitors = obj6;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap6.putSet(TempRef_map, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap7 = SafeMapIterator_c_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj7 = tempMap7.getMap(TempRef_c);
									if (obj7 == null) {
										obj7 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
										tempMap7.putMap(TempRef_c, obj7);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap8 = obj7;
									SafeMapIteratorMonitor_Set obj8 = tempMap8.getSet(TempRef_i);
									monitors = obj8;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap8.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap9 = SafeMapIterator_map_c_i_Map;
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj9 = tempMap9.getMap(TempRef_map);
									if (obj9 == null) {
										obj9 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap9.putMap(TempRef_map, obj9);
									}
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap10 = obj9;
									SafeMapIteratorMonitor_Set obj10 = tempMap10.getSet(TempRef_c);
									mainSet = obj10;
									if (mainSet == null) {
										mainSet = new SafeMapIteratorMonitor_Set();
										tempMap10.putSet(TempRef_c, mainSet);
									}
									mainSet.add(lastMonitor);

									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap11 = SafeMapIterator_i_Map;
									SafeMapIteratorMonitor_Set obj11 = tempMap11.getSet(TempRef_i);
									monitors = obj11;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap11.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				if (mainMonitor == null) {
					MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> origMap2 = SafeMapIterator_map_c_i_Map;
					origMonitor = origMap2.getLeaf(TempRef_map);
					if (origMonitor != null) {
						boolean timeCheck = true;

						if (TempRef_c.getDisabled() > origMonitor.tau) {
							timeCheck = false;
						}

						if (timeCheck) {
							mainMonitor = (SafeMapIteratorMonitor)origMonitor.clone();
							mainMonitor.RVMRef_c = TempRef_c;
							if (TempRef_c.getTau() == -1){
								TempRef_c.setTau(origMonitor.tau);
							}
							mainMap.putLeaf(TempRef_c, mainMonitor);
							mainSet.add(mainMonitor);
							mainMonitor.monitorInfo.isFullParam = false;

							MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap12 = SafeMapIterator_map_c_i_Map;
							SafeMapIteratorMonitor_Set obj12 = tempMap12.getSet(TempRef_map);
							monitors = obj12;
							if (monitors == null) {
								monitors = new SafeMapIteratorMonitor_Set();
								tempMap12.putSet(TempRef_map, monitors);
							}
							monitors.add(mainMonitor);

							MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap13 = SafeMapIterator_c__To__map_c_Map;
							SafeMapIteratorMonitor_Set obj13 = tempMap13.getSet(TempRef_c);
							monitors = obj13;
							if (monitors == null) {
								monitors = new SafeMapIteratorMonitor_Set();
								tempMap13.putSet(TempRef_c, monitors);
							}
							monitors.add(mainMonitor);

							SafeMapIterator__To__map_c_Set.add(mainMonitor);
						}
					}
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeMapIteratorMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.RVMRef_map = TempRef_map;
					mainMonitor.RVMRef_c = TempRef_c;

					mainMap.putLeaf(TempRef_c, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = SafeMapIterator_timestamp;
					if (TempRef_map.getTau() == -1){
						TempRef_map.setTau(SafeMapIterator_timestamp);
					}
					if (TempRef_c.getTau() == -1){
						TempRef_c.setTau(SafeMapIterator_timestamp);
					}
					SafeMapIterator_timestamp++;

					MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap14 = SafeMapIterator_map_c_i_Map;
					SafeMapIteratorMonitor_Set obj14 = tempMap14.getSet(TempRef_map);
					monitors = obj14;
					if (monitors == null) {
						monitors = new SafeMapIteratorMonitor_Set();
						tempMap14.putSet(TempRef_map, monitors);
					}
					monitors.add(mainMonitor);

					MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap15 = SafeMapIterator_c__To__map_c_Map;
					SafeMapIteratorMonitor_Set obj15 = tempMap15.getSet(TempRef_c);
					monitors = obj15;
					if (monitors == null) {
						monitors = new SafeMapIteratorMonitor_Set();
						tempMap15.putSet(TempRef_c, monitors);
					}
					monitors.add(mainMonitor);

					SafeMapIterator__To__map_c_Set.add(mainMonitor);
				}

				TempRef_map.setDisabled(SafeMapIterator_timestamp);
				TempRef_c.setDisabled(SafeMapIterator_timestamp);
				SafeMapIterator_timestamp++;
			}

			SafeMapIterator_map_c_Map_cachekey_0 = TempRef_map;
			SafeMapIterator_map_c_Map_cachekey_1 = TempRef_c;
			SafeMapIterator_map_c_Map_cacheset = mainSet;
			SafeMapIterator_map_c_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_createColl(map, c);
			violationProp1 = mainSet.violationProp1;
		}
		SafeMapIterator_RVMLock.unlock();
	}

	public static void createIterEvent(Collection c, Iterator i) {
		SafeMapIterator_activated = true;
		while (!SafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeMapIteratorMonitor mainMonitor = null;
		SafeMapIteratorMonitor origMonitor = null;
		SafeMapIteratorMonitor lastMonitor = null;
		SafeMapIteratorMonitor_Set mainSet = null;
		SafeMapIteratorMonitor_Set origSet = null;
		SafeMapIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_map;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (SafeMapIterator_c_i_Map_cachekey_1 != null && c == SafeMapIterator_c_i_Map_cachekey_1.get() && SafeMapIterator_c_i_Map_cachekey_2 != null && i == SafeMapIterator_c_i_Map_cachekey_2.get()) {
			TempRef_c = SafeMapIterator_c_i_Map_cachekey_1;
			TempRef_i = SafeMapIterator_c_i_Map_cachekey_2;

			mainSet = SafeMapIterator_c_i_Map_cacheset;
			mainMonitor = SafeMapIterator_c_i_Map_cachenode;
		} else {
			TempRef_c = SafeMapIterator_Collection_RefMap.findOrCreateWeakRef(c);
			TempRef_i = SafeMapIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap1 = SafeMapIterator_c_i_Map;
			MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj1 = tempMap1.getMap(TempRef_c);
			if (obj1 == null) {
				obj1 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(2);
				tempMap1.putMap(TempRef_c, obj1);
			}
			MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> mainMap = obj1;
			mainMonitor = mainMap.getLeaf(TempRef_i);
			mainSet = mainMap.getSet(TempRef_i);
			if (mainSet == null){
				mainSet = new SafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_i, mainSet);
			}

			if (mainMonitor == null) {
				MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> origMap1 = SafeMapIterator_c__To__map_c_Map;
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

							MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap2 = SafeMapIterator_map_c_i_Map;
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj2 = tempMap2.getMap(TempRef_map);
							if (obj2 == null) {
								obj2 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
								tempMap2.putMap(TempRef_map, obj2);
							}
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap3 = obj2;
							MapOfMonitor<SafeMapIteratorMonitor> obj3 = tempMap3.getMap(TempRef_c);
							if (obj3 == null) {
								obj3 = new MapOfMonitor<SafeMapIteratorMonitor>(2);
								tempMap3.putMap(TempRef_c, obj3);
							}
							MapOfMonitor<SafeMapIteratorMonitor> lastMap1 = obj3;
							lastMonitor = lastMap1.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_i.getDisabled() > origMonitor.tau|| (TempRef_i.getTau() > 0 && TempRef_i.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeMapIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_i = TempRef_i;
									if (TempRef_i.getTau() == -1){
										TempRef_i.setTau(origMonitor.tau);
									}
									lastMap1.putLeaf(TempRef_i, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap4 = SafeMapIterator_map_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj4 = tempMap4.getMap(TempRef_map);
									if (obj4 == null) {
										obj4 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap4.putMap(TempRef_map, obj4);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap5 = obj4;
									SafeMapIteratorMonitor_Set obj5 = tempMap5.getSet(TempRef_i);
									monitors = obj5;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap5.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap6 = SafeMapIterator_map_c_i_Map;
									SafeMapIteratorMonitor_Set obj6 = tempMap6.getSet(TempRef_map);
									monitors = obj6;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap6.putSet(TempRef_map, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap7 = SafeMapIterator_c_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj7 = tempMap7.getMap(TempRef_c);
									if (obj7 == null) {
										obj7 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
										tempMap7.putMap(TempRef_c, obj7);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap8 = obj7;
									SafeMapIteratorMonitor_Set obj8 = tempMap8.getSet(TempRef_i);
									mainSet = obj8;
									if (mainSet == null) {
										mainSet = new SafeMapIteratorMonitor_Set();
										tempMap8.putSet(TempRef_i, mainSet);
									}
									mainSet.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap9 = SafeMapIterator_map_c_i_Map;
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj9 = tempMap9.getMap(TempRef_map);
									if (obj9 == null) {
										obj9 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap9.putMap(TempRef_map, obj9);
									}
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap10 = obj9;
									SafeMapIteratorMonitor_Set obj10 = tempMap10.getSet(TempRef_c);
									monitors = obj10;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap10.putSet(TempRef_c, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap11 = SafeMapIterator_i_Map;
									SafeMapIteratorMonitor_Set obj11 = tempMap11.getSet(TempRef_i);
									monitors = obj11;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap11.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				origSet = SafeMapIterator__To__map_Set;
				if (origSet!= null) {
					int numAlive = 0;
					for(int i_1 = 0; i_1 < origSet.getSize(); i_1++) {
						origMonitor = origSet.get(i_1);
						Map map = (Map)origMonitor.RVMRef_map.get();
						if (!origMonitor.isTerminated() && map != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_map = origMonitor.RVMRef_map;

							MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap12 = SafeMapIterator_map_c_i_Map;
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj12 = tempMap12.getMap(TempRef_map);
							if (obj12 == null) {
								obj12 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
								tempMap12.putMap(TempRef_map, obj12);
							}
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap13 = obj12;
							MapOfMonitor<SafeMapIteratorMonitor> obj13 = tempMap13.getMap(TempRef_c);
							if (obj13 == null) {
								obj13 = new MapOfMonitor<SafeMapIteratorMonitor>(2);
								tempMap13.putMap(TempRef_c, obj13);
							}
							MapOfMonitor<SafeMapIteratorMonitor> lastMap2 = obj13;
							lastMonitor = lastMap2.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_c.getDisabled() > origMonitor.tau|| (TempRef_c.getTau() > 0 && TempRef_c.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}
								if (TempRef_i.getDisabled() > origMonitor.tau|| (TempRef_i.getTau() > 0 && TempRef_i.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeMapIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_c = TempRef_c;
									if (TempRef_c.getTau() == -1){
										TempRef_c.setTau(origMonitor.tau);
									}
									lastMonitor.RVMRef_i = TempRef_i;
									if (TempRef_i.getTau() == -1){
										TempRef_i.setTau(origMonitor.tau);
									}
									lastMap2.putLeaf(TempRef_i, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap14 = SafeMapIterator_map_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj14 = tempMap14.getMap(TempRef_map);
									if (obj14 == null) {
										obj14 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap14.putMap(TempRef_map, obj14);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap15 = obj14;
									SafeMapIteratorMonitor_Set obj15 = tempMap15.getSet(TempRef_i);
									monitors = obj15;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap15.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap16 = SafeMapIterator_map_c_i_Map;
									SafeMapIteratorMonitor_Set obj16 = tempMap16.getSet(TempRef_map);
									monitors = obj16;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap16.putSet(TempRef_map, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap17 = SafeMapIterator_c_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj17 = tempMap17.getMap(TempRef_c);
									if (obj17 == null) {
										obj17 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
										tempMap17.putMap(TempRef_c, obj17);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap18 = obj17;
									SafeMapIteratorMonitor_Set obj18 = tempMap18.getSet(TempRef_i);
									mainSet = obj18;
									if (mainSet == null) {
										mainSet = new SafeMapIteratorMonitor_Set();
										tempMap18.putSet(TempRef_i, mainSet);
									}
									mainSet.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap19 = SafeMapIterator_map_c_i_Map;
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj19 = tempMap19.getMap(TempRef_map);
									if (obj19 == null) {
										obj19 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap19.putMap(TempRef_map, obj19);
									}
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap20 = obj19;
									SafeMapIteratorMonitor_Set obj20 = tempMap20.getSet(TempRef_c);
									monitors = obj20;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap20.putSet(TempRef_c, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap21 = SafeMapIterator_i_Map;
									SafeMapIteratorMonitor_Set obj21 = tempMap21.getSet(TempRef_i);
									monitors = obj21;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap21.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeMapIteratorMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.RVMRef_c = TempRef_c;
					mainMonitor.RVMRef_i = TempRef_i;

					mainMap.putLeaf(TempRef_i, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = SafeMapIterator_timestamp;
					if (TempRef_c.getTau() == -1){
						TempRef_c.setTau(SafeMapIterator_timestamp);
					}
					if (TempRef_i.getTau() == -1){
						TempRef_i.setTau(SafeMapIterator_timestamp);
					}
					SafeMapIterator_timestamp++;

					MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap22 = SafeMapIterator_i_Map;
					SafeMapIteratorMonitor_Set obj22 = tempMap22.getSet(TempRef_i);
					monitors = obj22;
					if (monitors == null) {
						monitors = new SafeMapIteratorMonitor_Set();
						tempMap22.putSet(TempRef_i, monitors);
					}
					monitors.add(mainMonitor);

					SafeMapIterator__To__c_i_Set.add(mainMonitor);

					MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap23 = SafeMapIterator_c__To__c_i_Map;
					SafeMapIteratorMonitor_Set obj23 = tempMap23.getSet(TempRef_c);
					monitors = obj23;
					if (monitors == null) {
						monitors = new SafeMapIteratorMonitor_Set();
						tempMap23.putSet(TempRef_c, monitors);
					}
					monitors.add(mainMonitor);
				}

				TempRef_c.setDisabled(SafeMapIterator_timestamp);
				TempRef_i.setDisabled(SafeMapIterator_timestamp);
				SafeMapIterator_timestamp++;
			}

			SafeMapIterator_c_i_Map_cachekey_1 = TempRef_c;
			SafeMapIterator_c_i_Map_cachekey_2 = TempRef_i;
			SafeMapIterator_c_i_Map_cacheset = mainSet;
			SafeMapIterator_c_i_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_createIter(c, i);
			violationProp1 = mainSet.violationProp1;
		}
		SafeMapIterator_RVMLock.unlock();
	}

	public static void useIterEvent(Iterator i) {
		SafeMapIterator_activated = true;
		while (!SafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeMapIteratorMonitor mainMonitor = null;
		SafeMapIteratorMonitor origMonitor = null;
		SafeMapIteratorMonitor lastMonitor = null;
		SafeMapIteratorMonitor_Set mainSet = null;
		SafeMapIteratorMonitor_Set origSet = null;
		SafeMapIteratorMonitor_Set lastSet = null;
		SafeMapIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_map;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (SafeMapIterator_i_Map_cachekey_2 != null && i == SafeMapIterator_i_Map_cachekey_2.get()) {
			TempRef_i = SafeMapIterator_i_Map_cachekey_2;

			mainSet = SafeMapIterator_i_Map_cacheset;
			mainMonitor = SafeMapIterator_i_Map_cachenode;
		} else {
			TempRef_i = SafeMapIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> mainMap = SafeMapIterator_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);
			mainSet = mainMap.getSet(TempRef_i);
			if (mainSet == null){
				mainSet = new SafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_i, mainSet);
			}

			if (mainMonitor == null) {
				origSet = SafeMapIterator__To__map_c_Set;
				if (origSet!= null) {
					int numAlive = 0;
					for(int i_1 = 0; i_1 < origSet.getSize(); i_1++) {
						origMonitor = origSet.get(i_1);
						Map map = (Map)origMonitor.RVMRef_map.get();
						Collection c = (Collection)origMonitor.RVMRef_c.get();
						if (!origMonitor.isTerminated() && map != null && c != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_map = origMonitor.RVMRef_map;
							TempRef_c = origMonitor.RVMRef_c;

							MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap1 = SafeMapIterator_map_c_i_Map;
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj1 = tempMap1.getMap(TempRef_map);
							if (obj1 == null) {
								obj1 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
								tempMap1.putMap(TempRef_map, obj1);
							}
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap2 = obj1;
							MapOfMonitor<SafeMapIteratorMonitor> obj2 = tempMap2.getMap(TempRef_c);
							if (obj2 == null) {
								obj2 = new MapOfMonitor<SafeMapIteratorMonitor>(2);
								tempMap2.putMap(TempRef_c, obj2);
							}
							MapOfMonitor<SafeMapIteratorMonitor> lastMap1 = obj2;
							lastMonitor = lastMap1.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_i.getDisabled() > origMonitor.tau|| (TempRef_i.getTau() > 0 && TempRef_i.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeMapIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_i = TempRef_i;
									if (TempRef_i.getTau() == -1){
										TempRef_i.setTau(origMonitor.tau);
									}
									lastMap1.putLeaf(TempRef_i, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap3 = SafeMapIterator_map_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj3 = tempMap3.getMap(TempRef_map);
									if (obj3 == null) {
										obj3 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap3.putMap(TempRef_map, obj3);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap4 = obj3;
									SafeMapIteratorMonitor_Set obj4 = tempMap4.getSet(TempRef_i);
									monitors = obj4;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap4.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap5 = SafeMapIterator_map_c_i_Map;
									SafeMapIteratorMonitor_Set obj5 = tempMap5.getSet(TempRef_map);
									monitors = obj5;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap5.putSet(TempRef_map, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap6 = SafeMapIterator_c_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj6 = tempMap6.getMap(TempRef_c);
									if (obj6 == null) {
										obj6 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
										tempMap6.putMap(TempRef_c, obj6);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap7 = obj6;
									SafeMapIteratorMonitor_Set obj7 = tempMap7.getSet(TempRef_i);
									monitors = obj7;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap7.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap8 = SafeMapIterator_map_c_i_Map;
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj8 = tempMap8.getMap(TempRef_map);
									if (obj8 == null) {
										obj8 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap8.putMap(TempRef_map, obj8);
									}
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap9 = obj8;
									SafeMapIteratorMonitor_Set obj9 = tempMap9.getSet(TempRef_c);
									monitors = obj9;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap9.putSet(TempRef_c, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap10 = SafeMapIterator_i_Map;
									SafeMapIteratorMonitor_Set obj10 = tempMap10.getSet(TempRef_i);
									mainSet = obj10;
									if (mainSet == null) {
										mainSet = new SafeMapIteratorMonitor_Set();
										tempMap10.putSet(TempRef_i, mainSet);
									}
									mainSet.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				origSet = SafeMapIterator__To__map_Set;
				if (origSet!= null) {
					int numAlive = 0;
					for(int i_1 = 0; i_1 < origSet.getSize(); i_1++) {
						origMonitor = origSet.get(i_1);
						Map map = (Map)origMonitor.RVMRef_map.get();
						if (!origMonitor.isTerminated() && map != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_map = origMonitor.RVMRef_map;

							MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap11 = SafeMapIterator_map_i_Map;
							MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj11 = tempMap11.getMap(TempRef_map);
							if (obj11 == null) {
								obj11 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(2);
								tempMap11.putMap(TempRef_map, obj11);
							}
							MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> lastMap2 = obj11;
							lastMonitor = lastMap2.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_i.getDisabled() > origMonitor.tau|| (TempRef_i.getTau() > 0 && TempRef_i.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeMapIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_i = TempRef_i;
									if (TempRef_i.getTau() == -1){
										TempRef_i.setTau(origMonitor.tau);
									}
									lastMap2.putLeaf(TempRef_i, lastMonitor);
									lastSet.add(lastMonitor);
									lastMonitor.monitorInfo.isFullParam = false;

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap12 = SafeMapIterator_map_c_i_Map;
									SafeMapIteratorMonitor_Set obj12 = tempMap12.getSet(TempRef_map);
									monitors = obj12;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap12.putSet(TempRef_map, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap13 = SafeMapIterator_i_Map;
									SafeMapIteratorMonitor_Set obj13 = tempMap13.getSet(TempRef_i);
									mainSet = obj13;
									if (mainSet == null) {
										mainSet = new SafeMapIteratorMonitor_Set();
										tempMap13.putSet(TempRef_i, mainSet);
									}
									mainSet.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeMapIteratorMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.RVMRef_i = TempRef_i;

					SafeMapIterator_i_Map.putLeaf(TempRef_i, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = SafeMapIterator_timestamp;
					if (TempRef_i.getTau() == -1){
						TempRef_i.setTau(SafeMapIterator_timestamp);
					}
					SafeMapIterator_timestamp++;
				}

				TempRef_i.setDisabled(SafeMapIterator_timestamp);
				SafeMapIterator_timestamp++;
			}

			SafeMapIterator_i_Map_cachekey_2 = TempRef_i;
			SafeMapIterator_i_Map_cacheset = mainSet;
			SafeMapIterator_i_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_useIter(i);
			violationProp1 = mainSet.violationProp1;
		}
		SafeMapIterator_RVMLock.unlock();
	}

	public static void updateMapEvent(Map map) {
		SafeMapIterator_activated = true;
		while (!SafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeMapIteratorMonitor mainMonitor = null;
		SafeMapIteratorMonitor origMonitor = null;
		SafeMapIteratorMonitor lastMonitor = null;
		SafeMapIteratorMonitor_Set mainSet = null;
		SafeMapIteratorMonitor_Set origSet = null;
		SafeMapIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_map;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (SafeMapIterator_map_Map_cachekey_0 != null && map == SafeMapIterator_map_Map_cachekey_0.get()) {
			TempRef_map = SafeMapIterator_map_Map_cachekey_0;

			mainSet = SafeMapIterator_map_Map_cacheset;
			mainMonitor = SafeMapIterator_map_Map_cachenode;
		} else {
			TempRef_map = SafeMapIterator_Map_RefMap.findOrCreateWeakRef(map);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> mainMap = SafeMapIterator_map_c_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_map);
			mainSet = mainMap.getSet(TempRef_map);
			if (mainSet == null){
				mainSet = new SafeMapIteratorMonitor_Set();
				mainMap.putSet(TempRef_map, mainSet);
			}

			if (mainMonitor == null) {
				origSet = SafeMapIterator__To__c_i_Set;
				if (origSet!= null) {
					int numAlive = 0;
					for(int i_1 = 0; i_1 < origSet.getSize(); i_1++) {
						origMonitor = origSet.get(i_1);
						Collection c = (Collection)origMonitor.RVMRef_c.get();
						Iterator i = (Iterator)origMonitor.RVMRef_i.get();
						if (!origMonitor.isTerminated() && c != null && i != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_c = origMonitor.RVMRef_c;
							TempRef_i = origMonitor.RVMRef_i;

							MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap1 = SafeMapIterator_map_c_i_Map;
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj1 = tempMap1.getMap(TempRef_map);
							if (obj1 == null) {
								obj1 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
								tempMap1.putMap(TempRef_map, obj1);
							}
							MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap2 = obj1;
							MapOfMonitor<SafeMapIteratorMonitor> obj2 = tempMap2.getMap(TempRef_c);
							if (obj2 == null) {
								obj2 = new MapOfMonitor<SafeMapIteratorMonitor>(2);
								tempMap2.putMap(TempRef_c, obj2);
							}
							MapOfMonitor<SafeMapIteratorMonitor> lastMap1 = obj2;
							lastMonitor = lastMap1.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_map.getDisabled() > origMonitor.tau|| (TempRef_map.getTau() > 0 && TempRef_map.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeMapIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_map = TempRef_map;
									if (TempRef_map.getTau() == -1){
										TempRef_map.setTau(origMonitor.tau);
									}
									lastMap1.putLeaf(TempRef_i, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap3 = SafeMapIterator_map_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj3 = tempMap3.getMap(TempRef_map);
									if (obj3 == null) {
										obj3 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap3.putMap(TempRef_map, obj3);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap4 = obj3;
									SafeMapIteratorMonitor_Set obj4 = tempMap4.getSet(TempRef_i);
									monitors = obj4;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap4.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap5 = SafeMapIterator_map_c_i_Map;
									SafeMapIteratorMonitor_Set obj5 = tempMap5.getSet(TempRef_map);
									mainSet = obj5;
									if (mainSet == null) {
										mainSet = new SafeMapIteratorMonitor_Set();
										tempMap5.putSet(TempRef_map, mainSet);
									}
									mainSet.add(lastMonitor);

									MapOfAll<MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap6 = SafeMapIterator_c_i_Map;
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj6 = tempMap6.getMap(TempRef_c);
									if (obj6 == null) {
										obj6 = new MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(1);
										tempMap6.putMap(TempRef_c, obj6);
									}
									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap7 = obj6;
									SafeMapIteratorMonitor_Set obj7 = tempMap7.getSet(TempRef_i);
									monitors = obj7;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap7.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);

									MapOfAll<MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap8 = SafeMapIterator_map_c_i_Map;
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> obj8 = tempMap8.getMap(TempRef_map);
									if (obj8 == null) {
										obj8 = new MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor>(0);
										tempMap8.putMap(TempRef_map, obj8);
									}
									MapOfAll<MapOfMonitor<SafeMapIteratorMonitor>, SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap9 = obj8;
									SafeMapIteratorMonitor_Set obj9 = tempMap9.getSet(TempRef_c);
									monitors = obj9;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap9.putSet(TempRef_c, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<SafeMapIteratorMonitor_Set, SafeMapIteratorMonitor> tempMap10 = SafeMapIterator_i_Map;
									SafeMapIteratorMonitor_Set obj10 = tempMap10.getSet(TempRef_i);
									monitors = obj10;
									if (monitors == null) {
										monitors = new SafeMapIteratorMonitor_Set();
										tempMap10.putSet(TempRef_i, monitors);
									}
									monitors.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeMapIteratorMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.RVMRef_map = TempRef_map;

					SafeMapIterator_map_c_i_Map.putLeaf(TempRef_map, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = SafeMapIterator_timestamp;
					if (TempRef_map.getTau() == -1){
						TempRef_map.setTau(SafeMapIterator_timestamp);
					}
					SafeMapIterator_timestamp++;

					SafeMapIterator__To__map_Set.add(mainMonitor);
				}

				TempRef_map.setDisabled(SafeMapIterator_timestamp);
				SafeMapIterator_timestamp++;
			}

			SafeMapIterator_map_Map_cachekey_0 = TempRef_map;
			SafeMapIterator_map_Map_cacheset = mainSet;
			SafeMapIterator_map_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_updateMap(map);
			violationProp1 = mainSet.violationProp1;
		}
		SafeMapIterator_RVMLock.unlock();
	}

}
