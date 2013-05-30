package com.runtimeverification.rvmonitor.java.rvj.logicclient;

import com.runtimeverification.rvmonitor.java.rvj.Configuration;
import com.runtimeverification.rvmonitor.java.rvj.Main;
import com.runtimeverification.rvmonitor.java.rvj.RVMException;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.Formula;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.PropertyAndHandlers;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;
import com.runtimeverification.rvmonitor.java.rvj.parser.logicrepositorysyntax.LogicRepositoryType;
import com.runtimeverification.rvmonitor.java.rvj.parser.logicrepositorysyntax.PropertyType;
import com.runtimeverification.rvmonitor.util.StreamGobbler;
import com.runtimeverification.rvmonitor.util.Tool;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LogicRepositoryConnector {
	public static String serverName = "local";
	public static boolean verbose = false;

	public static LogicRepositoryType process(RVMonitorSpec mopSpec, PropertyAndHandlers prop) throws RVMException {
		if (mopSpec == null || prop == null)
			throw new RVMException("No annotation specified");

		String formula = "";
		String categories = "";

		formula += ((Formula) prop.getProperty()).getFormula().trim();
		for (String key : prop.getHandlers().keySet()) {
			categories += " " + key;
		}
		categories = categories.trim();

		LogicRepositoryType logicInputXML = new LogicRepositoryType();
		PropertyType logicProperty = new PropertyType();

		logicProperty.setFormula(formula);
		logicProperty.setLogic(prop.getProperty().getType());

		logicInputXML.setClient("RVMonitor");
		logicInputXML.setEvents(mopSpec.getEventStr());
		logicInputXML.setCategories(categories);
		logicInputXML.setProperty(logicProperty);

		LogicRepositoryData logicInputData = new LogicRepositoryData(logicInputXML);

		ByteArrayOutputStream logicOutput_OutputStream;
		LogicRepositoryData logicOutputData;
		LogicRepositoryType logicOutputXML;

		try {
			logicOutput_OutputStream = connectToServer(logicInputData);

			logicOutputData = new LogicRepositoryData(logicOutput_OutputStream);
			logicOutputXML = logicOutputData.getXML();
		} catch (Exception e) {
			if (Main.debug)
				e.printStackTrace();
			throw new RVMException("Logic Engine Error: " + e.getMessage());
		}

		if (logicOutputXML.getProperty() == null) {
			if (logicOutputXML.getMessage() != null) {
				String errStr = "";
				for (String errMsg : logicOutputXML.getMessage()) {
					if (errStr.length() != 0)
						errStr += "\n";
					errStr += errMsg.trim();
				}
				throw new RVMException(errStr);
			} else {
				throw new RVMException("Wrong Logic Repository Output");
			}
		}

		return logicOutputXML;
	}

	public static ByteArrayOutputStream connectToServer(LogicRepositoryData logicInputData) throws Exception {
		ByteArrayInputStream logicInput_InputStream = logicInputData.getInputStream();
		ByteArrayOutputStream logicInput_OutputStream = logicInputData.getOutputStream();
		String logicinputstr = logicInput_OutputStream.toString();

		if (verbose) {
			System.out.println("== send to logic repository ==");
			System.out.print(logicinputstr);
			System.out.println("");
		}

		ByteArrayOutputStream logicOutput_OutputStream;
		if (LogicRepositoryConnector.serverName.compareTo("local") == 0) {
//            System.out.println(System.getProperty("java.class.path"));
			Class<?> logicClass = Class.forName("com.runtimeverification.rvmonitor.logicrepository.Main");
			ClassLoader loader = logicClass.getClassLoader();
			String logicClassPath = loader.getResource("com/runtimeverification/rvmonitor/logicrepository/Main.class").toString();

			boolean isLogicRepositoryInJar = false;
			String logicJarFilePath = "";
			String logicPackageFilePath = "";

//            System.out.println("LogicClassPath: " + logicClassPath);
			if (logicClassPath.endsWith(".jar!/com/runtimeverification/rvmonitor/logicrepository/Main.class") && logicClassPath.startsWith("jar:")) {
				isLogicRepositoryInJar = true;

				logicJarFilePath = logicClassPath.substring("jar:file:".length(), logicClassPath.length() - "!/com/runtimeverification/rvmonitor/logicrepository/Main.class".length());
				logicJarFilePath = Tool.polishPath(logicJarFilePath);
//                System.out.println("LogicJarPath: " + logicJarFilePath);
			} else {
				logicPackageFilePath = logicClassPath.substring("file:".length(), logicClassPath.length() - "/Main.class".length());
				logicPackageFilePath = Tool.polishPath(logicPackageFilePath);
			}

			String logicPluginFarFilePath = new File(logicJarFilePath).getParent() + File.separator + "plugins" + File.separator + "*";
//            System.out.println("LogicPluginPath: " + logicPluginFarFilePath);

			if (isLogicRepositoryInJar) {
				String mysqlConnectorPath = new File(Main.jarFilePath).getParent() + "/lib/mysql-connector-java-3.0.9-stable-bin.jar";
//                System.out.println("MysqlConPath: " + mysqlConnectorPath);
				String executePath = new File(logicJarFilePath).getParent();
//                System.out.println("ExecutePath: " + executePath);

				String[] cmdarray = {
						"java",
						"-cp",
						Tool.polishPath(logicJarFilePath) + File.pathSeparator + logicPluginFarFilePath + File.pathSeparator + mysqlConnectorPath
								+ File.pathSeparator + new File(Main.jarFilePath).getParent() + "/scala-library.jar", "com.runtimeverification.rvmonitor.logicrepository.Main" };

				logicOutput_OutputStream = executeProgram(cmdarray, executePath, logicInput_InputStream);
			} else {
				String executePath = new File(logicPackageFilePath).getParent();
				String mysqlConnectorPath = executePath + File.separator + "lib" + File.separator + "mysql-connector-java-3.0.9-stable-bin.jar";
				String scalaPath = executePath + File.separator + "lib" + File.separator + "scala-library.jar";

				String[] cmdarray = { "java", "-cp",
						Tool.polishPath(executePath) + File.pathSeparator + mysqlConnectorPath + File.pathSeparator + scalaPath, "com.runtimeverification.rvmonitor.logicrepository.Main" };

				logicOutput_OutputStream = executeProgram(cmdarray, executePath, logicInput_InputStream);
			}
		} else {
			URL url;
			URLConnection urlConn;
			DataOutputStream printout;
			BufferedReader input;
			String result = "";

			if (LogicRepositoryConnector.serverName.compareTo("default") == 0) {
				if(Configuration.getServerAddr() == null)
					throw new RVMException("remote server address configuration file not found");
				
				url = new URL(Configuration.getServerAddr());
			} else
				url = new URL(LogicRepositoryConnector.serverName);

			// URL connection channel.
			urlConn = url.openConnection();
			((HttpURLConnection) urlConn).setRequestMethod("POST");
			// Let the run-time system (RTS) know that we want input.
			urlConn.setDoInput(true);
			// Let the RTS know that we want to do output.
			urlConn.setDoOutput(true);
			// No caching, we want the real thing.
			urlConn.setUseCaches(false);
			// Specify the content type.
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// Send POST output.
			printout = new DataOutputStream(urlConn.getOutputStream());
			String request = URLEncoder.encode("input", "UTF-8") + "=" + URLEncoder.encode(logicinputstr, "UTF-8");
			printout.writeBytes(request);
			printout.flush();
			printout.close();

			// Get response data.
			input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String str;
			while (null != ((str = input.readLine()))) {
				result += str + "\n";
			}
			input.close();

			logicOutput_OutputStream = new ByteArrayOutputStream();
			logicOutput_OutputStream.write(result.getBytes());
		}

		if (verbose) {
			System.out.println("== result from logic repository ==");
			System.out.println(logicOutput_OutputStream);
			System.out.println("");
		}

		return logicOutput_OutputStream;
	}

	static public ByteArrayOutputStream executeProgram(String[] cmdarray, String path, ByteArrayInputStream input) throws RVMException {
		Process child;
		String output = "";
//        System.out.println("Execute options: \n\n\n" + cmdarray.toString() + "\n" + path + "\n\n");
		try {
			child = Runtime.getRuntime().exec(cmdarray, null, new File(path));
			OutputStream out = child.getOutputStream();
			BufferedOutputStream bs = new BufferedOutputStream(out);

			StreamGobbler errorGobbler = new StreamGobbler(child.getErrorStream());
			StreamGobbler outputGobbler = new StreamGobbler(child.getInputStream());

			byte[] b = new byte[input.available()];
			input.read(b);

			bs.write(b);
			bs.flush();
			out.close();
			bs.close();

			errorGobbler.start();
			outputGobbler.start();

			child.waitFor();

			errorGobbler.join();
			outputGobbler.join();

			output = outputGobbler.text + errorGobbler.text;

			ByteArrayOutputStream logicOutput = new ByteArrayOutputStream();
			logicOutput.write(output.getBytes());

			return logicOutput;
		} catch (Exception e) {
			System.out.println(e);
			if (cmdarray.length > 0)
				throw new RVMException("Cannot execute the program: " + cmdarray[0]);
			else
				throw new RVMException("Cannot execute the program: ");
		}
	}

}
