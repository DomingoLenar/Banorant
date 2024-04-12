package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SessionDAL {

    public boolean registerNewSession(int fanID, int playerID, LocalDate date, int duration){ // insert new session if payment is accepted -> fan
        return false;
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
