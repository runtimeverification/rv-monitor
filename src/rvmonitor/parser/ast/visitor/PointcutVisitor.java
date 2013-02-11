package rvmonitor.parser.ast.visitor;

import rvmonitor.parser.ast.aspectj.ArgsPointCut;
import rvmonitor.parser.ast.aspectj.CFlowPointCut;
import rvmonitor.parser.ast.aspectj.CombinedPointCut;
import rvmonitor.parser.ast.aspectj.ConditionPointCut;
import rvmonitor.parser.ast.aspectj.EndObjectPointCut;
import rvmonitor.parser.ast.aspectj.EndProgramPointCut;
import rvmonitor.parser.ast.aspectj.EndThreadPointCut;
import rvmonitor.parser.ast.aspectj.FieldPointCut;
import rvmonitor.parser.ast.aspectj.IDPointCut;
import rvmonitor.parser.ast.aspectj.IFPointCut;
import rvmonitor.parser.ast.aspectj.MethodPointCut;
import rvmonitor.parser.ast.aspectj.NotPointCut;
import rvmonitor.parser.ast.aspectj.PointCut;
import rvmonitor.parser.ast.aspectj.StartThreadPointCut;
import rvmonitor.parser.ast.aspectj.TargetPointCut;
import rvmonitor.parser.ast.aspectj.ThisPointCut;
import rvmonitor.parser.ast.aspectj.ThreadBlockedPointCut;
import rvmonitor.parser.ast.aspectj.ThreadNamePointCut;
import rvmonitor.parser.ast.aspectj.ThreadPointCut;
import rvmonitor.parser.ast.aspectj.WithinPointCut;

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
