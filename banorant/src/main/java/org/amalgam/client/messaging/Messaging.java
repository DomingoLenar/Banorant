package org.amalgam.client.messaging;

import org.amalgam.client.callbacks.MessageCallbackImpl;
import org.amalgam.utils.Binder;
import org.amalgam.utils.services.MessageService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

//Should be run/used in another thread
public class Messaging implements Runnable {
    MessageService messageStub;
    Scanner kInput = new Scanner(System.in);
    public static void main(String[] args) {
        Messaging program = new Messaging();
        program.run();
    }

    public void run(){
        getMessageStub();
        try {
            MessageCallbackImpl messageCallback;
            System.out.print("Please enter a username: ");
            String username = kInput.nextLine();
            messageCallback = new MessageCallbackImpl(username);
            messageStub.logSession(messageCallback);
            System.out.println("Chat Away!");
            while(true){
                String message = kInput.nextLine();
                if(message.toLowerCase().equals("exit")){
                    messageStub.logout(messageCallback);
                    break;
                }
                messageStub.broadcast(messageCallback, message);
            }
            System.exit(0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void getMessageStub(){
        try{
            Registry reg = LocateRegistry.getRegistry(1099);
            messageStub = (MessageService) reg.lookup(Binder.messageService);
        }catch(RemoteException e){
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }


}
