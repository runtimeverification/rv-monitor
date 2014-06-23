package com.runtimeverification.rvmonitor.core.ast;

public class PropertyHandler {
    
    private final String state;
    private final String action;
    
    public PropertyHandler(String state, String action) {
        this.state = state;
        this.action = action;
    }
    
    public String getState() {
        return state;
    }
    
    public String getAction() {
        return action;
    }
}
