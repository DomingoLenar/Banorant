package org.amalgam.server.dataAccessLayer;

import org.amalgam.utils.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAL {
    public boolean registerAcceptedPayment(int fanID, int totalAmount, Status status, String paymentDate) { // payment processing is in client side but should be done by PSP third party
        try (Connection conn = DatabaseUtil.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO payments (user_id, amount, status, payment_date) VALUE " +
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
        return 0;
    }
}
