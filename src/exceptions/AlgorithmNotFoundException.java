package exceptions;

/**
 * Used to check if the specified algorithmname is found in the package algorithm
 * Used in combination with Reflections
 * @author Thomas
 *
 */
public class AlgorithmNotFoundException extends Exception{
	

	public String toString(){ 
		   System.out.println("The given algorithm was not found, check your spelling or the existance of the class");
	       return ("The given algorithm was not found, check your spelling or the existance of the class") ;
	    }

}
