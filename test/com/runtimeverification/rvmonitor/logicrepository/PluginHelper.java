package com.runtimeverification.rvmonitor.logicrepository;

import com.runtimeverification.rvmonitor.c.rvc.Main;
import com.runtimeverification.rvmonitor.logicrepository.LogicException;
import com.runtimeverification.rvmonitor.logicrepository.plugins.LogicPlugin;
import com.runtimeverification.rvmonitor.logicrepository.plugins.LogicPluginFactory;
import com.runtimeverification.rvmonitor.logicrepository.parser.logicrepositorysyntax.LogicRepositoryType;

public class PluginHelper {
	
	public static LogicRepositoryType runLogicPlugin(String logicName, LogicRepositoryType input) throws LogicException {
		System.out.println(System.getProperty("user.dir"));
		LogicPlugin plugin = LogicPluginFactory.findLogicPluginFromJar("lib/plugins/" + logicName.toUpperCase() + ".jar", logicName);
		if(plugin == null) {
			throw new RuntimeException("No such plugin: " + logicName);
		}
		return plugin.process(input);
	}
}