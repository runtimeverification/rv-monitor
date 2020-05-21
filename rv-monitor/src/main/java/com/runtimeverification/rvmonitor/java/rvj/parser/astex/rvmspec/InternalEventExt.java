package com.runtimeverification.rvmonitor.java.rvj.parser.astex.rvmspec;

import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.rvmspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.rvmspec.RVMParameters;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.ExtNode;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.visitor.VoidVisitor;

public class InternalEventExt extends ExtNode {
    private final String name;
    private final RVMParameters parameters;
    private final String block;

    public InternalEventExt(int line, int column, String name, List<RVMParameter> parameters, String block) {
        super(line, column);
        this.name = name;
        this.parameters = new RVMParameters(parameters);
        this.block = block;
    }

    public InternalEventExt(int line, int column, InternalEventExt ie) {
        super(line, column);
        this.name = ie.getName();
        this.parameters = ie.getParameters();
        this.block = ie.getBlock();
    }

    public String getName() { return this.name; }

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