package exceptions;

public class ProtocolNotFoundException extends Exception{
	

	public String toString(){ 
		   System.out.println("The given protcol was not found, check your spelling or the existance of the class");
	       return ("The given protcol was not found, check your spelling or the existance of the class") ;
	    }

}
