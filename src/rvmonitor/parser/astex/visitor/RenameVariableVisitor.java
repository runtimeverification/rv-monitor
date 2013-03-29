package rvmonitor.parser.astex.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rvmonitor.parser.ast.*;
import rvmonitor.parser.ast.RVMSpecFile;
import rvmonitor.parser.ast.aspectj.ArgsPointCut;
import rvmonitor.parser.ast.aspectj.BaseTypePattern;
import rvmonitor.parser.ast.aspectj.CFlowPointCut;
import rvmonitor.parser.ast.aspectj.CombinedPointCut;
import rvmonitor.parser.ast.aspectj.CombinedTypePattern;
import rvmonitor.parser.ast.aspectj.ConditionPointCut;
import rvmonitor.parser.ast.aspectj.CountCondPointCut;
import rvmonitor.parser.ast.aspectj.EndObjectPointCut;
import rvmonitor.parser.ast.aspectj.EndProgramPointCut;
import rvmonitor.parser.ast.aspectj.EndThreadPointCut;
import rvmonitor.parser.ast.aspectj.FieldPattern;
import rvmonitor.parser.ast.aspectj.FieldPointCut;
import rvmonitor.parser.ast.aspectj.IDPointCut;
import rvmonitor.parser.ast.aspectj.IFPointCut;
import rvmonitor.parser.ast.aspectj.MethodPattern;
import rvmonitor.parser.ast.aspectj.MethodPointCut;
import rvmonitor.parser.ast.aspectj.NotPointCut;
import rvmonitor.parser.ast.aspectj.NotTypePattern;
import rvmonitor.parser.ast.aspectj.PointCut;
import rvmonitor.parser.ast.aspectj.StartThreadPointCut;
import rvmonitor.parser.ast.aspectj.TargetPointCut;
import rvmonitor.parser.ast.aspectj.ThisPointCut;
import rvmonitor.parser.ast.aspectj.ThreadBlockedPointCut;
import rvmonitor.parser.ast.aspectj.ThreadNamePointCut;
import rvmonitor.parser.ast.aspectj.ThreadPointCut;
import rvmonitor.parser.ast.aspectj.TypePattern;
import rvmonitor.parser.ast.aspectj.WildcardParameter;
import rvmonitor.parser.ast.aspectj.WithinPointCut;
import rvmonitor.parser.ast.body.AnnotationDeclaration;
import rvmonitor.parser.ast.body.AnnotationMemberDeclaration;
import rvmonitor.parser.ast.body.ClassOrInterfaceDeclaration;
import rvmonitor.parser.ast.body.ConstructorDeclaration;
import rvmonitor.parser.ast.body.EmptyMemberDeclaration;
import rvmonitor.parser.ast.body.EmptyTypeDeclaration;
import rvmonitor.parser.ast.body.EnumConstantDeclaration;
import rvmonitor.parser.ast.body.EnumDeclaration;
import rvmonitor.parser.ast.body.FieldDeclaration;
import rvmonitor.parser.ast.body.InitializerDeclaration;
import rvmonitor.parser.ast.body.MethodDeclaration;
import rvmonitor.parser.ast.body.Parameter;
import rvmonitor.parser.ast.body.VariableDeclarator;
import rvmonitor.parser.ast.body.VariableDeclaratorId;
import rvmonitor.parser.ast.expr.ArrayAccessExpr;
import rvmonitor.parser.ast.expr.ArrayCreationExpr;
import rvmonitor.parser.ast.expr.ArrayInitializerExpr;
import rvmonitor.parser.ast.expr.AssignExpr;
import rvmonitor.parser.ast.expr.BinaryExpr;
import rvmonitor.parser.ast.expr.BooleanLiteralExpr;
import rvmonitor.parser.ast.expr.CastExpr;
import rvmonitor.parser.ast.expr.CharLiteralExpr;
import rvmonitor.parser.ast.expr.ClassExpr;
import rvmonitor.parser.ast.expr.ConditionalExpr;
import rvmonitor.parser.ast.expr.DoubleLiteralExpr;
import rvmonitor.parser.ast.expr.EnclosedExpr;
import rvmonitor.parser.ast.expr.Expression;
import rvmonitor.parser.ast.expr.FieldAccessExpr;
import rvmonitor.parser.ast.expr.InstanceOfExpr;
import rvmonitor.parser.ast.expr.IntegerLiteralExpr;
import rvmonitor.parser.ast.expr.IntegerLiteralMinValueExpr;
import rvmonitor.parser.ast.expr.LongLiteralExpr;
import rvmonitor.parser.ast.expr.LongLiteralMinValueExpr;
import rvmonitor.parser.ast.expr.MarkerAnnotationExpr;
import rvmonitor.parser.ast.expr.MemberValuePair;
import rvmonitor.parser.ast.expr.MethodCallExpr;
import rvmonitor.parser.ast.expr.NameExpr;
import rvmonitor.parser.ast.expr.NormalAnnotationExpr;
import rvmonitor.parser.ast.expr.NullLiteralExpr;
import rvmonitor.parser.ast.expr.ObjectCreationExpr;
import rvmonitor.parser.ast.expr.QualifiedNameExpr;
import rvmonitor.parser.ast.expr.SingleMemberAnnotationExpr;
import rvmonitor.parser.ast.expr.StringLiteralExpr;
import rvmonitor.parser.ast.expr.SuperExpr;
import rvmonitor.parser.ast.expr.SuperMemberAccessExpr;
import rvmonitor.parser.ast.expr.ThisExpr;
import rvmonitor.parser.ast.expr.UnaryExpr;
import rvmonitor.parser.ast.expr.VariableDeclarationExpr;
import rvmonitor.parser.ast.mopspec.*;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.stmt.AssertStmt;
import rvmonitor.parser.ast.stmt.BlockStmt;
import rvmonitor.parser.ast.stmt.BreakStmt;
import rvmonitor.parser.ast.stmt.CatchClause;
import rvmonitor.parser.ast.stmt.ContinueStmt;
import rvmonitor.parser.ast.stmt.DoStmt;
import rvmonitor.parser.ast.stmt.EmptyStmt;
import rvmonitor.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import rvmonitor.parser.ast.stmt.ExpressionStmt;
import rvmonitor.parser.ast.stmt.ForStmt;
import rvmonitor.parser.ast.stmt.ForeachStmt;
import rvmonitor.parser.ast.stmt.IfStmt;
import rvmonitor.parser.ast.stmt.LabeledStmt;
import rvmonitor.parser.ast.stmt.ReturnStmt;
import rvmonitor.parser.ast.stmt.SwitchEntryStmt;
import rvmonitor.parser.ast.stmt.SwitchStmt;
import rvmonitor.parser.ast.stmt.SynchronizedStmt;
import rvmonitor.parser.ast.stmt.ThrowStmt;
import rvmonitor.parser.ast.stmt.TryStmt;
import rvmonitor.parser.ast.stmt.TypeDeclarationStmt;
import rvmonitor.parser.ast.stmt.WhileStmt;
import rvmonitor.parser.ast.type.ClassOrInterfaceType;
import rvmonitor.parser.ast.type.PrimitiveType;
import rvmonitor.parser.ast.type.ReferenceType;
import rvmonitor.parser.ast.type.VoidType;
import rvmonitor.parser.ast.type.WildcardType;

