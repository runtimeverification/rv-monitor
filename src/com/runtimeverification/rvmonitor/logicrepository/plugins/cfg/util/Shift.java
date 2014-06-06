package com.runtimeverification.rvmonitor.logicrepository.plugins.cfg.util;

class Shift extends LRAction {
    private int target;
    
    public Shift(int t) { 
        target = t;
    }
    
    @Override
    public int hashCode() { 
        return target;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Shift)) return false;
        return target == (((Shift)o).target);
    }
    
    @Override
    public String toString() { 
        return "Shift "+target;
    }
    
    public ActType type() { 
        return ActType.SHIFT;
    }
    
    public int getTarget() {
        return target;
    }
}
