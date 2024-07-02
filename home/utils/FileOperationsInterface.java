package utils;

import java.io.IOException;
import java.util.List;
import exceptions.InvalidLineException;
import exceptions.InvalidFormatException;

/**
 * Interface for file operation for reading and writing to CSV and text files.
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public interface FileOperationsInterface {

    /**
     * Read data from a CSV file and returns it as a list of string arrays.
     * @param filepath The path of the CSV file.
     * @return A list of string arrays, each representing one line of the CSV file.
     * @throws IOException Check for availabillity for File reading and writing operations.
     * @throws InvalidLineException Check for a minimum number of fixed data points.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    List<String[]> readCSV(String filepath) throws IOException, InvalidLineException, InvalidFormatException;
    
    /**
     * Write data to a CSV file.
     * @param filepath The path of the CSV file.
     * @param data The data list to append to the file.
     * @param append Append the data if true.
     * @return A list of string arrays, each representing one line of the CSV file.
     * @throws IOException Check for availabillity for File writing operations.
     */
    void writeCSV(String filePath, List<String[]> data, boolean append) throws IOException;

    /**
     * Read data from a TXT file and returns it as a list of string arrays.
     * @param filepath The path of the TXT file.
     * @return A list of string arrays, each representing one line of the TXT file.
     * @throws IOException Check for availabillity for File reading operations.
     * @throws InvalidFormatException Check for an issue in data points.
     */
    List<String> readTXT(String filePath) throws IOException, InvalidFormatException;

    /**
     * Write data to a TXT file.
     * @param filepath The path of the TXT file.
     * @param data The data list to append to the file.
     * @param append Append the data if true.
     * @return A list of string arrays, each representing one line of the TXT file.
     * @throws IOException Check for availabillity for File writing operations.
     */
    void writeTXT(String filePath, List<String> lines, boolean append) throws IOException;
}

