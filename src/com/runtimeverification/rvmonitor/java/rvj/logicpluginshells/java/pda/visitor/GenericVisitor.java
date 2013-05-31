package com.runtimeverification.rvmonitor.java.rvj.logicpluginshells.java.pda.visitor;

import com.runtimeverification.rvmonitor.java.rvj.logicpluginshells.java.pda.ast.Event;
import com.runtimeverification.rvmonitor.java.rvj.logicpluginshells.java.pda.ast.PDA;
import com.runtimeverification.rvmonitor.java.rvj.logicpluginshells.java.pda.ast.StackSymbol;
import com.runtimeverification.rvmonitor.java.rvj.logicpluginshells.java.pda.ast.State;


public interface GenericVisitor<R, A> {

	public R visit(PDA n, A arg);

	public R visit(State n, A arg);

	public R visit(StackSymbol n, A arg);

	public R visit(Event n, A arg);

}