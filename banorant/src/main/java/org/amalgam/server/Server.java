package org.amalgam.server;

import org.amalgam.utils.services.MessageServiceImpl;
import org.amalgam.utils.Binder;
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
            MessageServiceImpl messageService = new MessageServiceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(Binder.userService, userService);
            registry.rebind(Binder.authService, authService);
            registry.rebind(Binder.messageService, messageService);

            System.out.println("Server is running and waiting for client calls...");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
