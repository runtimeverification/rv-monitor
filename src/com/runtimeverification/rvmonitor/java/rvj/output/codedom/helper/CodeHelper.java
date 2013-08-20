package com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper;

import java.util.ArrayList;
import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.output.NotImplementedException;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMemberField;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.RuntimeMonitorType;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeNew.Level;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

public class CodeHelper {
	public static class VariableName {
		public static CodeVariable getWeakRef(CodeType type, RVMParameter param) {
			String name = "wr_" + param.getName();
			return new CodeVariable(type, name);
		}

		public static CodeMemberField getWeakRefInMonitor(RVMParameter param, CodeType weakreftype) {
			// The following convention had been hard-coded in the monitor-generating class; so,
			// I followed it.
			String fieldname = "RVMRef_" + param.getName();
			return new CodeMemberField(fieldname, false, true, weakreftype);
		}
		
		/**
		 * This method creates a variable used to store all the intermediate nodes while
		 * reaching the needed entry.
		 * @param type the type of the resulting variable
		 * @param query all the candidate parameters used to name the variable
		 * @param until specifies parameters that have been read (inclusive)
		 * @return a variable based on the given type and parameter name
		 */
		public static CodeVariable getInternalNode(CodeType type, RVMParameters query, int until) {
			StringBuilder s = new StringBuilder();
			s.append("node");
			for (int i = 0; i <= until; ++i) {
				RVMParameter prm = query.get(i);
				s.append('_');
				s.append(prm.getName());
			}
			return new CodeVariable(type, s.toString());
		}
		
		public static String getIndexingTreeCacheKeyName(String treename, RVMParameter param) {
			String name = treename;
			name += "_cachekey_";
			name += param.getName();
			return name;
		}

		public static String getIndexingTreeCacheValueName(String treename) {
			String name = treename;
			name += "_cachevalue";
			return name;
		}
	}
	
	public static class RuntimeType {
		public static RuntimeMonitorType.Tuple getIndexingTreeTuple(List<RuntimeMonitorType> fields) {
			if (fields.size() < 2 || fields.size() > 3)
				throw new NotImplementedException();
		
			String pkgname = "com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter";
			String clsname = "Tuple" + fields.size();
			CodeType type;
			{
				List<CodeType> generics = new ArrayList<CodeType>();
				for (RuntimeMonitorType field : fields)
					generics.add(field);
				type = new CodeType(pkgname, clsname, generics);
			}
			return RuntimeMonitorType.forTuple(type, fields);
		}
		
		public static RuntimeMonitorType getIndexingTree(Level map, CodeType set, CodeType leaf, int weakreftag) {
			boolean m = map != null;
			boolean s = set != null;
			boolean l = leaf != null;
			
			String clsname = "";

			switch (weakreftag) {
			case -1:
				break;
			case 0:
				clsname += "BasicRef";
				break;
			default:
				clsname += "MultiTagRef";
				break;
			}
			clsname += "MapOf";
			if (m && s && l)
				clsname += "All";
			else if (m && s)
				clsname += "MapSet";
			else if (s && l)
				clsname += "SetMonitor";
			else if (m)
				clsname += "Map";
			else if (s)
				clsname += "Set";
			else if (l)
				clsname += "Monitor";
			else
				throw new IllegalArgumentException();
			
			List<CodeType> generics = new ArrayList<CodeType>(3);
			if (m)
				generics.add(map.getCodeType());
			if (s)
				generics.add(set);
			if (l)
				generics.add(leaf);
			
			String pkgname = "com.runtimeverification.rvmonitor.java.rt.table";
			CodeType treetype = new CodeType(pkgname, clsname, generics);
			return RuntimeMonitorType.forIndexingTree(treetype);
		}

		public static CodeType getWeakReference() {
			String pkgname = "com.runtimeverification.rvmonitor.java.rt.ref";
			String clsname = "CachedWeakReference";
			return new CodeType(pkgname, clsname);
		}

		public static CodeType getDisableHolderFromMonitor(CodeType monitor) {
			// *Monitor -> *DisableHolder
			String clsname = monitor.getClassName();
			if (!clsname.endsWith("Monitor"))
				throw new NotImplementedException();
			int i = clsname.lastIndexOf("Monitor");
			clsname = clsname.substring(0, i);
			clsname += "DisableHolder";
			return new CodeType(monitor.getPackageName(), clsname);
		}

		public static CodeType getMonitorInterfaceFromMonitor(CodeType monitor) {
			// *Monitor -> I*Monitor
			String clsname = monitor.getClassName();
			if (!clsname.endsWith("Monitor"))
				throw new NotImplementedException();
			clsname = "I" + clsname;
			return new CodeType(monitor.getPackageName(), clsname);
		}

		public static CodeType getInternalBehaviorMultiplexer() {
			String pkgname = "com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.observable";
			String clsname = "InternalBehaviorMultiplexer";
			return new CodeType(pkgname, clsname);
		}
	}
}
