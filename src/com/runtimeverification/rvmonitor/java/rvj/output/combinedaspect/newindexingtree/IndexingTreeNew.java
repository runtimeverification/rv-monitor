package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runtimeverification.rvmonitor.java.rvj.output.NotImplementedException;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeAssignStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeBlockStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeExprStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeFieldRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMemberField;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeMethodInvokeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeNewExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeObject;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarDeclStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeHelper;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodePair;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeGenerator;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.CodeType;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.type.RuntimeMonitorType;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.itf.WeakReferenceVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameters;

/**
 * This class represents an indexing tree, which may embed GWRT.
 * 
 * This class is similar to indexingtree.IndexingTree and others, but has been
 * separated in order to generate safer, more readable and more efficient code.
 * I also tried to avoid redundant code that exists in the old classes.
 * 
 * @author Choonghwan Lee <clee83@illinois.edu>
 */
public class IndexingTreeNew implements ICodeGenerator {
	private final RVMParameters specParams;
	private final RVMParameters queryParams;
	private final RVMParameters contentParams;
	private final Entry topEntry;
	private final CodeMemberField field;
	private final IndexingCacheNew cache;

	public IndexingCacheNew getCache() {
		return this.cache;
	}
	
	public String getName() {
		return this.field.getName();
	}
	
	public RVMParameters getQueryParams() {
		return this.queryParams;
	}
	
	public CodeMemberField getField() {
		return this.field;
	}
	
	/**
	 * 
	 * @param aspectName
	 * @param specParams
	 * @param queryParams
	 * @param contentParams
	 * @param set
	 * @param monitor
	 * @param evttype
	 * @param timetrack specifies whether time tracking (keeping the 'disable' and 't' fields) is necessary
	 */
	public IndexingTreeNew(String aspectName, RVMParameters specParams, RVMParameters queryParams, RVMParameters contentParams, MonitorSet set, SuffixMonitor monitor, EventKind evttype, boolean timetrack) {
		this.specParams = specParams;
		this.queryParams = queryParams;
		this.contentParams = contentParams;

		{
			CodeType settype = new CodeType(set.getName().toString());
			CodeType monitortype = new CodeType(monitor.getOutermostName().toString());
			boolean fullbinding = specParams.equals(queryParams);
			this.topEntry = Entry.determine(queryParams, settype, monitortype, fullbinding, evttype, timetrack);
		}
		
		String name = aspectName + "_";
		if (contentParams == null)
			name += queryParams.parameterStringUnderscore();
		else
			name += queryParams.parameterStringUnderscore() + "__To__" + contentParams.parameterStringUnderscore();
		name += "_Map";

		this.field = this.topEntry.generateField(name, this.specParams, this.queryParams);
		this.cache = IndexingCacheNew.fromTree(name, this.topEntry);
	}
	
	public static IndexingTreeNew combine(IndexingTreeNew l, IndexingTreeNew r) throws IllegalArgumentException {
		throw new NotImplementedException();
	}
	
	public String getPrettyName() {
		String from = this.queryParams.parameterStringUnderscore().replace('_', ',');
		String to = null;
		if (this.contentParams != null)
			to = this.contentParams.parameterStringUnderscore().replace('_', ',');
		
		if (to == null)
			return "<" + from + ">";
		return "<{" + from + "}:{" + to + "}>";
	}

	/**
	 * This method creates an empty block and puts the provided body into it, in order to
	 * avoid potential name clashes.
	 * @param body the body that will be put inside of the empty block
	 * @param comment optional comment for debugging
	 * @return the resulting block
	 */
	private CodeStmtCollection promoteSeparateBlock(CodeStmtCollection body, String comment) {
		if (body == null)
			return null;
	
//		if (comment != null)
//			body = new CodeStmtCollection(new CodeCommentStmt(comment), body);
		
		CodeBlockStmt block = new CodeBlockStmt(body);
		return new CodeStmtCollection(block);
	}
	
