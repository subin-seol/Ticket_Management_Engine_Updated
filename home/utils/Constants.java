package utils;

import java.util.Scanner;

/**
 * Constants class for definining all the constants.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class Constants {

    /**
     * Provides a constant scanner object that can be used to take input
     */
    public static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Provides a constant default venue file path
     */
    public static final String DEFAULT_VENUE = "assets/venue_default.txt";

    /**
     * Provides a constant show timing formatter
     */
    public static final String SHOW_TIMING_FORMATTER = "%-5s%-15s%-15s%-15s%-30s%-15s%-15s%-15s%n";

    /**
     * Provides a constant booking list formatter
     */
    public static final String BOOKING_LIST_FORMATTER = "%-5s%-15s%-15s%-10s%-15s%-15s%-10s%n";
    
    /**
     * Provides a constant ticket info formatter
     */
    public static final String TICKET_INFO_FORMATTER = "%-5s%-15s%-15s%-10s%-10s%n";

    /**
     * Provides a constant customer menu option to view ticket costs
     */
    public static final int CUSTOMER_TICKET_COSTS = 1;

    /**
     * Provides a constant customer menu option to view seat layout
     */
    public static final int CUSTOMER_SEATS_LAYOUT = 2;

    /**
     * Provides a constant customer menu option to book seats
     */
    public static final int CUSTOMER_BOOK_SEATS = 3;

    /**
     * Provides a constant customer menu option to view bookings
     */
    public static final int CUSTOMER_VIEW_BOOKING = 4;

    /**
     * Provides a constant admin menu option to view concert details
     */
    public static final int ADMIN_CONCERT_DETAILS = 1;

    /**
     * Provides a constant admin menu option to update ticket costs
     */
    public static final int ADMIN_UPDATE_COSTS = 2;

    /**
     * Provides a constant admin menu option to view all bookings
     */
    public static final int ADMIN_VIEW_BOOKING = 3;

    /**
     * Provides a constant admin menu option to view total payments received for concert
     */
    public static final int ADMIN_TOTAL_PAYMENT = 4;

    /**
     * Provides a constant menu option to exit the program
     */
    public static final int EXIT = 5;

    /**
     * Provides a constant message for selecting a concert
     */
    public static final String CONCERT_SELECT_MESSAGE = "Select a concert or 0 to exit";

    /**
     * Provides a constant message for invalid input
     */
    public static final String INVALID_INPUT_MESSAGE = "Invalid Input";


}

