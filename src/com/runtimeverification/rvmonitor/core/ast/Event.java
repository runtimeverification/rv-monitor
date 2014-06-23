package com.runtimeverification.rvmonitor.core.ast;

public class Event {
    
    private final String name;
    private final String definition;
    private final String action;
    
    public Event(String name, String definition, String action) {
        this.name = name;
        this.definition = definition;
        this.action = action;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDefinition() {
        return definition;
    }
    
    public String getAction() {
        return action;
    }
    
}
