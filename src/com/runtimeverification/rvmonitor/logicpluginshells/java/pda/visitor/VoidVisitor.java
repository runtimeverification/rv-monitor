package com.runtimeverification.rvmonitor.logicpluginshells.java.pda.visitor;

import com.runtimeverification.rvmonitor.logicpluginshells.java.pda.ast.Event;
import com.runtimeverification.rvmonitor.logicpluginshells.java.pda.ast.PDA;
import com.runtimeverification.rvmonitor.logicpluginshells.java.pda.ast.StackSymbol;
import com.runtimeverification.rvmonitor.logicpluginshells.java.pda.ast.State;


public interface VoidVisitor<A> {

	public void visit(PDA n, A arg);

	public void visit(State n, A arg);

	public void visit(StackSymbol n, A arg);

	public void visit(Event n, A arg);

}