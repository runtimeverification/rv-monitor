package com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeAssignStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarDeclStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarRefExpr;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeVariable;

/**
 * This interface defines what a code visitor should implement.
 *
 * @author Choonghwan Lee <clee83@illinois.edu>
 */
public interface ICodeVisitor {
    public void visit(CodeStmtCollection stmts);

    public void declareVariable(CodeVarDeclStmt decl);

    public void assignVariable(CodeAssignStmt assign);

    public void referVariable(CodeVariable referred);

    public void openScope();

    public void closeScope();
}
