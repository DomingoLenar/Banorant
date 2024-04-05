package org.amalgam.server.services;

import org.amalgam.server.models.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationServiceImpl extends UnicastRemoteObject implements AuthenticationService, Serializable {
    public AuthenticationServiceImpl() throws RemoteException {
    }

    @Override
    public User authenticateUser(User user) throws RemoteException {
        return null;
    }
}
