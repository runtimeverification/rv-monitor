package com.runtimeverification.rvmonitor.java.rvj.parser.astex.visitor;

import java.util.ArrayList;
import java.util.List;

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
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.aspectj.EventPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.astex.aspectj.HandlerPointCut;

public class CollectHandlerPointCutVisitor implements PointcutVisitor<List<HandlerPointCut>, Object> {

	@Override
	public List<HandlerPointCut> visit(PointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(ArgsPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(CombinedPointCut p, Object arg) {
		List<HandlerPointCut> ret = new ArrayList<HandlerPointCut>();
		
		for(PointCut p2 : p.getPointcuts()){
			List<HandlerPointCut> temp = p2.accept(this, arg);
			if(temp != null){
				ret.addAll(temp);
			}
		}
		return ret;
	}

	@Override
	public List<HandlerPointCut> visit(NotPointCut p, Object arg) {
		return p.getPointCut().accept(this, arg);
	}

	@Override
	public List<HandlerPointCut> visit(ConditionPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(FieldPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(MethodPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(TargetPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(ThisPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(CFlowPointCut p, Object arg) {
		return p.getPointCut().accept(this, arg);
	}

	@Override
	public List<HandlerPointCut> visit(IFPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(IDPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(WithinPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(ThreadPointCut p, Object arg) {
		return null;
	}
	
	@Override
	public List<HandlerPointCut> visit(ThreadBlockedPointCut p, Object arg) {
		return null;
	}
	
	@Override
	public List<HandlerPointCut> visit(ThreadNamePointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(EndProgramPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(EndThreadPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(EndObjectPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(StartThreadPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(EventPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<HandlerPointCut> visit(HandlerPointCut p, Object arg) {
		List<HandlerPointCut> ret = new ArrayList<HandlerPointCut>();
		ret.add(p);
		return ret;
	}
}
