package rvmonitor.parser.ast.aspectj;

import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.PointcutVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;

public class StartThreadPointCut extends PointCut {
	
	public StartThreadPointCut(int line, int column){
		super(line, column, "startThread");
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
    public <R, A> R accept(PointcutVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
