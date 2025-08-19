package ticket.booking.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBookingServices {

     private User user;

     private static List<User> userList;

     private final ObjectMapper objectMapper = new ObjectMapper();

     private static final String USERS_PATH = "E:\\My Projects\\My_Ticket_Booking_App\\app\\src\\main\\java\\ticket\\booking\\localDB\\users.json";

    private final TrainService trainService;


    public UserBookingServices(User user1) throws IOException {
        this.user = user1;
        loadUsers();
        this.trainService = new TrainService();
    }

    public UserBookingServices() throws IOException {
        loadUsers();
        this.trainService = new TrainService();
    }

    public void loadUsers() throws IOException {
        File users = new File(USERS_PATH);

        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});


        if (userList == null) {
            userList = new ArrayList<>();
        }
    }


    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashpassword());}).findFirst();
        return foundUser.isPresent();
    }

    public boolean signUp(User user1){
        if (user1.getName().length() < 5){
            System.out.println("Username must be of minimum length 5");
        }

        if (user1.getPassword().length() < 5){
            System.out.println("Username must be of minimum length 5");
        }

        try {
            boolean ifAlreadyExist = userList.stream().anyMatch(user -> user.getName().equals(user1.getName()));

            if (ifAlreadyExist) {
                System.out.println("Username already exists in the Database. Please try with different userName");
                return false;
            } else {

                userList.add(user1);
                saveUserListToFile();
                return true;
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public void saveUserListToFile() throws IOException {
        File userfile = new File(USERS_PATH);
        objectMapper.writeValue(userfile, userList);
    }

    public void fetchBooking(){
        user.printTickets();
    }

    public void cancelBooking( String ticketId) throws IOException {
        trainService.cancelBooking(user,ticketId);
    }

    public List<Train> getTrains(String source, String destination) throws IOException {
        return trainService.searchTrain(source, destination);
    }

    public boolean bookSeat(Train train, int berthNo, int seatNo) throws IOException {
        return trainService.bookSeats(train, berthNo, seatNo);
    }

    public boolean[][] fetchSeats(String trainId) {
        Train train = trainService.getTrainById(trainId);
        if (train != null) {
            return trainService.fetchSeatMatrix(train);
        } else {
            System.out.println("Train not found!");
            return null;
        }
    }


}
