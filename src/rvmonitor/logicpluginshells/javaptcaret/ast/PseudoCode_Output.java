package rvmonitor.logicpluginshells.javaptcaret.ast;

import rvmonitor.logicpluginshells.javaptcaret.visitor.DumpVisitor;
import rvmonitor.logicpluginshells.javaptcaret.visitor.GenericVisitor;
import rvmonitor.logicpluginshells.javaptcaret.visitor.VoidVisitor;

public class PseudoCode_Output extends PseudoCode_Node{
	PseudoCode_Expr expr;
	
	public PseudoCode_Output(PseudoCode_Expr expr){
		this.expr = expr;
	}
	
	public PseudoCode_Expr getExpr(){
		return expr;
	}

	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public String toString() {
		DumpVisitor visitor = new DumpVisitor();
		String formula = accept(visitor, null);
		return formula;
	}
}
