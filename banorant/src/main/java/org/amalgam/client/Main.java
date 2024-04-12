package org.amalgam.client;

import org.amalgam.utils.services.MessageService;
import org.amalgam.utils.Binder;
import org.amalgam.utils.models.User;
import org.amalgam.utils.services.AuthenticationService;
import org.amalgam.utils.services.UserService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

// NOTE: -> means to be used by

// todo: use messaging service if fan(session) == player(session) where payment element of session and iff payment == accepted
public class Main implements Runnable {
    private static UserService userService;
    private static AuthenticationService authService;
    private static MessageService messageService;
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
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        index();
    }

    private void index() {
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

    private void login() {
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

        System.out.println("standby");
        while (true) { // remove later

            if (user != null) {
                boolean isCelebrity = user.isCelebrity();

                if (isCelebrity) {
                    menuCelebrity(user);
                } else {
                    menuFan(user);
                }
            }
        }
    }

    private void menuFan(User user) {
        while (true) {
            System.out.println("Choose from the ff.");
            System.out.println("1. Messages");
            System.out.println("2. Players"); // prompt list of players -> then scheduling system correspond to celeb availability, this involves rate
            System.out.println("3. Profile"); // common

            int choice = Integer.parseInt(kyb.nextLine());
            switch (choice){
                case 1:
                    System.out.println("1. View Messages");
                    System.out.println("2. Send Message");
                    System.out.println("3. Manage Messages");
                    int messagesChoice = Integer.parseInt(kyb.nextLine());
                       switch (messagesChoice){
                           case 1: // To put logic later

                           case 2: // To put logic later

                           case 3: // To put logic later
                       }
                    break;
                case 2:
                    System.out.println("1. View Players");
                    System.out.println("2. Schedule Corresponding to Players");
                    int playerChoice = Integer.parseInt(kyb.nextLine());
                    if (playerChoice == 1){
                        try {
                            List<User> listOfPlayers = userService.getPlayers();
                            if (listOfPlayers.isEmpty()) {
                                System.out.println("No players found.");
                            } else {
                                System.out.println("List of Players:");
                                for (int i = 0; i < listOfPlayers.size(); i++) {
                                    User player = listOfPlayers.get(i);
                                    System.out.println((i + 1) + ". " + player.getUsername());
                                }
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // To put logic later
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
            System.out.println("1. Messages");
            System.out.println("2. Schedules");
            System.out.println("3. Profile");

            int choice = Integer.parseInt(kyb.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("1. View Messages");
                    System.out.println("2. Send Message");
                    System.out.println("3. Manage Messages");
                    int messagesChoice = Integer.parseInt(kyb.nextLine());
                    switch (messagesChoice) {
                        case 1:
                            // To put logic later
                            break;
                        case 2:
                            // To put logic later
                            break;
                        case 3:
                            // To put logic later
                            break;
                        default:
                            System.out.println("Invalid choice. Please choose a valid option for messages.");
                    }
                    break;
                case 2:
                    System.out.println("1. View Schedule");
                    System.out.println("2. Update Schedule");
                    int scheduleChoice = Integer.parseInt(kyb.nextLine());
                    switch (scheduleChoice) {
                        case 1:
                            // To put logic later
                            break;
                        case 2:
                            // To put logic later
                            break;
                        default:
                            System.out.println("Invalid choice. Please choose a valid option for schedule.");
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
                                        // You might want to exit or redirect after profile deletion
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


}
