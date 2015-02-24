package exceptions;

public class AlgorithmNotFoundException extends Exception{
	

	public String toString(){ 
		   System.out.println("The given algorithm was not found, check your spelling or the existance of the class");
	       return ("The given algorithm was not found, check your spelling or the existance of the class") ;
	    }

}
