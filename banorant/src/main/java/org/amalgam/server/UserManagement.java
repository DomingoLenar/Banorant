package org.amalgam.server;

import org.amalgam.utils.Binder;
import org.amalgam.utils.services.UserService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.Scanner;

public class UserManagement {
   static UserService userStub;
   static Scanner kInput = new Scanner(System.in);

    public static void main(String[] args) {
        getUserStub();
        try {
            LinkedList<String> forDeletion = userStub.fetchForDeletion();
            System.out.println("Select a user to delete: ");
            int x = 0;
            forDeletion.forEach(s -> {
                System.out.println(forDeletion.indexOf(s)+1 +" "+s);
            });

            System.out.print("Select number of user to delete");
            int num = kInput.nextInt();
            String username = forDeletion.get(num-1);
            userStub.deleteUser(username);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void getUserStub(){
        try{
            Registry reg = LocateRegistry.getRegistry(1099);
            userStub = (UserService) reg.lookup(Binder.userService);
        }catch(RemoteException e){
            e.printStackTrace();
        }catch(NotBoundException e){
            e.printStackTrace();
        }
    }
}
