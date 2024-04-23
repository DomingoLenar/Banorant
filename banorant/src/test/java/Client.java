import org.amalgam.client.Main;
import org.amalgam.client.messaging.MessageCallbackImpl;
import org.amalgam.utils.services.MessageService;
import org.junit.jupiter.api.Test;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    @Test
    public void testMessaging() {
        try {
            Scanner kInput = new Scanner(System.in);
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            MessageService userStub = (MessageService) reg.lookup("messageService");

            System.out.print("Enter username:");
            String username = kInput.nextLine();

            MessageCallbackImpl callback = new MessageCallbackImpl(username);
            userStub.logSession(callback);

            boolean repeat = true;
            while (repeat) {
                System.out.print("Type Message: ");
                userStub.broadcast(callback, kInput.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetCurrentDateTime() {
        Main main = new Main();
        System.out.println(main.getCurrentDateTime());
    }

//    public void main(String[] args) {
//        String currentDate = "2024-04-13 01:19:00";
//        Thread dateCheckingThread = new Thread(() -> {
//            boolean sessionStarted = false;
//            while (!sessionStarted) {
//                System.out.println(testGetCurrentDateTime());
//                if (testGetCurrentDateTime().equals(currentDate)) {
//                    System.out.println("Session is started");
//                    sessionStarted = true;
//                } else {
//                    System.out.println("Session is not started");
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        });
//        dateCheckingThread.start();
//        try {
//            dateCheckingThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("standby");
//        while (true){
//
//        }
//    }
}
