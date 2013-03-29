package rvmonitor.parser.ast.mopspec;

public class RVMonitorParameterPair {
	RVMParameters param1;
	RVMParameters param2;
	
	public RVMonitorParameterPair(RVMParameters param1, RVMParameters param2){
		this.param1 = param1;
		this.param2 = param2;
	}
	
	public RVMParameters getParam1(){
		return param1;
	}
	
	public RVMParameters getParam2(){
		return param2;
	}

	public boolean equals(RVMonitorParameterPair mopPair) {
		return this.getParam1().equals(mopPair.getParam1()) && this.getParam2().equals(mopPair.getParam2());
	}

	public String toString() {
		return "(" + param1.toString() + ", " + param2.toString() + ")";
	}
	
	public int hashCode() {
		return param1.hashCode() ^ param2.hashCode();
	}
}
