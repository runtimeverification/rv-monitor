package com.runtimeverification.rvmonitor.core.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InternalEvent {
    private final String name;
    private final String parameters;
    private final String block;

    public InternalEvent(final String name, final String parameters, final String block) {
        this.name = name;
        this.parameters = parameters;
        this.block = block;
    }

    public String getName() {
        return this.name;
    }

    public String getParameters() { return this.parameters; }

    public String getBlock() { return this.block; }

}
