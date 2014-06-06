package com.runtimeverification.rvmonitor.logicrepository.plugins.cfg.util;

/**
 * An action that can be taken as part of a LR parser.
 */
public abstract class LRAction implements java.io.Serializable {
    
    public abstract ActType type();
}
