package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static services.GuestService.isEmailExist;
import static utils.AlertUtils.textPage;

public class ForgetPasswordView {
    public static void getForgetPasswordView(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox forgetPasswordPage = new VBox(10);

        Label label = new Label("Please Type in your email address: ");
        TextField emailText = new TextField();
        emailText.setMaxWidth(400);
        emailText.setPromptText("Type In Your Email Address...");

        Button confirmButton = new Button("Done");
        confirmButton.setOnAction(e -> {
            if (!emailText.getText().isEmpty()) {
                isEmailExist(emailText.getText(), stage);
            }  else {
                textPage("Email Field is Empty", "ERROR: Invalid Input", true);
            }
        });

        forgetPasswordPage.getChildren().addAll(label, emailText, confirmButton);
        forgetPasswordPage.setAlignment(Pos.CENTER);

        Scene scene = new Scene(forgetPasswordPage, 500, 500);
        scene.getStylesheets().add("file:Style.css");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Forget Password");
        stage.show();
    }
}
