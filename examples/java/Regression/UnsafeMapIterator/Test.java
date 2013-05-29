import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

public class Test {
  public static void main(String[] args){
    Map<Integer, Integer> m = new HashMap<Integer, Integer>();
    UnsafeMapIteratorRuntimeMonitor.updateMapEvent(m);
    Collection c = m.keySet();
    UnsafeMapIteratorRuntimeMonitor.createCollEvent(m, c);
    Iterator i = c.iterator();
    UnsafeMapIteratorRuntimeMonitor.createIterEvent(c, i);
    UnsafeMapIteratorRuntimeMonitor.useIterEvent(i);
    UnsafeMapIteratorRuntimeMonitor.updateMapEvent(m);
    UnsafeMapIteratorRuntimeMonitor.useIterEvent(i);
  }
}
