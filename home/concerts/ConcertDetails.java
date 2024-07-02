package concerts;

import utils.*;
import exceptions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Manages a multiple concerts, accessing all concert details.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class ConcertDetails {
    private List<Concert> concerts;
    private List<String[]> customers;
    private List<String[]> concertsData;
    private List<String[]> bookings;
    private FileOperation fileOp;

    /**
    * Default constructor for concert.
    * @param fileOp File Operation to be accessed to run the program.
    */
    public ConcertDetails(FileOperation fileOp) throws IOException, InvalidLineException, InvalidFormatException {
        this.fileOp = fileOp;
        this.concerts = new ArrayList<>();
        loadConcerts();
    }


    /**
     * Load concert selected by concertId
     * @param concertId Unique id for each concert.
     * @return concert identified by Id.
     */
    public Concert getConcertById(String concertId) {
        for (Concert concert : concerts) {
            if (concert.getConcertId().equals(concertId)) {
                return concert;
            }
        }
        return null;
    }

    /**
     * Shows the concert timings.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void concertTimings() throws IOException, InvalidLineException, InvalidFormatException {
        bookings = fileOp.getBookings();
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf(Constants.SHOW_TIMING_FORMATTER, "#", "Date", "Artist Name", "Timing", "Venue Name", "Total Seats", "Seats Booked", "Seats Left");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        for (Concert concert : concerts) {
            Venue venue = concert.getVenue();
            int totalSeats = venue.calculateTotalSeats();
            int seatsBooked = venue.calculateSeatsBooked(bookings, concert.getConcertId());
            int seatsLeft = totalSeats - seatsBooked;
            System.out.printf(Constants.SHOW_TIMING_FORMATTER, concert.getConcertId(), concert.getConcertDate(), concert.getArtistName(), concert.getTiming(), concert.getVenueName(), totalSeats, seatsBooked, seatsLeft);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Shows the ticket costs.
     * @param concert Object containing concert.
     */
    public void showTicketCosts(Concert concert) {
        for (String[] zoneInfo : concert.getTicketPrices()) {
            String zoneName = zoneInfo[0];
            System.out.printf("---------- %8s ----------%n", zoneName);
            System.out.printf("Left Seats:   %.1f%n", Double.parseDouble(zoneInfo[1]));
            System.out.printf("Center Seats: %.1f%n", Double.parseDouble(zoneInfo[2]));
            System.out.printf("Right Seats:  %.1f%n", Double.parseDouble(zoneInfo[3]));
            System.out.println("------------------------------");
        }
    }

    /**
     * Load concerts information.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private void loadConcerts() throws IOException, InvalidLineException, InvalidFormatException {
        concertsData = fileOp.getConcerts();
        for (String[] concertData : concertsData) {
            Venue venue = new Venue(concertData[4], fileOp);
            Concert concert = new Concert(concertData[0], concertData[1], concertData[3], concertData[2], concertData[4], venue);
            concert.setTicketPrices(parseTicketPrices(concertData));
            concerts.add(concert);
        }
    }

    /**
     * Parse the price data in the list.
     * @param concertData List of concert data.
     * @return ticket prices list.
     */
    private List<String[]> parseTicketPrices(String[] concertData) {
        List<String[]> ticketPrices = new ArrayList<>();
        for (int i = 5; i < concertData.length; i++) {
            ticketPrices.add(concertData[i].split(":"));
        }
        return ticketPrices;
    }

    /**
     * Add concert to the concert list
     * @param concert Object containing concert.
     */
    public void addConcert(Concert concert) {
        concerts.add(concert);
    }

    /**
     * Update concert ticket costs
     * @param concert Object containing concert.
     * @param zoneType The zone type of the seat.
     * @param leftPrice The price of the seat in the left section.
     * @param middlePrice The price of the seat in the middle section.
     * @param rightPrice The price of the seat in the right section.
     */
    public void updateTicketCosts(Concert concert, String zoneType, double leftPrice, double middlePrice, double rightPrice) {
        switch (zoneType.toUpperCase()) {
            case "VIP":
            case "SEATING":
            case "STANDING":
                concert.setLeftZonePrice(leftPrice);
                concert.setMiddleZonePrice(middlePrice);
                concert.setRightZonePrice(rightPrice);
                break;
            default:
                System.out.println("Invalid zone type.");
        }
    }

    /**
     * Update concert file with the updated price.
     * @param concert Object containing concert.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void saveUpdatedTicketPrices(Concert concert) throws IOException, InvalidLineException, InvalidFormatException {
        concertsData = fileOp.getConcerts();
        for (String[] concertData : concertsData) {
            if (concertData[0].equals(concert.getConcertId())) {
                for (int i = 0; i < concert.getTicketPrices().size(); i++) {
                    String[] zoneInfo = concert.getTicketPrices().get(i);

                    zoneInfo[1] = String.valueOf(Double.parseDouble(zoneInfo[1]));
                    zoneInfo[2] = String.valueOf(Double.parseDouble(zoneInfo[2]));
                    zoneInfo[3] = String.valueOf(Double.parseDouble(zoneInfo[3]));
                    concertData[5 + i] = String.join(":", zoneInfo);
                }
                break;
            }
        }
        fileOp.writeCSV(fileOp.getConcertFilePath(), concertsData, false);
    }

    /**
     * Return the concerts list.
     * @return the concerts list.
     */
    public List<Concert> getConcerts() {
        return concerts;
    }
}

