package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static controllers.SceneController.exit;
import static services.BookingService.insertNewBooking;
import static services.PaymentServices.getAmount;
import static services.PaymentServices.getPaymentMethods;
import static views.FullGuestInterface.showFullGuestUI;

public class PaymentPageView {
    public static void showPaymentPage(Stage oldstage, LocalDate checkIn, LocalDate checkOut, long days, String roomID, String details, Integer userID, Image profilepic) {

        GridPane gridPane = new GridPane();
        Stage stage = new Stage();
        Label introduction = new Label("Room Details: ");
        introduction.setStyle("-fx-font-size: 30px;");

        Image progressBarImage = new Image("file:Images/System Logo/Process 3.png");
        ImageView progressView = new ImageView(progressBarImage);

        progressView.setPreserveRatio(true);
        progressView.fitWidthProperty().bind(stage.widthProperty().multiply(0.9));

        Text detailText = new Text(details +
                "\nCheck In Date: " + checkIn +
                "\nCheck Out Date: " + checkOut +
                "\nTotal Nights: " + ChronoUnit.DAYS.between(checkIn, checkOut)
        );
        detailText.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-fill: black;"  // Corrected this line
        );


        Label PaymentLabel = new Label("Payment Method: ");
        ChoiceBox<String> payMethods = new ChoiceBox<>();
        Label AmountLabel = new Label("Amount: ");
        Text Amount = new Text();
        Button confirmButton = new Button("Confirm Payment");

        Button exit = new Button("exit");
        exit.setOnAction(e -> exit(oldstage, stage));

        gridPane.add(PaymentLabel, 0, 0);
        gridPane.add(payMethods, 1, 0);
        gridPane.add(AmountLabel, 0, 1);
        gridPane.add(Amount, 1, 1);

        getAmount(Amount, days, userID);

        payMethods.getItems().addAll(getPaymentMethods());

        confirmButton.setOnAction(e -> {
            insertNewBooking(userID, roomID, checkIn, checkOut, Amount.getText(), payMethods.getValue(), null);
            stage.close();
            showFullGuestUI(userID, profilepic, oldstage);
        });

        VBox vBox = new VBox(15, progressView, introduction, detailText, gridPane, confirmButton, exit);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        stage.setTitle("Booking");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        stage.show();
        stage.setResizable(false);
        oldstage.close();
    }
}
