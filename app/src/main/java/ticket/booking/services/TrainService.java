package ticket.booking.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;
import ticket.booking.entities.Train;
import ticket.booking.entities.Ticket;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainService {

    private final List<Train> trainList;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TRAIN_DB_PATH = "E:\\My Projects\\My_Ticket_Booking_App\\app\\src\\main\\java\\ticket\\booking\\localDB\\trains.json";

    private final boolean[][] seatMatrix = new boolean[5][5];

    public TrainService() throws IOException {
        File trains = new File(TRAIN_DB_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrain(String source, String destination){
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    public boolean validTrain(Train train, String source, String destination){

        List<String> stationOrder = train.getStationOrderList();

        if (!stationOrder.contains(source) || !stationOrder.contains((destination))){
            return false;
        }

        int sourceIndex = stationOrder.indexOf(source);
        int destinationIndex = stationOrder.indexOf(destination);

        return sourceIndex < destinationIndex;

    }

    public void addTrain(Train newTrain) throws IOException {
        boolean ifExist = false;

        for (Train train : trainList){
            if (train.getTrainID().equalsIgnoreCase(newTrain.getTrainID())){
                updateTrain(newTrain);
                ifExist = true;
                break;
            }
        }

        if (!ifExist){
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train train) throws IOException {
        boolean ifExist = false;

        for (int i=0; i<trainList.size(); i++){
            Train trainExist = trainList.get(i);

            if (trainExist.getTrainID().equalsIgnoreCase(train.getTrainID())){
                trainList.set(i, train);
                ifExist = true;
            }
        }
        if (!ifExist){
            addTrain(train);
        }
        saveTrainListToFile();
    }

    public void saveTrainListToFile() throws IOException {

            File userfile = new File( TRAIN_DB_PATH );
            objectMapper.writeValue(userfile, trainList);

    }



    public Boolean cancelBooking(User user, String ticketId) throws IOException {

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        Ticket ticketToCancel = user.getTicketsBooked().stream()
                .filter(ticket -> ticket.getTicketID().equalsIgnoreCase(ticketId))
                .findFirst()
                .orElse(null);

        boolean removed = user.getTicketsBooked().remove(ticketToCancel);

        if (removed) {
            assert ticketToCancel != null;
            int berthIndex = ticketToCancel.getBerthNo() - 1;
            int seatIndex = ticketToCancel.getSeatNo() - 1;
            seatMatrix[berthIndex][seatIndex] = false;
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            new UserBookingServices().saveUserListToFile();
            return Boolean.TRUE;

        } else {
            System.out.println("No ticket found with ID " + ticketId);
            new UserBookingServices().saveUserListToFile();
            return Boolean.FALSE;
        }


    }

    public boolean bookSeats(Train train, int berthNo, int seatNo) throws IOException {
        List<List<Boolean>> seats = train.getSeats();

        if (berthNo < 1 || berthNo > seats.size() || seatNo < 1 || seatNo > seats.get(0).size()) {
            System.out.println("Seat number out of range. Try again.");
            return false;
        }

        int bIndex = berthNo - 1;
        int sIndex = seatNo - 1;

        if (!seats.get(bIndex).get(sIndex)) {

            seats.get(bIndex).set(sIndex, true);

            int index = trainList.indexOf(train);
            if (index != -1) {
                trainList.set(index, train);
            }

            saveTrainListToFile();
            System.out.println("Your seat is booked.");
            return true;
        } else {
            System.out.println("Seat already booked. Try another.");
            return false;
        }
    }


    public boolean[][] fetchSeatMatrix(Train train) {
        List<List<Boolean>> seatsList = train.getSeats();
        boolean[][] matrix = new boolean[seatsList.size()][seatsList.get(0).size()];
        for (int i = 0; i < seatsList.size(); i++) {
            for (int j = 0; j < seatsList.get(0).size(); j++) {
                matrix[i][j] = seatsList.get(i).get(j);
//                if (seatsList.get(i).get(j).equals(false)){
//                    System.out.print(0 + " ");
//                }
//                else{
//                    System.out.print(1 + " ");
//                };
            }
//            System.out.println();
        }
        return matrix;
    }


    public Train getTrainById(String trainId) {
        for (Train t : trainList) {
            if (t.getTrainID().equalsIgnoreCase(trainId)) {
                return t;
            }
        }
        return null;
    }

}




