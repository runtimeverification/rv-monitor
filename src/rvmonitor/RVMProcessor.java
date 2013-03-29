/**
 * @author Feng Chen, Dongyun Jin
 * The class handling the mop specification tree
 */

package rvmonitor;

import rvmonitor.logicclient.LogicRepositoryConnector;
import rvmonitor.logicpluginshells.LogicPluginShellFactory;
import rvmonitor.logicpluginshells.LogicPluginShellResult;
import rvmonitor.output.AspectJCode;
import rvmonitor.output.JavaLibCode;
import rvmonitor.parser.ast.RVMSpecFile;
import rvmonitor.parser.ast.body.BodyDeclaration;
import rvmonitor.parser.ast.mopspec.EventDefinition;
import rvmonitor.parser.ast.mopspec.RVMParameter;
import rvmonitor.parser.ast.mopspec.RVMonitorSpec;
import rvmonitor.parser.ast.mopspec.PropertyAndHandlers;
import rvmonitor.parser.ast.visitor.CollectUserVarVisitor;
import rvmonitor.parser.logicrepositorysyntax.LogicRepositoryType;
import rvmonitor.util.Tool;

import java.util.List;

public class RVMProcessor {
	public static boolean verbose = false;

	public String name;

	public RVMProcessor(String name) {
		this.name = name;
	}

	private void registerUserVar(RVMonitorSpec mopSpec) throws RVMException {
		for (EventDefinition event : mopSpec.getEvents()) {
			RVMNameSpace.addUserVariable(event.getId());
			for(RVMParameter param : event.getRVMParameters()){
				RVMNameSpace.addUserVariable(param.getName());
			}
		}
		for (RVMParameter param : mopSpec.getParameters()) {
			RVMNameSpace.addUserVariable(param.getName());
		}
		RVMNameSpace.addUserVariable(mopSpec.getName());
		for (BodyDeclaration bd : mopSpec.getDeclarations()) {
			List<String> vars = bd.accept(new CollectUserVarVisitor(), null);

			if (vars != null)
				RVMNameSpace.addUserVariables(vars);
		}
	}

	public String process(RVMSpecFile rvmSpecFile) throws RVMException {
		String result;

		// register all user variables to RVMNameSpace to avoid conflicts
		for(RVMonitorSpec mopSpec : rvmSpecFile.getSpecs())
			registerUserVar(mopSpec);

		// Connect to Logic Repository
		for(RVMonitorSpec mopSpec : rvmSpecFile.getSpecs()){
			for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
				// connect to the logic repository and get the logic output
				LogicRepositoryType logicOutput = LogicRepositoryConnector.process(mopSpec, prop);
				// get the monitor from the logic shell
				LogicPluginShellResult logicShellOutput = LogicPluginShellFactory.process(logicOutput, mopSpec.getEventStr());
				prop.setLogicShellOutput(logicShellOutput);
				
				if(logicOutput.getMessage().contains("versioned stack")){
					prop.setVersionedStack();
				}

				if (verbose) {
					System.out.println("== result from logic shell ==");
					System.out.print(logicShellOutput);
					System.out.println("");
				}
			}
		}

		// Error Checker
		for(RVMonitorSpec mopSpec : rvmSpecFile.getSpecs()){
			RVMErrorChecker.verify(mopSpec);
		}

		// Generate output code
		if (Main.toJavaLib)
			result = (new JavaLibCode(name, rvmSpecFile)).toString();
		else
			result = (new AspectJCode(name, rvmSpecFile)).toString();


		// Do indentation
		result = Tool.changeIndentation(result, "", "\t");

		return result;
	}


}
