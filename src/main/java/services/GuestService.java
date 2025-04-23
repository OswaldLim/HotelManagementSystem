package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Guest;

import java.sql.*;
import java.util.ArrayList;

import static utils.AlertUtils.textPage;

public class GuestService {
    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";

    public static void updateGuestInDatabase(int guestID, String column, Object newValue){
        String sql = "update guestinfo set " + column + " = ? WHERE GuestID = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,guestID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkUserExists(TextField... creden) {
        String query = "SELECT * FROM guestinfo " +
                "WHERE LastName = ? " +
                "AND ICNum = ? " +
                "AND Email = ? " +
                "AND PhoneNumber = ? " +
                "AND Password = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            ArrayList<TextField> userData = new ArrayList<>();
            for (TextField dataType : creden) {
                userData.add(dataType);
            }

            ArrayList<String> sqlData = getData(userData);

            String lastName = sqlData.get(0);
            String iC = sqlData.get(1);
            String eMail = sqlData.get(2);
            String phoneNum = sqlData.get(3);
            String passWord = sqlData.get(4);
            String confirmPWord = sqlData.get(5);

            if (!passWord.equals(confirmPWord)) {
                throw new Error("Password doesn't match");
            }

            stmt.setString(1, lastName);
            stmt.setString(2, iC);
            stmt.setString(3, eMail);
            stmt.setString(4, phoneNum);
            stmt.setString(5, passWord);
            ResultSet rs = stmt.executeQuery();

            if (!(rs.next())) {
                String insertQuery = "INSERT INTO guestinfo (LastName, ICNum, Email, PhoneNumber, Password)" +
                        "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, lastName.toLowerCase());
                    preparedStatement.setString(2, iC);
                    preparedStatement.setString(3, eMail);
                    preparedStatement.setString(4, phoneNum);
                    preparedStatement.setString(5, passWord);

                    preparedStatement.executeUpdate();
                    rs.close();

                    return true;
                }
            } else {
                rs.close();
                throw new Exception("Account Exists");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions (e.g., database connection errors)
            showError("Invalid Input. Possible Errors: \n" +
                    " - Phone Number already registered \n" +
                    " - Email is already registered \n" +
                    " - Input Fields are empty");
        } catch (Error e) {
            showError("Passwords don't Match");
        } catch (Exception e) {
            showError("Account already exists");
        }
        return false;
    }

    private static void showError(String message) {
        // Implement your custom error handling method here, e.g., show a pop-up or log it
        textPage(message, "ERROR: Invalid Input", true);
    }

    public static ArrayList<String> getData(ArrayList<TextField> arrayList) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            data.add(arrayList.get(i).getText());
        }
        return data;
    }

    public static ObservableList<Integer> getAllGuestID(){
        ObservableList<Integer> allGuestIDs = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt2 = conn.createStatement();
             ResultSet rs2 = stmt2.executeQuery("Select GuestID from guestinfo");
        ) {
            while (rs2.next()) {
                allGuestIDs.add(rs2.getInt("GuestID"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return allGuestIDs;
    }

    public static ObservableList<Guest> getAllGuestData(){
        ObservableList<Guest> allGuestDataList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("Select * from guestinfo");
        ) {
            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getInt("GuestID"),
                        rs.getString("LastName"),
                        rs.getString("ICNum"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        new Image("file:Images/Profile/"+rs.getString("ProfilePicPath"))
                );
                allGuestDataList.add(guest);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return allGuestDataList;
    }
}
