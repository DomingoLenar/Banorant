package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDAL {
    public boolean registerAcceptedPayment(int fanID, int totalAmount, Status status, String paymentDate) { // payment processing is in client side but should be done by PSP third party
        try (Connection conn = DatabaseUtil.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO payment (userID, totalAmount, status, paymentDate) VALUE " +
                    "(?,?,?,?)");
            stmt.setInt(1, fanID);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, status.toString());
            stmt.setString(4, paymentDate);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPaymentIDByUserID(int userID) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT paymentID FROM payment WHERE userID = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("paymentID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0; // Return 0 if no payment ID found for the user ID
    }

    public int getPaymentIDByDate(String date) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT payment_id FROM payments WHERE payment_date = ?");
            stmt.setString(1, date);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("payment_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0; // Return 0 if no payment ID found for the user ID
    }

}
