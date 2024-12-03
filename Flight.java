package Assigment;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Flight {
    private String flightID;
    private LocalDate date;
    private String time;
    private String startingPoint;
    private String destination;
    private int totalSeats;
    private int confirmedCount;
    private Passenger[] confirmedTickets;
    private Queue<Passenger> waitingList;

    private static final String TEXT_FILE = "C:\\Users\\leesy\\Downloads\\notes DS\\Assigment\\Passenger_Information.txt"; // Change to your file path
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");

    public Flight(String flightID, String date, String time, String startingPoint, String destination, int totalSeats) {
        this.flightID = flightID;
        this.date = LocalDate.parse(date, dateFormatter);
        this.time = time;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.confirmedCount = 0;
        this.confirmedTickets = new Passenger[totalSeats];
        this.waitingList = new LinkedList<>();
        loadDataFromTextFile();  //every rerun the system, will read the textfile
    }

    //Read the textfile
    private void loadDataFromTextFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 4) {
                    try {
                        int ticketNumber = Integer.parseInt(data[0]);
                        String flightID = data[1];
                        String name = data[2];
                        String passport = data[3];
                        Passenger passenger = new Passenger(name, passport, ticketNumber,flightID);

                        if (confirmedCount < totalSeats) {
                            confirmedTickets[confirmedCount++] = passenger;
                        } else {
                            waitingList.add(passenger);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid ticket number format in text file: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load data from text file: " + e.getMessage(), "Text File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public String getFlightID() {
        return flightID;
    }

    public void bookTicket(String name, String passportNumber, String flightID) {
        int ticketNumber = confirmedCount + waitingList.size() + 1;  //formula of ticket number(set by ownself)
        Passenger passenger = new Passenger(name, passportNumber, ticketNumber, flightID);

        if (confirmedCount < totalSeats) {
            confirmedTickets[confirmedCount++] = passenger;
            saveToTextFile(passenger);
            JOptionPane.showMessageDialog(null, "Ticket confirmed for: " + passenger.getName() + " | Ticket Number: " + ticketNumber, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            waitingList.add(passenger);
            JOptionPane.showMessageDialog(null, "Flight full! Added to waiting list: " + passenger.getName() + " | Ticket Number: " + ticketNumber, "Booking Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void editTicket(int ticketNumber, String name, String passport, String flightID) {

        // Read the current file data
        List<String> updatedRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    int currentTicketNumber = Integer.parseInt(data[0]);
                    String currentFlightID = data[1];
                    String currentName = data[2];
                    String currentPassport = data[3];

                    if (currentTicketNumber == ticketNumber && currentName.equals(name) && currentPassport.equals(passport) && currentFlightID.equals(flightID)) {
                        // Ticket found, replace the line with new information
                        String newName = JOptionPane.showInputDialog("Enter new name:");
                        String newPassport = JOptionPane.showInputDialog("Enter new passport number:");
                        String updatedLine = ticketNumber + "," + flightID + "," + newName + "," + newPassport;
                        updatedRecords.add(updatedLine);
                        found = true;
                    } else {
                        // Keep the existing record if it's not the one to edit
                        updatedRecords.add(line);
                    }
                }
            }

            // If ticket was found, write the updated records back to the file
            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE))) {
                    for (String record : updatedRecords) {
                        writer.write(record);
                        writer.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "Ticket updated successfully!", "Ticket Updated", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to update the text file.", "Text File Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ticket not found or details do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewTicketStatus(int ticketNumber, String name, String passportNumber, String flightID) {
        for (Passenger p : confirmedTickets) {
            if (p != null && p.getTicketNumber() == ticketNumber && p.getName().equals(name) && p.getPassportNumber().equals(passportNumber) && p.getFlightID().equals(flightID)) {
                JOptionPane.showMessageDialog(null, "Ticket Status: Confirmed - " + p, "Ticket Status", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        for (Passenger p : waitingList) {
            if (p.getTicketNumber() == ticketNumber && p.getName().equals(name) && p.getPassportNumber().equals(passportNumber) && p.getFlightID().equals(flightID)) {
                JOptionPane.showMessageDialog(null, "Ticket Status: Waiting - " + p, "Ticket Status", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Ticket not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void cancelTicket(int ticketNumber, String name, String passportNumber, String flightID) {
        List<String> updatedRecords = new ArrayList<>();
        boolean ticketFound = false;

        // Read the current file data to filter out the target row
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    try {
                        int currentTicketNumber = Integer.parseInt(data[0].trim());
                        String currentFlightID = data[1].trim();
                        String currentName = data[2].trim();
                        String currentPassport = data[3].trim();

                        // Only add the row if it doesn't match the target ticket
                        if (!(currentTicketNumber == ticketNumber && currentName.equals(name) && currentPassport.equals(passportNumber) && currentFlightID.equals(flightID))) {
                            updatedRecords.add(line); // Keep non-target rows
                        } else {
                            // If it's the matching row, skip it
                            ticketFound = true;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid ticket number: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading the file: " + e.getMessage(), "Read Error", JOptionPane.ERROR_MESSAGE);
        }

        // If the ticket was found and removed, rewrite the file
        if (ticketFound) {
            updateTextFile(updatedRecords); // Update file with non-target records
            JOptionPane.showMessageDialog(null, "Ticket canceled successfully.", "Ticket Canceled", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Ticket not found for cancellation!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTextFile(List<String> newRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE, false))) {
            for (String record : newRecords) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update text file: " + e.getMessage(), "Text File Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void saveToTextFile(Passenger passenger) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE, true))) {
            String record = passenger.getTicketNumber() + "," + flightID + "," + passenger.getName() + "," + passenger.getPassportNumber();
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save ticket to text file: " + e.getMessage(), "Text File Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public boolean FindTicket(int ticketNumber, String name, String passportNumber, String flightID) {
        for (Passenger p : confirmedTickets) {
            if (p != null && p.getTicketNumber() == ticketNumber && p.getName().equals(name) && p.getPassportNumber().equals(passportNumber) p.getFlightID().equals(flightID)) {
                return true;
            }
        }

        for (Passenger p : waitingList) {
            if (p.getTicketNumber() == ticketNumber && p.getName().equals(name) && p.getPassportNumber().equals(passportNumber) && p.getFlightID().equals(flightID)) {
                return true;
            }
        }

        return false;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return totalSeats - confirmedCount;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public String getTime() {
        return time;
    }


    @Override
    public String toString() {
        return "FlightID: " + flightID + "   Date: " + date + "   Time: " + time + "   From: " + startingPoint + "   To: " + destination;
    }
}
