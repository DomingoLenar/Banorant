package org.amalgam.util.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageInterface extends Remote {

    public void logSession(MessageCallbackInter messageCallbackInter) throws RemoteException;

    public void broadcast(MessageCallbackInter messageCallback, String msg) throws RemoteException;
}
