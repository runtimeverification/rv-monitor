package com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.*;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.typepattern.*;

public class RVMParameter extends Node implements Comparable<RVMParameter> {
	private final TypePattern type;
	private final String name;
	
	public RVMParameter(int line, int column, TypePattern type, String name){
		super(line, column);
		this.type = type;
		this.name = name;
	}
	
	public TypePattern getType() {return type;}
	public String getName() {return name;}
	
	public boolean equals(RVMParameter param){
		return type.equals(param.getType()) && name.equals(param.getName());
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
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
	public int compareTo(RVMParameter that) {
		return this.name.compareTo(that.name);
	}
}
