package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static services.LoginService.getLastName;
import static utils.AlertUtils.textPage;
import static views.FullGuestInterface.showFullGuestUI;
import static views.PaymentPageView.showPaymentPage;

public class BookingDetailsView {

    public static void showBookingDetails(Stage oldstage, String roomID, ImageView imageView, String description, LocalDate CheckInDate, LocalDate CheckOutDate, Integer userID, Image profilepic){
        GridPane gridPane = new GridPane();
        Stage stage = new Stage();
        imageView.setFitHeight(400);
        imageView.setFitWidth(500);

        Text detailsText = new Text("Booking Details: ");
        detailsText.setFont(new Font("Georgia",30));

        //Load in the progress bar from images
        Image progressBarImage = new Image("file:Images/System Logo/Process 2.png");
        ImageView progressView = new ImageView(progressBarImage);
        progressView.setPreserveRatio(true);
        progressView.fitWidthProperty().bind(stage.widthProperty().multiply(0.9));

        //Rectangle for wrapping the imageView
        Rectangle rectangle = new Rectangle(530,430); // width, height
        rectangle.setStyle(
                "-fx-fill: radial-gradient(focus-angle 45deg, focus-distance 20%, center 50% 50%, radius 80%, #8B5A2B, #A67B5B, #DEB887);" +
                        "-fx-stroke: #5C4033;" +
                        "-fx-stroke-width: 3;"
        );

        //StackPane for wrapping the imageView and to give a border to the imageView
        StackPane imagePane = new StackPane(rectangle, imageView);
        imagePane.setStyle(
                "-fx-border-color: black;" +
                        "-fx-background-color: #FFF5EE;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;"
        );

        //Show room details under the Image
        Text roomDetailLabel = new Text("Room Details:");
        roomDetailLabel.setFont(new Font("Georgia", 24));
        Text roomDetails = new Text(description);
        roomDetails.setFont(new Font("Georgia",20));
        roomDetails.setTextAlignment(TextAlignment.LEFT);

        //Exit button to exit booking and return to room selection page
        Button exit = new Button("exit");
        exit.setOnAction(e -> {
            stage.close();
            showFullGuestUI(userID, profilepic, getLastName());
        });

        //Next button to proceed to payment page
        Button nextButton = new Button("Proceed to Payment");
        nextButton.setOnAction(e -> {
            //Show payment page
            showPaymentPage(stage, CheckInDate, CheckOutDate, ChronoUnit.DAYS.between(CheckInDate,CheckOutDate), roomID, description, profilepic);
        });

        //VBox to hold all booking details
        VBox detailTextBox = new VBox(10, roomDetailLabel, roomDetails);
        detailTextBox.setStyle("-fx-border-color: #8B5A2B; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10px; " +
                "-fx-padding: 10px; " +
                "-fx-background-color: #FFF5EE;");


        VBox vBox = new VBox(10,progressView, detailsText,imagePane,detailTextBox,gridPane,nextButton,exit);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(vBox);

        scene.getStylesheets().add("file:Style.css");

        stage.setTitle("Booking");
        stage.setScene(scene);
        stage.show();
        oldstage.close();
    }

}
