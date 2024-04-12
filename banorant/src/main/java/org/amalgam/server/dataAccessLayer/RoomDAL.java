package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RoomDAL {
    public Room getRoomByFanAndPlayer(int paymentID) { // room is created iff payment is accepted
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE payment_id = ?");
            stmt.setInt(1, paymentID);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    int fetchedRoomID = rs.getInt("room_id");
                    String fetchedRoomName = rs.getString("name");
                    String fetchedDate = rs.getString("date");
                    int fetchedPaymentID = rs.getInt("payment_id");
                    return new Room(fetchedRoomID, fetchedRoomName, fetchedDate, fetchedPaymentID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Room getRoomByDate(String date) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE date = ?");
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    int roomID = rs.getInt("room_id");
                    String roomName= rs.getString("name");
                    String fetchedDate =rs.getString("date");
                    int paymentID = rs.getInt("payment_id");
                    return new Room(roomID,roomName,fetchedDate,paymentID);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean registerNewRoom(String room_name, int paymentID, String date) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO rooms (name, payment_id, date) VALUE (?,?,?)");
            stmt.setString(1,room_name);
            stmt.setInt(2, paymentID);
            stmt.setString(3,date);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRoomIDByPaymentID (int paymentID){
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT room_id FROM rooms WHERE payment_id = ?");
            stmt.setInt(1, paymentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("room_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0; // Return 0 if no payment ID found for the user ID
    }




}
