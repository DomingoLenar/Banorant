package org.amalgam.client;

import org.amalgam.utils.models.User;
import org.amalgam.utils.services.AuthenticationService;
import org.amalgam.utils.services.UserService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    static Scanner kyb = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            UserService userService = (UserService) registry.lookup("userService");
            AuthenticationService authService = (AuthenticationService) registry.lookup("authService");
            String username;
            String password;
            do {
                System.out.println("username: ");
                username = kyb.nextLine();
                System.out.println("password: ");
                password = kyb.nextLine();

                if (username != null && password != null) {
                    User currUser = authService.authenticateUser(username, password);

                    if (currUser != null) {
                        System.out.println("true");
                    } else {
                        System.out.println("false");
                    }

                }

            } while (username == null || password == null);
            System.out.println("standby");
            String input;
            while (true) {
                input = kyb.nextLine();
                if (input.equals("exit")){
                    System.exit(0);
                }
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
