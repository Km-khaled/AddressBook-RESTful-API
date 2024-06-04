package com.tprest.tprest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    public static Connection cnnx;

    public static Connection getConnection() throws SQLException {
        if (cnnx == null || cnnx.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cnnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/mid", "root", "");
            } catch (ClassNotFoundException | SQLException e) {
                // Log or throw an exception to indicate connection failure
                throw new SQLException("Failed to establish database connection", e);
            }
        }
        return cnnx;
    }

    public static void closeConnection() {
        if (cnnx != null) {
            try {
                cnnx.close();
            } catch (SQLException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
