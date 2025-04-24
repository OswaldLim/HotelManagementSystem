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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static controllers.SceneController.exit;
import static services.BookingService.insertNewBooking;
import static services.LoginService.getLastName;
import static services.LoginService.getUserID;
import static services.PaymentServices.getAmount;
import static services.PaymentServices.getPaymentMethods;
import static utils.AlertUtils.textPage;
import static views.FullGuestInterface.showFullGuestUI;

public class PaymentPageView {
    //Creates Payment Page where users can choose their payment method and confirm their booking
    public static void showPaymentPage(Stage oldstage, LocalDate checkIn, LocalDate checkOut, long days, String roomID, String details, Image profilepic) {
        Integer userID = getUserID();

        GridPane gridPane = new GridPane();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Label introduction = new Label("Booking Details: ");
        introduction.setStyle("-fx-font-size: 30px;");

        //loads the progress bar image to show users that they are almost done with the booking process
        Image progressBarImage = new Image("file:Images/System Logo/Process 3.png");
        ImageView progressView = new ImageView(progressBarImage);

        progressView.setPreserveRatio(true);
        progressView.fitWidthProperty().bind(stage.widthProperty().multiply(0.9));

        //Text details and styling
        Text detailText = new Text(details +
                "\nCheck In Date: " + checkIn +
                "\nCheck Out Date: " + checkOut +
                "\nTotal Nights: " + ChronoUnit.DAYS.between(checkIn, checkOut)
        );

        detailText.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-fill: black;"
        );

        VBox bookingDetails = new VBox(15, introduction, detailText);
        bookingDetails.setStyle("-fx-border-color: #8B5A2B; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10px; " +
                "-fx-padding: 10px; " +
                "-fx-background-color: #FFF5EE;"   );


        //Choice box that contains available payment methods
        Label PaymentLabel = new Label("Payment Method: ");
        ChoiceBox<String> payMethods = new ChoiceBox<>();

        //Text Box to show the total pricing of the reservation
        Label AmountLabel = new Label("Total Amount: RM");
        Text Amount = new Text();
        Amount.setFont(new Font("Georgia",20));

        //button area
        Button confirmButton = new Button("Confirm Payment");

        Button exit = new Button("exit");
        exit.setOnAction(e -> exit(oldstage, stage));


        //GridPane to format the Labels and Input boxes
        gridPane.add(PaymentLabel, 0, 0);
        gridPane.add(payMethods, 1, 0);
        gridPane.add(AmountLabel, 0, 1);
        gridPane.add(Amount, 1, 1);
        gridPane.setVgap(5);

        getAmount(Amount, days, userID); //Function to calculate the total pricing of the reservation

        payMethods.getItems().addAll(getPaymentMethods());//Add all accepted payment methods into the choice Box

        confirmButton.setOnAction(e -> {
            AtomicBoolean conf = new AtomicBoolean(false);
            //Confirm that users are sure that they want to make the reservation
            if (payMethods.getValue() != null){
                textPage("Are you sure you want to make this reservation?", "Confirmation", false, true, confirmed -> {
                    if (confirmed) {
                        conf.set(true);
                    }
                });
            } else {
                textPage("Please Choose Payment Method", "ERROR: Invalid Input", true);
            }
            if (conf.get()){ //if confirmed, insert all the details into the database and close all unused stages
                insertNewBooking(userID, roomID, checkIn, checkOut, Amount.getText(), payMethods.getValue(), "Guest");
                stage.close();
                oldstage.close();
                showFullGuestUI(userID, profilepic, getLastName());
            }
        });

        VBox vBox = new VBox(15, progressView, bookingDetails, gridPane, confirmButton, exit);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        stage.setTitle("Booking");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        stage.show();
        stage.setResizable(false);
    }
}
