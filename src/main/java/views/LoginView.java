package views;

import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Bookings;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import static controllers.AnimationController.moveRightMovement;
import static services.LoginService.loginAction;
import static utils.ImageUtils.fadeImage;
import static views.ForgetPasswordView.getForgetPasswordView;
import static views.MainView.setAction;

public class LoginView {

    //Creates the Login Interface
    public static VBox getLoginInterface(Stage stage, Rectangle rectangle, ImageView imageView, Image secondImage){
        Text welcomeText = new Text("Welcome Back!");
        welcomeText.setFont(new Font("Times New Roman",25));

        Label nameLabel = new Label("Last Name: ");
        TextField username = new TextField();
        Label ICLabel = new Label("IC Number: ");
        TextField ICnum = new TextField();
        Label passwordLabel = new Label("Password: ");
        PasswordField password = new PasswordField();
        Button loginButton = new Button("Login");
        //Allows the login button to resize
        loginButton.setPrefWidth(Double.MAX_VALUE);

        loginButton.setOnAction(e -> {
            //Logs in the users
            loginAction(username.getText().toLowerCase(),ICnum.getText(), password.getText());
        });

        GridPane credentials = new GridPane();
        credentials.setPrefHeight(300);
        credentials.setMinWidth(400);

        //Add the labels and text boxes into the gridPane
        credentials.add(nameLabel, 0, 0);
        credentials.add(username,1,0);
        credentials.add(ICLabel, 0, 1);
        credentials.add(ICnum,1,1);
        credentials.add(passwordLabel, 0, 2);
        credentials.add(password,1,2);


        Button forgetPassword = new Button("forget password");
        forgetPassword.setOnAction(event -> {
            getForgetPasswordView();
        });
        GridPane.setHalignment(forgetPassword, HPos.RIGHT);
        credentials.add(forgetPassword, 1, 3);

        credentials.setVgap(30);
        credentials.setHgap(10);

        //GridPane column length formatting
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        credentials.getColumnConstraints().addAll(col1, col2);

        Button signUpButton = new Button("Sign Up");

        //SignUpButton That switches the scene to the sign up page
        signUpButton.setOnAction(e -> {
            setAction("signUp");
            moveRightMovement(stage, rectangle);
            fadeImage(imageView,secondImage);

        });

        VBox logInInterface = new VBox(20,signUpButton,welcomeText,credentials,loginButton);
        logInInterface.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
        logInInterface.setPadding(new Insets(50));
        logInInterface.setAlignment(Pos.TOP_LEFT);

        return logInInterface;
    }

}