	/**
	 * This method generates code for accessing the entry that corresponds to
	 * the given list of weak references specified by 'weakrefs'.
	 * An entry contains a leaf and/or a set.
	 * This method may create internal nodes if they have not been created.
	 * @param weakrefs the list of weak references used to access the node or leaf
	 * @return pair of the generated code and the reference to the found node or leaf
	 */
	public CodeStmtCollection generateFindOrCreateEntryCode(WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter) {
		CodeFieldRefExpr root = new CodeFieldRefExpr(this.field);
		CodeStmtCollection stmts = this.topEntry.generateFindOrCreateCode(this.queryParams, Access.Entry, this.specParams, weakrefs, inserter, root);
		return this.promoteSeparateBlock(stmts, "FindOrCreateEntry");
	}

	public CodeStmtCollection generateFindEntryWithStrongRefCode(WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter, boolean suppressLastNullCheck) {
		CodeFieldRefExpr root = new CodeFieldRefExpr(this.field);
		CodeStmtCollection stmts = this.topEntry.generateFindWithStrongRefCode(this.queryParams, Access.Entry, weakrefs, inserter, root, suppressLastNullCheck);
		return this.promoteSeparateBlock(stmts, "FindEntry");
	}
	
	public CodeStmtCollection generateFindOrCreateCode(Access access, WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter) {
		CodeFieldRefExpr root = new CodeFieldRefExpr(this.field);
		CodeStmtCollection stmts = this.topEntry.generateFindOrCreateCode(this.queryParams, access, this.specParams, weakrefs, inserter, root);
		return this.promoteSeparateBlock(stmts, "FindOrCreate");
	}

	public CodeStmtCollection generateFindCode(Access access, WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter) {
		CodeFieldRefExpr root = new CodeFieldRefExpr(this.field);
		CodeStmtCollection stmts = this.topEntry.generateFindCode(this.queryParams, access, weakrefs, inserter, root);
		return this.promoteSeparateBlock(stmts, "FindCode");
	}

	public CodeStmtCollection generateInsertMonitorCode(WeakReferenceVariables weakrefs, final CodeVarRefExpr monitorref) {
		CodeFieldRefExpr root = new CodeFieldRefExpr(this.field);
		StmtCollectionInserter<CodeExpr> inserter = new StmtCollectionInserter<CodeExpr>() {
			@Override
			public CodeStmtCollection insertLastEntry(Entry entry, CodeExpr entryref) {
				CodeStmtCollection stmts = new CodeStmtCollection();
				CodePair<CodeVarRefExpr> codepair = entry.generateFieldGetCode(entryref, Access.Set, "target");
				stmts.add(codepair.getGeneratedCode());
				
				CodeVarRefExpr fieldref = codepair.getLogicalReturn();
				CodeMethodInvokeExpr invoke = new CodeMethodInvokeExpr(CodeType.foid(), fieldref, "add", monitorref);
				stmts.add(new CodeExprStmt(invoke));
				return stmts;
			}
		};
		CodeStmtCollection stmts = this.topEntry.generateFindOrCreateCode(this.queryParams, Access.Entry, this.specParams, weakrefs, inserter, root);
		return this.promoteSeparateBlock(stmts, "InsertMonitor");
	}
	
	/**
	 * This method returns the exact type of this indexing tree.
	 * The returned type can be used to declare this tree in the generated code.
	 * @return the type of this indexing tree
	 */
	public CodeType getCodeType() {
		return this.topEntry.getCodeType();
	}
	
	public Entry lookupEntry(RVMParameters params) {
		return this.topEntry.lookupEntry(params);
	}

	/**
	 * This method returns the exact type of the leaf or set that corresponds to
	 * 'eventParams', the set of parameters, by following each level one-by-one.
	 * It should be noted that the type is not necessarily the leaf of this
	 * indexing tree, because multiple trees with the same prefix can be combined.
	 * @param access specifies whether set or leaf is wanted
	 * @param params the set of parameters
	 * @return the exact type of the entry
	 */
	public CodeType getQueryResultType(Access access, RVMParameters params) {
		return this.topEntry.query(access, params);
	}

	public CodeType getQueryResultType(Access access) {
		Entry entry = this.topEntry.getDeepestEntry();
		return entry.accessEntry(access);
	}
	
	/**
	 * Returns the type of the type of the second last entry.
	 * @return null if the top entry is the last entry
	 */
	public CodeType getSecondLastResultType() {
		if (this.topEntry.map == null)
			return null;
	
		Entry entry = this.topEntry.getSecondDeepestEntry();
		return entry.accessEntry(Access.Map);
	}
	
