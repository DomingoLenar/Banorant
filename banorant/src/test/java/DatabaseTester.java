import org.amalgam.server.dataAccessLayer.DatabaseUtil;
import org.amalgam.server.dataAccessLayer.UserDAL;
import org.amalgam.utils.services.AuthenticationService;
import org.amalgam.utils.services.UserService;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class DatabaseTester {

    @Test
    public void testGetConnection(){
        try {
            DatabaseUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateUser(){
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            UserService userService = (UserService) registry.lookup("userService");
            System.out.println(userService.createUser("test", "123"));
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAuthenticateUser(){
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            AuthenticationService authService = (AuthenticationService) registry.lookup("authService");
            System.out.println(authService.authenticateUser("lou", "123"));
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
