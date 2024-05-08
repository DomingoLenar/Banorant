package org.amalgam.utils.services;

import org.amalgam.utils.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationService extends Remote {
    User authenticateUser(String username, String password) throws RemoteException;
}
