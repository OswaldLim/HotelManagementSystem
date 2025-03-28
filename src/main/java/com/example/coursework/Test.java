package com.example.coursework;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test extends Application {
    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db"; // Change 'hotel.db' as needed
    public static void main(String[] args) {
        launch(args);
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connected to SQLite!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    @Override
    public void start(Stage primaryStage) {
        connect();
    }
}
