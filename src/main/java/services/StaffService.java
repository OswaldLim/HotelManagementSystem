package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import models.Staff;

import java.sql.*;

public class StaffService {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";
    private static ObservableList<Staff> staffDataList = FXCollections.observableArrayList();

    public static void updateStaffInDatabase(int adminID, String column, Object newValue){
        String sql = "update Admin set " + column + " = ? WHERE AdminID = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,adminID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Staff> getStaffList() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("Select * from Admin");
        ){
            while (rs.next()) {
                staffDataList.add(new Staff(
                        rs.getInt("AdminID"),
                        rs.getString("Username"),
                        rs.getString("ICNum"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber")
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return staffDataList;
    }

    public static void insertNewStaff(Staff newStaff){
        String sqlQuery = "insert into Admin (Username, ICNum, Password, Email, PhoneNumber, Role) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)
        ) {
            pstmt.setString(1, newStaff.getStaffUsername());
            pstmt.setString(2, newStaff.getStaffIC());
            pstmt.setString(3, newStaff.getStaffPassword());
            pstmt.setString(4, newStaff.getStaffEmail());
            pstmt.setString(5, newStaff.getStaffPhoneNumber());
            pstmt.setString(6, newStaff.getStaffRole());
            pstmt.executeUpdate();

            staffDataList.add(newStaff);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStaff(Staff staff, TableView<Staff> tableView){
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("Delete from Admin where AdminID = ?")) {
            pstmt.setInt(1, staff.getStaffID());
            pstmt.executeUpdate();
            staffDataList.remove(staff);
            tableView.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
