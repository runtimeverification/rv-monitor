package com.runtimeverification.rvmonitor.logicpluginshells.cfg.util;

public class NonTerminal extends Symbol{
   public NonTerminal(String s){super(s);}
   public NonTerminal(Symbol s){super(s.name);}
   public String toString(){return "nt("+name+")";}
}
