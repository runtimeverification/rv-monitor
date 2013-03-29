package rvmonitor.output;

import rvmonitor.Main;
import rvmonitor.parser.ast.mopspec.PropertyAndHandlers;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RVMJavaCode {
	String code;
	RVMVariable monitorName = null;
	PropertyAndHandlers prop = null;
	Set<String> localVars;

	public RVMJavaCode(String code) {
		this.code = code;
		if (this.code != null)
			this.code = this.code.trim();
	}

	public RVMJavaCode(String code, RVMVariable monitorName) {
		this.code = code;
		if (this.code != null)
			this.code = this.code.trim();
		this.monitorName = monitorName;
	}

	public RVMJavaCode(PropertyAndHandlers prop, String code, RVMVariable monitorName) {
		this.prop = prop;
		this.code = code;
		if (this.code != null)
			this.code = this.code.trim();
		this.monitorName = monitorName;
	}

	public RVMJavaCode(PropertyAndHandlers prop, String code, RVMVariable monitorName, Set<String> localVars) {
		this(prop, code, monitorName);
		this.localVars = localVars;
	}

	public String rewriteVariables(String input) {
		String ret = input;
		String tagPattern = "\\$(\\w+)\\$";
		Pattern pattern = Pattern.compile(tagPattern);
		Matcher matcher = pattern.matcher(ret);

		while (matcher.find()) {
			String tagStr = matcher.group();
			String varName = tagStr.replaceAll(tagPattern, "$1");
			RVMVariable var;

			if (prop == null)
				var = new RVMVariable(varName);
			else {
				if (localVars != null && localVars.contains(varName))
					var = new RVMVariable(varName);
				else
					var = new RVMVariable("Prop_" + prop.getPropertyId() + "_" + varName);
			}

			ret = ret.replaceAll(tagStr.replaceAll("\\$", "\\\\\\$"), var.toString());
		}
		return ret;
	}

	public boolean isEmpty() {
		if (code == null || code.length() == 0)
			return true;
		else
			return false;
	}

	public String toString() {
		String ret = "";

		if (code != null)
			ret += code;

		if (this.monitorName != null)
			ret = ret.replaceAll("\\@MONITORCLASS", monitorName.toString());

		ret = rewriteVariables(ret);

		if (ret.length() != 0 && !ret.endsWith("\n"))
			ret += "\n";

		if (Main.dacapo) {
			if(Main.silent){
				ret = ret.replaceAll("System.out.println\\(", "{}//System.out.println\\(");
				ret = ret.replaceAll("System.err.println\\(", "{}//System.err.println\\(");
			} else {
				ret = ret.replaceAll("System.out.println\\(\"", "System.out.println\\(\"VIOLATION:");
				ret = ret.replaceAll("System.err.println\\(\"", "System.err.println\\(\"VIOLATION:");
			}
		}

		return ret;
	}
}
