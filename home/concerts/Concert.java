package concerts;

import java.util.List;

/**
 * Represents a concert event including details such as date, artist, timing, venue name, and seating information.
 * and managing the count of booked seats.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class Concert {
    private String concertId;
    private String concertDate;
    private String timing;
    private String artistName;
    private String venueName;
    private int totalSeats;
    private int bookedSeats;
    private List<String[]> ticketPrices;
    private double leftZonePrice;
    private double middleZonePrice;
    private double rightZonePrice;
    private Venue venue;

    /**
    * Default constructor for concert.
    * @param concertId Unique id for each concert.
    * @param date The date of the concert.
    * @param artistName The name of the artist performing.
    * @param timing The timing of the concert.
    * @param venueName The name of the venue.
    * @param venue The Venue object associated with the concert.
    */
    public Concert(String concertId, String concertDate, String artistName, String timing, String venueName, Venue venue) {
        this.concertId = concertId;
        this.concertDate = concertDate;
        this.timing = timing;
        this.artistName = artistName;
        this.venueName = venueName;
        this.venue = venue;
        this.totalSeats = venue.calculateTotalSeats();
        this.bookedSeats = 0;
    }

    /**
     * Returns the concert id
     * @return the concert id
     */
    public String getConcertId() {
        return concertId;
    }

    /**
     * Returns the concert date
     * @return the concert date in string format yyyy-MM-dd
     */
    public String getConcertDate() {
        return concertDate;
    }

    /**
     * Returns the concert timing
     * @return the concert timing
     */
    public String getTiming() {
        return timing;
    }

    /**
     * Returns the artist name
     * @return the artist name
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Returns the venue name
     * @return the venue name
     */
    public String getVenueName() {
        return venueName;
    }

    /**
     * Returns the number of total seats
     * @return the number of total seats
     */
    public int getTotalSeats() {
        return totalSeats;
    }

    /**
     * Returns the number of booked seats
     * @return the number of booked seats
     */
    public int getBookedSeats() {
        return bookedSeats;
    }

    /**
     * Returns the venue
     * @return the venue of an object venue
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * Returns the ticket prices
     * @return the ticket prices list
     */
    public List<String[]> getTicketPrices() {
        return ticketPrices;
    }

    /**
     * Returns the seat prices for specific zone and section
     * @return the seat price
     */
    public double getPriceForSeat(int seatNumber, String zoneType) {
        List<Integer> sections = venue.getSections();
        int leftSectionSize = sections.get(0);
        int middleSectionSize = sections.get(1);
        int rightSectionSize = sections.get(2);

        if (zoneType.equalsIgnoreCase("STANDING")) {
            if (seatNumber <= leftSectionSize) {
                return getSectionPrice("STANDING", "Left");
            } else if (seatNumber <= leftSectionSize + middleSectionSize) {
                return getSectionPrice("STANDING", "Middle");
            } else {
                return getSectionPrice("STANDING", "Right");
            }
        } else if (zoneType.equalsIgnoreCase("SEATING")) {
            if (seatNumber <= leftSectionSize) {
                return getSectionPrice("SEATING", "Left");
            } else if (seatNumber <= leftSectionSize + middleSectionSize) {
                return getSectionPrice("SEATING", "Middle");
            } else {
                return getSectionPrice("SEATING", "Right");
            }
        } else {
            if (seatNumber <= leftSectionSize) {
                return getSectionPrice("VIP", "Left");
            } else if (seatNumber <= leftSectionSize + middleSectionSize) {
                return getSectionPrice("VIP", "Middle");
            } else {
                return getSectionPrice("VIP", "Right");
            }
        }
    }

    /**
     * Returns the seat prices for specific section, left, middle and right
     * @param zoneType the zone type (VIP, STANDING, SEATING)
     * @param section the seat section (left, middle, right)
     * @return the seat price
     */
    private double getSectionPrice(String zoneType, String section) {
        for (String[] price : ticketPrices) {
            if (price[0].equalsIgnoreCase(zoneType)) {
                switch (section) {
                    case "Left":
                        return Double.parseDouble(price[1]);
                    case "Middle":
                        return Double.parseDouble(price[2]);
                    case "Right":
                        return Double.parseDouble(price[3]);
                }
            }
        }
        return 0.0;
    }

    /**
     * Returns the seat prices for left zone
     * @param zoneType the zone type (VIP, STANDING, SEATING)
     * @return the seat price of left zone
     */
    public double getLeftZonePrice(String zoneType) {
        for (String[] zone : ticketPrices) {
            if (zone[0].equalsIgnoreCase(zoneType)) {
                return Double.parseDouble(zone[1]);
            }
        }
        return 0.0;
    }

    /**
     * Returns the seat prices for middle zone
     * @param zoneType The zone type (VIP, STANDING, SEATING)
     * @return the seat price of middle zone
     */
    public double getMiddleZonePrice(String zoneType) {
        for (String[] zone : ticketPrices) {
            if (zone[0].equalsIgnoreCase(zoneType)) {
                return Double.parseDouble(zone[2]);
            }
        }
        return 0.0;
    }

    /**
     * Returns the seat prices for right zone
     * @param zoneType The zone type (VIP, STANDING, SEATING)
     * @return the seat price of right zone
     */
    public double getRightZonePrice(String zoneType) {
        for (String[] zone : ticketPrices) {
            if (zone[0].equalsIgnoreCase(zoneType)) {
                return Double.parseDouble(zone[3]);
            }
        }
        return 0.0;
    }

    /**
     * Sets the ticket prices
     * @param ticketPrices the list of ticket prices
     */
    public void setTicketPrices(List<String[]> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    /**
     * Sets the left zone ticket prices
     * @param leftZonePrice the left zone seating price
     */
    public void setLeftZonePrice(double leftZonePrice) {
        this.leftZonePrice = leftZonePrice;
    }

    /**
     * Sets the middle zone ticket prices
     * @param middleZonePrice the middle zone seating price
     */
    public void setMiddleZonePrice(double middleZonePrice) {
        this.middleZonePrice = middleZonePrice;
    }

    /**
     * Sets the right zone ticket prices
     * @param rightZonePrice the right zone seating price
     */
    public void setRightZonePrice(double rightZonePrice) {
        this.rightZonePrice = rightZonePrice;
    }
}

