package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static services.GuestService.updateGuestInDatabase;
import static utils.AlertUtils.textPage;

public class ResetPasswordView {
    public  static  void getResetPasswordView(Integer guestID){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Please Type In Your New Password: ");
        Label passwordLabel = new Label("Password: ");
        PasswordField password = new PasswordField();
        password.setPromptText("Type In Your New Password...");

        HBox passwordBox = new HBox(10, passwordLabel, password);

        Label confirmPasswordLabel = new Label("Confirm Password");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Type In Your Password Again...");

        HBox confirmPasswordBox = new HBox(10, confirmPasswordLabel, confirmPassword);

        Button confirmButton = new Button("Reset Password");
        confirmButton.setOnAction(e -> {
            //if both password fields are not empty
            if (password.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
                //show error popup
                textPage("Both Text Boxes Must Be Filled", "ERROR: Invalid Input", true);
            } else { //if both passwords are the same
                if (password.getText().equals(confirmPassword.getText())){
                    //reset the password to the new password in database
                    updateGuestInDatabase(guestID, "Password", password.getText());
                    textPage("Password Was Changed Successfully, Please Log In With New Password", "Password Change Success", false);
                    //close current page to return to login page
                    stage.close();
                } else {
                    //popup error showing that both passwords do not match
                    textPage("Both Password Do Not Match", "ERROR: Passwords Not Matching", true);
                }
            }
        });

        VBox pageLayout = new VBox(15, label, passwordBox, confirmPasswordBox, confirmButton);
        pageLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(pageLayout, 500, 500);
        scene.getStylesheets().add("file:Style.css");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Reset Password");
        stage.show();
    }
}
