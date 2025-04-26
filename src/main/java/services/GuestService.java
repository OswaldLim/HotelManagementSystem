package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Guest;
import models.Room;

import java.sql.*;
import java.util.ArrayList;

import static utils.AlertUtils.textPage;
import static views.ResetPasswordView.getResetPasswordView;

public class GuestService {
    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";

    //Update certain Guest data in the database
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

    //Used to verify Guest Sign Up and check if user already exists in the database
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

            //Get User Details
            String lastName = sqlData.get(0);
            String iC = sqlData.get(1);
            String eMail = sqlData.get(2);
            String phoneNum = sqlData.get(3);
            String passWord = sqlData.get(4);
            String confirmPWord = sqlData.get(5);

            //Throws error if passwords don't match
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
                //Insert the new Guest account details into the database
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
                //Throws error if account already exist
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
        textPage(message, "ERROR: Invalid Input", true);
    }

    //Loop to get Data from TextFields
    public static ArrayList<String> getData(ArrayList<TextField> arrayList) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            data.add(arrayList.get(i).getText());
        }
        return data;
    }

    //Used to get a list of all guest IDs present in our account
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

    //Get a list of all guest Data
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


    public static void deleteGuest(TableView<Guest> tableView, ObservableList<Guest> allGuestList){
        Guest selectedGuest = tableView.getSelectionModel().getSelectedItem();

        if (selectedGuest == null){
            textPage("Please Select an Account to Delete", "ERROR: Invalid Input", true);
            return;
        }

        textPage("Are you sure you want to delete this Account?", "Confirmation", false, true, confirmed -> {
            if (confirmed) {
                try (Connection conn = DriverManager.getConnection(URL);
                     PreparedStatement pstmt = conn.prepareStatement("Delete from guestinfo where GuestID = ?")) {
                    pstmt.setInt(1, selectedGuest.getGuestID());
                    pstmt.executeUpdate();
                    allGuestList.remove(selectedGuest);
                    tableView.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Checks if email exist in database
    public static void isEmailExist(String email, Stage oldStage){
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("Select GuestID from guestinfo where Email = ?")
        ) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    oldStage.close();
                    System.out.println("here");
                    getResetPasswordView(rs.getInt("GuestID"));
                } else {
                    textPage("Your Email Doesn't Exist", "ERROR: Invalid Input", true);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
