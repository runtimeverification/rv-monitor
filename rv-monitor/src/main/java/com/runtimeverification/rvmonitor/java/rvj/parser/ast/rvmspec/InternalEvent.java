package com.runtimeverification.rvmonitor.java.rvj.parser.ast.rvmspec;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.typepattern.TypePattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;

import java.util.List;

public class InternalEvent extends Node {
    private final String name;
    private final RVMParameters parameters;
    private final String block;

    public InternalEvent(int beginLine, int beginColumn, String name,List<RVMParameter> parameters, String block) {
        super(beginLine, beginColumn);
        this.name = name;
        this.parameters = new RVMParameters(parameters);
        this.block = block;
    }

    public String getName() {
        return this.name;
    }

    public RVMParameters getParameters() {
        return this.parameters;
    }

    public String getBlock() {
        return this.block;
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
