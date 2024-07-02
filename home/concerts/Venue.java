package concerts;

import utils.*;
import exceptions.InvalidFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A class for representing venue (seat layout) and number of total seats and booked seats.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class Venue {
    private String venueName;
    private FileOperation fileOp;
    private List<String> layout;
    private List<String> seatBookings;

    /**
     * Default constructor of venue class.
     * @param venueName The name of the venue.
     * @param fileOp File Operation to be accessed to run the program.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public Venue(String venueName, FileOperation fileOp) throws IOException, InvalidFormatException {
        this.venueName = venueName;
        this.fileOp = fileOp;
        this.layout = fileOp.readTXT(fileOp.getVenueFilePath(venueName));
        this.seatBookings = new ArrayList<>(); 
    }

    /**
     * Calculate the total seats number from the venue layout
     * @return The total number of seats.
     */
    public int calculateTotalSeats() {
        int totalSeats = 0;
        for (String row : layout) {
            totalSeats += row.split("\\[").length - 1; 
        }
        return totalSeats;
    }

    /**
     * Calculate the number of booked seats. 
     * @param bookings The list of bookings.
     * @param concertId Unique Id for each concert.
     * @return The number of booked seats.
     */
    public int calculateSeatsBooked(List<String[]> bookings, String concertId) {
        int seatsBooked = 0;
        for (String[] booking : bookings) {
            if (booking.length > 3 && booking[3].equals(concertId) && booking.length > 4) {
                int ticketsBooked = Integer.parseInt(booking[4]);
                seatsBooked += ticketsBooked;
            }
        }
        return seatsBooked;
    }

    /**
     * Show the layout of the venue with updated booking status. 
     */
    public void viewLayout() {
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
        String lastZoneType = null; 

        for (String row : layout) {
            // Get the aisle / zonetype from the layout
            String currentZoneType = getAisleFromLine(row).substring(0, 1);

            // Check if the current zonetype is different to the last zonetype
            if (lastZoneType != null && !lastZoneType.equals(currentZoneType)) {
                System.out.println(); 
            }
            lastZoneType = currentZoneType; 

            Matcher matcher = pattern.matcher(row);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                int seatNumber = Integer.parseInt(matcher.group(1));
                String aisle = getAisleFromLine(row);
                boolean isBooked = seatBookings.contains(aisle + "-" + seatNumber);
                matcher.appendReplacement(sb, isBooked ? "[X]" : "[" + seatNumber + "]");
            }
            matcher.appendTail(sb);
            System.out.println(sb.toString());
        }
    }

    /**
     * Implements seat bookings in layout
     * @param aisle The aisle in venue layout.
     * @param startSeat The start seat booked.
     * @param numberOfSeats The number of seats booked
     * @return true if the booking is successful
     */
    public boolean bookSeatsinLayout(String aisle, int startSeat, int numberOfSeats) {
        for (int i = startSeat; i < startSeat + numberOfSeats; i++) {
            if (seatBookings.contains(aisle + "-" + i)) {
                System.out.println("Some of the seats are already booked.");
                return false;
            }
        }
        for (int i = startSeat; i < startSeat + numberOfSeats; i++) {
            seatBookings.add(aisle + "-" + i);
        }
        updateLayout(aisle, startSeat, numberOfSeats);
        return true;
    }

    /**
     * Update layout based on the seat booking.
     * @param aisle The aisle in venue layout.
     * @param startSeat The start seat booked.
     * @param numberOfSeats The number of seats booked
     */
    private void updateLayout(String aisle, int startSeat, int numberOfSeats) {
        for (int i = 0; i < layout.size(); i++) {
            String row = layout.get(i);
            if (row.startsWith(aisle)) {
                for (int j = startSeat; j < startSeat + numberOfSeats; j++) {
                    row = row.replace("[" + j + "]", "[X]");
                }
                layout.set(i, row);
                break;
            }
        }
    }

    /**
     * Return aisle from the line of the layout.
     * @param line Represents the row.
     * @return aisle from the layout.
     */
    private String getAisleFromLine(String line) {
        return line.split(" ")[0];
    }

    /**
     * Return the number of seats in each section, left, right and middle.
     * @return The number of seats in each section.
     */
    public List<Integer> getSections() {
        if (layout.isEmpty()) {
            return List.of(0, 0, 0);
        }

        String firstRow = layout.get(0);
        String[] parts = firstRow.split(" ");
        int left = 0, middle = 0, right = 0;

        boolean foundLeft = false, foundMiddle = false, foundRight = false;

        for (int i = 1; i < parts.length - 1; i++) {
            if (parts[i].startsWith("[") && parts[i].endsWith("]")) {
                int seatCount = parts[i].split("\\[").length - 1;
                if (!foundLeft) {
                    left = seatCount;
                    foundLeft = true;
                } else if (foundLeft && !foundMiddle) {
                    middle = seatCount;
                    foundMiddle = true;
                } else if (foundMiddle) {
                    right = seatCount;
                    foundRight = true;
                }
            }
        }

        return List.of(left, middle, right);
    }

    /**
     * Return the latest list of booked seats.
     * @return the latest list of booked seats.
     */
    public List<String> getSeatBookings() {
        return seatBookings;
    }

    /**
     * Set the new bookings to the list.
     * @return the latest list of booked seats.
     */
    public void setSeatBookings(List<String> seatBookings) {
        this.seatBookings = seatBookings;
    }
}

