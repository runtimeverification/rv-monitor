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
	boolean matchProp1;
	boolean failProp1;

	SafeFileWriterMonitor_Set(){
		this.size = 0;
		this.elements = new SafeFileWriterMonitor[4];
	}

	final void event_open(FileWriter f) {
		this.matchProp1 = false;
		this.failProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeFileWriterMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_open(f);
				failProp1 |= monitor.Prop_1_Category_fail;
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(f);
				}
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(f);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_write(FileWriter f) {
		this.matchProp1 = false;
		this.failProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeFileWriterMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_write(f);
				failProp1 |= monitor.Prop_1_Category_fail;
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(f);
				}
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(f);
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}

	final void event_close(FileWriter f) {
		this.matchProp1 = false;
		this.failProp1 = false;
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeFileWriterMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				monitor.Prop_1_event_close(f);
				failProp1 |= monitor.Prop_1_Category_fail;
				if(monitor.Prop_1_Category_fail) {
					monitor.Prop_1_handler_fail(f);
				}
				matchProp1 |= monitor.Prop_1_Category_match;
				if(monitor.Prop_1_Category_match) {
					monitor.Prop_1_handler_match(f);
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
	static final int Prop_1_transition_open[] = {1, 3, 3, 3};;
	static final int Prop_1_transition_write[] = {3, 2, 2, 3};;
	static final int Prop_1_transition_close[] = {3, 3, 0, 3};;

	boolean Prop_1_Category_fail = false;
	boolean Prop_1_Category_match = false;

	SafeFileWriterMonitor () {
		Prop_1_state = 0;

	}

	final boolean Prop_1_event_open(FileWriter f) {
		{
			this.writes = 0;
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_open[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
		Prop_1_Category_match = Prop_1_state == 0;
		return true;
	}

	final boolean Prop_1_event_write(FileWriter f) {
		{
			this.writes++;
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_write[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
		Prop_1_Category_match = Prop_1_state == 0;
		return true;
	}

	final boolean Prop_1_event_close(FileWriter f) {
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_close[Prop_1_state];
		Prop_1_Category_fail = Prop_1_state == 3;
		Prop_1_Category_match = Prop_1_state == 0;
		return true;
	}

	final void Prop_1_handler_fail (FileWriter f){
		{
			System.out.println("write after close");
			this.reset();
		}

	}

	final void Prop_1_handler_match (FileWriter f){
		{
			System.out.println((++(counter)) + ":" + writes);
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_fail = false;
		Prop_1_Category_match = false;
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
	public static boolean matchProp1 = false;
	public static boolean failProp1 = false;
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

		matchProp1 = false;
		failProp1 = false;
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
		failProp1 |= mainMonitor.Prop_1_Category_fail;
		if(mainMonitor.Prop_1_Category_fail) {
			mainMonitor.Prop_1_handler_fail(f);
		}
		matchProp1 |= mainMonitor.Prop_1_Category_match;
		if(mainMonitor.Prop_1_Category_match) {
			mainMonitor.Prop_1_handler_match(f);
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

		matchProp1 = false;
		failProp1 = false;
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
		failProp1 |= mainMonitor.Prop_1_Category_fail;
		if(mainMonitor.Prop_1_Category_fail) {
			mainMonitor.Prop_1_handler_fail(f);
		}
		matchProp1 |= mainMonitor.Prop_1_Category_match;
		if(mainMonitor.Prop_1_Category_match) {
			mainMonitor.Prop_1_handler_match(f);
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

		matchProp1 = false;
		failProp1 = false;
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
		failProp1 |= mainMonitor.Prop_1_Category_fail;
		if(mainMonitor.Prop_1_Category_fail) {
			mainMonitor.Prop_1_handler_fail(f);
		}
		matchProp1 |= mainMonitor.Prop_1_Category_match;
		if(mainMonitor.Prop_1_Category_match) {
			mainMonitor.Prop_1_handler_match(f);
		}
		SafeFileWriter_RVMLock.unlock();
	}

}
