package services;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class PaymentServices {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";

    //get total price of booking throughout the specified duration
    public static void getAmount(Text Amount, long days, Integer id){
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("Select Pricing from room where RoomID = ?")
        ) {
            pstmt.setString(1, String.valueOf(id));
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    //multiply the price of the room per night with the duration of stay
                    Amount.setText(String.valueOf(rs.getDouble("Pricing") * days));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Get Accepted Payment Methods
    public static ObservableList<String> getPaymentMethods(){
        ObservableList<String> allPaymentMethods = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * from paymentstype")
        ) {
            while (rs.next()) {
                allPaymentMethods.add(rs.getString("PaymentType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPaymentMethods;
    }
}
