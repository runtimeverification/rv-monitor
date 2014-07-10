package com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;

public class NotTypePattern extends TypePattern {

	TypePattern type;
	
	public NotTypePattern(int line, int column, TypePattern type){
		super(line, column, "!");
		this.type = type;
	}
	
	public TypePattern getType() { return type; }
	
	@Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
	
}
