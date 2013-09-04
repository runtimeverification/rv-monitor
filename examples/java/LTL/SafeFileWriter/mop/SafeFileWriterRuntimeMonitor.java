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

class SafeFileWriterMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeFileWriterMonitor> {
	boolean violationProp1;

	SafeFileWriterMonitor_Set(){
		this.size = 0;
		this.elements = new SafeFileWriterMonitor[4];
	}

	final void event_open(FileWriter f) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeFileWriterMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_open(f);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(f);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_write(FileWriter f) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeFileWriterMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_write(f);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(f);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_close(FileWriter f) {
		this.violationProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeFileWriterMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_close(f);
				violationProp1 |= monitor.Prop_1_Category_violation;
				if(monitor.Prop_1_Category_violation) {
					monitor.Prop_1_handler_violation(f);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
}

class SafeFileWriterMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	protected Object clone() {
		try {
			SafeFileWriterMonitor ret = (SafeFileWriterMonitor) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	static int counter = 0;
	int writes = 0;

	int Prop_1_state;
	static final int Prop_1_transition_open[] = {1, 1, 3, 3};;
	static final int Prop_1_transition_write[] = {2, 1, 3, 3};;
	static final int Prop_1_transition_close[] = {0, 0, 3, 3};;

	boolean Prop_1_Category_violation = false;

	SafeFileWriterMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_open(FileWriter f) {
		{
			this.writes = 0;
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_open[Prop_1_state];
		Prop_1_Category_violation = Prop_1_state == 2;
		return true;
	}

	final boolean Prop_1_event_write(FileWriter f) {
		{
			this.writes++;
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_write[Prop_1_state];
		Prop_1_Category_violation = Prop_1_state == 2;
		return true;
	}

	final boolean Prop_1_event_close(FileWriter f) {
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_close[Prop_1_state];
		Prop_1_Category_violation = Prop_1_state == 2;
		return true;
	}

	final void Prop_1_handler_violation (FileWriter f){
		{
			System.out.println("write after close");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_violation = false;
	}

	CachedWeakReference RVMRef_f;

	//alive_parameters_0 = [FileWriter f]
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
			//open
			//alive_f
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//write
			//alive_f
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//close
			//alive_f
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

}

public class SafeFileWriterRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	public static boolean violationProp1 = false;
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeFileWriterMapManager;
	static {
		com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
		SafeFileWriterMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeFileWriterMapManager.start();
	}

	// Declarations for the Lock
	static ReentrantLock SafeFileWriter_RVMLock = new ReentrantLock();
	static Condition SafeFileWriter_RVMLock_cond = SafeFileWriter_RVMLock.newCondition();

	private static boolean SafeFileWriter_activated = false;

	// Declarations for Indexing Trees
	static BasicRefMapOfMonitor<SafeFileWriterMonitor> SafeFileWriter_f_Map = new BasicRefMapOfMonitor<SafeFileWriterMonitor>(0);
	static CachedWeakReference SafeFileWriter_f_Map_cachekey_0 = null;
	static SafeFileWriterMonitor SafeFileWriter_f_Map_cachenode = null;

	// Trees for References
	static BasicRefMapOfMonitor<SafeFileWriterMonitor> SafeFileWriter_FileWriter_RefMap = SafeFileWriter_f_Map;

	public static void openEvent(FileWriter f) {
		SafeFileWriter_activated = true;
		while (!SafeFileWriter_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeFileWriterMonitor mainMonitor = null;
		CachedWeakReference TempRef_f;

		violationProp1 = false;
		// Cache Retrieval
		if (SafeFileWriter_f_Map_cachekey_0 != null && f == SafeFileWriter_f_Map_cachekey_0.get()) {
			TempRef_f = SafeFileWriter_f_Map_cachekey_0;

			mainMonitor = SafeFileWriter_f_Map_cachenode;
		} else {
			TempRef_f = SafeFileWriter_f_Map.findOrCreateWeakRef(f);
		}

		if (mainMonitor == null) {
			BasicRefMapOfMonitor<SafeFileWriterMonitor> mainMap = SafeFileWriter_f_Map;
			mainMonitor = mainMap.getLeaf(TempRef_f);

			if (mainMonitor == null) {
				mainMonitor = new SafeFileWriterMonitor();

				mainMonitor.RVMRef_f = TempRef_f;

				SafeFileWriter_f_Map.putLeaf(TempRef_f, mainMonitor);
			}

			SafeFileWriter_f_Map_cachekey_0 = TempRef_f;
			SafeFileWriter_f_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_open(f);
		violationProp1 |= mainMonitor.Prop_1_Category_violation;
		if(mainMonitor.Prop_1_Category_violation) {
			mainMonitor.Prop_1_handler_violation(f);
		}
		SafeFileWriter_RVMLock.unlock();
	}

	public static void writeEvent(FileWriter f) {
		SafeFileWriter_activated = true;
		while (!SafeFileWriter_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeFileWriterMonitor mainMonitor = null;
		CachedWeakReference TempRef_f;

		violationProp1 = false;
		// Cache Retrieval
		if (SafeFileWriter_f_Map_cachekey_0 != null && f == SafeFileWriter_f_Map_cachekey_0.get()) {
			TempRef_f = SafeFileWriter_f_Map_cachekey_0;

			mainMonitor = SafeFileWriter_f_Map_cachenode;
		} else {
			TempRef_f = SafeFileWriter_f_Map.findOrCreateWeakRef(f);
		}

		if (mainMonitor == null) {
			BasicRefMapOfMonitor<SafeFileWriterMonitor> mainMap = SafeFileWriter_f_Map;
			mainMonitor = mainMap.getLeaf(TempRef_f);

			if (mainMonitor == null) {
				mainMonitor = new SafeFileWriterMonitor();

				mainMonitor.RVMRef_f = TempRef_f;

				SafeFileWriter_f_Map.putLeaf(TempRef_f, mainMonitor);
			}

			SafeFileWriter_f_Map_cachekey_0 = TempRef_f;
			SafeFileWriter_f_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_write(f);
		violationProp1 |= mainMonitor.Prop_1_Category_violation;
		if(mainMonitor.Prop_1_Category_violation) {
			mainMonitor.Prop_1_handler_violation(f);
		}
		SafeFileWriter_RVMLock.unlock();
	}

	public static void closeEvent(FileWriter f) {
		SafeFileWriter_activated = true;
		while (!SafeFileWriter_RVMLock.tryLock()) {
			Thread.yield();
		}
		SafeFileWriterMonitor mainMonitor = null;
		CachedWeakReference TempRef_f;

		violationProp1 = false;
		// Cache Retrieval
		if (SafeFileWriter_f_Map_cachekey_0 != null && f == SafeFileWriter_f_Map_cachekey_0.get()) {
			TempRef_f = SafeFileWriter_f_Map_cachekey_0;

			mainMonitor = SafeFileWriter_f_Map_cachenode;
		} else {
			TempRef_f = SafeFileWriter_f_Map.findOrCreateWeakRef(f);
		}

		if (mainMonitor == null) {
			BasicRefMapOfMonitor<SafeFileWriterMonitor> mainMap = SafeFileWriter_f_Map;
			mainMonitor = mainMap.getLeaf(TempRef_f);

			if (mainMonitor == null) {
				mainMonitor = new SafeFileWriterMonitor();

				mainMonitor.RVMRef_f = TempRef_f;

				SafeFileWriter_f_Map.putLeaf(TempRef_f, mainMonitor);
			}

			SafeFileWriter_f_Map_cachekey_0 = TempRef_f;
			SafeFileWriter_f_Map_cachenode = mainMonitor;
		}

		mainMonitor.Prop_1_event_close(f);
		violationProp1 |= mainMonitor.Prop_1_Category_violation;
		if(mainMonitor.Prop_1_Category_violation) {
			mainMonitor.Prop_1_handler_violation(f);
		}
		SafeFileWriter_RVMLock.unlock();
	}

}
