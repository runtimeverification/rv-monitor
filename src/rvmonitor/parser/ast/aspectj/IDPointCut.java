package rvmonitor.parser.ast.aspectj;

import java.util.*;
import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.PointcutVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;

public class IDPointCut extends PointCut {
	
	List<TypePattern> args;
	String id;

	public IDPointCut(int line, int column, String id, List<TypePattern> args){
		super(line, column, "id");
		this.args = args;
		this.id = id;
	}
	
	public List<TypePattern> getArgs() { return args; }
	public String getId() { return id; }
	
    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(PointcutVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