	public boolean canQuery(Access access, RVMParameters params) {
		try {
			this.topEntry.query(access, params);
			return true;
		}
		catch (IllegalArgumentException ignored) {
			return false;
		}
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
		this.field.getCode(fmt);
		
		if (this.cache != null)
			this.cache.getCode(fmt);
	}
	
	@Override
	public String toString() {
		String q = this.queryParams.parameterString();
		String c = this.contentParams == null ? null : this.contentParams.parameterString();
		if (c == null)
			return q;
		return q + " -> " + c;
	}
	
	public static class StmtCollectionInserter<T extends CodeObject> {
		/**
		 * Allows the user to insert code at the second last map.
		 * By the second last map, we mean the map that lies right
		 * before the leaf node; i.e., there is no more map below this.
		 * @param mapref reference to the second last map
		 * @return code to be added
		 */
		public CodeStmtCollection insertSecondLastMap(T mapref) {
			return null;
		}
		
		/**
		 * Allows the user to insert code at the last entry.
		 * By the last entry, we mean there is no entry below this.
		 * This also implies that this entry does not contain any map.
		 * @param entry the last entry
		 * @param entryref reference to the last entry
		 * @return code to be added
		 */
		public CodeStmtCollection insertLastEntry(Entry entry, T entryref) {
			return null;
		}
	
		/**
		 * Allows the user to insert code at the last field (such as map, set or leaf).
		 * @param entry the last entry that includes the provided field
		 * @param contextref reference to the field
		 * @return code to be added
		 */
		public CodeStmtCollection insertLastField(Entry entry, T fieldref) {
			return null;
		}
	}
	
	public enum Access {
		Entry,
		Map,
		Set,
		Leaf,
	}
	
	public enum LeafKind {
		Monitor,
		Holder,
	}
	
	public enum EventKind {
		AlwaysCreate,
		MayCreate,
		NeverCreate,
	}
	
	public static class Entry {
		private final Level map;
		private final RuntimeMonitorType.MonitorSet set;
		private final RuntimeMonitorType.Leaf leaf;
		private final Map<LeafKind, RuntimeMonitorType.Leaf> leafTypes;
		
		public final Level getMap() {
			return this.map;
		}
		
		public final CodeType getSet() {
			return this.set;
		}

		public final RuntimeMonitorType getLeaf() {
			return this.leaf;
		}
		
		public final RuntimeMonitorType getLeafType(LeafKind leaf) {
			RuntimeMonitorType t = this.leafTypes.get(leaf);
			if (t == null)
				throw new IllegalArgumentException();
			return t;
		}
		
		private Entry(Level map, CodeType set, CodeType monitor, Set<LeafKind> leafflags) {
			RuntimeMonitorType.Leaf leaftype = null;
			Map<LeafKind, RuntimeMonitorType.Leaf> typemap = new HashMap<LeafKind, RuntimeMonitorType.Leaf>();
			if (leafflags != null) {
				int numtypes = 0;
				for (LeafKind l : leafflags) {
					switch (l) {
					case Monitor:
						leaftype = RuntimeMonitorType.forMonitor(monitor);
						typemap.put(l, leaftype);
						break;
					case Holder:
						leaftype = RuntimeMonitorType.forDisableHolder(monitor);
						typemap.put(l, leaftype);
						break;
					default:
						throw new NotImplementedException();
					}
					++numtypes;
				}
				
				if (numtypes > 1)
					leaftype = RuntimeMonitorType.forInterface(monitor);
			}
			
			this.map = map;
			this.set = RuntimeMonitorType.forMonitorSet(set);
			this.leaf = leaftype;
			this.leafTypes = typemap;
		}
		
