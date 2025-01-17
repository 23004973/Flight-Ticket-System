package Assigment;

public class Passenger {
    private String name;
    private String passportNumber;
    private int ticketNumber;
    private String flightID;

    public Passenger(String name, String passportNumber, int ticketNumber, String flightID){
        this.name=name;
        this.passportNumber=passportNumber;
        this.ticketNumber=ticketNumber;
        this.flightID=flightID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNumber(){
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

     public int getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String toString(){
        return "\n\nPassenger:" +
                "\nName = " + name  +
                "\nPassport = " + passportNumber +
                "\nTicket Number = " + ticketNumber +
                "\nFlightID= " + flightID;

    }
}
