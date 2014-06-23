package com.runtimeverification.rvmonitor.c.rvc;

import com.runtimeverification.rvmonitor.core.ast.Event;
import com.runtimeverification.rvmonitor.core.ast.Property;
import com.runtimeverification.rvmonitor.core.ast.PropertyHandler;
import com.runtimeverification.rvmonitor.core.ast.Specification;

import java.util.Map;
import java.util.HashMap;

public class CSpecification {
    
    private final Specification wrapped;
    
    public CSpecification(Specification wrapped) {
        this.wrapped = wrapped;
        for(Event event : wrapped.getEvents()) {
            String params = event.getDefinition().trim();
            if(params.charAt(0) != '(' || params.charAt(params.length()-1) != ')') {
                throw new RuntimeException("C event parameters must begin and end with ( and )");
            }
        }
    }
    
    public String getIncludes() {
        return wrapped.getPreDeclarations();
    }
    
    public String getSpecName() {
        return wrapped.getName();
    }
    
    public HashMap<String, String> getEvents() {
        HashMap<String, String> events = new HashMap<String, String>();
        for(Event event : wrapped.getEvents()) {
            events.put(event.getName(), event.getAction());
        }
        return events;
    }
    
    public HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        for(Event event : wrapped.getEvents()) {
            parameters.put(event.getName(), event.getDefinition());
        }
        return parameters;
    }
    
    public HashMap<String, String> getPParameters() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        for(Event event : wrapped.getEvents()) {
            String params = event.getDefinition().trim();
            // Use a comma if there is at least one parameter.
            String separator = params.matches(".*[a-zA-Z]+.*") ? ", " : "";
            params = params.substring(0, params.length() - 1) + separator + "void* key)";
            parameters.put(event.getName(), params);
        }
        return parameters;
    }
    
    public HashMap<String, String> getHandlers() {
        HashMap<String, String> handlers = new HashMap<String, String>();
        for(PropertyHandler handler : wrapped.getProperties().get(0).getHandlers()) {
            handlers.put(handler.getState(), handler.getAction());
        }
        return handlers;
    }
    
    public String getDeclarations() {
        return wrapped.getLanguageDeclarations();
    }
    
    public String getFormalism() {
        return wrapped.getProperties().get(0).getName();
    }
    
    public String getFormula() {
        return wrapped.getProperties().get(0).getSyntax();
    }
}
