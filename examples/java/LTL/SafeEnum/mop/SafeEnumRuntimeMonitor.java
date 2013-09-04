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

class SafeEnumMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeEnumMonitor> {
	boolean violationProp1;

	SafeEnumMonitor_Set(){
		this.size = 0;
		this.elements = new SafeEnumMonitor[4];
	}

	final void event_create(Vector v, Enumeration e) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeEnumMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_create(v, e);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(v, e);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_updatesource(Vector v) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeEnumMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_updatesource(v);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(v, null);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_next(Enumeration e) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeEnumMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_next(e);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(null, e);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
}

class SafeEnumMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	long tau = -1;
	protected Object clone() {
		try {
			SafeEnumMonitor ret = (SafeEnumMonitor) super.clone();
			ret.monitorInfo = (com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo)this.monitorInfo.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_create[] = {1, 1, 3, 3};;
	static final int Prop_1_transition_updatesource[] = {0, 0, 3, 3};;
	static final int Prop_1_transition_next[] = {2, 1, 3, 3};;

	boolean Prop_1_Category_violation = false;

	SafeEnumMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_create(Vector v, Enumeration e) {
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_create[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
		return true;
	}

	final boolean Prop_1_event_updatesource(Vector v) {
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_updatesource[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
		return true;
	}

	final boolean Prop_1_event_next(Enumeration e) {
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_violation = Prop_1_state == 2;
		}
		return true;
	}

	final void Prop_1_handler_violation (Vector v, Enumeration e){
		{
			System.out.println("improper enumeration usage at " + Thread.currentThread().getStackTrace()[4].toString());
			this.reset();
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_violation = false;
	}

	CachedTagWeakReference RVMRef_v;
	CachedTagWeakReference RVMRef_e;

	//alive_parameters_0 = [Vector v, Enumeration e]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Enumeration e]
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
			//alive_v && alive_e
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//updatesource
			//alive_e
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//next
			//alive_v && alive_e
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

public class SafeEnumRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean violationProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeEnumMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		SafeEnumMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeEnumMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock SafeEnum_RVMLock = new ReentrantLock();
	static Condition SafeEnum_RVMLock_cond = SafeEnum_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeEnum_timestamp = 1;

	private static boolean SafeEnum_activated = false;

	// Declarations for Indexing Trees
	static CachedTagWeakReference SafeEnum_v_Map_cachekey_0 = null;
	static SafeEnumMonitor_Set SafeEnum_v_Map_cacheset = null;
	static SafeEnumMonitor SafeEnum_v_Map_cachenode = null;
	static MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> SafeEnum_v_e_Map = new MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor>(0);
	static CachedTagWeakReference SafeEnum_v_e_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeEnum_v_e_Map_cachekey_1 = null;
	static SafeEnumMonitor SafeEnum_v_e_Map_cachenode = null;
	static MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor> SafeEnum_e_Map = new MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor>(1);
	static CachedTagWeakReference SafeEnum_e_Map_cachekey_1 = null;
	static SafeEnumMonitor_Set SafeEnum_e_Map_cacheset = null;
	static SafeEnumMonitor SafeEnum_e_Map_cachenode = null;
	static SafeEnumMonitor_Set SafeEnum__To__v_Set = new SafeEnumMonitor_Set();

	// Trees for References
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeEnum_Enumeration_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeEnum_Vector_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

	public static void createEvent(Vector v, Enumeration e) {
		SafeEnum_activated = true;
		while (!SafeEnum_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeEnumMonitor mainMonitor = null;
		SafeEnumMonitor origMonitor = null;
		SafeEnumMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_v;
		CachedTagWeakReference TempRef_e;

		violationProp1 = false;
		// Cache Retrieval
		if (SafeEnum_v_e_Map_cachekey_0 != null && v == SafeEnum_v_e_Map_cachekey_0.get() && SafeEnum_v_e_Map_cachekey_1 != null && e == SafeEnum_v_e_Map_cachekey_1.get()) {
			TempRef_v = SafeEnum_v_e_Map_cachekey_0;
			TempRef_e = SafeEnum_v_e_Map_cachekey_1;

			mainMonitor = SafeEnum_v_e_Map_cachenode;
		} else {
			TempRef_v = SafeEnum_Vector_RefMap.findOrCreateWeakRef(v);
			TempRef_e = SafeEnum_Enumeration_RefMap.findOrCreateWeakRef(e);
		}

		if (mainMonitor == null) {
			MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> tempMap1 = SafeEnum_v_e_Map;
			MapOfMonitor<SafeEnumMonitor> obj1 = tempMap1.getMap(TempRef_v);
			if (obj1 == null) {
				obj1 = new MapOfMonitor<SafeEnumMonitor>(1);
				tempMap1.putMap(TempRef_v, obj1);
			}
			MapOfMonitor<SafeEnumMonitor> mainMap = obj1;
			mainMonitor = mainMap.getLeaf(TempRef_e);

			if (mainMonitor == null) {
				if (mainMonitor == null) {
					MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> origMap1 = SafeEnum_v_e_Map;
					origMonitor = origMap1.getLeaf(TempRef_v);
					if (origMonitor != null) {
						boolean timeCheck = true;

						if (TempRef_e.getDisabled() > origMonitor.tau) {
							timeCheck = false;
						}

						if (timeCheck) {
							mainMonitor = (SafeEnumMonitor)origMonitor.clone();
							mainMonitor.RVMRef_e = TempRef_e;
							if (TempRef_e.getTau() == -1){
								TempRef_e.setTau(origMonitor.tau);
							}
							mainMap.putLeaf(TempRef_e, mainMonitor);
							mainMonitor.monitorInfo.isFullParam = true;

							MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> tempMap2 = SafeEnum_v_e_Map;
							SafeEnumMonitor_Set obj2 = tempMap2.getSet(TempRef_v);
							monitors = obj2;
							if (monitors == null) {
								monitors = new SafeEnumMonitor_Set();
								tempMap2.putSet(TempRef_v, monitors);
							}
							monitors.add(mainMonitor);

							MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor> tempMap3 = SafeEnum_e_Map;
							SafeEnumMonitor_Set obj3 = tempMap3.getSet(TempRef_e);
							monitors = obj3;
							if (monitors == null) {
								monitors = new SafeEnumMonitor_Set();
								tempMap3.putSet(TempRef_e, monitors);
							}
							monitors.add(mainMonitor);
						}
					}
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeEnumMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = true;

					mainMonitor.RVMRef_v = TempRef_v;
					mainMonitor.RVMRef_e = TempRef_e;

					mainMap.putLeaf(TempRef_e, mainMonitor);
					mainMonitor.tau = SafeEnum_timestamp;
					if (TempRef_v.getTau() == -1){
						TempRef_v.setTau(SafeEnum_timestamp);
					}
					if (TempRef_e.getTau() == -1){
						TempRef_e.setTau(SafeEnum_timestamp);
					}
					SafeEnum_timestamp++;

					MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> tempMap4 = SafeEnum_v_e_Map;
					SafeEnumMonitor_Set obj4 = tempMap4.getSet(TempRef_v);
					monitors = obj4;
					if (monitors == null) {
						monitors = new SafeEnumMonitor_Set();
						tempMap4.putSet(TempRef_v, monitors);
					}
					monitors.add(mainMonitor);

					MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor> tempMap5 = SafeEnum_e_Map;
					SafeEnumMonitor_Set obj5 = tempMap5.getSet(TempRef_e);
					monitors = obj5;
					if (monitors == null) {
						monitors = new SafeEnumMonitor_Set();
						tempMap5.putSet(TempRef_e, monitors);
					}
					monitors.add(mainMonitor);
				}

				TempRef_e.setDisabled(SafeEnum_timestamp);
				SafeEnum_timestamp++;
			}

			SafeEnum_v_e_Map_cachekey_0 = TempRef_v;
			SafeEnum_v_e_Map_cachekey_1 = TempRef_e;
			SafeEnum_v_e_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_create(v, e);
		violationProp1 |= mainMonitor.Prop_1_Category_violation;
		if(mainMonitor.Prop_1_Category_violation) {
			mainMonitor.Prop_1_handler_violation(v, e);
		}
		SafeEnum_RVMLock.unlock();
	}

	public static void updatesourceEvent(Vector v) {
		SafeEnum_activated = true;
		while (!SafeEnum_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeEnumMonitor mainMonitor = null;
		SafeEnumMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_v;

		// Cache Retrieval
		if (SafeEnum_v_Map_cachekey_0 != null && v == SafeEnum_v_Map_cachekey_0.get()) {
			TempRef_v = SafeEnum_v_Map_cachekey_0;

			mainSet = SafeEnum_v_Map_cacheset;
			mainMonitor = SafeEnum_v_Map_cachenode;
		} else {
			TempRef_v = SafeEnum_Vector_RefMap.findOrCreateWeakRef(v);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> mainMap = SafeEnum_v_e_Map;
			mainMonitor = mainMap.getLeaf(TempRef_v);
			mainSet = mainMap.getSet(TempRef_v);
			if (mainSet == null){
				mainSet = new SafeEnumMonitor_Set();
				mainMap.putSet(TempRef_v, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new SafeEnumMonitor();
				mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				mainMonitor.monitorInfo.isFullParam = false;

				mainMonitor.RVMRef_v = TempRef_v;

				SafeEnum_v_e_Map.putLeaf(TempRef_v, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = SafeEnum_timestamp;
				if (TempRef_v.getTau() == -1){
					TempRef_v.setTau(SafeEnum_timestamp);
				}
				SafeEnum_timestamp++;

				SafeEnum__To__v_Set.add(mainMonitor);
			}

			SafeEnum_v_Map_cachekey_0 = TempRef_v;
			SafeEnum_v_Map_cacheset = mainSet;
			SafeEnum_v_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_updatesource(v);
			violationProp1 = mainSet.violationProp1;
		}
		SafeEnum_RVMLock.unlock();
	}

	public static void nextEvent(Enumeration e) {
		SafeEnum_activated = true;
		while (!SafeEnum_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeEnumMonitor mainMonitor = null;
		SafeEnumMonitor origMonitor = null;
		SafeEnumMonitor lastMonitor = null;
		SafeEnumMonitor_Set mainSet = null;
		SafeEnumMonitor_Set origSet = null;
		SafeEnumMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_v;
		CachedTagWeakReference TempRef_e;

		// Cache Retrieval
		if (SafeEnum_e_Map_cachekey_1 != null && e == SafeEnum_e_Map_cachekey_1.get()) {
			TempRef_e = SafeEnum_e_Map_cachekey_1;

			mainSet = SafeEnum_e_Map_cacheset;
			mainMonitor = SafeEnum_e_Map_cachenode;
		} else {
			TempRef_e = SafeEnum_Enumeration_RefMap.findOrCreateWeakRef(e);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor> mainMap = SafeEnum_e_Map;
			mainMonitor = mainMap.getLeaf(TempRef_e);
			mainSet = mainMap.getSet(TempRef_e);
			if (mainSet == null){
				mainSet = new SafeEnumMonitor_Set();
				mainMap.putSet(TempRef_e, mainSet);
			}

			if (mainMonitor == null) {
				origSet = SafeEnum__To__v_Set;
				if (origSet!= null) {
					int numAlive = 0;
					for(int i = 0; i < origSet.getSize(); i++) {
						origMonitor = origSet.get(i);
						Vector v = (Vector)origMonitor.RVMRef_v.get();
						if (!origMonitor.isTerminated() && v != null) {
							origSet.set(numAlive, origMonitor);
							numAlive++;

							TempRef_v = origMonitor.RVMRef_v;

							MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> tempMap1 = SafeEnum_v_e_Map;
							MapOfMonitor<SafeEnumMonitor> obj1 = tempMap1.getMap(TempRef_v);
							if (obj1 == null) {
								obj1 = new MapOfMonitor<SafeEnumMonitor>(1);
								tempMap1.putMap(TempRef_v, obj1);
							}
							MapOfMonitor<SafeEnumMonitor> lastMap1 = obj1;
							lastMonitor = lastMap1.getLeaf(TempRef_e);
							if (lastMonitor == null) {
								boolean timeCheck = true;

								if (TempRef_e.getDisabled() > origMonitor.tau|| (TempRef_e.getTau() > 0 && TempRef_e.getTau() < origMonitor.tau)) {
									timeCheck = false;
								}

								if (timeCheck) {
									lastMonitor = (SafeEnumMonitor)origMonitor.clone();
									lastMonitor.RVMRef_e = TempRef_e;
									if (TempRef_e.getTau() == -1){
										TempRef_e.setTau(origMonitor.tau);
									}
									lastMap1.putLeaf(TempRef_e, lastMonitor);
									lastMonitor.monitorInfo.isFullParam = true;

									MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> tempMap2 = SafeEnum_v_e_Map;
									SafeEnumMonitor_Set obj2 = tempMap2.getSet(TempRef_v);
									monitors = obj2;
									if (monitors == null) {
										monitors = new SafeEnumMonitor_Set();
										tempMap2.putSet(TempRef_v, monitors);
									}
									monitors.add(lastMonitor);

									MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor> tempMap3 = SafeEnum_e_Map;
									SafeEnumMonitor_Set obj3 = tempMap3.getSet(TempRef_e);
									mainSet = obj3;
									if (mainSet == null) {
										mainSet = new SafeEnumMonitor_Set();
										tempMap3.putSet(TempRef_e, mainSet);
									}
									mainSet.add(lastMonitor);
								}
							}
						}
					}

					origSet.eraseRange(numAlive);
				}
				if (mainMonitor == null) {
					mainMonitor = new SafeEnumMonitor();
					mainMonitor.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
					mainMonitor.monitorInfo.isFullParam = false;

					mainMonitor.RVMRef_e = TempRef_e;

					SafeEnum_e_Map.putLeaf(TempRef_e, mainMonitor);
					mainSet.add(mainMonitor);
					mainMonitor.tau = SafeEnum_timestamp;
					if (TempRef_e.getTau() == -1){
						TempRef_e.setTau(SafeEnum_timestamp);
					}
					SafeEnum_timestamp++;
				}

				TempRef_e.setDisabled(SafeEnum_timestamp);
				SafeEnum_timestamp++;
			}

			SafeEnum_e_Map_cachekey_1 = TempRef_e;
			SafeEnum_e_Map_cacheset = mainSet;
			SafeEnum_e_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_next(e);
			violationProp1 = mainSet.violationProp1;
		}
		SafeEnum_RVMLock.unlock();
	}

}
