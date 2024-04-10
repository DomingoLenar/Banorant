package org.amalgam.client.messaging;

import org.amalgam.util.interfaces.MessageCallbackInter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MessageCallbackImpl extends UnicastRemoteObject implements MessageCallbackInter {
    String username;

    protected MessageCallbackImpl(String username) throws RemoteException {
        this.username = username;
    }

    @Override
    public void broadcastCall(String msg) throws RemoteException {
        System.out.println(msg);
    }

    @Override
    public String getUser() throws RemoteException {
        return this.username;
    }
}
