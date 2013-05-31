
import java.util.*;

public class HasNext_2 {
	public static void main(String[] args){
		Vector<Integer> v = new Vector<Integer>();

		v.add(1);
		v.add(2);
		v.add(4);
		v.add(8);

		Iterator i = v.iterator();
		int sum = 0;

		while(i.hasNext()){
			mop.HasNextRuntimeMonitor.hasnextEvent(i);
			mop.HasNextRuntimeMonitor.nextEvent(i);
			sum += (Integer)i.next();
		}
		mop.HasNextRuntimeMonitor.hasnextEvent(i);

		System.out.println("sum: " + sum);
	}
}



