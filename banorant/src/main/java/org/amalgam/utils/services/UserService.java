package org.amalgam.utils.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {
    boolean createUser(String username, String password) throws RemoteException;
    boolean updateUser(String username, String newPassword) throws RemoteException;
    boolean deleteUser(String username) throws RemoteException;
    int getUserIdByUsername(String username) throws RemoteException;

}
