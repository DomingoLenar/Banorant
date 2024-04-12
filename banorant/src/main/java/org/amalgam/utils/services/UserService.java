package org.amalgam.utils.services;

import org.amalgam.utils.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserService extends Remote {
    boolean createUser(String username, String password) throws RemoteException;
    boolean updateUser(String username, String newPassword) throws RemoteException;
    boolean deleteUser(String username) throws RemoteException;
    int getUserIdByUsername(String username) throws RemoteException;
    List<User> getPlayers() throws RemoteException;

}
