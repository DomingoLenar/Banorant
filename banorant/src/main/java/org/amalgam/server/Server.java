package org.amalgam.server;

import org.amalgam.utils.services.AuthenticationServiceImpl;
import org.amalgam.utils.services.UserService;
import org.amalgam.utils.services.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            UserService userService = new UserServiceImpl();
            AuthenticationServiceImpl authService = new AuthenticationServiceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("userService", userService);
            registry.rebind("authService", authService);

            System.out.println("Server is running and waiting for client calls...");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
