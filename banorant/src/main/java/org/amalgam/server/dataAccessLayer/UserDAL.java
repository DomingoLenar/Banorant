package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.User;

import java.security.InvalidParameterException;
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

    /**
     * Method is used to delete a user based on either id or username, provide only on of each. If you will use this to
     * delete based on username, set the parameter of id to -1 otherwise set username to null and have id be a value
     * greater than 0
     *
     * @param id            id of username
     * @param username      username of the user
     * @return              boolean, true if the query is successful
     * @throws InvalidParameterException    thrown parameter usage is invalid
     */
    public boolean deleteUser(int id, String username) throws InvalidParameterException {
        try(Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = null;
            if(username != null){
                stmt = conn.prepareStatement("DELETE FROM user WHERE username = ?");
                stmt.setString(1, username);
            } else if (id > 0){
                stmt = conn.prepareStatement("DELETE FROM user WHERE userID = ?");
                stmt.setInt(1, id);
            } else {
                throw new InvalidParameterException("Invalid Parameter please refer to documentation of this method");
            }
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
