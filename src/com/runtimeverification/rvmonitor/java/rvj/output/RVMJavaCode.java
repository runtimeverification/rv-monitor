package com.runtimeverification.rvmonitor.java.rvj.output;

import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;

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
	
	/**
	 * Returns the name of the variable for holding the state.
	 * This variable is used to determine the slot in a set of monitors.
	 * If multiple variables are needed to store states, this method
	 * returns null, meaning that the partitioned-set optimization cannot
	 * be used.
	 * Since it seems JavaMOP does not parse the given string, I do a similar
	 * unreliable and dirty string manipulation here.
	 * @return the name of the variable for holding the state
	 */
	public String extractStateVariable() {
		String ret = this.code;
		String tagPattern = "\\$(\\w+)\\$";
		Pattern pattern = Pattern.compile(tagPattern);
		Matcher matcher = pattern.matcher(ret);
		
		String varname = null;

		while (matcher.find()) {
			String tagStr = matcher.group();
			String varName = tagStr.replaceAll(tagPattern, "$1");
			RVMVariable var;
			
			if (!varName.startsWith("state"))
				continue;

			if (prop == null)
				var = new RVMVariable(varName);
			else {
				if (localVars != null && localVars.contains(varName))
					var = new RVMVariable(varName);
				else
					var = new RVMVariable("Prop_" + prop.getPropertyId() + "_" + varName);
			}
			
			// This method works only if there is a single variable.
			if (varname != null)
				return null;
			varname = var.toString();
		}
		return varname;
	}

	public int getNumberOfStates() {
		//String pattern = "\\$transition_\\w+\\$\\[\\]";
		//String pattern = "\\$transition_\\w+\\$\\[\\] = ";
		String pattern = "\\$transition_\\w+\\$\\[\\] = \\{([ ,\\d]+)\\}";
		Matcher matcher = Pattern.compile(pattern).matcher(this.code);
		
		int maxstate = -1;
		
		while (matcher.find()) {
			String tbl = matcher.group(1);
			for (String tostr : tbl.split(",")) {
				int to = Integer.parseInt(tostr.trim());
				maxstate = Math.max(to, maxstate);
			}
		}
		
		// For the sake of the initial state.
		++maxstate;

		return maxstate;
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
