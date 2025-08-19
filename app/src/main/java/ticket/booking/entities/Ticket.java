package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

    @JsonProperty("ticket_id")
    private String ticketID;

    @JsonProperty("user_id")
    private String userID;

    private String source;

    private String destination;

    @JsonProperty("date_of_travel")
    private String dateOfTravel;

    private Train train;

    private String confirmationStatus;


    private int berthNo;

    private int seatNo;

    public int getBerthNo() {
        return berthNo;
    }

    public void setBerthNo(int berthNo) {
        this.berthNo = berthNo;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }


    public String getTicketInfo(){
        return String.format("Ticket ID: %s belongs to User %s from %s to %s on %s", ticketID, userID, source, destination, dateOfTravel);
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

}
