package rvmonitor.parser.astex.mopspec;

import rvmonitor.parser.ast.aspectj.PointCut;
import rvmonitor.parser.ast.aspectj.TypePattern;
import rvmonitor.parser.ast.mopspec.MOPParameter;
import rvmonitor.parser.ast.mopspec.MOPParameters;
import rvmonitor.parser.ast.stmt.BlockStmt;
import rvmonitor.parser.astex.ExtNode;
import rvmonitor.parser.astex.visitor.GenericVisitor;
import rvmonitor.parser.astex.visitor.VoidVisitor;

import java.io.ByteArrayInputStream;
import java.util.List;



public class EventDefinitionExt extends ExtNode {

	String id;

	String purePointCutStr;

	MOPParameters parameters;

	MOPParameters mopParameters;

	BlockStmt block;

	// will be modified by JavaMOPSpec when creation events are not specified
	boolean startEvent = false;

	String condition;
	String threadVar;
	TypePattern endObjectType;
	String endObjectId;
	boolean endProgram = false;
	boolean endThread = false;
	boolean startThread = false;
	boolean endObject = false;

	// things that should be defined afterward
	int idnum; // will be defined in JavaMOPSpec
	boolean duplicated = false; // will be defined in JavaMOPSpec
	String uniqueId = null; // will be defined in JavaMOPSpec
	MOPParameters mopParametersOnSpec; // will be defined in JavaMOPSpec

	MOPParameters parametersWithoutThreadVar = null;
	private Boolean cachedHas__SKIP = null;
	private Boolean cachedHas__LOC = null;

/*
EventDefinitionExt Event()
{
	String name;
	List parameters;
	BlockStmt block = null;
	int line = -1;
	int column = 0;
	boolean startEvent = false;
}
{
	{ return new EventDefinitionExt(line, column, name, parameters, block, startEvent); }
}
 */
	public EventDefinitionExt(int line, int column, String id, List<MOPParameter> parameters, BlockStmt block, boolean startEvent)
			throws rvmonitor.parser.main_parser.ParseException {
		super(line, column);
		this.id = id;
		this.parameters = new MOPParameters(parameters);
		this.block = block;
		this.startEvent = startEvent;
		this.mopParameters = new MOPParameters();
		this.mopParameters.addAll(this.parameters);
	}


	public EventDefinitionExt(int line, int column, EventDefinitionExt e) {
		super(line, column);
		this.id = e.getId();
		this.parameters = e.getParameters();
		this.block = e.getBlock();
		this.startEvent = e.getStartEvent();
		this.mopParameters = e.getMOPParameters();
		this.condition = e.getCondition();
		this.threadVar = e.getThreadVar();
		this.endObjectType = e.getEndObjectType();
		this.endObjectId = e.getEndObjectId();

		this.endProgram = e.isEndProgram();
		this.endThread = e.isEndThread();
		this.endObject = e.isEndObject();

		this.idnum = e.getIdNum(); // will be defined in JavaMOPSpec
		this.duplicated = e.isDuplicated(); // will be defined in JavaMOPSpec
		this.uniqueId = e.getUniqueId(); // will be defined in JavaMOPSpec
		this.mopParametersOnSpec = e.getMOPParametersOnSpec(); // will be
																// defined in
																// JavaMOPSpec

		this.parametersWithoutThreadVar = e.getParametersWithoutThreadVar();
		this.cachedHas__SKIP = e.isCashedHas__SKIP();
		// this.cachedHas__LOC = e.isCachedHas__LOC();

	}

