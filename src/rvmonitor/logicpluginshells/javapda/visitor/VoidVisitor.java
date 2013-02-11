package rvmonitor.logicpluginshells.javapda.visitor;

import rvmonitor.logicpluginshells.javapda.ast.Event;
import rvmonitor.logicpluginshells.javapda.ast.PDA;
import rvmonitor.logicpluginshells.javapda.ast.StackSymbol;
import rvmonitor.logicpluginshells.javapda.ast.State;


public interface VoidVisitor<A> {

	public void visit(PDA n, A arg);

	public void visit(State n, A arg);

	public void visit(StackSymbol n, A arg);

	public void visit(Event n, A arg);

}