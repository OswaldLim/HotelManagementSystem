package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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
import java.util.concurrent.atomic.AtomicReference;

import static services.BookingService.showBookingProgress;
import static services.FeedbackService.submitFeedback;
import static views.ChangeProfileView.showChangeImageStage;

public class UserInfoColumnView {

    public static VBox showUserInfoColumn(Image profilePic, Integer userID, Stage stage, String lastName){
        Label welcomeText = new Label("Welcome:");

        ImageView imageView = new ImageView(profilePic);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        imageView.setClip(clip);
        Button profileButton = new Button();
        profileButton.setGraphic(imageView);

        //change
        profileButton.setOnAction(event -> {
            showChangeImageStage(imageView, userID, "null");
        });
        //change

        Label userIdText = new Label(
                "User ID: "+String.valueOf(userID) + "\nLast Name: "+lastName
        );
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double fontSize = width/30;
            double fontSize2 = width/40;
            welcomeText.setStyle(
                    "-fx-font-size: " + fontSize + "px;" +
                            "-fx-font-family: 'Lucida Handwriting';"
            );
            userIdText.setStyle(
                    "-fx-font-size: " + fontSize2 + "px;" +
                            "-fx-font-family: 'Lucida Handwriting';"
            );
        });

        MenuButton booking = new MenuButton("Booking Progress");
        booking.prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
        booking.prefHeightProperty().bind(stage.heightProperty().multiply(0.1));

        MenuButton booked = new MenuButton("Booked rooms");
        booked.prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
        booked.prefHeightProperty().bind(stage.heightProperty().multiply(0.1));

        Label feedback = new Label("Contact Details:\n" +
                "Phone Number: 012-345 6789\n" +
                "Email: manager@email.com\n" +
                "Please give us some feedback: ");
        feedback.setWrapText(true);
        Button feedbackButton = new Button("Click here to provide feedback");

        VBox feedbackBox = new VBox(10,feedback, feedbackButton);

        //change
        feedbackButton.setOnAction(e -> {
            Stage feedbackStage = new Stage();
            VBox feedbackPage = new VBox(10);
            feedbackPage.setPadding(new Insets(15));
            Label label = new Label("Feedback: ");

            TextArea feedbackTextArea = new TextArea();
            feedbackTextArea.setPromptText("Enter your feedback...");

            ChoiceBox<String> ratingBox = new ChoiceBox<>();
            ratingBox.setValue("Rate Us...");
            ratingBox.getItems().addAll("Rate Us...","1","2","3","4","5");

            Button submitButton = new Button("Submit");
            submitButton.setOnAction(e1 -> {
                submitFeedback(userID, feedbackTextArea.getText(), ratingBox.getValue(), feedbackStage);
            });
            feedbackPage.getChildren().addAll(label,feedbackTextArea,ratingBox,submitButton);
            Scene scene = new Scene(feedbackPage,300,400);
            label.setStyle("-fx-font-family: 'Lucida Handwriting';");
            scene.getStylesheets().add("file:Style.css");
            feedbackStage.setScene(scene);
            feedbackStage.setResizable(false);
            feedbackStage.setTitle("FeedBack");
            feedbackStage.show();
        });
        //change

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox userInfo = new VBox(20, welcomeText,profileButton,userIdText,booking,booked,spacer,feedbackBox);
        userInfo.prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
        profileButton.prefWidthProperty().bind(userInfo.widthProperty().multiply(0.6));
        profileButton.prefHeightProperty().bind(profileButton.widthProperty());
        imageView.fitWidthProperty().bind(profileButton.widthProperty().multiply(0.7));
        imageView.fitHeightProperty().bind(imageView.fitWidthProperty());


        userInfo.setPadding(new Insets(20));
        userInfo.setStyle(
                "-fx-background-color: #FDFCE1;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 10px;" +
                        "-fx-padding: 10px;"
        );

        showBookingProgress(userID,booking,booked);



        return userInfo;
    }

}