	private PointCut parsePointCutAsRaw(String input) throws rvmonitor.parser.main_parser.ParseException {
		// create a token for exceptions
		rvmonitor.parser.main_parser.Token t = new rvmonitor.parser.main_parser.Token();
		t.beginLine = super.getBeginLine();
		t.beginColumn = super.getBeginColumn();

		PointCut originalPointCut;
		PointCut resultPointCut;
		purePointCutStr = "";
		threadVar = "";
		condition = "";

		try {
			originalPointCut = rvmonitor.parser.aspectj_parser.AspectJParser.parse(new ByteArrayInputStream(input.getBytes()));
		} catch (rvmonitor.parser.aspectj_parser.ParseException e) {
			throw new rvmonitor.parser.main_parser.ParseException("The following error encountered when parsing the pointcut in the event definition: " + e.getMessage());
		}

		resultPointCut = originalPointCut;

		if (resultPointCut == null)
			throw new rvmonitor.parser.main_parser.ParseException("endObject() pointcut should appear at the root level in a conjuction form");

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

	public MOPParameters getParameters() {
		return parameters;
	}

	public MOPParameters getParametersWithoutThreadVar() {
		if (parametersWithoutThreadVar != null)
			return parametersWithoutThreadVar;

		parametersWithoutThreadVar = new MOPParameters();
		for (MOPParameter param : parameters) {
			if (getThreadVar() != null && getThreadVar().length() != 0 && param.getName().equals(getThreadVar()))
				continue;
			parametersWithoutThreadVar.add(param);
		}

		return parametersWithoutThreadVar;
	}

	public MOPParameters getMOPParameters() {
		return mopParameters;
	}

	MOPParameters mopParametersWithoutThreadVar = null;

	public MOPParameters getMOPParametersWithoutThreadVar() {
		if (mopParametersWithoutThreadVar != null)
			return mopParametersWithoutThreadVar;

		mopParametersWithoutThreadVar = new MOPParameters();
		for (MOPParameter param : mopParameters) {
			if (getThreadVar() != null && getThreadVar().length() != 0 && param.getName().equals(getThreadVar()))
				continue;
			mopParametersWithoutThreadVar.add(param);
		}
		return mopParametersWithoutThreadVar;
	}

	public MOPParameters getMOPParametersOnSpec() {
		return mopParametersOnSpec;
	}

	public BlockStmt getAction() {
		return block;
	}

	public String getThreadVar() {
		return threadVar;
	}

	public String getCondition() {
		return condition;
	}

	public String getPurePointCutString() {
		return purePointCutStr;
	}

	public String getEndObjectVar() {
		if (this.endObject)
			return endObjectId;
		else
			return null;
	}

	public TypePattern getEndObjectType() {
		if (this.endObject)
			return endObjectType;
		else
			return null;
	}

	public boolean isStartEvent() {
		return this.startEvent;
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

	public boolean has__SKIP() {
		if (cachedHas__SKIP != null)
			return cachedHas__SKIP.booleanValue();

		if (this.getAction() != null) {
			String eventAction = this.getAction().toString();
			if (eventAction.indexOf("__SKIP") != -1) {
				cachedHas__SKIP = new Boolean(true);
				return true;
			}
		}
		cachedHas__SKIP = new Boolean(false);
		return false;
	}

	public boolean has__LOC() {
		if (cachedHas__LOC != null)
			return cachedHas__LOC.booleanValue();

		if (this.getAction() != null) {
			String eventAction = this.getAction().toString();
			if (eventAction.indexOf("__LOC") != -1
          || 
          eventAction.indexOf("__DEFAULT_MESSAGE") != -1) {
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

	public BlockStmt getBlock() {
		return this.block;
	}

	public boolean getStartEvent() {
		return this.startEvent;
	}

	public String getEndObjectId() {
		return this.endObjectId;
	}

	public boolean isDuplicated() {
		return this.duplicated;
	}

	public Boolean isCashedHas__SKIP() {

		return this.cachedHas__SKIP;
	}

	public Boolean isCachedHas__LOC() {
		return this.isCachedHas__LOC();
	}
	
	public boolean isImplementing(EventDefinitionExt absEvent){
		if (!this.getId().equals(absEvent.getId()))
			return false;
		if (this.getParameters().matchTypes(absEvent.getParameters()))
			return false;

		return true; 
	}
}
