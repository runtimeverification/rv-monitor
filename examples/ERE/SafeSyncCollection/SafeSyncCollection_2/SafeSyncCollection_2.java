import java.util.*;

public class SafeSyncCollection_2 {
	public static void main(String[] args){
		ArrayList<String> list = new ArrayList<String>();
		list.add("Foo");
		list.add("Bar");

		Collection c = list;
		c = Collections.synchronizedCollection(list);
		mop.SafeSyncCollectionRuntimeMonitor.syncEvent(c);

		Iterator i = null;
		synchronized(c){
			i = c.iterator();
			mop.SafeSyncCollectionRuntimeMonitor.asyncCreateIterEvent(c,i);
			mop.SafeSyncCollectionRuntimeMonitor.syncCreateIterEvent(c,i);
		}

		System.out.println("lists---");
		mop.SafeSyncCollectionRuntimeMonitor.accessIterEvent(i);
		while(i.hasNext()){
			mop.SafeSyncCollectionRuntimeMonitor.accessIterEvent(i);
			System.out.println(i.next());
			mop.SafeSyncCollectionRuntimeMonitor.accessIterEvent(i);
		}
	}
}
