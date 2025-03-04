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

    public List<Availability> getAvailabilityByUserID(int userId) {
        List<Availability> availabilityList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM availability WHERE userID = ?");
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int availabilityID = rs.getInt("availabilityID");
                    String availabilityDate = rs.getString("availabilityDate");
                    String startTime = rs.getString("startTime");
                    String endTime = rs.getString("endTime");
                    int ratePerHour = rs.getInt("ratePerHour");
                    availabilityList.add(new Availability(availabilityID, userId, availabilityDate, startTime, endTime, ratePerHour));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return availabilityList;
    }



    public boolean createAvailability(Availability availability) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO availability (userID, availabilityDate, startTime, endTime, ratePerHour) " +
                             "VALUES (?, ?, ?, ?, ?)")) {
            stmt.setInt(1, availability.getPlayerID());
            stmt.setString(2, availability.getAvailabilityDate());
            stmt.setString(3, availability.getStartTime());
            stmt.setString(4, availability.getEndTime());
            stmt.setDouble(5, availability.getRatePerHour());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStartingTime(String updatedStartTime, int availabilityID){
        System.out.println(updatedStartTime);
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE availability SET startTime = ? WHERE availabilityID = ?")
        ){
            stmt.setString(1, updatedStartTime);
            stmt.setInt(2, availabilityID);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e){}

        return false;
    }

    public boolean updateAvailability(Availability availability) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE availability SET availabilityDate = ?, startTime = ?, endTime = ?, ratePerHour = ? " +
                             "WHERE availabilityID = ? AND userID = ?")) {
            stmt.setString(1, availability.getAvailabilityDate());
            stmt.setString(2, availability.getStartTime());
            stmt.setString(3, availability.getEndTime());
            stmt.setDouble(4, availability.getRatePerHour());
            stmt.setInt(5, availability.getAvailabilityID());
            stmt.setInt(6, availability.getPlayerID());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRateByUserID (int userID){
        int ratePerHour = 0;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM availability WHERE userID = ?")){
             stmt.setInt(1,userID);

             try (ResultSet rs = stmt.executeQuery()){
                 if (rs.next()){
                   ratePerHour = rs.getInt("ratePerHour");
                 }
             }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ratePerHour;
    }

    public Availability getAvailabilityByID(int availabilityID) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM availability WHERE availabilityID = ?")) {
            stmt.setInt(1, availabilityID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userID = rs.getInt("userID");
                    String availabilityDate = rs.getString("availabilityDate");
                    String startTime = rs.getString("startTime");
                    String endTime = rs.getString("endTime");
                    int ratePerHour = rs.getInt("ratePerHour");
                    return new Availability(availabilityID, userID, availabilityDate, startTime, endTime, ratePerHour);
                } else {
                    return null; // Return null if no availability found with the given ID
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
