package com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;

import java.util.Optional;

public class Name extends Node {

    private final String identifier;
    private final Name qualifier;

    public Name(int line, int column, Name qualifier, String identifier) {
        super(line, column);
        this.qualifier = qualifier;
        this.identifier = identifier;
    }

    public Optional<Name> getQualifier() {
        return Optional.ofNullable(qualifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFullNameString() {
        if (this.getQualifier().isPresent()) {
            String quantifier = this.getQualifier().get().getFullNameString();
            return quantifier + "." + this.identifier;
        } else {
            return this.identifier;
        }
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
