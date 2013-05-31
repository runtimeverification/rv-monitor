package com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj;

import java.util.*;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.PointcutVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;

public class CombinedPointCut extends PointCut {
	
	List<PointCut> pointcuts;

	public CombinedPointCut(int line, int column, String type, List<PointCut> pointcuts){
		super(line, column, type);
		
		this.pointcuts = pointcuts;
	}
	
	public List<PointCut> getPointcuts() { return pointcuts; }
	
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
