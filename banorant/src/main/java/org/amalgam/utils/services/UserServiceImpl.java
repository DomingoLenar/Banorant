package org.amalgam.utils.services;

import org.amalgam.server.dataAccessLayer.UserDAL;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserServiceImpl extends UnicastRemoteObject implements UserService, Serializable {
    private UserDAL userDAL;
    public UserServiceImpl() throws RemoteException {
        super();
        this.userDAL = new UserDAL();
    }

    // crud oper in dbms
    @Override
    public boolean createUser(String username, String password) throws RemoteException {
        userDAL.createUser(username, password);
        return true;
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
