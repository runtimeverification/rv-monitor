package com.runtimeverification.rvmonitor.java.rt.tablebase;


interface TupleTrait<TTuple> {
	public void set(TTuple targettuple, TTuple newtuple, int flag);
}

class Tuple2Trait<T extends IIndexingTreeValue, U extends IIndexingTreeValue> implements TupleTrait<Tuple2<T, U>> {
	@Override
	public void set(Tuple2<T, U> targettuple, Tuple2<T, U> newtuple, int flag) {
		switch (flag) {
		case 1:
			targettuple.setValue1(newtuple.getValue1());
			break;
		case 2:
			targettuple.setValue2(newtuple.getValue2());
			break;
		default:
			assert false;
		}
	}
}

class Tuple3Trait<T extends IIndexingTreeValue, U extends IIndexingTreeValue, V extends IIndexingTreeValue> implements TupleTrait<Tuple3<T, U, V>> {
	@Override
	public void set(Tuple3<T, U, V> targettuple, Tuple3<T, U, V> newtuple, int flag) {
		switch (flag) {
		case 1:
			targettuple.setValue1(newtuple.getValue1());
			break;
		case 2:
			targettuple.setValue2(newtuple.getValue2());
			break;
		case 3:
			targettuple.setValue3(newtuple.getValue3());
			break;
		default:
			assert false;
		}
	}
}

class Tuple0 implements IIndexingTreeValue {
	@Override
	public final void terminate(int treeid) {
	}
	
	@Override
	public final boolean checkTerminatedWhileCleaningUp() {
		// Since this table is a pure GWRT (i.e., this is not an indexing tree),
		// no value is associated with a key. Thus, it is infeasible for this tuple
		// to notify the caller that the value is no longer needed.
		return false;
	}
}

class Tuple2<T extends IIndexingTreeValue, U extends IIndexingTreeValue> implements IIndexingTreeValue {
	private T value1;
	private U value2;
	
	public final T getValue1() {
		return this.value1;
	}
	
	public final U getValue2() {
		return this.value2;
	}
	
	public final void setValue1(T val1) {
		this.value1 = val1;
	}
	
	public final void setValue2(U val2) {
		this.value2 = val2;
	}
	
	public Tuple2(T v1, U v2) {
		this.value1 = v1;
		this.value2 = v2;
	}

	@Override
	public final void terminate(int treeid) {
		if (this.value1 != null)
			this.value1.terminate(treeid);
		if (this.value2 != null)
			this.value2.terminate(treeid);
	}
	
	@Override
	public final boolean checkTerminatedWhileCleaningUp() {
		if (this.value1 != null) {
			if (this.value1.checkTerminatedWhileCleaningUp())
				this.value1 = null;
		}
		if (this.value2 != null) {
			if (this.value2.checkTerminatedWhileCleaningUp())
				this.value2 = null;
		}
		return this.value1 == null && this.value2 == null;
	}
}

class Tuple3<T extends IIndexingTreeValue, U extends IIndexingTreeValue, V extends IIndexingTreeValue> implements IIndexingTreeValue {
	private T value1;
	private U value2;
	private V value3;
	
	public final T getValue1() {
		return this.value1;
	}
	
	public final U getValue2() {
		return this.value2;
	}
	
	public final V getValue3() {
		return this.value3;
	}
	
	public final void setValue1(T val1) {
		this.value1 = val1;
	}
	
	public final void setValue2(U val2) {
		this.value2 = val2;
	}
	
	public final void setValue3(V val3) {
		this.value3 = val3;
	}
	
	public Tuple3(T v1, U v2, V v3) {
		this.value1 = v1;
		this.value2 = v2;
		this.value3 = v3;
	}

	@Override
	public final void terminate(int treeid) {
		if (this.value1 != null)
			this.value1.terminate(treeid);
		if (this.value2 != null)
			this.value2.terminate(treeid);
		if (this.value3 != null)
			this.value3.terminate(treeid);
	}
	
	@Override
	public final boolean checkTerminatedWhileCleaningUp() {
		if (this.value1 != null) {
			if (this.value1.checkTerminatedWhileCleaningUp())
				this.value1 = null;
		}
		if (this.value2 != null) {
			if (this.value2.checkTerminatedWhileCleaningUp())
				this.value2 = null;
		}
		if (this.value3 != null) {
			if (this.value3.checkTerminatedWhileCleaningUp())
				this.value3 = null;
		}
		return this.value1 == null && this.value2 == null && this.value3 == null;
	}
}
