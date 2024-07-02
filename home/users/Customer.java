package users;

import exceptions.*;
import utils.*;
import concerts.*;
import java.io.*;
import java.util.*;

/**
 * The customer class extends the User class and provides specific functionalities tailored for customer option.
 * This includes navigating through menus to view concert timings, ticket costs, seating layouts, book seats, and bookings.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class Customer extends User {
    private List<String[]> customers;
    private String customerId;
    private String name;
    private String password;

    /**
     * Default constructor of customer class.
     * @param customerId Unique id for each customer.
     * @param name Name of the customer.
     * @param password Unique password for the customer.
     * @param fileOp File Operation to be accessed to run the program.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public Customer(String customerId, String name, String password, FileOperation fileOp) throws IOException, InvalidLineException, InvalidFormatException {
        super(fileOp);
        this.customerId = customerId;
        this.name = name;
        this.password = password;
        this.customers = fileOp.getCustomers();
    }

    /**
     * Shows the main menu for the customer. It overrides the mainMenu method from the User class.
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    @Override
    public void mainMenu() throws IOException, InvalidLineException, InvalidFormatException {
        while (true) {
            System.out.println(Constants.CONCERT_SELECT_MESSAGE);
            Concert selectedConcert = selectConcert();
            if (selectedConcert == null) {
                return; // Exit customer mode
            }

            boolean runLoop = true;
            while (runLoop) {
                displayMenu();
                int input = Constants.SCANNER.nextInt();
                Constants.SCANNER.nextLine(); 
                switch (input) {
                    case Constants.CUSTOMER_TICKET_COSTS:
                        concertDetails.showTicketCosts(selectedConcert);
                        break;
                    case Constants.CUSTOMER_SEATS_LAYOUT:
                        viewSeatsLayout(selectedConcert);
                        break;
                    case Constants.CUSTOMER_BOOK_SEATS:
                        bookSeats(selectedConcert);
                        break;
                    case Constants.CUSTOMER_VIEW_BOOKING:
                        bookingsHandler.viewBookingDetails(customerId, selectedConcert);
                        break;
                    case Constants.EXIT:
                        System.out.println("Exiting this concert");
                        runLoop = false;
                        break;
                    default:
                        System.out.println(Constants.INVALID_INPUT_MESSAGE + ".");
                }
            }
        }
    }

    /**
     * Shows the welcome message for the customer. It overrides the displayWelcomeMessage method from the User class.
     */
    @Override
    public void displayWelcomeMessage() {
        System.out.println("Welcome " + this.name + " to Ticket Management System");
    }

    /**
     * Shows the exit message for the customer. It overrides the exit method from the User class.
     */
    @Override
    public void exit() {
        System.out.println("Exiting customer mode");
    }

    /**
     * Shows the main menu options for the customer. It overrides the displayMenu method from the User class.
     */
    @Override
    protected void displayMenu() {
        System.out.println("Select an option to get started!");
        System.out.println("Press 1 to look at the ticket costs");
        System.out.println("Press 2 to view seats layout");
        System.out.println("Press 3 to book seats");
        System.out.println("Press 4 to view booking details");
        System.out.println("Press 5 to exit");
        System.out.print("> ");
    }

    /**
     * Implements authentication of customer from the command line arguments.
     * @param concertId Unique id for each customer.
     * @param password Unique password for the customer.
     * @return true if the customer information is valid from the customers list.
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public boolean signIn(String customerId, String password) throws IOException, NotFoundException, InvalidLineException, IncorrectPasswordException, InvalidFormatException {
        for (String[] customer : customers) {
            if (customer[0].equals(customerId)) {
                if (customer[2].equals(password)) {
                    this.name = customer[1];
                    return true;
                } else {
                    throw new IncorrectPasswordException("Incorrect Password. Terminating Program");
                }
            }
        }
        throw new NotFoundException("Customer does not exist. Terminating Program");
    }

    /**
     * Prompt the user to provide a name and password. 
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void signUp() throws IOException, InvalidLineException, InvalidFormatException {
        System.out.print("Enter your name: ");
        this.name = Constants.SCANNER.nextLine();
        System.out.print("Enter your password: ");
        this.password = Constants.SCANNER.nextLine();
        this.customerId = autogenerateCustomerId();
        fileOp.writeCSV(fileOp.getCustomerFilePath(), Collections.singletonList(new String[] {this.customerId, this.name, this.password}), true);
    }

    /**
     * Autogenerate customer id.
     * @return true if the customer information is valid from the customers list.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private String autogenerateCustomerId() throws IOException, InvalidLineException, InvalidFormatException {
        int lastId = customers.stream().mapToInt(data -> Integer.parseInt(data[0])).max().orElse(0);
        return String.valueOf(lastId + 1);
    }

    /**
     * Show seating layout.
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private void viewSeatsLayout(Concert concert) throws IOException, InvalidLineException, InvalidFormatException {
        Venue venue = concert.getVenue();
        bookingsHandler.manageBookings(venue, concert.getConcertId());
        venue.viewLayout();
    }

    /**
     * Implements seats booking. Expect users to provide aisle number, seat number and number of seats.
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File writing operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private void bookSeats(Concert concert) throws IOException, InvalidLineException, InvalidFormatException {
        Venue venue = concert.getVenue();
        bookingsHandler.manageBookings(venue, concert.getConcertId());
        venue.viewLayout();

        System.out.print("Enter the aisle number: ");
        String aisle = Constants.SCANNER.next();

        System.out.print("Enter the seat number: ");
        int seatNumber = Constants.SCANNER.nextInt();
        System.out.print("Enter the number of seats to be booked: ");
        int numSeats = Constants.SCANNER.nextInt();

        String zoneType = aisle.substring(0, 1).equalsIgnoreCase("T") ? "STANDING" :
                          aisle.substring(0, 1).equalsIgnoreCase("S") ? "SEATING" : "VIP";

        if (venue.bookSeatsinLayout(aisle, seatNumber, numSeats)) {
            updateBookings(concert, aisle, seatNumber, numSeats, zoneType, fileOp.getBookings());
        } else {
            System.out.println("Failed to book seats.");
        }
    }

    /**
     * Updates the bookings list with new booking details.
     * @param concert Object containing concert.
     * @param aisle The aisle where the booking seats are located.
     * @param seatNumber The seat number for the booking.
     * @param numSeats The number of seats booked.
     * @param zoneType The zone type of the seats, VIP, STANDING, SEATING.
     * @param bookings The list of all bookings.
     * @throws IOException Check for an issue of writing a new booking information.
     */
    private void updateBookings(Concert concert, String aisle, int seatNumber, int numSeats, String zoneType, List<String[]> bookings) throws IOException {
        int bookingId = generateBookingId(bookings, customerId, concert.getConcertId());
        String[] newBooking = new String[5 + numSeats * 5];
        
        newBooking[0] = String.valueOf(bookingId);
        newBooking[1] = customerId;
        newBooking[2] = name;
        newBooking[3] = concert.getConcertId();
        newBooking[4] = String.valueOf(numSeats);

        for (int i = 0; i < numSeats; i++) {
            newBooking[5 + i * 5] = String.valueOf(i + 1);
            newBooking[6 + i * 5] = String.valueOf(aisle.substring(1)); 
            newBooking[7 + i * 5] = String.valueOf(seatNumber + i);
            newBooking[8 + i * 5] = zoneType;
            double price = concert.getPriceForSeat(seatNumber + i, zoneType);
            newBooking[9 + i * 5] = String.valueOf((int)price); 
        }

        bookings.add(newBooking);
        fileOp.writeCSV(fileOp.getBookingsFilePath(), bookings, false);
    }

    /**
     * Generate booking ID for new bookings.
     * @param bookings The list of all bookings.
     * @param customerId Unique id for each customer.
     * @param concertId Unique id for each concert.
     */
    private int generateBookingId(List<String[]> bookings, String customerId, String concertId) {
        int maxId = 0;
        for (String[] booking : bookings) {
            if (booking[1].equals(customerId) && booking[3].equals(concertId)) {
                int currentId = Integer.parseInt(booking[0]);
                if (currentId > maxId) {
                    maxId = currentId;
                }
            }
        }
        return maxId + 1;
    }
}

