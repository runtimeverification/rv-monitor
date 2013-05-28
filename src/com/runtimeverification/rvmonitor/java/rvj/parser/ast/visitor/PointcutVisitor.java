package com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ArgsPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.CFlowPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.CombinedPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ConditionPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.EndObjectPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.EndProgramPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.EndThreadPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.FieldPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.IDPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.IFPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.MethodPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.NotPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.PointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.StartThreadPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.TargetPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThisPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThreadBlockedPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThreadNamePointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThreadPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.WithinPointCut;

public interface PointcutVisitor<R, A> {

	public R visit(PointCut p, A arg);
	
	public R visit(ArgsPointCut p, A arg);

	public R visit(CombinedPointCut p, A arg);

	public R visit(NotPointCut p, A arg);

	public R visit(ConditionPointCut p, A arg);

	public R visit(FieldPointCut p, A arg);

	public R visit(MethodPointCut p, A arg);

	public R visit(TargetPointCut p, A arg);

	public R visit(ThisPointCut p, A arg);

	public R visit(CFlowPointCut p, A arg);

	public R visit(IFPointCut p, A arg);

	public R visit(IDPointCut p, A arg);

	public R visit(WithinPointCut p, A arg);

	public R visit(ThreadPointCut p, A arg);
	
	public R visit(ThreadNamePointCut p, A arg);

	public R visit(ThreadBlockedPointCut p, A arg);
	
	public R visit(EndProgramPointCut p, A arg);

	public R visit(EndThreadPointCut p, A arg);
	
	public R visit(EndObjectPointCut p, A arg);

	public R visit(StartThreadPointCut p, A arg);
	
}
