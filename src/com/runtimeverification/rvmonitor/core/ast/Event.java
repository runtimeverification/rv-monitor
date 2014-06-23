package com.runtimeverification.rvmonitor.core.ast;

/**
 * An event monitored in the output program.
 */
public class Event {
    
    private final String name;
    private final String definition;
    private final String action;
    
    /**
     * Construct an Event out of its component parts.
     * @param name The name of the event to monitor.
     * @param definition The descrption of what the event is on, e.g. its parameters.
     * @param action The action to take on encountering the event.
     */
    public Event(String name, String definition, String action) {
        this.name = name;
        this.definition = definition;
        this.action = action;
    }
    
    /**
     * The name of the event.
     * @return The event's name, which is also used in the logic states.
     */
    public String getName() {
        return name;
    }
    
    /**
     * The event's parameters.
     * @return The parameters of the event, described in a language-specific way.
     */
    public String getDefinition() {
        return definition;
    }
    
    /**
     * Language-specific code to take on encountering the event.
     * @return Code in the target language to run on encountering the event.
     */
    public String getAction() {
        return action;
    }
    
}
