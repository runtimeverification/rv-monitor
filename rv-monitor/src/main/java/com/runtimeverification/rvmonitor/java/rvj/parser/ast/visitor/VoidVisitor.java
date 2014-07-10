/*
 * Copyright (C) 2008 Feng Chen.
 * 
 * This file is part of RV Monitor parser.
 *
 * RV Monitor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RV Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RV Monitor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.CompilationUnit;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.ImportDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.PackageDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.TypeParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.AnnotationDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.AnnotationMemberDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.ClassOrInterfaceDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.ConstructorDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.EmptyMemberDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.EmptyTypeDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.EnumConstantDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.EnumDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.FieldDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.InitializerDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.MethodDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.Parameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.VariableDeclarator;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.body.VariableDeclaratorId;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.ArrayAccessExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.ArrayCreationExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.ArrayInitializerExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.AssignExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.BinaryExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.BooleanLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.CastExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.CharLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.ClassExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.ConditionalExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.DoubleLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.EnclosedExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.FieldAccessExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.InstanceOfExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.IntegerLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.IntegerLiteralMinValueExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.LongLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.LongLiteralMinValueExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.MarkerAnnotationExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.MemberValuePair;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.MethodCallExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.NameExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.NormalAnnotationExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.NullLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.ObjectCreationExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.QualifiedNameExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.SingleMemberAnnotationExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.StringLiteralExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.SuperExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.SuperMemberAccessExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.ThisExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.UnaryExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.VariableDeclarationExpr;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.AssertStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BlockStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BreakStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.CatchClause;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.ContinueStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.DoStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.EmptyStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.ExpressionStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.ForStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.ForeachStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.IfStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.LabeledStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.ReturnStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.SwitchEntryStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.SwitchStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.SynchronizedStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.ThrowStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.TryStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.TypeDeclarationStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.WhileStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.type.ClassOrInterfaceType;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.type.PrimitiveType;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.type.ReferenceType;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.type.VoidType;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.type.WildcardType;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.*;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.*;;

/**
 * @author Julio Vilmar Gesser
 */
public interface VoidVisitor<A> {

    public void visit(Node n, A arg);
    
    //- RV Monitor components
    
    public void visit(RVMSpecFile f, A arg);
    
    public void visit(RVMonitorSpec s, A arg);
    
    public void visit(RVMParameter p, A arg);
    
    public void visit(EventDefinition e, A arg);
    
    public void visit(PropertyAndHandlers p, A arg);
    
    public void visit(Formula f, A arg);
    
    //- AspectJ components --------------------
    
    public void visit(WildcardParameter w, A arg);
    
    public void visit(ArgsPointCut p, A arg);
    
    public void visit(CombinedPointCut p, A arg);
    
    public void visit(NotPointCut p, A arg);
    
    public void visit(ConditionPointCut p, A arg);
    
    public void visit(CountCondPointCut p, A arg);
    
    public void visit(FieldPointCut p, A arg);
    
    public void visit(MethodPointCut p, A arg);
    
    public void visit(TargetPointCut p, A arg);
    
    public void visit(ThisPointCut p, A arg);

    public void visit(CFlowPointCut p, A arg);

    public void visit(IFPointCut p, A arg);
    
    public void visit(IDPointCut p, A arg);

    public void visit(WithinPointCut p, A arg);

    public void visit(ThreadPointCut p, A arg);
    
    public void visit(ThreadNamePointCut p, A arg);
    
    public void visit(ThreadBlockedPointCut p, A arg);

    public void visit(EndProgramPointCut p, A arg);

    public void visit(EndThreadPointCut p, A arg);
    
    public void visit(EndObjectPointCut p, A arg);

    public void visit(StartThreadPointCut p, A arg);

    public void visit(FieldPattern p, A arg);
    
    public void visit(MethodPattern p, A arg);
    
    public void visit(CombinedTypePattern p, A arg);
    
    public void visit(NotTypePattern p, A arg);
    
    public void visit(BaseTypePattern p, A arg);

    //- Compilation Unit ----------------------------------

    public void visit(CompilationUnit n, A arg);

    public void visit(PackageDeclaration n, A arg);

