package com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.PointcutVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;

public abstract class PointCut extends Node {

	String type;	
	
	public PointCut(int line, int column, String type){
		super(line, column);
		this.type = type;
	}
	
	public String getType() { return type; }

	@Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
    
    public <R, A> R accept(PointcutVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

}
