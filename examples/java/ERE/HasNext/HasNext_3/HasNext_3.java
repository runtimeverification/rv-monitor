package HasNext_3;

import java.util.Iterator;
import java.util.Vector;

public class HasNext_3 {
       public static void main(String[] args) {
               Vector<Integer> v = new Vector<Integer>();
               v.add(1); v.add(2);
               Iterator it = v.iterator();
               while(it.hasNext()) {
				   mop.HasNextRuntimeMonitor.hasnextEvent(it);
				   mop.HasNextRuntimeMonitor.nextEvent(it);
				   final Integer next = (Integer) it.next();
				   mop.HasNextRuntimeMonitor.nextEvent(it);
				   int sum = next + (Integer)it.next();
                       System.out.println("sum = " + sum);
               }
       }
}

