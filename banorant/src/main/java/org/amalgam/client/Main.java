package org.amalgam.client;

import org.amalgam.client.callbacks.MessageCallbackImpl;
import org.amalgam.utils.Status;
import org.amalgam.utils.models.Availability;
import org.amalgam.utils.models.Session;
import org.amalgam.utils.services.*;
import org.amalgam.utils.Binder;
import org.amalgam.utils.models.User;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * Main class for the Amalgam client application.
 * Implements the Runnable interface for running the client.
 */

public class Main implements Runnable {
    private UserService userService;
    private AuthenticationService authService;
    private MessageService messageService;
    private CelebrityFanService celebrityFanService;
    private MessageCallbackImpl messageCallback;
    private final Scanner kyb = new Scanner(System.in);

    /**
     * Main method to start the client application.
     *
     * @param args Command-line arguments.
     */

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    /**
     * Implementation of the run method from the Runnable interface.
     * Sets up RMI services and starts the client.
     */

    @Override
    public void run() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            userService = (UserService) registry.lookup(Binder.userService);
            authService = (AuthenticationService) registry.lookup(Binder.authService);
            messageService = (MessageService) registry.lookup(Binder.messageService);
            celebrityFanService = (CelebrityFanService) registry.lookup(Binder.celebrityFanService);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        try {
            index();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the index menu and handles user input.
     * Allows users to login, sign in, or exit the application.
     */

    private void index() throws RemoteException {
        while (true) {
            System.out.println("Choose from the following");
            System.out.println("1. Login");
            System.out.println("2. SignIn");
            System.out.println("3. Exit");
            int choice = Integer.parseInt(kyb.nextLine());

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    signin();
                    break;
                case 3:
                    System.out.println("Thank you have a nice day!");
                    System.exit(0);
                default:
                    System.out.println("Please choose appropriate options");
            }
        }
    }

    /**
     * Handles the login process for the user.
     * Authenticates the user with the provided credentials.
     * Redirects to appropriate menu based on user role.
     */

