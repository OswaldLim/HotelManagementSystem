package servicesTest;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import models.Room;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.RoomService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static services.BookingService.setUrl;

public class RoomServiceTest {
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // Using H2 in-memory database

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void setUpRoomTestDatabase() {
        String sql = """
                INSERT INTO room (RoomID, Capacity, Pricing, Type, Pictures, Status) VALUES
                (1, 2, 150, 'Deluxe', 'deluxe2Capacity.jpg', 'available'),
                (2, 4, 200, 'Deluxe', 'deluxe4Capacity.jpg', 'cleaning'),
                (3, 5, 250, 'Deluxe', 'deluxe5Capacity.jpg', 'cleaning'),
                (4, 1, 80, 'Single', 'single room.jpg', 'available'),
                (5, 2, 100, 'Standard', 'Standard room1.jpg', 'available'),
                (6, 2, 100, 'Standard', 'Standard room2.jpg', 'available'),
                (7, 2, 100, 'Standard', 'Standard room3.jpg', 'cleaning'),
                (8, 2, 300, 'Suite', 'Suite2Capacity.jpg', 'cleaning'),
                (9, 3, 350, 'Suite', 'Suite3Capacity.jpg', 'available'),
                (10, 4, 400, 'Suite', 'Suite4Capacity.jpg', 'available'),
                (11, 3, 175, 'Deluxe', 'Deluxe3Capacity.jpg', 'available');
                """;
        String createRoomTableSQL = "CREATE TABLE room (" +
                "RoomID INTEGER PRIMARY KEY, " +
                "Capacity INTEGER NOT NULL, " +
                "Pricing REAL NOT NULL, " +
                "Type TEXT NOT NULL, " +
                "Pictures TEXT NOT NULL, " +
                "Status TEXT DEFAULT 'available'" +
                ")";
        try (Connection conn = DriverManager.getConnection(URL);

             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createRoomTableSQL);
            // Create the room table and insert some test data
            stmt.execute(sql);;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAvailableRooms() {
        // Arrange: Set up the in-memory database
        setUrl(URL);
        setUpRoomTestDatabase();

        // Arrange: Define the check-in and check-out dates and capacity
        LocalDate checkInDate = LocalDate.of(2025, 4, 28);
        LocalDate checkOutDate = LocalDate.of(2025, 4, 30);
        Integer capacity = 2; // Looking for rooms with a capacity of 2 or more

        Platform.runLater(() -> {
            // Act: Call the method to get available rooms
            ObservableList<Room> availableRooms = RoomService.getAvailableRooms(checkInDate, checkOutDate, capacity);
            // Assert: Check the size of the returned list
            assertEquals(7, availableRooms.size(), "There should be 7 available rooms.");
        });

    }
}
