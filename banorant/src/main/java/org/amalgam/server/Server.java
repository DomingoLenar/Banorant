package org.amalgam.server;

import org.amalgam.utils.services.*;
import org.amalgam.utils.Binder;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            UserService userService = new UserServiceImpl();
            AuthenticationService authService = new AuthenticationServiceImpl();
            MessageService messageService = new MessageServiceImpl();
            CelebrityFanService celebrityFanService = new CelebrityFanServiceImpl();


            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(Binder.userService, userService);
            registry.rebind(Binder.authService, authService);
            registry.rebind(Binder.messageService, messageService);
            registry.rebind(Binder.celebrityFanService, celebrityFanService);

            System.out.println("Server is running and waiting for client calls...");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
