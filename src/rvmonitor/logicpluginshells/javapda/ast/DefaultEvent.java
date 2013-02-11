package rvmonitor.logicpluginshells.javapda.ast;

import rvmonitor.logicpluginshells.javapda.visitor.DumpVisitor;
import rvmonitor.logicpluginshells.javapda.visitor.GenericVisitor;
import rvmonitor.logicpluginshells.javapda.visitor.VoidVisitor;

public class DefaultEvent extends Event {
	public DefaultEvent(){
		super();
		this.isDefault = true;
	}

    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public final String toString() {
        DumpVisitor visitor = new DumpVisitor();
        String formula = accept(visitor, null);
        return formula;
    }

	@Override
	public boolean equals(Object o){
		if(!(o instanceof DefaultEvent))
			return false;
		return this == o;
	}
}
