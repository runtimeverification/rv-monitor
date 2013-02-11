package rvmonitor.logicpluginshells.javaptcaret.ast;

import rvmonitor.logicpluginshells.javaptcaret.visitor.GenericVisitor;
import rvmonitor.logicpluginshells.javaptcaret.visitor.VoidVisitor;

public abstract class PseudoCode_Expr extends PseudoCode_Node{
	
	public PseudoCode_Expr(){
		
	}
	
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

    @Override
    abstract public String toString();

}
