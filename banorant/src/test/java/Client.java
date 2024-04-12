import org.amalgam.client.messaging.MessageCallbackImpl;
import org.amalgam.utils.services.MessageService;
import org.junit.Test;

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


}
