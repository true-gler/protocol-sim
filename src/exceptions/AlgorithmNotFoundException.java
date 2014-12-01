package exceptions;

public class AlgorithmNotFoundException extends Exception{
	

	public String toString(){ 
		   System.out.println("The given algorithm was not found");
	       return ("The given algorithm was not found") ;
	    }

}
