package org.amalgam.client.messaging;

import org.amalgam.util.interfaces.MessageInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class Test {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Scanner kInput = new Scanner(System.in);
        Registry reg = LocateRegistry.getRegistry("localhost",1099);
        MessageInterface userStub = (MessageInterface) reg.lookup("messageService");

        System.out.print("Enter username:");
        String username = kInput.nextLine();

        MessageCallbackImpl callback = new MessageCallbackImpl(username);
        userStub.logSession(callback);

        boolean repeat = true;
        while(repeat){
            System.out.print("Type Message: ");
            userStub.broadcast(callback,kInput.nextLine());

        }
    }
}
