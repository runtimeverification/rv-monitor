package rvmonitor.logicpluginshells.javaptcaret.visitor;

import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_Assignment;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_Assignments;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_BinExpr;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_EventExpr;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_Expr;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_FalseExpr;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_Node;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_NotExpr;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_Output;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_TrueExpr;
import rvmonitor.logicpluginshells.javaptcaret.ast.PseudoCode_VarExpr;

public interface VoidVisitor<A> {

	public void visit(PseudoCode_Expr n, A arg);

	public void visit(PseudoCode_TrueExpr n, A arg);

	public void visit(PseudoCode_FalseExpr n, A arg);
	
	public void visit(PseudoCode_VarExpr n, A arg);
	
	public void visit(PseudoCode_EventExpr n, A arg);
	
	public void visit(PseudoCode_BinExpr n, A arg);
	
	public void visit(PseudoCode_NotExpr n, A arg);

	public void visit(PseudoCode_Assignments n, A arg);
	
	public void visit(PseudoCode_Assignment n, A arg);
	
	public void visit(PseudoCode_Output n, A arg);
	
	public void visit(PseudoCode n, A arg);
	
	public void visit(PseudoCode_Node n, A arg);
}