package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeGenerator;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.event.itf.WeakReferenceVariables;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree.RefTree;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeImplementation.Access;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeImplementation.Entry;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeImplementation.EventKind;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.newindexingtree.IndexingTreeImplementation.StmtCollectionInserter;
import com.runtimeverification.rvmonitor.java.rvj.output.monitor.SuffixMonitor;
import com.runtimeverification.rvmonitor.java.rvj.output.monitorset.MonitorSet;
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
public class IndexingTreeInterface implements ICodeGenerator {
	private final String name;
	private final RVMParameters specParams;
	private final RVMParameters queryParams;
	private final RVMParameters contentParams;
	private IndexingTreeImplementation impl;
	private IndexingCacheNew cache;

	public String getName() {
		return this.name;
	}

	public RVMParameters getSpecParams() {
		return this.specParams;
	}

	public RVMParameters getQueryParams() {
		return this.queryParams;
	}
	
	public String getPrettyName() {
		return IndexingTreeNameMangler.prettyName(this.queryParams, this.contentParams);
	}
	
	public IndexingTreeImplementation getImplementation() {
		return this.impl;
	}

	public boolean isMasterTree() {
		return this == this.impl.getMasterInterface();
	}
	
	public IndexingCacheNew getCache() {
		return this.cache;
	}

	public boolean isFullyFledgedTree() {
		// If an indexing tree does not take any parameter, then the code generator
		// generates a zero-level object (such as a set, monitor or tuple) for the
		// indexing tree, instead of an actual indexing tree.
		// It turned out that it's more consistent and easier to treat such trees as
		// indexing trees in most cases. However, if one needs to make distinction,
		// this method can tell the difference.
		RVMParameters params = this.getQueryParams();
		return params.size() > 0;
	}
	
	public IndexingTreeInterface(String aspectName, RVMParameters specParams, RVMParameters queryParams, RVMParameters contentParams, MonitorSet set, SuffixMonitor monitor, EventKind evttype, boolean timetrack) {
		this.specParams = specParams;
		this.queryParams = queryParams;
		this.contentParams = contentParams;

		IndexingTreeImplementation impl = new IndexingTreeImplementation(this, aspectName, specParams, queryParams, contentParams, set, monitor, evttype, timetrack);
		this.name = IndexingTreeNameMangler.fieldName(aspectName, queryParams, contentParams);
		this.switchImplementation(impl);
	}
	
	void switchImplementation(IndexingTreeImplementation newimpl) {
		this.impl = newimpl;
		this.cache = IndexingCacheNew.fromTree(this.name, this);
	}

	public boolean subsumes(IndexingTreeInterface that) {
		if (this.specParams != that.specParams) {
			// Combining indexing trees among specifications is not supported.
			// According to the original paper, this does not improve performance.
			throw new IllegalArgumentException();
		}
		
		return this.queryParams.subsumes(that.queryParams, this.specParams);
	}

	public void embedGWRT(RefTree refTree) {
		if (!this.isMasterTree())
			throw new IllegalArgumentException();

		this.impl.embedGWRT(refTree, this.specParams, this.queryParams);
	}

	public CodeStmtCollection generateFindOrCreateEntryCode(WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter) {
		return this.impl.generateFindOrCreateEntryCode(this.queryParams, this.specParams, weakrefs, inserter);
	}

	public CodeStmtCollection generateFindEntryWithStrongRefCode(WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter, boolean suppressLastNullCheck) {
		return this.impl.generateFindEntryWithStrongRefCode(this.queryParams, this.specParams, weakrefs, inserter, suppressLastNullCheck);
	}

	public CodeStmtCollection generateFindOrCreateCode(Access access, WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter) {
		return this.impl.generateFindOrCreateCode(this.queryParams, access, this.specParams, weakrefs, inserter);
	}

	public CodeStmtCollection generateFindCode(Access access, WeakReferenceVariables weakrefs, StmtCollectionInserter<CodeExpr> inserter) {
		return this.impl.generateFindCode(this.queryParams, access, this.specParams, weakrefs, inserter);
	}

	public CodeStmtCollection generateInsertMonitorCode(WeakReferenceVariables weakrefs, final CodeVarRefExpr monitorref) {
		return this.impl.generateInsertMonitorCode(this.queryParams, this.specParams, weakrefs, monitorref);
	}

	public Entry lookupEntry(RVMParameters params) {
		return this.impl.lookupEntry(params);
	}

	@Override
	public void getCode(ICodeFormatter fmt) {
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
}

class IndexingTreeNameMangler {
	public static String fieldName(String aspectName, RVMParameters queryParams, RVMParameters contentParams) {
		String name = aspectName + "_";
		if (contentParams == null)
			name += queryParams.parameterStringUnderscore();
		else
			name += queryParams.parameterStringUnderscore() + "__To__" + contentParams.parameterStringUnderscore();
		name += "_Map";
		return name;
	}

	public static String prettyName(RVMParameters queryParams, RVMParameters contentParams) {
		String from = queryParams.parameterStringUnderscore().replace('_', ',');
		String to = null;
		if (contentParams != null)
			to = contentParams.parameterStringUnderscore().replace('_', ',');
		
		if (to == null)
			return "<" + from + ">";
		return "<{" + from + "}:{" + to + "}>";
	}
}