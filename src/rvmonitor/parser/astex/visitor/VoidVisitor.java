/*
 * Copyright (C) 2008 Feng Chen.
 * 
 * This file is part of JavaMOP parser.
 *
 * JavaMOP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JavaMOP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JavaMOP.  If not, see <http://www.gnu.org/licenses/>.
 */

package rvmonitor.parser.astex.visitor;

import rvmonitor.parser.astex.aspectj.EventPointCut;
import rvmonitor.parser.astex.aspectj.HandlerPointCut;
import rvmonitor.parser.astex.mopspec.EventDefinitionExt;
import rvmonitor.parser.astex.mopspec.ExtendedSpec;
import rvmonitor.parser.astex.mopspec.FormulaExt;
import rvmonitor.parser.astex.mopspec.JavaMOPSpecExt;
import rvmonitor.parser.astex.mopspec.PropertyAndHandlersExt;
import rvmonitor.parser.astex.mopspec.ReferenceSpec;

/**
 * @author Julio Vilmar Gesser
 */
public interface VoidVisitor<A> extends rvmonitor.parser.ast.visitor.VoidVisitor<A> {

	// All extended componenets
	
    //- JavaMOP components
    
	public void visit(ReferenceSpec r, A arg);

    public void visit(JavaMOPSpecExt s, A arg);
    
    public void visit(EventDefinitionExt e, A arg);
    
    public void visit(PropertyAndHandlersExt p, A arg);
    
    public void visit(FormulaExt f, A arg);
    
    public void visit(ExtendedSpec extendedSpec, A arg);
    
    //- AspectJ components --------------------
    
    public void visit(EventPointCut p, A arg);
    
    public void visit(HandlerPointCut p, A arg);

}
