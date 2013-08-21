package mop;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.lang.ref.*;
import com.runtimeverification.rvmonitor.java.rt.*;
import com.runtimeverification.rvmonitor.java.rt.ref.*;
import com.runtimeverification.rvmonitor.java.rt.table.*;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IBucketNode;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple2;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple3;

class HasNextSuffixMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<HasNextSuffixMonitor> {
boolean matchProp1;

HasNextSuffixMonitor_Set(){
this.size = 0;
this.elements = new HasNextSuffixMonitor[4];
}

final void event_hasnext(Iterator i) {
this.matchProp1 = false;
int numAlive = 0 ;
for(int i_1 = 0; i_1 < this.size; i_1++){
HasNextSuffixMonitor monitor = this.elements[i_1];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

monitor.event_hasnext(i);
matchProp1 |= monitor.matchProp1;
}
}
for(int i_1 = numAlive; i_1 < this.size; i_1++){
this.elements[i_1] = null;
}
size = numAlive;
}

final void event_next(Iterator i) {
this.matchProp1 = false;
int numAlive = 0 ;
for(int i_1 = 0; i_1 < this.size; i_1++){
HasNextSuffixMonitor monitor = this.elements[i_1];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

monitor.event_next(i);
matchProp1 |= monitor.matchProp1;
}
}
for(int i_1 = numAlive; i_1 < this.size; i_1++){
this.elements[i_1] = null;
}
size = numAlive;
}
}

class HasNextSuffixMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
boolean matchProp1;
Vector<HasNextMonitor> monitorList = new Vector<HasNextMonitor>();
protected Object clone() {
try {
HasNextSuffixMonitor ret = (HasNextSuffixMonitor) super.clone();
ret.monitorList = new Vector<HasNextMonitor>();
for(HasNextMonitor monitor : this.monitorList){
HasNextMonitor newMonitor = (HasNextMonitor)monitor.clone();
ret.monitorList.add(newMonitor);
}
return ret;
}
catch (CloneNotSupportedException e) {
throw new InternalError(e.toString());
}
}

final void event_hasnext(Iterator i) {
matchProp1 = false;
RVM_lastevent = 0;
HashSet monitorSet = new HashSet();
HasNextMonitor newMonitor = new HasNextMonitor();
monitorList.add(newMonitor);
Iterator it = monitorList.iterator();
while (it.hasNext()){
HasNextMonitor monitor = (HasNextMonitor)it.next();
monitor.Prop_1_event_hasnext(i);
matchProp1 |= monitor.Prop_1_Category_match;
if(monitor.Prop_1_Category_match) {
monitor.Prop_1_handler_match(i);
}
if(monitorSet.contains(monitor) || monitor.Prop_1_Category_match ) {
it.remove();
} else {
monitorSet.add(monitor);
}
}
}

final void event_next(Iterator i) {
matchProp1 = false;
RVM_lastevent = 1;
HashSet monitorSet = new HashSet();
HasNextMonitor newMonitor = new HasNextMonitor();
monitorList.add(newMonitor);
Iterator it = monitorList.iterator();
while (it.hasNext()){
HasNextMonitor monitor = (HasNextMonitor)it.next();
monitor.Prop_1_event_next(i);
matchProp1 |= monitor.Prop_1_Category_match;
if(monitor.Prop_1_Category_match) {
monitor.Prop_1_handler_match(i);
}
if(monitorSet.contains(monitor) || monitor.Prop_1_Category_match ) {
it.remove();
} else {
monitorSet.add(monitor);
}
}
}

CachedWeakReference RVMRef_i;


@Override
protected final void terminateInternal(int idnum) {
switch(idnum){
case 0:
break;
}
switch(RVM_lastevent) {
case -1:
return;
case 0:
//hasnext
return;
case 1:
//next
return;
}
return;
}

}

class HasNextMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
protected Object clone() {
try {
HasNextMonitor ret = (HasNextMonitor) super.clone();
ret.Prop_1_stacks = new ArrayList<IntStack>();
for(int Prop_1_i = 0; Prop_1_i < this.Prop_1_stacks.size(); Prop_1_i++){
IntStack Prop_1_stack = this.Prop_1_stacks.get(Prop_1_i);
ret.Prop_1_stacks.add(Prop_1_stack.fclone());
}
return ret;
}
catch (CloneNotSupportedException e) {
throw new InternalError(e.toString());
}
}

/* %%_%_CFG_%_%% */ArrayList<IntStack> Prop_1_stacks = new ArrayList<IntStack>();
static int[][] Prop_1_gt = { { 0, -1,  }, { 0, 3,  }, { 0, -1,  }, { 0, -1,  },  };
;
static int[][][][] Prop_1_at = { { {  }, { { 2,  },  },  }, { {  }, { { 0,  },  },  }, { {  }, {  },  }, { {  }, {  },  },  };
;
static boolean[] Prop_1_acc = { false, false, true, true, };
int Prop_1_cat; // ACCEPT = 0, UNKNOWN = 1, FAIL = 2
int Prop_1_event = -1;
class IntStack implements java.io.Serializable {
int[] data;
int curr_index = 0;
public IntStack(){
data = new int[32];
}
public String toString(){
String ret = "[";
for (int i = curr_index; i>=0; i--){
ret += Integer.toString(data[i])+",";
}
return ret+"]";
}
public int hashCode() {
return curr_index^peek();
}
public boolean equals(Object o) {
if (o == null) return false;
if (!(o instanceof IntStack)) return false;
IntStack s = (IntStack)o;
if(curr_index != s.curr_index) return false;
for(int i = 0; i < curr_index; i++){
if(data[i] != s.data[i]) return false;
}
return true;
}
public IntStack(int size){
data = new int[size];
}
public int peek(){
return data[curr_index - 1];
}
public int pop(){
return data[--curr_index];
}
public void pop(int num){
curr_index -= num;
}
public void push(int datum){
if(curr_index < data.length){
data[curr_index++] = datum;
} else{
int len = data.length;
int[] old = data;
data = new int[len * 2];
for(int i = 0; i < len; ++i){
data[i] = old[i];
}
data[curr_index++] = datum;
}
}
public IntStack fclone(){
IntStack ret = new IntStack(data.length);
ret.curr_index = curr_index;
for(int i = 0; i < curr_index; ++i){
ret.data[i] = data[i];
}
return ret;
}
public void clear(){
curr_index = 0;
}
}

boolean Prop_1_Category_match = false;

HasNextMonitor () {
IntStack stack = new IntStack();
stack.push(-2);
stack.push(1);
Prop_1_stacks.add(stack);

}

final boolean Prop_1_event_hasnext(Iterator i) {

Prop_1_event = 1;
if (Prop_1_cat != 2) {
Prop_1_event--;
Prop_1_cat = 1;
for (int Prop_1_i = Prop_1_stacks.size()-1; Prop_1_i >=0; Prop_1_i--) {
IntStack stack = Prop_1_stacks.get(Prop_1_i);
Prop_1_stacks.set(Prop_1_i,null);
while (stack != null) {
int s = stack.peek();
if (s >= 0 && Prop_1_at[s][Prop_1_event].length >= 0) {
/* not in an error state and something to do? */
for (int j = 0; j < Prop_1_at[s][Prop_1_event].length; j++) {
IntStack tstack;
if (Prop_1_at[s][Prop_1_event].length > 1){
tstack = stack.fclone();
} else{
tstack = stack;
}
switch (Prop_1_at[s][Prop_1_event][j].length) {
case 1:/* Shift */
tstack.push(Prop_1_at[s][Prop_1_event][j][0]);
Prop_1_stacks.add(tstack);
if (Prop_1_acc[Prop_1_at[s][Prop_1_event][j][0]]) Prop_1_cat = 0;
break;
case 2: /* Reduce */
tstack.pop(Prop_1_at[s][Prop_1_event][j][1]);
int Prop_1_old = tstack.peek();
tstack.push(Prop_1_gt[Prop_1_old][Prop_1_at[s][Prop_1_event][j][0]]);
Prop_1_stacks.add(Prop_1_i,tstack);
break;
}
}
}
stack = Prop_1_stacks.get(Prop_1_i);
Prop_1_stacks.remove(Prop_1_i);
}
}
if (Prop_1_stacks.isEmpty())
Prop_1_cat = 2;
}
Prop_1_Category_match = Prop_1_cat == 0;
return true;
}

