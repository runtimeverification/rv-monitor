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

class UnsafeIteratorMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<UnsafeIteratorMonitor> {
	boolean matchProp1;

	UnsafeIteratorMonitor_Set(){
		this.size = 0;
		this.elements = new UnsafeIteratorMonitor[4];
	}

	final void event_create(Collection c, Iterator i) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_create(c, i);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(c, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_updatesource(Collection c) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_updatesource(c);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(c, null);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_next(Iterator i) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeIteratorMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_next(i);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(null, i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
}

class UnsafeIteratorMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	long tau = -1;
	protected Object clone() {
		try {
			UnsafeIteratorMonitor ret = (UnsafeIteratorMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int Prop_1_state;
	static final int Prop_1_transition_create[] = {1, 4, 4, 4, 4};;
	static final int Prop_1_transition_updatesource[] = {4, 2, 2, 4, 4};;
	static final int Prop_1_transition_next[] = {4, 1, 3, 4, 4};;

	boolean Prop_1_Category_match = false;

	UnsafeIteratorMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_create(Collection c, Iterator i) {
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_create[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final boolean Prop_1_event_updatesource(Collection c) {
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_updatesource[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final boolean Prop_1_event_next(Iterator i) {
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final void Prop_1_handler_match (Collection c, Iterator i){
		{
			System.out.println("improper iterator usage");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
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

}

public class UnsafeIteratorRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean matchProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager UnsafeIteratorMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		UnsafeIteratorMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		UnsafeIteratorMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock UnsafeIterator_RVMLock = new ReentrantLock();
	static Condition UnsafeIterator_RVMLock_cond = UnsafeIterator_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long UnsafeIterator_timestamp = 1;

	private static boolean UnsafeIterator_activated = false;

	// Declarations for Indexing Trees
	static CachedTagWeakReference UnsafeIterator_c_Map_cachekey_0 = null;
	static UnsafeIteratorMonitor_Set UnsafeIterator_c_Map_cacheset = null;
	static UnsafeIteratorMonitor UnsafeIterator_c_Map_cachenode = null;
	static MapOfAll<MapOfMonitor<UnsafeIteratorMonitor>, UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor> UnsafeIterator_c_i_Map = new MapOfAll<MapOfMonitor<UnsafeIteratorMonitor>, UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor>(0);
	static CachedTagWeakReference UnsafeIterator_c_i_Map_cachekey_0 = null;
	static CachedTagWeakReference UnsafeIterator_c_i_Map_cachekey_1 = null;
	static UnsafeIteratorMonitor UnsafeIterator_c_i_Map_cachenode = null;
	static MapOfSetMonitor<UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor> UnsafeIterator_i_Map = new MapOfSetMonitor<UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor>(1);
	static CachedTagWeakReference UnsafeIterator_i_Map_cachekey_1 = null;
	static UnsafeIteratorMonitor_Set UnsafeIterator_i_Map_cacheset = null;
	static UnsafeIteratorMonitor UnsafeIterator_i_Map_cachenode = null;

	// Trees for References
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap UnsafeIterator_Collection_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap UnsafeIterator_Iterator_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

	public static void createEvent(Collection c, Iterator i) {
		UnsafeIterator_activated = true;
		while (!UnsafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		UnsafeIteratorMonitor mainMonitor = null;
		UnsafeIteratorMonitor_Set monitors = null;
		CachedTagWeakReference TempRef_c;
		CachedTagWeakReference TempRef_i;

		matchProp1 = false;
		// Cache Retrieval
		if (UnsafeIterator_c_i_Map_cachekey_0 != null && c == UnsafeIterator_c_i_Map_cachekey_0.get() && UnsafeIterator_c_i_Map_cachekey_1 != null && i == UnsafeIterator_c_i_Map_cachekey_1.get()) {
			TempRef_c = UnsafeIterator_c_i_Map_cachekey_0;
			TempRef_i = UnsafeIterator_c_i_Map_cachekey_1;

			mainMonitor = UnsafeIterator_c_i_Map_cachenode;
		} else {
			TempRef_c = UnsafeIterator_Collection_RefMap.findOrCreateWeakRef(c);
			TempRef_i = UnsafeIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainMonitor == null) {
			MapOfAll<MapOfMonitor<UnsafeIteratorMonitor>, UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor> tempMap1 = UnsafeIterator_c_i_Map;
			MapOfMonitor<UnsafeIteratorMonitor> obj1 = tempMap1.getMap(TempRef_c);
			if (obj1 == null) {
				obj1 = new MapOfMonitor<UnsafeIteratorMonitor>(1);
				tempMap1.putMap(TempRef_c, obj1);
			}
			MapOfMonitor<UnsafeIteratorMonitor> mainMap = obj1;
			mainMonitor = mainMap.getLeaf(TempRef_i);

			if (mainMonitor == null) {
				mainMonitor = new UnsafeIteratorMonitor();

				mainMonitor.RVMRef_c = TempRef_c;
				mainMonitor.RVMRef_i = TempRef_i;

				mainMap.putLeaf(TempRef_i, mainMonitor);
				mainMonitor.tau = UnsafeIterator_timestamp;
				if (TempRef_c.getTau() == -1){
					TempRef_c.setTau(UnsafeIterator_timestamp);
				}
				if (TempRef_i.getTau() == -1){
					TempRef_i.setTau(UnsafeIterator_timestamp);
				}
				UnsafeIterator_timestamp++;

				MapOfAll<MapOfMonitor<UnsafeIteratorMonitor>, UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor> tempMap2 = UnsafeIterator_c_i_Map;
				UnsafeIteratorMonitor_Set obj2 = tempMap2.getSet(TempRef_c);
				monitors = obj2;
				if (monitors == null) {
					monitors = new UnsafeIteratorMonitor_Set();
					tempMap2.putSet(TempRef_c, monitors);
				}
				monitors.add(mainMonitor);

				MapOfSetMonitor<UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor> tempMap3 = UnsafeIterator_i_Map;
				UnsafeIteratorMonitor_Set obj3 = tempMap3.getSet(TempRef_i);
				monitors = obj3;
				if (monitors == null) {
					monitors = new UnsafeIteratorMonitor_Set();
					tempMap3.putSet(TempRef_i, monitors);
				}
				monitors.add(mainMonitor);
			}

			UnsafeIterator_c_i_Map_cachekey_0 = TempRef_c;
			UnsafeIterator_c_i_Map_cachekey_1 = TempRef_i;
			UnsafeIterator_c_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_create(c, i);
		matchProp1 |= mainMonitor.Prop_1_Category_match;
		if(mainMonitor.Prop_1_Category_match) {
			mainMonitor.Prop_1_handler_match(c, i);
		}
		UnsafeIterator_RVMLock.unlock();
	}

	public static void updatesourceEvent(Collection c) {
		UnsafeIterator_activated = true;
		while (!UnsafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		UnsafeIteratorMonitor mainMonitor = null;
		UnsafeIteratorMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_c;

		// Cache Retrieval
		if (UnsafeIterator_c_Map_cachekey_0 != null && c == UnsafeIterator_c_Map_cachekey_0.get()) {
			TempRef_c = UnsafeIterator_c_Map_cachekey_0;

			mainSet = UnsafeIterator_c_Map_cacheset;
			mainMonitor = UnsafeIterator_c_Map_cachenode;
		} else {
			TempRef_c = UnsafeIterator_Collection_RefMap.findOrCreateWeakRef(c);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfMonitor<UnsafeIteratorMonitor>, UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor> mainMap = UnsafeIterator_c_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_c);
			mainSet = mainMap.getSet(TempRef_c);
			if (mainSet == null){
				mainSet = new UnsafeIteratorMonitor_Set();
				mainMap.putSet(TempRef_c, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new UnsafeIteratorMonitor();

				mainMonitor.RVMRef_c = TempRef_c;

				UnsafeIterator_c_i_Map.putLeaf(TempRef_c, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = UnsafeIterator_timestamp;
				if (TempRef_c.getTau() == -1){
					TempRef_c.setTau(UnsafeIterator_timestamp);
				}
				UnsafeIterator_timestamp++;
			}

			UnsafeIterator_c_Map_cachekey_0 = TempRef_c;
			UnsafeIterator_c_Map_cacheset = mainSet;
			UnsafeIterator_c_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_updatesource(c);
			matchProp1 = mainSet.matchProp1;
		}
		UnsafeIterator_RVMLock.unlock();
	}

	public static void nextEvent(Iterator i) {
		UnsafeIterator_activated = true;
		while (!UnsafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}
		UnsafeIteratorMonitor mainMonitor = null;
		UnsafeIteratorMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_i;

		// Cache Retrieval
		if (UnsafeIterator_i_Map_cachekey_1 != null && i == UnsafeIterator_i_Map_cachekey_1.get()) {
			TempRef_i = UnsafeIterator_i_Map_cachekey_1;

			mainSet = UnsafeIterator_i_Map_cacheset;
			mainMonitor = UnsafeIterator_i_Map_cachenode;
		} else {
			TempRef_i = UnsafeIterator_Iterator_RefMap.findOrCreateWeakRef(i);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfSetMonitor<UnsafeIteratorMonitor_Set, UnsafeIteratorMonitor> mainMap = UnsafeIterator_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);
			mainSet = mainMap.getSet(TempRef_i);
			if (mainSet == null){
				mainSet = new UnsafeIteratorMonitor_Set();
				mainMap.putSet(TempRef_i, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new UnsafeIteratorMonitor();

				mainMonitor.RVMRef_i = TempRef_i;

				UnsafeIterator_i_Map.putLeaf(TempRef_i, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = UnsafeIterator_timestamp;
				if (TempRef_i.getTau() == -1){
					TempRef_i.setTau(UnsafeIterator_timestamp);
				}
				UnsafeIterator_timestamp++;
			}

			UnsafeIterator_i_Map_cachekey_1 = TempRef_i;
			UnsafeIterator_i_Map_cacheset = mainSet;
			UnsafeIterator_i_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_next(i);
			matchProp1 = mainSet.matchProp1;
		}
		UnsafeIterator_RVMLock.unlock();
	}

}
