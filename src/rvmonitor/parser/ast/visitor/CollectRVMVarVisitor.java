package rvmonitor.parser.ast.visitor;

import rvmonitor.parser.ast.*;
import rvmonitor.parser.ast.aspectj.*;
import rvmonitor.parser.ast.body.*;
import rvmonitor.parser.ast.expr.*;
import rvmonitor.parser.ast.mopspec.*;
import rvmonitor.parser.ast.stmt.*;
import rvmonitor.parser.ast.type.*;

import java.util.Collection;

public class CollectRVMVarVisitor implements GenericVisitor<RVMParameters, RVMParameters> {

	public RVMParameters visit(Node n, RVMParameters arg) {
		return null;
	}

	// helper function
	private RVMParameters process(RVMParameters ret, Object n, RVMParameters arg) {
		if (n == null)
			return ret;

		if (n instanceof Node) {
			Node n2 = (Node) n;

			RVMParameters temp = n2.accept(this, arg);

			ret.addAll(temp);
		} else if (n instanceof Collection) {
			Collection<?> c = (Collection<?>) n;

			for (Object o : c) {
				if (o instanceof Node) {
					Node n2 = (Node) o;

					RVMParameters temp = n2.accept(this, arg);

					if (temp != null)
						ret.addAll(temp);
				}
			}
		}

		return ret;
	}

	// - RV Monitor components

