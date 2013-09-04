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

class SafeSyncCollectionMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeSyncCollectionMonitor> {
	boolean matchProp1;

	SafeSyncCollectionMonitor_Set(){
		this.size = 0;
		this.elements = new SafeSyncCollectionMonitor[4];
	}

	final void event_sync(Object c) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncCollectionMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_sync(c);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(c, null);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_syncCreateIter(Object c, Iterator iter) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncCollectionMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_syncCreateIter(c, iter);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(c, iter);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_asyncCreateIter(Object c, Iterator iter) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncCollectionMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_asyncCreateIter(c, iter);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(c, iter);
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
			SafeSyncCollectionMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_accessIter(iter);
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(null, iter);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
}

class SafeSyncCollectionMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	long tau = -1;
	protected Object clone() {
		try {
			SafeSyncCollectionMonitor ret = (SafeSyncCollectionMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	Object c;

	int Prop_1_state;
	static final int Prop_1_transition_sync[] = {1, 4, 4, 4, 4};;
	static final int Prop_1_transition_syncCreateIter[] = {4, 3, 4, 4, 4};;
	static final int Prop_1_transition_asyncCreateIter[] = {4, 2, 4, 4, 4};;
	static final int Prop_1_transition_accessIter[] = {4, 4, 4, 2, 4};;

	boolean Prop_1_Category_match = false;

	SafeSyncCollectionMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_sync(Object c) {
		{
			this.c = c;
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_sync[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 2;
		return true;
	}

	final boolean Prop_1_event_syncCreateIter(Object c, Iterator iter) {
		{
			if (!Thread.holdsLock(c)) return true;
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_syncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 2;
		return true;
	}

	final boolean Prop_1_event_asyncCreateIter(Object c, Iterator iter) {
		{
			if (Thread.holdsLock(c)) return true;
		}
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_asyncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 2;
		return true;
	}

	final boolean Prop_1_event_accessIter(Iterator iter) {
		{
			if (Thread.holdsLock(this.c)) return true;
		}
		RVM_lastevent = 3;

		Prop_1_state = Prop_1_transition_accessIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 2;
		return true;
	}

	final void Prop_1_handler_match (Object c, Iterator iter){
		{
			System.out.println("pattern matched!");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	CachedTagWeakReference RVMRef_c;
	CachedTagWeakReference RVMRef_iter;

	//alive_parameters_0 = [Object c, Iterator iter]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Iterator iter]
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
			//sync
			//alive_c && alive_iter
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//syncCreateIter
			//alive_iter
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//asyncCreateIter
			return;
			case 3:
			//accessIter
			return;
		}
		return;
	}

}

public class SafeSyncCollectionRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean matchProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeSyncCollectionMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		SafeSyncCollectionMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeSyncCollectionMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock SafeSyncCollection_RVMLock = new ReentrantLock();
	static Condition SafeSyncCollection_RVMLock_cond = SafeSyncCollection_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeSyncCollection_timestamp = 1;

	private static boolean SafeSyncCollection_activated = false;

	// Declarations for Indexing Trees
	static CachedTagWeakReference SafeSyncCollection_c_Map_cachekey_0 = null;
	static SafeSyncCollectionMonitor_Set SafeSyncCollection_c_Map_cacheset = null;
	static SafeSyncCollectionMonitor SafeSyncCollection_c_Map_cachenode = null;
	static MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> SafeSyncCollection_c_iter_Map = new MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor>(0);
	static CachedTagWeakReference SafeSyncCollection_c_iter_Map_cachekey_0 = null;
	static CachedTagWeakReference SafeSyncCollection_c_iter_Map_cachekey_1 = null;
	static SafeSyncCollectionMonitor SafeSyncCollection_c_iter_Map_cachenode = null;
	static MapOfSetMonitor<SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> SafeSyncCollection_iter_Map = new MapOfSetMonitor<SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor>(1);
	static CachedTagWeakReference SafeSyncCollection_iter_Map_cachekey_1 = null;
	static SafeSyncCollectionMonitor_Set SafeSyncCollection_iter_Map_cacheset = null;
	static SafeSyncCollectionMonitor SafeSyncCollection_iter_Map_cachenode = null;

	// Trees for References
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeSyncCollection_Iterator_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
	static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeSyncCollection_Object_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

	public static void syncEvent(Object c) {
		SafeSyncCollection_activated = true;
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeSyncCollectionMonitor mainMonitor = null;
		SafeSyncCollectionMonitor_Set mainSet = null;
		CachedTagWeakReference TempRef_c;

		// Cache Retrieval
		if (SafeSyncCollection_c_Map_cachekey_0 != null && c == SafeSyncCollection_c_Map_cachekey_0.get()) {
			TempRef_c = SafeSyncCollection_c_Map_cachekey_0;

			mainSet = SafeSyncCollection_c_Map_cacheset;
			mainMonitor = SafeSyncCollection_c_Map_cachenode;
		} else {
			TempRef_c = SafeSyncCollection_Object_RefMap.findOrCreateWeakRef(c);
		}

		if (mainSet == null || mainMonitor == null) {
			MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> mainMap = SafeSyncCollection_c_iter_Map;
			mainMonitor = mainMap.getLeaf(TempRef_c);
			mainSet = mainMap.getSet(TempRef_c);
			if (mainSet == null){
				mainSet = new SafeSyncCollectionMonitor_Set();
				mainMap.putSet(TempRef_c, mainSet);
			}

			if (mainMonitor == null) {
				mainMonitor = new SafeSyncCollectionMonitor();

				mainMonitor.RVMRef_c = TempRef_c;

				SafeSyncCollection_c_iter_Map.putLeaf(TempRef_c, mainMonitor);
				mainSet.add(mainMonitor);
				mainMonitor.tau = SafeSyncCollection_timestamp;
				if (TempRef_c.getTau() == -1){
					TempRef_c.setTau(SafeSyncCollection_timestamp);
				}
				SafeSyncCollection_timestamp++;
			}

			SafeSyncCollection_c_Map_cachekey_0 = TempRef_c;
			SafeSyncCollection_c_Map_cacheset = mainSet;
			SafeSyncCollection_c_Map_cachenode = mainMonitor;
		}

		if(mainSet != null) {
			mainSet.event_sync(c);
			matchProp1 = mainSet.matchProp1;
		}
		SafeSyncCollection_RVMLock.unlock();
	}

	public static void syncCreateIterEvent(Object c, Iterator iter) {
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}
		if (SafeSyncCollection_activated) {
			SafeSyncCollectionMonitor mainMonitor = null;
			SafeSyncCollectionMonitor origMonitor = null;
			SafeSyncCollectionMonitor_Set monitors = null;
			CachedTagWeakReference TempRef_c;
			CachedTagWeakReference TempRef_iter;

			matchProp1 = false;
			// Cache Retrieval
			if (SafeSyncCollection_c_iter_Map_cachekey_0 != null && c == SafeSyncCollection_c_iter_Map_cachekey_0.get() && SafeSyncCollection_c_iter_Map_cachekey_1 != null && iter == SafeSyncCollection_c_iter_Map_cachekey_1.get()) {
				TempRef_c = SafeSyncCollection_c_iter_Map_cachekey_0;
				TempRef_iter = SafeSyncCollection_c_iter_Map_cachekey_1;

				mainMonitor = SafeSyncCollection_c_iter_Map_cachenode;
			} else {
				TempRef_c = SafeSyncCollection_Object_RefMap.findOrCreateWeakRef(c);
				TempRef_iter = SafeSyncCollection_Iterator_RefMap.findOrCreateWeakRef(iter);
			}

			if (mainMonitor == null) {
				MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap1 = SafeSyncCollection_c_iter_Map;
				MapOfMonitor<SafeSyncCollectionMonitor> obj1 = tempMap1.getMap(TempRef_c);
				if (obj1 != null) {
					MapOfMonitor<SafeSyncCollectionMonitor> mainMap = obj1;
					mainMonitor = mainMap.getLeaf(TempRef_iter);
				}

				if (mainMonitor == null) {
					if (mainMonitor == null) {
						MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> origMap1 = SafeSyncCollection_c_iter_Map;
						origMonitor = origMap1.getLeaf(TempRef_c);
						if (origMonitor != null) {
							boolean timeCheck = true;

							if (TempRef_iter.getDisabled() > origMonitor.tau) {
								timeCheck = false;
							}

							if (timeCheck) {
								mainMonitor = (SafeSyncCollectionMonitor)origMonitor.clone();
								mainMonitor.RVMRef_iter = TempRef_iter;
								if (TempRef_iter.getTau() == -1){
									TempRef_iter.setTau(origMonitor.tau);
								}
								MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap2 = SafeSyncCollection_c_iter_Map;
								MapOfMonitor<SafeSyncCollectionMonitor> obj2 = tempMap2.getMap(TempRef_c);
								if (obj2 == null) {
									obj2 = new MapOfMonitor<SafeSyncCollectionMonitor>(1);
									tempMap2.putMap(TempRef_c, obj2);
								}
								MapOfMonitor<SafeSyncCollectionMonitor> tempMap3 = obj2;
								tempMap3.putLeaf(TempRef_iter, mainMonitor);

								MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap4 = SafeSyncCollection_c_iter_Map;
								SafeSyncCollectionMonitor_Set obj3 = tempMap4.getSet(TempRef_c);
								monitors = obj3;
								if (monitors == null) {
									monitors = new SafeSyncCollectionMonitor_Set();
									tempMap4.putSet(TempRef_c, monitors);
								}
								monitors.add(mainMonitor);

								MapOfSetMonitor<SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap5 = SafeSyncCollection_iter_Map;
								SafeSyncCollectionMonitor_Set obj4 = tempMap5.getSet(TempRef_iter);
								monitors = obj4;
								if (monitors == null) {
									monitors = new SafeSyncCollectionMonitor_Set();
									tempMap5.putSet(TempRef_iter, monitors);
								}
								monitors.add(mainMonitor);
							}
						}
					}

					TempRef_iter.setDisabled(SafeSyncCollection_timestamp);
					SafeSyncCollection_timestamp++;
				}

				SafeSyncCollection_c_iter_Map_cachekey_0 = TempRef_c;
				SafeSyncCollection_c_iter_Map_cachekey_1 = TempRef_iter;
				SafeSyncCollection_c_iter_Map_cachenode = mainMonitor;
			}

			if (mainMonitor != null ) {
				mainMonitor.Prop_1_event_syncCreateIter(c, iter);
				matchProp1 |= mainMonitor.Prop_1_Category_match;
				if(mainMonitor.Prop_1_Category_match) {
					mainMonitor.Prop_1_handler_match(c, iter);
				}
			}
		}
		SafeSyncCollection_RVMLock.unlock();
	}

	public static void asyncCreateIterEvent(Object c, Iterator iter) {
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}
		if (SafeSyncCollection_activated) {
			SafeSyncCollectionMonitor mainMonitor = null;
			SafeSyncCollectionMonitor origMonitor = null;
			SafeSyncCollectionMonitor_Set monitors = null;
			CachedTagWeakReference TempRef_c;
			CachedTagWeakReference TempRef_iter;

			matchProp1 = false;
			// Cache Retrieval
			if (SafeSyncCollection_c_iter_Map_cachekey_0 != null && c == SafeSyncCollection_c_iter_Map_cachekey_0.get() && SafeSyncCollection_c_iter_Map_cachekey_1 != null && iter == SafeSyncCollection_c_iter_Map_cachekey_1.get()) {
				TempRef_c = SafeSyncCollection_c_iter_Map_cachekey_0;
				TempRef_iter = SafeSyncCollection_c_iter_Map_cachekey_1;

				mainMonitor = SafeSyncCollection_c_iter_Map_cachenode;
			} else {
				TempRef_c = SafeSyncCollection_Object_RefMap.findOrCreateWeakRef(c);
				TempRef_iter = SafeSyncCollection_Iterator_RefMap.findOrCreateWeakRef(iter);
			}

			if (mainMonitor == null) {
				MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap1 = SafeSyncCollection_c_iter_Map;
				MapOfMonitor<SafeSyncCollectionMonitor> obj1 = tempMap1.getMap(TempRef_c);
				if (obj1 != null) {
					MapOfMonitor<SafeSyncCollectionMonitor> mainMap = obj1;
					mainMonitor = mainMap.getLeaf(TempRef_iter);
				}

				if (mainMonitor == null) {
					if (mainMonitor == null) {
						MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> origMap1 = SafeSyncCollection_c_iter_Map;
						origMonitor = origMap1.getLeaf(TempRef_c);
						if (origMonitor != null) {
							boolean timeCheck = true;

							if (TempRef_iter.getDisabled() > origMonitor.tau) {
								timeCheck = false;
							}

							if (timeCheck) {
								mainMonitor = (SafeSyncCollectionMonitor)origMonitor.clone();
								mainMonitor.RVMRef_iter = TempRef_iter;
								if (TempRef_iter.getTau() == -1){
									TempRef_iter.setTau(origMonitor.tau);
								}
								MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap2 = SafeSyncCollection_c_iter_Map;
								MapOfMonitor<SafeSyncCollectionMonitor> obj2 = tempMap2.getMap(TempRef_c);
								if (obj2 == null) {
									obj2 = new MapOfMonitor<SafeSyncCollectionMonitor>(1);
									tempMap2.putMap(TempRef_c, obj2);
								}
								MapOfMonitor<SafeSyncCollectionMonitor> tempMap3 = obj2;
								tempMap3.putLeaf(TempRef_iter, mainMonitor);

								MapOfAll<MapOfMonitor<SafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap4 = SafeSyncCollection_c_iter_Map;
								SafeSyncCollectionMonitor_Set obj3 = tempMap4.getSet(TempRef_c);
								monitors = obj3;
								if (monitors == null) {
									monitors = new SafeSyncCollectionMonitor_Set();
									tempMap4.putSet(TempRef_c, monitors);
								}
								monitors.add(mainMonitor);

								MapOfSetMonitor<SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> tempMap5 = SafeSyncCollection_iter_Map;
								SafeSyncCollectionMonitor_Set obj4 = tempMap5.getSet(TempRef_iter);
								monitors = obj4;
								if (monitors == null) {
									monitors = new SafeSyncCollectionMonitor_Set();
									tempMap5.putSet(TempRef_iter, monitors);
								}
								monitors.add(mainMonitor);
							}
						}
					}

					TempRef_iter.setDisabled(SafeSyncCollection_timestamp);
					SafeSyncCollection_timestamp++;
				}

				SafeSyncCollection_c_iter_Map_cachekey_0 = TempRef_c;
				SafeSyncCollection_c_iter_Map_cachekey_1 = TempRef_iter;
				SafeSyncCollection_c_iter_Map_cachenode = mainMonitor;
			}

			if (mainMonitor != null ) {
				mainMonitor.Prop_1_event_asyncCreateIter(c, iter);
				matchProp1 |= mainMonitor.Prop_1_Category_match;
				if(mainMonitor.Prop_1_Category_match) {
					mainMonitor.Prop_1_handler_match(c, iter);
				}
			}
		}
		SafeSyncCollection_RVMLock.unlock();
	}

	public static void accessIterEvent(Iterator iter) {
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}
		if (SafeSyncCollection_activated) {
			SafeSyncCollectionMonitor mainMonitor = null;
			SafeSyncCollectionMonitor_Set mainSet = null;
			CachedTagWeakReference TempRef_iter;

			// Cache Retrieval
			if (SafeSyncCollection_iter_Map_cachekey_1 != null && iter == SafeSyncCollection_iter_Map_cachekey_1.get()) {
				TempRef_iter = SafeSyncCollection_iter_Map_cachekey_1;

				mainSet = SafeSyncCollection_iter_Map_cacheset;
				mainMonitor = SafeSyncCollection_iter_Map_cachenode;
			} else {
				TempRef_iter = SafeSyncCollection_Iterator_RefMap.findOrCreateWeakRef(iter);
			}

			if (mainSet == null || mainMonitor == null) {
				MapOfSetMonitor<SafeSyncCollectionMonitor_Set, SafeSyncCollectionMonitor> mainMap = SafeSyncCollection_iter_Map;
				mainMonitor = mainMap.getLeaf(TempRef_iter);
				mainSet = mainMap.getSet(TempRef_iter);

				if (mainMonitor == null) {

					TempRef_iter.setDisabled(SafeSyncCollection_timestamp);
					SafeSyncCollection_timestamp++;
				}

				SafeSyncCollection_iter_Map_cachekey_1 = TempRef_iter;
				SafeSyncCollection_iter_Map_cacheset = mainSet;
				SafeSyncCollection_iter_Map_cachenode = mainMonitor;
			}

			if(mainSet != null) {
				mainSet.event_accessIter(iter);
				matchProp1 = mainSet.matchProp1;
			}
		}
		SafeSyncCollection_RVMLock.unlock();
	}

}
