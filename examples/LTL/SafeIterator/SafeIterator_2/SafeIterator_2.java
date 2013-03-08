import java.util.*;

public class SafeIterator_2 {
  public static void main(String[] args){
    Set<Integer> testSet = new HashSet<Integer>(); 
    for(int i = 0; i < 10; ++i){
      testSet.add(new Integer(i));
		mop.SafeIteratorRuntimeMonitor.updatesourceEvent(testSet);
    }
    Iterator i = testSet.iterator();
	  mop.SafeIteratorRuntimeMonitor.createEvent(testSet, i);
  
    int output = 0;	
    for(int j = 0; j < 10 && i.hasNext(); ++j){
		mop.SafeIteratorRuntimeMonitor.nextEvent(i);
	  output += (Integer)i.next();
    }
	System.out.println(output);
  }
}
