package com.runtimeverification.rvmonitor.logicrepository.plugins.ere;

//class representing a repeated symbol in an ERE
public class Repeat {
//  public String name;

//We won't even use derive here or even
//subclass ERE, we just immediately 
//return a new concatenation list of the child repeated
//num times
  public static ERE get(ERE child, int num){
    ERE ret = Concat.get(child, child);
    for(int i = 2; i < num; ++i){
      ret = Concat.get(child, ret); 
    } 
    return ret;
  }
}
