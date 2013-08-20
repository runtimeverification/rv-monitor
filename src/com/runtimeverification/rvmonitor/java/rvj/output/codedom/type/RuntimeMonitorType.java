package com.runtimeverification.rvmonitor.java.rvj.output.codedom.type;

import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeHelper;

public class RuntimeMonitorType extends CodeType {
	protected RuntimeMonitorType(CodeType t) {
		super(t);
	}
	
	public static RuntimeMonitorType.Interface forInterface(CodeType monitortype) {
		CodeType i = CodeHelper.RuntimeType.getMonitorInterfaceFromMonitor(monitortype);
		CodeType d = CodeHelper.RuntimeType.getDisableHolderFromMonitor(monitortype);
		return new Interface(i, d, monitortype);
	}
	
	public static RuntimeMonitorType.DisableHolder forDisableHolder(CodeType monitortype) {
		CodeType t = CodeHelper.RuntimeType.getDisableHolderFromMonitor(monitortype);
		return new DisableHolder(t);
	}
	
	public static RuntimeMonitorType.Monitor forMonitor(CodeType monitortype) {
		return new Monitor(monitortype);
	}

	public static RuntimeMonitorType.MonitorSet forMonitorSet(CodeType settype) {
		if (settype == null)
			return null;
		return new MonitorSet(settype);
	}
	
	public static RuntimeMonitorType.Tuple forTuple(CodeType tupletype, List<RuntimeMonitorType> elems) {
		return new Tuple(tupletype, elems);
	}

	public static RuntimeMonitorType.IndexingTree forIndexingTree(CodeType treetype) {
		return new IndexingTree(treetype);
	}
	
	public static class Leaf extends RuntimeMonitorType {
		protected Leaf(CodeType t) {
			super(t);
		}
	}
	
	public static class Interface extends Leaf {
		private final DisableHolder disholder;
		private final Monitor monitor;

		public DisableHolder getDisableHolderType() {
			return this.disholder;
		}
		
		public Monitor getMonitorType() {
			return this.monitor;
		}

		Interface(CodeType itf, CodeType disholder, CodeType monitor) {
			super(itf);
	
			this.disholder = new DisableHolder(disholder);
			this.monitor = new Monitor(monitor);
		}
	}
	
	public static class DisableHolder extends Leaf {
		DisableHolder(CodeType t) {
			super(t);
		}
	}
	
	public static class Monitor extends Leaf {
		Monitor(CodeType t) {
			super(t);
		}
	}
	
	public static class MonitorSet extends RuntimeMonitorType {
		MonitorSet(CodeType t) {
			super(t);
		}
	}
	
	public static class Tuple extends RuntimeMonitorType {
		private final List<RuntimeMonitorType> elems;
		
		public List<RuntimeMonitorType> getElements() {
			return this.elems;
		}

		Tuple(CodeType t, List<RuntimeMonitorType> elems) {
			super(t);
			
			this.elems = elems;
		}
	}
	
	public static class IndexingTree extends RuntimeMonitorType {
		IndexingTree(CodeType t) {
			super(t);
		}
	}
}
