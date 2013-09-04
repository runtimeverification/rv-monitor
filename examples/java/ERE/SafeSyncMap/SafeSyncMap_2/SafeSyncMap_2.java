
package SafeSyncMap_2;

import java.util.*;

public class SafeSyncMap_2 {
	public static void main(String[] args){
	  Map<String,String> testMap = new HashMap<String,String>();
      testMap = Collections.synchronizedMap(testMap);
		mop.SafeSyncMapRuntimeMonitor.syncEvent(testMap);
      testMap.put("Foo", "Bar");
      testMap.put("Bar", "Foo");
	  Set<String> keys = testMap.keySet();
		mop.SafeSyncMapRuntimeMonitor.createSetEvent(testMap, keys);
		Iterator i = keys.iterator();
		mop.SafeSyncMapRuntimeMonitor.asyncCreateIterEvent(keys, i);
		mop.SafeSyncMapRuntimeMonitor.syncCreateIterEvent(keys, i);
		mop.SafeSyncMapRuntimeMonitor.accessIterEvent(i);
		while(i.hasNext()){
			mop.SafeSyncMapRuntimeMonitor.accessIterEvent(i);
			System.out.println(testMap.get(i.next()));
			mop.SafeSyncMapRuntimeMonitor.accessIterEvent(i);
		}
	}
}
