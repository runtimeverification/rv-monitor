package rvmonitor.logicpluginshells.java.pda.visitor;

import rvmonitor.logicpluginshells.java.pda.ast.Event;
import rvmonitor.logicpluginshells.java.pda.ast.PDA;
import rvmonitor.logicpluginshells.java.pda.ast.StackSymbol;
import rvmonitor.logicpluginshells.java.pda.ast.State;


public interface GenericVisitor<R, A> {

	public R visit(PDA n, A arg);

	public R visit(State n, A arg);

	public R visit(StackSymbol n, A arg);

	public R visit(Event n, A arg);

}