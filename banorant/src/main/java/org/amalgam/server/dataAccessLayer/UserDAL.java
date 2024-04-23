package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAL {

    public User authenticateUser(String username, String password) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("userID");
                    String fetchedUsername = rs.getString("username");
                    String fetchedPassword = rs.getString("password");
                    boolean isCelebrity = rs.getBoolean("isCelebrity");
                    return new User(userId, fetchedUsername, fetchedPassword, isCelebrity);
                } else {
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
             PreparedStatement stmt = conn.prepareStatement("SELECT userID FROM user WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userID");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<User> getPlayers() {
        List<User> players = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE isCelebrity = ?")) {
            stmt.setBoolean(1, true);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("userID");
                    String fetchedUsername = rs.getString("username");
                    String fetchedPassword = rs.getString("password");
                    boolean isCelebrity = rs.getBoolean("isCelebrity");
                    players.add(new User(userId, fetchedUsername, fetchedPassword, isCelebrity));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return players;
    }
}
