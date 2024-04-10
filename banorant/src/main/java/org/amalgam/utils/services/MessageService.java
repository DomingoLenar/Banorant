package org.amalgam.utils.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageService extends Remote {

    public void logSession(MessageCallback messageCallbackInter) throws RemoteException;

    public void broadcast(MessageCallback messageCallback, String msg) throws RemoteException;
}
