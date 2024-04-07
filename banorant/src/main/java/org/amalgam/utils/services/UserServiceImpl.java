package org.amalgam.utils.services;

import org.amalgam.server.dataAccessLayer.DatabaseUtil;
import org.amalgam.server.dataAccessLayer.UserDAL;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserServiceImpl extends UnicastRemoteObject implements UserService, Serializable {
    private UserDAL userDAL;
    public UserServiceImpl() throws RemoteException {
        super();
        this.userDAL = new UserDAL();
    }
    @Override
    public boolean createUser(String username, String password) throws RemoteException {
        try(Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (username, password) VALUES (?,?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUser(String username, String newPassword) throws RemoteException {
        try(Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET password = ? WHERE username = ?");
            stmt.setString(1, username);
            stmt.setString(2, newPassword);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteUser(String username) throws RemoteException {
        try(Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM user WHERE username = ?");
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getUserIdByUsername(String username) throws RemoteException {
        return userDAL.getUserIdByUsername(username);
    }
}
