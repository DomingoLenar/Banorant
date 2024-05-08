package org.amalgam.utils.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageCallback extends Remote { // at the same time act as session manager

    public void broadcastCall(String msg) throws RemoteException;

    public String getUser() throws RemoteException;
}
