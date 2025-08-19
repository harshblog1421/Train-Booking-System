package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {

    @JsonProperty("train_id")
    private String trainID;

    @JsonProperty("train_no")
    private String trainNo;


    private List<List<Boolean>> seats;

    @JsonProperty("station_times")
    private Map<String, String> stations;

    @JsonProperty("stations")
    private List<String> stationOrderList;

    private String source;

    private String destination;

    public Train(String trainID, String trainNo, List<List<Boolean>> seats, Map<String, String> stations, String source, String destination) {
        this.trainID = trainID;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stations = stations;
        this.source = source;
        this.destination = destination;
    }

    public Train(){

    }

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Boolean>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Boolean>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStations() {
        return stations;
    }

    public void setStations(Map<String, String> stations) {
        this.stations = stations;
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

    public String getTrainInfo(){
        return String.format("Train ID: %s Train No: %s", trainID, trainNo);
    }

    public void setStationOrderList(List<String> givenOrderList){
        this.stationOrderList = givenOrderList;
    }

    public List<String> getStationOrderList(){
        return stationOrderList;
    }


}