final boolean Prop_1_event_next(Iterator i) {

Prop_1_event = 2;
if (Prop_1_cat != 2) {
Prop_1_event--;
Prop_1_cat = 1;
for (int Prop_1_i = Prop_1_stacks.size()-1; Prop_1_i >=0; Prop_1_i--) {
IntStack stack = Prop_1_stacks.get(Prop_1_i);
Prop_1_stacks.set(Prop_1_i,null);
while (stack != null) {
int s = stack.peek();
if (s >= 0 && Prop_1_at[s][Prop_1_event].length >= 0) {
/* not in an error state and something to do? */
for (int j = 0; j < Prop_1_at[s][Prop_1_event].length; j++) {
IntStack tstack;
if (Prop_1_at[s][Prop_1_event].length > 1){
tstack = stack.fclone();
} else{
tstack = stack;
}
switch (Prop_1_at[s][Prop_1_event][j].length) {
case 1:/* Shift */
tstack.push(Prop_1_at[s][Prop_1_event][j][0]);
Prop_1_stacks.add(tstack);
if (Prop_1_acc[Prop_1_at[s][Prop_1_event][j][0]]) Prop_1_cat = 0;
break;
case 2: /* Reduce */
tstack.pop(Prop_1_at[s][Prop_1_event][j][1]);
int Prop_1_old = tstack.peek();
tstack.push(Prop_1_gt[Prop_1_old][Prop_1_at[s][Prop_1_event][j][0]]);
Prop_1_stacks.add(Prop_1_i,tstack);
break;
}
}
}
stack = Prop_1_stacks.get(Prop_1_i);
Prop_1_stacks.remove(Prop_1_i);
}
}
if (Prop_1_stacks.isEmpty())
Prop_1_cat = 2;
}
Prop_1_Category_match = Prop_1_cat == 0;
return true;
}

final void Prop_1_handler_match (Iterator i){
{
    System.err.println("! hasNext not called before next");
    this.reset();
}

}

final void reset() {
Prop_1_stacks.clear();
IntStack stack = new IntStack();
stack.push(-2);
stack.push(1);
Prop_1_stacks.add(stack);
Prop_1_Category_match = false;
}

public final int hashCode() {
if(Prop_1_stacks.size() == 0) return 0;
return Prop_1_stacks.size() ^ Prop_1_stacks.get(Prop_1_stacks.size() - 1).hashCode();
}

public final boolean equals(Object o) {
if(o == null) return false;
if(! (o instanceof HasNextMonitor)) return false ;
HasNextMonitor m = (HasNextMonitor) o;
if (Prop_1_stacks.size() != m.Prop_1_stacks.size()) return false;
for(int Prop_1_i = 0; Prop_1_i < Prop_1_stacks.size(); Prop_1_i++){
IntStack Prop_1_stack = Prop_1_stacks.get(Prop_1_i);
IntStack Prop_1_stack2 = m.Prop_1_stacks.get(Prop_1_i);
if(Prop_1_stack.curr_index != Prop_1_stack2.curr_index) return false;
for(int Prop_1_j = 0; Prop_1_j < Prop_1_stack.curr_index; Prop_1_j++){
if(Prop_1_stack.data[Prop_1_j] != Prop_1_stack2.data[Prop_1_j]) return false;
}
}
return true;
}

}

