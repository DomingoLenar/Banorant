import org.amalgam.server.dataAccessLayer.DatabaseUtil;
import org.amalgam.server.dataAccessLayer.UserDAL;
import org.junit.Test;

import java.sql.Connection;
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
        UserDAL userDAL = new UserDAL();
        userDAL.createUser("lou", "123");
    }
}
