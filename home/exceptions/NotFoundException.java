package exceptions;

/**
 * Exception thrown when customer id is not found in the customer list.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class NotFoundException extends Exception {

    /** Constructor of NotFoundException with the error mesage.
    * @param error message to print.
    */
    public NotFoundException(String message) {
        super(message);
    }
}