    private void login() throws RemoteException {
        User user = null;
        String username;
        String password;
        do {
            System.out.println("Input username:");
            username = kyb.nextLine();
            System.out.println("Input password:");
            password = kyb.nextLine();

            if (username != null && password != null) {
                try {
                    user = authService.authenticateUser(username, password);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Please input accordingly");
            }
        } while (username == null || password == null || user == null);

        boolean isCelebrity = user.isCelebrity();
        if (isCelebrity) {
            menuCelebrity(user);
        } else {
            menuFan(user);
        }

    }

    /**
     * Handles the sign-in process for new users.
     * Creates a new user account with provided credentials.
     */

    private void signin(){
        String username;
        String password;
        boolean creation = false;
        do {
            System.out.println("Input username: ");
            username = kyb.nextLine();
            System.out.println("Input password: ");
            password = kyb.nextLine();

            if (username != null && password != null) {
                try {
                    creation = userService.createUser(username, password);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Please input accordingly");
            }
        } while (username == null || password == null);

        if (creation) {
            System.out.println("Account is now created");
        } else {
            System.out.println("Error creating an account");
        }
    }

    /**
     * Displays the menu options for a fan user and handles user input.
     */

    private void menuFan(User user) throws RemoteException {
        boolean loop = true;
        while (loop) {
            System.out.println("Choose from the ff.");
            System.out.println("1. Session");
            System.out.println("2. Players");
            System.out.println("3. Profile");

            int choice = Integer.parseInt(kyb.nextLine());
            switch (choice){
                case 1:
                    sessionChoice(user);
                    break;
                case 2:
                    playersChoice(user);
                    break;
                case 3:
                    profileManagementFan(user);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }

            System.out.println("Do you want to return to the main menu? (yes/no)");
            String answer = kyb.nextLine().trim().toLowerCase();
            if (!answer.equals("yes")) {
                loop = false;
            }
        }
    }

    /**
     * Displays the menu options for a celebrity user and handles user input.
     */

    private void menuCelebrity(User user) {
        boolean loop = true;

        while (loop) {
            System.out.println("Choose from the following options:");
            System.out.println("1. Session");
            System.out.println("2. Profile");

            int choice = Integer.parseInt(kyb.nextLine());
            switch (choice) {
                case 1:
                    sessionChoice(user);
                    break;

                case 2:
                    try {
                        profileManagementCeleb(user);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }

            System.out.println("Do you want to return to the main menu? (yes/no)");
            String answer = kyb.nextLine().trim().toLowerCase();
            if (!answer.equals("yes")) {
                loop = false;
            }
        }
    }



    /**
     * Handles the session choice menu for both fan and celebrity users.
     */

    public void sessionChoice(User user) {
        try {
            List<Session> sessionList = celebrityFanService.getAcceptedSession(user.getUserID());

            if (sessionList.isEmpty()) {
                System.out.println("No accepted sessions found");
                return;
            }

            displaySessions(sessionList);

            int selectedSessionIndex = getSelectedSessionIndex(sessionList.size());

            if (isValidSessionSelection(selectedSessionIndex, sessionList.size())) {
                handleSessionEntry(user.getUsername(), sessionList.get(selectedSessionIndex));
            } else {
                System.out.println("Invalid session number");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the players choice menu for fan users.
     */

    public void playersChoice(User user) {
        try {
            System.out.println("1. View Players");
            int playerChoice = Integer.parseInt(kyb.nextLine());

            if (playerChoice == 1) {
                List<User> listOfPlayers = userService.getPlayers();

                if (listOfPlayers.isEmpty()) {
                    System.out.println("No players found.");
                    return;
                }

                displayPlayers(listOfPlayers);

                int selectedPlayerIndex = getSelectedPlayerIndex(listOfPlayers.size());

                if (isValidSelection(selectedPlayerIndex, listOfPlayers.size())) {
                    User selectedPlayer = listOfPlayers.get(selectedPlayerIndex);
                    displayPlayerAvailability(user,selectedPlayer);
                } else {
                    System.out.println("Invalid player selection.");
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Handles the profile management menu for fan users.
     */

    public void profileManagementFan(User user){
        System.out.println("1. Update Password");
        System.out.println("2. Delete Profile");

        int profileChoice = Integer.parseInt(kyb.nextLine());
        switch (profileChoice) {

            case 1:
                System.out.println("Enter new password:");
                String newPassword = kyb.nextLine();
                try {
                    boolean updated = userService.updateUser(user.getUsername(), newPassword);
                    if (updated) {
                        System.out.println("Profile updated successfully.");
                    } else {
                        System.out.println("Failed to update profile.");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                System.out.println("Are you sure you want to delete your profile? (Y/N)");
                String confirm = kyb.nextLine().toLowerCase();
                if (confirm.equals("y")) {
                    try {
                        boolean deleted = userService.deleteUser(user.getUsername());
                        if (deleted) {
                            System.out.println("Profile deleted successfully.");
                        } else {
                            System.out.println("Failed to delete profile.");
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Profile deletion cancelled.");
                }
                break;
            default:
                System.out.println("Invalid choice. Please choose a valid option for profile.");
        }


    }

    /**
     * Handles the profile management menu for celebrity users.
     */

    public void profileManagementCeleb(User user) throws RemoteException {
        System.out.println("1. Update Password");
        System.out.println("2. Edit Availability");
        System.out.println("3. Create Availability");
        System.out.println("4. Delete Profile");

        int profileChoice = Integer.parseInt(kyb.nextLine());
        switch (profileChoice) {

            case 1:
                System.out.println("Enter new password:");
                String newPassword = kyb.nextLine();
                try {
                    boolean updated = userService.updateUser(user.getUsername(), newPassword);
                    if (updated) {
                        System.out.println("Profile updated successfully.");
                    } else {
                        System.out.println("Failed to update profile.");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;

            case 2:
                editAvailability (user);
                break;
            case 3:
                createAvailability(user);
                break;
            case 4:
                System.out.println("Are you sure you want to delete your profile? (Y/N)");
                String confirm = kyb.nextLine().toLowerCase();
                if (confirm.equals("y")) {
                    try {
                        boolean deleted = userService.deleteUser(user.getUsername());
                        if (deleted) {
                            System.out.println("Profile deleted successfully.");

                        } else {
                            System.out.println("Failed to delete profile.");
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Profile deletion cancelled.");
                }
                break;
            default:
                System.out.println("Invalid choice. Please choose a valid option for profile.");
        }
    }

    /**
     * Simulates the payment process for session booking.
     * Prompts user to enter payment amount and simulates payment processing.
     */

    public boolean processPayment(User user, double computedRate) {
        try {
            System.out.println("Enter the amount you will pay:");
            int paymentAmount = Integer.parseInt(kyb.nextLine());

            if (paymentAmount < computedRate) {
                System.out.println("Payment amount is less than the player rate. Please pay again.");
                return false;
            }

            System.out.println("Payment is " + Status.Pending);
            simulatePaymentProcessing();

            boolean isPaymentAccepted = simulatePaymentAcceptance();

            if (isPaymentAccepted) {
                boolean paymentRegistered = registerPayment(user, paymentAmount, isPaymentAccepted);
                if (paymentRegistered) {
                    System.out.println("Payment registered successfully.");
                    return true;
                } else {
                    System.out.println("Failed to register payment.");
                    return false;
                }
            } else {
                System.out.println("Payment was rejected by the third-party provider.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
            return false;
        } catch (RemoteException e) {
            System.out.println("Error communicating with the payment service provider.");
            return false;
        }
    }

    /**
     * Simulates the processing delay for payment.
     */
    private void simulatePaymentProcessing() {
        try {
            Thread.sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Simulates the acceptance or rejection of payment.
     */

    private boolean simulatePaymentAcceptance() {
       return new Random().nextBoolean();
    }

    /**
     * Registers the payment for session booking.
     */
    private boolean registerPayment(User user, int amount, boolean isAccepted) throws RemoteException {
        return celebrityFanService.registerAcceptedPayment(user.getUserID(), amount,
                isAccepted ? Status.Accepted : Status.Rejected, getCurrentDateTime());
    }

    /**
     * Gets the current date and time as a formatted string.
     */
    public String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    /**
     * Displays the list of players along with their rates.
     * @param listOfPlayers List of User objects representing players
     * @throws RemoteException if there is a remote communication problem
     */
    private void displayPlayers(List<User> listOfPlayers) throws RemoteException {
        System.out.println("List of Players:");
        for (int i = 0; i < listOfPlayers.size(); i++) {
            User player = listOfPlayers.get(i);
            double playerRate = celebrityFanService.getRateByUserID(player.getUserID());
            System.out.println((i + 1) + ". Name:  " + player.getUsername() + " \n   Rate: " + playerRate);
        }
    }

    /**
     * Gets the index of the selected player.
     * @param maxIndex Maximum index allowed for selection
     * @return Selected player index
     */
    private int getSelectedPlayerIndex(int maxIndex) {
        System.out.println("Enter player number to select:");
        return Integer.parseInt(kyb.nextLine()) - 1;
    }

    /**
     * Checks if the selected index is valid.
     * @param index Index to check
     * @param maxSize Maximum size allowed
     * @return True if the index is valid, otherwise false
     */
    private boolean isValidSelection(int index, int maxSize) {
        return index >= 0 && index < maxSize;
    }

    /**
     * Handles the creation of a session for a user with a selected player.
     * @param user The user initiating the session
     * @param selectedPlayer The player selected for the session
     * @param date Date of the session
     * @param duration Duration of the session in minutes
     * @param moneyToPay Amount of money to be paid for the session
     * @return True if the session creation is successful, otherwise false
     * @throws RemoteException if there is a remote communication problem
     */
    private boolean handleSessionCreation(User user, User selectedPlayer, String date, int duration, double moneyToPay) throws RemoteException {

        System.out.println("Creating session for player: " + selectedPlayer.getUsername());
        System.out.println("Money to be paid: "+ moneyToPay);
        boolean paymentAccepted = processPayment(user, moneyToPay);
        String paymentStatus = "";

        if (paymentAccepted) {
            registerSession(user, selectedPlayer, date, duration);

            return true;
        } else {
            System.out.println("Failed to register payment. Session booking cancelled.");
            return false;
        }
    }

    /**
     * Gets user input for date and time.
     * @param prompt Prompt message for input
     * @return User input for date and time
     */
    private String getDateTimeInput(String prompt) {
        System.out.println(prompt);
        return kyb.nextLine();
    }


    /**
     * Registers a session for the user and the selected player.
     * @param user The user initiating the session
     * @param selectedPlayer The player selected for the session
     * @param date Date of the session
     * @param duration Duration of the session in minutes
     * @throws RemoteException if there is a remote communication problem
     */
    private void registerSessionAndBooking(User user, User selectedPlayer, String date, int duration) throws RemoteException {
        int userID = user.getUserID();
        boolean sessionRegisteredForFan = celebrityFanService.registerNewSession(userID, date, duration);
        boolean sessionRegisteredForPlayer = celebrityFanService.registerNewSession(selectedPlayer.getUserID(), date, duration);

        boolean bookingRegisteredForFan = celebrityFanService.registerNewBooking();
        boolean bookingRegisteredForPlayer = celebrityFanService.registerNewBooking();

        if (!sessionRegisteredForFan && !sessionRegisteredForPlayer) {
            System.out.println("Failed to register session.");
        }
    }

    /**
     * Displays the list of sessions.
     * @param sessionList List of Session objects representing sessions
     */
    private void displaySessions(List<Session> sessionList) {
        System.out.println("List of sessions");
        for (int i = 0; i < sessionList.size(); i++) {
            Session session = sessionList.get(i);
            System.out.println("Session Number(" + (i + 1) + ") Date:" + session.getDate());
        }
    }

    /**
     * Gets the index of the selected session.
     * @param maxIndex Maximum index allowed for selection
     * @return Selected session index
     */
    private int getSelectedSessionIndex(int maxIndex) {
        System.out.println("Enter the session number: ");
        return Integer.parseInt(kyb.nextLine()) - 1;
    }

    /**
     * Checks if the selected session index is valid.
     * @param index Index to check
     * @param maxSize Maximum size allowed
     * @return True if the index is valid, otherwise false
     */
    private boolean isValidSessionSelection(int index, int maxSize) {
        return index >= 0 && index < maxSize;
    }

    /**
     * Handles the user's entry into a session.
     * @param username Username of the user
     * @param selectedSession Selected session for entry
     * @throws RemoteException if there is a remote communication problem
     */
    private void handleSessionEntry(String username, Session selectedSession) throws RemoteException {
        System.out.println("You have entered the session: " + selectedSession.getSessionID());
        getMessageStub();
        try {
            messageCallback = new MessageCallbackImpl(username);
            messageService.logSession(messageCallback);
            System.out.println("Chat Away!");
            while(true){
                String message = kyb.nextLine();
                if(message.toLowerCase().equals("exit")){
                    messageService.logout(messageCallback);
                    break;
                }
                messageService.broadcast(messageCallback, message);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the message stub for communication.
     */
    public void getMessageStub(){
        try{
            Registry reg = LocateRegistry.getRegistry(1099);
            messageService = (MessageService) reg.lookup(Binder.messageService);
        }catch(RemoteException e){
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the availability of a player.
     * @param fan The fan requesting the availability
     * @param player The player whose availability is requested
     * @throws RemoteException if there is a remote communication problem
     */
    private void displayPlayerAvailability(User fan, User player) throws RemoteException {
        int userId = player.getUserID();
        List<Availability> availabilityList = celebrityFanService.getAvailabilityByUserID(userId);

        if (availabilityList.isEmpty()) {
            System.out.println("No availability found for this player.");
            return;
        }

        System.out.println("Availability for " + player.getUsername() + ":");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Availability availability : availabilityList) {
            System.out.println("Date: " + availability.getAvailabilityDate() + ", Time: " +
                    LocalTime.parse(availability.getStartTime()).format(timeFormatter) + " - " +
                    LocalTime.parse(availability.getEndTime()).format(timeFormatter) + ", Rate: $" +
                    availability.getRatePerHour());
        }

        try {
            System.out.println("Enter the date (YYYY-MM-DD) you want to book:");
            String selectedDateStr = kyb.nextLine();
            LocalDate selectedDate = LocalDate.parse(selectedDateStr);

            System.out.println("Enter the duration in minutes:");
            int durationMinutes = Integer.parseInt(kyb.nextLine());

            adjustStartTime(availabilityList, selectedDateStr, durationMinutes, fan, player);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format entered. Please enter the date in YYYY-MM-DD format.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid duration format entered. Please enter a valid number of minutes.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Adjusts the start time of an availability based on the duration and checks for conflicts.
     * @param availabilityList List of availabilities for the player
     * @param selectedDate Selected date for the session
     * @param durationMinutes Duration of the session in minutes
     * @param fan The fan requesting the session
     * @param player The player for the session
     * @throws RemoteException if there is a remote communication problem
     */
    private void adjustStartTime(List<Availability> availabilityList, String selectedDate, int durationMinutes, User fan, User player) throws RemoteException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime adjustedStartTime = null;

        for (Availability availability : availabilityList) {
            if (availability.getAvailabilityDate().equals(selectedDate)) {
                LocalTime originalStartTime = LocalTime.parse(availability.getStartTime());

                LocalTime startTime = originalStartTime.plusMinutes(durationMinutes);
                if (startTime.isBefore(LocalTime.parse(availability.getEndTime()))) {
                    adjustedStartTime = startTime;
                    availability.setStartTime(adjustedStartTime.format(dateTimeFormatter));
                } else {
                    System.out.println("Invalid duration. The adjusted end time exceeds the original end time.");
                    return;
                }

                if (adjustedStartTime.isAfter(originalStartTime)) {
                    System.out.println(12);
                    System.out.println("Adjusted start time: " + adjustedStartTime.format(dateTimeFormatter));
                    celebrityFanService.updateStartingTime(adjustedStartTime.format(dateTimeFormatter), availability.getAvailabilityID());
                    double durationHours = (double) durationMinutes / 60.0;
                    double moneyToPay = availability.getRatePerHour() * durationHours;

                    boolean accepted = handleSessionCreation(fan, player, selectedDate, durationMinutes, moneyToPay);

                    if (!accepted) {

                        celebrityFanService.updateStartingTime(originalStartTime.format(dateTimeFormatter), availability.getAvailabilityID());
                    }
                } else {
                    System.out.println("Invalid duration. The adjusted start time is before or equal to the original start time.");
                    return;
                }
            }
        }

        if (adjustedStartTime == null) {
            System.out.println("No availability found for the selected date.");
        }
    }

    /**
     * Creates availability for a user.
     * @param user The user for whom availability is created
     * @throws RemoteException if there is a remote communication problem
     */
    private void createAvailability(User user) throws RemoteException {
        System.out.println("Enter schedule details:");
        System.out.println("Availability Date (YYYY-MM-DD):");
        String availabilityDate = kyb.nextLine();
        System.out.println("Start Time (HH:MM:SS):");
        String startTime = kyb.nextLine();
        System.out.println("End Time (HH:MM:SS):");
        String endTime = kyb.nextLine();
        System.out.println("Rate Per Hour:");
        int ratePerHour = Integer.parseInt(kyb.nextLine());

        Availability availability = new Availability(user.getUserID(), availabilityDate, startTime, endTime, ratePerHour);
        boolean created = celebrityFanService.createAvailability(availability);

        if (created) {
            System.out.println("Schedule created successfully.");
        } else {
            System.out.println("Failed to create availability.");
        }
    }

    /**
     * Edits the availability of a user.
     * @param user The user whose availability is edited
     * @throws RemoteException if there is a remote communication problem
     */
    private void editAvailability(User user) throws RemoteException {
        System.out.println("Enter availability ID to edit:");
        int availabilityID = Integer.parseInt(kyb.nextLine());

        Availability initialAvailability = celebrityFanService.getAvailabilityByID(availabilityID);
        System.out.println("Details:");
        System.out.println("Date: "+ initialAvailability.getAvailabilityDate());
        System.out.println("Start time: "+initialAvailability.getStartTime());
        System.out.println("End time: " + initialAvailability.getEndTime());
        System.out.println("Rate/Hour: "+ initialAvailability.getRatePerHour()+"\n");


        System.out.println("Enter new availability details:");
        System.out.println("Availability Date (YYYY-MM-DD):");
        String availabilityDate = kyb.nextLine();
        System.out.println("Start Time (HH:MM:SS):");
        String startTime = kyb.nextLine();
        System.out.println("End Time (HH:MM:SS):");
        String endTime = kyb.nextLine();
        System.out.println("Rate Per Hour:");
        int ratePerHour = Integer.parseInt(kyb.nextLine());

        Availability availability = new Availability(availabilityID, user.getUserID(), availabilityDate, startTime, endTime, ratePerHour);
        boolean updated = celebrityFanService.updateAvailability(availability);

        if (updated) {
            System.out.println("Availability updated successfully.");
        } else {
            System.out.println("Failed to update availability.");
        }
    }
}
