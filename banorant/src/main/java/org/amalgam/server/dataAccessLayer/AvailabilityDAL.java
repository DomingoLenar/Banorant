package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.Availability;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Availability> getAvailabilityByUserID(int userId) {
        List<Availability> availabilityList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM availability WHERE playerID = ?");
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int availabilityID = rs.getInt("availabilityID");
                    String availabilityDate = rs.getString("availabilityDate");
                    String startTime = rs.getString("startTime");
                    String endTime = rs.getString("endTime");
                    double ratePerHour = rs.getDouble("ratePerHour");
                    availabilityList.add(new Availability(availabilityID, userId, availabilityDate, startTime, endTime, ratePerHour));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return availabilityList;
    }
}
