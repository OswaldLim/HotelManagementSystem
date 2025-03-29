package com.example.coursework;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        String insertQuery = "INSERT INTO booking (GuestID, RoomID, CheckInDate, CheckOutDate,TotalAmount, PaymentType, BookingDate, Status)" +
                " VALUES (?,?,?,?,?,?,?,'Pending')";
        String setUnavailable = "UPDATE room SET Status = 'occupied' WHERE room.RoomID = ?";
        try (Connection connection = DriverManager.getConnection(URL)) {
            long startTime = System.currentTimeMillis();
            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                pstmt.setString(1, String.valueOf(1));
                pstmt.setString(2, "1");
                pstmt.setString(3, String.valueOf(2020-2-2));
                pstmt.setString(4, String.valueOf(2020-2-2));
                pstmt.setString(5, "150");
                pstmt.setString(6, "Credit");
                pstmt.setString(7, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                pstmt.executeUpdate();
            } catch (SQLException exception) {
                long endTime = System.currentTimeMillis();
                System.out.println("Insert query ended in: "+(endTime-startTime));
            }


            try (PreparedStatement pstmt2 = connection.prepareStatement(setUnavailable)) {
                pstmt2.setString(1, "1");
                pstmt2.executeUpdate();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        System.out.println("Finished");
    }
}
