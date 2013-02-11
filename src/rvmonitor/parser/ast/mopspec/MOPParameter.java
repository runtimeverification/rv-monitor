package rvmonitor.parser.ast.mopspec;

import rvmonitor.parser.ast.*;
import rvmonitor.parser.ast.visitor.GenericVisitor;
import rvmonitor.parser.ast.visitor.VoidVisitor;
import rvmonitor.parser.ast.aspectj.*;

public class MOPParameter extends Node{
	TypePattern type;
	String name = "";
	
	public MOPParameter (int line, int column, TypePattern type, String name){
		super(line, column);
		this.type = type;
		this.name = name;
	}
	
	public TypePattern getType() {return type;}
	public String getName() {return name;}
	
	public boolean equals(MOPParameter param){
		return type.equals(param.getType()) && name.equals(param.getName());
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
