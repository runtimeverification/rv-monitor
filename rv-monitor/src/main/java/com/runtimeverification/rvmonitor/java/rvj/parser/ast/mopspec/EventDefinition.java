package com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec;

import com.runtimeverification.rvmonitor.java.rvj.parser.ast.CompilationUnit;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.ImportDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.Node;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.PackageDeclaration;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.RVMSpecFile;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.TypeParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ArgsPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.BaseTypePattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.CFlowPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.CombinedPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.CombinedTypePattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ConditionPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.CountCondPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.EndObjectPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.EndProgramPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.EndThreadPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.FieldPattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.FieldPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.IDPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.IFPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.MethodPattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.MethodPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.NotPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.NotTypePattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.StartThreadPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.TargetPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThisPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThreadBlockedPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThreadNamePointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.ThreadPointCut;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.TypePattern;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.WildcardParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.aspectj.WithinPointCut;
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
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.expr.Expression;
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
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.stmt.Statement;
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
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.visitor.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EventDefinition extends Node implements Comparable<EventDefinition> {

	private final String id;

	private String purePointCutStr;

	private final RVMParameters parameters;

	final RVMParameters rvmParameters;

	private final String block;
	private RVMParameters usedParameter = null;


	// will be modified by RVMonitorSpec when creation events are not specified
	boolean startEvent = false;
	
	boolean blockingEvent = false;

	private String condition;
	private String threadVar;
	private ArrayList<String> threadBlockedVars;
	private TypePattern endObjectType;
	private String endObjectId;
	private boolean endProgram = false;
	private boolean endThread = false;
	private boolean startThread = false;
	private boolean endObject = false;
	String countCond;

	// things that should be defined afterward
	int idnum; // will be defined in RVMonitorSpec
	boolean duplicated = false; // will be defined in RVMonitorSpec
	String uniqueId = null; // will be defined in RVMonitorSpec
	RVMParameters rvmParametersOnSpec; // will be defined in RVMonitorSpec

	public EventDefinition(int beginLine, int beginColumn, String id, List<RVMParameter> rvmParameters, String block, boolean startEvent, boolean blockingEvent) {
		super(beginLine, beginColumn);
		this.id=id;
		this.parameters = new RVMParameters(rvmParameters);
		this.rvmParameters = new RVMParameters(rvmParameters);
		this.block = block;
		this.startEvent = startEvent;
		this.blockingEvent = blockingEvent;
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

	public String getAction() {
		return block;
	}
	
	public RVMParameters getUsedParametersIn(RVMParameters specParam){
        // All of them. If you want to have a property that doesn't use all the parameters,
        // put it in another specification.
        return specParam;
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
	
	public RVMParameters getReferredParameters(RVMParameters projectedon) {
        return projectedon;
	}
	

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

	@Override
	public int compareTo(EventDefinition that) {
		return this.id.compareTo(that.id);
	}
}
