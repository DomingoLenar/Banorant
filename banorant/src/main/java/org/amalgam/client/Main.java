package org.amalgam.client;

import org.amalgam.utils.Status;
import org.amalgam.utils.models.Room;
import org.amalgam.utils.models.Session;
import org.amalgam.utils.services.*;
import org.amalgam.utils.Binder;
import org.amalgam.utils.models.User;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

// NOTE: -> means to be used by

// todo: use messaging service if fan(session) == player(session) where payment element of session and iff payment == accepted
public class Main implements Runnable {
    private static UserService userService;
    private static AuthenticationService authService;
    private static MessageService messageService;
    private static CelebrityFanService celebrityFanService;
    private static final Scanner kyb = new Scanner(System.in);

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
        } while (username == null || password == null);

        if (user != null) {
            boolean isCelebrity = user.isCelebrity();
            if (isCelebrity) {
                menuCelebrity(user);
            } else {
                menuFan(user);
            }
        }

    }

    private void menuFan(User user) throws RemoteException {
        while (true) {
            System.out.println("Choose from the ff.");
            System.out.println("1. Session");
            System.out.println("2. Players"); // prompt list of players -> then scheduling system correspond to celeb availability, this involves rate
            System.out.println("3. Profile"); // common

            int choice = Integer.parseInt(kyb.nextLine());
            switch (choice){
                case 1:
                    try {
                        //Enter the session number, if user press any of the sessions number, then it will display that the fan entered the room
                        List<Session> sessionList = celebrityFanService.getAcceptedSession(user.getUserID());
                        if (sessionList.isEmpty()){
                            System.out.println("No accepted sessions found");
                        } else {
                            System.out.println("List of accepted sessions");
                            for (int i=0; i<sessionList.size();i++){
                                Session session = sessionList.get(i);
                                System.out.println((i+1) + ". "+session.getSessionID());
                            }

                            System.out.println("Enter the session number: ");
                            int sessionNo = Integer.parseInt(kyb.nextLine());

                            if (sessionNo > 0 && sessionNo <= sessionList.size()) {

                                System.out.println("You have entered the session: " + sessionList.get(sessionNo - 1).getSessionID());
                                int paymentId = celebrityFanService.getPaymentIDByUserID(user.getUserID());
                                Room room = celebrityFanService.getRoomByFanAndPlayer(paymentId);
                                System.out.println("Room name: " + room.getName() + "\nRoom ID:" +room.getRoomID() );

                            } else {
                                System.out.println("Invalid session number");
                            }

                        }

                    } catch (RemoteException e){
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    System.out.println("1. View Players");
                    int playerChoice = Integer.parseInt(kyb.nextLine());
                    if (playerChoice == 1) {
                        try {
                            List<User> listOfPlayers = userService.getPlayers();
                            if (listOfPlayers.isEmpty()) {
                                System.out.println("No players found.");
                            } else {
                                System.out.println("List of Players:");
                                for (int i = 0; i < listOfPlayers.size(); i++) {
                                    User player = listOfPlayers.get(i);
                                    double playerRate = celebrityFanService.getPlayerRateByPlayerID(player.getUserID());
                                    System.out.println((i + 1) + ". " + player.getUsername() + " Rate: " + playerRate);
                                }
                                System.out.println("Enter player number to select:");
                                int selectedPlayerIndex = Integer.parseInt(kyb.nextLine()) - 1;
                                if (selectedPlayerIndex >= 0 && selectedPlayerIndex < listOfPlayers.size()) {
                                    User selectedPlayer = listOfPlayers.get(selectedPlayerIndex);
                                    System.out.println("Creating session for player: " + selectedPlayer.getUsername());
                                    System.out.println("Enter session date (YYYY-MM-DD-HH-MM-SS):");
                                    String date = kyb.nextLine();
                                    System.out.println("Enter session duration (in minutes):");
                                    int duration = Integer.parseInt(kyb.nextLine());

                                    System.out.println("Payment processing...");
                                    boolean paymentAccepted = processPayment(user, celebrityFanService.getPlayerRateByPlayerID(selectedPlayer.getUserID()));

                                    if (paymentAccepted) {
                                        // If payment registration is successful, register the session
                                        int userID = user.getUserID();
                                        boolean sessionRegistered = celebrityFanService.registerNewSession(userID, date, duration, selectedPlayer.isCelebrity());

                                        if (sessionRegistered) {
                                            System.out.println("Session registered successfully.");
                                            String roomName = "meeting "+selectedPlayer.getUsername() +" "+user.getUsername();
                                            int paymentID = celebrityFanService.getPaymentIDByUserID(userID);
                                            boolean roomRegistered = celebrityFanService.registerNewRoom(roomName, paymentID);

                                            if (roomRegistered){
                                                System.out.println("Room registered successfully.");

                                                int sessionID = celebrityFanService.getSessionIDByUserID(user.getUserID());
                                                int roomID = celebrityFanService.getRoomIDByPaymentID(paymentID);

                                                boolean registerBooking = celebrityFanService.registerNewBooking(userID,sessionID,roomID,paymentID,date);

                                                if (registerBooking){
                                                    System.out.println("Booking registered successfully. ");
                                                } else System.out.println("Failed to register booking");

                                            } else System.out.println("Failed to register room. ");

                                        } else {
                                            System.out.println("Failed to register session.");
                                        }
                                    } else {
                                        System.out.println("Failed to register payment.");
                                    }
                                } else {
                                    System.out.println("Invalid player selection.");
                                }
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;


                case 3:
                    System.out.println("1. View Profile");
                    System.out.println("2. Update Profile");
                    System.out.println("3. Delete Profile");

                    int profileChoice = Integer.parseInt(kyb.nextLine());
                    switch (profileChoice) {
                        case 1:
                            //To put logic later
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
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }

    private void menuCelebrity(User user) {
        while (true) {
            System.out.println("Choose from the following options:");
            System.out.println("1. Session");
            System.out.println("2. Profile");

            int choice = Integer.parseInt(kyb.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("1. View Sessions");
                    System.out.println("2. Update Session");
                    int scheduleChoice = Integer.parseInt(kyb.nextLine());
                    switch (scheduleChoice) {
                        case 1:
                            try {
                                //Enter the session number, if user press any of the sessions number, then it will display that the fan entered the room
                                List<Session> sessionList = celebrityFanService.getAcceptedSession(user.getUserID());
                                if (sessionList.isEmpty()){
                                    System.out.println("No accepted sessions found");
                                } else {
                                    System.out.println("List of accepted sessions");
                                    for (int i=0; i<sessionList.size();i++){
                                        Session session = sessionList.get(i);
                                        System.out.println((i+1) + ". "+session.getSessionID());
                                    }

                                    System.out.println("Enter the session number: ");
                                    int sessionNo = Integer.parseInt(kyb.nextLine());

                                    if (sessionNo > 0 && sessionNo <= sessionList.size()) {

                                        System.out.println("You have entered the session: " + sessionList.get(sessionNo - 1).getSessionID());
                                        int paymentId = celebrityFanService.getPaymentIDByUserID(user.getUserID());
                                        Room room = celebrityFanService.getRoomByFanAndPlayer(paymentId);
                                        System.out.println("Room name: " + room.getName() + "\nRoom ID:" +room.getRoomID() );

                                    } else {
                                        System.out.println("Invalid session number");
                                    }

                                }

                            } catch (RemoteException e){
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            // To put logic later
                            break;
                        default:
                            System.out.println("Invalid choice. Please choose a valid option for schedule.");
                    }
                    break;
                case 2:
                    System.out.println("1. View Profile");
                    System.out.println("2. Update Password");
                    System.out.println("3. Delete Profile");

                    int profileChoice = Integer.parseInt(kyb.nextLine());
                    switch (profileChoice) {
                        case 1:
                            //To put logic later

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
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }


    private void signin(){
        String username;
        String password;
        do {
            System.out.println("Input username: ");
            username = kyb.nextLine();
            System.out.println("Input password: ");
            password = kyb.nextLine();

            if (username != null && password != null) {
                try {
                    userService.createUser(username, password);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Please input accordingly");
            }
        } while (username == null || password == null);
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

    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }
}
