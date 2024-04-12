import org.amalgam.server.dataAccessLayer.DatabaseUtil;
import org.amalgam.server.dataAccessLayer.SessionDAL;
import org.amalgam.server.dataAccessLayer.UserDAL;
import org.amalgam.utils.models.Session;
import org.amalgam.utils.models.User;
import org.amalgam.utils.services.AuthenticationService;
import org.amalgam.utils.services.UserService;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;

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

    @Test
    public void testGetPlayers() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            UserService userService = (UserService) registry.lookup("userService");
            List<User> players = userService.getPlayers();

            for (User player : players){
                System.out.println(player.getUsername());
            }

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAcceptedSession() {
        SessionDAL sessionDAL = new SessionDAL();
        List<Session> sessions = sessionDAL.getAcceptedSession(2);

        for (Session session: sessions) {
            System.out.println(session.getSessionID());
            System.out.println(session.getUserID());
            System.out.println(session.getDate());
            System.out.println(session.getDuration());
        }
    }
}
