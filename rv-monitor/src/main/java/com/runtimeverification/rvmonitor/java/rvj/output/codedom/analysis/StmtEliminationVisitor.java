package com.runtimeverification.rvmonitor.java.rvj.output.codedom.analysis;

import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeAssignStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeStmtCollection;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.CodeVarDeclStmt;
import com.runtimeverification.rvmonitor.java.rvj.output.codedom.helper.CodeVariable;

import java.util.HashSet;
import java.util.Set;

/**
 * This class implements a visitor that eliminates the given junks from all the
 * visited statements.
 *
 * @author Choonghwan Lee <clee83@illinois.edu>
 */
public class StmtEliminationVisitor implements ICodeVisitor {
    /**
     * Keeps all the remaining junk statements. Initially, junk statements are
     * given by the caller.
     */
    private final Set<CodeStmt> junks;
    /**
     * Keeps all the eliminated statements.
     */
    private final Set<CodeStmt> eliminated;

    public Set<CodeStmt> getRemainingJunks() {
        return this.junks;
    }

    public Set<CodeStmt> getEliminatedJunks() {
        return this.eliminated;
    }

    public StmtEliminationVisitor(Set<CodeStmt> junks) {
        this.junks = junks;
        this.eliminated = new HashSet<CodeStmt>();
    }

    @Override
    public void visit(CodeStmtCollection stmts) {
        Set<CodeStmt> removed = new HashSet<CodeStmt>();
        for (CodeStmt junk : this.junks) {
            if (stmts.remove(junk))
                removed.add(junk);
        }

        this.junks.removeAll(removed);
        this.eliminated.addAll(removed);
    }

    @Override
    public void declareVariable(CodeVarDeclStmt decl) {
    }

    @Override
    public void assignVariable(CodeAssignStmt assign) {
    }

    @Override
    public void referVariable(CodeVariable referred) {
    }

    @Override
    public void openScope() {
    }

    @Override
    public void closeScope() {
    }
}
