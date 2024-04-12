package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAL {
    public boolean registerAcceptedPayment(int fanID, int sessionID, int totalAmount, Status status, String paymentDate) { // payment processing is in client side but should be done by PSP third party
        try (Connection conn = DatabaseUtil.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO payments (user_id, session_id, amount, status, payment_date) VALUE " +
                    "(?,?,?,?,?)");
            stmt.setInt(1, fanID);
            stmt.setInt(2, sessionID);
            stmt.setDouble(3, totalAmount);
            stmt.setString(4, status.toString());
            stmt.setString(5, paymentDate);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
