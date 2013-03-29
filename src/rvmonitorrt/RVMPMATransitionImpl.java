package rvmonitorrt;

public class RVMPMATransitionImpl {
    public int action;
    public RVMPMAStateImpl state;

    public RVMPMATransitionImpl(int action, RVMPMAStateImpl state){
    this.action = action;
    this.state = state;
  }
}

