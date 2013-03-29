package rvmonitor;

import rvmonitor.parser.RVMonitorExtender;
import rvmonitor.parser.ast.RVMSpecFile;
import rvmonitor.parser.astex.RVMSpecFileExt;
import rvmonitor.parser.main_parser.RVMonitorParser;
import rvmonitor.util.Tool;

import java.io.ByteArrayInputStream;
import java.io.File;

public class SpecExtractor {

	static private String convertFileToString(String path) throws RVMException {
		String content;
		try {
			content = Tool.convertFileToString(path);
		} catch (Exception e) {
			throw new RVMException(e.getMessage());
		}
		return content;
	}

	static private String getAnnotations(String input) throws RVMException {
		String content = "";

		int start = input.indexOf("/*@", 0), end;

		while (start > -1) {
			end = input.indexOf("@*/", start);

			if (end > -1)
				content += input.substring(start + 3, end); // 4 means /*@ + a space
			else
				throw new RVMException("annotation block didn't end");

			start = input.indexOf("/*@", start + 1);
		}
		return content;
	}

	static public String process(File file) throws RVMException {
		if (Tool.isSpecFile(file.getName())) {
			return convertFileToString(file.getAbsolutePath());
		} else if (Tool.isJavaFile(file.getName())) {
			String javaContent = convertFileToString(file.getAbsolutePath());
			String specContent = getAnnotations(javaContent);
			return specContent;
		} else {
			return "";
		}
	}

	static public RVMSpecFile parse(String input) throws RVMException {
		RVMSpecFile rvmSpecFile;
		try {
			RVMSpecFileExt rvmSpecFileExt = RVMonitorParser.parse(new ByteArrayInputStream(input.getBytes()));
			rvmSpecFile = RVMonitorExtender.translateMopSpecFile(rvmSpecFileExt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RVMException("Error when parsing a specification file:\n" + e.getMessage());
		}

		return rvmSpecFile;
	}

}
