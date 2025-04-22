package services;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.*;

import static app.MainApp.getHomePage;
import static utils.AlertUtils.textPage;
import static views.FullGuestInterface.showFullGuestUI;
import static views.MainAdminView.showAdminUI;

public class LoginService {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";
    private static Integer userID;
    private static String lastName;
    private static String role;
    private static Image profilePic;

    public static String getLastName() {
        return lastName;
    }

    public static Integer getUserID() {
        return userID;
    }

    public static void loginAction(String username, String ICnum, String password) {
        Stage homePage = getHomePage();

        String firstCheckQuery = "SELECT * FROM Admin WHERE Username = ?" +
                " AND ICNum = ?" +
                " AND Password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt1 = conn.prepareStatement(firstCheckQuery)) {

            pstmt1.setString(1, username);
            pstmt1.setString(2, ICnum);
            pstmt1.setString(3, password);
            ResultSet resultSet1 = pstmt1.executeQuery();

            if (resultSet1.next()) {
                userID = resultSet1.getInt("AdminID");
                role = resultSet1.getString("Role");
                lastName = resultSet1.getString("LastName");
                homePage.close();
                showAdminUI(role);
                resultSet1.close();
            } else {
                resultSet1.close();
                String secondCheckQuery = "SELECT * FROM guestinfo WHERE LastName = ?" +
                        " AND ICNum = ?" +
                        " AND Password = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(secondCheckQuery)) {
                    pstmt2.setString(1, username);
                    pstmt2.setString(2, ICnum);
                    pstmt2.setString(3, password);
                    ResultSet resultSet2 = pstmt2.executeQuery();
                    if (resultSet2.next()) {
                        userID = resultSet2.getInt("GuestID");
                        profilePic = new Image("file:Images/Profile/" + resultSet2.getString("ProfilePicPath"));
                    } else {
                        throw new SQLException("Invalid login credentials");
                    }
                    resultSet2.close();
                    showFullGuestUI(userID, profilePic, lastName);

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            textPage("Invalid Login credentials", "Invalid Input", true);
        }
    }
}
