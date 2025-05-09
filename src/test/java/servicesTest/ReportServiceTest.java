package servicesTest;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import models.Room;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.RoomService;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static services.ReportService.getYearForFilter;
import static services.ReportService.setURL;

public class ReportServiceTest {
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // Using H2 in-memory database

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void setUpTestDatabase() {
        setURL(URL);
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            // Create the booking table and insert some test data
            String createTableSQL = "CREATE TABLE booking (" +
                    "BookingID INTEGER AUTO_INCREMENT, " +
                    "GuestID INTEGER, " +
                    "RoomID INTEGER, " +
                    "CheckInDate DATE, " +
                    "CheckOutDate DATE, " +
                    "TotalAmount REAL, " +
                    "PaymentType TEXT, " +
                    "BookingDate DATE, " +
                    "Status TEXT, " +
                    "PRIMARY KEY (BookingID))";
            stmt.executeUpdate(createTableSQL);

            String insertSQL = "INSERT INTO booking (BookingID, GuestID, RoomID, CheckInDate, CheckOutDate, TotalAmount, PaymentType, BookingDate, Status) VALUES\n" +
                    "(17, 1, 10, '2025-04-28', '2025-04-29', 4200, 'Debit Card', '2022-04-28', 'Success')," +
                    "(2, 2, 2, '2025-04-28', '2025-04-29', 300.0, 'Credit Card','2025-04-28', 'Checked In')," +
                    "(4, 4, 4, '2025-04-28', '2025-04-29', 300.0, 'Credit Card','2025-04-28', 'Checked In')," +
                    "(5, 5, 5, '2025-04-28', '2025-04-29', 300.0, 'Credit Card','2025-04-28', 'Checked In')," +
                    "(6, 6, 6, '2023-04-28', '2023-04-29', 300.0, 'Credit Card','2023-04-28', 'Checked In')," +
                    "(7, 7, 7, '2022-04-28', '2022-04-29', 300.0, 'Credit Card','2022-04-28', 'Checked In')," +
                    "(3, 3, 3, '2025-04-20', '2025-04-21', 150.0, 'Credit Card','2025-04-28', 'Checked Out');";
            stmt.executeUpdate(insertSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetYearForFilter() {
        // Arrange: Set up the in-memory database
        setUpTestDatabase();

        // Arrange: Create a ChoiceBox and call the method under test
        ChoiceBox<String> yearChoice = new ChoiceBox<>();

        // Act: Call the method to populate the ChoiceBox
        String getYear = """
                SELECT YEAR(CheckInDate) AS "year"
                FROM booking group by "year";
                """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs2 = stmt.executeQuery(getYear)) {
            //Revenue Summary
            while (rs2.next()) {
                //Adds all existing year values to a choice box for filter
                yearChoice.getItems().add(rs2.getString("year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Assert: Check that the ChoiceBox contains the correct years
        assertEquals(3, yearChoice.getItems().size(), "ChoiceBox should contain exactly 3 unique years");
        assertTrue(yearChoice.getItems().contains("2023"), "ChoiceBox should contain 2023");
        assertTrue(yearChoice.getItems().contains("2022"), "ChoiceBox should contain 2022");
        assertTrue(yearChoice.getItems().contains("2025"), "ChoiceBox should contain 2022");
    }


}
