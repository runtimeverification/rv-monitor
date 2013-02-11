/*
 * Copyright (C) 2007 Julio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05/10/2006
 */
package rvmonitor.parser.astex.visitor;

import rvmonitor.parser.astex.MOPSpecFileExt;
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
public interface GenericVisitor<R, A> extends rvmonitor.parser.ast.visitor.GenericVisitor<R, A>{

	// All extended componenets
	
    //- JavaMOP components
    
    public R visit(MOPSpecFileExt f, A arg);
    
	public R visit(ReferenceSpec r, A arg);

    public R visit(JavaMOPSpecExt s, A arg);
    
    public R visit(EventDefinitionExt e, A arg);
    
    public R visit(PropertyAndHandlersExt p, A arg);
    
    public R visit(FormulaExt f, A arg);
    
    public R visit(ExtendedSpec extendedSpec, A arg);
    
    //- AspectJ components --------------------
    
    public R visit(EventPointCut p, A arg);
    
    public R visit(HandlerPointCut p, A arg);

}
