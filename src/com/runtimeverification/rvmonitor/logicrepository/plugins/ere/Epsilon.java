package com.runtimeverification.rvmonitor.logicrepository.plugins.ere;

/**
 * An ERE that matches only the empty string.
 */
public class Epsilon extends ERE {
    
    /**
     * Acquire an instance of the Epsilon ERE.
     * @return An instance of the Epsilon ERE.
     */
    static public Epsilon get() {
        return epsilon;
    }
    
    public EREType getEREType() { 
        return EREType.EPS;
    }
    
    public boolean equals(Object o) {
        return this == o;
    }
    
    public int compareTo(Object o) {
        if(!(o instanceof ERE)) {
            return -1;
        }
        ERE E = (ERE) o;
        return EREType.EPS.compareTo(E.getEREType());
    }
    
    public ERE copy() {
        return this;
    }
    
    public String toString() {
        return "epsilon";
    }
    
    public boolean containsEpsilon() {
        return true;
    }
    
    public ERE derive(Symbol s) {
        return empty;
    }
}
