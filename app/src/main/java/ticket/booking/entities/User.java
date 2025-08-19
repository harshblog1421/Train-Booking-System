package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String name;

    private String password;

    @JsonProperty("hashed_password")
    private String hashpassword;


    @JsonProperty("tickets_booked")
    private List<Ticket> ticketsBooked;

    @JsonProperty("user_id")
    private String userID;

    public User(String name, String password, String hashpassword, List<Ticket> ticketsBooked, String userID) {
        this.name = name;
        this.password = password;
        this.hashpassword = hashpassword;
        this.ticketsBooked = ticketsBooked;
        this.userID = userID;
    }

    public User(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashpassword() {
        return hashpassword;
    }

    public void setHashpassword(String hashpassword) {
        this.hashpassword = hashpassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void printTickets(){
        for (int i=0; i< ticketsBooked.size(); i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }
}