    public void visit(ImportDeclaration n, A arg);

    public void visit(TypeParameter n, A arg);

    //- Body ----------------------------------------------

    public void visit(ClassOrInterfaceDeclaration n, A arg);

    public void visit(EnumDeclaration n, A arg);

    public void visit(EmptyTypeDeclaration n, A arg);

    public void visit(EnumConstantDeclaration n, A arg);

    public void visit(AnnotationDeclaration n, A arg);

    public void visit(AnnotationMemberDeclaration n, A arg);

    public void visit(FieldDeclaration n, A arg);

    public void visit(VariableDeclarator n, A arg);

    public void visit(VariableDeclaratorId n, A arg);

    public void visit(ConstructorDeclaration n, A arg);

    public void visit(MethodDeclaration n, A arg);

    public void visit(Parameter n, A arg);

    public void visit(EmptyMemberDeclaration n, A arg);

    public void visit(InitializerDeclaration n, A arg);

    //- Type ----------------------------------------------

    public void visit(ClassOrInterfaceType n, A arg);

    public void visit(PrimitiveType n, A arg);

    public void visit(ReferenceType n, A arg);

    public void visit(VoidType n, A arg);

    public void visit(WildcardType n, A arg);

    //- Expression ----------------------------------------

    public void visit(ArrayAccessExpr n, A arg);

    public void visit(ArrayCreationExpr n, A arg);

    public void visit(ArrayInitializerExpr n, A arg);

    public void visit(AssignExpr n, A arg);

    public void visit(BinaryExpr n, A arg);

    public void visit(CastExpr n, A arg);

    public void visit(ClassExpr n, A arg);

    public void visit(ConditionalExpr n, A arg);

    public void visit(EnclosedExpr n, A arg);

    public void visit(FieldAccessExpr n, A arg);

    public void visit(InstanceOfExpr n, A arg);

    public void visit(StringLiteralExpr n, A arg);

    public void visit(IntegerLiteralExpr n, A arg);

    public void visit(LongLiteralExpr n, A arg);

    public void visit(IntegerLiteralMinValueExpr n, A arg);

    public void visit(LongLiteralMinValueExpr n, A arg);

    public void visit(CharLiteralExpr n, A arg);

    public void visit(DoubleLiteralExpr n, A arg);

    public void visit(BooleanLiteralExpr n, A arg);

    public void visit(NullLiteralExpr n, A arg);

    public void visit(MethodCallExpr n, A arg);

    public void visit(NameExpr n, A arg);

    public void visit(ObjectCreationExpr n, A arg);

    public void visit(QualifiedNameExpr n, A arg);

    public void visit(SuperMemberAccessExpr n, A arg);

    public void visit(ThisExpr n, A arg);

    public void visit(SuperExpr n, A arg);

    public void visit(UnaryExpr n, A arg);
    
    public void visit(VariableDeclarationExpr n, A arg);

    public void visit(MarkerAnnotationExpr n, A arg);

    public void visit(SingleMemberAnnotationExpr n, A arg);

    public void visit(NormalAnnotationExpr n, A arg);

    public void visit(MemberValuePair n, A arg);

    //- Statements ----------------------------------------

    public void visit(ExplicitConstructorInvocationStmt n, A arg);

    public void visit(TypeDeclarationStmt n, A arg);

    public void visit(AssertStmt n, A arg);

    public void visit(BlockStmt n, A arg);

    public void visit(LabeledStmt n, A arg);

    public void visit(EmptyStmt n, A arg);

    public void visit(ExpressionStmt n, A arg);

    public void visit(SwitchStmt n, A arg);

    public void visit(SwitchEntryStmt n, A arg);

    public void visit(BreakStmt n, A arg);

    public void visit(ReturnStmt n, A arg);

    public void visit(IfStmt n, A arg);

    public void visit(WhileStmt n, A arg);

    public void visit(ContinueStmt n, A arg);

    public void visit(DoStmt n, A arg);

    public void visit(ForeachStmt n, A arg);

    public void visit(ForStmt n, A arg);

    public void visit(ThrowStmt n, A arg);

    public void visit(SynchronizedStmt n, A arg);

    public void visit(TryStmt n, A arg);

    public void visit(CatchClause n, A arg);

}
