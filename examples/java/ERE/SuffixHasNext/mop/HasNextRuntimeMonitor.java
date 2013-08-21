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

class HasNextSuffixMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<HasNextSuffixMonitor> {
	boolean matchProp1;

	HasNextSuffixMonitor_Set(){
		this.size = 0;
		this.elements = new HasNextSuffixMonitor[4];
	}

	final void event_hasnext(Iterator i) {
		this.matchProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			HasNextSuffixMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.event_hasnext(i);
				matchProp1 |= monitor.matchProp1;
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
			HasNextSuffixMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.event_next(i);
				matchProp1 |= monitor.matchProp1;
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
}

class HasNextSuffixMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	boolean matchProp1;
	Vector<HasNextMonitor> monitorList = new Vector<HasNextMonitor>();
	protected Object clone() {
		try {
			HasNextSuffixMonitor ret = (HasNextSuffixMonitor) super.clone();
			ret.monitorList = new Vector<HasNextMonitor>();
			for(HasNextMonitor monitor : this.monitorList){
				HasNextMonitor newMonitor = (HasNextMonitor)monitor.clone();
				ret.monitorList.add(newMonitor);
			}
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	final void event_hasnext(Iterator i) {
		matchProp1 = false;
		RVM_lastevent = 0;
		HashSet monitorSet = new HashSet();
		HasNextMonitor newMonitor = new HasNextMonitor();
		monitorList.add(newMonitor);
		Iterator it = monitorList.iterator();
		while (it.hasNext()){
			HasNextMonitor monitor = (HasNextMonitor)it.next();
			monitor.Prop_1_event_hasnext(i);
			matchProp1 |= monitor.Prop_1_Category_match;
			if(monitor.Prop_1_Category_match) {
				monitor.Prop_1_handler_match(i);
			}
			if(monitorSet.contains(monitor) || monitor.Prop_1_Category_match ) {
				it.remove();
			} else {
				monitorSet.add(monitor);
			}
		}
	}

	final void event_next(Iterator i) {
		matchProp1 = false;
		RVM_lastevent = 1;
		HashSet monitorSet = new HashSet();
		HasNextMonitor newMonitor = new HasNextMonitor();
		monitorList.add(newMonitor);
		Iterator it = monitorList.iterator();
		while (it.hasNext()){
			HasNextMonitor monitor = (HasNextMonitor)it.next();
			monitor.Prop_1_event_next(i);
			matchProp1 |= monitor.Prop_1_Category_match;
			if(monitor.Prop_1_Category_match) {
				monitor.Prop_1_handler_match(i);
			}
			if(monitorSet.contains(monitor) || monitor.Prop_1_Category_match ) {
				it.remove();
			} else {
				monitorSet.add(monitor);
			}
		}
	}

	CachedWeakReference RVMRef_i;

	//alive_parameters_0 = [Iterator i]
	boolean alive_parameters_0 = true;

	@Override
	protected final void terminateInternal(int idnum) {
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
		}
		switch(RVM_lastevent) {
			case -1:
			return;
			case 0:
			//hasnext
			return;
			case 1:
			//next
			//alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

class HasNextMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
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
	static final int Prop_1_transition_hasnext[] = {3, 3, 3, 3};;
	static final int Prop_1_transition_next[] = {2, 3, 1, 3};;

	boolean Prop_1_Category_match = false;

	HasNextMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_hasnext(Iterator i) {

		Prop_1_state = Prop_1_transition_hasnext[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 1;
		return true;
	}

	final boolean Prop_1_event_next(Iterator i) {

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 1;
		return true;
	}

	final void Prop_1_handler_match (Iterator i){
		{
			System.err.println("! hasNext() not called before calling next()");
			this.reset();
		}

	}

	final void reset() {
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

}

public class HasNextRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean matchProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager HasNextMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		HasNextMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		HasNextMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock HasNext_RVMLock = new ReentrantLock();
	static Condition HasNext_RVMLock_cond = HasNext_RVMLock.newCondition();

	private static boolean HasNext_activated = false;

	// Declarations for Indexing Trees
	static BasicRefMapOfMonitor<HasNextSuffixMonitor> HasNext_i_Map = new BasicRefMapOfMonitor<HasNextSuffixMonitor>(0);
	static CachedWeakReference HasNext_i_Map_cachekey_0 = null;
	static HasNextSuffixMonitor HasNext_i_Map_cachenode = null;

	// Trees for References
	static BasicRefMapOfMonitor<HasNextSuffixMonitor> HasNext_Iterator_RefMap = HasNext_i_Map;

	public static void hasnextEvent(Iterator i) {
		HasNext_activated = true;
		while (!HasNext_RVMLock.tryLock()) {
			Thread.yield();
		}
		HasNextSuffixMonitor mainMonitor = null;
		CachedWeakReference TempRef_i;

		matchProp1 = false;
		// Cache Retrieval
		if (HasNext_i_Map_cachekey_0 != null && i == HasNext_i_Map_cachekey_0.get()) {
			TempRef_i = HasNext_i_Map_cachekey_0;

			mainMonitor = HasNext_i_Map_cachenode;
		} else {
			TempRef_i = HasNext_i_Map.findOrCreateWeakRef(i);
		}

		if (mainMonitor == null) {
			BasicRefMapOfMonitor<HasNextSuffixMonitor> mainMap = HasNext_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);

			if (mainMonitor == null) {
				mainMonitor = new HasNextSuffixMonitor();

				mainMonitor.RVMRef_i = TempRef_i;

				HasNext_i_Map.putLeaf(TempRef_i, mainMonitor);
			}

			HasNext_i_Map_cachekey_0 = TempRef_i;
			HasNext_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.event_hasnext(i);
		matchProp1 |= mainMonitor.matchProp1;
		HasNext_RVMLock.unlock();
	}

	public static void nextEvent(Iterator i) {
		HasNext_activated = true;
		while (!HasNext_RVMLock.tryLock()) {
			Thread.yield();
		}
		HasNextSuffixMonitor mainMonitor = null;
		CachedWeakReference TempRef_i;

		matchProp1 = false;
		// Cache Retrieval
		if (HasNext_i_Map_cachekey_0 != null && i == HasNext_i_Map_cachekey_0.get()) {
			TempRef_i = HasNext_i_Map_cachekey_0;

			mainMonitor = HasNext_i_Map_cachenode;
		} else {
			TempRef_i = HasNext_i_Map.findOrCreateWeakRef(i);
		}

		if (mainMonitor == null) {
			BasicRefMapOfMonitor<HasNextSuffixMonitor> mainMap = HasNext_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);

			if (mainMonitor == null) {
				mainMonitor = new HasNextSuffixMonitor();

				mainMonitor.RVMRef_i = TempRef_i;

				HasNext_i_Map.putLeaf(TempRef_i, mainMonitor);
			}

			HasNext_i_Map_cachekey_0 = TempRef_i;
			HasNext_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.event_next(i);
		matchProp1 |= mainMonitor.matchProp1;
		HasNext_RVMLock.unlock();
	}

}
