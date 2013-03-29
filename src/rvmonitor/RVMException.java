/*
 * Created on Aug 17, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package rvmonitor;

/**
 * @author fengchen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RVMException extends Exception {
	private static final long serialVersionUID = 2145299315023315212L;
	public RVMException(Exception e){
		super("RV Monitor Expection:" + e.getMessage());
	}
	public RVMException(String str){
		super(str);
	}
}
