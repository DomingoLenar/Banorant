import org.amalgam.utils.Binder;
import org.amalgam.utils.models.User;
import org.amalgam.utils.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class RequestDeletionTester {
    static UserService userStub;
    @BeforeAll
    public static void retrieveUserStub(){
        try {
            Registry reg = LocateRegistry.getRegistry(1099);
            userStub = (UserService) reg.lookup(Binder.userService);
        }catch(RemoteException e){
            e.printStackTrace();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void addRequestForDeletion(){
        try {
            boolean result = userStub.requestUserDeletion("lestat");
            Assertions.assertTrue(result);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void fetchListOfForDeletion(){
        try{
            LinkedList<String> forDeletion;
            forDeletion = userStub.fetchForDeletion();
            Assertions.assertNotNull(forDeletion);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
