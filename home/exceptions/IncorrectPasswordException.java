package exceptions;

/**
 * Exception thrown when an incorrect password passed do not match the password in customer list.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class IncorrectPasswordException extends Exception {

    /** Constructor of IncorrectPasswordException with the error mesage.
    * @param error message to print.
    */
    public IncorrectPasswordException(String message) {
        super(message);
    }
}

