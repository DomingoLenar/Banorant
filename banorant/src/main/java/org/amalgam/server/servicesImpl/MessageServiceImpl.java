package org.amalgam.server.servicesImpl;

import org.amalgam.utils.services.MessageCallback;
import org.amalgam.utils.services.MessageService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;

public class MessageServiceImpl extends UnicastRemoteObject implements MessageService {
    LinkedList<MessageCallback> users = new LinkedList<>();
    public MessageServiceImpl() throws RemoteException {
    }
    @Override
    public void logSession(MessageCallback messageCallbackInter) throws RemoteException {
        users.add(messageCallbackInter);
        System.out.println("Your session has started...");
        for(int x = 0; x<users.size();x++){
            users.get(x).broadcastCall(messageCallbackInter.getUser()+ " logged in...");

        }
    }

    @Override
    public void broadcast(MessageCallback messageCallback, String msg) throws RemoteException {
        for(int x = 0; x<users.size();x++){
            users.get(x).broadcastCall(messageCallback.getUser()+": "+msg);
        }
    }

    @Override
    public void logout(MessageCallback messageCallback) throws RemoteException {
        users.remove(messageCallback); // removes the instance of message callback of a user
        for(int x = 0; x<users.size();x++){
            users.get(x).broadcastCall(messageCallback.getUser()+" has logged out");
        }
    }
}
