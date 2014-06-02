package com.runtimeverification.rvmonitor.logicrepository.plugins.ere;

/**
 * 
 */
public class Symbol extends ERE {

  /**
   * Acquire a Symbol ERE for the given string.
   * Each string symbol name is associated with a specific instance of the Symbol class.
   * These instances can be reference-compared for equality or inequality.
   * @return An instance of Symbol unique to its name.
   */
  static public Symbol get(String name){
    Symbol self = ERE.stringToRef.get(name); 
	 if(self != null) return self;
	 Symbol ret = new Symbol();
	 stringToRef.put(name, ret);
	 refToString.put(ret, name);
	 return ret;
  }

  public EREType getEREType(){ 
	  return EREType.S;
  }

  public boolean equals(Object o){
	 return this == o;
  }
 
  public int compareTo(Object o){
	 if(!(o instanceof ERE)) return -1;
	 ERE E = (ERE) o;
    if(E.getEREType() == EREType.S) {
      if(this == E) return 0;
		return ERE.refToString.get(this).compareTo(ERE.refToString.get((Symbol)o));
	 }
	 return EREType.S.compareTo(E.getEREType());
  }

  public ERE copy(){
    return this;
  }

  public String toString(){
    return ERE.refToString.get(this);
  }

  public boolean containsEpsilon(){
	 return false;
  }

  public ERE derive(Symbol s){
    if(this == s) return epsilon;
	 return empty;
  }
}
