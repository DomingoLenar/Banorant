package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionDAL {

    public boolean registerNewSession(int userID, String date, int duration){ // insert new session if payment is accepted -> fan
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO session (userID, bookingDate, duration) VALUE (?,?,?)");
            stmt.setInt(1, userID);
            stmt.setString(2, date);
            stmt.setInt(3, duration);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Session> getAcceptedSession(int userID){ // list of session where payment status is accepted | celebrity menu & fan menu
        List<Session> sessions = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM session WHERE userID = ? ");
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    int fetchedSessionID = rs.getInt("sessionID");
                    int fetchedUserID = rs.getInt("userID");
                    String fetchedDate = rs.getString("bookingDate");
                    int fetchedDuration = rs.getInt("duration");
                    sessions.add(new Session(fetchedSessionID, fetchedUserID, fetchedDate, fetchedDuration));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sessions;
    }

    public int getSessionIDByUserID (int userID)  {
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT sessionID FROM session WHERE userID = ?");
            stmt.setInt(1,userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                return rs.getInt("sessionID");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

}
