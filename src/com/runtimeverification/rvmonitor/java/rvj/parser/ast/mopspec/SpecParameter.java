package com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.*;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.*;

public class SpecParameter extends Node{
    private final TypePattern type;
    private final String name;
    
    public SpecParameter (int line, int column, TypePattern type, String name){
        super(line, column);
        this.type = type;
        this.name = name;
    }
    
    public TypePattern getType() {return type;}
    public String getName() {return name;}
    
    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }
    
    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
