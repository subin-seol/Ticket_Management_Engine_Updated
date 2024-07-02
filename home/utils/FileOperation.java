package utils;

import exceptions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * A File Operating class that handles all file reading and writing, implemented from the FileOperationsInterface.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class FileOperation implements FileOperationsInterface {
    private static String customerFilePath;
    private static String concertFilePath;
    private static String bookingsFilePath;
    private static List<String> venueFilePaths = new ArrayList<>();

    private static List<String[]> customers;
    private static List<String[]> concerts;
    private static List<String[]> bookings;
    private static Map<String, List<String>> venueLayouts = new HashMap<>();

    /** Default constructor of File Operation.
    * @param args command line arguments passed to the program.
    */
    public FileOperation(String[] args) throws InvalidLineException {}

    /**
     * Load admin file path from command line argument.
     * @param args command line arguments passed to the program.
     * @throws IOException Check for availabillity for File operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void loadAdminFile(String[] args) throws IOException, InvalidLineException, InvalidFormatException {
        if (args.length < 4) {
            System.out.println("Insufficient arguments for Admin mode.");
        } else {
            customerFilePath = args[1];
            concertFilePath = args[2];
            bookingsFilePath = args[3];
            for (int i = 4; i < args.length; i++) {
                venueFilePaths.add(args[i]);
            }
        }
        loadFile(customerFilePath, concertFilePath, bookingsFilePath);
    }

    /**
     * Load customer file path from command line argument.
     * @param args command line arguments passed to the program.
     * @throws IOException Check for availabillity for File operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    public void loadCustomerFile(String[] args) throws IOException, InvalidLineException, InvalidFormatException {
        if (args.length >= 4) {
            int startIndex = 1;

            if (args.length > 4 && !args[startIndex].contains(".csv")) {
                startIndex += 2;  // Skip customer id and password
            }

            customerFilePath = args[startIndex];
            concertFilePath = args[startIndex + 1];
            bookingsFilePath = args[startIndex + 2];

            for (int i = startIndex + 3; i < args.length; i++) {
                venueFilePaths.add(args[i]);
            }
        } else {
            System.out.println("Insufficient arguments for Customer mode.");
        }
        loadFile(customerFilePath, concertFilePath, bookingsFilePath);
    }

    /**
     * Read data from a CSV file and returns it as a list of string arrays.
     * @param filepath The path of the CSV file.
     * @return A list of string arrays, each representing one line of the CSV file.
     * @throws IOException Check for availabillity for File reading and writing operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    @Override
    public List<String[]> readCSV(String filePath) throws IOException, InvalidLineException, InvalidFormatException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + " (No such file or directory)");
        }

        List<String[]> dataList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                try {
                    validateData(data, filePath);
                    dataList.add(data);
                } catch (InvalidLineException | InvalidFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return dataList;
    }

    /**
     * Write data to a CSV file.
     * @param filepath The path of the CSV file.
     * @param data The data list to append to the file.
     * @param append Append the data if true.
     * @return A list of string arrays, each representing one line of the CSV file.
     * @throws IOException Check for availabillity for File writing operations.
     */
    @Override
    public void writeCSV(String filePath, List<String[]> data, boolean append) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, append)) {
            for (String[] record : data) {
                writer.write(String.join(",", record));
                writer.write("\n");
            }
        }
    }

    /**
     * Read data from a TXT file and returns it as a list of string arrays.
     * @param filepath The path of the TXT file.
     * @return A list of string arrays, each representing one line of the TXT file.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    @Override
    public List<String> readTXT(String filePath) throws IOException, InvalidFormatException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + " (No such file or directory)");
        }

        List<String> validLines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty()) {
                    if (validateZoneType(trimmedLine)) {
                        validLines.add(line); 
                    } else {
                        System.out.println("Invalid Zone Type. Skipping this line.");
                    }
                }
            }
        }
        return validLines;
    }

    /**
     * Write data to a TXT file.
     * @param filepath The path of the TXT file.
     * @param data The data list to append to the file.
     * @param append Append the data if true.
     * @return A list of string arrays, each representing one line of the TXT file.
     * @throws IOException Check for availabillity for File writing operations.
     */
    @Override
    public void writeTXT(String filePath, List<String> lines, boolean append) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Load all files.
     * @param customerFilePath The file path to the customer data.
     * @param concertFilePath The file path to the concert data.
     * @param bookingsFilePath The file path to the bookings data.
     * @throws IOException Check for availabillity for File operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    private void loadFile(String customerFilePath, String concertFilePath, String bookingsFilePath) throws IOException, InvalidLineException, InvalidFormatException {
        customers = readCSV(customerFilePath);
        concerts = readCSV(concertFilePath);
        bookings = readCSV(bookingsFilePath);
    }

    /**
    * Validate data from the files to handle exceptions.
    * @param data The list of data .
    * @param filePath The specific file path.
    * @throws InvalidLineException Check for a minimum number of fixed data points.
    * @throws InvalidFormatException Check for an issue in data points.
    */
    private void validateData(String[] data, String filePath) throws InvalidLineException, InvalidFormatException {
        if (filePath.contains("customer") && data.length < 3) {
            throw new InvalidLineException("Invalid Customer Files. Skipping this line.");
        } else if (filePath.contains("concert") && data.length < 8) {
            throw new InvalidLineException("Invalid Concert Files. Skipping this line.");
        } else if (filePath.contains("bookings")) {
            if (data.length < 5) {
                throw new InvalidLineException("Invalid booking Files. Skipping this line.");
            }
        }

        if (filePath.contains("concert")) {
            checkNumeric(data[0], "Concert Id is in incorrect format. Skipping this line.");
        } else if (filePath.contains("customer")) {
            checkNumeric(data[0], "Customer Id is in incorrect format. Skipping this line.");
        }

        if (filePath.contains("bookings")) {
            checkNumeric(data[0], "Booking Id is in incorrect format. Skipping this line.");
            checkNumeric(data[4], "Incorrect Number of Tickets. Skipping this line.");

            int totalTickets = Integer.parseInt(data[4]);
            if (totalTickets <= 0) {
                throw new InvalidFormatException("Incorrect Number of Tickets. Skipping this line.");
            }
            
            int expectedLength = 5 + totalTickets * 5;
            if (data.length < expectedLength) {
                throw new InvalidLineException("Invalid booking Files. Skipping this line.");
            }
        }
    } 

    /**
    * Check the format of the line to handle exceptions.
    * @param line The line to check for validation.
    * @param errorMessage The error message to print.
    * @throws InvalidFormatException Check for an issue in data points.
    */
    private void checkNumeric(String line, String errorMessage) throws InvalidFormatException {
        if (line == null || !line.matches("\\d+")) {  
            throw new InvalidFormatException(errorMessage);
        }
    }

    /**
    * Check the format of the data to handle exceptions.
    * @param line The line to check.
    * @param errorMessage The error message to print.
    * @return true if the zone type is valid.
    */
    private boolean validateZoneType(String line) {
        if (line.isEmpty()) {
            return false;  
        }
        String zoneType = line.substring(0, 1);  
        List<String> validZoneTypes = Arrays.asList("V", "S", "T");
        return validZoneTypes.contains(zoneType); 
    }

    /**
     * Returns the customer data
     * @return the customer data in list
     */
    public List<String[]> getCustomers() {
        return customers;
    }

    /**
     * Returns the concert data
     * @return the concert data in list
     */
    public List<String[]> getConcerts() {
        return concerts;
    }

    /**
     * Returns the bookings data
     * @return the bookings data in list
     */
    public List<String[]> getBookings() {
        return bookings;
    }

    /**
     * Returns the venue layout by specific venue name.
     * @param venueName The name of the venue.
     * @return the layout of the venue
     */
    public List<String> getVenueLayout(String venueName) {
        return venueLayouts.getOrDefault(venueName, venueLayouts.get("default"));
    }

    /**
     * Return the venue file path
     * @param venueName The name of the venue.
     * @return the venue file path.
     */
    public String getVenueFilePath(String venueName) {
        for (String venueFilePath : venueFilePaths) {
            if (venueFilePath.contains(venueName.toLowerCase())) {
                return venueFilePath;
            }
        }
        return Constants.DEFAULT_VENUE;  
    }

    /**
     * Return the customer file path.
     * @return the customer file path.
     */
    public String getCustomerFilePath() {
        return customerFilePath;
    }

    /**
     * Return the concert file path.
     * @return the concert file path.
     */
    public String getConcertFilePath() {
        return concertFilePath;
    }

    /**
     * Return the bookings file path.
     * @return the bookings file path.
     */
    public String getBookingsFilePath() {
        return bookingsFilePath;
    }

    /**
     * Extract the venue name from the file name.
     * @param venueName The name of the venue.
     * @return the name of the venue.
     */
    private String extractVenueName(String venueFilePath) {
        String name = venueFilePath.substring(venueFilePath.lastIndexOf('/') + 1).replace("venue_", "").replace(".txt", "");
        return name.toLowerCase();
    }
}

