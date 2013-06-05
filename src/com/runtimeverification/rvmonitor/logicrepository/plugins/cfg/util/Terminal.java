package com.runtimeverification.rvmonitor.logicrepository.plugins.cfg.util;

public class Terminal extends Symbol{
   public Terminal(String s){super(s); }
   public Terminal(Symbol s){super(s.name); }
   public String toString(){return name;}
}
