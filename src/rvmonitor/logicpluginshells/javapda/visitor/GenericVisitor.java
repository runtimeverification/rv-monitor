package rvmonitor.logicpluginshells.javapda.visitor;

import rvmonitor.logicpluginshells.javapda.ast.Event;
import rvmonitor.logicpluginshells.javapda.ast.PDA;
import rvmonitor.logicpluginshells.javapda.ast.StackSymbol;
import rvmonitor.logicpluginshells.javapda.ast.State;


public interface GenericVisitor<R, A> {

	public R visit(PDA n, A arg);

	public R visit(State n, A arg);

	public R visit(StackSymbol n, A arg);

	public R visit(Event n, A arg);

}