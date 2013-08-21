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

class SafeFileMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeFileMonitor> {
boolean failProp1;

SafeFileMonitor_Set(){
this.size = 0;
this.elements = new SafeFileMonitor[4];
}

final void event_open(FileReader f, Thread t) {
this.failProp1 = false;
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

monitor.Prop_1_event_open(f, t);
failProp1 |= monitor.Prop_1_Category_fail;
if(monitor.Prop_1_Category_fail) {
monitor.Prop_1_handler_fail(f, t);
}
}
}
for(int i = numAlive; i < this.size; i++){
this.elements[i] = null;
}
size = numAlive;
}

final void event_close(FileReader f, Thread t) {
this.failProp1 = false;
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

monitor.Prop_1_event_close(f, t);
failProp1 |= monitor.Prop_1_Category_fail;
if(monitor.Prop_1_Category_fail) {
monitor.Prop_1_handler_fail(f, t);
}
}
}
for(int i = numAlive; i < this.size; i++){
this.elements[i] = null;
}
size = numAlive;
}

final void event_beginCall(Thread t) {
this.failProp1 = false;
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

monitor.Prop_1_event_beginCall(t);
failProp1 |= monitor.Prop_1_Category_fail;
if(monitor.Prop_1_Category_fail) {
monitor.Prop_1_handler_fail(null, t);
}
}
}
for(int i = numAlive; i < this.size; i++){
this.elements[i] = null;
}
size = numAlive;
}

final void event_endCall(Thread t) {
this.failProp1 = false;
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

monitor.Prop_1_event_endCall(t);
failProp1 |= monitor.Prop_1_Category_fail;
if(monitor.Prop_1_Category_fail) {
monitor.Prop_1_handler_fail(null, t);
}
}
}
for(int i = numAlive; i < this.size; i++){
this.elements[i] = null;
}
size = numAlive;
}
}

class SafeFileMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
long tau = -1;
protected Object clone() {
try {
SafeFileMonitor ret = (SafeFileMonitor) super.clone();
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
static int[][] Prop_1_gt = { { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 6, -1,  }, { 0, -1, -1,  }, { 0, 17, -1,  }, { 0, -1, -1,  }, { 0, 1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 12, 34,  }, { 0, 41, -1,  }, { 0, -1, -1,  }, { 0, 23, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 24, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 40, -1,  }, { 0, -1, -1,  }, { 0, 18, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 32, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 19, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 12, 31,  }, { 0, 26, -1,  }, { 0, -1, -1,  }, { 0, 30, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  },  };
;
static int[][][][] Prop_1_at = { { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, {  },  }, { { { 7,  },  }, { { 10,  },  }, { { 15,  },  }, {  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { { { 1, 4,  },  }, {  }, { { 1, 4,  },  }, { { 1, 4,  },  },  }, { { { 25,  },  }, {  }, { { 49,  },  }, { { 0,  },  },  }, { { { 21,  },  }, {  }, { { 38,  },  }, { { 22,  },  },  }, { { { 13,  },  }, { { 8,  },  }, { { 42,  },  }, {  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  }, { { { 13,  },  }, { { 37,  },  }, { { 42,  },  }, {  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, {  },  }, { { { 1, 2,  },  }, { { 1, 2,  },  }, { { 1, 2,  },  }, {  },  }, { { { 47,  },  }, {  }, { { 27,  },  }, {  },  }, { { { 13,  },  }, { { 11,  },  }, { { 42,  },  }, {  },  }, { { { 1, 4,  },  }, { { 1, 4,  },  }, { { 1, 4,  },  }, {  },  }, { { { 25,  },  }, {  }, { { 49,  },  }, { { 16,  },  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  }, { { { 7,  },  }, { { 43,  },  }, { { 15,  },  }, {  },  }, { { { 21,  },  }, {  }, { { 38,  },  }, { { 29,  },  },  }, { { { 21,  },  }, {  }, { { 38,  },  }, { { 36,  },  },  }, { { { 1, 2,  }, { 1, 3,  },  }, {  }, { { 1, 2,  }, { 1, 3,  },  }, {  },  }, { { { 13,  },  }, { { 3,  },  }, { { 42,  },  }, {  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, {  },  }, { { { 21,  },  }, {  }, { { 38,  },  }, { { 14,  },  },  }, { { { 7,  },  }, { { 4,  },  }, { { 15,  },  }, {  },  }, { { { 13,  },  }, { { 39,  },  }, { { 42,  },  }, {  },  }, { { { 7,  },  }, { { 50,  },  }, { { 15,  },  }, {  },  }, { { { 25,  },  }, {  }, { { 49,  },  }, { { 28,  },  },  }, { { { 1, 2,  }, { 1, 3,  },  }, {  }, { { 1, 2,  }, { 1, 3,  },  }, {  },  }, { { { 1, 3,  }, { 1, 4,  },  }, {  }, { { 1, 3,  }, { 1, 4,  },  }, {  },  }, { { { 21,  },  }, {  }, { { 38,  },  }, { { 45,  },  },  }, { {  }, {  }, {  }, {  },  }, { { { 21,  },  }, {  }, { { 38,  },  }, { { 48,  },  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { {  }, {  }, {  }, {  },  }, { { { 1, 2,  },  }, { { 1, 2,  },  }, { { 1, 2,  },  }, {  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  }, { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, {  },  }, { { { 25,  },  }, {  }, { { 49,  },  }, { { 2,  },  },  }, { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, { { 1, 2,  },  },  }, { { { 7,  },  }, { { 33,  },  }, { { 15,  },  }, {  },  }, { { { 7,  },  }, { { 51,  },  }, { { 15,  },  }, {  },  }, { { { 25,  },  }, {  }, { { 49,  },  }, { { 35,  },  },  }, { { { 1, 4,  },  }, { { 1, 4,  },  }, { { 1, 4,  },  }, {  },  }, { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, { { 1, 2,  },  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { { { 9,  },  }, {  }, { { 5,  },  }, {  },  }, { { { 13,  },  }, { { 20,  },  }, { { 42,  },  }, {  },  }, { { { 1, 4,  },  }, {  }, { { 1, 4,  },  }, { { 1, 4,  },  },  }, { { { 25,  },  }, {  }, { { 49,  },  }, { { 44,  },  },  }, { { { 1, 3,  }, { 1, 4,  },  }, {  }, { { 1, 3,  }, { 1, 4,  },  }, {  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  },  };
;
static boolean[] Prop_1_acc = { true, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, true, true, false, true, false, false, true, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, };
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

boolean Prop_1_Category_fail = false;

SafeFileMonitor () {
IntStack stack = new IntStack();
stack.push(-2);
stack.push(46);
Prop_1_stacks.add(stack);

}

final boolean Prop_1_event_open(FileReader f, Thread t) {
RVM_lastevent = 0;

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
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final boolean Prop_1_event_close(FileReader f, Thread t) {
RVM_lastevent = 1;

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
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final boolean Prop_1_event_beginCall(Thread t) {
RVM_lastevent = 2;

Prop_1_event = 3;
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
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final boolean Prop_1_event_endCall(Thread t) {
RVM_lastevent = 3;

Prop_1_event = 4;
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
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final void Prop_1_handler_fail (FileReader f, Thread t){
{
    System.out.println("improper use of files");
}

}

final void reset() {
RVM_lastevent = -1;
Prop_1_stacks.clear();
IntStack stack = new IntStack();
stack.push(-2);
stack.push(46);
Prop_1_stacks.add(stack);
Prop_1_Category_fail = false;
}

public final int hashCode() {
if(Prop_1_stacks.size() == 0) return 0;
return Prop_1_stacks.size() ^ Prop_1_stacks.get(Prop_1_stacks.size() - 1).hashCode();
}

public final boolean equals(Object o) {
if(o == null) return false;
if(! (o instanceof SafeFileMonitor)) return false ;
SafeFileMonitor m = (SafeFileMonitor) o;
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

CachedTagWeakReference RVMRef_f;
CachedTagWeakReference RVMRef_t;


@Override
protected final void terminateInternal(int idnum) {
switch(idnum){
case 0:
break;
case 1:
break;
}
switch(RVM_lastevent) {
case -1:
return;
case 0:
//open
return;
case 1:
//close
return;
case 2:
//beginCall
return;
case 3:
//endCall
return;
}
return;
}

}

public class SafeFileRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
public static boolean failProp1 = false;
private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeFileMapManager;
static {
com.runtimeverification.rvmonitor.java.rt.RuntimeOption.enableFineGrainedLock(false);
SafeFileMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
SafeFileMapManager.start();
}

// Declarations for the Lock 
static ReentrantLock SafeFile_RVMLock = new ReentrantLock();
static Condition SafeFile_RVMLock_cond = SafeFile_RVMLock.newCondition();

// Declarations for Timestamps 
private static long SafeFile_timestamp = 1;

private static boolean SafeFile_activated = false;

// Declarations for Indexing Trees 
static MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> SafeFile_t_Map = new MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor>(1);
static CachedTagWeakReference SafeFile_t_Map_cachekey_1 = null;
static SafeFileMonitor_Set SafeFile_t_Map_cacheset = null;
static SafeFileMonitor SafeFile_t_Map_cachenode = null;
static MapOfAll<MapOfMonitor<SafeFileMonitor>, SafeFileMonitor_Set, SafeFileMonitor> SafeFile_f_t_Map = new MapOfAll<MapOfMonitor<SafeFileMonitor>, SafeFileMonitor_Set, SafeFileMonitor>(0);
static CachedTagWeakReference SafeFile_f_t_Map_cachekey_0 = null;
static CachedTagWeakReference SafeFile_f_t_Map_cachekey_1 = null;
static SafeFileMonitor SafeFile_f_t_Map_cachenode = null;

// Trees for References
static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeFile_FileReader_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();
static com.runtimeverification.rvmonitor.java.rt.table.TagRefMap SafeFile_Thread_RefMap = new com.runtimeverification.rvmonitor.java.rt.table.TagRefMap();

public static void openEvent(FileReader f, Thread t) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}
SafeFileMonitor mainMonitor = null;
SafeFileMonitor origMonitor = null;
SafeFileMonitor_Set monitors = null;
CachedTagWeakReference TempRef_f;
CachedTagWeakReference TempRef_t;

failProp1 = false;
// Cache Retrieval
if (SafeFile_f_t_Map_cachekey_0 != null && f == SafeFile_f_t_Map_cachekey_0.get() && SafeFile_f_t_Map_cachekey_1 != null && t == SafeFile_f_t_Map_cachekey_1.get()) {
TempRef_f = SafeFile_f_t_Map_cachekey_0;
TempRef_t = SafeFile_f_t_Map_cachekey_1;

mainMonitor = SafeFile_f_t_Map_cachenode;
} else {
TempRef_f = SafeFile_FileReader_RefMap.findOrCreateWeakRef(f);
TempRef_t = SafeFile_Thread_RefMap.findOrCreateWeakRef(t);
}

if (mainMonitor == null) {
MapOfAll<MapOfMonitor<SafeFileMonitor>, SafeFileMonitor_Set, SafeFileMonitor> tempMap1 = SafeFile_f_t_Map;
MapOfMonitor<SafeFileMonitor> obj1 = tempMap1.getMap(TempRef_f);
if (obj1 == null) {
obj1 = new MapOfMonitor<SafeFileMonitor>(1);
tempMap1.putMap(TempRef_f, obj1);
}
MapOfMonitor<SafeFileMonitor> mainMap = obj1;
mainMonitor = mainMap.getLeaf(TempRef_t);

if (mainMonitor == null) {
if (mainMonitor == null) {
MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> origMap1 = SafeFile_t_Map;
origMonitor = origMap1.getLeaf(TempRef_t);
if (origMonitor != null) {
boolean timeCheck = true;

if (TempRef_f.getDisabled() > origMonitor.tau) {
timeCheck = false;
}

if (timeCheck) {
mainMonitor = (SafeFileMonitor)origMonitor.clone();
mainMonitor.RVMRef_f = TempRef_f;
if (TempRef_f.getTau() == -1){
TempRef_f.setTau(origMonitor.tau);
}
mainMap.putLeaf(TempRef_t, mainMonitor);

MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> tempMap2 = SafeFile_t_Map;
SafeFileMonitor_Set obj2 = tempMap2.getSet(TempRef_t);
monitors = obj2;
if (monitors == null) {
monitors = new SafeFileMonitor_Set();
tempMap2.putSet(TempRef_t, monitors);
}
monitors.add(mainMonitor);
}
}
}
if (mainMonitor == null) {
mainMonitor = new SafeFileMonitor();

mainMonitor.RVMRef_f = TempRef_f;
mainMonitor.RVMRef_t = TempRef_t;

mainMap.putLeaf(TempRef_t, mainMonitor);
mainMonitor.tau = SafeFile_timestamp;
if (TempRef_f.getTau() == -1){
TempRef_f.setTau(SafeFile_timestamp);
}
if (TempRef_t.getTau() == -1){
TempRef_t.setTau(SafeFile_timestamp);
}
SafeFile_timestamp++;

MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> tempMap3 = SafeFile_t_Map;
SafeFileMonitor_Set obj3 = tempMap3.getSet(TempRef_t);
monitors = obj3;
if (monitors == null) {
monitors = new SafeFileMonitor_Set();
tempMap3.putSet(TempRef_t, monitors);
}
monitors.add(mainMonitor);
}

TempRef_f.setDisabled(SafeFile_timestamp);
SafeFile_timestamp++;
}

SafeFile_f_t_Map_cachekey_0 = TempRef_f;
SafeFile_f_t_Map_cachekey_1 = TempRef_t;
SafeFile_f_t_Map_cachenode = mainMonitor;
}

mainMonitor.Prop_1_event_open(f, t);
failProp1 |= mainMonitor.Prop_1_Category_fail;
if(mainMonitor.Prop_1_Category_fail) {
mainMonitor.Prop_1_handler_fail(f, t);
}
SafeFile_RVMLock.unlock();
}

public static void closeEvent(FileReader f, Thread t) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}
SafeFileMonitor mainMonitor = null;
SafeFileMonitor origMonitor = null;
SafeFileMonitor_Set monitors = null;
CachedTagWeakReference TempRef_f;
CachedTagWeakReference TempRef_t;

failProp1 = false;
// Cache Retrieval
if (SafeFile_f_t_Map_cachekey_0 != null && f == SafeFile_f_t_Map_cachekey_0.get() && SafeFile_f_t_Map_cachekey_1 != null && t == SafeFile_f_t_Map_cachekey_1.get()) {
TempRef_f = SafeFile_f_t_Map_cachekey_0;
TempRef_t = SafeFile_f_t_Map_cachekey_1;

mainMonitor = SafeFile_f_t_Map_cachenode;
} else {
TempRef_f = SafeFile_FileReader_RefMap.findOrCreateWeakRef(f);
TempRef_t = SafeFile_Thread_RefMap.findOrCreateWeakRef(t);
}

if (mainMonitor == null) {
MapOfAll<MapOfMonitor<SafeFileMonitor>, SafeFileMonitor_Set, SafeFileMonitor> tempMap1 = SafeFile_f_t_Map;
MapOfMonitor<SafeFileMonitor> obj1 = tempMap1.getMap(TempRef_f);
if (obj1 == null) {
obj1 = new MapOfMonitor<SafeFileMonitor>(1);
tempMap1.putMap(TempRef_f, obj1);
}
MapOfMonitor<SafeFileMonitor> mainMap = obj1;
mainMonitor = mainMap.getLeaf(TempRef_t);

if (mainMonitor == null) {
if (mainMonitor == null) {
MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> origMap1 = SafeFile_t_Map;
origMonitor = origMap1.getLeaf(TempRef_t);
if (origMonitor != null) {
boolean timeCheck = true;

if (TempRef_f.getDisabled() > origMonitor.tau) {
timeCheck = false;
}

if (timeCheck) {
mainMonitor = (SafeFileMonitor)origMonitor.clone();
mainMonitor.RVMRef_f = TempRef_f;
if (TempRef_f.getTau() == -1){
TempRef_f.setTau(origMonitor.tau);
}
mainMap.putLeaf(TempRef_t, mainMonitor);

MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> tempMap2 = SafeFile_t_Map;
SafeFileMonitor_Set obj2 = tempMap2.getSet(TempRef_t);
monitors = obj2;
if (monitors == null) {
monitors = new SafeFileMonitor_Set();
tempMap2.putSet(TempRef_t, monitors);
}
monitors.add(mainMonitor);
}
}
}
if (mainMonitor == null) {
mainMonitor = new SafeFileMonitor();

mainMonitor.RVMRef_f = TempRef_f;
mainMonitor.RVMRef_t = TempRef_t;

mainMap.putLeaf(TempRef_t, mainMonitor);
mainMonitor.tau = SafeFile_timestamp;
if (TempRef_f.getTau() == -1){
TempRef_f.setTau(SafeFile_timestamp);
}
if (TempRef_t.getTau() == -1){
TempRef_t.setTau(SafeFile_timestamp);
}
SafeFile_timestamp++;

MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> tempMap3 = SafeFile_t_Map;
SafeFileMonitor_Set obj3 = tempMap3.getSet(TempRef_t);
monitors = obj3;
if (monitors == null) {
monitors = new SafeFileMonitor_Set();
tempMap3.putSet(TempRef_t, monitors);
}
monitors.add(mainMonitor);
}

TempRef_f.setDisabled(SafeFile_timestamp);
SafeFile_timestamp++;
}

SafeFile_f_t_Map_cachekey_0 = TempRef_f;
SafeFile_f_t_Map_cachekey_1 = TempRef_t;
SafeFile_f_t_Map_cachenode = mainMonitor;
}

mainMonitor.Prop_1_event_close(f, t);
failProp1 |= mainMonitor.Prop_1_Category_fail;
if(mainMonitor.Prop_1_Category_fail) {
mainMonitor.Prop_1_handler_fail(f, t);
}
SafeFile_RVMLock.unlock();
}

public static void beginCallEvent(Thread t) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}
SafeFileMonitor mainMonitor = null;
SafeFileMonitor_Set mainSet = null;
CachedTagWeakReference TempRef_t;

// Cache Retrieval
if (SafeFile_t_Map_cachekey_1 != null && t == SafeFile_t_Map_cachekey_1.get()) {
TempRef_t = SafeFile_t_Map_cachekey_1;

mainSet = SafeFile_t_Map_cacheset;
mainMonitor = SafeFile_t_Map_cachenode;
} else {
TempRef_t = SafeFile_Thread_RefMap.findOrCreateWeakRef(t);
}

if (mainSet == null || mainMonitor == null) {
MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> mainMap = SafeFile_t_Map;
mainMonitor = mainMap.getLeaf(TempRef_t);
mainSet = mainMap.getSet(TempRef_t);
if (mainSet == null){
mainSet = new SafeFileMonitor_Set();
mainMap.putSet(TempRef_t, mainSet);
}

if (mainMonitor == null) {
mainMonitor = new SafeFileMonitor();

mainMonitor.RVMRef_t = TempRef_t;

SafeFile_t_Map.putLeaf(TempRef_t, mainMonitor);
mainSet.add(mainMonitor);
mainMonitor.tau = SafeFile_timestamp;
if (TempRef_t.getTau() == -1){
TempRef_t.setTau(SafeFile_timestamp);
}
SafeFile_timestamp++;
}

SafeFile_t_Map_cachekey_1 = TempRef_t;
SafeFile_t_Map_cacheset = mainSet;
SafeFile_t_Map_cachenode = mainMonitor;
}

if(mainSet != null) {
mainSet.event_beginCall(t);
failProp1 = mainSet.failProp1;
}
SafeFile_RVMLock.unlock();
}

public static void endCallEvent(Thread t) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}
SafeFileMonitor mainMonitor = null;
SafeFileMonitor_Set mainSet = null;
CachedTagWeakReference TempRef_t;

// Cache Retrieval
if (SafeFile_t_Map_cachekey_1 != null && t == SafeFile_t_Map_cachekey_1.get()) {
TempRef_t = SafeFile_t_Map_cachekey_1;

mainSet = SafeFile_t_Map_cacheset;
mainMonitor = SafeFile_t_Map_cachenode;
} else {
TempRef_t = SafeFile_Thread_RefMap.findOrCreateWeakRef(t);
}

if (mainSet == null || mainMonitor == null) {
MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> mainMap = SafeFile_t_Map;
mainMonitor = mainMap.getLeaf(TempRef_t);
mainSet = mainMap.getSet(TempRef_t);
if (mainSet == null){
mainSet = new SafeFileMonitor_Set();
mainMap.putSet(TempRef_t, mainSet);
}

if (mainMonitor == null) {
mainMonitor = new SafeFileMonitor();

mainMonitor.RVMRef_t = TempRef_t;

SafeFile_t_Map.putLeaf(TempRef_t, mainMonitor);
mainSet.add(mainMonitor);
mainMonitor.tau = SafeFile_timestamp;
if (TempRef_t.getTau() == -1){
TempRef_t.setTau(SafeFile_timestamp);
}
SafeFile_timestamp++;
}

SafeFile_t_Map_cachekey_1 = TempRef_t;
SafeFile_t_Map_cacheset = mainSet;
SafeFile_t_Map_cachenode = mainMonitor;
}

if(mainSet != null) {
mainSet.event_endCall(t);
failProp1 = mainSet.failProp1;
}
SafeFile_RVMLock.unlock();
}

}
