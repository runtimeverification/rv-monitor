
import java.util.*;

public class HasNext_3 {
       public static void main(String[] args) {
               Vector<Integer> v = new Vector<Integer>();
               v.add(1); v.add(2);
               Iterator it = v.iterator();
		   boolean  b;
               while(b = it.hasNext()) {
				   mop.HasNextRuntimeMonitor.hasnexttrueEvent(it, b);
				   mop.HasNextRuntimeMonitor.hasnextfalseEvent(it, b);
				   mop.HasNextRuntimeMonitor.nextEvent(it);
				   final Integer next1 = (Integer) it.next();
				   mop.HasNextRuntimeMonitor.nextEvent(it);
				   int sum = next1 + (Integer)it.next();
                       System.out.println("sum = " + sum);
               }
       }
}

