package com.studentdb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Save DB in the root directory of the project
    private static final String URL = "jdbc:sqlite:student_manager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "roll_number TEXT UNIQUE NOT NULL,"
                + "full_name TEXT NOT NULL,"
                + "dob TEXT,"
                + "gender TEXT,"
                + "email TEXT,"
                + "phone TEXT,"
                + "department TEXT,"
                + "year_of_study INTEGER,"
                + "address TEXT,"
                + "cgpa REAL"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
