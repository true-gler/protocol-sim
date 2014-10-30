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
		   System.out.println("The given Nodetype was not found");
	       return ("The given Nodetype was not found") ;
	    }
}
