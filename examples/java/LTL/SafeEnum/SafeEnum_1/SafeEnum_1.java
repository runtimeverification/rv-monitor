
import java.util.*;

public class SafeEnum_1 {
	public static void main(String[] args){
		Vector<Integer> v = new Vector<Integer>();

		v.add(1);
		mop.SafeEnumRuntimeMonitor.updatesourceEvent(v);
		v.add(2);
		mop.SafeEnumRuntimeMonitor.updatesourceEvent(v);
		v.add(4);
		mop.SafeEnumRuntimeMonitor.updatesourceEvent(v);
		v.add(8);
		mop.SafeEnumRuntimeMonitor.updatesourceEvent(v);

		Enumeration e = v.elements();
		mop.SafeEnumRuntimeMonitor.createEvent(v,e);

		int sum = 0;

		if(e.hasMoreElements()){
			mop.SafeEnumRuntimeMonitor.nextEvent(e);
			sum += (Integer)e.nextElement();
			v.add(11);
			mop.SafeEnumRuntimeMonitor.updatesourceEvent(v);
		}

		while(e.hasMoreElements()){
			sum += (Integer)e.nextElement();
			mop.SafeEnumRuntimeMonitor.nextEvent(e);
		}

		v.clear();
		mop.SafeEnumRuntimeMonitor.updatesourceEvent(v);

		System.out.println("sum: " + sum);
	}
}



