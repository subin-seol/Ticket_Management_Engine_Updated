package concerts;

import utils.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import exceptions.InvalidFormatException;
import exceptions.InvalidLineException;

/**
 * Handle all booking details.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class BookingsHandler{
    private List<String[]> bookings;
    private FileOperation fileOp;

    /**
     * Default constructor of bookingsHandler class.
     * @param fileOp File Operation to be accessed to run the program.
     * @throws IOException Check for availabillity for File operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public BookingsHandler(FileOperation fileOp) throws IOException, InvalidLineException, InvalidFormatException {
        this.fileOp = fileOp;
        this.bookings = fileOp.getBookings();
    }

    /**
     * Manage all bookings 
     * @param venue Object containing venue
     * @param concertId Unique id for each concert
     * @throws IOException Check for availabillity for File writing operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void manageBookings(Venue venue, String concertId) throws IOException, InvalidLineException, InvalidFormatException {
        List<String> seatBookings = new ArrayList<>();
        for (String[] booking : bookings) {
            if (booking[3].equals(concertId)) {
                int totalTickets = Integer.parseInt(booking[4]);
                for (int i = 0; i < totalTickets; i++) {
                    String zoneType = booking[8 + i * 5];  
                    String rowNumber = booking[6 + i * 5]; 
                    String aisle = getAisleFromZoneType(zoneType, rowNumber);
                    int seat = Integer.parseInt(booking[7 + i * 5]); 
                    seatBookings.add(aisle + "-" + seat); 
                }
            }
        }
        venue.setSeatBookings(seatBookings);
    }

    /**
     * Shows booking details for the concert.
     * @param customerId Unique id for each customer.
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void viewBookingDetails(String customerId, Concert concert) throws IOException, InvalidLineException, InvalidFormatException {
        bookings = fileOp.getBookings();
        List<String[]> bookingDetails = new ArrayList<>();

        for (String[] booking : bookings) {
            if (booking[1].equals(customerId) && booking[3].equals(concert.getConcertId())) {
                bookingDetails.add(booking);
            }
        }

        printBookingDetails(bookingDetails, concert);
    }

    /**
     * Shows all bookings.
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void viewAllBookingsForConcert(Concert concert) throws IOException, InvalidLineException, InvalidFormatException {
        List<String[]> bookingDetails = new ArrayList<>();

        for (String[] booking : bookings) {
            if (booking[3].equals(concert.getConcertId())) {
                bookingDetails.add(booking);
            }
        }

        printBookingDetails(bookingDetails, concert);
    }

    /**
     * Prints booking details.
     * @param bookingDetails Booking details list.
     * @param concert Object containing concert.
     */
    public void printBookingDetails(List<String[]> bookingDetails, Concert concert) {
        if (bookingDetails.isEmpty()) {
            System.out.println("No Bookings found for this concert\n");
            return;
        }

        System.out.println("Bookings");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf(Constants.BOOKING_LIST_FORMATTER, "Id", "Concert Date", "Artist Name", "Timing", "Venue Name", "Seats Booked", "Total Price");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");

        for (String[] booking : bookingDetails) {
            int seatsBooked = Integer.parseInt(booking[4]);
            double totalPrice = 0;

            for (int i = 0; i < seatsBooked; i++) {
                totalPrice += Double.parseDouble(booking[9 + i * 5]);
            }

            System.out.printf(Constants.BOOKING_LIST_FORMATTER, booking[0], concert.getConcertDate(), concert.getArtistName(), concert.getTiming(), concert.getVenueName(), booking[4], totalPrice);
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------------");

        System.out.println("\nTicket Info");
        for (String[] booking : bookingDetails) {
            System.out.println("############### Booking Id: " + booking[0] + " ####################");
            System.out.printf(Constants.TICKET_INFO_FORMATTER, "Id", "Aisle Number", "Seat Number", "Seat Type", "Price");
            System.out.println("##################################################");

            int seatsBooked = Integer.parseInt(booking[4]);
            for (int i = 0; i < seatsBooked; i++) {
                System.out.printf(Constants.TICKET_INFO_FORMATTER, booking[5 + i * 5], booking[6 + i * 5], booking[7 + i * 5], booking[8 + i * 5], Double.parseDouble(booking[9 + i * 5]));
            }

            System.out.println("##################################################\n");
        }
        System.out.println();
    }

    /**
     * Calculate total payments.
     * @param concert Object containing concert.
     */
    public void calculateTotalPayments(Concert concert) {
        double totalPayment = 0;
        for (String[] booking : bookings) {
            if (booking[3].equals(concert.getConcertId())) {
                int seatsBooked = Integer.parseInt(booking[4]);
                for (int i = 0; i < seatsBooked; i++) {
                    totalPayment += Double.parseDouble(booking[9 + i * 5]); 
                }
            }
        }
        System.out.println("Total Price for this concert is AUD " + totalPayment);
    }

    /**
     * Get aisle from the zone type.
     * @param zoneType The zone type of the seat.
     * @param rowNumber The row number of the seat.
     * @return aisle 
     */
    private String getAisleFromZoneType(String zoneType, String rowNumber) {
        if (zoneType.equalsIgnoreCase("STANDING")) {
            return "T" + rowNumber; 
        } else if (zoneType.equalsIgnoreCase("SEATING")) {
            return "S" + rowNumber; 
        } else {
            return "V" + rowNumber; 
        }
    }
}

