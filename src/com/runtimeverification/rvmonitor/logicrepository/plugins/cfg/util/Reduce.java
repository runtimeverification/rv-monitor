package com.runtimeverification.rvmonitor.logicrepository.plugins.cfg.util;

class Reduce extends LRAction {
    private int nt;
    private int size;
    
    public Reduce(int oldnt, int oldsize) {
        nt = oldnt; 
        size = oldsize;
    }
    
    @Override
    public int hashCode() { 
        return nt+size;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Reduce)) {
            return false;
        }
        return nt == (((Reduce)o).nt) && size == (((Reduce)o).size);
    }
    
    @Override
    public String toString() { 
        return "Reduce "+nt +" "+size;
    }
    
    public ActType type() {
        return ActType.REDUCE;
    }
    
    public int getNt() {
        return nt;
    }
    
    public int getSize() {
        return size;
    }
}
