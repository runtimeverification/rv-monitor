package rvmonitor.parser.astex.aspectj;

import rvmonitor.parser.ast.aspectj.PointCut;
import rvmonitor.parser.astex.mopspec.ReferenceSpec;
import rvmonitor.parser.astex.visitor.DumpVisitor;
import rvmonitor.parser.astex.visitor.GenericVisitor;
import rvmonitor.parser.astex.visitor.VoidVisitor;

public class HandlerPointCut extends PointCut{
	
	ReferenceSpec r;
	String state;
	
	public HandlerPointCut(int line, int column, String type, String specName, String referenceElement, String state) {
		super(line, column, type);
		this.r = new ReferenceSpec(line,column, specName, referenceElement, "property") ;
		this.state = state;
	}
	
	public void setReference(ReferenceSpec r){
		this.r = r;
	}
	
	public ReferenceSpec getReferenceSpec(){return r;}
	
	public String getState(){
		return state;
	}
	
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    public String toString() {
        DumpVisitor visitor = new DumpVisitor();
        accept(visitor, null);
        return visitor.getSource();
    }
}
