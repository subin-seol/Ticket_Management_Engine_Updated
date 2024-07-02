package exceptions;

/**
 * Exception thrown when the data point is in incorrect format.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class InvalidFormatException extends Exception {

    /** Constructor of InvalidFormatException with the error mesage.
    * @param error message to print.
    */
    public InvalidFormatException(String message) {
        super(message);
    }
}

