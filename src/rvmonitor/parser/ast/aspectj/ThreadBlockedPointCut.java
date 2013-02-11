package rvmonitor.parser.ast.aspectj;

import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.PointcutVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;

/**
 * 
 * ThreadBlocked point cut used to check whether a thread is blocked
 * 
 * */
public class ThreadBlockedPointCut extends PointCut {
	
	String id;

	public ThreadBlockedPointCut(int line, int column, String id){
		super(line, column, "threadBlocked");
		this.id = id;
	}
	
	public String getId() { return id; }

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
