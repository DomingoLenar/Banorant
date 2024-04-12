package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionDAL {

    public boolean registerNewSession(int userID, String date, int duration, boolean isCelebrity){ // insert new session if payment is accepted -> fan
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO sessions (user_id, date, duration, isCelebrity) VALUE (?,?,?,?)");
            stmt.setInt(1, userID);
            stmt.setString(2, date);
            stmt.setInt(3, duration);
            stmt.setBoolean(4, isCelebrity);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Session> getAcceptedSession(int userID){ // list of session where payment status is accepted | celebrity menu & fan menu
        List<Session> sessions = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sessions WHERE user_id = ? ");
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    int fetchedSessionID = rs.getInt("session_id");
                    int fetchedUserID = rs.getInt("user_id");
                    String fetchedDate = rs.getString("date");
                    int fetchedDuration = rs.getInt("duration");
                    sessions.add(new Session(fetchedSessionID, fetchedUserID, fetchedDate, fetchedDuration));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sessions;
    }
}
