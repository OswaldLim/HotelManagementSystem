package servicesTest;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.Bookings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.BookingService;

import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static services.BookingService.*;

class BookingServiceTest {

    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // In-memory database URL
    private Connection connection;

    private ObservableList<Bookings> bookingDataList;


    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize the in-memory database and create the necessary table
        connection = DriverManager.getConnection(URL);
        setUrl(URL);
        try (Statement stmt = connection.createStatement()) {
            // Drop the table if it exists to avoid the "table already exists" error
            stmt.executeUpdate("DROP TABLE IF EXISTS booking");
            stmt.executeUpdate("DROP TABLE IF EXISTS room");

            // Create the booking table
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

            // Create the room table
            String createRoomTableSQL = "CREATE TABLE room (" +
                    "RoomID INTEGER PRIMARY KEY, " +
                    "Capacity INTEGER NOT NULL, " +
                    "Pricing REAL NOT NULL, " +
                    "Type TEXT NOT NULL, " +
                    "Pictures TEXT NOT NULL, " +
                    "Status TEXT DEFAULT 'available'" +
                    ")";
            stmt.executeUpdate(createRoomTableSQL);

            // Insert some initial data into the booking and room tables
            String insertSQL = "INSERT INTO booking (BookingID, GuestID, RoomID, CheckInDate, CheckOutDate, TotalAmount, PaymentType, BookingDate, Status) VALUES\n" +
                    "(17, 1, 10, '2025-04-27', '2025-04-29', 4200, 'Debit Card', '2022-04-28', 'Success')," +
                    "(2, 2, 2, '2025-04-27', '2025-04-29', 300.0, 'Credit Card','2025-04-28', 'Pending')," +
                    "(4, 4, 4, '2025-04-28', '2025-04-29', 300.0, 'Credit Card','2025-04-28', 'Pending')," +
                    "(5, 5, 5, '2025-04-26', '2025-04-28', 300.0, 'Credit Card','2025-04-28', 'Checked Out')," +
                    "(7, 7, 7, '2025-04-28', '2025-04-29', 400.0, 'Cash','2025-04-28', 'Pending')," +
                    "(3, 3, 3, '2025-04-20', '2025-04-21', 150.0, 'Credit Card','2025-04-28', 'Checked Out');";
            stmt.executeUpdate(insertSQL);

            bookingDataList = FXCollections.observableArrayList(
                    new Bookings(17, 1, 10, LocalDate.of(2025, 4, 27), LocalDate.of(2025, 4, 29), 4200.0, "Debit Card", LocalDate.of(2022, 4, 28), "Success"),
                    new Bookings(2, 2, 2, LocalDate.of(2025, 4, 27), LocalDate.of(2025, 4, 29), 300.0, "Credit Card", LocalDate.of(2025, 4, 28), "Pending"),
                    new Bookings(4, 4, 4, LocalDate.of(2025, 4, 28), LocalDate.of(2025, 4, 29), 300.0, "Credit Card", LocalDate.of(2025, 4, 28), "Pending"),
                    new Bookings(5, 5, 5, LocalDate.of(2025, 4, 26), LocalDate.of(2025, 4, 28), 300.0, "Credit Card", LocalDate.of(2025, 4, 28), "Checked Out"),
                    new Bookings(7, 7, 7, LocalDate.of(2025, 4, 28), LocalDate.of(2025, 4, 29), 400.0, "Cash", LocalDate.of(2025, 4, 28), "Pending"),
                    new Bookings(3, 3, 3, LocalDate.of(2025, 4, 20), LocalDate.of(2025, 4, 21), 150.0, "Credit Card", LocalDate.of(2025, 4, 28), "Checked Out")
            );
            String insertRoomSQL = "INSERT INTO room (RoomID, Capacity, Pricing, Type, Pictures, Status) VALUES " +
                    "(1, 2, 150, 'Deluxe', 'deluxe2Capacity.jpg', 'available')";
            stmt.executeUpdate(insertRoomSQL);
        }
    }

    @Test
    void testUpdateBookingInDatabase() throws SQLException {
        // Create a new instance of the BookingService class
        BookingService bookingService = new BookingService();

        // Update the name of the booking with BookingID 1
        bookingService.updateBookingInDatabase(17, "RoomID", 1);

        // Verify the database has been updated
        String query = "SELECT RoomID FROM booking WHERE BookingID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 17);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                Integer roomID = resultSet.getInt("RoomID");
                assertEquals(1, roomID, "The RoomID should be updated to 1");
            } else {
                fail("Booking not found in the database");
            }
        }
    }

    @Test
    public void testCancelBooking() {
        // Simulate the user interface components (MenuButton and MenuItem)
        MenuButton menuButton = new MenuButton();
        MenuItem menuItem = new MenuItem();

        // Before calling cancelBooking, verify that the booking exists
        try (Connection conn = DriverManager.getConnection(URL)) {
            String query = "SELECT Status FROM booking WHERE BookingID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, 7);
                ResultSet rs = pstmt.executeQuery();
                assertTrue(rs.next());
                assertEquals("Pending", rs.getString("Status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error in database setup or query");
        }

        Platform.runLater(() -> {
            Stage mockStage = new Stage();  // Create a mock stage
            cancelBooking("7", "7", menuItem, menuButton, mockStage);

            // Verify the booking status has been updated to 'Canceled'
            try (Connection conn = DriverManager.getConnection(URL)) {
                String query = "SELECT Status FROM booking WHERE BookingID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, 7);
                    ResultSet rs = pstmt.executeQuery();
                    assertTrue(rs.next());
                    assertEquals("Canceled", rs.getString("Status"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Error verifying booking status after cancellation");
            }

            // Verify the room status has been updated to 'available'
            try (Connection conn = DriverManager.getConnection(URL)) {
                String query = "SELECT Status FROM room WHERE RoomID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, 7);
                    ResultSet rs = pstmt.executeQuery();
                    assertTrue(rs.next());
                    assertEquals("available", rs.getString("Status"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Error verifying room status after cancellation");
            }

            // Verify the menuItem has been removed from menuButton
            assertTrue(menuButton.getItems().isEmpty());
        });
    }

    @Test
    void testNoBookingsLeft() throws SQLException {
        // Create mock objects for MenuItem, MenuButton, and Stage
        Platform.runLater(() -> {
            MenuButton menuButton = new MenuButton();
            MenuItem menuItem = new MenuItem("Cancel Booking");
            Stage stage = new Stage();

            // Add menu item to menu button
            menuButton.getItems().add(menuItem);

            // Simulate the cancelBooking method execution with no remaining bookings
            cancelBooking("17", "10", menuItem, menuButton, stage);

            // Check that a new menu item is added after canceling booking
            assertEquals(1, menuButton.getItems().size(), "MenuButton should contain one item after canceling booking");
            assertEquals("No booking process in pending", menuButton.getItems().get(0).getText(), "The new menu item should display 'No booking process in pending'");

        });
    }

    @Test
    void testShowBookingProgress() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                MenuButton booking = new MenuButton();
                MenuButton booked = new MenuButton();

                BookingService.showBookingProgress(123, booking, booked);

                // Should contain 1 pending and 1 confirmed
                assertEquals(1, booking.getItems().size(), "Booking menu should have 1 pending item");
                assertEquals(1, booked.getItems().size(), "Booked menu should have 1 confirmed item");

                MenuItem pendingItem = booking.getItems().get(0);
                MenuItem bookedItem = booked.getItems().get(0);

                assertTrue(pendingItem.getText().contains("Booking ID: 1"));
                assertTrue(bookedItem.getText().contains("Booking ID: 2"));
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testInsertNewBooking() throws Exception {
        Integer guestId = 1;
        String roomId = "101";
        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(7);
        String amount = "250.0";
        String paymentMethod = "Credit Card";
        String usage = "Guest"; // not Admin

        Platform.runLater(() -> {
            BookingService.insertNewBooking(guestId, roomId, checkIn, checkOut, amount, paymentMethod, usage);

            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement()) {

                ResultSet rsBooking = stmt.executeQuery("SELECT * FROM booking WHERE GuestID = " + guestId);
                assertTrue(rsBooking.next(), "Booking record should exist");
                assertEquals(guestId, rsBooking.getInt("GuestID"));
                assertEquals(Integer.parseInt(roomId), rsBooking.getInt("RoomID"));
                assertEquals(amount, String.valueOf(rsBooking.getDouble("TotalAmount")));
                assertEquals("Pending", rsBooking.getString("Status"));

                ResultSet rsRoom = stmt.executeQuery("SELECT * FROM room WHERE RoomID = " + roomId);
                assertTrue(rsRoom.next(), "Room record should exist");
                assertEquals("occupied", rsRoom.getString("Status"), "Room should be marked as occupied");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    void testSetBookingStatus() {
        setBookingDataList(bookingDataList);
        // Call the method that sets the status
        setBookingStatus();

        String query = "SELECT BookingID, Status FROM booking";
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Integer bookingID = resultSet.getInt("BookingID");
                String status = resultSet.getString("Status");
                if (bookingID == 17){
                    assertEquals("Checked In",status);
                } else if (bookingID == 2) {
                    assertEquals("Canceled", status);
                } else if (bookingID == 5){
                    assertEquals("Checked Out", status);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void testGetPricing() {
        // Arrange: Setup test data and expected values
        Integer roomId = 1;
        LocalDate checkInDate = LocalDate.of(2025, 4, 1);
        LocalDate checkOutDate = LocalDate.of(2025, 4, 5);

        Double totalAmount = getPricing(roomId, checkInDate, checkOutDate);

        Double expectedAmount = 4 * 150.0;  // 4 days * price of 150 per night
        assertEquals(expectedAmount, totalAmount, "The total amount is calculated incorrectly.");
    }

    @Test
    void testDeleteBooking() {
        // Arrange: Setup test data and mock dependencies
        TableView<Bookings> tableView = new TableView<>(bookingDataList);

        // Assume we already have a Booking object with known ID and details
        Bookings testBooking = new Bookings(2, 2, 2, LocalDate.of(2025, 4, 28), LocalDate.of(2025, 4, 29), 120.0, "Debit", LocalDate.now(), "Pending");

        // Add the booking to the list and table view for testing

        // Act: Call the deleteBooking method to remove the booking
        deleteBooking(tableView, testBooking);

        // Assert: Check that the booking is removed from the list and database
        assertFalse(bookingDataList.contains(testBooking), "Booking should be removed from tableView's list.");

        // Assert: Check that the database entry is deleted (you need to verify this manually)
        // Example query to check the status of the booking
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM booking WHERE BookingID = ?")) {
            pstmt.setInt(1, testBooking.getBookingID());
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next(), "Booking should not exist in the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
