package services;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import models.RevenueData;

import java.sql.*;

public class ReportService {
    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";


    public static void generateAllReportData(ChoiceBox<String> yearChoice, ObservableList<RevenueData> paymentData, PieChart pieChart, BarChart<String, Number> barChart,XYChart.Series<String, Number> dataSeries, ObservableList<RevenueData> data){

        yearChoice.setOnAction(e -> {
            getReport(yearChoice.getValue(), dataSeries, data);

            getPaymentReport(yearChoice.getValue(),paymentData, pieChart);
        });
        barChart.getData().add(dataSeries);
        yearChoice.setValue(yearChoice.getItems().getLast());
    }


    public static void getYearForFilter(ChoiceBox<String> yearChoice){
        String getYear = """
                SELECT strftime('%Y', CheckInDate / 1000, 'unixepoch') AS year
                FROM booking group by year;
                """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs2 = stmt.executeQuery(getYear)) {
            //Revenue Summary
            while (rs2.next()) {
                yearChoice.getItems().add(rs2.getString("year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getPaymentReport(String year, ObservableList<RevenueData> paymentData, PieChart pieChart){
        String paymentRevenue = """
                Select PaymentType, strftime('%Y-%m-%d', BookingDate / 1000, 'unixepoch') AS formatted_date,
                COUNT(*) AS total_transactions,
                SUM(TotalAmount) AS total_revenue
                from booking
                where strftime('%Y', formatted_date) = ?
                Group by PaymentType
                """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt2 = conn.prepareStatement(paymentRevenue)) {
            pstmt2.setString(1,year);
            ResultSet rs1 = pstmt2.executeQuery();
            while (rs1.next()) {
                paymentData.add(new RevenueData(rs1.getString("PaymentType"), rs1.getDouble("total_revenue"), rs1.getInt("total_transactions")));
                pieChart.getData().add(new PieChart.Data(rs1.getString("PaymentType"),rs1.getDouble("total_revenue")));
            }
            rs1.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    public static void getReport(String year, XYChart.Series<String, Number> dataSeries, ObservableList<RevenueData> data){
        String totalMoneyPerMonth = """
                    WITH RECURSIVE DateSeries AS (
                        -- Initial Select
                        SELECT\s
                            strftime('%Y-%m-%d', CheckInDate / 1000, 'unixepoch') AS stay_date,\s
                            strftime('%Y-%m-%d', CheckoutDate / 1000, 'unixepoch') AS checkout_date,\s
                            TotalAmount
                        FROM booking
                        WHERE Status not in ('Pending','Cancelled')
                        AND strftime('%Y', CheckInDate / 1000, 'unixepoch') = ? \s
                        UNION ALL
                        SELECT\s
                            DATE(stay_date, '+1 day'),\s
                            checkout_date,\s
                            TotalAmount
                        FROM DateSeries
                        WHERE stay_date < checkout_date
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
                        ROUND((COUNT(*) * 100.0) /\s
                            (strftime('%d', DATE(stay_date, 'start of month', '+1 month', '-1 day')) *
                            (SELECT COUNT(DISTINCT RoomID) FROM booking)), 2) AS occupancy_rate
                    FROM DateSeries
                    GROUP BY strftime('%m', stay_date)
                    ORDER BY strftime('%m', stay_date);

        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(totalMoneyPerMonth)) {
            pstmt.setString(1,year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String month = rs.getString("month");
                double total = rs.getDouble("total");
                double occupancy = rs.getDouble("occupancy_rate");
                RevenueData newRevenueDate = new RevenueData(month, total, occupancy);
                data.add(newRevenueDate);
                dataSeries.getData().add(new XYChart.Data<>(month, total));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }
}
