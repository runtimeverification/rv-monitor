package com.runtimeverification.rvmonitor.core.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Specification {
    
    private final String preDeclarations;
    private final List<String> languageModifiers;
    private final String name;
    private final String languageParameters;
    private final String languageDeclarations;
    private final List<Event> events;
    private final List<Property> properties;
    
    public Specification(String preDeclarations, List<String> languageModifiers, String name, 
            String languageParameters, String languageDeclarations, List<Event> events, 
            List<Property> properties) {
        this.preDeclarations = preDeclarations;
        this.languageModifiers = 
            Collections.unmodifiableList(new ArrayList<String>(languageModifiers));
        this.name = name;
        this.languageParameters = languageParameters;
        this.languageDeclarations = languageDeclarations;
        this.events = Collections.unmodifiableList(new ArrayList<Event>(events));
        this.properties = Collections.unmodifiableList(new ArrayList<Property>(properties));
    }
    
    public String getPreDeclarations() {
        return preDeclarations;
    }
    
    public List<String> getLanguageModifiers() {
        return languageModifiers;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLanguageParameters() {
        return languageParameters;
    }
    
    public String getLanguageDeclarations() {
        return languageDeclarations;
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public List<Property> getProperties() {
        return properties;
    }
    
}
