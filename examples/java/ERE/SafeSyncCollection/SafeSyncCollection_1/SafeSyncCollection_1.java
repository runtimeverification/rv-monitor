import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class SafeSyncCollection_1 {
	public static void main(String[] args){
		ArrayList<String> list = new ArrayList<String>();
		Collection c = list;
		c = Collections.synchronizedCollection(c);
		mop.SafeSyncCollectionRuntimeMonitor.syncEvent(c);

		list.add("Foo");
		list.add("Bar");
		Iterator i = c.iterator();
		mop.SafeSyncCollectionRuntimeMonitor.asyncCreateIterEvent(c,i);
		mop.SafeSyncCollectionRuntimeMonitor.syncCreateIterEvent(c,i);
		mop.SafeSyncCollectionRuntimeMonitor.accessIterEvent(i);
		while(i.hasNext()){
			mop.SafeSyncCollectionRuntimeMonitor.accessIterEvent(i);
			System.out.println(i.next());
			mop.SafeSyncCollectionRuntimeMonitor.accessIterEvent(i);
		}
	}
}
