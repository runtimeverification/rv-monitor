package SafeMapIterator_1;

import java.util.*;

 public class SafeMapIterator_1 {
   public static void main(String[] args){
    try{
        Map<String, String> testMap = new HashMap<String,String>();
        testMap.put("Foo", "Bar");
		mop.SafeMapIteratorRuntimeMonitor.updateMapEvent(testMap);
        testMap.put("Bar", "Foo");
		mop.SafeMapIteratorRuntimeMonitor.updateMapEvent(testMap);
        Set<String> keys = testMap.keySet();
		mop.SafeMapIteratorRuntimeMonitor.createCollEvent(testMap, keys);
        Iterator i = keys.iterator();
		mop.SafeMapIteratorRuntimeMonitor.createIterEvent(keys, i);
        testMap.put("breaker", "borked");
		mop.SafeMapIteratorRuntimeMonitor.updateMapEvent(testMap);
		mop.SafeMapIteratorRuntimeMonitor.useIterEvent(i);
        System.out.println(i.next());
     }
     catch(Exception e){
        System.out.println("java found the problem too");
     }
   }
 }

