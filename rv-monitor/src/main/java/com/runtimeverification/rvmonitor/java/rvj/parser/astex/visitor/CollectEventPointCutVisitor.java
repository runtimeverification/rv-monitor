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

public class CollectEventPointCutVisitor implements PointcutVisitor<List<EventPointCut>, Object> {

	@Override
	public List<EventPointCut> visit(PointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(ArgsPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(CombinedPointCut p, Object arg) {
		List<EventPointCut> ret = new ArrayList<EventPointCut>();
		
		for(PointCut p2 : p.getPointcuts()){
			List<EventPointCut> temp = p2.accept(this, arg);
			if(temp != null){
				ret.addAll(temp);
			}
		}
		return ret;
	}

	@Override
	public List<EventPointCut> visit(NotPointCut p, Object arg) {
		return p.getPointCut().accept(this, arg);
	}

	@Override
	public List<EventPointCut> visit(ConditionPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(FieldPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(MethodPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(TargetPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(ThisPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(CFlowPointCut p, Object arg) {
		return p.getPointCut().accept(this, arg);
	}

	@Override
	public List<EventPointCut> visit(IFPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(IDPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(WithinPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(ThreadPointCut p, Object arg) {
		return null;
	}
	
	@Override
	public List<EventPointCut> visit(ThreadBlockedPointCut p, Object arg) {
		return null;
	}
	
	@Override
	public List<EventPointCut> visit(ThreadNamePointCut p, Object arg) {
		return null;
	}
	
	@Override
	public List<EventPointCut> visit(EndProgramPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(EndThreadPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(EndObjectPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(StartThreadPointCut p, Object arg) {
		return null;
	}

	@Override
	public List<EventPointCut> visit(EventPointCut p, Object arg) {
		List<EventPointCut> ret = new ArrayList<EventPointCut>();
		ret.add(p);
		return ret;
	}

	@Override
	public List<EventPointCut> visit(HandlerPointCut p, Object arg) {
		return null;
	}
}
