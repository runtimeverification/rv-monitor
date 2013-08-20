package com.runtimeverification.rvmonitor.java.rvj.output.codedom;

import java.util.ArrayList;
import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeFormatters;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.ICodeFormatter;

public class CodeStmtCollection {
	private final List<CodeStmt> list;
	
	public boolean isSingle() {
		return this.list.size() == 1;
	}
	
	public CodeStmtCollection() {
		this.list = new ArrayList<CodeStmt>();
	}
	
	public CodeStmtCollection(CodeStmt ... stmts) {
		this.list = new ArrayList<CodeStmt>();
		for (CodeStmt stmt : stmts)
			this.add(stmt);
	}
	
	public CodeStmtCollection(CodeStmtCollection ... stmtscoll) {
		this.list = new ArrayList<CodeStmt>();
		for (CodeStmtCollection stmts : stmtscoll)
			this.add(stmts);
	}
	
	public static CodeStmtCollection empty() {
		return new CodeStmtCollection();
	}

	public void add(CodeStmt stmt) {
		if (stmt != null)
			this.list.add(stmt);
	}
	
	public void add(CodeStmtCollection stmts) {
		if (stmts != null) {
			for (CodeStmt stmt : stmts.list)
				this.add(stmt);
		}
	}
	
	/**
	 * Since adding comments is so frequently performed, a shortcut has been created.
	 * @param cmt comment
	 */
	public void comment(String cmt) {
		this.add(new CodeCommentStmt(cmt));
	}

	public static CodeStmtCollection fromLegacy(String str) {
		CodeStmtCollection coll = new CodeStmtCollection();
		coll.add(CodeStmt.fromLegacy(str));
		return coll;
	}
	
	/**
	 * This method flattens the structure if CodeBlockStmt is the only child.
	 * This method is for pretty-printing.
	 */
	public void simplify() {
		if (this.list.size() == 1 && (this.list.get(0) instanceof CodeBlockStmt)) {
			CodeBlockStmt block = (CodeBlockStmt)this.list.get(0);
			block.getBody().simplify();

			this.list.clear();
			this.list.addAll(block.getBody().list);
		}
	}
	
	public void getCode(ICodeFormatter fmt) {
		for (CodeStmt stmt : this.list) {
			stmt.getCode(fmt);
			if (!stmt.isBlock())
				fmt.endOfStatement();
		}
	}
	
	@Override
	public String toString() {
		ICodeFormatter fmt = CodeFormatters.getDefault();
		this.getCode(fmt);
		return fmt.getCode();
	}
}