	public RVMParameters visit(RVMSpecFile f, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(RVMonitorSpec s, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(RVMParameter p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(EventDefinition e, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(PropertyAndHandlers p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(Formula f, RVMParameters arg) {
		return null;
	}

	// - AspectJ components --------------------

	public RVMParameters visit(WildcardParameter w, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ArgsPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(CombinedPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(NotPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ConditionPointCut p, RVMParameters arg) {
		return null;
	}
	
	public RVMParameters visit(CountCondPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(FieldPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(MethodPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(TargetPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ThisPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(CFlowPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(IFPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(IDPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(WithinPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ThreadPointCut p, RVMParameters arg) {
		return null;
	}
	
	public RVMParameters visit(ThreadNamePointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ThreadBlockedPointCut p, RVMParameters arg) {
		return null;
	}
	
	public RVMParameters visit(EndProgramPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(EndThreadPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(EndObjectPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(StartThreadPointCut p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(FieldPattern p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(MethodPattern p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(CombinedTypePattern p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(NotTypePattern p, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(BaseTypePattern p, RVMParameters arg) {
		return null;
	}

	// - Compilation Unit ----------------------------------

	public RVMParameters visit(CompilationUnit n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(PackageDeclaration n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ImportDeclaration n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(TypeParameter n, RVMParameters arg) {
		return null;
	}

	// - Body ----------------------------------------------

	public RVMParameters visit(ClassOrInterfaceDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);

		return ret;
	}

	public RVMParameters visit(EnumDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);
		process(ret, n.getEntries(), arg);

		return ret;
	}

	public RVMParameters visit(EmptyTypeDeclaration n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(EnumConstantDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);
		process(ret, n.getArgs(), arg);
		process(ret, n.getClassBody(), arg);

		return ret;
	}

	public RVMParameters visit(AnnotationDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);

		return ret;
	}

	public RVMParameters visit(AnnotationMemberDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);
		process(ret, n.getDefaultValue(), arg);

		return ret;
	}

	public RVMParameters visit(FieldDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);
		process(ret, n.getVariables(), arg);

		return ret;
	}

	public RVMParameters visit(VariableDeclarator n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getInit(), arg);

		return ret;
	}

	public RVMParameters visit(VariableDeclaratorId n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ConstructorDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);
		process(ret, n.getParameters(), arg);
		process(ret, n.getThrows(), arg);
		process(ret, n.getBlock(), arg);

		return ret;
	}

	public RVMParameters visit(MethodDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);
		process(ret, n.getParameters(), arg);
		process(ret, n.getThrows(), arg);
		process(ret, n.getBody(), arg);

		return ret;
	}

	public RVMParameters visit(Parameter n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);

		return ret;
	}

	public RVMParameters visit(EmptyMemberDeclaration n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(InitializerDeclaration n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getBlock(), arg);

		return ret;
	}

	// - Type ----------------------------------------------

	public RVMParameters visit(ClassOrInterfaceType n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(PrimitiveType n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ReferenceType n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(VoidType n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(WildcardType n, RVMParameters arg) {
		return null;
	}

	// - Expression ----------------------------------------

	public RVMParameters visit(ArrayAccessExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getName(), arg);
		process(ret, n.getIndex(), arg);

		return ret;
	}

	public RVMParameters visit(ArrayCreationExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getInitializer(), arg);
		process(ret, n.getDimensions(), arg);

		return ret;
	}

	public RVMParameters visit(ArrayInitializerExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getValues(), arg);

		return ret;
	}

	public RVMParameters visit(AssignExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getTarget(), arg);
		process(ret, n.getValue(), arg);

		return ret;
	}

	public RVMParameters visit(BinaryExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getLeft(), arg);
		process(ret, n.getRight(), arg);

		return ret;
	}

	public RVMParameters visit(CastExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpr(), arg);

		return ret;
	}

	public RVMParameters visit(ClassExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ConditionalExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getCondition(), arg);
		process(ret, n.getThenExpr(), arg);
		process(ret, n.getElseExpr(), arg);

		return ret;
	}

	public RVMParameters visit(EnclosedExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getInner(), arg);

		return ret;
	}

	public RVMParameters visit(FieldAccessExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(InstanceOfExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpr(), arg);

		return ret;
	}

	public RVMParameters visit(StringLiteralExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(IntegerLiteralExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(LongLiteralExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(IntegerLiteralMinValueExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(LongLiteralMinValueExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(CharLiteralExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(DoubleLiteralExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(BooleanLiteralExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(NullLiteralExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(MethodCallExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getArgs(), arg);

		return ret;
	}

	public RVMParameters visit(NameExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		if (arg.getParam(n.getName()) != null) {
			ret.add(arg.getParam(n.getName()));
		}

		return ret;
	}

	public RVMParameters visit(ObjectCreationExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getArgs(), arg);
		process(ret, n.getAnonymousClassBody(), arg);

		return ret;
	}

	public RVMParameters visit(QualifiedNameExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getQualifier(), arg);

		return ret;
	}

	public RVMParameters visit(SuperMemberAccessExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ThisExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getClassExpr(), arg);

		return ret;
	}

	public RVMParameters visit(SuperExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getClassExpr(), arg);

		return ret;
	}

	public RVMParameters visit(UnaryExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpr(), arg);

		return ret;
	}

	public RVMParameters visit(VariableDeclarationExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getAnnotations(), arg);
		process(ret, n.getVars(), arg);

		return ret;
	}

	public RVMParameters visit(MarkerAnnotationExpr n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(SingleMemberAnnotationExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getMemberValue(), arg);

		return ret;
	}

	public RVMParameters visit(NormalAnnotationExpr n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getPairs(), arg);

		return ret;
	}

	public RVMParameters visit(MemberValuePair n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getValue(), arg);

		return ret;
	}

	// - Statements ----------------------------------------

	public RVMParameters visit(ExplicitConstructorInvocationStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpr(), arg);
		process(ret, n.getArgs(), arg);

		return ret;
	}

	public RVMParameters visit(TypeDeclarationStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getTypeDeclaration(), arg);

		return ret;
	}

	public RVMParameters visit(AssertStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getCheck(), arg);
		process(ret, n.getMessage(), arg);

		return ret;
	}

	public RVMParameters visit(BlockStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getStmts(), arg);

		return ret;
	}

	public RVMParameters visit(LabeledStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getStmt(), arg);

		return ret;
	}

	public RVMParameters visit(EmptyStmt n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ExpressionStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpression(), arg);

		return ret;
	}

	public RVMParameters visit(SwitchStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getSelector(), arg);
		process(ret, n.getEntries(), arg);

		return ret;
	}

	public RVMParameters visit(SwitchEntryStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getLabel(), arg);
		process(ret, n.getStmts(), arg);

		return ret;
	}

	public RVMParameters visit(BreakStmt n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(ReturnStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpr(), arg);

		return ret;
	}

	public RVMParameters visit(IfStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getCondition(), arg);
		process(ret, n.getThenStmt(), arg);
		process(ret, n.getElseStmt(), arg);

		return ret;
	}

	public RVMParameters visit(WhileStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getCondition(), arg);
		process(ret, n.getBody(), arg);

		return ret;
	}

	public RVMParameters visit(ContinueStmt n, RVMParameters arg) {
		return null;
	}

	public RVMParameters visit(DoStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getBody(), arg);
		process(ret, n.getCondition(), arg);

		return ret;
	}

	public RVMParameters visit(ForeachStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getVariable(), arg);
		process(ret, n.getIterable(), arg);
		process(ret, n.getBody(), arg);

		return ret;
	}

	public RVMParameters visit(ForStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getInit(), arg);
		process(ret, n.getCompare(), arg);
		process(ret, n.getUpdate(), arg);
		process(ret, n.getBody(), arg);

		return ret;
	}

	public RVMParameters visit(ThrowStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpr(), arg);

		return ret;
	}

	public RVMParameters visit(SynchronizedStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExpr(), arg);
		process(ret, n.getBlock(), arg);

		return ret;
	}

	public RVMParameters visit(TryStmt n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getTryBlock(), arg);
		process(ret, n.getCatchs(), arg);
		process(ret, n.getFinallyBlock(), arg);

		return ret;
	}

	public RVMParameters visit(CatchClause n, RVMParameters arg) {
		RVMParameters ret = new RVMParameters();

		process(ret, n.getExcept(), arg);
		process(ret, n.getCatchBlock(), arg);

		return ret;
	}

}
