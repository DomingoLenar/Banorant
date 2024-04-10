package org.amalgam.server.services;

import org.amalgam.util.interfaces.MessageCallbackInter;
import org.amalgam.util.interfaces.MessageInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class MessageInterfaceImpl extends UnicastRemoteObject implements MessageInterface {
    LinkedList<MessageCallbackInter> users = new LinkedList<>();

    public MessageInterfaceImpl() throws RemoteException {
    }


    @Override
    public void logSession(MessageCallbackInter messageCallbackInter) throws RemoteException {
        users.add(messageCallbackInter);
        for(int x = 0; x<users.size();x++){
            users.get(x).broadcastCall(messageCallbackInter.getUser()+ "Logged in...");
        }
    }

    @Override
    public void broadcast(MessageCallbackInter messageCallback, String msg) throws RemoteException {
        for(int x = 0; x<users.size();x++){
            users.get(x).broadcastCall(messageCallback.getUser()+": "+msg);
        }
    }
}