public class HasNextRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
public static boolean matchProp1 = false;
private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager HasNextMapManager;
static {
com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
HasNextMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
HasNextMapManager.start();
}

// Declarations for the Lock 
static ReentrantLock HasNext_RVMLock = new ReentrantLock();
static Condition HasNext_RVMLock_cond = HasNext_RVMLock.newCondition();

private static boolean HasNext_activated = false;

// Declarations for Indexing Trees 
static BasicRefMapOfMonitor<HasNextSuffixMonitor> HasNext_i_Map = new BasicRefMapOfMonitor<HasNextSuffixMonitor>(0);
static CachedWeakReference HasNext_i_Map_cachekey_0 = null;
static HasNextSuffixMonitor HasNext_i_Map_cachenode = null;

public static void hasnextEvent(Iterator i) {
HasNext_activated = true;
while (!HasNext_RVMLock.tryLock()) {
Thread.yield();
}
HasNextSuffixMonitor mainMonitor = null;
CachedWeakReference TempRef_i;

matchProp1 = false;
// Cache Retrieval
if (HasNext_i_Map_cachekey_0 != null && i == HasNext_i_Map_cachekey_0.get()) {
TempRef_i = HasNext_i_Map_cachekey_0;

mainMonitor = HasNext_i_Map_cachenode;
} else {
TempRef_i = null;
}

if (mainMonitor == null) {
BasicRefMapOfMonitor<HasNextSuffixMonitor> mainMap = HasNext_i_Map;
{
IBucketNode<CachedWeakReference, HasNextSuffixMonitor> node = mainMap.getNodeWithStrongRef(i);
if (node == null) {
mainMonitor = null;
if (TempRef_i == null) {
TempRef_i = new CachedWeakReference(i);
}
}
else {
mainMonitor = node.getValue();
TempRef_i = node.getKey();
}
}

if (mainMonitor == null) {
mainMonitor = new HasNextSuffixMonitor();

mainMonitor.RVMRef_i = TempRef_i;

HasNext_i_Map.putLeaf(TempRef_i, mainMonitor);
}

HasNext_i_Map_cachekey_0 = TempRef_i;
HasNext_i_Map_cachenode = mainMonitor;
}

mainMonitor.event_hasnext(i);
matchProp1 |= mainMonitor.matchProp1;
HasNext_RVMLock.unlock();
}

public static void nextEvent(Iterator i) {
HasNext_activated = true;
while (!HasNext_RVMLock.tryLock()) {
Thread.yield();
}
HasNextSuffixMonitor mainMonitor = null;
CachedWeakReference TempRef_i;

matchProp1 = false;
// Cache Retrieval
if (HasNext_i_Map_cachekey_0 != null && i == HasNext_i_Map_cachekey_0.get()) {
TempRef_i = HasNext_i_Map_cachekey_0;

mainMonitor = HasNext_i_Map_cachenode;
} else {
TempRef_i = null;
}

if (mainMonitor == null) {
BasicRefMapOfMonitor<HasNextSuffixMonitor> mainMap = HasNext_i_Map;
{
IBucketNode<CachedWeakReference, HasNextSuffixMonitor> node = mainMap.getNodeWithStrongRef(i);
if (node == null) {
mainMonitor = null;
if (TempRef_i == null) {
TempRef_i = new CachedWeakReference(i);
}
}
else {
mainMonitor = node.getValue();
TempRef_i = node.getKey();
}
}

if (mainMonitor == null) {
mainMonitor = new HasNextSuffixMonitor();

mainMonitor.RVMRef_i = TempRef_i;

HasNext_i_Map.putLeaf(TempRef_i, mainMonitor);
}

HasNext_i_Map_cachekey_0 = TempRef_i;
HasNext_i_Map_cachenode = mainMonitor;
}

mainMonitor.event_next(i);
matchProp1 |= mainMonitor.matchProp1;
HasNext_RVMLock.unlock();
}

}