public class RenameVariableVisitor implements rvmonitor.parser.ast.visitor.GenericVisitor<Node, HashMap<String, RVMParameter>> {

	@Override
	public Node visit(Node n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(RVMSpecFile f, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(RVMonitorSpec s, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(RVMParameter p, HashMap<String, RVMParameter> arg) {
		RVMParameter param = arg.get(p.getName());
		
		if(param != null)
			return new RVMParameter(p.getBeginLine(), p.getBeginColumn(), p.getType(), param.getName());

		return p;
	}

	@Override
	public Node visit(EventDefinition e, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(PropertyAndHandlers p, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(Formula f, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(WildcardParameter w, HashMap<String, RVMParameter> arg) {
		return w;
	}

	@Override
	public Node visit(ArgsPointCut p, HashMap<String, RVMParameter> arg) {
		List<TypePattern> list = new ArrayList<TypePattern>();

		for(int i = 0; i < p.getArgs().size(); i++){
			TypePattern type = p.getArgs().get(i);
			
			list.add((TypePattern)type.accept(this, arg));
		}
		
		return new ArgsPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), list);
	}

	@Override
	public Node visit(CombinedPointCut p, HashMap<String, RVMParameter> arg) {
		List<PointCut> pointcuts = new ArrayList<PointCut>();
		
		for(PointCut p2 : p.getPointcuts()){
			PointCut p3 = (PointCut)p2.accept(this, arg);
			
			pointcuts.add(p3);
		}
		
		return new CombinedPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), pointcuts);
	}

	@Override
	public Node visit(NotPointCut p, HashMap<String, RVMParameter> arg) {
		PointCut sub = (PointCut)p.getPointCut().accept(this, arg);
		
		if(p.getPointCut() == sub)
			return p;
		
		return new NotPointCut(p.getBeginLine(), p.getBeginColumn(), sub);
	}

	@Override
	public Node visit(ConditionPointCut p, HashMap<String, RVMParameter> arg) {
		Expression expr = (Expression)p.getExpression().accept(this, arg);
		
		if(p.getExpression() == expr)
			return p;
		
		return new ConditionPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), expr);
	}
	
