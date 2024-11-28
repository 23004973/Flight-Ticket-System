package Assigment;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FlightSystem {
    private List<Flight> flights;

    public FlightSystem(String filePath) {
        flights = new ArrayList<>();
        loadFlights(filePath);
    }

    //Read the csv file
    private void loadFlights(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Skip the header
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the first line (header)
                    continue;
                }
                String[] data = line.split(",");
                String flightID = data[0];
                String date = data[3]; // Date is in the 4th column
                String time = data[4];
                String startingPoint = data[5];
                String destination = data[6];
                int seats = Integer.parseInt(data[7]); // Parse number of seats as integer
                flights.add(new Flight(flightID, date, time, startingPoint, destination, seats));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void searchFlight(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        StringBuilder flightInfo = new StringBuilder("Flights from " + startDate + " to " + endDate + ":\n");
        for (Flight flight : flights) {
            if ((flight.getDate().isEqual(start) || flight.getDate().isAfter(start)) &&
                    (flight.getDate().isEqual(end) || flight.getDate().isBefore(end))) {
                flightInfo.append(flight).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, flightInfo.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void displayAllFlights() {
        StringBuilder flightInfo = new StringBuilder("Available Flights:\n");
        for (Flight flight : flights) {
            flightInfo.append(flight).append("\n");
        }
        JOptionPane.showMessageDialog(null, flightInfo.toString(), "All Flights", JOptionPane.INFORMATION_MESSAGE);
    }
}
