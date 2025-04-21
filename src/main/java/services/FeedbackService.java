package services;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.Feedback;

import java.sql.*;
import java.time.LocalDate;

import static utils.AlertUtils.textPage;

public class FeedbackService {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";

    public static void submitFeedback(Integer userID, String feedbackText, String rating, Stage feedbackStage){
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt2 = connection.prepareStatement("Insert Into feedback (GuestID, Feedback, Rating, created_at) values (?,?,?,?)")){
            pstmt2.setString(1,String.valueOf(userID));
            pstmt2.setString(2,feedbackText);
            pstmt2.setString(3,rating);
            pstmt2.setDate(4, Date.valueOf(LocalDate.now()));
            pstmt2.executeUpdate();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        feedbackStage.close();
        textPage("Thank You for the feedback","Feedback Accepted",false);
    }

    public static void getFeedback(ObservableList<Feedback> feedbackDataList){
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("Select * from feedback")
        ) {
            while (rs.next()) {
                feedbackDataList.add(new Feedback(
                        rs.getInt("FeedbackID"),
                        rs.getInt("GuestID"),
                        rs.getString("Feedback"),
                        rs.getString("Rating"),
                        rs.getDate("created_at").toLocalDate()
                ));
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }
}
