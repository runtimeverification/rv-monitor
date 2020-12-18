
package HasNext_1;

import java.util.*;

public class HasNext_1 {
	public static void main(String[] args){
        Vector<Integer> v = new Vector<Integer>();

        v.add(1);
        v.add(2);
        v.add(4);
        v.add(8);

        rvm.HasNextRuntimeMonitor.enable();

        Iterator i = v.iterator();
        int sum = 0;

        rvm.HasNextRuntimeMonitor.nextEvent(i);
        sum += (Integer)i.next();
        rvm.HasNextRuntimeMonitor.nextEvent(i);
        sum += (Integer)i.next();
        
        rvm.HasNextRuntimeMonitor.resetMonitor();

        rvm.HasNextRuntimeMonitor.enable();

        while(i.hasNext()){
            rvm.HasNextRuntimeMonitor.hasnextEvent(i);
            rvm.HasNextRuntimeMonitor.nextEvent(i);
            sum += (Integer)i.next();
        }
        rvm.HasNextRuntimeMonitor.hasnextEvent(i);

        rvm.HasNextRuntimeMonitor.resetMonitor();

        System.out.println("sum: " + sum);
    }
}
