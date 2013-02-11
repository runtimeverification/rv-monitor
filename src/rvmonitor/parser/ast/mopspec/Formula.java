package rvmonitor.parser.ast.mopspec;

import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;

public class Formula extends Property {
	
	String formula;

	public Formula(int line, int column, String type, String formula) {
		super(line, column, type);
		this.formula = formula;
	}
	
	public String getFormula() { return formula; }
	
    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

}
