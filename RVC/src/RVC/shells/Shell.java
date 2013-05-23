package RVC.shells;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;

public abstract class Shell {
	public String monitorType = "Error";

	public Shell() {
	}

	public ShellResult process(LogicRepositoryType logicOutput, String events) throws Exception {

		throw new Exception("Non-recognizable Monitor Type");
	}

	public Properties addEnableSets(Properties properties, LogicRepositoryType logicOutputXML) {
		String enableSetsStr = logicOutputXML.getEnableSets();
		
		Pattern p = Pattern.compile("\\s*//\\s*\\w+(\\s*\\w*)*\\s*\\n");
		Matcher matcher = p.matcher(enableSetsStr);

		String tag = "";
		String value = "";
		int prevPosition = -1;

		while (matcher.find()) {
			if (prevPosition != -1) {
				value = enableSetsStr.substring(prevPosition, matcher.start());
				properties.setProperty(tag.trim().toLowerCase(), value);
			}

			tag = matcher.group();
			tag = tag.replaceAll("\\s*//\\s*(\\w+(\\s*\\w*)*)\\s*\\n", "$1");
			prevPosition = matcher.end();
		}
		if (prevPosition != -1) {
			value = enableSetsStr.substring(prevPosition);
			properties.setProperty(tag.trim().toLowerCase(), value);
		}

		return properties;
	}

}
