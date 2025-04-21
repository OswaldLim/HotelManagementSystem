//package com.example.coursework;
//
//import javafx.animation.FadeTransition;
//import javafx.animation.FillTransition;
//import javafx.animation.ParallelTransition;
//import javafx.animation.TranslateTransition;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.chart.*;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.ChoiceBoxTableCell;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextAlignment;
//import javafx.stage.FileChooser;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import javafx.util.converter.DoubleStringConverter;
//import javafx.util.converter.IntegerStringConverter;
//import models.*;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.sql.*;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.stream.Collectors;
//
//import static services.BookingService.updateBookingInDatabase;
//import static services.RoomService.updateRoomInDatabase;
//
////Live Chill Hangout or Lounge, Chill, Hibernate
//
//public class Backup extends Application {
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db"; // Database URL
//
//    private PieChart.Data availableData;
//    private PieChart.Data cleaningData;
//    private PieChart.Data maintenenceData;
//    private PieChart.Data occupiedData;
//
//    private ObservableList<Bookings> bookingDataList = FXCollections.observableArrayList();
//
//    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//            availableData = new PieChart.Data("Available Rooms", 0),
//            cleaningData = new PieChart.Data("Rooms that need Cleaning", 0),
//            maintenenceData = new PieChart.Data("Rooms in maintenance", 0),
//            occupiedData = new PieChart.Data("Occupied Rooms", 0)
//    );
//
//    // Create PieChart
//    private final PieChart pieChart = new PieChart(pieChartData);
//
//    private int userID;
//    private Image profilePic;
//    private String role;
//
//    private Stage homePage;
//    private String action = "login";
//
//    @Override
//    public void start(Stage stage) throws IOException {
//        String bookingInfoQuery = "Select * from booking order by status";
//        try (Connection conn = DriverManager.getConnection(URL);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(bookingInfoQuery)
//        ) {
//            while (rs.next()) {
//                Bookings bookings = new Bookings(rs.getInt("BookingID"),
//                        rs.getInt("GuestID"),
//                        rs.getInt("RoomID"),
//                        rs.getDate("CheckInDate").toLocalDate(),
//                        rs.getDate("CheckOutDate").toLocalDate(),
//                        rs.getDouble("TotalAmount"),
//                        rs.getString("PaymentType"),
//                        rs.getDate("BookingDate").toLocalDate(),
//                        rs.getString("Status"));
//                this.bookingDataList.add(bookings);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        for (Bookings bookings : this.bookingDataList) {
//            if (bookings.getCheckOutDate().isBefore(LocalDate.now()) && !bookings.getStatus().equals("Canceled")) {
//                updateBookingInDatabase(bookings.getBookingID(), "Status", "Checked Out");
//                updateRoomInDatabase(bookings.getRoomID(), "Status", "cleaning");
//            } else if (bookings.getCheckInDate().isBefore(LocalDate.now())) {
//                if (bookings.getStatus().equals("Success")) {
//                    //Check in if booking is accepted
//                    updateBookingInDatabase(bookings.getBookingID(), "Status", "Checked In");
//                    updateRoomInDatabase(bookings.getRoomID(), "Status", "occupied");
//                } else if (!bookings.getStatus().equals("Checked Out") || ! bookings.getStatus().equals("Checked In")){
//                    //if the reservation is not checked out or checked in,
//                    updateBookingInDatabase(bookings.getBookingID(), "Status", "Canceled");
//                    updateRoomInDatabase(bookings.getRoomID(), "Status", "available");
//                }
//            }
//        }
//
//        this.homePage = stage;
//        //Interface
//        Text welcomeText = new Text("Welcome Back!");
//        welcomeText.setFont(new Font("Times New Roman",25));
//
//        Label nameLabel = new Label("Last Name: ");
//        TextField username = new TextField();
//        Label ICLabel = new Label("IC Number: ");
//        TextField ICnum = new TextField();
//        Label passwordLabel = new Label("Password: ");
//        PasswordField password = new PasswordField();
//        Button loginButton = new Button("Login");
//        loginButton.setPrefWidth(Double.MAX_VALUE);
//
//        loginButton.setOnAction(e -> {
//            String firstCheckQuery = "SELECT * FROM Admin WHERE Username = ?" +
//                    " AND ICNum = ?" +
//                    " AND Password = ?";
//            try (Connection conn = DriverManager.getConnection(URL);
//                 PreparedStatement pstmt1 = conn.prepareStatement(firstCheckQuery)) {
//
//                pstmt1.setString(1,username.getText());
//                pstmt1.setString(2,ICnum.getText());
//                pstmt1.setString(3,password.getText());
//                ResultSet resultSet1 = pstmt1.executeQuery();
//
//                if (resultSet1.next()) {
//                    this.userID = resultSet1.getInt("AdminID");
//                    this.role = resultSet1.getString("Role");
//                    stage.close();
//                    AdminPage();
//                    resultSet1.close();
//                } else {
//                    resultSet1.close();
//                    String secondCheckQuery = "SELECT * FROM guestinfo WHERE LastName = ?" +
//                            " AND ICNum = ?" +
//                            " AND Password = ?";
//                    try (PreparedStatement pstmt2 = conn.prepareStatement(secondCheckQuery)){
//                        pstmt2.setString(1,username.getText());
//                        pstmt2.setString(2,ICnum.getText());
//                        pstmt2.setString(3,password.getText());
//                        ResultSet resultSet2 = pstmt2.executeQuery();
//                        if (resultSet2.next()) {
//                            this.userID = resultSet2.getInt("GuestID");
//                            this.profilePic = new Image("file:Images/Profile/"+resultSet2.getString("ProfilePicPath"));
//                        } else {
//                            throw new SQLException("Invalid login credentials");
//                        }
//                        resultSet2.close();
//                        rooms(stage);
//                        username.clear();
//                        ICnum.clear();
//                        password.clear();
//                    }
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//                textPage("Invalid Login credentials","Invalid Input", true);
//            }
//        });
//
//        GridPane credentials = new GridPane();
//        credentials.setPrefHeight(300);
//        credentials.setMinWidth(400);
//        credentials.add(nameLabel, 0, 0);
//        credentials.add(username,1,0);
//        credentials.add(ICLabel, 0, 1);
//        credentials.add(ICnum,1,1);
//        credentials.add(passwordLabel, 0, 2);
//        credentials.add(password,1,2);
//        credentials.setVgap(30);
//        credentials.setHgap(10);
//
//        ColumnConstraints col1 = new ColumnConstraints();
//        col1.setPercentWidth(30);
//        ColumnConstraints col2 = new ColumnConstraints();
//        col2.setPercentWidth(70);
//
//        credentials.getColumnConstraints().addAll(col1, col2);
//
//        Button signUpButton = new Button("Sign Up");
//
//        Image image = new Image("file:logo_noBackground.png");
//        ImageView imageView = new ImageView(image);
//
//        imageView.setPreserveRatio(true);
//        imageView.fitHeightProperty().bind(stage.heightProperty().multiply(0.9));
//
//        Rectangle rectangle = new Rectangle();
//        rectangle.widthProperty().bind(stage.widthProperty().multiply(0.5));
//        rectangle.heightProperty().bind(stage.heightProperty());
//        rectangle.setX(0);
//        rectangle.setFill(Color.web("#1E3A8A"));
//
//        //make sure that you remake the rectangle translation when full screen
//        imageView.translateXProperty().bind(
//                rectangle.translateXProperty()
//                        .add(rectangle.widthProperty().divide(-2))
//                        .subtract(imageView.fitWidthProperty().divide(2))
//        );
//
//        signUpButton.setOnAction(e -> {
//            this.action = "signUp";
//            TranslateTransition moveRight = new TranslateTransition(Duration.seconds(1),rectangle);
//            moveRight.setToX(stage.getWidth()*0.49);
//
//            FillTransition blueToYellow = new FillTransition(Duration.seconds(1),rectangle);
//            blueToYellow.setFromValue(Color.web("#1E3A8A"));
//            blueToYellow.setToValue(Color.web("#EAB308"));
//
//            moveRight.play();
//            blueToYellow.play();
//        });
//
//        VBox logInInterface = new VBox(20,signUpButton,welcomeText,credentials,loginButton);
//        logInInterface.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
//        logInInterface.setPadding(new Insets(50));
//        logInInterface.setAlignment(Pos.TOP_LEFT);
//
//        //SignUpPage start
//
//        Text text = new Text("Welcome!");
//        text.setFont(new Font("Times New Roman",25));
//        Label SUnameLabel = new Label("Last Name: ");
//        TextField SUusername = new TextField();
//        Label SUICLabel = new Label("IC Number: ");
//        TextField SUICnum = new TextField();
//        checkInputType(SUICnum,Integer.class);
//
//        Label SUemailLabel = new Label("Email: ");
//        TextField SUemail = new TextField();
//        Label SUphoneNumberLabel = new Label("Phone Number: ");
//        TextField SUphoneNumber = new TextField();
//        checkInputType(SUphoneNumber, Integer.class);
//        Label SUpasswordLabel = new Label("Password: ");
//        PasswordField SUpassword = new PasswordField();
//        Label SUconfirmLabel = new Label("Confirm Password: ");
//        PasswordField SUconfirmPassword = new PasswordField();
//
//        Button SUloginButton = new Button("Log In");
//        SUloginButton.setOnAction(e -> {
//            action = "login";
//            TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(1),rectangle);
//            moveLeft.setFromX(stage.getWidth()*0.5);
//            moveLeft.setToX(0);
//
//            FillTransition yellowToBlue = new FillTransition(Duration.seconds(1),rectangle);
//            yellowToBlue.setFromValue(Color.web("#EAB308"));
//            yellowToBlue.setToValue(Color.web("#1E3A8A"));
//
//            yellowToBlue.play();
//            moveLeft.play();
//
//        });
//
//        Button SUsignUpButton = new Button("Sign Up");
//        SUsignUpButton.setPrefWidth(Double.MAX_VALUE);
//
//        SUsignUpButton.setOnAction(e -> {
//            if (CheckUserExists(stage,SUusername,SUICnum,SUemail,SUphoneNumber,SUpassword,SUconfirmPassword)) {
//                TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(1),rectangle);
//                moveLeft.setFromX(stage.getWidth()*0.5);
//                moveLeft.setToX(0);
//
//                FillTransition yellowToBlue = new FillTransition(Duration.seconds(1),rectangle);
//                yellowToBlue.setFromValue(Color.web("#EAB308"));
//                yellowToBlue.setToValue(Color.web("#1E3A8A"));
//
//                yellowToBlue.play();
//                moveLeft.play();
//
//                textPage("Please Login To Continue","Sign Up Successful!", false);
//                SUusername.clear();
//                SUICnum.clear();
//                SUemail.clear();
//                SUphoneNumber.clear();
//                SUpassword.clear();
//                SUconfirmPassword.clear();
//            }
//        });
//
//        GridPane SUcredentials = new GridPane();
//        SUcredentials.setPrefHeight(300);
//        SUcredentials.setMinWidth(400);
//        SUcredentials.add(SUnameLabel, 0, 0);
//        SUcredentials.add(SUusername,1,0);
//        SUcredentials.add(SUICLabel, 0, 1);
//        SUcredentials.add(SUICnum,1,1);
//        SUcredentials.add(SUemailLabel,0,2);
//        SUcredentials.add(SUphoneNumberLabel,0,3);
//        SUcredentials.add(SUphoneNumber,1,3);
//        SUcredentials.add(SUemail,1,2);
//        SUcredentials.add(SUpasswordLabel, 0, 4);
//        SUcredentials.add(SUpassword,1,4);
//        SUcredentials.add(SUconfirmLabel,0,5);
//        SUcredentials.add(SUconfirmPassword,1,5);
//        SUcredentials.setHgap(10);
//        SUcredentials.setVgap(10);
//
//        SUcredentials.getColumnConstraints().addAll(col1, col2);
//
//        VBox signUpInterface = new VBox(20,SUloginButton,text,SUcredentials,SUsignUpButton);
//        signUpInterface.setId("signUP");
//        signUpInterface.prefWidthProperty().bind(stage.widthProperty().multiply(0.5));
//        signUpInterface.setPadding(new Insets(50));
//        signUpInterface.setAlignment(Pos.TOP_RIGHT);
//
//        //SIgnUpPage Ending
//
//        BorderPane finalBorderPane = new BorderPane();
//        finalBorderPane.setLeft(signUpInterface);
//        finalBorderPane.setRight(logInInterface);
//
//        StackPane finalPane = new StackPane(finalBorderPane,rectangle,imageView);
//        StackPane.setAlignment(rectangle,Pos.CENTER_LEFT);
//
//        stage.widthProperty().addListener((obs,oldWidth,newWidth) -> {
//            if (this.action.equals("signUp")){
//                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),rectangle);
//                translateTransition.setToX(stage.getWidth()*0.49);
//                translateTransition.play();
//            } else {
//                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),rectangle);
//                translateTransition.setToX(0);
//                translateTransition.play();
//            }
//        });
//
//        Scene scene = new Scene(finalPane,1000,500);
//        stage.setTitle("Login Page");
//        stage.setScene(scene);
//        scene.getStylesheets().add("file:Style.css");
//        stage.show();
//    }
//
//
//
//
//
//
//    public static void main(String[] args) {
//        launch();
//    }
//
//
//
//}