	@Override
	public Node visit(CountCondPointCut p, HashMap<String, RVMParameter> arg) {
		Expression expr = (Expression)p.getExpression().accept(this, arg);
		
		if(p.getExpression() == expr)
			return p;
		
		return new CountCondPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), expr);
	}

	@Override
	public Node visit(FieldPointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(MethodPointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(TargetPointCut p, HashMap<String, RVMParameter> arg) {
		TypePattern target = (TypePattern)p.getTarget().accept(this, arg);
		
		if(p.getTarget() == target)
			return p;
		
		return new TargetPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), target);
	}

	@Override
	public Node visit(ThisPointCut p, HashMap<String, RVMParameter> arg) {
		TypePattern target = (TypePattern)p.getTarget().accept(this, arg);
		
		if(p.getTarget() == target)
			return p;
		
		return new ThisPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), target);
	}

	@Override
	public Node visit(CFlowPointCut p, HashMap<String, RVMParameter> arg) {
		PointCut sub = (PointCut)p.getPointCut().accept(this, arg);
		
		if(p.getPointCut() == sub)
			return p;
		
		return new CFlowPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), sub);
	}

	@Override
	public Node visit(IFPointCut p, HashMap<String, RVMParameter> arg) {
		Expression expr = (Expression)p.getExpression().accept(this, arg);
		
		if(p.getExpression() == expr)
			return p;
		
		return new IFPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), expr);
	}

	@Override
	public Node visit(IDPointCut p, HashMap<String, RVMParameter> arg) {
		List<TypePattern> list = new ArrayList<TypePattern>();

		for(int i = 0; i < p.getArgs().size(); i++){
			TypePattern type = p.getArgs().get(i);
			
			list.add((TypePattern)type.accept(this, arg));
		}
		
		return new IDPointCut(p.getBeginLine(), p.getBeginColumn(), p.getType(), list);
	}

	@Override
	public Node visit(WithinPointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(ThreadPointCut p, HashMap<String, RVMParameter> arg) {
		RVMParameter param = arg.get(p.getId());
		
		if(param != null)
			return new ThreadPointCut(p.getBeginLine(), p.getBeginColumn(), param.getName());

		return p;
	}
	
	@Override
	public Node visit(ThreadNamePointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}
	
	@Override
	public Node visit(ThreadBlockedPointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(EndProgramPointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(EndThreadPointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(EndObjectPointCut p, HashMap<String, RVMParameter> arg) {
		RVMParameter param = arg.get(p.getId());
		
		if(param != null)
			return new EndObjectPointCut(p.getBeginLine(), p.getBeginColumn(), p.getTargetType(), param.getName());

		return p;
	}

	@Override
	public Node visit(StartThreadPointCut p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(FieldPattern p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(MethodPattern p, HashMap<String, RVMParameter> arg) {
		return p;
	}

	@Override
	public Node visit(CombinedTypePattern p, HashMap<String, RVMParameter> arg) {
		List<TypePattern> subTypes = new ArrayList<TypePattern>();

		for(int i = 0; i < p.getSubTypes().size(); i++){
			subTypes.add((TypePattern)p.getSubTypes().get(i).accept(this, arg));
		}

		return new CombinedTypePattern(p.getBeginLine(), p.getBeginColumn(), p.getOp(), subTypes);
	}

	@Override
	public Node visit(NotTypePattern p, HashMap<String, RVMParameter> arg) {
		TypePattern type = (TypePattern)p.getType().accept(this, arg);
		
		if(p.getType() == type)
			return p;
		
		return new NotTypePattern(p.getBeginLine(), p.getBeginColumn(), type);
	}

	@Override
	public Node visit(BaseTypePattern p, HashMap<String, RVMParameter> arg) {
		RVMParameter param = arg.get(p.getOp());
		
		if(param != null)
			return new BaseTypePattern(p.getBeginLine(), p.getBeginColumn(), param.getName());

		return p;
	}

	@Override
	public Node visit(CompilationUnit n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(PackageDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(ImportDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(TypeParameter n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(ClassOrInterfaceDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(EnumDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(EmptyTypeDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(EnumConstantDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(AnnotationDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(AnnotationMemberDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(FieldDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(VariableDeclarator n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(VariableDeclaratorId n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(ConstructorDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(MethodDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(Parameter n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(EmptyMemberDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(InitializerDeclaration n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(ClassOrInterfaceType n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(PrimitiveType n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(ReferenceType n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(VoidType n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(WildcardType n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(ArrayAccessExpr n, HashMap<String, RVMParameter> arg) {
		Expression name = (Expression)n.getName().accept(this, arg);
		Expression index = (Expression)n.getIndex().accept(this, arg);

		if(n.getName() == name && n.getIndex() == index)
			return n;

		return new ArrayAccessExpr(n.getBeginLine(), n.getBeginColumn(), name, index);
	}

	@Override
	public Node visit(ArrayCreationExpr n, HashMap<String, RVMParameter> arg) {
		if(n.getDimensions() != null){
			List<Expression> dims = new ArrayList<Expression>();
	
			for(int i = 0; i < n.getDimensions().size(); i++){
				dims.add((Expression)n.getDimensions().get(i).accept(this, arg));
			}
			
			return new ArrayCreationExpr(n.getBeginLine(), n.getBeginColumn(), n.getType(), n.getTypeArgs(), dims, n.getArrayCount());
		} if(n.getInitializer() != null){
			ArrayInitializerExpr initializer = (ArrayInitializerExpr)n.getInitializer().accept(this, arg);
			
			if(n.getInitializer() == initializer)
				return n;
			
			return new ArrayCreationExpr(n.getBeginLine(), n.getBeginColumn(), n.getType(), n.getTypeArgs(), n.getArrayCount(), initializer);
		}
		
		return n;
	}

	@Override
	public Node visit(ArrayInitializerExpr n, HashMap<String, RVMParameter> arg) {
		List<Expression> values = new ArrayList<Expression>();
		
		for(int i = 0; i < n.getValues().size(); i++){
			values.add((Expression)n.getValues().get(i).accept(this, arg));
		}
		
		return new ArrayInitializerExpr(n.getBeginLine(), n.getBeginColumn(), values);
	}

	@Override
	public Node visit(AssignExpr n, HashMap<String, RVMParameter> arg) {
		Expression target = (Expression)n.getTarget().accept(this, arg);
		Expression value = (Expression)n.getValue().accept(this, arg);

		if(n.getTarget() == target && n.getValue() == value)
			return n;

		return new AssignExpr(n.getBeginLine(), n.getBeginColumn(), target, value, n.getOperator());
	}

	@Override
	public Node visit(BinaryExpr n, HashMap<String, RVMParameter> arg) {
		Expression left = (Expression)n.getLeft().accept(this, arg);
		Expression right = (Expression)n.getRight().accept(this, arg);

		if(n.getLeft() == left && n.getRight() == right)
			return n;
		
		return new BinaryExpr(n.getBeginLine(), n.getBeginColumn(), left, right, n.getOperator());
	}

	@Override
	public Node visit(CastExpr n, HashMap<String, RVMParameter> arg) {
		Expression expr = (Expression)n.getExpr().accept(this, arg);
		
		if(n.getExpr() == expr)
			return n;
		
		return new CastExpr(n.getBeginLine(), n.getBeginColumn(), n.getType(), expr);
	}

	@Override
	public Node visit(ClassExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ConditionalExpr n, HashMap<String, RVMParameter> arg) {
		Expression condition = (Expression)n.getCondition().accept(this, arg);
		Expression thenExpr = (Expression)n.getThenExpr().accept(this, arg);
		Expression elseExpr = (Expression)n.getElseExpr().accept(this, arg);
		
		if(n.getCondition() == condition && n.getThenExpr() == thenExpr && n.getElseExpr() == elseExpr)
			return n;
		
		return new ConditionalExpr(n.getBeginLine(), n.getBeginColumn(), condition, thenExpr, elseExpr);
	}

	@Override
	public Node visit(EnclosedExpr n, HashMap<String, RVMParameter> arg) {
		Expression inner = (Expression)n.getInner().accept(this, arg);

		if(n.getInner() == inner)
			return n;
		
		return new EnclosedExpr(n.getBeginLine(), n.getBeginColumn(), inner);
	}

	@Override
	public Node visit(FieldAccessExpr n, HashMap<String, RVMParameter> arg) {
		Expression scope = (Expression)n.getScope().accept(this, arg);
		
		if(n.getScope() == scope)
			return n;
		
		return new FieldAccessExpr(n.getBeginLine(), n.getBeginColumn(), scope, n.getTypeArgs(), n.getField());
	}

	@Override
	public Node visit(InstanceOfExpr n, HashMap<String, RVMParameter> arg) {
		Expression expr = (Expression)n.getExpr().accept(this, arg);
		
		if(n.getExpr() == expr)
			return n;

		return new InstanceOfExpr(n.getBeginLine(), n.getBeginColumn(), n.getExpr(), n.getType());
	}

	@Override
	public Node visit(StringLiteralExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(IntegerLiteralExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(LongLiteralExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(IntegerLiteralMinValueExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(LongLiteralMinValueExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(CharLiteralExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(DoubleLiteralExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(BooleanLiteralExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(NullLiteralExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(MethodCallExpr n, HashMap<String, RVMParameter> arg) {
		Expression scope = (Expression)n.getScope().accept(this, arg);
		
		List<Expression> args = new ArrayList<Expression>();

		for(int i = 0; i < n.getArgs().size(); i++){
			args.add((Expression)n.getArgs().get(i).accept(this, arg));
		}

		return new MethodCallExpr(n.getBeginLine(), n.getBeginColumn(), scope, n.getTypeArgs(), n.getName(), args);
	}

	@Override
	public Node visit(NameExpr n, HashMap<String, RVMParameter> arg) {
		RVMParameter param = arg.get(n.getName());

		if(param != null)
			return new NameExpr(n.getBeginLine(), n.getBeginColumn(), param.getName());
		
		return n;
	}

	@Override
	public Node visit(ObjectCreationExpr n, HashMap<String, RVMParameter> arg) {
		Expression scope = (Expression)n.getScope().accept(this, arg);
		
		List<Expression> args = new ArrayList<Expression>();

		for(int i = 0; i < n.getArgs().size(); i++){
			args.add((Expression)n.getArgs().get(i).accept(this, arg));
		}

		return new ObjectCreationExpr(n.getBeginLine(), n.getBeginColumn(), scope, n.getType(), n.getTypeArgs(), args, n.getAnonymousClassBody());

	}

	@Override
	public Node visit(QualifiedNameExpr n, HashMap<String, RVMParameter> arg) {
		NameExpr qualifier = (NameExpr)n.getQualifier().accept(this, arg);
		RVMParameter param = arg.get(n.getName());
		
		if(n.getQualifier() == qualifier && param == null)
			return n;
		
		return new QualifiedNameExpr(n.getBeginLine(), n.getBeginColumn(), qualifier, param.getName());
	}

	@Override
	public Node visit(SuperMemberAccessExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ThisExpr n, HashMap<String, RVMParameter> arg) {
		Expression classExpr = (Expression)n.getClassExpr().accept(this, arg);
		
		if(n.getClassExpr() == classExpr)
			return n;

		return new ThisExpr(n.getBeginLine(), n.getBeginColumn(), classExpr);
	}

	@Override
	public Node visit(SuperExpr n, HashMap<String, RVMParameter> arg) {
		Expression classExpr = (Expression)n.getClassExpr().accept(this, arg);
		
		if(n.getClassExpr() == classExpr)
			return n;

		return new SuperExpr(n.getBeginLine(), n.getBeginColumn(), classExpr);
	}

	@Override
	public Node visit(UnaryExpr n, HashMap<String, RVMParameter> arg) {
		Expression expr = (Expression)n.getExpr().accept(this, arg);
		
		if(n.getExpr() == expr)
			return n;

		return new UnaryExpr(n.getBeginLine(), n.getBeginColumn(), expr, n.getOperator());

	}

	@Override
	public Node visit(VariableDeclarationExpr n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(MarkerAnnotationExpr n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(SingleMemberAnnotationExpr n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(NormalAnnotationExpr n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(MemberValuePair n, HashMap<String, RVMParameter> arg) {
		return null;
	}

	@Override
	public Node visit(ExplicitConstructorInvocationStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(TypeDeclarationStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(AssertStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(BlockStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(LabeledStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(EmptyStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ExpressionStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(SwitchStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(SwitchEntryStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(BreakStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ReturnStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(IfStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(WhileStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ContinueStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(DoStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ForeachStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ForStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(ThrowStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(SynchronizedStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(TryStmt n, HashMap<String, RVMParameter> arg) {
		return n;
	}

	@Override
	public Node visit(CatchClause n, HashMap<String, RVMParameter> arg) {
		return n;
	}
}

