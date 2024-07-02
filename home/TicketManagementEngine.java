import utils.*;
import users.*;
import exceptions.*;
import java.io.*;

/**
 * Engine class for running the entire project
 * @author Subin Seol 1086852 sseol@student.unimelb.edu.au
 */
public class TicketManagementEngine {

    /**
    * Default constructor for TicketManagementEngine.
    */
    public TicketManagementEngine() {}

    /**
     * Main method to load user mode.
     * @param args command line arguments passed to the program. Expecting the user mode, and filepaths.
     */
    public static void main(String[] args) {
        TicketManagementEngine tme = new TicketManagementEngine();

        try {
            FileOperation fileOp = new FileOperation(args);

            if (args[0].equals("--customer")) {
                fileOp.loadCustomerFile(args);
                loadCustomerMode(args, fileOp);

            } else if (args[0].equals("--admin")) {
                fileOp.loadAdminFile(args);
                loadAdminMode(fileOp);
            
            } else {
                System.out.println("Invalid user mode. Terminating program now.");
            }
        } catch (InvalidLineException | InvalidFormatException e ) {
                System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }

    /**
     * Load admin mode and shows the main menu for the admin. This method overrides the mainMenu method from the User class.
     * @param fileOp File Operation to be accessed to run the program.
     */
    private static void loadAdminMode(FileOperation fileOp) {
        try {
            Admin admin = new Admin(fileOp);
            admin.displayWelcomeMessage();
            displayMessage();
            admin.mainMenu();

        } catch (InvalidLineException | InvalidFormatException e ) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());

        }

    }

    /**
     * Load customer mode and shows the main menu for the customer. This method overrides the mainMenu method from the User class.
     * Customer Id and password assigned if provided, else customers are asked to type their name and password.
     * @param args command line arguments passed to the program. Expecting customerId and password.
     * @param fileOp File Operation to be accessed to run the program.
     */
    private static void loadCustomerMode(String[] args, FileOperation fileOp) {
        Customer customer;
        if (args.length > 4 && !args[1].contains(".csv")) {
            String customerId = args[1];
            String password = args[2];

            try {
                customer = new Customer(customerId, null, password, fileOp);
                if (customer.signIn(customerId, password)) {
                    customer.displayWelcomeMessage();
                    displayMessage();
                    customer.mainMenu();
                }
            } catch (NotFoundException | IncorrectPasswordException | InvalidLineException | InvalidFormatException e ) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                customer = new Customer(null, null, null, fileOp);
                customer.signUp();
                customer.displayWelcomeMessage();
                displayMessage();
                customer.mainMenu();
            } catch (InvalidLineException | InvalidFormatException e ) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * displays the welcome message.
     */
    public static void displayMessage() {
        System.out.print("\n" +
                " ________  ___ _____ \n" +
                "|_   _|  \\/  |/  ___|\n" +
                "  | | | .  . |\\ `--. \n" +
                "  | | | |\\/| | `--. \\\n" +
                "  | | | |  | |/\\__/ /\n" +
                "  \\_/ \\_|  |_/\\____/ \n" +
                "                    \n" +
                "                    \n");
    }
}

