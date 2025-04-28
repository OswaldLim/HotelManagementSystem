package services;

import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

import static app.MainApp.getHomePage;
import static utils.AlertUtils.textPage;
import static views.FullGuestInterface.showFullGuestUI;
import static views.MainAdminView.showAdminUI;

public class LoginService {

    private static String URL = "jdbc:sqlite:hotelManagementSystem.db";
    private static Integer userID;
    private static String lastName;
    private static String role;
    private static Image profilePic;

    public static void setUrl(String newUrl){
        URL = newUrl;
    }

    public static String getLastName() {
        return lastName;
    }

    public static Integer getUserID() {
        return userID;
    }

    public static String getRole(){
        return role;
    }

    //Method to handle login actions
    public static void loginAction(String username, String ICnum, String password) {
        Stage homePage = getHomePage();

        //Check if user is a staff or not
        String firstCheckQuery = "SELECT * FROM Admin WHERE LOWER(Username) = ?" +
                " AND ICNum = ?" +
                " AND Password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt1 = conn.prepareStatement(firstCheckQuery)) {

            pstmt1.setString(1, username);
            pstmt1.setString(2, ICnum);
            pstmt1.setString(3, password);
            ResultSet resultSet1 = pstmt1.executeQuery();

            if (resultSet1.next()) {
                //If user is a staff, save the id and the role of the staff
                userID = resultSet1.getInt("AdminID");
                role = resultSet1.getString("Role");
                homePage.close();
                //Opens the Admin UI
                showAdminUI(role);
                resultSet1.close();
            } else {
                resultSet1.close();
                //If user is not a staff, checks uf user is already in the guest database
                String secondCheckQuery = "SELECT * FROM guestinfo WHERE LOWER(LastName) = ?" +
                        " AND ICNum = ?" +
                        " AND Password = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(secondCheckQuery)) {
                    pstmt2.setString(1, username);
                    pstmt2.setString(2, ICnum);
                    pstmt2.setString(3, password);
                    ResultSet resultSet2 = pstmt2.executeQuery();
                    if (resultSet2.next()) {
                        //Save the Guest Details like profile page, user id, and their names
                        userID = resultSet2.getInt("GuestID");
                        profilePic = new Image("file:Images/Profile/" + resultSet2.getString("ProfilePicPath"));
                        lastName = resultSet2.getString("LastName");
                    } else {
                        throw new SQLException("Invalid login credentials");
                    }
                    resultSet2.close();
                    //Opens the guest UI
                    showFullGuestUI(userID, profilePic, getLastName());

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            textPage("Invalid Login credentials", "Invalid Input", true);
        }
    }

}
