package org.amalgam.server.services;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserServiceImpl extends UnicastRemoteObject implements UserService, Serializable {
    public UserServiceImpl() throws RemoteException {
    }

    @Override
    public boolean createUser(String username, String password) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateUser(String username, String newPassword) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteUser(String username) throws RemoteException {
        return false;
    }
}
