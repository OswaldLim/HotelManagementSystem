package com.example.coursework;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.undo.StateEdit;
import java.sql.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class SignUpPage extends Application {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db"; // Database URL

    private int userID;

    private Stage homePage;
    private String action = "login";

    @Override
    public void start(Stage stage) throws IOException {
        this.homePage = stage;
        //Interface
        Text welcomeText = new Text("Welcome Back!");
        welcomeText.setFont(new Font("Times New Roman",25));

        Label nameLabel = new Label("Last Name: ");
        TextField username = new TextField();
        Label ICLabel = new Label("IC Number: ");
        TextField ICnum = new TextField();
        Label passwordLabel = new Label("Password: ");
        PasswordField password = new PasswordField();
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(Double.MAX_VALUE);

        loginButton.setOnAction(e -> {
            try {
                Connection conn = DriverManager.getConnection(URL);
                String firstCheckQuery = "SELECT * FROM Admin WHERE Username = ?" +
                        " AND ICNum = ?" +
                        " AND Password = ?";

                PreparedStatement pstmt1 = conn.prepareStatement(firstCheckQuery);


                pstmt1.setString(1,username.getText());
                pstmt1.setString(2,ICnum.getText());
                pstmt1.setString(3,password.getText());
                ResultSet resultSet1 = pstmt1.executeQuery();


                if (resultSet1.next()) {
                    this.userID = resultSet1.getInt("AdminID");
                    System.out.println("Admin");
                    //Admin Page
                } else {
                    String secondCheckQuery = "SELECT * FROM guestinfo WHERE LastName = ?" +
                            " AND ICNum = ?" +
                            " AND Password = ?";
                    PreparedStatement pstmt2 = conn.prepareStatement(secondCheckQuery);
                    pstmt2.setString(1,username.getText());
                    pstmt2.setString(2,ICnum.getText());
                    pstmt2.setString(3,password.getText());
                    ResultSet resultSet2 = pstmt2.executeQuery();
                    if (resultSet2.next()) {
                        this.userID = resultSet2.getInt("GuestID");
                    } else {
                        throw new SQLException("Invalid login credentials");
                    }
                    rooms(stage);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                textPage("Invalid Login credentials","Invalid Input", true);
            }
        });

        GridPane credentials = new GridPane();
        credentials.setPrefHeight(300);
        credentials.setMinWidth(400);
        credentials.add(nameLabel, 0, 0);
        credentials.add(username,1,0);
        credentials.add(ICLabel, 0, 1);
        credentials.add(ICnum,1,1);
        credentials.add(passwordLabel, 0, 2);
        credentials.add(password,1,2);
        credentials.setVgap(30);
        credentials.setHgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        credentials.getColumnConstraints().addAll(col1, col2);

        Button signUpButton = new Button("Sign Up");

        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(stage.widthProperty().multiply(0.5));
        rectangle.heightProperty().bind(stage.heightProperty());
        rectangle.setX(0);
        rectangle.setFill(Color.web("#1E3A8A"));

        //signUpButton.setOnAction(e -> SignUp(stage));

        signUpButton.setOnAction(e -> {
            this.action = "signUp";
            TranslateTransition moveRight = new TranslateTransition(Duration.seconds(1),rectangle);
            moveRight.setToX(stage.getWidth()*0.49);

            FillTransition blueToYellow = new FillTransition(Duration.seconds(1),rectangle);
            blueToYellow.setFromValue(Color.web("#1E3A8A"));
            blueToYellow.setToValue(Color.web("#EAB308"));

            moveRight.play();
            blueToYellow.play();
        });

        VBox logInInterface = new VBox(20,signUpButton,welcomeText,credentials,loginButton);
        logInInterface.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
        logInInterface.setPadding(new Insets(50));
        logInInterface.setAlignment(Pos.TOP_LEFT);

        //SignUpPage start

        Text text = new Text("Welcome!");
        text.setFont(new Font("Times New Roman",25));
        Label SUnameLabel = new Label("Last Name: ");
        TextField SUusername = new TextField();
        Label SUICLabel = new Label("IC Number: ");
        TextField SUICnum = new TextField();
        checkInteger(SUICnum);

        Label SUemailLabel = new Label("Email: ");
        TextField SUemail = new TextField();
        Label SUphoneNumberLabel = new Label("Phone Number: ");
        TextField SUphoneNumber = new TextField();
        checkInteger(SUphoneNumber);
        Label SUpasswordLabel = new Label("Password: ");
        PasswordField SUpassword = new PasswordField();
        Label SUconfirmLabel = new Label("Confirm Password: ");
        PasswordField SUconfirmPassword = new PasswordField();

        Button SUloginButton = new Button("Log In");
        SUloginButton.setOnAction(e -> {
            TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(1),rectangle);
            moveLeft.setFromX(stage.getWidth()*0.5);
            moveLeft.setToX(0);

            FillTransition yellowToBlue = new FillTransition(Duration.seconds(1),rectangle);
            yellowToBlue.setFromValue(Color.web("#EAB308"));
            yellowToBlue.setToValue(Color.web("#1E3A8A"));

            yellowToBlue.play();
            moveLeft.play();

        });

        Button SUsignUpButton = new Button("Sign Up");
        SUsignUpButton.setPrefWidth(Double.MAX_VALUE);

        SUsignUpButton.setOnAction(e -> {
            if (CheckUserExists(stage,SUusername,SUICnum,SUemail,SUphoneNumber,SUpassword,SUconfirmPassword)) {
                TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(1),rectangle);
                moveLeft.setFromX(stage.getWidth()*0.5);
                moveLeft.setToX(0);

                FillTransition yellowToBlue = new FillTransition(Duration.seconds(1),rectangle);
                yellowToBlue.setFromValue(Color.web("#EAB308"));
                yellowToBlue.setToValue(Color.web("#1E3A8A"));

                yellowToBlue.play();
                moveLeft.play();

                textPage("Please Login To Continue","Sign Up Successful!", false);
            }


        });

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

        VBox signUpInterface = new VBox(20,SUloginButton,text,SUcredentials,SUsignUpButton);
        signUpInterface.setId("signUP");
        signUpInterface.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
        signUpInterface.setPadding(new Insets(50));
        signUpInterface.setAlignment(Pos.TOP_RIGHT);

        //SIgnUpPage Ending

        BorderPane finalBorderPane = new BorderPane();
        finalBorderPane.setLeft(signUpInterface);
        finalBorderPane.setRight(logInInterface);

        StackPane finalPane = new StackPane(finalBorderPane,rectangle);
        StackPane.setAlignment(rectangle,Pos.CENTER_LEFT);

        stage.widthProperty().addListener((obs,oldWidth,newWidth) -> {
            if (this.action.equals("signUp")){
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),rectangle);
                translateTransition.setToX(stage.getWidth()*0.49);
                translateTransition.play();
            }
        });

        Scene scene = new Scene(finalPane,1000,500);
        stage.setTitle("Login Page");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        stage.show();
    }


    private void AdminPage(){
        ScrollPane scrollPane = new ScrollPane();

        try {
            String query = "SELECT * from booking where ";
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(query);



        } catch (SQLException e) {
            e.printStackTrace();
        }

        //total booking

        //available rooms

        //Revenue

        //room management: Add/Edit/Delete rooms,

        //Room Avalability/Cleaning Status

        //Pricing & Discounts

        //manage hotel staff

        //
    }








    private void exit(Stage oldstage, Stage current) {
        current.close();
        oldstage.show();
    }

    private ArrayList<String> getData(ArrayList<TextField> arrayList) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            data.add(arrayList.get(i).getText());
        }
        return data;
    }

    private void checkInteger(TextField textField) {
        textField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
    }

    private void textPage(String text, String title,boolean err){
        Stage error = new Stage();
        Text info = new Text(text);
        info.setWrappingWidth(400);
        info.setFont(new Font("Georgia",14));
        VBox vBox = new VBox(info);
        vBox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Image image = new Image("file:Images/Error.jpeg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        if (err) {
            hBox.getChildren().addAll(imageView, vBox);
        } else {
            hBox.getChildren().add(vBox);
        }
        hBox.setPadding(new Insets(20));
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(hBox,400,150);
        error.setResizable(false);
        scene.getStylesheets().add("file:Style.css");
        error.setScene(scene);
        error.setTitle(title);
        error.show();
    }

    private boolean CheckUserExists(Stage stage, TextField... creden){
        try{
            String query = "SELECT * FROM guestinfo " +
                    "WHERE LastName = ? " +
                    "AND ICNum = ? " +
                    "AND Email = ? " +
                    "AND PhoneNumber = ? " +
                    "AND Password = ?";
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement stmt = conn.prepareStatement(query);
            ArrayList<TextField> userData = new ArrayList<>();
            for (TextField dataType : creden){
                userData.add(dataType);
            }

            ArrayList<String> sqlData = getData(userData);

            String lastName = sqlData.get(0);
            String iC = sqlData.get(1);
            String eMail = sqlData.get(2);
            String phoneNum = sqlData.get(3);
            String passWord = sqlData.get(4);
            String confirmPWord = sqlData.get(5);

            if (!passWord.equals(confirmPWord)){
                throw new Error("Password doesn't match");
            }

            stmt.setString(1, lastName);
            stmt.setString(2, iC);
            stmt.setString(3, eMail);
            stmt.setString(4, phoneNum);
            stmt.setString(5, passWord);

            ResultSet rs = stmt.executeQuery();

            if (!(rs.next())){
                String insertQuery = "INSERT INTO guestinfo (LastName, ICNum, Email, PhoneNumber, Password)" +
                        "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                preparedStatement.setString(1,lastName);
                preparedStatement.setString(2,iC);
                preparedStatement.setString(3,eMail);
                preparedStatement.setString(4,phoneNum);
                preparedStatement.setString(5,passWord);

                preparedStatement.executeUpdate();
                exit(stage,this.homePage);

                return true;
            }
            else {
                throw new Exception("Account Exists");
            }

        } catch (SQLException e) {
            e.printStackTrace();

            //Create new window to show error for invalid input
            textPage("Invalid Input. " +
                    "Possible Errors: \n" +
                    " - Phone Number already registered \n" +
                    " - Email is already registered \n" +
                    " - Input Fields are empty", "ERROR: Invalid Input", true);

        } catch (Error e) {
            textPage("Passwords don't Match","ERROR: Invalid Input",true);
        } catch (Exception e) {
            textPage("Account already exists", "ERROR: Invalid Input",true);
        }
        return false;
    }

    private void rooms(Stage oldstage){
        Stage stage = new Stage();
        String query = "SELECT * From room WHERE Status = 'available'";
        VBox vBox = new VBox(10);
        try {
            Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Image image;
            String picURL;
            while (rs.next()) {
                picURL = rs.getString("Pictures");
                image = new Image("file:Images/"+picURL+".jpg");
                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(250);
                imageView.setFitHeight(200);

                String id = String.valueOf(rs.getInt("RoomID"));
                String description = "Capacity: " + String.valueOf(rs.getInt("Capacity")) +
                        "\nPricing (per night): RM" + String.valueOf(rs.getDouble("Pricing")) +
                        "\nRoom Type: " + rs.getString("Type");
                Button button = new Button(description, imageView);
                button.setContentDisplay(ContentDisplay.RIGHT);
                button.setFont(new Font("Georgia", 25));
                button.setGraphicTextGap(20);
                button.setPrefSize(Double.MAX_VALUE, 200);
                button.setOnAction(e -> booking(stage,id,imageView,description));
                vBox.getChildren().add(button);
                vBox.setPadding(new Insets(20));
            }
            ScrollPane scrollPane = new ScrollPane(vBox);
            vBox.setBackground(new Background(new BackgroundFill(Color.web("#D0EFFF"), null, null)));
            scrollPane.setFitToWidth(true);
            Button exitButton = new Button("Exit");
            exitButton.setOnAction(e -> exit(this.homePage, stage));
            VBox exitBox = new VBox(exitButton);
            exitBox.setStyle(
                    "-fx-background-color: #FFFFE0; " +  // Light yellow background
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 10; " +
                            "-fx-border-color: linear-gradient(to bottom, #8B5A2B, #A67B5B, #DEB887); " + // Wood-like colors
                            "-fx-border-width: 10; " +           // Border thickness
                            "-fx-border-radius: 15; "
            );

            exitBox.setPadding(new Insets(50));
            exitBox.setAlignment(Pos.BOTTOM_RIGHT);

            MenuButton booking = new MenuButton("Booking Progress");

            try {
                VBox information = new VBox(10);
                String bookingProcessQuery = "select * from booking where GuestID = ?";
                PreparedStatement pstmt = conn.prepareStatement(bookingProcessQuery);
                pstmt.setString(1, String.valueOf(this.userID));
                ResultSet rs1 = pstmt.executeQuery();
                while (rs1.next()){
                    String bookingID = String.valueOf(rs1.getInt("BookingID"));
                    String statusInfo =  rs1.getString("Status");
                    String desc =
                        "Booking ID: " + bookingID +
                        "\nRoom ID: " + String.valueOf(rs1.getInt("RoomID")) +
                        "\nCheck In Date: " + rs1.getString("CheckInDate")+
                        "\nCheck Out Date: " + rs1.getString("CheckOutDate")+
                        "\nPayment Type: " + rs1.getString("PaymentType")+
                        "\nTotal Amount: " + String.valueOf(rs1.getDouble("TotalAmount")) +
                        "\nBooking Date: " + rs1.getString("BookingDate") +
                        "\nStatus: " + statusInfo
                    ;
                    MenuItem menuItem = new MenuItem(desc);
                    menuItem.setOnAction(e -> {
                        Stage infoPage = new Stage();
                        Text text = new Text(desc);
                        Button closeButton = new Button("Close");
                        closeButton.setOnAction(e3 -> {
                            infoPage.close();
                        });
                        information.getChildren().addAll(text,closeButton);
                        Button cancelBooking = new Button("Cancel Booking");
                        if (statusInfo.equals("Pending")){
                            information.getChildren().add(cancelBooking);
                        }
                        cancelBooking.setAlignment(Pos.BOTTOM_RIGHT);
                        cancelBooking.setOnAction(event -> {
                            try {
                                PreparedStatement cancelQuery = conn.prepareStatement(
                                        "Delete from booking where BookingID = ? AND Status = 'Pending'"
                                );
                                cancelQuery.setString(1,bookingID);
                                cancelQuery = conn.prepareStatement("Alter");
                                cancelQuery.executeUpdate();

                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        });

                        information.setAlignment(Pos.CENTER);
                        Scene scene = new Scene(information,300,400);
                        infoPage.setScene(scene);
                        infoPage.setResizable(false);
                        infoPage.show();

                    });
                    booking.getItems().add(menuItem);
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }


            Text text = new Text(String.valueOf(userID));
            VBox userInfo = new VBox(10, text,booking);
            userInfo.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));

            BorderPane borderPane = new BorderPane();
            BorderPane borderPane2 = new BorderPane();
            borderPane.setCenter(scrollPane);
            borderPane.setBottom(exitBox);
            borderPane2.setLeft(userInfo);
            borderPane2.setCenter(borderPane);
            Scene scene = new Scene(borderPane2, 800, 500);
            stage.setTitle("Rooms");
            stage.setScene(scene);
            scene.getStylesheets().add("file:Style.css");
            oldstage.hide();
            stage.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void Payment(Stage oldstage, int id, LocalDate checkIn, LocalDate checkOut, long days, String roomID){
        GridPane gridPane = new GridPane();
        Stage stage = new Stage();
        Label PaymentLabel = new Label("Payment Method: ");
        ChoiceBox<String> payMethods = new ChoiceBox<>();
        Label AmountLabel = new Label("Amount: ");
        Text Amount = new Text();
        Button confirmButton = new Button("Confirm Payment");

        Button exit = new Button("exit");
        exit.setOnAction(e -> exit(oldstage,stage));

        gridPane.add(PaymentLabel, 0, 0);
        gridPane.add(payMethods,1,0);
        gridPane.add(AmountLabel,0,1);
        gridPane.add(Amount,1,1);

        try {
            String query = "SELECT * From paymentstype";
            String query2 = "SELECT Pricing From room WHERE RoomID = "+id;
            Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement(); //Create a statement to allow query execution
            Statement stmt2 = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query); //store and process results of a SQL query
            while (rs.next()) {
                payMethods.getItems().add(rs.getString("PaymentType"));
            }
            ResultSet rs2 = stmt2.executeQuery(query2);
            if (rs2.next()){
                Amount.setText(String.valueOf(rs2.getDouble("Pricing")*days));
            }

            confirmButton.setOnAction(e -> {
                try {
                    String insertQuery = "INSERT INTO booking (GuestID, RoomID, CheckInDate, CheckOutDate,TotalAmount, PaymentType, BookingDate, Status)" +
                            " VALUES (?,?,?,?,?,?,?,'Pending')";
                    PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                    pstmt.setString(1, String.valueOf(id));
                    pstmt.setString(2, roomID);
                    pstmt.setString(3, String.valueOf(checkIn));
                    pstmt.setString(4, String.valueOf(checkOut));
                    pstmt.setString(5, Amount.getText());
                    pstmt.setString(6,payMethods.getValue());
                    pstmt.setString(7,LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    pstmt.executeUpdate();

                    String setUnavailable = "UPDATE room SET Status = 'occupied' WHERE room.RoomID = ?";
                    pstmt = conn.prepareStatement(setUnavailable);
                    pstmt.setString(1,roomID);
                    pstmt.executeUpdate();
                    stage.close();

                    rooms(oldstage);
                    textPage("Thank You for booking at our hotel. \nEnjoy your stay :)","Confirmation",false);

                } catch (SQLException e1){
                    e1.printStackTrace();
                    textPage("Invalid Input","ERROR: Invalid Input",true);
                }
            });

            VBox vBox = new VBox(10,gridPane,confirmButton,exit);
            vBox.setPadding(new Insets(20));
            vBox.setAlignment(Pos.CENTER);

            Scene scene = new Scene(vBox);
            stage.setTitle("Booking");
            stage.setScene(scene);
            scene.getStylesheets().add("file:Style.css");
            stage.show();
            oldstage.hide();

        } catch (SQLException e) {
            System.out.println("2");
            e.printStackTrace();
        }
    }

    public void booking(Stage oldstage,String id, ImageView imageView, String description){
        GridPane gridPane = new GridPane();
        Stage stage = new Stage();
        imageView.setFitHeight(400);
        imageView.setFitWidth(500);
        Rectangle rectangle = new Rectangle(520,420); // width, height
        rectangle.setFill(Color.BROWN);
        StackPane imagePane = new StackPane(rectangle,imageView);

        //Cancel Booking

        Text roomDetails = new Text("Room Details: \n" +description);
        roomDetails.setFont(new Font("Georgia",15));
        roomDetails.setTextAlignment(TextAlignment.LEFT);
        Text pickDate = new Text("Please Pick Your Check in and Check Out Date: ");
        pickDate.setFont(new Font("Georgia",30));

        Label checkInLabel = new Label("Check In Date: ");
        DatePicker checkInPicker = new DatePicker(LocalDate.now());
        Label checkOutLabel = new Label("Check Out Date: ");
        DatePicker checkOutPicker = new DatePicker(LocalDate.now());
        Button exit = new Button("exit");
        exit.setOnAction(e -> rooms(stage));

        gridPane.add(checkInLabel,0,0);
        gridPane.add(checkInPicker,1,0);
        gridPane.add(checkOutLabel,0,1);
        gridPane.add(checkOutPicker,1,1);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        gridPane.getColumnConstraints().addAll(col1, col2);
        gridPane.setHgap(20);
        Button nextButton = new Button("Proceed to Payment");
        nextButton.setOnAction(e -> {
            LocalDate CheckInDate = checkInPicker.getValue();
            LocalDate CheckOutDate = checkOutPicker.getValue();

            if (ChronoUnit.DAYS.between(CheckInDate,CheckOutDate) < 1){
                textPage("Invalid dates","ERROR: Invalid Input",true);
            } else if (LocalDate.now().isAfter(CheckInDate)) {
                textPage("Check In Date Must Be After Today's Date", "ERROR: Invalid Input",true);
            } else {
                Payment(stage,userID, CheckInDate, CheckOutDate,ChronoUnit.DAYS.between(CheckInDate,CheckOutDate), id);
            }
        });




        VBox vBox = new VBox(10,pickDate,imagePane,roomDetails,gridPane,nextButton,exit);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(vBox);

        scene.getStylesheets().add("file:Style.css");

        stage.setTitle("Booking");
        stage.setScene(scene);
        stage.show();
        oldstage.close();
    }

    public static void main(String[] args) {
        launch();
    }
}