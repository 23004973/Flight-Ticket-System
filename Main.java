package Assigment;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlightSystem system = new FlightSystem("C:\\Users\\leesy\\Downloads\\notes DS\\Assigment\\Flight Information.csv");
        boolean running = true;

        while (running) {
            // Display the menu
            String menu = "1. Search for Flights\n" +
                    "2. Book a Ticket\n" +
                    "3. Edit Ticket Information\n" +
                    "4. View Ticket Status\n" +
                    "5. Cancel a Ticket\n" +
                    "6. Display All Flights\n" +
                    "7. Exit";

            // Show the menu and get user input
            String choiceString = JOptionPane.showInputDialog(null, menu, "Flight Ticket Booking System", JOptionPane.PLAIN_MESSAGE);
            if (choiceString == null) {
                break; // Exit if the user closes the input dialog
            }

            int choice;
            try {
                choice = Integer.parseInt(choiceString);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a number between 1 and 7.", "Error", JOptionPane.ERROR_MESSAGE);
                continue; // Go back to the main menu
            }

            switch (choice) {
                //Search for Flights
                case 1:
                    String startDate = JOptionPane.showInputDialog("Enter start date (yyyy/MM/dd):");
                    String endDate = JOptionPane.showInputDialog("Enter end date (yyyy/MM/dd):");
                    system.searchFlight(startDate, endDate);
                    break;

                //Book a Ticket
                case 2:
                    String flightID = JOptionPane.showInputDialog("Enter flight ID:");
                    String name = JOptionPane.showInputDialog("Enter passenger name:");
                    String passport = JOptionPane.showInputDialog("Enter passport number:");
                    Flight flight = system.getFlights().stream()
                            .filter(f -> f.getFlightID().equals(flightID))
                            .findFirst()
                            .orElse(null);
                    if (flight != null) {
                        flight.bookTicket(name, passport, flightID);
                    } else {
                        JOptionPane.showMessageDialog(null, "Flight not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                // Edit Ticket Information
                case 3:
                    try {
                        // Get user input for ticket editing
                        int ticketNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter ticket number to edit:"));
                        String passportNumber = JOptionPane.showInputDialog("Enter passport number:");
                        String NAME = JOptionPane.showInputDialog("Enter the name:");
                        String FlightID = JOptionPane.showInputDialog("Enter the flight ID:");

                        // Search for the ticket to edit
                        boolean ticketFound = false;
                        for (Flight f : system.getFlights()) {
                            if (f.FindTicket(ticketNumber, NAME, passportNumber, FlightID)) {
                                f.editTicket(ticketNumber, NAME, passportNumber, FlightID);
                                ticketFound = true;
                                break;
                            }
                        }

                        if (!ticketFound) {
                            JOptionPane.showMessageDialog(null, "Ticket not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid ticket number! Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;


                // View Ticket Status
                case 4:
                    try {
                        int ticketNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter ticket number to view the status:"));
                        String passportNumber = JOptionPane.showInputDialog("Enter passport number:");
                        String Name = JOptionPane.showInputDialog("Enter the name to view status");
                        String FlightID = JOptionPane.showInputDialog("Enter the flight ID to view status:");


                        boolean found = false;

                        for (Flight f : system.getFlights()) {
                            if (f.FindTicket(ticketNumber, Name, passportNumber, FlightID)) {
                                f.viewTicketStatus(ticketNumber, Name, passportNumber, FlightID);
                                found = true;
                                break; // Exit loop once ticket status is found
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Ticket not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid ticket number! Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                // Cancel a Ticket
                case 5:
                    try {
                        // Get user input for ticket cancellation
                        int cancelTicketNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter ticket number to cancel:"));
                        String cancelPassportNumber = JOptionPane.showInputDialog("Enter passport number:");
                        String passengerName = JOptionPane.showInputDialog("Enter the name:");
                        String FlightID = JOptionPane.showInputDialog("Enter the flight ID:");


                        boolean found = false;

                        // Iterate through all flights in the system to find the ticket
                        for (Flight f : system.getFlights()) {
                            // Use the findTicket method to check for a matching ticket
                            if (f.FindTicket(cancelTicketNumber, passengerName, cancelPassportNumber, FlightID)) {
                                // If ticket found, cancel it
                                f.cancelTicket(cancelTicketNumber, passengerName, cancelPassportNumber, FlightID);
                                found = true;
                                break; // Exit loop after finding and canceling the ticket
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Ticket not found or details do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        // Handle invalid input for ticket number
                        JOptionPane.showMessageDialog(null, "Invalid ticket number! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 6:
                    system.displayAllFlights();
                    break;

                case 7:
                    running = false;
                    JOptionPane.showMessageDialog(null, "Thank you! Goodbye", "Exit", JOptionPane.INFORMATION_MESSAGE);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option! Please select a number between 1 and 7.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }
}
