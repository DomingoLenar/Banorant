package org.amalgam.server.dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// todo: create methods for accessing 'availability table' data to be used by clients
public class AvailabilityDAL {
    public boolean getUserCredentials(int userID) {

        try (Connection conn = DatabaseUtil.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
