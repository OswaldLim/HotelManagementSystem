package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static controllers.AnimationController.moveLeftMovement;
import static services.GuestService.checkUserExists;
import static utils.AlertUtils.textPage;
import static utils.ImageUtils.fadeImage;
import static utils.InputUtils.*;
import static views.MainView.setAction;

public class SignUpView {

    public static VBox getSignUpView(Stage stage, Rectangle rectangle, ImageView imageView, Image secondImage){
        // Set up column width percentages for form layout
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        // Title text for the sign-up section
        Text text = new Text("Welcome!");
        text.setFont(new Font("Times New Roman",25));

        // Input fields and labels for user sign-up form
        Label SUnameLabel = new Label("Last Name: ");
        TextField SUusername = new TextField();

        Label SUICLabel = new Label("IC Number: ");
        TextField SUICnum = new TextField();
        checkInputType(SUICnum,Integer.class); // Allow only integers

        Label SUemailLabel = new Label("Email: ");
        TextField SUemail = new TextField();

        Label SUphoneNumberLabel = new Label("Phone Number: ");
        TextField SUphoneNumber = new TextField();
        checkInputType(SUphoneNumber, Integer.class); // Allow only integers

        Label SUpasswordLabel = new Label("Password: ");
        PasswordField SUpassword = new PasswordField();

        Label SUconfirmLabel = new Label("Confirm Password: ");
        PasswordField SUconfirmPassword = new PasswordField();

        // Button to navigate to login screen
        Button SUloginButton = new Button("Log In");
        SUloginButton.setOnAction(e -> {
            setAction("login");
            moveLeftMovement(stage, rectangle); // Animate transition to login
            fadeImage(imageView, secondImage); // Crossfade to next image
        });

        // Sign-up button with validation and navigation logic
        Button SUsignUpButton = new Button("Sign Up");
        SUsignUpButton.setPrefWidth(Double.MAX_VALUE);

        SUsignUpButton.setOnAction(e -> {
            // Check if any input fields are empty
            if (checkIfInputEmpty("",SUusername, SUICnum, SUemail, SUphoneNumber, SUpassword, SUconfirmPassword)) {
                // Validate email format
                if (!isValidEmail(SUemail.getText())) {
                    textPage("Invalid Email Format", "ERROR: Invalid Input", true);
                }
                // Validate IC format
                else if (!isValidIC(SUICnum.getText())){
                    textPage("Invalid IC Number Format", "ERROR: Invalid Input", true);
                }
                // Validate phone number
                else if (!isValidPhone(SUphoneNumber.getText())) {
                    textPage("Invalid Phone Number", "ERROR: Invalid Input", true);
                }
                else {
                    // Check if user exists and passwords match
                    if (checkUserExists(SUusername,SUICnum,SUemail,SUphoneNumber,SUpassword,SUconfirmPassword)) {
                        moveLeftMovement(stage, rectangle);
                        fadeImage(imageView,secondImage);
                        textPage("Please Login To Continue","Sign Up Successful!", false);

                        // Clear form fields after successful sign-up
                        SUusername.clear();
                        SUICnum.clear();
                        SUemail.clear();
                        SUphoneNumber.clear();
                        SUpassword.clear();
                        SUconfirmPassword.clear();
                    }
                }
            } else {
                textPage("All Fields Should Be Filled", "ERROR: Invalid Input",true);
            }
        });

        // Grid layout to organize form inputs
        GridPane SUcredentials = new GridPane();
        SUcredentials.setPrefHeight(300);
        SUcredentials.setMinWidth(400);
        SUcredentials.add(SUnameLabel, 0, 0);
        SUcredentials.add(SUusername,1,0);
        SUcredentials.add(SUICLabel, 0, 1);
        SUcredentials.add(SUICnum,1,1);
        SUcredentials.add(SUemailLabel,0,2);
        SUcredentials.add(SUphoneNumberLabel,0,3);
        SUcredentials.add(SUphoneNumber,1,3);
        SUcredentials.add(SUemail,1,2);
        SUcredentials.add(SUpasswordLabel, 0, 4);
        SUcredentials.add(SUpassword,1,4);
        SUcredentials.add(SUconfirmLabel,0,5);
        SUcredentials.add(SUconfirmPassword,1,5);
        SUcredentials.setHgap(10);
        SUcredentials.setVgap(10);
        SUcredentials.getColumnConstraints().addAll(col1, col2);

        // Main vertical layout for the sign-up screen
        VBox signUpInterface = new VBox(20,SUloginButton,text,SUcredentials,SUsignUpButton);
        signUpInterface.setId("signUP");
        signUpInterface.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
        signUpInterface.setPadding(new Insets(50));
        signUpInterface.setAlignment(Pos.TOP_RIGHT);

        return signUpInterface;
    }
}