		public static Entry determine(RVMParameters params, CodeType set, CodeType leaf, boolean fullbinding, EventKind evttype, boolean timetrack) {
			if (params.size() == 0) {
				Set<LeafKind> leafflags;
				
				if (timetrack) {
					switch (evttype) {
					case AlwaysCreate:
						leafflags = EnumSet.of(LeafKind.Monitor);
						break;
					case MayCreate:
						leafflags = EnumSet.of(LeafKind.Monitor, LeafKind.Holder);
						break;
					case NeverCreate:
						if (fullbinding) {
							// Even though the given event is not a creation event, a monitor instance can be
							// created by combining existing parameters.
							leafflags = EnumSet.of(LeafKind.Monitor, LeafKind.Holder);
						}
						else {
							// Otherwise, a monitor instance may not be created. This is based on conjecture; i.e.,
							// this assumption might be wrong. Unfortunately, this assumption is wrong. At this
							// moment, let's do conservatively.
							// leafflags = EnumSet.of(LeafKind.Holder);
							leafflags = EnumSet.of(LeafKind.Monitor, LeafKind.Holder);
						}
						break;
					default:
						throw new NotImplementedException();
					}
				}
				else {
					// If time tracking is unnecessary, a DisableHolder object will never be created.
					leafflags = EnumSet.of(LeafKind.Monitor);
				}

				if (fullbinding)
					return new Entry(null, null, leaf, leafflags);
				else {
					// If time tracking is unnecessary, there is no need to have a field for
					// a DisableHolder, which is merely for time tracking.
					if (timetrack)
						return new Entry(null, set, leaf, leafflags);
					else
						return new Entry(null, set, null, null);
				}
			}
			else {
				Level map = Level.determine(params, set, leaf, fullbinding, evttype, timetrack);
				return new Entry(map, null, null, null);
			}
		}
		
		public Entry lookupEntry(RVMParameters params) {
			List<RVMParameter> list = params.toList();
			return this.lookupEntry(list, 0);
		}

		public Entry lookupEntry(RVMParameter ... params) {
			List<RVMParameter> list = Arrays.asList(params);
			return this.lookupEntry(list, 0);
		}
		
		private Entry lookupEntry(List<RVMParameter> params, int index) {
			if (index == params.size())
				return this;
	
			if (this.map == null)
				throw new IllegalArgumentException();

			RVMParameter param = params.get(index);
			if (!this.map.key.equals(param))
				throw new IllegalArgumentException();
			
			return this.map.value.lookupEntry(params, index + 1);
		}
		
		public Entry getDeepestEntry() {
			if (this.map == null)
				return this;
			return this.map.value.getDeepestEntry();
		}
		
		public Entry getSecondDeepestEntry() {
			Entry entry;
			for (entry = this; entry.map != null; entry = entry.map.value) {
				if (entry.map.value.map == null)
					break;
			}
			return entry;
		}
		
		public CodeType query(IndexingTreeNew.Access access, RVMParameters params) {
			Entry entry = this.lookupEntry(params);
			return entry.accessEntry(access);
		}
		
		public CodeType query(IndexingTreeNew.Access access, RVMParameter ... params) {
			Entry entry = this.lookupEntry(params);
			return entry.accessEntry(access);
		}
		
		private CodeType accessEntry(IndexingTreeNew.Access access) {
			switch (access) {
			case Entry:
				return this.getCodeType();
			case Map:
				if (this.map == null)
					throw new IllegalArgumentException();
				return this.map.getCodeType();
			case Set:
				if (this.set == null)
					throw new IllegalArgumentException();
				return this.set;
			case Leaf:
				if (this.leaf == null)
					throw new IllegalArgumentException();
				return this.leaf;
			default:
				throw new IllegalArgumentException();
			}
		}
		
		public CodeType getCodeType() {
			ArrayList<RuntimeMonitorType> enabled = new ArrayList<RuntimeMonitorType>();
			
			if (this.map != null)
				enabled.add(this.map.getCodeType());
			if (this.set != null)
				enabled.add(this.set);
			if (this.leaf != null)
				enabled.add(this.leaf);
	
			if (enabled.size() == 1)
				return enabled.get(0);
			
			return CodeHelper.RuntimeType.getIndexingTreeTuple(enabled);
		}
		
