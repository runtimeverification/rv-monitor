package com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.reftree;

import java.util.ArrayList;

import com.runtimeverification.rvmonitor.java.rvj.output.RVMVariable;
import com.runtimeverification.rvmonitor.java.rvj.output.combinedaspect.indexingtree.IndexingTree;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMParameter;
import com.runtimeverification.rvmonitor.java.rvj.parser.ast.mopspec.RVMonitorSpec;

public class RefTree {
	RVMVariable name;

	public String type;

	public ArrayList<RVMonitorSpec> properties = new ArrayList<RVMonitorSpec>();
	public ArrayList<RVMonitorSpec> generalProperties = new ArrayList<RVMonitorSpec>();
	
	public IndexingTree hostIndexingTree = null;
	
	// rv-monitor cannot assume that 'thisJoinPoint' exists, unlike JavaMOP.
	// The joinpoint ids can be passed from the .aj code but it seems using 1-level
	// cache based on these ids is not that beneficial.
	private final boolean useJoinPointId = false;

	public RefTree(String aspectName, RVMParameter param) {
		this.type = param.getType().toString();

		String typeStr = type;
		int dim;

		if (typeStr.endsWith("]")) {
			int firstBracket = typeStr.indexOf("[");
			int lastBracket = typeStr.lastIndexOf("[");

			dim = lastBracket - firstBracket + 1;

			typeStr = typeStr.substring(0, firstBracket);

			typeStr += "Array";
			
			if (dim > 1)
				typeStr += dim;
		}

		this.name = new RVMVariable(aspectName + "_" + typeStr + "_RefMap");
	}

	public void addProperty(RVMonitorSpec spec) {
		properties.add(spec);
		if(spec.isGeneral())
			generalProperties.add(spec);
	}
	
	public void setHost(IndexingTree indexingTree){
		hostIndexingTree = indexingTree;
	}

	public String get(RVMVariable tempRef, RVMParameter p) {
		String ret = "";
		RVMVariable name;
		
		if(hostIndexingTree == null)
			name = this.name;
		else
			name = hostIndexingTree.getName();
		
		ret += tempRef + " = " + name + ".findOrCreateWeakRef(" + p.getName();
		
		if(this.useJoinPointId && properties.size() > 1)
			ret += ", thisJoinPoint.getStaticPart().getId()";
		
		ret += ");\n";
		
		return ret;
	}

	public String getRefNonCreative(RVMVariable tempRef, RVMParameter p) {
		String ret = "";
		RVMVariable name;
		
		if(hostIndexingTree == null)
			name = this.name;
		else
			name = hostIndexingTree.getName();

		ret += tempRef + " = " + name + ".findWeakRef(" + p.getName();

		if(this.useJoinPointId && properties.size() > 1)
			ret += ", thisJoinPoint.getStaticPart().getId()";
		
		ret += ");\n";

		return ret;
	}

	public boolean isTagging() {
		return generalProperties.size() != 0;
	}

	public int getTagNumber(RVMonitorSpec spec) {
		if (generalProperties.size() <= 1)
			return -1;
		else
			return generalProperties.indexOf(spec);
	}

	public String getResultType() {
		String ret = "";

		ret += "Cached";
		if (generalProperties.size() == 0)
			ret += "";
		else if (generalProperties.size() == 1)
			ret += "Tag";
		else
			ret += "MultiTag";
		ret += "WeakReference";

		return ret;
	}

	public String getType() {
		String ret = "";
		
		if (hostIndexingTree == null) {
			if (generalProperties.size() == 0)
				ret = "com.runtimeverification.rvmonitor.java.rt.table.BasicRefMap";
			else if (generalProperties.size() == 1)
				ret = "com.runtimeverification.rvmonitor.java.rt.table.TagRefMap";
			else
				ret = "com.runtimeverification.rvmonitor.java.rt.table.MultiTagRefMap";
		}
		else {
			ret = hostIndexingTree.getRefTreeType();
		}

		return ret;
	}
	
	public RVMVariable getName() {
		return name;
	}

	public String toString() {
		String ret = "";

		ret += "static " + this.getType() + " " + name + " = ";	
		if(hostIndexingTree == null){
			if(generalProperties.size() > 1)
				ret += "new " + getType() + "(" + generalProperties.size() + ");\n";
			else
				ret += "new " + getType() + "();\n";
		} else {
			ret += hostIndexingTree.getName() + ";\n";
		}

		return ret;
	}

	public String reset() {
		String ret = "";

		ret += name;
		ret += " = ";
		if(hostIndexingTree == null){
			if(generalProperties.size() > 1)
				ret += "new " + getType() + "(" + generalProperties.size() + ");\n";
			else
				ret += "new " + getType() + "();\n";
		} else {
			ret += hostIndexingTree.getName() + ";\n";
		}

		return ret;
	}

}
