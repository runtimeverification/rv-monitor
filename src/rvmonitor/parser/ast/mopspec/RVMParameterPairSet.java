package rvmonitor.parser.ast.mopspec;

import java.util.*;

public class RVMParameterPairSet implements Iterable<RVMonitorParameterPair>{

	public ArrayList<RVMonitorParameterPair> paramPairSet;

	public RVMParameterPairSet() {
		this.paramPairSet = new ArrayList<RVMonitorParameterPair>();
	}
	
	public RVMonitorParameterPair getParameterPair(RVMParameters param1, RVMParameters param2) {
		for (RVMonitorParameterPair paramPair : paramPairSet) {
			if(paramPair.getParam1().equals((Object)param1) && paramPair.getParam2().equals((Object)param2)){
				return paramPair;
			}
		}
		return null;
	}

	public void add(RVMParameters param1, RVMParameters param2) {
		if (getParameterPair(param1, param2) == null) {
			paramPairSet.add(new RVMonitorParameterPair(param1, param2));
		}
	}
	
	public void add(RVMonitorParameterPair paramPair) {
		if (getParameterPair(paramPair.getParam1(), paramPair.getParam2()) == null) {
			paramPairSet.add(paramPair);
		}
	}
	
	public void addAll(RVMParameterPairSet paramPairSet){
		for(RVMonitorParameterPair paramPair : paramPairSet){
			this.add(paramPair);
		}
	}
	
	public Iterator<RVMonitorParameterPair> iterator(){
		return paramPairSet.iterator();
	}
	
	public String toString(){
		return paramPairSet.toString();
	}
}
