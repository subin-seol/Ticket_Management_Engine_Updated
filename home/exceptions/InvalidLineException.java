package exceptions;

/**
 * Exception thrown when each line in the file has the required number of data points
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class InvalidLineException extends Exception {

    /** Constructor of InvalidLineException with the error mesage.
    * @param error message to print.
    */
    public InvalidLineException(String message) {
        super(message);
    }
}

