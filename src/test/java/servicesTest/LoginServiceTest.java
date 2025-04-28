package servicesTest;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import models.Bookings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static services.LoginService.setUrl;

public class LoginServiceTest {

    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // In-memory database URL
    private Connection connection;

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

        try (Statement stmt = connection.createStatement()) {
            setUrl(URL);
            // Drop the table if it exists to avoid the "table already exists" error
            stmt.executeUpdate("DROP TABLE IF EXISTS guestinfo");

            // Create the booking table
            String createTableSQL = """
                    CREATE TABLE guestinfo (
                                GuestID INT AUTO_INCREMENT PRIMARY KEY,
                                LastName VARCHAR(255) NOT NULL,
                        ICNum VARCHAR(255) NOT NULL,  -- changed from INTEGER to TEXT
                        Email VARCHAR(255) NOT NULL UNIQUE,
                        PhoneNumber VARCHAR(255) NOT NULL UNIQUE,
                        Password VARCHAR(255) NOT NULL,
                        ProfilePicPath VARCHAR(255) DEFAULT 'defaultProfile.jpg'
                                );
                    """;
            stmt.executeUpdate(createTableSQL);

            String createAdminTable = """
                    CREATE TABLE Admin (
                        AdminID INTEGER,
                        Username TEXT NOT NULL UNIQUE,
                        ICNum INTEGER,
                        Password TEXT NOT NULL,
                        Email TEXT UNIQUE,
                        PhoneNumber TEXT NOT NULL,
                        Role TEXT,
                        PRIMARY KEY(AdminID)
                    );
                    """;
            stmt.executeUpdate(createAdminTable);

            // Insert some initial data into the booking and room tables
            String insertSQL = """
                    INSERT INTO guestinfo (GuestID, LastName, ICNum, Email, PhoneNumber, Password, ProfilePicPath)
                    VALUES
                    (3, 'testUser', '12345', '1', '1', 'password123', 'logo.jpg'),
                    (4, '6', '6', '6', '6', '6', '1-2-volt-battery-5650155.jpeg'),
                    (5, 'Seven', '8888', 'seven@email.com', '7777777777', '7', 'defaultProfile.jpg'),
                    (6, 'lim', '000000000000', 'lim@email.com', '0000000000', 'LIM', 'defaultProfile.jpg');       
                    """;
            stmt.executeUpdate(insertSQL);

            String insertAdmin = """
                    INSERT INTO Admin (AdminID, Username, ICNum, Password, Email, PhoneNumber, Role) VALUES
                    (2, 'admin', 2222, 'Admin', 'admin@gmail.com', '2222222222', 'Admin'),
                    (3, 'Cleaner', 3333, 'Cleaner', 'cleaner@gmail.com', '3333333333', 'Cleaner'),
                    (4, 'Receptionist', 4444, 'Receptionist', 'receptionist@gmail.com', '4444444444', 'Receptionist'),
                    (5, 'testAdmin', '12345', 'password123', 'five@gmail.com', '5555555555', 'Receptionist'),
                    (6, '6', 6666, '6', 'six@email.com', '6666666666', 'Cleaner');
                    """;
            stmt.executeUpdate(insertAdmin);
        }
    }

    // This is your original loginAction method to test
    public void loginAction(String username, String ICnum, String password) throws SQLException {
        // Simulate the first check for admin credentials
        String firstCheckQuery = "SELECT * FROM Admin WHERE LOWER(Username) = ? AND ICNum = ? AND Password = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt1 = conn.prepareStatement(firstCheckQuery)) {

            pstmt1.setString(1, username);
            pstmt1.setString(2, ICnum);
            pstmt1.setString(3, password);
            ResultSet resultSet1 = pstmt1.executeQuery();

            if (resultSet1.next()) {
                String role = resultSet1.getString("Role");
                System.out.println("Admin UI opened for role: " + role);
            } else {
                String secondCheckQuery = "SELECT * FROM guestinfo WHERE LOWER(LastName) = ? AND ICNum = ? AND Password = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(secondCheckQuery)) {
                    pstmt2.setString(1, username);
                    pstmt2.setString(2, ICnum);
                    pstmt2.setString(3, password);
                    ResultSet resultSet2 = pstmt2.executeQuery();
                    if (resultSet2.next()) {
                        int userID = resultSet2.getInt("GuestID");
                        System.out.println("Guest UI opened for GuestID: " + userID);
                    } else {
                        throw new SQLException("Invalid login credentials");
                    }
                }
            }
        }
    }


    @Test
    void testLoginActionAsAdmin() throws SQLException {
        // Test Admin login
        loginAction("admin", "2222", "Admin");

    }

    @Test
    void testLoginActionAsGuest() throws SQLException {
        // Test Guest login
        loginAction("6", "6", "6");
    }

    @Test
    void testInvalidLogin() {
        assertThrows(SQLException.class, () -> {
            loginAction("invalidUser", "00000", "wrongPassword");
        });
    }
}
