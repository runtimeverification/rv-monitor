package com.runtimeverification.rvmonitor.logicpluginshells;

import java.util.List;
import java.util.Properties;

public class LogicPluginShellResult {
    public Properties properties;
    public List<String> startEvents;

    @Override
    public String toString() {
        return "properties=" + properties + "\n" + "startEvents=" + startEvents;
    }
}
