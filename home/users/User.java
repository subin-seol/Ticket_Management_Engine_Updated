package users;

import exceptions.*;
import utils.*;
import concerts.*;
import java.io.*;

/**
 * Abstract base class for users of the ticket management system.
 * @author Subin Seol
 * @studentId 1086852
 * @email sseol@student.unimelb.edu.au
 */
public abstract class User {
    protected FileOperation fileOp;
    protected ConcertDetails concertDetails;
    protected BookingsHandler bookingsHandler;

    /**
    * Default constructor for user class.
    * @param fileOp File Operation to be accessed to run the program.
    * @throws IOException Check for availabillity for File reading operations.
    * @throws InvalidLineException Check for a minimum number of fixed data points.
    * @throws InvalidFormatException Check for an issue in data points.
    */
    public User(FileOperation fileOp) throws IOException, InvalidLineException, InvalidFormatException {
        this.fileOp = fileOp;
        this.concertDetails = new ConcertDetails(fileOp);
        this.bookingsHandler = new BookingsHandler(fileOp);
    }

    /**
     * Implements concert selection.
     * @return A selected concert.
     */
    protected Concert selectConcert() throws IOException, InvalidLineException, InvalidFormatException {
        concertDetails.concertTimings();
        System.out.print("> ");
        int concertIndex = Constants.SCANNER.nextInt();

        if (concertIndex == 0) {
            exit();
            return null;
        }

        return concertDetails.getConcerts().get(concertIndex - 1);
    }

    /**
     * Define the main menu specific to the type of user.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public abstract void mainMenu() throws IOException, InvalidLineException, InvalidFormatException;

    /**
     * Print the welcome message specific to the type of user.
     */
    public abstract void displayWelcomeMessage();

    /**
     * Prints the main menu specific to the type of user.
     */
    protected abstract void displayMenu();

    /**
     * Prints the exit message specific to the type of user.
     */
    protected abstract void exit();
}

