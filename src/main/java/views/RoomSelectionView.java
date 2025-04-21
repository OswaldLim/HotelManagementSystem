package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import static services.FeedbackService.submitFeedback;
import static services.RoomService.filterRooms;
import static utils.AlertUtils.textPage;
import static utils.InputUtils.checkInputType;
import static views.ExitBoxView.showExitBox;
import static views.UserInfoColumnView.showUserInfoColumn;

import services.RoomService;
import services.FeedbackService;

public class RoomSelectionView {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";

    public static BorderPane showAvailableRooms(Stage stage, Integer userID, Image profilepic, Stage homePage){

        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();

        Label checkInLabel = new Label("Check In Date: ");
        DatePicker checkInPicker = new DatePicker(LocalDate.now());
        checkInPicker.getEditor().setDisable(true);
        checkInPicker.getEditor().setOpacity(1);
        Label checkOutLabel = new Label("Check Out Date: ");
        DatePicker checkOutPicker = new DatePicker(LocalDate.now().plusDays(1));
        checkOutPicker.getEditor().setDisable(true);
        checkOutPicker.getEditor().setOpacity(1);

        Label capacityFilterLabel = new Label("Capacity: ");
        TextField capacityAmount = new TextField();
        capacityAmount.setPromptText("Input Capacity....");
        capacityAmount.setMaxWidth(200);
        checkInputType(capacityAmount, Integer.class);

        gridPane.add(checkInLabel,0,0);
        gridPane.add(checkInPicker,1,0);
        gridPane.add(checkOutLabel,0,1);
        gridPane.add(checkOutPicker,1,1);
        gridPane.add(capacityFilterLabel,0,2);
        gridPane.add(capacityAmount,1,2);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        gridPane.getColumnConstraints().addAll(col1, col2);
        gridPane.setHgap(20);

        Button searchButton = new Button("Search");
        //filter capacity
        HBox filterBox = new HBox(30, gridPane, searchButton);

        Image progressBarImage = new Image("file:Images/System Logo/Process 1.png");
        ImageView progressView = new ImageView(progressBarImage);

        progressView.setPreserveRatio(true);
        progressView.fitWidthProperty().bind(scrollPane.widthProperty());

        VBox topPane = new VBox(10, filterBox, progressView);
        filterBox.setPadding(new Insets(20));
        HBox.setMargin(searchButton, new Insets(25,0,0,0));

        //change
        searchButton.setOnAction(e -> {
            borderPane.setCenter(scrollPane);
            LocalDate CheckInDate = checkInPicker.getValue();
            LocalDate CheckOutDate = checkOutPicker.getValue();

            VBox vBox = new VBox(10);

            filterRooms(vBox, capacityAmount, CheckInDate, CheckOutDate, stage, userID, profilepic,homePage);

            vBox.setPadding(new Insets(20));
            vBox.setBackground(new Background(new BackgroundFill(Color.web("#D0EFFF"), null, null)));
            scrollPane.setContent(vBox);
            scrollPane.setFitToWidth(true);

        });
        //change

        borderPane.setCenter(scrollPane);
        borderPane.setTop(topPane);
        return borderPane;
    }



}