		private int calculateFieldIndex(Access access) {
			boolean m = this.map != null;
			boolean s = this.set != null;
			boolean l = this.leaf != null;
			int count = (m ? 1 : 0) + (s ? 1 : 0) + (l ? 1 : 0);
			
			// No wrappers are created if the entry holds a single field.
			if (count == 1 || access == Access.Entry)
				return 0;

			int index = 0;
			switch (access) {
			case Entry:
				throw new NotImplementedException();
			case Map: index = 1; break;
			case Set: index = 2; break;
			case Leaf: index = 3; break;
			default:
				throw new NotImplementedException();
			}
			
			if (m) {
				if (access == Access.Map) return index;
			}
			else --index;
			
			if (s) {
				if (access == Access.Set) return index;
			}
			else --index;
			
			if (l) {
				if (access == Access.Leaf) return index;
			}
			else --index;
			throw new IllegalArgumentException();
		}

		public CodeMemberField generateField(String name, RVMParameters specParams, RVMParameters queryParams) {
			CodeExpr init;
			CodeType type = this.getCodeType();
			{
				List<CodeExpr> args = new ArrayList<CodeExpr>();
				if (type instanceof RuntimeMonitorType.Tuple) {
					RuntimeMonitorType.Tuple tuple = (RuntimeMonitorType.Tuple)type;
					for (RuntimeMonitorType elem : tuple.getElements()) {
						// A set in a tuple needs to be instantiated.
						if (elem instanceof RuntimeMonitorType.MonitorSet) {
							CodeExpr createset = new CodeNewExpr(elem);
							args.add(createset);
						}
						else
							args.add(CodeLiteralExpr.nul());
					}
				}
				else if (this.getMap() != null) {
					// A map need an argument, which indicates the tree id.
					args.add(CodeLiteralExpr.integer(specParams.getIdnum(queryParams.get(0))));
				}
				init = new CodeNewExpr(type, args);
			}

			return new CodeMemberField(name, true, false, type, init);
		}

		/**
		 * Generates code for accessing a specific field from the given entry.
		 * If the entry has only a single field (i.e., the type is not Tuple2 or Tuple3),
		 * this method does not do anything.
		 * @param entryref the entry where the field is retrieved
		 * @param access specifies which field is of interest
		 * @param varnameprefix the prefix of the variable where the retrieved field is assigned
		 * @return the pair of the generated code and the reference to either the created variable or the entry
		 */
		public CodePair<CodeVarRefExpr> generateFieldGetCode(CodeExpr entryref, Access access, String varnameprefix) {
			CodeExpr result;

			int index = this.calculateFieldIndex(access);
			if (index == 0)
				result = entryref;
			else {
				String getter = "getValue" + index;
				CodeType type = this.accessEntry(access);
				result = new CodeMethodInvokeExpr(type, entryref, getter);
			}
			
			if (result instanceof CodeVarRefExpr)
				return new CodePair<CodeVarRefExpr>((CodeVarRefExpr)result);
	
			String varname;
			if (varnameprefix == null)
				varname = access.toString().toLowerCase();
			else
				varname = varnameprefix + access.toString();
			CodeVariable var = new CodeVariable(result.getType(), varname);

			CodeVarDeclStmt decl = new CodeVarDeclStmt(var, result);
			return new CodePair<CodeVarRefExpr>(decl, new CodeVarRefExpr(var));
		}

		public CodeStmtCollection generateFieldSetCode(CodeExpr entryref, Access access, CodeExpr value) {
			CodeStmt assign;

			int index = this.calculateFieldIndex(access);
			if (index == 0) {
				assign = new CodeAssignStmt(entryref, value);
			}
			else {
				String setter = "setValue" + index;
				CodeType type = this.accessEntry(access);
				CodeExpr invoke = new CodeMethodInvokeExpr(type, entryref, setter, value);
				assign = new CodeExprStmt(invoke);
			}
	
			return new CodeStmtCollection(assign);
		}
	
