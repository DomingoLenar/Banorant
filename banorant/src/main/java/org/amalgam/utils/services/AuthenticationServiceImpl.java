package org.amalgam.utils.services;

import org.amalgam.server.dataAccessLayer.UserDAL;
import org.amalgam.utils.models.User;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationServiceImpl extends UnicastRemoteObject implements AuthenticationService, Serializable {
    private UserDAL userDAL; // Injected UserDAL instance
    public AuthenticationServiceImpl() throws RemoteException {
        super();
        this.userDAL = new UserDAL(); // Automatically create and inject UserDAL instance
    }

    @Override
    public User authenticateUser(String username, String password) throws RemoteException {
        return userDAL.authenticateUser(username, password);
    }
}
