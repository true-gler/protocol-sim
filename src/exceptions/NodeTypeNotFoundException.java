/**
 * 
 */
package exceptions;

/**
 * @author Thomas
 *
 */
public class NodeTypeNotFoundException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toString(){ 
		   System.out.println("The given nodetype was not found");
	       return ("The given nodetype was not found") ;
	    }
}
