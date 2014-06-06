package com.runtimeverification.rvmonitor.logicrepository.plugins.cfg.util;

class Accept extends LRAction {
    
    @Override
    public int hashCode() { 
        return 1;
    }
    
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Accept)) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        return "Accept";
    }
    
    public ActType type() { 
        return ActType.ACCEPT;
    }
}
