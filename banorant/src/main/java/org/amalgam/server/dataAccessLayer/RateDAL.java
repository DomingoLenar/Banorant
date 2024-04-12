package org.amalgam.server.dataAccessLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RateDAL {
    public double getPlayerRateByPlayerID(int playerID) { // if fan click a player then show player rate
        try (Connection conn = DatabaseUtil.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT rate FROM rates WHERE user_id = ? ");
            stmt.setInt(1, playerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Player found, return player rate
                    double fetchedRate = rs.getDouble("rate");
                    return fetchedRate;
                } else {
                    // Player rate not found
                    return -1;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
