package com.runtimeverification.rvmonitor.java.rvj.output.codedom.type;

import java.util.ArrayList;
import java.util.List;

public class CodeType {
	private final String pkgname;
	private final String clsname;
	private final int dimension;
	private final List<CodeType> generics;
	
	public String getPackageName() {
		return this.pkgname;
	}
	
	public String getClassName() {
		return this.clsname;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public CodeType(CodeType o) {
		this(o.pkgname, o.clsname, o.dimension, o.generics);
	}
	
	public CodeType(String clsname) {
		this(null, clsname);
	}
	
	public CodeType(String pkgname, String clsname) {
		this(pkgname, clsname, 0);
	}
	
	public CodeType(String pkgname, String clsname, List<CodeType> generics) {
		this(pkgname, clsname, 0, generics);
	}
	
	public CodeType(String pkgname, String clsname, int dimension) {
		this(pkgname, clsname, dimension, null);
	}
	
	public CodeType(String pkgname, String clsname, int dimension, List<CodeType> generics) {
		this.pkgname = pkgname;
		this.clsname = clsname;
		this.dimension = dimension;
		this.generics = generics == null ? new ArrayList<CodeType>() : generics;
		
		this.validate();
	}
	
	private void validate() {
		// 'pkgname' can be null if the type is native or it is accessed within
		// the same package.

		if (this.clsname == null)
			throw new IllegalArgumentException();
		if (this.dimension < 0)
			throw new IllegalArgumentException();
		if (this.generics == null)
			throw new IllegalArgumentException();
	}
	
	private final static CodeType nativeVoid;
	private final static CodeType nativeInteger;
	private final static CodeType nativeLong;
	private final static CodeType nativeBoolean;
	private final static CodeType nativeObject;
	private final static CodeType nativeString;
	
	static {
		nativeVoid = new CodeType(null, "void");
		nativeInteger = new CodeType(null, "int");
		nativeLong = new CodeType(null, "long");
		nativeBoolean = new CodeType(null, "boolean");
		
		// The following types are not primitive, but they are worth being
		// treated specially.
		nativeObject = new CodeType("java.lang", "Object");
		nativeString = new CodeType("java.lang", "String");
	}
	
	public static CodeType foid() {
		return nativeVoid;
	}
	public static CodeType integer() {
		return nativeInteger;
	}
	public static CodeType rong() {
		return nativeLong;
	}
	public static CodeType bool() {
		return nativeBoolean;
	}
	public static CodeType object() {
		return nativeObject;
	}
	public static CodeType string() {
		return nativeString;
	}

	public boolean isInteger() {
		return this == nativeInteger;
	}
	public boolean isLong() {
		return this == nativeLong;
	}
	public boolean isBool() {
		return this == nativeBoolean;
	}
	
	CodeType createArrayType(int dimension) {
		return new CodeType(this.pkgname, this.clsname, dimension);
	}
	
	public String getJavaType() {
		return this.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CodeType))
			return false;
		
		CodeType that = (CodeType)obj;
		{
			boolean l = this.pkgname != null;
			boolean r = that.pkgname != null;
			if (l != r)
				return false;
			if (l) {
				if (!this.pkgname.equals(that.pkgname))
					return false;
			}
		}
		if (!this.clsname.equals(that.clsname))
			return false;
		if (this.dimension != that.dimension)
			return false;
		if (this.generics.size() != that.generics.size())
			return false;
		for (int i = 0; i < this.generics.size(); ++i) {
			CodeType l = this.generics.get(i);
			CodeType r = that.generics.get(i);
			if (!l.equals(r))
				return false;
		}

		return true;
	}
	
	@Override
	public int hashCode() {
		return this.pkgname.hashCode() + this.clsname.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.clsname);
		if (this.generics.size() > 0) {
			s.append('<');
			boolean first = true;
			for (CodeType g : this.generics) {
				if (first) first = false;
				else s.append(", ");
				s.append(g.toString());
			}
			s.append('>');
		}
		
		for (int i = 0; i < this.dimension; ++i)
			s.append("[]");
		return s.toString();
	}
}
