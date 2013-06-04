package com.runtimeverification.rvmonitor.logicpluginshells.java.cfg.parser;

public class NonTerminal extends Symbol{
   NonTerminal(String s){super(s);}
   NonTerminal(Symbol s){super(s.name);}
   public String toString(){return "nt("+name+")";}
}