		private CodeStmtCollection traverseNodeInternal(RVMParameters query, int index, WeakReferenceVariables weakrefs, boolean usestrongref, CodeExpr entryref, IIndexingTreeVisitor<CodeVarRefExpr> visitor) {
			CodeStmtCollection stmts = new CodeStmtCollection();
			if (index == query.size())
				stmts.add(visitor.visitLast(this, entryref));
			else {
				if (index + 1 == query.size())
					stmts.add(visitor.visitSecondLast(this, entryref));

				RVMParameter head = query.get(index);
				if (this.map == null)
					throw new IllegalArgumentException();
				if (!this.map.key.equals(head))
					throw new IllegalArgumentException();
				Entry child = this.map.value;
				CodeVarRefExpr weakref = new CodeVarRefExpr(weakrefs.getWeakRef(head));

				CodeVarRefExpr resultref;
				{
					CodeMethodInvokeExpr invoke;
					if (usestrongref) {
						if (index + 1 == query.size()) {
							CodeType type = child.accessEntry(Access.Entry);
							CodeVarRefExpr strongref = new CodeVarRefExpr(new CodeVariable(type, head.getName()));
							invoke = new CodeMethodInvokeExpr(type, entryref, "getNodeWithStrongRef", strongref);
						}
						else
							invoke = null;
					}
					else {
						if (index + 1 == query.size()) {
							CodeType type = child.accessEntry(Access.Entry);
							invoke = new CodeMethodInvokeExpr(type, entryref, "getNode", weakref);
						}
						else {
							CodeType type = child.accessEntry(Access.Map);
							invoke = new CodeMethodInvokeExpr(type, entryref, "getMap", weakref);
						}
					}
					CodeVariable result = CodeHelper.VariableName.getInternalNode(invoke.getType(), query, index);
					resultref = new CodeVarRefExpr(result);
					CodeVarDeclStmt stmt = new CodeVarDeclStmt(result, invoke);
					stmts.add(stmt);
				}

				RVMParameter nextprm = query.get(index + 1);
				stmts.add(visitor.visitPreNode(this, entryref, weakref, resultref, nextprm));
	
				CodeStmtCollection nested = child.traverseNodeInternal(query, index + 1, weakrefs, usestrongref, resultref, visitor);
				stmts.add(visitor.visitPostNode(this, resultref, nextprm, nested));
			}
			return stmts;
		}
		
		private CodeStmtCollection traverseNode(RVMParameters query, WeakReferenceVariables weakrefs, boolean usestrongref, CodeExpr entryref, IIndexingTreeVisitor<CodeVarRefExpr> visitor) {
			return this.traverseNodeInternal(query, 0, weakrefs, usestrongref, entryref, visitor);
		}
		
		static CodeStmtCollection callInsertSecondLastMap(StmtCollectionInserter<CodeExpr> inserter, Entry entry, CodeExpr entryref) {
			CodeStmtCollection stmts = new CodeStmtCollection();
	
			CodePair<CodeVarRefExpr> codepair = entry.generateFieldGetCode(entryref, Access.Map, "itmd");
			stmts.add(codepair.getGeneratedCode());
			CodeVarRefExpr mapref = codepair.getLogicalReturn();
			
			CodeStmtCollection usercode = inserter.insertSecondLastMap(mapref);
			if (usercode != null) {
				stmts.add(usercode);
				return stmts;
			}

			return null;
		}
		
		static CodeStmtCollection callInsertLastEntryAndField(Access access, StmtCollectionInserter<CodeExpr> inserter, Entry entry, CodeExpr entryref) {
			CodeStmtCollection stmts = new CodeStmtCollection();
			
			CodeStmtCollection usercode1 = inserter.insertLastEntry(entry, entryref);
			stmts.add(usercode1);
	
			if (access != Access.Entry) {
				CodeStmtCollection stmts2 = new CodeStmtCollection();
				CodePair<CodeVarRefExpr> codepair = entry.generateFieldGetCode(entryref, access, "itmd");
				stmts2.add(codepair.getGeneratedCode());
				CodeVarRefExpr fieldref = codepair.getLogicalReturn();
	
				CodeStmtCollection usercode2 = inserter.insertLastField(entry, fieldref);
				if (usercode2 != null) {
					stmts2.add(usercode2);
					stmts.add(stmts2);
				}
			}
	
			return stmts;
		}

		static CodeStmtCollection generateFieldGetCodeInVisitList(Access access, StmtCollectionInserter<CodeExpr> inserter, Entry entry, CodeExpr entryref) {
			CodeStmtCollection stmts = new CodeStmtCollection();
			CodePair<CodeVarRefExpr> codepair = entry.generateFieldGetCode(entryref, access, "itmd");
			stmts.add(codepair.getGeneratedCode());
			CodeVarRefExpr fieldref = codepair.getLogicalReturn();

			CodeStmtCollection usercode = inserter.insertLastField(entry, fieldref);
			if (usercode == null)
				return null;
			stmts.add(usercode);
			return stmts;
		}

