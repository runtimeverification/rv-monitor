package HasNext_1;

import java.util.*;

public class HasNext_1 {
	public static void main(String[] args){
		Vector<Integer> v = new Vector<Integer>();

		v.add(1);
		v.add(2);
		v.add(4);
		v.add(8);

		Iterator i = v.iterator();
		int sum = 0;

		final boolean b = i.hasNext();
		mop.HasNextRuntimeMonitor.hasnextEvent(i);
		if(b){
			mop.HasNextRuntimeMonitor.nextEvent(i);
			sum += (Integer)i.next();
			mop.HasNextRuntimeMonitor.nextEvent(i);
			sum += (Integer)i.next();
			mop.HasNextRuntimeMonitor.nextEvent(i);
			sum += (Integer)i.next();
			mop.HasNextRuntimeMonitor.nextEvent(i);
			sum += (Integer)i.next();
		}

		System.out.println("sum: " + sum);
	}
}



