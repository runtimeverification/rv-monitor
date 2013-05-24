package rvmonitor.logicpluginshells.java.pda.visitor;

import rvmonitor.logicpluginshells.java.pda.ast.Event;
import rvmonitor.logicpluginshells.java.pda.ast.PDA;
import rvmonitor.logicpluginshells.java.pda.ast.StackSymbol;
import rvmonitor.logicpluginshells.java.pda.ast.State;


public interface VoidVisitor<A> {

	public void visit(PDA n, A arg);

	public void visit(State n, A arg);

	public void visit(StackSymbol n, A arg);

	public void visit(Event n, A arg);

}