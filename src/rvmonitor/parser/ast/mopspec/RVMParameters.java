package rvmonitor.parser.ast.mopspec;

import java.util.*;

import rvmonitor.parser.ast.aspectj.BaseTypePattern;

public class RVMParameters implements Iterable<RVMParameter> {

	ArrayList<RVMParameter> parameters;

	public RVMParameters() {
		this.parameters = new ArrayList<RVMParameter>();
	}

	public RVMParameters(List<RVMParameter> params) {
		this.parameters = new ArrayList<RVMParameter>();

		if(params != null){
			for(RVMParameter param : params){
				this.add(param);
			}
		}
	}

	public RVMParameters(RVMParameters params) {
		this.parameters = new ArrayList<RVMParameter>();

		if(params != null){
			for(RVMParameter param : params){
				this.add(param);
			}
		}
	}

	public void add(RVMParameter p) {
		if (this.getParam(p.getName()) == null) {
			RVMParameter p2 = p;
			if (p.getType().getOp().charAt(p.getType().getOp().length() - 1) == '+') {
				BaseTypePattern t2 = new BaseTypePattern(p.getType().getBeginLine(), p.getType().getBeginColumn(), p.getType().getOp().substring(0,
						p.getType().getOp().length() - 1));
				p2 = new RVMParameter(p.getBeginLine(), p.getBeginColumn(), t2, p.getName());
			}
			this.parameters.add(p2);
		}
	}

	public void addAll(RVMParameters set) {
		if (set == null)
			return;
		for (RVMParameter p : set.parameters) {
			if (this.getParam(p.getName()) == null)
				this.parameters.add(p);
		}
	}

	public void addAll(List<RVMParameter> set) {
		if (set == null)
			return;
		for (RVMParameter p : set) {
			if (this.getParam(p.getName()) == null) {
				RVMParameter p2 = p;
				if (p.getType().getOp().charAt(p.getType().getOp().length() - 1) == '+') {
					BaseTypePattern t2 = new BaseTypePattern(p.getType().getBeginLine(), p.getType().getBeginColumn(), p.getType().getOp().substring(0,
							p.getType().getOp().length() - 1));
					p2 = new RVMParameter(p.getBeginLine(), p.getBeginColumn(), t2, p.getName());
				}
				this.parameters.add(p2);
			}
		}
	}

	/**
	 * Find a parameter with the given name
	 * 
	 * @param name
	 *            a parameter name
	 */
	public RVMParameter getParam(String name) {
		RVMParameter ret = null;

		for (RVMParameter param : this.parameters) {
			if (param.getName().compareTo(name) == 0) {
				ret = param;
				break;
			}
		}
		return ret;
	}

	static public RVMParameters unionSet(RVMParameters set1, RVMParameters set2) {
		RVMParameters ret = new RVMParameters();

		if (set1 != null)
			ret.addAll(set1);

		if (set2 != null)
			ret.addAll(set2);
		return ret;
	}

	static public RVMParameters intersectionSet(RVMParameters set1, RVMParameters set2) {
		RVMParameters ret = new RVMParameters();

		for (RVMParameter p1 : set1) {
			for (RVMParameter p2 : set2) {
				if (p1.getName().compareTo(p2.getName()) == 0) {
					ret.add(p1);
					break;
				}
			}
		}
		return ret;
	}

	public RVMParameters sortParam(RVMParameters set) {
		RVMParameters ret = new RVMParameters();

		for (RVMParameter p : this.parameters) {
			if (set.contains(p))
				ret.add(p);
		}

		for (RVMParameter p : set.parameters) {
			if (!ret.contains(p))
				ret.add(p);
		}
		return ret;
	}

	public boolean contains(RVMParameter p) {
		return (this.getParam(p.getName()) != null);
	}

	public boolean contains(RVMParameters set) {
		for (RVMParameter p : set.parameters) {
			if (!this.contains(p))
				return false;
		}
		return true;
	}

	public int size() {
		return this.parameters.size();
	}

	/**
	 * Compare a list of parameters with this one to see if they contains the
	 * same parameters
	 * 
	 * @param set
	 *            MoPParameters
	 */
	public boolean equals(Object set) {
		if (!(set instanceof RVMParameters))
			return false;
		
		return this.equals((RVMParameters) set);
	}

	public boolean equals(RVMParameters set) {
		if(set == null)
			return false;
		if (this.size() != set.size())
			return false;
		return this.contains(set) && set.contains(this);
	}
	
	public boolean matchTypes(RVMParameters set){
		if(this.size() != set.size())
			return false;
		for (int i = 0; i < this.parameters.size(); i++) {
			if (this.parameters.get(i).getType().getOp().equals(set.get(i).getType().getOp())){
				return false;
			}
		}				
		
		return true;
	}
	

	public RVMParameter get(int i) {
		if (i < 0 || i >= this.parameters.size())
			return null;
		return this.parameters.get(i);
	}
	
	ArrayList<RVMParameter> lexico = null;
	
	public RVMParameter get_lexicographic(int i) {
		if (i < 0 || i >= this.parameters.size())
			return null;
		
		if(this.lexico == null || this.lexico.size() != this.parameters.size()){
			this.lexico = new ArrayList<RVMParameter>(this.parameters);
			Collections.sort(lexico, new Comparator<RVMParameter>() {
				public int compare(RVMParameter p1, RVMParameter p2) {
					int ret = p1.getType().getOp().compareTo(p2.getType().getOp());
					if(ret != 0)
						return ret;
					ret = p1.getName().compareTo(p2.getName());
					return ret;
				}
			});
		}
		
		return this.lexico.get(i);
	}


	public Iterator<RVMParameter> iterator() {
		return this.parameters.iterator();
	}

	public String parameterString() {
		String ret = "";

		for (RVMParameter param : this.parameters) {
			ret += ", " + param.getName();
		}
		if (ret.length() != 0)
			ret = ret.substring(2);

		return ret;
	}

	public String parameterStringIn(RVMParameters all) {
		String ret = "";

		for (RVMParameter param : all) {
			if(this.contains(param)){
				ret += ", " + param.getName();
			} else {
				ret += ", null"; 
			}
			
		}
		
		if (ret.length() != 0)
			ret = ret.substring(2);

		return ret;
	}
	
	public String parameterDeclString() {
		String ret = "";

		for (RVMParameter param : this.parameters) {
			ret += ", " + param.getType() + " " + param.getName();
		}
		if (ret.length() != 0)
			ret = ret.substring(2);

		return ret;
	}
	
	public String parameterInvokeString() {
		String ret = "";

		for (RVMParameter param : this.parameters) {
			ret += ", " + param.getName();
		}
		if (ret.length() != 0)
			ret = ret.substring(2);

		return ret;
	}

	public String parameterStringUnderscore() {
		String ret = "";

		for (RVMParameter param : this.parameters) {
			ret += "_" + param.getName();
		}
		if (ret.length() != 0)
			ret = ret.substring(1);

		return ret;
	}


	public int getIdnum(RVMParameter p) {
		for (int i = 0; i < this.parameters.size(); i++) {
			RVMParameter param = this.parameters.get(i);
			
			if (param.getName().compareTo(p.getName()) == 0) {
				return i;
			}
		}
		return -1;
	}
	
	public int hashCode() {
		int code = 0;
				
		for (RVMParameter param : this.parameters) {
			code ^= param.hashCode();
		}
		
		return code;
	}

	public String toString() {
		return parameters.toString();
	}
	
	public List<RVMParameter> toList(){
		return parameters;
	}
	
}
