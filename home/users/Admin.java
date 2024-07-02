package users;

import exceptions.*;
import utils.*;
import concerts.*;
import java.io.*;
import java.util.*;

/**
 * The admin class extends the User class and provides specific functionalities tailored for admin option.
 * This includes navigating through menus to view concert timings, update ticket costs, view all bookings, and total payments received.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class Admin extends User {

    /** Default constructor of admin class.
    * @param fileOp File Operation to be accessed to run the program.
    * @throws IOException Check for availabillity for File reading operations.
    * @throws InvalidLineException Check for a minimum number of fixed data points.
    * @throws InvalidFormatException Check for an issue in data points.
    */
    public Admin(FileOperation fileOp) throws IOException, InvalidLineException, InvalidFormatException {
        super(fileOp);
    }

    /**
     * Shows the main menu for the admin. It overrides the mainMenu method from the User class.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    @Override
    public void mainMenu() throws IOException, InvalidLineException, InvalidFormatException {
        boolean runLoop = true;
        while (runLoop) {
            displayMenu();
            int input = Constants.SCANNER.nextInt();

            switch (input) {
                case Constants.ADMIN_CONCERT_DETAILS:
                    concertDetails.concertTimings();
                    break;
                case Constants.ADMIN_UPDATE_COSTS:
                    updateTicketCosts();
                    break;
                case Constants.ADMIN_VIEW_BOOKING:
                    viewBookings();
                    break;
                case Constants.ADMIN_TOTAL_PAYMENT:
                    viewTotalPayments();
                    break;
                case Constants.EXIT:
                    exit();
                    runLoop = false;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT_MESSAGE);
            }
        }
    }

    /**
     * Shows the welcome message for the admin. It overrides the displayWelcomeMessage method from the User class.
     */
    @Override
    public void displayWelcomeMessage() {
        System.out.println("Welcome to Ticket Management System Admin Mode.");
    }

    /**
     * Shows the exit message for the admin. It overrides the exit method from the User class.
     */
    @Override
    protected void exit() {
        System.out.println("Exiting admin mode");
    }

    /**
     * Shows the main menu options for the admin. It overrides the displayMenu method from the User class.
     */
    @Override
    protected void displayMenu() {
        System.out.println("Select an option to get started!");
        System.out.println("Press 1 to view all the concert details");
        System.out.println("Press 2 to update the ticket costs");
        System.out.println("Press 3 to view booking details");
        System.out.println("Press 4 to view total payment received for a concert");
        System.out.println("Press 5 to exit");
        System.out.print("> ");
    }

    /**
     * Update ticket costs for selected concert
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private void updateTicketCosts() throws IOException, InvalidLineException, InvalidFormatException {
        System.out.println(Constants.CONCERT_SELECT_MESSAGE);
        concertDetails.concertTimings();
        System.out.print("> ");
        int concertIndex = Constants.SCANNER.nextInt();

        if (concertIndex == 0) {
            return;
        }

        Concert selectedConcert = concertDetails.getConcerts().get(concertIndex - 1);
        concertDetails.showTicketCosts(selectedConcert);

        System.out.print("Enter the zone : VIP, SEATING, STANDING: ");
        String zoneType = Constants.SCANNER.next(); 
        System.out.println();
        System.out.print("Left zone price: ");
        double leftPrice = Constants.SCANNER.nextDouble();
        System.out.print("Centre zone price: ");
        double middlePrice = Constants.SCANNER.nextDouble();
        System.out.print("Right zone price: ");
        double rightPrice = Constants.SCANNER.nextDouble();

        concertDetails.updateTicketCosts(selectedConcert, zoneType, leftPrice, middlePrice, rightPrice);
        concertDetails.saveUpdatedTicketPrices(selectedConcert);
    }

    /**
     * Shows all bookings for the selected concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private void viewBookings() throws IOException, InvalidLineException, InvalidFormatException {
        System.out.println(Constants.CONCERT_SELECT_MESSAGE);
        Concert selectedConcert = selectConcert();
        if (selectedConcert == null) {
            return;
        }
        bookingsHandler.viewAllBookingsForConcert(selectedConcert);
    }

    /**
     * Shows total payments received for the selected concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private void viewTotalPayments() throws IOException, InvalidLineException, InvalidFormatException {
        System.out.println(Constants.CONCERT_SELECT_MESSAGE);
        Concert selectedConcert = selectConcert();
        if (selectedConcert == null) {
            return;
        }
        bookingsHandler.calculateTotalPayments(selectedConcert);
    }

}

