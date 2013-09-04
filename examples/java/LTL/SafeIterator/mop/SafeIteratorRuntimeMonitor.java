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

class SafeIteratorMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeIteratorMonitor> {
	boolean violationProp1;

	SafeIteratorMonitor_Set(){
		this.size = 0;
		this.elements = new SafeIteratorMonitor[4];
	}

	final void event_create(Collection c, Iterator i) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			SafeIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_create(c, i);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(c, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_updatesource(Collection c) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			SafeIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_updatesource(c);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(c, null);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_next(Iterator i) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			SafeIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_next(i);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(null, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
}

class SafeIteratorMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	long tau = -1;
	protected Object clone() {
		try {
			SafeIteratorMonitor ret = (SafeIteratorMonitor) super.clone();
			ret.monitorInfo = (com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo)this.monitorInfo.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_create[] = {2, 3, 2, 3};;
	static final int Prop_1_transition_updatesource[] = {0, 3, 0, 3};;
	static final int Prop_1_transition_next[] = {1, 3, 2, 3};;

	boolean Prop_1_Category_violation = false;

	SafeIteratorMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_create(Collection c, Iterator i) {
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_create[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 1;
		}
		return true;
	}

	final boolean Prop_1_event_updatesource(Collection c) {
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_updatesource[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 1;
		}
		return true;
	}

	final boolean Prop_1_event_next(Iterator i) {
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 1;
		}
		return true;
	}

	final void Prop_1_handler_violation (Collection c, Iterator i){
		{
			System.out.println("improper iterator usage");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_violation = false;
	}

	CachedTagWeakReference RVMRef_c;
	CachedTagWeakReference RVMRef_i;

	//alive_parameters_0 = [Collection c, Iterator i]
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
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
		}
		switch(RVM_lastevent) {
			case -1:
			return;
			case 0:
			//create
			//alive_c && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//updatesource
			//alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//next
			//alive_c && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

	com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo monitorInfo;
}

public class SafeIteratorRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean violationProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeIteratorMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		SafeIteratorMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeIteratorMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock SafeIterator_RVMLock = new ReentrantLock();
	static Condition SafeIterator_RVMLock_cond = SafeIterator_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeIterator_timestamp = 1;

	private static boolean SafeIterator_activated = false;

	// Declarations for Indexing Trees
	static CachedTagWeakReference SafeIterator_c_Map_cachekey_0 = null;
	static SafeIteratorMonitor_Set SafeIterator_c_Map_cacheset = null;
	static SafeIteratorMonitor SafeIterator_c_Map_cachenode = null;
	static MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> SafeIterator_c_i_Map = new MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor>(0);
	static CachedTagWeakReference SafeIterator_c_i_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeIterator_c_i_Map_cachekey_1 = null;
	static SafeIteratorMonitor SafeIterator_c_i_Map_cachenode = null;
	static MapOfSetMonitor<SafeIteratorMonitor_Set, SafeIteratorMonitor> SafeIterator_i_Map = new MapOfSetMonitor<SafeIteratorMonitor_Set, SafeIteratorMonitor>(1);
	static CachedTagWeakReference SafeIterator_i_Map_cachekey_1 = null;
	static SafeIteratorMonitor_Set SafeIterator_i_Map_cacheset = null;
	static SafeIteratorMonitor SafeIterator_i_Map_cachenode = null;
	static SafeIteratorMonitor_Set SafeIterator__To__c_Set = new SafeIteratorMonitor_Set();

	// Trees for References
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeIterator_Collection_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeIterator_Iterator_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

	public static void createEvent(Collection c, Iterator i) {
		SafeIterator_activated = true;
		while (!SafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeIteratorMonitor mainMonitor = null;
		SafeIteratorMonitor origMonitor = null;
		SafeIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		violationProp1 = false;
		// Cache Retrieval
		if (SafeIterator_c_i_Map_cachekey_0 != null && c == SafeIterator_c_i_Map_cachekey_0.get() && SafeIterator_c_i_Map_cachekey_1 != null && i == SafeIterator_c_i_Map_cachekey_1.get()) {
			TempRef_c = SafeIterator_c_i_Map_cachekey_0;
			TempRef_i = SafeIterator_c_i_Map_cachekey_1;

			mainMonitor = SafeIterator_c_i_Map_cachenode;
		} else {
			TempRef_c = SafeIterator_Collection_RefMap.findOrCreateWeakRef(c);
			TempRef_i = SafeIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainMonitor == null) {
			MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap1 = SafeIterator_c_i_Map;
			MapOfMonitor<SafeIteratorMonitor> obj1 = tempMap1.getMap(TempRef_c);
			if (obj1 == null) {
				obj1 = new MapOfMonitor<SafeIteratorMonitor>(1);
				tempMap1.putMap(TempRef_c, obj1);
			}
			MapOfMonitor<SafeIteratorMonitor> mainMap = obj1;
			mainMonitor = mainMap.getLeaf(TempRef_i);

			if (mainMonitor == null) {
				if (mainMonitor == null) {
					MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> origMap1 = SafeIterator_c_i_Map;
					origMonitor = origMap1.getLeaf(TempRef_c);
					if (origMonitor != null) {
						boolean timeCheck = true;

						if (TempRef_i.getDisabled() > origMonitor.tau) {
							timeCheck = false;
						}

						if (timeCheck) {
							mainMonitor = (SafeIteratorMonitor)origMonitor.clone();
							mainMonitor.RVMRef_i = TempRef_i;
							if (TempRef_i.getTau() == -1){
								TempRef_i.setTau(origMonitor.tau);
							}
							mainMap.putLeaf(TempRef_i, mainMonitor);
							mainMonitor.monitorInfo.isFullParam = true;

							MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap2 = SafeIterator_c_i_Map;
							SafeIteratorMonitor_Set obj2 = tempMap2.getSet(TempRef_c);
							monitors = obj2;
							if (monitors == null) {
								monitors = new SafeIteratorMonitor_Set();
								tempMap2.putSet(TempRef_c, monitors);
							}
							monitors.add(mainMonitor);

							MapOfSetMonitor<SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap3 = SafeIterator_i_Map;
							SafeIteratorMonitor_Set obj3 = tempMap3.getSet(TempRef_i);
							monitors = obj3;
							if (monitors == null) {
								monitors = new SafeIteratorMonitor_Set();
								tempMap3.putSet(TempRef_i, monitors);
							}
							monitors.add(mainMonitor);
						}
					}
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeIteratorMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = true;

					mainMonitor.RVMRef_c = TempRef_c;
					mainMonitor.RVMRef_i = TempRef_i;

					mainMap.putLeaf(TempRef_i, mainMonitor);
					mainMonitor.tau = SafeIterator_timestamp;
					if (TempRef_c.getTau() == -1){
						TempRef_c.setTau(SafeIterator_timestamp);
					}
					if (TempRef_i.getTau() == -1){
						TempRef_i.setTau(SafeIterator_timestamp);
					}
					SafeIterator_timestamp++;

					MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap4 = SafeIterator_c_i_Map;
					SafeIteratorMonitor_Set obj4 = tempMap4.getSet(TempRef_c);
					monitors = obj4;
					if (monitors == null) {
						monitors = new SafeIteratorMonitor_Set();
						tempMap4.putSet(TempRef_c, monitors);
					}
					monitors.add(mainMonitor);

					MapOfSetMonitor<SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap5 = SafeIterator_i_Map;
					SafeIteratorMonitor_Set obj5 = tempMap5.getSet(TempRef_i);
					monitors = obj5;
					if (monitors == null) {
						monitors = new SafeIteratorMonitor_Set();
						tempMap5.putSet(TempRef_i, monitors);
					}
					monitors.add(mainMonitor);
				}

				TempRef_i.setDisabled(SafeIterator_timestamp);
				SafeIterator_timestamp++;
			}

			SafeIterator_c_i_Map_cachekey_0 = TempRef_c;
			SafeIterator_c_i_Map_cachekey_1 = TempRef_i;
			SafeIterator_c_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_create(c, i);
		violationProp1 |= mainMonitor.Prop_1_Category_violation;
		if(mainMonitor.Prop_1_Category_violation) {
			mainMonitor.Prop_1_handler_violation(c, i);
		}
		SafeIterator_RVMLock.unlock();
	}

	public static void updatesourceEvent(Collection c) {
		SafeIterator_activated = true;
		while (!SafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeIteratorMonitor mainMonitor = null;
		SafeIteratorMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_c;

		// Cache Retrieval
		if (SafeIterator_c_Map_cachekey_0 != null && c == SafeIterator_c_Map_cachekey_0.get()) {
			TempRef_c = SafeIterator_c_Map_cachekey_0;

			mainSet = SafeIterator_c_Map_cacheset;
			mainMonitor = SafeIterator_c_Map_cachenode;
		} else {
			TempRef_c = SafeIterator_Collection_RefMap.findOrCreateWeakRef(c);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> mainMap = SafeIterator_c_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_c);
			mainSet = mainMap.getSet(TempRef_c);
			if (mainSet == null){
				mainSet = new SafeIteratorMonitor_Set();
				mainMap.putSet(TempRef_c, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new SafeIteratorMonitor();
				mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				mainMonitor.monitorInfo.isFullParam = false;

				mainMonitor.RVMRef_c = TempRef_c;

				SafeIterator_c_i_Map.putLeaf(TempRef_c, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = SafeIterator_timestamp;
				if (TempRef_c.getTau() == -1){
					TempRef_c.setTau(SafeIterator_timestamp);
				}
				SafeIterator_timestamp++;

				SafeIterator__To__c_Set.add(mainMonitor);
			}

			SafeIterator_c_Map_cachekey_0 = TempRef_c;
			SafeIterator_c_Map_cacheset = mainSet;
			SafeIterator_c_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_updatesource(c);
			violationProp1 = mainSet.violationProp1;
		}
		SafeIterator_RVMLock.unlock();
	}

	public static void nextEvent(Iterator i) {
		SafeIterator_activated = true;
		while (!SafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeIteratorMonitor mainMonitor = null;
		SafeIteratorMonitor origMonitor = null;
		SafeIteratorMonitor lastMonitor = null;
		SafeIteratorMonitor_Set mainSet = null;
		SafeIteratorMonitor_Set origSet = null;
		SafeIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (SafeIterator_i_Map_cachekey_1 != null && i == SafeIterator_i_Map_cachekey_1.get()) {
			TempRef_i = SafeIterator_i_Map_cachekey_1;

			mainSet = SafeIterator_i_Map_cacheset;
			mainMonitor = SafeIterator_i_Map_cachenode;
		} else {
			TempRef_i = SafeIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfSetMonitor<SafeIteratorMonitor_Set, SafeIteratorMonitor> mainMap = SafeIterator_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);
			mainSet = mainMap.getSet(TempRef_i);
			if (mainSet == null){
				mainSet = new SafeIteratorMonitor_Set();
				mainMap.putSet(TempRef_i, mainSet);
			}

			if (mainMonitor == null) {
				origSet = SafeIterator__To__c_Set;
				if (origSet!= null) {
					int numAlive = 0;
					for(int i_1 = 0; i_1 < origSet.getSize(); i_1++) {
						origMonitor = origSet.get(i_1);
						Collection c = (Collection)origMonitor.RVMRef_c.get();
						if (!origMonitor.isTerminated() && c != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_c = origMonitor.RVMRef_c;

							MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap1 = SafeIterator_c_i_Map;
							MapOfMonitor<SafeIteratorMonitor> obj1 = tempMap1.getMap(TempRef_c);
							if (obj1 == null) {
								obj1 = new MapOfMonitor<SafeIteratorMonitor>(1);
								tempMap1.putMap(TempRef_c, obj1);
							}
							MapOfMonitor<SafeIteratorMonitor> lastMap1 = obj1;
							lastMonitor = lastMap1.getLeaf(TempRef_i);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_i.getDisabled() > origMonitor.tau|| (TempRef_i.getTau() > 0 && TempRef_i.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeIteratorMonitor)origMonitor.clone();
									lastMonitor.RVMRef_i = TempRef_i;
									if (TempRef_i.getTau() == -1){
										TempRef_i.setTau(origMonitor.tau);
									}
									lastMap1.putLeaf(TempRef_i, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfMonitor<SafeIteratorMonitor>, SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap2 = SafeIterator_c_i_Map;
									SafeIteratorMonitor_Set obj2 = tempMap2.getSet(TempRef_c);
									monitors = obj2;
									if (monitors == null) {
										monitors = new SafeIteratorMonitor_Set();
										tempMap2.putSet(TempRef_c, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<SafeIteratorMonitor_Set, SafeIteratorMonitor> tempMap3 = SafeIterator_i_Map;
									SafeIteratorMonitor_Set obj3 = tempMap3.getSet(TempRef_i);
									mainSet = obj3;
									if (mainSet == null) {
										mainSet = new SafeIteratorMonitor_Set();
										tempMap3.putSet(TempRef_i, mainSet);
									}
									mainSet.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeIteratorMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.RVMRef_i = TempRef_i;

					SafeIterator_i_Map.putLeaf(TempRef_i, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = SafeIterator_timestamp;
					if (TempRef_i.getTau() == -1){
						TempRef_i.setTau(SafeIterator_timestamp);
					}
					SafeIterator_timestamp++;
				}

				TempRef_i.setDisabled(SafeIterator_timestamp);
				SafeIterator_timestamp++;
			}

			SafeIterator_i_Map_cachekey_1 = TempRef_i;
			SafeIterator_i_Map_cacheset = mainSet;
			SafeIterator_i_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_next(i);
			violationProp1 = mainSet.violationProp1;
		}
		SafeIterator_RVMLock.unlock();
	}

}
