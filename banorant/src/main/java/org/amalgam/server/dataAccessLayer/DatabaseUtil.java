package org.amalgam.server.dataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/banorant?user=root&password";
    private static Connection connection;

    private DatabaseUtil() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Create a new connection if it doesn't exist or is closed
            connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("\nnew Connection to database:" + connection);
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
