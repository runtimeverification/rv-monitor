package com.runtimeverification.rvmonitor.core.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Property {
    
    private final String name;
    private final String syntax;
    private final List<PropertyHandler> handlers;
    
    public Property(String name, String syntax, List<PropertyHandler> handlers) {
        this.name = name;
        this.syntax = syntax;
        this.handlers = Collections.unmodifiableList(new ArrayList<PropertyHandler>(handlers));
    }
    
    public String getName() {
        return name;
    }
    
    public String getSyntax() {
        return syntax;
    }
    
    public List<PropertyHandler> getHandlers() {
        return handlers;
    }
}
