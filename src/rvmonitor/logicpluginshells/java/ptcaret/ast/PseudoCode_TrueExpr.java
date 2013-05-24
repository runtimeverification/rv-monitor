package rvmonitor.logicpluginshells.java.ptcaret.ast;

import rvmonitor.logicpluginshells.java.ptcaret.visitor.DumpVisitor;
import rvmonitor.logicpluginshells.java.ptcaret.visitor.GenericVisitor;
import rvmonitor.logicpluginshells.java.ptcaret.visitor.VoidVisitor;

public class PseudoCode_TrueExpr extends PseudoCode_Expr {
	public PseudoCode_TrueExpr() {

	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	@Override
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
