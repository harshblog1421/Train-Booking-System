package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingServices;
import ticket.booking.utils.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("Running Train booking system");
        Scanner scanner = new Scanner(System.in);

        UserBookingServices userBookingServices;
        try {
            userBookingServices = new UserBookingServices();
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println("There is some error");
            return;
        }

        Train trainSelectedForBooking = null;
        User currentUser = null;
        boolean hasSignedUp = false;
        int option = 0;

        while (true) {

            System.out.println("\nChoose option:");

            if (currentUser == null) {
                // Not logged-in
                if (!hasSignedUp) {
                    System.out.println("1. Sign up");
                    System.out.println("2. Login");
                    System.out.println("3. Exit");
                } else {
                    System.out.println("1. Login");
                    System.out.println("2. Exit");
                }
            } else {
                // Logged-in user → show features
                System.out.println("1. Fetch Bookings");
                System.out.println("2. Fetch Seats");
                System.out.println("3. Search Trains");
                System.out.println("4. Book a Seat");
                System.out.println("5. Cancel my Booking");
                System.out.println("6. Logout");
            }

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (currentUser == null) {
                // Not logged-in
                if (!hasSignedUp) {
                    switch (option) {
                        case 1:
                            System.out.println("Enter the username to signup");
                            String nameToSignUp = scanner.nextLine();
                            System.out.println("Enter the password to signup");
                            String passwordToSignUp = scanner.nextLine();

                            User userToSignup = new User(
                                    nameToSignUp,
                                    passwordToSignUp,
                                    UserServiceUtil.hashPassword(passwordToSignUp),
                                    new ArrayList<>(),
                                    UUID.randomUUID().toString()
                            );

                            boolean ok = userBookingServices.signUp(userToSignup);
                            if (ok) {
                                hasSignedUp = true;
                                System.out.println("Signup successful. Please login now.");
                            } else {
                                System.out.println("Signup failed. Try again.");
                            }
                            break;

                        case 2:
                            // Login option
                            currentUser = handleLogin(scanner, userBookingServices);
                            break;

                        case 3:
                            System.out.println("Exiting app. Goodbye!");
                            return;

                        default:
                            System.out.println("Please choose a valid option.");
                    }
                } else {
                    switch (option) {
                        case 1:
                            currentUser = handleLogin(scanner, userBookingServices);
                            break;

                        case 2:
                            System.out.println("Exiting app. Goodbye!");
                            return;

                        default:
                            System.out.println("Please choose a valid option.");
                    }
                }
            } else {
                // Logged-in → features
                switch (option) {
                    case 1:
                        System.out.println("Fetching bookings: ");
                        userBookingServices.fetchBooking();
                        break;

                    case 2:
                        System.out.println("Enter the train ID");
                        String trainID = scanner.next();
                        System.out.println("Fetching Seat Matrix: ");
                        boolean[][] fetchedSeats = userBookingServices.fetchSeats(trainID);
                        for (boolean[] seats : fetchedSeats) {
                            for (boolean s : seats) {
                                System.out.print((s ? 1 : 0) + " ");
                            }
                            System.out.println();
                        }
                        break;

                    case 3:
                        System.out.println("Enter the source station: ");
                        String source = scanner.next();
                        System.out.println("Enter the destination station: ");
                        String destination = scanner.next();

                        List<Train> trains;
                        try {
                            trains = userBookingServices.getTrains(source, destination);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Internal error occured! Try again later");
                            break;
                        }

                        if (trains == null || trains.isEmpty()) {
                            System.out.println("No trains found between these stations");
                        } else {
                            int index = 1;
                            for (Train train : trains) {
                                System.out.println(index + " TrainID-> " + train.getTrainID());
                                if (train.getStations() != null) {
                                    for (Map.Entry<String, String> entry : train.getStations().entrySet()) {
                                        System.out.println("   Station: " + entry.getKey() + " | Time: " + entry.getValue());
                                    }
                                }
                                index++;
                            }
                        }

                        System.out.println("Select trains between 1  2  3 ...");
                        if (!scanner.hasNextInt()) {
                            System.out.println("Invalid input");
                            scanner.nextLine();
                            break;
                        }

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        if (choice < 1 || choice > Objects.requireNonNull(trains).size()) {
                            System.out.println("Enter a valid train number");
                            break;
                        }

                        trainSelectedForBooking = trains.get(choice - 1);
                        System.out.println("Selected train id is " + trainSelectedForBooking.getTrainID());
                        break;

                    case 4: // Book a Seat
                        if (trainSelectedForBooking == null) {
                            System.out.println("Please search and select a train first (option 3).");
                            break;
                        }

                        Train trainToBook = trainSelectedForBooking;
                        boolean[][] currentSeats = userBookingServices.fetchSeats(trainToBook.getTrainID());

                        // Display current seat matrix
                        for (boolean[] seats : currentSeats) {
                            for (boolean s : seats) {
                                System.out.print((s ? 1 : 0) + " ");
                            }
                            System.out.println();
                        }

                        System.out.println("Select the berth number (0-based index): ");
                        int berthNo = scanner.nextInt();
                        System.out.println("Select the seat number (0-based index): ");
                        int seatNumber = scanner.nextInt();

                        // Use TrainService to book seat
                        boolean booked = userBookingServices.bookSeat(trainToBook, berthNo + 1, seatNumber + 1);
                        // note: bookSeat expects 1-based indexes

                        if (booked) {
                            System.out.println("Seat booked successfully.");
                        } else {
                            System.out.println("Seat could not be booked, maybe already booked.");
                        }
                        break;


                    case 5:
                        System.out.println("Enter ticket ID to cancel: ");
                        String ticketId = scanner.nextLine();
                        try {
                            userBookingServices.cancelBooking(ticketId);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Error while cancelling ticket.");
                        }
                        break;

                    case 6:
                        System.out.println("Logging out...");
                        currentUser = null;
                        trainSelectedForBooking = null;
                        break;

                    default:
                        System.out.println("Please choose a valid option.");
                }
            }
        }
    }

    private static User handleLogin(Scanner scanner, UserBookingServices userBookingServices) {
        System.out.println("Enter the username to login");
        String loginName = scanner.nextLine();
        System.out.println("Enter the password to login");
        String loginPassword = scanner.nextLine();

        User userToLogIn = new User(
                loginName,
                loginPassword,
                UserServiceUtil.hashPassword(loginPassword),
                new ArrayList<>(),
                UUID.randomUUID().toString()
        );

        boolean okayLogin;
        try {
            UserBookingServices userLoggingIN = new UserBookingServices(userToLogIn);
            okayLogin = userLoggingIN.loginUser();
            if (okayLogin) {
                System.out.println("Login successful. Welcome " + loginName + "!");
                return userToLogIn;
            } else {
                System.out.println("Invalid credentials!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Internal error. Try again later.");
            return null;
        }
    }
}