		CodeStmtCollection generateFindOrCreateCode(RVMParameters query, final Access access, RVMParameters specprms, WeakReferenceVariables weakrefs, final StmtCollectionInserter<CodeExpr> inserter, CodeExpr entryref) {
			IIndexingTreeVisitor<CodeVarRefExpr> visitor = new GenerativeIndexingTreeVisitor(specprms) {
				@Override
				public CodeStmtCollection visitSecondLast(Entry entry, CodeExpr entryref) {
					return Entry.callInsertSecondLastMap(inserter, entry, entryref);
				}

				@Override
				public CodeStmtCollection visitLast(Entry entry, CodeExpr entryref) {
					return Entry.callInsertLastEntryAndField(access, inserter, entry, entryref);
				}
			};

			return this.traverseNode(query, weakrefs, false, entryref, visitor);
		}

		CodeStmtCollection generateFindWithStrongRefCode(RVMParameters query, final Access access, WeakReferenceVariables weakrefs, final StmtCollectionInserter<CodeExpr> inserter, CodeExpr entryref, boolean suppressLastNullCheck) {
			IIndexingTreeVisitor<CodeVarRefExpr> visitor = new NonGenerativeIndexingTreeVisitor(suppressLastNullCheck) {
				@Override
				public CodeStmtCollection visitSecondLast(Entry entry, CodeExpr entryref) {
					return null;
				}

				@Override
				public CodeStmtCollection visitLast(Entry entry, CodeExpr entryref) {
					return Entry.callInsertLastEntryAndField(access, inserter, entry, entryref);
				}
			};
			
			return this.traverseNode(query, weakrefs, true, entryref, visitor);
		}

		CodeStmtCollection generateFindCode(RVMParameters query, final Access access, WeakReferenceVariables weakrefs, final StmtCollectionInserter<CodeExpr> inserter, CodeExpr entryref) {
			IIndexingTreeVisitor<CodeVarRefExpr> visitor = new NonGenerativeIndexingTreeVisitor(false) {
				@Override
				public CodeStmtCollection visitSecondLast(Entry entry, CodeExpr entryref) {
//					return Entry.callInsertSecondLastMap(inserter, entry, entryref);
					return null;
				}

				@Override
				public CodeStmtCollection visitLast(Entry entry, CodeExpr entryref) {
					return Entry.callInsertLastEntryAndField(access, inserter, entry, entryref);
				}
			};
			
			return this.traverseNode(query, weakrefs, false, entryref, visitor);
		}

		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
	
			List<String> nested = new ArrayList<String>(); 
			if (this.map != null)
				nested.add(this.map.toString());
			if (this.set != null)
				nested.add("S");
			if (this.leaf != null)
				nested.add("L");
			
			if (nested.size() > 1)
				s.append("<");
			for (int i = 0; i < nested.size(); ++i) {
				if (i > 0)
					s.append(',');
				s.append(nested.get(i));
			}
			if (nested.size() > 1)
				s.append(">");
	
			return s.toString();
		}
	}
	
	public static class Level {
		private final RVMParameter key;
		private final Entry value;
		
		public final RVMParameter getKey() {
			return this.key;
		}
		
		public final Entry getValue() {
			return this.value;
		}
		
		private Level(RVMParameter key, Entry value) {
			this.key = key;
			this.value = value;
		}
		
		public static Level determine(RVMParameters params, CodeType set, CodeType leaf, boolean fullbinding, EventKind evttype, boolean timetrack) {
			if (params.size() < 1)
				throw new IllegalArgumentException();

			RVMParameter key = params.get(0);
			Entry value = Entry.determine(params.tail(), set, leaf, fullbinding, evttype, timetrack);
			return new Level(key, value);
		}
		
		public RuntimeMonitorType getCodeType() {
			return CodeHelper.RuntimeType.getIndexingTree(this.value.map, this.value.set, this.value.leaf, -1);
		}	

		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append('[');
			s.append(this.key.getName());
			s.append(":");
			s.append(this.value.toString());
			s.append("]");
			return s.toString();
		}
	}
}
