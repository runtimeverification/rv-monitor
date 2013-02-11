package rvmonitor.parser.astex.aspectj;

import java.util.List;

import rvmonitor.parser.ast.aspectj.PointCut;
import rvmonitor.parser.astex.mopspec.ReferenceSpec;
import rvmonitor.parser.astex.visitor.DumpVisitor;
import rvmonitor.parser.astex.visitor.GenericVisitor;
import rvmonitor.parser.astex.visitor.VoidVisitor;

public class EventPointCut extends PointCut {

	ReferenceSpec r;
	List<String> parameterNames;

	public EventPointCut(int line, int column, String type, String specName, String referenceElement, List<String> parameterNames) {
		super(line, column, type);
		this.parameterNames = parameterNames;
		this.r = new ReferenceSpec(line, column, specName, referenceElement, "event");
	}

	public ReferenceSpec getReferenceSpec() {
		return r;
	}

	public List<String> getParameters() {
		return parameterNames;
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
