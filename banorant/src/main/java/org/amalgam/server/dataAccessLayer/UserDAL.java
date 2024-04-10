package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAL {
    public User authenticateUser(String username, String password) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // User found, construct and return a User object
                    int userId = rs.getInt("user_id");
                    String fetchedUsername = rs.getString("username");
                    String fetchedPassword = rs.getString("password");
                    boolean isCelebrity = rs.getBoolean("isCelebrity");
                    // You can retrieve other user information similarly from the ResultSet
                    return new User(userId, fetchedUsername, fetchedPassword, isCelebrity);
                } else {
                    // User not found, return null
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getUserIdByUsername(String username) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // User found, return the user ID
                    return rs.getInt("user_id");
                } else {
                    // User not found, return -1
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<User> getPlayers() { // fetch list of players -> fan
        return null;
    }
}
