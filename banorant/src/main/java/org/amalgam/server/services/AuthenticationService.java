package org.amalgam.server.services;

import org.amalgam.server.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationService extends Remote {
    User authenticateUser(User user) throws RemoteException;
}
