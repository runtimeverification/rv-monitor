package com.runtimeverification.rvmonitor.java.testsuite;

import com.runtimeverification.rvmonitor.java.rvj.util.StreamGobbler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ProgramCompileFunctor implements TestCaseFunctor {
	public FunctorResult apply(TestCase testCase) {
		String rvmonitorrtLibPath = null;
		if (Main.isJarFile)
			rvmonitorrtLibPath = new File(Main.jarFilePath).getParent() + "/rt.jar";
		else
			rvmonitorrtLibPath = Main.rvmonitorDir + "/lib/rt.jar";

		FunctorResult ret = new FunctorResult();

		for (TestCaseProgDir testCaseProg : testCase.testing_programs) {
			copyToMopDir(testCase.basepath + testCase.path, testCaseProg.dirName,
					testCase.specFiles.get(0).monitor_filename);
			ret.addSubCase(testCaseProg.dirName);
//			System.out.println(testCaseProg.dirName);

			for (File javaFile : testCaseProg.javaFiles) {
				String javaFilePath = null;
				try {
					javaFilePath = javaFile.getCanonicalPath();
				} catch (Exception e) {
					ret.addStdErr(testCaseProg.dirName, e.getMessage());
					ret.success = false;
					break;
				}

				if (!javaFile.exists()) {
					ret.addStdErr(testCaseProg.dirName, "  [ERROR] test program does not exist.");
					ret.success = false;
					break;
				}

				String origDirPath = testCase.basepath + File.separator + testCase.path + File.separator + testCaseProg.dirName;
				String monitoredDirPath =  // origDirPath;
						testCase.basepath + File.separator + testCase.path + File.separator + testCaseProg.dirName + "_RVM";

				File origDir = new File(origDirPath);
				if (!origDir.exists()) {
					boolean status = origDir.mkdirs();
					if (!status) {
						ret.addStdErr(testCaseProg.dirName, "  [ERROR] original directory " + origDirPath
								+ "does not exist and it could not be created either.");
						ret.success = false;
						continue;
					}
				}
				File monitoredDir = new File(monitoredDirPath);
				if (!monitoredDir.exists()) {
					boolean status = monitoredDir.mkdirs();
					if (!status) {
						ret.addStdErr(testCaseProg.dirName, "  [ERROR] _RVM directory " + monitoredDir
								+ "does not exist and it could not be created either.");
						ret.success = false;
						continue;
					}
				}

				String[] cmdarray = { "javac", "-cp", origDirPath +
						File.pathSeparator + rvmonitorrtLibPath,
						"-Xlint:unchecked", "-d", origDirPath, javaFilePath };

				try {
					Process child;
					String output = "";

					if (Main.Debug)
						System.out.println("ProgramCompile breakpoint 1");

//					System.out.println("Executing: '");
//					for (String x : cmdarray) System.out.print(x + " ");
//					System.out.println("'");
					child = Runtime.getRuntime().exec(cmdarray, null);

					if (Main.Debug)
						System.out.println("ProgramCompile breakpoint 2");

					StreamGobbler errorGobbler = new StreamGobbler(child.getErrorStream());
					StreamGobbler outputGobbler = new StreamGobbler(child.getInputStream());

					errorGobbler.start();
					outputGobbler.start();

					//child.waitFor();

					outputGobbler.join();
	  			    errorGobbler.join();

					if (Main.Debug)
						System.out.println("ProgramCompile breakpoint 3");

					ret.addStdOut(testCaseProg.dirName, outputGobbler.text);
					ret.addStdErr(testCaseProg.dirName, errorGobbler.text);

					if (Main.Debug)
						System.out.println("ProgramCompile breakpoint 4");

					if (output.indexOf("error") == -1) {
						String origianlClassPath = javaFile.getCanonicalPath().substring(0, javaFile.getCanonicalPath().length() - 5)
								+ ".class";
						String monitoredClassPath = monitoredDirPath + File.separator
								+ javaFile.getName().substring(0, javaFile.getName().length() - 5) + ".class";

						if(new File(origianlClassPath).exists())
							Main.copy(origianlClassPath, monitoredClassPath);
					} else {
						ret.success = false;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					ret.addStdErr(testCaseProg.dirName, e.getMessage());
					ret.success = false;
					break;
				}
			}
			
			// maybe we should copy directories under the origDirPath to the monitoredDirPath
			
		}

		return ret;
	}

	private void copyToMopDir(String testCaseFilePath, String testFilePath,
							  String monitorFileName) {
		String mopDirPath = testCaseFilePath + File.separator + testFilePath +
				File.separator	+ "mop";
		File mopDir = new File(mopDirPath);

		// if the directory does not exist, create it
		if (!mopDir.exists())
		{
			boolean result = mopDir.mkdir();
			if(!result){
				System.err.println("Could not create directory " + mopDirPath);
			}

		} else if (!mopDir.isDirectory()) {
			System.err.println(mopDirPath + " is not a directory!");
		}  else if (!mopDir.canWrite()) {
			System.err.println(mopDirPath + " is not writable!");
		}
		String oldMonitorPath = testCaseFilePath + File.separator +
				monitorFileName;
		String newMonitorPath = mopDirPath + File.separator + monitorFileName;

//		System.out.println("Moving from " + oldMonitorPath + " to " +
//				newMonitorPath);
		File oldMonitor = new File(oldMonitorPath);
		File newMonitor = new File(newMonitorPath);
		if (newMonitor.exists()) {
			newMonitor.delete();
		}


		try {
			copyFile(oldMonitor, newMonitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyFile(File input, File output) throws IOException {
		FileInputStream inputStream = new FileInputStream(input);
		FileOutputStream outputStream = new FileOutputStream(output);
		try {
			FileChannel fc = inputStream.getChannel();
			FileChannel oc = outputStream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			oc.write(bb);
		} finally {
			inputStream.close();
			outputStream.close();
		}
	}
}
