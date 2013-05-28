/*
 * Copyright (C) 2007 Julio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 03/11/2006
 */
package com.runtimeverification.rvmonitor.java.rvj.parser.ast.body;


import java.util.List;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.AnnotationExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.type.Type;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.GenericVisitor;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class Parameter extends Node {

    private final int modifiers;

    private final List<AnnotationExpr> annotations;

    private final Type type;

    private final boolean isVarArgs;

    private final VariableDeclaratorId id;

    public Parameter(int line, int column, int modifiers, List<AnnotationExpr> annotations, Type type, boolean isVarArgs, VariableDeclaratorId id) {
        super(line, column);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.isVarArgs = isVarArgs;
        this.id = id;
    }

    public int getModifiers() {
        return modifiers;
    }

    public List<AnnotationExpr> getAnnotations() {
        return annotations;
    }

    public Type getType() {
        return type;
    }

    public boolean isVarArgs() {
        return isVarArgs;
    }

    public VariableDeclaratorId getId() {
        return id;
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
