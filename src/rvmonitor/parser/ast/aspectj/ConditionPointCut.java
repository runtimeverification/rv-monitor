package rvmonitor.parser.ast.aspectj;

import rvmonitor.parser.ast.expr.*;
import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.PointcutVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;

public class ConditionPointCut extends PointCut {
	
	Expression expr;

	public ConditionPointCut(int line, int column, String type, Expression expr) {
		super(line, column, type);
		this.expr = expr;
	}
	
	public Expression getExpression() { return expr; }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(PointcutVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
