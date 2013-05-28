package com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.PointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.TypePattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.BlockStmt;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class EventDefinition extends Node {

	String id;

	String purePointCutStr;

	RVMParameters parameters;

	RVMParameters rvmParameters;

	BlockStmt block;
	RVMParameters usedParameter = null;


	// will be modified by RVMonitorSpec when creation events are not specified
	boolean startEvent = false;
	
	boolean blockingEvent = false;

	String condition;
	String threadVar;
	String threadNameVar;
	ArrayList<String> threadBlockedVars;
	TypePattern endObjectType;
	String endObjectId;
	boolean endProgram = false;
	boolean endThread = false;
	boolean startThread = false;
	boolean endObject = false;
	String countCond;

	// things that should be defined afterward
	int idnum; // will be defined in RVMonitorSpec
	boolean duplicated = false; // will be defined in RVMonitorSpec
	String uniqueId = null; // will be defined in RVMonitorSpec
	RVMParameters rvmParametersOnSpec; // will be defined in RVMonitorSpec

	public EventDefinition(int beginLine, int beginColumn, String id, List<RVMParameter> rvmParameters, BlockStmt block, boolean startEvent, boolean blockingEvent) {
		super(beginLine, beginColumn);
		this.id=id;
		this.parameters = new RVMParameters(rvmParameters);
		this.rvmParameters = new RVMParameters(rvmParameters);
		this.block = block;
		this.startEvent = startEvent;
		this.blockingEvent = blockingEvent;
	}

	private PointCut parsePointCut(String input) throws com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException {
		// create a token for exceptions
		com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.Token t = new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.Token();
		t.beginLine = super.getBeginLine();
		t.beginColumn = super.getBeginColumn();
		
		PointCut originalPointCut;
		PointCut resultPointCut;
		purePointCutStr = "";
		threadVar = "";
		condition = "";

		try {
			originalPointCut = com.runtimeverification.rvmonitor.java.rvj.parser.aspectj_parser.AspectJParser.parse(new ByteArrayInputStream(input.getBytes()));
		} catch (com.runtimeverification.rvmonitor.java.rvj.parser.aspectj_parser.ParseException e) {
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("The following error encountered when parsing the pointcut in the event definition: "
					+ e.getMessage());
		}
		
		// thread pointcut
		threadVar = originalPointCut.accept(new ThreadVarVisitor(), null);
		if (threadVar == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one thread() pointcut.");
		if (threadVar.length() != 0) {
			resultPointCut = originalPointCut.accept(new RemoveThreadVisitor(), new Integer(1));
		} else
			resultPointCut = originalPointCut;
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("thread() pointcut should appear at the root level in a conjuction form");
		
		// thread name pointcut
		threadNameVar = resultPointCut.accept(new ThreadNameVarVisitor(), null);
		if (threadNameVar == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one threadName() pointcut.");
		if (threadNameVar.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveThreadNameVisitor(), new Integer(1));
		} 
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("threadName() pointcut should appear at the root level in a conjuction form");
		
		// thread blocked pointcut
		String blockedThreads = resultPointCut.accept(new ThreadBlockedVarVisitor(), null);
		if (blockedThreads == null) {
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("threadBlocked() should have one parameter.");
		} 
		if (blockedThreads.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveThreadBlockedVisitor(), new Integer(1));
			threadBlockedVars = new ArrayList<String>();
			String vars[] = blockedThreads.split("@");
			for (String var : vars) {
				threadBlockedVars.add(var);
			}
		}
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("threadBlocked() pointcut should appear at the root level in a conjuction form");

		
		// condition pointcut
		condition = resultPointCut.accept(new ConditionVisitor(), null);
		if (condition == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one condition() pointcut.");
		if (condition.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveConditionVisitor(), new Integer(1));
		}
		// syntax de-sugar threadName pointcut into condition constraint
		if (threadNameVar != null && threadNameVar.length() != 0) {
			if (condition.length() != 0) {
				condition = ("Thread.currentThread().getName().equals(" + threadNameVar + ") && (" 
						+ condition + ")");
			} else {
				condition = "Thread.currentThread().getName().equals(" + threadNameVar + ")";
			}
		}
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("condition() pointcut should appear at the root level in a conjuction form");

		
		// Count condition pointcut
		countCond = resultPointCut.accept(new CountCondVisitor(), null);
		if (countCond == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one countCond() pointcut.");
		if (countCond.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveCountCondVisitor(), new Integer(1));
		}
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("countCond() pointcut should appear at the root level in a conjuction form");
		
		// endProgram pointcut
		String checkEndProgram = resultPointCut.accept(new EndProgramVisitor(), null);
		if (checkEndProgram == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one endProgram() pointcut.");
		if (checkEndProgram.length() != 0) {
			endProgram = true;
			resultPointCut = resultPointCut.accept(new RemoveEndProgramVisitor(), new Integer(1));
		} else {
			endProgram = false;
		}
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("endProgram() pointcut should appear at the root level in a conjuction form");

		// endThread pointcut
		String checkEndThread = resultPointCut.accept(new EndThreadVisitor(), null);
		if (checkEndThread == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one endThread() pointcut.");
		if (checkEndThread.length() != 0) {
			endThread = true;
			resultPointCut = resultPointCut.accept(new RemoveEndThreadVisitor(), new Integer(1));
		} else {
			endThread = false;
		}
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("endThread() pointcut should appear at the root level in a conjuction form");
		if (endProgram && endThread)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("endProgram() pointcut and endThread() pointcut cannot appear at the same time");

		// startThread pointcut
		String checkStartThread = resultPointCut.accept(new StartThreadVisitor(), null);
		if (checkStartThread == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one startThread() pointcut.");
		if (checkStartThread.length() != 0) {
			startThread = true;
			resultPointCut = resultPointCut.accept(new RemovePointCutVisitor("startThread"), new Integer(1));
		} else {
			startThread = false;
		}
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("startThread() pointcut should appear at the root level in a conjuction form");
		if (endThread && startThread)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("startThread() pointcut and endThread() pointcut cannot appear at the same time");
		
		// endObject pointcut
		endObjectId = resultPointCut.accept(new EndObjectVisitor(), null);
		endObjectType = resultPointCut.accept(new EndObjectTypeVisitor(), null);
		if (endObjectId == null || (endObjectId.length() != 0 && endObjectType == null))
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("There are more than one endObject() pointcut.");
		if (endObjectId.length() != 0) {
			endObject = true;
			resultPointCut = resultPointCut.accept(new RemoveEndObjectVisitor(), new Integer(1));
		} else {
			endObject = false;
		}
		if (resultPointCut == null)
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("endObject() pointcut should appear at the root level in a conjuction form");
		if (endObject && (endProgram || endThread))
			throw new com.runtimeverification.rvmonitor.java.rvj.parser.main_parser.ParseException("endProgram() pointcut, endThread(), and endObject() pointcut cannot appear at the same time");

		purePointCutStr = resultPointCut.toString();

		return resultPointCut;
	}

	public String getId() {
		return id;
	}

	public int getIdNum() {
		return idnum;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public RVMParameters getParameters() {
		return parameters;
	}

	RVMParameters parametersWithoutThreadVar = null;

	public RVMParameters getParametersWithoutThreadVar() {
		if (parametersWithoutThreadVar != null)
			return parametersWithoutThreadVar;

		parametersWithoutThreadVar = new RVMParameters();
		for (RVMParameter param : parameters) {
			if (getThreadVar() != null && getThreadVar().length() != 0 && param.getName().equals(getThreadVar()))
				continue;
			parametersWithoutThreadVar.add(param);
		}

		return parametersWithoutThreadVar;
	}

	public RVMParameters getRVMParameters() {
		return rvmParameters;
	}

	RVMParameters rvmParametersWithoutThreadVar = null;
	public RVMParameters getRVMParametersWithoutThreadVar() {
		if(rvmParametersWithoutThreadVar != null)
			return rvmParametersWithoutThreadVar;
		
		rvmParametersWithoutThreadVar = new RVMParameters();
		for(RVMParameter param : rvmParameters){
			if (getThreadVar() != null && getThreadVar().length() != 0 && param.getName().equals(getThreadVar()))
				continue;
			rvmParametersWithoutThreadVar.add(param);
		}
		return rvmParametersWithoutThreadVar;
	}
	
	public RVMParameters getRVMParametersOnSpec() {
		return rvmParametersOnSpec;
	}

	public BlockStmt getAction() {
		return block;
	}
	
	public RVMParameters getUsedParametersIn(RVMParameters specParam){
		//if cached, return it.
		if(usedParameter != null)
			return usedParameter;
		
		usedParameter = block.accept(new CollectRVMVarVisitor(), specParam);
		
		return usedParameter;
	}

	public String getThreadVar() {
		return threadVar;
	}
	
	public ArrayList<String> getThreadBlockedVar() {
		return threadBlockedVars;
	}

	public String getCondition() {
		return condition;
	}
	
	public String getCountCond() {
		return countCond;
	}

	public String getPurePointCutString() {
		return purePointCutStr;
	}

	public String getEndObjectVar() {
		if(this.endObject)
			return endObjectId;
		else
			return null;
	}
	
	public TypePattern getEndObjectType(){
		if(this.endObject)
			return endObjectType;
		else
			return null;
	}
	
	public boolean isStartEvent() {
		return this.startEvent;
	}
	
	public boolean isBlockingEvent() {
		return this.blockingEvent;
	}

	public boolean isEndProgram() {
		return this.endProgram;
	}

	public boolean isEndThread() {
		return this.endThread;
	}

	public boolean isEndObject() {
		return this.endObject;
	}

	public boolean isStartThread() {
		return this.startThread;
	}

	private Boolean cachedHas__SKIP = null;

	public boolean has__SKIP() {
		if (cachedHas__SKIP != null)
			return cachedHas__SKIP.booleanValue();

		if(this.getAction() != null){
			String eventAction = this.getAction().toString();
			if (eventAction.indexOf("__SKIP") != -1){
				cachedHas__SKIP = new Boolean(true);
				return true;
			}
		}
		cachedHas__SKIP = new Boolean(false);
		return false;
	}
	
	private Boolean cachedHas__LOC = null;

	public boolean has__LOC() {
		if (cachedHas__LOC != null)
			return cachedHas__LOC.booleanValue();

		if(this.getAction() != null){
			String eventAction = this.getAction().toString();
			if (eventAction.indexOf("__LOC") != -1
        || eventAction.indexOf("__DEFAULT_MESSAGE") != -1){
				cachedHas__LOC = new Boolean(true);
				return true;
			}
		}
		cachedHas__LOC = new Boolean(false);
		return false;
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
