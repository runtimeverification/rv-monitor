package rvmonitor.parser.ast.aspectj;

import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;

public class BaseTypePattern extends TypePattern {

	public BaseTypePattern(int line, int column, String op) {
		super(line, column, op);
	}
	
	@Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

}
