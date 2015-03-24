package exceptions;



/**
 * Used to check if the specified protocol-name is found in the package protocol
 * Used in combination with Reflections
 * @author Thomas
 *
 */
public class ProtocolNotFoundException extends Exception{
	

	public String toString(){ 
		   System.out.println("The given protcol was not found, check your spelling or the existance of the class");
	       return ("The given protcol was not found, check your spelling or the existance of the class") ;
	    }

}
