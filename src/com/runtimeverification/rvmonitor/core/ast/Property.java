package com.runtimeverification.rvmonitor.core.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A logic property in the specification with handlers.
 * @author A. Cody Schuffelen
 */
public class Property {
    
    private final String name;
    private final String syntax;
    private final List<PropertyHandler> handlers;
    
    /**
     * Construct the Property out of its component elements.
     * @param name The logic name of the property (e.g. ere, fsm).
     * @param syntax The code describing the property.
     * @param handlers Handlers used to respond to states in the property.
     */
    public Property(String name, String syntax, List<PropertyHandler> handlers) {
        this.name = name;
        this.syntax = syntax;
        this.handlers = Collections.unmodifiableList(new ArrayList<PropertyHandler>(handlers));
    }
    
    /**
     * The logic name of the property.
     * @return The name of the logic repository plugin used in the property.
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     */
    public String getSyntax() {
        return syntax;
    }
    
    public List<PropertyHandler> getHandlers() {
        return handlers;
    }
}
