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

class HasNextMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<HasNextMonitor> {
	boolean failProp1;

	HasNextMonitor_Set(){
		this.size = 0;
		this.elements = new HasNextMonitor[4];
	}

	final void event_hasnext(Iterator i) {
		this.failProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			HasNextMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_hasnext(i);
				failProp1 |= monitor.Prop_1_Category_fail;
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}

	final void event_next(Iterator i) {
		this.failProp1 = false;
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			HasNextMonitor monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_next(i);
				failProp1 |= monitor.Prop_1_Category_fail;
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(i);
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
}

class HasNextMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
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

	final boolean Prop_1_event_hasnext(Iterator i) {
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_hasnext[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 2;
		return true;
	}

	final boolean Prop_1_event_next(Iterator i) {
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 2;
		return true;
	}

	final void Prop_1_handler_fail (Iterator i){
		{
			System.out.println("! hasNext() has not been called" + " before calling next() for an" + " iterator");
			this.reset();
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_fail = false;
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
			//alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

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

public class HasNextRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean failProp1 = false;
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
	static BasicRefMapOfMonitor<HasNextMonitor> HasNext_i_Map = new BasicRefMapOfMonitor<HasNextMonitor>(0);
	static CachedWeakReference HasNext_i_Map_cachekey_0 = null;
	static HasNextMonitor HasNext_i_Map_cachenode = null;

	// Trees for References
	static BasicRefMapOfMonitor<HasNextMonitor> HasNext_Iterator_RefMap = HasNext_i_Map;

	public static void hasnextEvent(Iterator i) {
		HasNext_activated = true;
		while (!HasNext_RVMLock.tryLock()) {
			Thread.yield();
		}
		HasNextMonitor mainMonitor = null;
		CachedWeakReference TempRef_i;

		failProp1 = false;
		// Cache Retrieval
		if (HasNext_i_Map_cachekey_0 != null && i == HasNext_i_Map_cachekey_0.get()) {
			TempRef_i = HasNext_i_Map_cachekey_0;

			mainMonitor = HasNext_i_Map_cachenode;
		} else {
			TempRef_i = HasNext_i_Map.findOrCreateWeakRef(i);
		}

		if (mainMonitor == null) {
			BasicRefMapOfMonitor<HasNextMonitor> mainMap = HasNext_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);

			if (mainMonitor == null) {
				mainMonitor = new HasNextMonitor();

				mainMonitor.RVMRef_i = TempRef_i;

				HasNext_i_Map.putLeaf(TempRef_i, mainMonitor);
			}

			HasNext_i_Map_cachekey_0 = TempRef_i;
			HasNext_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_hasnext(i);
		failProp1 |= mainMonitor.Prop_1_Category_fail;
		if(mainMonitor.Prop_1_Category_fail) {
			mainMonitor.Prop_1_handler_fail(i);
		}
		HasNext_RVMLock.unlock();
	}

	public static void nextEvent(Iterator i) {
		HasNext_activated = true;
		while (!HasNext_RVMLock.tryLock()) {
			Thread.yield();
		}
		HasNextMonitor mainMonitor = null;
		CachedWeakReference TempRef_i;

		failProp1 = false;
		// Cache Retrieval
		if (HasNext_i_Map_cachekey_0 != null && i == HasNext_i_Map_cachekey_0.get()) {
			TempRef_i = HasNext_i_Map_cachekey_0;

			mainMonitor = HasNext_i_Map_cachenode;
		} else {
			TempRef_i = HasNext_i_Map.findOrCreateWeakRef(i);
		}

		if (mainMonitor == null) {
			BasicRefMapOfMonitor<HasNextMonitor> mainMap = HasNext_i_Map;
			mainMonitor = mainMap.getLeaf(TempRef_i);

			if (mainMonitor == null) {
				mainMonitor = new HasNextMonitor();

				mainMonitor.RVMRef_i = TempRef_i;

				HasNext_i_Map.putLeaf(TempRef_i, mainMonitor);
			}

			HasNext_i_Map_cachekey_0 = TempRef_i;
			HasNext_i_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_next(i);
		failProp1 |= mainMonitor.Prop_1_Category_fail;
		if(mainMonitor.Prop_1_Category_fail) {
			mainMonitor.Prop_1_handler_fail(i);
		}
		HasNext_RVMLock.unlock();
	}

}
