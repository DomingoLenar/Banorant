package org.amalgam.client;

import org.amalgam.client.messaging.MessageCallbackImpl;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// NOTE: -> means to be used by

// todo: use messaging service if fan(session) == player(session) where payment element of session and iff payment == accepted
// todo: fix the user interaction of a fan and player
public class Main implements Runnable {
    private UserService userService;
    private AuthenticationService authService;
    private MessageService messageService;
    private CelebrityFanService celebrityFanService;
    private MessageCallbackImpl messageCallback;
    private final Scanner kyb = new Scanner(System.in);

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

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
                handleSessionEntry(user, sessionList.get(selectedSessionIndex));
            } else {
                System.out.println("Invalid session number");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void playersChoice(User user) {
        System.out.println("1. View Players");
        int playerChoice = Integer.parseInt(kyb.nextLine());

        if (playerChoice == 1) {
            try {
                List<User> listOfPlayers = userService.getPlayers();

                if (listOfPlayers.isEmpty()) {
                    System.out.println("No players found.");
                    return;
                }

                displayPlayers(listOfPlayers);

                int selectedPlayerIndex = getSelectedPlayerIndex(listOfPlayers.size());

                if (isValidSelection(selectedPlayerIndex, listOfPlayers.size())) {
                    User selectedPlayer = listOfPlayers.get(selectedPlayerIndex);
                    displayPlayerAvailability(selectedPlayer);
                    //handleSessionCreation(user, selectedPlayer);
                } else {
                    System.out.println("Invalid player selection.");
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void profileManagementFan(User user){
        System.out.println("1. Update Profile");
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

    public void profileManagementCeleb(User user) throws RemoteException {
        System.out.println("1. View Profile");
        System.out.println("2. Update Password");
        System.out.println("3. Edit Availability");
        System.out.println("4. Create Availability");
        System.out.println("5. Delete Profile");

        int profileChoice = Integer.parseInt(kyb.nextLine());
        switch (profileChoice) {
            case 1:

                try {
                    System.out.println("User profile: "+user.getUsername());
                    celebrityFanService.getUserCredentials(user.getUserID());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                break;
            case 2:
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

            case 3:
                editAvailability (user);
                break;
            case 4:
                createAvailability(user);
                break;
            case 5:
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

    public boolean processPayment(User user, double playerRate) {
        try {

            System.out.println("Enter the amount you will pay:");
            int paymentAmount = Integer.parseInt(kyb.nextLine());

            if (paymentAmount < playerRate) {
                System.out.println("Payment amount is less than the player rate. Please pay again.");
                return false;
            }

            boolean paymentRegistered = celebrityFanService.registerAcceptedPayment(user.getUserID(), paymentAmount, Status.Accepted,getCurrentDateTime() );
            if (paymentRegistered) {
                System.out.println("Payment registered successfully.");
                return true;
            } else {
                System.out.println("Failed to register payment.");
                return false;
            }
        } catch (NumberFormatException | RemoteException e) {
            System.out.println("Invalid input format.");
            return false;
        }
    }

    public String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    private void displayPlayers(List<User> listOfPlayers) throws RemoteException {
        System.out.println("List of Players:");
        for (int i = 0; i < listOfPlayers.size(); i++) {
            User player = listOfPlayers.get(i);
//            double playerRate = celebrityFanService.getPlayerRateByPlayerID(player.getUserID());
//            System.out.println((i + 1) + ". " + player.getUsername() + " Rate: " + playerRate);
        }
    }

    private int getSelectedPlayerIndex(int maxIndex) {
        System.out.println("Enter player number to select:");
        return Integer.parseInt(kyb.nextLine()) - 1;
    }

    private boolean isValidSelection(int index, int maxSize) {
        return index >= 0 && index < maxSize;
    }

    @Deprecated
    private void handleSessionCreation(User user, User selectedPlayer) throws RemoteException {
        System.out.println("Creating session for player: " + selectedPlayer.getUsername());

        String date = getDateTimeInput("Enter session date (YYYY-MM-DD-HH-MM-SS):");
        int duration = getDurationInput("Enter session duration (in minutes):");

//        boolean paymentAccepted = processPayment(user, celebrityFanService.getPlayerRateByPlayerID(selectedPlayer.getUserID()));
//
//        if (paymentAccepted) {
            registerSession(user, selectedPlayer, date, duration);
//        } else {
//            System.out.println("Failed to register payment.");
//        }
    }

    private String getDateTimeInput(String prompt) {
        System.out.println(prompt);
        return kyb.nextLine();
    }

    private int getDurationInput(String prompt) {
        System.out.println(prompt);
        return Integer.parseInt(kyb.nextLine());
    }

    private void registerSession(User user, User selectedPlayer, String date, int duration) throws RemoteException {
        int userID = user.getUserID();
        boolean sessionRegisteredForFan = celebrityFanService.registerNewSession(userID, date, duration);
        boolean sessionRegisteredForPlayer = celebrityFanService.registerNewSession(selectedPlayer.getUserID(), date, duration);

        if (sessionRegisteredForFan && sessionRegisteredForPlayer) {
            registerRoomAndBooking(user, selectedPlayer, date);
        } else {
            System.out.println("Failed to register session.");
        }
    }

    private void registerRoomAndBooking(User user, User selectedPlayer, String date) throws RemoteException {
        System.out.println("Payment processing...");

        String roomName = "meeting " + selectedPlayer.getUsername() + " " + user.getUsername();
        int paymentID = celebrityFanService.getPaymentIDByUserID(user.getUserID());

        int sessionID = celebrityFanService.getSessionIDByUserID(user.getUserID());

//        boolean registerBooking = celebrityFanService.registerNewBooking(user.getUserID(), sessionID, availabilityID, paymentID, date);
//        if (registerBooking) {
//            System.out.println("Booking registered successfully.");
//        } else {
//            System.out.println("Failed to register booking.");
//        }

    }


    private void displaySessions(List<Session> sessionList) {
        System.out.println("List of sessions");
        for (int i = 0; i < sessionList.size(); i++) {
            Session session = sessionList.get(i);
            System.out.println("Session Number(" + (i + 1) + ") Date:" + session.getDate());
        }
    }

    private int getSelectedSessionIndex(int maxIndex) {
        System.out.println("Enter the session number: ");
        return Integer.parseInt(kyb.nextLine()) - 1;
    }

    private boolean isValidSessionSelection(int index, int maxSize) {
        return index >= 0 && index < maxSize;
    }

    private void handleSessionEntry(User user, Session selectedSession) throws RemoteException {
        System.out.println("You have entered the session: " + selectedSession.getSessionID());

        String fetchedDate = selectedSession.getDate();

        Thread dateCheckingThread = new Thread(() -> {
            boolean sessionStarted = false;
            while (!sessionStarted) {
                if (getCurrentDateTime().equals(fetchedDate)) {
                    System.out.println("Session is started");
                    sessionStarted = true;
                    try {
                        messageCallback = new MessageCallbackImpl(user.getUsername());
                        messageService.logSession(messageCallback);

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        dateCheckingThread.start();

        while (true) {
            try {
                dateCheckingThread.join();
                while (true) {
                    System.out.print("Type Message: ");
                    String input = kyb.nextLine();
                    if (input.equalsIgnoreCase("exit")) {
                        messageService.logout(messageCallback);
                    } else {
                        messageService.broadcast(messageCallback, messageCallback.getUser() + " has left the chat");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayPlayerAvailability(User player) throws RemoteException {
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

        System.out.println("Enter the date (YYYY/MM/DD) you want to book:");
        String selectedDate = kyb.nextLine();

        System.out.println("Enter the duration in minutes:");
        int durationMinutes = Integer.parseInt(kyb.nextLine());

        adjustStartTime(availabilityList, selectedDate, durationMinutes);
    }

    private void adjustStartTime(List<Availability> availabilityList, String selectedDate, int durationMinutes) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime adjustedStartTime = null;

        for (Availability availability : availabilityList) {
            if (availability.getAvailabilityDate().equals(selectedDate)) {
                LocalTime startTime = LocalTime.parse(availability.getStartTime());
                LocalTime adjustedTime = startTime.plusMinutes(durationMinutes);

                if (adjustedTime.isBefore(LocalTime.parse(availability.getEndTime()))) {
                    adjustedStartTime = adjustedTime;
                    availability.setStartTime(adjustedStartTime.format(dateTimeFormatter));
                } else {
                    System.out.println("Invalid duration. The adjusted end time exceeds the original end time.");
                    return;
                }

                if (adjustedStartTime.isAfter(LocalTime.parse(availability.getStartTime()))) {
                    System.out.println("Adjusted start time: " + adjustedStartTime.format(dateTimeFormatter));
                    // TODO Implement the payment process here please sir
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

        Availability availability = new Availability(0, user.getUserID(), availabilityDate, startTime, endTime, ratePerHour);
        boolean created = celebrityFanService.createAvailability(availability);

        if (created) {
            System.out.println("Schedule created successfully.");
        } else {
            System.out.println("Failed to create availability.");
        }
    }

    private void editAvailability(User user) throws RemoteException {
        System.out.println("Enter availability ID to edit:");
        int availabilityID = Integer.parseInt(kyb.nextLine());

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
