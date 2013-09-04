
package HasNext2_2;
import java.util.*;

public class HasNext2_2 {
	public static void main(String[] args) {
		Vector<Integer> v = new Vector<Integer>();
		v.add(1);
		v.add(2);

		Iterator i = v.iterator();
		int sum = 0;

		// JavaMOP should not match "next next"
		if (i.hasNext()) {
			mop.HasNext2RuntimeMonitor.hasnextEvent(i);
			mop.HasNext2RuntimeMonitor.nextEvent(i);
			sum += (Integer)i.next();
		} else mop.HasNext2RuntimeMonitor.hasnextEvent(i);


		System.out.println("sum: " + sum);
	}
}

