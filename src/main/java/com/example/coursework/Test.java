package com.example.coursework;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
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

    public static class RevenueDatas {
        private final String month;
        private final Double totalAmount;
        private final Integer occupancyRate;

        public RevenueDatas(String month, double totalAmount, int occupancyRate) {
            this.month = month;
            this.totalAmount = totalAmount;
            this.occupancyRate = occupancyRate;
        }

        public String getMonth() {
            return month;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public Integer getOccupancyRate() {
            return occupancyRate;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        TableView<RevenueDatas> tableView = new TableView<>();

        // Create columns
        TableColumn<RevenueDatas, String> monthColumn = new TableColumn<>("Month");
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month")); // Matches the getter method

        TableColumn<RevenueDatas, Double> revenueColumn = new TableColumn<>("Total Revenue");
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount")); // Matches the getter method

        TableColumn<RevenueDatas, Integer> occupancyColumn = new TableColumn<>("Occupancy Rate");
        occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));

        // Add columns to the TableView
        tableView.getColumns().addAll(monthColumn, revenueColumn, occupancyColumn); // This line should not produce a warning

        // ObservableList to hold the data
        ObservableList<RevenueDatas> data = FXCollections.observableArrayList();

        // SQL query for total revenue per month
        String totalMoneyPerMonth = """
            WITH RECURSIVE DateSeries AS (
                SELECT CheckinDate AS stay_date, CheckoutDate, TotalAmount
                FROM booking
                WHERE Status = 'Success'
                UNION ALL
                SELECT DATE(stay_date, '+1 day'), CheckoutDate, TotalAmount
                FROM DateSeries
                WHERE stay_date < DATE(CheckoutDate, '-1 day')
            )
            
            SELECT 
                CASE strftime('%m', stay_date) 
                    WHEN '01' THEN 'January' 
                    WHEN '02' THEN 'February' 
                    WHEN '03' THEN 'March' 
                    WHEN '04' THEN 'April' 
                    WHEN '05' THEN 'May' 
                    WHEN '06' THEN 'June' 
                    WHEN '07' THEN 'July' 
                    WHEN '08' THEN 'August' 
                    WHEN '09' THEN 'September' 
                    WHEN '10' THEN 'October' 
                    WHEN '11' THEN 'November' 
                    WHEN '12' THEN 'December' 
                END AS month, 
                IFNULL(SUM(TotalAmount), 0) AS total,
                COUNT(*) AS total_occupied_nights, 
                ROUND((COUNT(*) * 100.0) / 
                    (strftime('%d', DATE(stay_date, 'start of month', '+1 month', '-1 day')) 
                    * (SELECT COUNT(DISTINCT RoomID) FROM booking)), 2) AS occupancy_rate
            FROM DateSeries
            GROUP BY strftime('%m', stay_date)
            ORDER BY strftime('%m', stay_date);
        """;


        // Database connection and data retrieval
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(totalMoneyPerMonth)) {

            while (rs.next()) {
                String month = rs.getString("month");
                double total = rs.getDouble("total");
                int occupancy = rs.getInt("occupancy_rate");
                data.add(new RevenueDatas(month, total, occupancy)); // Add data to the list
            }
            tableView.setItems(data); // Set the items in the TableView

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set up the layout and scene
        VBox vBox = new VBox(tableView);
        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Revenue Summary");
        primaryStage.show();
    }
}

