package org.amalgam.util.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageCallbackInter extends Remote {

    public void broadcastCall(String msg) throws RemoteException;

    public String getUser() throws RemoteException;
}
