package views;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Feedback;

import java.sql.*;
import java.time.LocalDate;

import static services.FeedbackService.getFeedback;

public class FeedbackView {
    //Used to generate feedback Table to show in admin page
    public static TableView<Feedback> getFeedbackTable() {
        TableView<Feedback> tableView = new TableView<>();

        TableColumn<Feedback, Integer> feedbackIDColumn = new TableColumn<>("FeedBack ID");
        feedbackIDColumn.setCellValueFactory(new PropertyValueFactory<>("feedbackID"));

        TableColumn<Feedback, Integer> guestIDColumn = new TableColumn<>("Guest ID");
        guestIDColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));

        TableColumn<Feedback, String> feedbackColumn = new TableColumn<>("FeedBack");
        feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));

        TableColumn<Feedback, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<Feedback, LocalDate> created_atColumn = new TableColumn<>("Date Created");
        created_atColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));

        tableView.getColumns().addAll(feedbackIDColumn, guestIDColumn, feedbackColumn, ratingColumn, created_atColumn);
        return tableView;
    }



}
