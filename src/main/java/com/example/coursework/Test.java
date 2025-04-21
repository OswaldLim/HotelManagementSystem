package com.example.coursework;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

//Live Chill Hangout or Lounge, Chill, Hibernate

public class Test extends Application {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db"; // Database URL

    private PieChart.Data availableData;
    private PieChart.Data cleaningData;
    private PieChart.Data maintenenceData;
    private PieChart.Data occupiedData;

    private ObservableList<Bookings> bookingDataList = FXCollections.observableArrayList();

    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            availableData = new PieChart.Data("Available Rooms", 0),
            cleaningData = new PieChart.Data("Rooms that need Cleaning", 0),
            maintenenceData = new PieChart.Data("Rooms in maintenance", 0),
            occupiedData = new PieChart.Data("Occupied Rooms", 0)
    );

    // Create PieChart
    private final PieChart pieChart = new PieChart(pieChartData);

    private int userID;
    private Image profilePic;
    private String role;

    private Stage homePage;
    private String action = "login";

    public static class Staff {
        private int staffID;
        private String staffUsername;
        private String staffIC;
        private String staffPassword;
        private String staffRole;
        private String staffEmail;
        private String staffPhoneNumber;

        public Staff(int staffID, String staffUsername, String staffIC, String staffPassword, String staffRole, String staffEmail, String staffPhoneNumber) {
            this.staffID = staffID;
            this.staffIC = staffIC;
            this.staffUsername = staffUsername;
            this.staffPassword = staffPassword;
            this.staffRole = staffRole;
            this.staffEmail = staffEmail;
            this.staffPhoneNumber = staffPhoneNumber;
        }

        public String getStaffPassword() {
            return staffPassword;
        }

        public void setStaffPassword(String staffPassword) {
            this.staffPassword = staffPassword;
        }

        public String getStaffIC() {
            return staffIC;
        }

        public void setStaffIC(String staffIC) {
            this.staffIC = staffIC;
        }

        public int getStaffID() {
            return staffID;
        }

        public void setStaffID(int staffID) {
            this.staffID = staffID;
        }

        public String getStaffUsername() {
            return staffUsername;
        }

        public void setStaffUsername(String staffUsername) {
            this.staffUsername = staffUsername;
        }

        public String getStaffRole() {
            return staffRole;
        }

        public void setStaffRole(String staffRole) {
            this.staffRole = staffRole;
        }

        public String getStaffEmail() {
            return staffEmail;
        }

        public void setStaffEmail(String staffEmail) {
            this.staffEmail = staffEmail;
        }

        public String getStaffPhoneNumber() {
            return staffPhoneNumber;
        }

        public void setStaffPhoneNumber(String staffPhoneNumber) {
            this.staffPhoneNumber = staffPhoneNumber;
        }
    }

    public static class Feedback {
        private Integer feedbackID;
        private Integer guestID;
        private String feedback;
        private String rating;
        private LocalDate created_at;

        public Feedback(Integer feedbackID, Integer guestID, String feedback, String rating, LocalDate created_at) {
            this.feedbackID = feedbackID;
            this.guestID = guestID;
            this.feedback = feedback;
            this.rating = rating;
            this.created_at = created_at;
        }

        public Integer getFeedbackID() {
            return feedbackID;
        }

        public Integer getGuestID() {
            return guestID;
        }

        public String getFeedback() {
            return feedback;
        }

        public String getRating() {
            return rating;
        }

        public LocalDate getCreated_at() {
            return created_at;
        }
    }

    public static class Bookings {
        private Integer bookingID;
        private Integer guestID;
        private Integer roomID;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private Double totalAmount;
        private String paymentType;
        private LocalDate bookingDate;
        private String status;

        public Bookings(Integer bookingID, Integer guestID, Integer roomID, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, String paymentType, LocalDate bookingDate, String status) {
            this.bookingID = bookingID;
            this.guestID = guestID;
            this.roomID = roomID;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalAmount = totalAmount;
            this.paymentType = paymentType;
            this.bookingDate = bookingDate;
            this.status = status;
        }

        public LocalDate getBookingDate() {
            return bookingDate;
        }

        public Integer getBookingID() {
            return bookingID;
        }

        public Integer getGuestID() {
            return guestID;
        }

        public Integer getRoomID() {
            return roomID;
        }

        public LocalDate getCheckInDate() {
            return checkInDate;
        }

        public LocalDate getCheckOutDate() {
            return checkOutDate;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public String getStatus() {
            return status;
        }

        public void setBookingID(Integer bookingID) {
            this.bookingID = bookingID;
        }

        public void setGuestID(Integer guestID) {
            this.guestID = guestID;
        }

        public void setRoomID(Integer roomID) {
            this.roomID = roomID;
        }

        public void setCheckInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
        }

        public void setCheckOutDate(LocalDate checkOutDate) {
            this.checkOutDate = checkOutDate;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public void setBookingDate(LocalDate bookingDate) {
            this.bookingDate = bookingDate;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public interface ConfirmationCallBack {
        void onConfirmed(boolean confirmed);
    }

    private void updateStaffInDatabase(int adminID, String column, Object newValue){
        String sql = "update Admin set " + column + " = ? WHERE AdminID = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,adminID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBookingInDatabase(int bookingID, String column, Object newValue){
        String sql = "update booking set " + column + " = ? WHERE BookingID = ?";
        try (Connection conn =DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,bookingID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGuestInDatabase(int guestID, String column, Object newValue){
        String sql = "update guestinfo set " + column + " = ? WHERE GuestID = ?";
        try (Connection conn =DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,guestID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Room {
        private Integer roomIdentificationNumber;
        private Integer roomCapacity;
        private Double roomPricing;
        private String roomType;
        private Image image;
        private String roomStatus;

        public Room(Integer roomIdentificationNumber, Integer roomCapacity, Double roomPricing, String roomType, Image image, String roomStatus) {
            this.roomIdentificationNumber = roomIdentificationNumber;
            this.roomCapacity = roomCapacity;
            this.roomPricing = roomPricing;
            this.roomType = roomType;
            this.image = image;
            this.roomStatus = roomStatus;
        }

        public Integer getRoomIdentificationNumber() {
            return roomIdentificationNumber;
        }

        public Integer getRoomCapacity() {
            return roomCapacity;
        }

        public Double getRoomPricing() {
            return roomPricing;
        }

        public String getRoomType() {
            return roomType;
        }

        public Image getImage() {
            return image;
        }

        public String getRoomStatus() {
            return roomStatus;
        }

        public void setRoomIdentificationNumber(Integer roomIdentificationNumber) {
            this.roomIdentificationNumber = roomIdentificationNumber;
        }

        public void setRoomCapacity(Integer roomCapacity) {
            this.roomCapacity = roomCapacity;
        }

        public void setRoomPricing(Double roomPricing) {
            this.roomPricing = roomPricing;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public void setPicturePath(Image image) {
            this.image = image;
        }

        public void setRoomStatus(String roomStatus) {
            this.roomStatus = roomStatus;
        }
    }

    public static class RevenueData {
        private final String month;
        private final Double totalAmount;
        private final Double occupancyRate;
        private final String paymentType;
        private final Integer transactionAmount;

        public RevenueData(String paymentType, double totalAmount, int transactionAmount) {
            this.paymentType = paymentType;
            this.totalAmount = totalAmount;
            this.transactionAmount = transactionAmount;

            this.month = null;
            this.occupancyRate=null;
        }

        public RevenueData(String month, double totalAmount, double occupancyRate){
            this.month = month;
            this.totalAmount = totalAmount;
            this.occupancyRate = occupancyRate;

            this.paymentType = null;
            this.transactionAmount = null;
        }

        public String getMonth() {
            return month;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public Double getOccupancyRate() {
            return occupancyRate;
        }

        public Integer getTransactionAmount() {
            return transactionAmount;
        }

        public String getPaymentType() {
            return paymentType;
        }
    }

    private void getRoomStatus(Label availabilityLabel, Label cleaningRoomLabel, Label maintenenceLabel, Label totalRoomLabel) {
        String roomCount = "SELECT Status, Count(*) AS Total from room Group BY Status";
        boolean available = false;
        boolean cleaning = false;
        boolean maintenance = false;
        boolean occupied = false;
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt2 = conn.createStatement();
             ResultSet rs2 = stmt2.executeQuery(roomCount);) {
            int total = 0;
            while (rs2.next()) {

                String status = rs2.getString("Status");
                int count = rs2.getInt("Total");
                total += count;
                switch (status) {
                    case "occupied":
                        occupiedData.setPieValue(count);
                        if (!pieChartData.contains(occupiedData)) {
                            pieChartData.add(occupiedData);
                        }
                        occupied = true;
                        break;
                    case "available":
                        availabilityLabel.setText(String.valueOf(count));
                        availableData.setPieValue(count);
                        if (!pieChartData.contains(availableData)) {
                            pieChartData.add(availableData);
                        }
                        available = true;
                        break;
                    case "cleaning":
                        cleaningRoomLabel.setText(String.valueOf(count));
                        cleaningData.setPieValue(count);
                        if (!pieChartData.contains(cleaningData)) {
                            pieChartData.add(cleaningData);
                        }
                        cleaning = true;
                        break;
                    case "maintenance":
                        maintenenceLabel.setText(String.valueOf(count));
                        maintenenceData.setPieValue(count);
                        if (!pieChartData.contains(maintenenceData)) {
                            pieChartData.add(maintenenceData);
                        }
                        maintenance = true;
                        break;
                    default:
                        break;
                }
            }

            if (!occupied) {
                pieChartData.remove(occupiedData);
            }
            if (!available) {
                availabilityLabel.setText("0");
                pieChartData.remove(availableData);
            }
            if (!cleaning) {
                cleaningRoomLabel.setText("0");
                pieChartData.remove(cleaningData);
            }
            if (!maintenance) {
                maintenenceLabel.setText("0");
                pieChartData.remove(maintenenceData);
            }

            totalRoomLabel.setText(String.valueOf(total));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateRoomInDatabase(int roomId, String column, Object newValue) {
        String sql = "update room set " + column + " = ? WHERE RoomID = ?";
        try (Connection conn =DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,roomId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        String bookingInfoQuery = "Select * from booking order by status";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(bookingInfoQuery)
        ) {
            while (rs.next()) {
                Bookings bookings = new Bookings(rs.getInt("BookingID"),
                        rs.getInt("GuestID"),
                        rs.getInt("RoomID"),
                        rs.getDate("CheckInDate").toLocalDate(),
                        rs.getDate("CheckOutDate").toLocalDate(),
                        rs.getDouble("TotalAmount"),
                        rs.getString("PaymentType"),
                        rs.getDate("BookingDate").toLocalDate(),
                        rs.getString("Status"));
                this.bookingDataList.add(bookings);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Bookings bookings : this.bookingDataList) {
            if (bookings.getCheckOutDate().isBefore(LocalDate.now()) && !bookings.getStatus().equals("Canceled")) {
                updateBookingInDatabase(bookings.getBookingID(), "Status", "Checked Out");
                updateRoomInDatabase(bookings.getRoomID(), "Status", "cleaning");
            } else if (bookings.getCheckInDate().isBefore(LocalDate.now())) {
                if (bookings.getStatus().equals("Success")) {
                    //Check in if booking is accepted
                    updateBookingInDatabase(bookings.getBookingID(), "Status", "Checked In");
                    updateRoomInDatabase(bookings.getRoomID(), "Status", "occupied");
                } else if (!bookings.getStatus().equals("Checked Out") || ! bookings.getStatus().equals("Checked In")){
                    //if the reservation is not checked out or checked in,
                    updateBookingInDatabase(bookings.getBookingID(), "Status", "Canceled");
                    updateRoomInDatabase(bookings.getRoomID(), "Status", "available");
                }
            }
        }

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
            String firstCheckQuery = "SELECT * FROM Admin WHERE Username = ?" +
                    " AND ICNum = ?" +
                    " AND Password = ?";
            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt1 = conn.prepareStatement(firstCheckQuery)) {

                pstmt1.setString(1,username.getText());
                pstmt1.setString(2,ICnum.getText());
                pstmt1.setString(3,password.getText());
                ResultSet resultSet1 = pstmt1.executeQuery();

                if (resultSet1.next()) {
                    this.userID = resultSet1.getInt("AdminID");
                    this.role = resultSet1.getString("Role");
                    stage.close();
                    System.out.println(String.valueOf(this.userID + this.role) );
                    AdminPage();
                    resultSet1.close();
                } else {
                    resultSet1.close();
                    String secondCheckQuery = "SELECT * FROM guestinfo WHERE LastName = ?" +
                            " AND ICNum = ?" +
                            " AND Password = ?";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(secondCheckQuery)){
                        pstmt2.setString(1,username.getText());
                        pstmt2.setString(2,ICnum.getText());
                        pstmt2.setString(3,password.getText());
                        ResultSet resultSet2 = pstmt2.executeQuery();
                        if (resultSet2.next()) {
                            this.userID = resultSet2.getInt("GuestID");
                            this.profilePic = new Image("file:Images/Profile/"+resultSet2.getString("ProfilePicPath"));
                        } else {
                            throw new SQLException("Invalid login credentials");
                        }
                        resultSet2.close();
                        rooms(stage);
                        username.clear();
                        ICnum.clear();
                        password.clear();
                    }
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

        Image image = new Image("file:logo_noBackground.png");
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(stage.heightProperty().multiply(0.9));

        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(stage.widthProperty().multiply(0.5));
        rectangle.heightProperty().bind(stage.heightProperty());
        rectangle.setX(0);
        rectangle.setFill(Color.web("#1E3A8A"));

        //make sure that you remake the rectangle translation when full screen
        imageView.translateXProperty().bind(
                rectangle.translateXProperty()
                        .add(rectangle.widthProperty().divide(-2))
                        .subtract(imageView.fitWidthProperty().divide(2))
        );

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
        checkInputType(SUICnum,Integer.class);

        Label SUemailLabel = new Label("Email: ");
        TextField SUemail = new TextField();
        Label SUphoneNumberLabel = new Label("Phone Number: ");
        TextField SUphoneNumber = new TextField();
        checkInputType(SUphoneNumber, Integer.class);
        Label SUpasswordLabel = new Label("Password: ");
        PasswordField SUpassword = new PasswordField();
        Label SUconfirmLabel = new Label("Confirm Password: ");
        PasswordField SUconfirmPassword = new PasswordField();

        Button SUloginButton = new Button("Log In");
        SUloginButton.setOnAction(e -> {
            action = "login";
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
                SUusername.clear();
                SUICnum.clear();
                SUemail.clear();
                SUphoneNumber.clear();
                SUpassword.clear();
                SUconfirmPassword.clear();
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

        StackPane finalPane = new StackPane(finalBorderPane,rectangle,imageView);
        StackPane.setAlignment(rectangle,Pos.CENTER_LEFT);

        stage.widthProperty().addListener((obs,oldWidth,newWidth) -> {
            if (this.action.equals("signUp")){
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),rectangle);
                translateTransition.setToX(stage.getWidth()*0.49);
                translateTransition.play();
            } else {
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),rectangle);
                translateTransition.setToX(0);
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
        //main admin page
        Stage adminPage = new Stage();
        VBox vBox = new VBox(10);

        Label roleLabel = new Label("Role: "+this.role);

        vBox.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane();

        Image image = new Image("file:logo_noBackground.png");

        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(adminPage.widthProperty().multiply(0.85));
        imageView.setPreserveRatio(true);

        StackPane stackPane = new StackPane(imageView, scrollPane);
        stackPane.setStyle("-fx-background-color: #FFF5EE");

        Button reportGeneration = new Button("Reports");
        reportGeneration.setOnAction(generateReport -> {
            // left side report generation
            TableView<RevenueData> tableView = new TableView<>();

            TableColumn<RevenueData, String> monthColumn = new TableColumn<>("Month");
            monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));

            TableColumn<RevenueData, Double> revenueColumn = new TableColumn<>("Total Revenue");
            revenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

            TableColumn<RevenueData, Double> occupancyColumn = new TableColumn<>("Occupancy Rate (%)");
            occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));

            tableView.getColumns().addAll(monthColumn, revenueColumn, occupancyColumn);
            for (TableColumn<?,?> column : tableView.getColumns()) {
                String headerText = column.getText();
                double headerWidthEstimate = headerText.length() * 10; // rough width per character
                column.setPrefWidth(headerWidthEstimate + 20); // add padding
            }
            ObservableList<RevenueData> data = FXCollections.observableArrayList();

            //Bar chart generation
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Month");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Total Revenue");

            BarChart<String, Number> barChart = new BarChart<>(xAxis,yAxis);
            barChart.setTitle("Monthly Revenue");
            barChart.setLegendVisible(false);

            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();

            //Second table to show payment types
            TableView<RevenueData> tableView2 = new TableView<>();

            TableColumn<RevenueData, String> paymentTypeColumn = new TableColumn<>("Payment Types");
            paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

            TableColumn<RevenueData, Integer> totalTransactionColumn = new TableColumn<>("Total Transactions");
            totalTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionAmount"));

            TableColumn<RevenueData, Double> paymentRevenueColumn = new TableColumn<>("Total Revenue");
            paymentRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

            tableView2.getColumns().addAll(paymentTypeColumn,paymentRevenueColumn,totalTransactionColumn);

            for (TableColumn<?,?> column : tableView2.getColumns()) {
                String headerText = column.getText();
                double headerWidthEstimate = headerText.length() * 10; // rough width per character
                column.setPrefWidth(headerWidthEstimate + 20); // add padding
            }

            ObservableList<RevenueData> paymentData = FXCollections.observableArrayList();

            PieChart pieChart1 = new PieChart();

            pieChart1.setTitle("Payment Types");
            pieChart1.setLegendVisible(false);
            pieChart1.setLabelsVisible(true);

            String getYear = """
                SELECT strftime('%Y', CheckInDate / 1000, 'unixepoch') AS year
                FROM booking group by year;
                """;
            ChoiceBox<String> yearChoice = new ChoiceBox<>();

            Label filterLabel = new Label("Filter by Year: ");
            HBox filterArea = new HBox(20, filterLabel, yearChoice);

            String totalMoneyPerMonth = """
                    WITH RECURSIVE DateSeries AS (
                        -- Initial Select
                        SELECT\s
                            strftime('%Y-%m-%d', CheckInDate / 1000, 'unixepoch') AS stay_date,\s
                            strftime('%Y-%m-%d', CheckoutDate / 1000, 'unixepoch') AS checkout_date,\s
                            TotalAmount
                        FROM booking
                        WHERE Status not in ('Pending','Cancelled')
                        AND strftime('%Y', CheckInDate / 1000, 'unixepoch') = ? \s
                        UNION ALL
                        SELECT\s
                            DATE(stay_date, '+1 day'),\s
                            checkout_date,\s
                            TotalAmount
                        FROM DateSeries
                        WHERE stay_date < checkout_date
                    )

                    SELECT
                        CASE strftime('%m', stay_date)
                            WHEN '01' THEN 'January'
                            WHEN '02' THEN 'February'
                            WHEN '03' THEN 'March'
                            WHEN '04' THEN 'April'
                            WHEN '05' THEN 'May'
                            WHEN '06' THEN 'June'
                            WHEN '07' THEN 'July'
                            WHEN '08' THEN 'August'
                            WHEN '09' THEN 'September'
                            WHEN '10' THEN 'October'
                            WHEN '11' THEN 'November'
                            WHEN '12' THEN 'December'
                        END AS month,
                        IFNULL(SUM(TotalAmount), 0) AS total,
                        COUNT(*) AS total_occupied_nights,
                        ROUND((COUNT(*) * 100.0) /\s
                            (strftime('%d', DATE(stay_date, 'start of month', '+1 month', '-1 day')) *
                            (SELECT COUNT(DISTINCT RoomID) FROM booking)), 2) AS occupancy_rate
                    FROM DateSeries
                    GROUP BY strftime('%m', stay_date)
                    ORDER BY strftime('%m', stay_date);

        """;

            String paymentRevenue = """
                Select PaymentType, strftime('%Y-%m-%d', BookingDate / 1000, 'unixepoch') AS formatted_date,
                COUNT(*) AS total_transactions,
                SUM(TotalAmount) AS total_revenue
                from booking
                where strftime('%Y', formatted_date) = ?
                Group by PaymentType
                """;

            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs2 = stmt.executeQuery(getYear)) {
                //Revenue Summary
                while (rs2.next()) {
                    yearChoice.getItems().add(rs2.getString("year"));
                }

                yearChoice.setOnAction(e -> {
                    try (PreparedStatement pstmt = conn.prepareStatement(totalMoneyPerMonth)) {
                        pstmt.setString(1,yearChoice.getValue());
                        ResultSet rs = pstmt.executeQuery();
                        while (rs.next()) {
                            String month = rs.getString("month");
                            double total = rs.getDouble("total");
                            double occupancy = rs.getDouble("occupancy_rate");
                            data.add(new RevenueData(month, total, occupancy));
                            dataSeries.getData().add(new XYChart.Data<>(month, total));
                        }
                        rs.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    try (PreparedStatement pstmt2 = conn.prepareStatement(paymentRevenue)) {
                        pstmt2.setString(1,yearChoice.getValue());
                        ResultSet rs1 = pstmt2.executeQuery();
                        while (rs1.next()) {
                            paymentData.add(new RevenueData(rs1.getString("PaymentType"), rs1.getDouble("total_revenue"), rs1.getInt("total_transactions")));
                            pieChart1.getData().add(new PieChart.Data(rs1.getString("PaymentType"),rs1.getDouble("total_revenue")));
                        }
                        rs1.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });
                barChart.getData().add(dataSeries);
                yearChoice.setValue(yearChoice.getItems().getLast());

            } catch (SQLException e) {
                e.printStackTrace();
            }


            //right side feedbacks
            TableView<Feedback> tableView1 = new TableView<>();
            ObservableList<Feedback> feedbackDataList = FXCollections.observableArrayList();

            TableColumn<Feedback, Integer> feedbackIDColumn = new TableColumn<>("FeedBack ID");
            feedbackIDColumn.setCellValueFactory(new PropertyValueFactory<>("feedbackID"));

            TableColumn<Feedback, Integer> guestIDColumn = new TableColumn<>("Guest ID");
            guestIDColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));

            TableColumn<Feedback, String> feedbackColumn = new TableColumn<>("FeedBack");
            feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));

            TableColumn<Feedback, String> ratingColumn = new TableColumn<>("Rating");
            ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

            TableColumn<Feedback, LocalDate> created_atColumn = new TableColumn<>("Date Created");
            created_atColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));

            tableView1.getColumns().addAll(feedbackIDColumn, guestIDColumn, feedbackColumn, ratingColumn, created_atColumn);
            for (TableColumn<?,?> column : tableView1.getColumns()) {
                String headerText = column.getText();
                double headerWidthEstimate = headerText.length() * 10; // rough width per character
                column.setPrefWidth(headerWidthEstimate + 20); // add padding
            }

            Label feedbackLabel = new Label("Feedback and Ratings");
            VBox rightSidePane = new VBox(20, feedbackLabel, tableView1);

            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("Select * from feedback")
            ) {
                while (rs.next()) {
                    feedbackDataList.add(new Feedback(
                            rs.getInt("FeedbackID"),
                            rs.getInt("GuestID"),
                            rs.getString("Feedback"),
                            rs.getString("Rating"),
                            rs.getDate("created_at").toLocalDate()
                    ));
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }

            Label table1Text = new Label("Revenue Summary: ");
            tableView.setItems(data);
            tableView1.setItems(feedbackDataList);
            tableView2.setItems(paymentData);
            VBox table1 = new VBox(10, table1Text, tableView);
            HBox table1Box = new HBox(20, table1, barChart);
            table1Box.setStyle(
                    "-fx-border-color: #8B5A2B; " +         // Wood-like border color
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-padding: 10px; " +
                            "-fx-background-color: #F5F5DC;"        // Optional warm neutral background (like beige)
            );

            Label table2Text = new Label("Payment Methods");
            VBox table2 = new VBox(10, table2Text, tableView2);
            HBox table2Box = new HBox(20, table2, pieChart1);
            table2Box.setStyle(
                    "-fx-border-color: #8B5A2B; " +         // Wood-like border color
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-padding: 10px; " +
                            "-fx-background-color: #F5F5DC;"        // Optional warm neutral background (like beige)
            );
            VBox leftSidePane = new VBox(10,filterArea,table1Box,table2Box);
            HBox insideScrollPane = new HBox(10, leftSidePane, rightSidePane);
            insideScrollPane.setStyle("-fx-background-color: #FFF5EE");
            rightSidePane.setPadding(new Insets(20));
            leftSidePane.setPadding(new Insets(20));
            switchContent(insideScrollPane, scrollPane);
            //end of report page
        });

        Button roomManagement = new Button("Room Management");
        roomManagement.setOnAction(manageRoom -> {
            //view all rooms table layout
            Label label1 = new Label("Total Rooms: ");
            Label totalRoomLabel = new Label();
            VBox totalRoomPane = new VBox(10,label1, totalRoomLabel);

            Label label2 = new Label("Rooms Available now: ");
            Label availabilityLabel = new Label();
            VBox availabilityPane = new VBox(10,label2,availabilityLabel);

            Label label3 = new Label("Rooms that need Cleaning: ");
            Label cleaningRoomLabel = new Label();
            VBox cleaningRoomPane = new VBox(10,label3,cleaningRoomLabel);

            Label label4 = new Label("Rooms that need Maintenance: ");
            Label maintenenceLabel = new Label();
            VBox maintenenceRoomPane = new VBox(10,label4, maintenenceLabel);

            VBox roomPanes = new VBox(10,totalRoomPane,availabilityPane,cleaningRoomPane,maintenenceRoomPane, pieChart);
            for (Node nodes : roomPanes.getChildren()) {
                if (nodes instanceof VBox) {
                    nodes.setStyle(
                            "-fx-border-color: #8B5A2B; " +         // Wood-like border color
                                    "-fx-border-width: 2px; " +
                                    "-fx-border-radius: 10px; " +
                                    "-fx-padding: 10px; " +
                                    "-fx-background-color: #F5F5DC;"        // Optional warm neutral background (like beige)
                    );
                    ((VBox) nodes).setAlignment(Pos.CENTER);
                    ((VBox) nodes).setPadding(new Insets(10));
                }
            }


            Label viewAllRooms = new Label("View All Rooms");
            TableView<Room> tableView = new TableView<>();

            // Optional: show labels
            pieChart.setLabelsVisible(false);
            pieChart.setLegendVisible(true);
            pieChart.setPrefSize(300, 300);
            pieChart.setMinSize(300, 300);
            pieChart.setMaxSize(300, 300);

            HBox allRoomDataArea = new HBox(10, tableView, roomPanes);

            TableColumn<Room, Integer> roomIDColumn = new TableColumn<>("Room ID");
            roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomIdentificationNumber"));

            TableColumn<Room, Integer> roomCapacityColumn = new TableColumn<>("Room Capacity");
            roomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
            roomCapacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            roomCapacityColumn.setOnEditCommit(editCapacity -> {
                Room room = editCapacity.getRowValue();
                room.setRoomCapacity(editCapacity.getNewValue());
                updateRoomInDatabase(room.getRoomIdentificationNumber(),"Capacity",editCapacity.getNewValue());
            });

            TableColumn<Room, Double> roomPricingColumn = new TableColumn<>("Room Pricing/night");
            roomPricingColumn.setCellValueFactory(new PropertyValueFactory<>("roomPricing"));
            roomPricingColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            roomPricingColumn.setOnEditCommit(editPrice -> {
                Room room = editPrice.getRowValue();
                room.setRoomPricing(editPrice.getNewValue());
                updateRoomInDatabase(room.getRoomIdentificationNumber(), "Price",editPrice.getNewValue());
            });

            TableColumn<Room, String> roomTypeColumn = new TableColumn<>("Room Type");
            roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
            ObservableList<String> roomTypes = FXCollections.observableArrayList("Deluxe", "Suite", "Standard", "Single Room");
            roomTypeColumn.setCellFactory(tableCell -> new ChoiceBoxTableCell<>(roomTypes));
            roomTypeColumn.setOnEditCommit(editType -> {
                Room room = editType.getRowValue();
                room.setRoomType(editType.getNewValue());
                updateRoomInDatabase(room.getRoomIdentificationNumber(), "Type",editType.getNewValue());
            });

            TableColumn<Room, Image> roomPictureColumn = new TableColumn<>("Image");
            roomPictureColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            roomPictureColumn.setCellFactory(col -> new TableCell<>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Image item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        imageView.setImage(item);
                        imageView.setFitWidth(80);   // Optional: Resize the image
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    }
                }
            });
            roomPictureColumn.setOnEditCommit(editPicture -> {
                Room room = editPicture.getRowValue();
                room.setPicturePath(editPicture.getNewValue());
                updateRoomInDatabase(room.getRoomIdentificationNumber(), "Pictures",editPicture.getNewValue());
            });

            TableColumn<Room, String> roomStatusColumn = new TableColumn<>("Room Availability");
            roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
            ObservableList<String> roomStatus;
            if (this.role.equals("Admin")) {
                roomStatus = FXCollections.observableArrayList("available","occupied","cleaning","maintenance");
            } else {
                roomStatus = FXCollections.observableArrayList("available","cleaning");
            }
            roomStatusColumn.setCellFactory(tc -> new ChoiceBoxTableCell<>(roomStatus));
            roomStatusColumn.setOnEditCommit(editStatus -> {
                Room room = editStatus.getRowValue();
                room.setRoomStatus(editStatus.getNewValue());
                updateRoomInDatabase(room.getRoomIdentificationNumber(),"Status", editStatus.getNewValue());
                getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);
            });


            tableView.getColumns().addAll(roomIDColumn,roomCapacityColumn, roomPricingColumn, roomTypeColumn, roomPictureColumn, roomStatusColumn);
            for (TableColumn<?,?> column : tableView.getColumns()) {
                String headerText = column.getText();
                double headerWidthEstimate = headerText.length() * 10; // rough width per character
                column.setPrefWidth(headerWidthEstimate + 20); // add padding
            }
            ObservableList<Room> roomDataList = FXCollections.observableArrayList();

            TextField roomCapacityInfo = new TextField();
            checkInputType(roomCapacityInfo,Integer.class);
            roomCapacityInfo.setPromptText("Enter Room Capacity...");

            TextField roomPricingInfo = new TextField();
            checkInputType(roomPricingInfo,Double.class);
            roomPricingInfo.setPromptText("Enter Pricing/night");

            TextField roomTypeInfo = new TextField();
            roomTypeInfo.setPromptText("Enter Room Type...");

            Button importButton = new Button("Import Image");
            ImageView roomImageView = new ImageView();
            roomImageView.setPreserveRatio(true);
            roomImageView.setFitWidth(200);

            AtomicReference<String> filePath = new AtomicReference<>("");
            importButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose an Image");

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );

                File selectedFile = fileChooser.showOpenDialog(adminPage);
                if (selectedFile != null) {
                    try {
                        String destDir = "Images/Room";

                        Path targetPath = Paths.get(destDir, selectedFile.getName());

                        Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                        filePath.set(selectedFile.toString());
                        Image roomImage = new Image("Images/Room/"+filePath.get());
                        roomImageView.setImage(roomImage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            Button submitButton = new Button("Insert Data");
            submitButton.setOnAction(submitEvent -> {
                String insertQuery = "Insert into room (Capacity, Pricing, Type, Pictures) Values (?,?,?,?)";
                if (roomCapacityInfo.getText().isEmpty() ||
                        roomPricingInfo.getText().isEmpty() ||
                        roomTypeInfo.getText().isEmpty() ||
                        filePath.get().isEmpty()
                ) {
                    textPage("Input Text Cannot be Empty", "ERROR: Invalid Input",true);
                } else {
                    try (Connection conn = DriverManager.getConnection(URL);
                         PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                    ) {
                        int insertCapacity = Integer.valueOf(roomCapacityInfo.getText());
                        double insertPrice = Double.valueOf(roomPricingInfo.getText());
                        String insertType = roomTypeInfo.getText();
                        String insertPicture = filePath.get();

                        pstmt.setString(1,String.valueOf(insertCapacity));
                        pstmt.setDouble(2,insertPrice);
                        pstmt.setString(3,insertType);
                        pstmt.setString(4,insertPicture);
                        pstmt.executeUpdate();
                        int id = roomDataList.getLast().getRoomIdentificationNumber() +1;
                        roomDataList.add(new Room(id,insertCapacity, insertPrice, insertType, new Image("Images/Room/"+filePath.get()), "available"));
                        getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);
                    } catch (SQLException exception){
                        exception.printStackTrace();
                    }
                }

            });

            Button editButton = new Button("Edit Data");
            editButton.setOnAction(editDataEvent -> {
                if (tableView.isEditable()) {
                    tableView.setEditable(false);
                    editButton.setText("Edit Data");
                } else {
                    tableView.setEditable(true);
                    editButton.setText("Save Edit");
                }
            });

            Button deleteButton = new Button("Delete Data");
            deleteButton.setOnAction(deleteDataEvent -> {
                Room selectedRoom = tableView.getSelectionModel().getSelectedItem();

                if (selectedRoom == null){
                    textPage("Please Select a room to Delete", "ERROR: Invalid Input", true);
                    return;
                }

                textPage("Are you sure you want to delete this room?", "Confirmation", false, true, confirmed -> {
                    if (confirmed) {
                        try (Connection conn = DriverManager.getConnection(URL);
                             PreparedStatement pstmt = conn.prepareStatement("Delete from room where RoomID = ?")) {
                            pstmt.setInt(1, selectedRoom.getRoomIdentificationNumber());
                            pstmt.executeUpdate();
                            roomDataList.remove(selectedRoom);
                            tableView.refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            });

            HBox roomDetailQuery = new HBox(10);
            HBox buttonArea = new HBox(20);
            if (this.role.equals("Admin")) {
                roomDetailQuery.getChildren().addAll(roomCapacityInfo, roomPricingInfo, roomTypeInfo, importButton);
                buttonArea.getChildren().addAll(submitButton,editButton, deleteButton);
            } else {
                tableView.setEditable(true);
                roomIDColumn.setEditable(false);
                roomCapacityColumn.setEditable(false);
                roomPricingColumn.setEditable(false);
                roomTypeColumn.setEditable(false);
                roomPictureColumn.setEditable(false);
                roomStatusColumn.setEditable(true);
            }

            //Available rooms
            String availableRooms = "SELECT * from room";
            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(availableRooms);
            ) {
                getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);

                while (rs.next()) {
                    Image newImage = new Image("file:Images/Room/"+rs.getString("Pictures"));
                    Room roomData = new Room(rs.getInt("RoomID"),rs.getInt("Capacity"),
                            rs.getDouble("Pricing"), rs.getString("Type"),
                            newImage,rs.getString("Status"));
                    roomDataList.add(roomData);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            tableView.setItems(roomDataList);
            VBox roomManagementPage = new VBox(10, viewAllRooms,allRoomDataArea, roomDetailQuery, buttonArea);
            roomManagementPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.86));
            roomManagementPage.prefHeightProperty().bind(adminPage.heightProperty());
            roomManagementPage.setStyle("-fx-background-color: #FFF5EE");
            roomManagementPage.setPadding(new Insets(10));
            switchContent(roomManagementPage, scrollPane);
        });

        Button reservationManagement = new Button("Reservations");
        reservationManagement.setOnAction(manageReservation -> {
            ObservableList<Integer> allRoomIDs = FXCollections.observableArrayList();
            ObservableList<Integer> allGuestIDs = FXCollections.observableArrayList();
            ObservableList<String> allPaymentMethods = FXCollections.observableArrayList();
            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt1 = conn.createStatement();
                 Statement stmt2 = conn.createStatement();
                 Statement stmt3 = conn.createStatement();
                 ResultSet rs1 = stmt1.executeQuery("Select RoomID from room where Status = 'available'");
                 ResultSet rs2 = stmt2.executeQuery("Select GuestID from guestinfo");
                 ResultSet rs3 = stmt3.executeQuery("Select PaymentType from paymentstype")
            ) {
                while (rs1.next()) {
                    allRoomIDs.add(rs1.getInt("RoomID"));
                }
                while (rs2.next()) {
                    allGuestIDs.add(rs2.getInt("GuestID"));
                }
                while (rs3.next()) {
                    allPaymentMethods.add(rs3.getString("PaymentType"));
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            VBox allReservationsPage = new VBox(10);

            TableView<Bookings> tableView = new TableView<>();

            TableColumn<Bookings, Integer> bookingIdColumn = new TableColumn<>("Booking ID");
            bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingID"));

            TableColumn<Bookings, Integer> guestIdColumn = new TableColumn<>("Guest ID");
            guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));
            guestIdColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allGuestIDs));
            guestIdColumn.setOnEditCommit(editGuestId -> {
                Bookings bookings = editGuestId.getRowValue();
                bookings.setGuestID(editGuestId.getNewValue());
                updateBookingInDatabase(bookings.getBookingID(), "GuestID", editGuestId.getNewValue());
            });

            TableColumn<Bookings, Integer> roomIdColumn = new TableColumn<>("Room ID");
            roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
            roomIdColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allRoomIDs));
            roomIdColumn.setOnEditCommit(editRoomId -> {
                Bookings bookings = editRoomId.getRowValue();
                bookings.setRoomID(editRoomId.getNewValue());
                updateBookingInDatabase(bookings.getBookingID(), "RoomID", editRoomId.getNewValue());
            });

            TableColumn<Bookings, LocalDate> checkInColumn = new TableColumn<>("Check In Date");
            checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
            checkInColumn.setCellFactory(tablecell -> new TableCell<>() {
                private final DatePicker checkInDatePicker = new DatePicker();
                {
                    checkInDatePicker.setOnAction(event -> {
                        Platform.runLater(() -> {
                            commitEdit(checkInDatePicker.getValue());
                        });
                    });

                }

                public void startEdit() {
                    super.startEdit();
                    if (!isEmpty()) {
                        checkInDatePicker.setValue(getItem());
                        setText(null);
                        setGraphic(checkInDatePicker);
                    }
                }

                public void cancelEdit() {
                    super.cancelEdit();
                    LocalDate originalDate = getItem(); // This is the unedited value
                    checkInDatePicker.setValue(originalDate); // Reset DatePicker UI

                    setText(originalDate != null ? originalDate.format(formatter) : null);
                    setGraphic(null);
                }

                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (isEditing()) {
                            checkInDatePicker.setValue(date);
                            setText(null);
                            setGraphic(checkInDatePicker);
                        } else {
                            setText(date != null ? date.format(formatter) : null);
                            setGraphic(null);
                        }
                    }
                }

                public void commitEdit(LocalDate newDate) {
                    super.commitEdit(newDate);
                    // Always access row item safely
                    Bookings booking = getTableRow().getItem();
                    if (booking != null) {
                        LocalDate checkOutDate = booking.getCheckOutDate();
                        if (newDate.isAfter(checkOutDate) || newDate.isBefore(LocalDate.now())) {
                            textPage(
                                    "Possible Errors: \n" +
                                            "- New Check In Date is After Check Out Date\n" +
                                            "- New Check In Date is Before today",
                                    "ERROR: Invalid Date", true);
                            cancelEdit();
                        }
                        else {
                            booking.setCheckInDate(newDate);
                            updateBookingInDatabase(booking.bookingID, "CheckInDate", newDate);
                        }
                    }
                }
            });

            TableColumn<Bookings, LocalDate> checkOutColumn = new TableColumn<>("Check Out Column");
            checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
            checkOutColumn.setCellFactory(tablecell -> new TableCell<>() {
                private final DatePicker checkOutDatePicker = new DatePicker();
                {
                    checkOutDatePicker.setOnAction(event -> {
                        Platform.runLater(() -> {
                            commitEdit(checkOutDatePicker.getValue());
                        });
                    });
                }

                public void startEdit() {
                    super.startEdit();
                    if (!isEmpty()) {
                        checkOutDatePicker.setValue(getItem());
                        setText(null);
                        setGraphic(checkOutDatePicker);
                    }
                }

                public void cancelEdit() {
                    super.cancelEdit();
                    LocalDate originalDate = getItem(); // This is the unedited value
                    checkOutDatePicker.setValue(originalDate); // Reset DatePicker UI

                    setText(originalDate != null ? originalDate.format(formatter) : null);
                    setGraphic(null);
                }

                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (isEditing()) {
                            checkOutDatePicker.setValue(date);
                            setText(null);
                            setGraphic(checkOutDatePicker);
                        } else {
                            setText(date != null ? date.format(formatter) : null);
                            setGraphic(null);
                        }
                    }
                }

                public void commitEdit(LocalDate newDate) {
                    super.commitEdit(newDate);
                    // Always access row item safely
                    Bookings booking = getTableRow().getItem();
                    if (booking != null) {
                        LocalDate checkInDate = booking.getCheckInDate();
                        if (newDate.isBefore(checkInDate) || newDate.isBefore(LocalDate.now())) {
                            textPage(
                                    "Possible Errors: \n" +
                                            "- New Check Out Date is Before Check In Date\n" +
                                            "- New Check Out Date is Before today",
                                    "ERROR: Invalid Date", true);
                            cancelEdit();
                        }
                        else {
                            booking.setCheckOutDate(newDate);
                            updateBookingInDatabase(booking.bookingID, "CheckOutDate", newDate);
                        }
                    }
                }
            });

            TableColumn<Bookings, Double> totalAmountColumn = new TableColumn<>("Payment Amount");
            totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            totalAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            totalAmountColumn.setOnEditCommit(editTotalAmount -> {
                Bookings bookings = editTotalAmount.getRowValue();
                bookings.setTotalAmount(editTotalAmount.getNewValue());
                updateBookingInDatabase(bookings.getBookingID(), "TotalAmount", editTotalAmount.getNewValue());
            });

            TableColumn<Bookings, String> paymentTypeColumn = new TableColumn<>("Payment Type");
            paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
            paymentTypeColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allPaymentMethods));
            paymentTypeColumn.setOnEditCommit(editPaymentType -> {
                Bookings bookings = editPaymentType.getRowValue();
                bookings.setPaymentType(editPaymentType.getNewValue());
                updateBookingInDatabase(bookings.getBookingID(), "PaymentType", editPaymentType.getNewValue());
            });

            TableColumn<Bookings, LocalDate> bookingDateColumn = new TableColumn<>("Booking Date");
            bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));

            TableColumn<Bookings, String> statusColumn = new TableColumn<>("Status");
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            ObservableList<String> statusType = FXCollections.observableArrayList("Success", "Pending", "Canceled");
            statusColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(statusType));
            statusColumn.setOnEditCommit(editStatusType -> {
                Bookings bookings = editStatusType.getRowValue();
                String newStatus = editStatusType.getNewValue();
                String oldStatus = editStatusType.getOldValue();
                textPage("Are You Sure you want to edit the Status?", "Confirmation", false, true, confirmed -> {
                    if (confirmed) {
                        bookings.setStatus(newStatus);
                        updateBookingInDatabase(bookings.getBookingID(), "Status", newStatus);
                        editStatusType.getTableView().refresh();
                    } else {
                        bookings.setStatus(oldStatus);
                        editStatusType.getTableView().refresh();
                    }
                });
            });

            tableView.getColumns().addAll(bookingIdColumn, guestIdColumn, roomIdColumn, checkInColumn, checkOutColumn, totalAmountColumn, paymentTypeColumn, bookingDateColumn, statusColumn);
            for (TableColumn<?,?> column : tableView.getColumns()) {
                String headerText = column.getText();
                double headerWidthEstimate = headerText.length() * 10; // rough width per character
                column.setPrefWidth(headerWidthEstimate + 20); // add padding
            }
            tableView.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.7));
            tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.7));

            //input boxes for inserting data
            Label insertGuestIdLabel = new Label("Insert Guest ID");
            ChoiceBox<Integer> insertGuestID = new ChoiceBox<>(allGuestIDs);

            Label insertRoomIdLabel = new Label("Insert Room ID: ");
            ChoiceBox<Integer> insertRoomID = new ChoiceBox<>(allRoomIDs);

            Label insertPaymentMethodLabel = new Label("Insert Payment Method: ");
            ChoiceBox<String> insertPaymentMethod = new ChoiceBox<>(allPaymentMethods);

            HBox inputBoxes = new HBox(10, insertGuestIdLabel ,insertGuestID, insertRoomIdLabel, insertRoomID, insertPaymentMethodLabel, insertPaymentMethod);

            Label insertCheckInDateLabel = new Label("Pick Check In Date: ");
            DatePicker insertCheckInDate = new DatePicker(LocalDate.now());

            Label insertCheckOutDateLabel = new Label("Pick Check Out Date: ");
            DatePicker insertCheckOutDate = new DatePicker(LocalDate.now());

            HBox inputDatesBox = new HBox(10, insertCheckInDateLabel, insertCheckInDate, insertCheckOutDateLabel, insertCheckOutDate);
            HBox.setMargin(insertCheckInDate, new Insets(0, 50, 0, 0));
            //end of input boxes

            //Button Area
            Button insertDataButton = new Button("Add Reservations");
            insertDataButton.setOnAction(insertDataEvent -> {
                Integer roomId = insertRoomID.getValue();
                Double totalAmount =0.0;

                LocalDate checkInDate = insertCheckInDate.getValue();
                LocalDate checkOutDate = insertCheckOutDate.getValue();

                if (ChronoUnit.DAYS.between(checkInDate,checkOutDate) < 1){
                    textPage("Check In Date Must Be Before Check Out Date","ERROR: Invalid Input",true);
                    return;
                } else if (LocalDate.now().isAfter(checkInDate)) {
                    textPage("Check In Date Must Be After Today's Date", "ERROR: Invalid Input",true);
                    return;
                }

                if (insertGuestID.getValue()==null || insertRoomID.getValue()==null || insertPaymentMethod.getValue() == null) {
                    textPage("All data fields must be filled", "ERROR: Invalid Input", true);
                    return;
                }

                try (Connection conn = DriverManager.getConnection(URL);
                     PreparedStatement pstmt = conn.prepareStatement("Select Pricing from room where RoomID = ?")
                ) {
                    pstmt.setInt(1, roomId);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        totalAmount = ChronoUnit.DAYS.between(checkInDate, checkOutDate) * rs.getDouble("Pricing");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }



                Bookings newReservation = new Bookings(
                        this.bookingDataList.getLast().getBookingID()+1,
                        insertGuestID.getValue(),
                        insertRoomID.getValue(),
                        insertCheckInDate.getValue(),
                        insertCheckOutDate.getValue(),
                        totalAmount,
                        insertPaymentMethod.getValue(),
                        LocalDate.now(),
                        "Success"
                );

                try (Connection conn = DriverManager.getConnection(URL);
                     PreparedStatement pstmt = conn.prepareStatement("Insert into booking (GuestID, RoomID, CheckInDate, CheckOutDate, TotalAmount, PaymentType , BookingDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                ) {
                    pstmt.setInt(1, newReservation.getGuestID());
                    pstmt.setInt(2, newReservation.getRoomID());
                    pstmt.setDate(3, Date.valueOf(newReservation.getCheckInDate()));
                    pstmt.setDate(4, Date.valueOf(newReservation.getCheckOutDate()));
                    pstmt.setDouble(5, newReservation.getTotalAmount());
                    pstmt.setString(6, newReservation.getPaymentType());
                    pstmt.setDate(7, Date.valueOf(newReservation.getBookingDate()));
                    pstmt.setString(8, newReservation.getStatus());
                    pstmt.executeUpdate();

                    updateRoomInDatabase(newReservation.getRoomID(), "Status", "occupied");
                    this.bookingDataList.add(newReservation);
                    tableView.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            Button editDataButton = new Button("Edit Reservations");
            editDataButton.setOnAction(editReservationEvent -> {
                if (tableView.isEditable()) {
                    editDataButton.setText("Edit Reservations");
                    tableView.setEditable(false);
                } else {
                    editDataButton.setText("Save Edits");
                    tableView.setEditable(true);
                }
            });

            Button deleteDataButton = new Button("Delete Reservations");
            deleteDataButton.setOnAction(deleteReservation -> {
                Bookings bookings = tableView.getSelectionModel().getSelectedItem();

                if (bookings == null){
                    textPage("Please Select a reservation to Delete", "ERROR: Invalid Input", true);
                    return;
                }

                textPage("Are you sure you want to delete this Reservation?", "Confirmation", false, true, confirmed -> {
                    if (confirmed) {
                        try (Connection conn = DriverManager.getConnection(URL);
                             PreparedStatement pstmt = conn.prepareStatement("Delete from booking where BookingID = ?")) {
                            pstmt.setInt(1, bookings.getBookingID());
                            pstmt.executeUpdate();
                            this.bookingDataList.remove(bookings);
                            tableView.refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            });

            HBox buttonArea = new HBox(20, insertDataButton, editDataButton, deleteDataButton);
            //End of Button Area

            tableView.setItems(this.bookingDataList);
            allReservationsPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.84));
            allReservationsPage.prefHeightProperty().bind(adminPage.heightProperty());
            allReservationsPage.getChildren().addAll(tableView, inputDatesBox, inputBoxes, buttonArea);
            allReservationsPage.setPadding(new Insets(20));
            allReservationsPage.setStyle("-fx-background-color: #FFF5EE");
            switchContent(allReservationsPage, scrollPane);
        });

        Button staffManagement = new Button("Staff Management");
        staffManagement.setOnAction(manageStaff -> {
            ObservableList<Staff> staffDataList = FXCollections.observableArrayList();
            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("Select * from Admin");
            ){
                while (rs.next()) {
                    staffDataList.add(new Staff(
                            rs.getInt("AdminID"),
                            rs.getString("Username"),
                            rs.getString("ICNum"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("Email"),
                            rs.getString("PhoneNumber")
                    ));
                }


            } catch (SQLException e){
                e.printStackTrace();
            }

            VBox allStaffPage = new VBox(10);

            TableView<Staff> tableView = new TableView<>();

            TableColumn<Staff, Integer> staffIDColumn = new TableColumn<>("Staff ID");
            staffIDColumn.setCellValueFactory(new PropertyValueFactory<>("staffID"));

            TableColumn<Staff, String> staffNameColumn = new TableColumn<>("Name");
            staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
            staffNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            staffNameColumn.setOnEditCommit(editName -> {
                Staff staff = editName.getRowValue();

                staff.setStaffUsername(editName.getNewValue());
                updateStaffInDatabase(staff.getStaffID(), "Username", editName.getNewValue());
            });

            TableColumn<Staff, String> staffICColumn = new TableColumn<>("IC Number");
            staffICColumn.setCellValueFactory(new PropertyValueFactory<>("staffIC"));
            staffICColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            staffICColumn.setOnEditCommit(editIC -> {
                Staff staff = editIC.getRowValue();
                staff.setStaffIC(editIC.getNewValue());
                updateStaffInDatabase(staff.getStaffID(), "ICNum", editIC.getNewValue());
            });


            TableColumn<Staff, String> staffPasswordColumn = new TableColumn<>("Password");
            staffPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("staffPassword"));
            staffICColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            staffICColumn.setOnEditCommit(editPassword -> {
                Staff staff = editPassword.getRowValue();

                staff.setStaffPassword(editPassword.getNewValue());
                updateStaffInDatabase(staff.getStaffID(), "Password", editPassword.getNewValue());
            });

            TableColumn<Staff, String> staffRoleColumn = new TableColumn<>("Role");
            staffRoleColumn.setCellValueFactory(new PropertyValueFactory<>("staffRole"));
            staffRoleColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn("Admin", "Cleaner", "Receptionist"));
            staffRoleColumn.setOnEditCommit(editRole -> {
                Staff staff = editRole.getRowValue();

                staff.setStaffRole(editRole.getNewValue());
                updateStaffInDatabase(staff.getStaffID(), "Role", editRole.getNewValue());
            });

            TableColumn<Staff, String> staffEmailColumn = new TableColumn<>("Email");
            staffEmailColumn.setCellValueFactory(new PropertyValueFactory<>("staffEmail"));
            staffEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            staffEmailColumn.setOnEditCommit(editEmail -> {
                Staff staff = editEmail.getRowValue();

                staff.setStaffPassword(editEmail.getNewValue());
                updateStaffInDatabase(staff.getStaffID(), "Email", editEmail.getNewValue());
            });

            TableColumn<Staff, String> staffPhoneNumberColumn = new TableColumn<>("Phone Number");
            staffPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("staffPhoneNumber"));
            staffPhoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            staffPhoneNumberColumn.setOnEditCommit(editPhoneNumber -> {
                Staff staff = editPhoneNumber.getRowValue();

                staff.setStaffPassword(editPhoneNumber.getNewValue());
                updateStaffInDatabase(staff.getStaffID(), "PhoneNumber", editPhoneNumber.getNewValue());
            });

            tableView.setItems(staffDataList);
            //without password
            tableView.getColumns().addAll(staffIDColumn, staffNameColumn, staffICColumn, staffRoleColumn, staffEmailColumn, staffPhoneNumberColumn);
            //with password
//            tableView.getColumns().addAll(staffIDColumn, staffNameColumn, staffICColumn, staffPasswordColumn, staffRoleColumn, staffEmailColumn, staffPhoneNumberColumn);
            for (TableColumn<?,?> column : tableView.getColumns()) {
                String headerText = column.getText();
                double headerWidthEstimate = headerText.length() * 20; // rough width per character
                column.setPrefWidth(headerWidthEstimate + 20); // add padding
            }
            tableView.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.7));
            tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.7));

            TextField nameField = new TextField();
            nameField.setPromptText("Enter Name...");

            TextField ICField = new TextField();
            ICField.setPromptText("Enter IC Number...");

            TextField passwordField = new PasswordField();
            passwordField.setPromptText("Enter Password...");

            TextField emailField = new TextField();
            emailField.setPromptText("Enter Email...");

            TextField phoneNumberField = new TextField();
            phoneNumberField.setPromptText("Enter Phone Number...");

            ChoiceBox<String> roleBox = new ChoiceBox<>();
            roleBox.getItems().addAll("Admin", "Receptionist", "Cleaner", "Select Role...");
            roleBox.setValue("Select Role...");

            Button addStaffButton = new Button("Add New Staff");
            addStaffButton.setOnAction(addStaff -> {
                if (!(nameField.getText().isEmpty() && emailField.getText().isEmpty() && phoneNumberField.getText().isEmpty() && roleBox.getItems().equals("Select Role..."))) {
                    String sqlQuery = "insert into Admin (Username, ICNum, Password, Email, PhoneNumber, Role) VALUES (?,?,?,?,?,?)";
                    try (Connection conn = DriverManager.getConnection(URL);
                         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)
                    ) {
                        Staff newStaff = new Staff(
                                staffDataList.getLast().getStaffID()+1,
                                nameField.getText(),
                                ICField.getText(),
                                passwordField.getText(),
                                roleBox.getValue(),
                                emailField.getText(),
                                phoneNumberField.getText()
                        );


                        pstmt.setString(1, newStaff.getStaffUsername());
                        pstmt.setString(2, newStaff.getStaffIC());
                        pstmt.setString(3, newStaff.getStaffPassword());
                        pstmt.setString(4, newStaff.getStaffEmail());
                        pstmt.setString(5, newStaff.getStaffPhoneNumber());
                        pstmt.setString(6, newStaff.getStaffRole());
                        pstmt.executeUpdate();

                        staffDataList.add(newStaff);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button removeStaffButton = new Button("Remove Staff");
            removeStaffButton.setOnAction(removeStaff -> {
                Staff staff = tableView.getSelectionModel().getSelectedItem();

                if (staff == null) {
                    textPage("Please Select a Staff to Remove", "ERROR: Invalid Input", true);
                } else {

                    textPage("Are you sure you want to remove this Staff Account?", "Confirmation", false, true, confirmed -> {
                        if (confirmed) {
                            try (Connection conn = DriverManager.getConnection(URL);
                                 PreparedStatement pstmt = conn.prepareStatement("Delete from Admin where AdminID = ?")) {
                                pstmt.setInt(1, staff.getStaffID());
                                pstmt.executeUpdate();
                                staffDataList.remove(staff);
                                tableView.refresh();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

            Button editStaffButton = new Button("Edit Staff");
            editStaffButton.setOnAction(editStaff -> {
                if (tableView.isEditable()){
                    editStaffButton.setText("Save Edit");
                    tableView.setEditable(false);
                } else  {
                    editStaffButton.setText("Edit Staff");
                    tableView.setEditable(true);
                }
            });

            HBox inputFields = new HBox(10, nameField, ICField, passwordField, emailField, phoneNumberField, roleBox);

            for (Node node : inputFields.getChildren()) {
                if (node instanceof TextField) {
                    ((TextField) node).prefWidthProperty().bind(tableView.widthProperty().divide(6));
                    ((TextField) node).setPrefHeight(30);
                }
            }

            HBox buttonArea = new HBox(10, addStaffButton, editStaffButton, removeStaffButton);

            allStaffPage.getChildren().addAll(tableView, inputFields, buttonArea);
            allStaffPage.setPadding(new Insets(20));
            allStaffPage.setStyle("-fx-background-color: #FFF5EE");
            allStaffPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.87));
            allStaffPage.prefHeightProperty().bind(adminPage.heightProperty());
            switchContent(allStaffPage, scrollPane);
        });

        Button exitButton = new Button("Exit Button");
        exitButton.setOnAction(exitAction -> {
            exit(this.homePage, adminPage);
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        if (this.role.equals("Admin")) {
            vBox.getChildren().addAll(roleLabel, reportGeneration, roomManagement, reservationManagement, staffManagement, spacer, exitButton);
        } else if (this.role.equals("Receptionist")) {
            vBox.getChildren().addAll(roleLabel, roomManagement, reservationManagement, spacer, exitButton);
        } else {
            vBox.getChildren().addAll(roleLabel, roomManagement, spacer, exitButton);
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scrollPane);
        scrollPane.setContent(stackPane);
        borderPane.setLeft(vBox);
        //end of main admin page

        Scene scene = new Scene(borderPane,1100,500);
        scene.getStylesheets().add("file:Style.css");
        adminPage.setTitle("Admin page");
        adminPage.setScene(scene);
        adminPage.show();


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

    private <T> void checkInputType(TextField textField, Class<T> type) {
        textField.setTextFormatter(new TextFormatter<T>(change -> {
            String newText = change.getControlNewText();
            if (type == Integer.class && newText.matches("\\d*")) {
                return change;
            } else if (type == Double.class && newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }

    private void textPage(String text, String title, boolean err) {
        textPage(text, title, err, false, confirmed -> {
            return;
        });
    }

    private void textPage(String text, String title,boolean err, boolean conf, ConfirmationCallBack callBack){
        Stage error = new Stage();
        error.initModality(Modality.APPLICATION_MODAL);
        Text info = new Text(text);
        info.setWrappingWidth(400);
        info.setFont(new Font("Georgia",14));
        VBox vBox = new VBox(30, info);
        vBox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        Image image = new Image("file:Images/System Logo/Error.jpeg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        if (err) {
            hBox.getChildren().addAll(imageView, vBox);
        } else {
            if (conf) {
                Button yesButton = new Button("Yes");
                yesButton.setOnAction(yesEvent -> {
                    callBack.onConfirmed(true);
                    error.close();
                });
                Button noButton = new Button("No");
                noButton.setOnAction(noEvent -> {
                    callBack.onConfirmed(false);
                    error.close();
                });
                HBox confirmationArea = new HBox(20, yesButton, noButton);
                vBox.getChildren().addAll(confirmationArea);
            }
            hBox.getChildren().add(vBox);
        }

        hBox.setPadding(new Insets(20));
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(hBox,400,150);
        error.setResizable(false);
        scene.getStylesheets().add("file:Style.css");
        error.setScene(scene);
        error.setTitle(title);
        error.showAndWait();
    }

    private boolean CheckUserExists(Stage stage, TextField... creden){
        String query = "SELECT * FROM guestinfo " +
                "WHERE LastName = ? " +
                "AND ICNum = ? " +
                "AND Email = ? " +
                "AND PhoneNumber = ? " +
                "AND Password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
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
                rs.close();

                return true;
            }
            else {
                rs.close();
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

    private void Payment(Stage oldstage, int id, LocalDate checkIn, LocalDate checkOut, long days, String roomID, String details) {

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

        try (Connection conn = DriverManager.getConnection(URL)) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * from paymentstype")) {
                while (rs.next()) {
                    payMethods.getItems().add(rs.getString("PaymentType"));
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement("Select Pricing from room where RoomID = ?")) {
                pstmt.setString(1, String.valueOf(id));
                try (ResultSet rs2 = pstmt.executeQuery()){
                    if (rs2.next()) {
                        Amount.setText(String.valueOf(rs2.getDouble("Pricing") * days));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        confirmButton.setOnAction(e -> {
            String insertQuery = "INSERT INTO booking (GuestID, RoomID, CheckInDate, CheckOutDate, TotalAmount, PaymentType, BookingDate, Status)" +
                    " VALUES (?,?,?,?,?,?,?,'Pending')";
            String setUnavailable = "UPDATE room SET Status = 'occupied' WHERE room.RoomID = ?";
            try (Connection connection = DriverManager.getConnection(URL)) {
                try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                    pstmt.setString(1, String.valueOf(id));
                    pstmt.setString(2, roomID);
                    pstmt.setDate(3, Date.valueOf(checkIn));
                    pstmt.setDate(4, Date.valueOf(checkOut));
                    pstmt.setString(5, Amount.getText());
                    pstmt.setString(6, payMethods.getValue());
                    pstmt.setDate(7, Date.valueOf(LocalDate.now()));
                    pstmt.executeUpdate();
                }


                try (PreparedStatement pstmt2 = connection.prepareStatement(setUnavailable)) {
                    pstmt2.setString(1, roomID);
                    pstmt2.executeUpdate();
                }

                stage.close();
                rooms(oldstage);
                textPage("Thank You for booking at our hotel. \nEnjoy your stay :)", "Confirmation", false);
            } catch (SQLException e1) {
                e1.printStackTrace();
                textPage("Invalid Input INSERT", "ERROR: Invalid Input", true);
            }
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

    public void booking(Stage oldstage,String id, ImageView imageView, String description, LocalDate CheckInDate, LocalDate CheckOutDate){
        GridPane gridPane = new GridPane();
        Stage stage = new Stage();
        imageView.setFitHeight(400);
        imageView.setFitWidth(500);

        Image progressBarImage = new Image("file:Images/System Logo/Process 2.png");
        ImageView progressView = new ImageView(progressBarImage);

        progressView.setPreserveRatio(true);
        progressView.fitWidthProperty().bind(stage.widthProperty().multiply(0.9));

        Rectangle rectangle = new Rectangle(530,430); // width, height
        rectangle.setStyle(
                "-fx-fill: radial-gradient(focus-angle 45deg, focus-distance 20%, center 50% 50%, radius 80%, #8B5A2B, #A67B5B, #DEB887);" +
                        "-fx-stroke: #5C4033;" +
                        "-fx-stroke-width: 3;"
        );

        StackPane imagePane = new StackPane(rectangle, imageView);
        imagePane.setStyle(
                "-fx-border-color: black;" +
                        "-fx-background-color: #FFF5EE;" +
                        "-fx-border-width: 2;" +          // Outline thickness
                        "-fx-border-radius: 5;"           // Optional: rounded corners
        );

        //Cancel Booking
        Text roomDetailLabel = new Text("Room Details:");
        roomDetailLabel.setFont(new Font("Georgia", 24));
        Text roomDetails = new Text(description);
        roomDetails.setFont(new Font("Georgia",20));
        roomDetails.setTextAlignment(TextAlignment.LEFT);
        Text detailsText = new Text("Booking Details: ");
        detailsText.setFont(new Font("Gorgia",30));

        Button exit = new Button("exit");
        exit.setOnAction(e -> rooms(stage));

        Button nextButton = new Button("Proceed to Payment");
        nextButton.setOnAction(e -> {

            if (ChronoUnit.DAYS.between(CheckInDate,CheckOutDate) < 1){
                textPage("Invalid dates","ERROR: Invalid Input",true);
            } else if (LocalDate.now().isAfter(CheckInDate)) {
                textPage("Check In Date Must Be After Today's Date", "ERROR: Invalid Input",true);
            } else {
                Payment(stage,userID, CheckInDate, CheckOutDate, ChronoUnit.DAYS.between(CheckInDate,CheckOutDate), id, description);
            }
        });

        VBox vBox = new VBox(10,progressView, detailsText,imagePane,roomDetailLabel,roomDetails,gridPane,nextButton,exit);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(vBox);

        scene.getStylesheets().add("file:Style.css");

        stage.setTitle("Booking");
        stage.setScene(scene);
        stage.show();
        oldstage.close();
    }

    public void switchContent(Node newContent, ScrollPane scrollPane) {
        Node oldContent = scrollPane.getContent();

        if (oldContent != null) {
            // Animate old content out
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), oldContent);
            slideOut.setFromX(0);
            slideOut.setToX(-scrollPane.getWidth());

            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), oldContent);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            ParallelTransition exit = new ParallelTransition(slideOut, fadeOut);
            exit.setOnFinished(e -> {
                // Set new content after old one slides out
                scrollPane.setContent(newContent);

                // Animate new content in
                newContent.setTranslateX(scrollPane.getWidth());
                newContent.setOpacity(0.0);

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newContent);
                slideIn.setFromX(scrollPane.getWidth());
                slideIn.setToX(0);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);

                new ParallelTransition(slideIn, fadeIn).play();
            });

            exit.play();
        } else {
            // No current content, just show with animation
            scrollPane.setContent(newContent);
            newContent.setTranslateX(scrollPane.getWidth());
            newContent.setOpacity(0.0);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newContent);
            slideIn.setFromX(scrollPane.getWidth());
            slideIn.setToX(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newContent);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            new ParallelTransition(slideIn, fadeIn).play();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    private void rooms(Stage oldstage){
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();
        BorderPane borderPane2 = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();

        Label checkInLabel = new Label("Check In Date: ");
        DatePicker checkInPicker = new DatePicker(LocalDate.now());
        Label checkOutLabel = new Label("Check Out Date: ");
        DatePicker checkOutPicker = new DatePicker(LocalDate.now());

        Label capacityFilterLabel = new Label("Capacity: ");
        TextField capacityAmount = new TextField();
        capacityAmount.setPromptText("Input Capacity....");
        capacityAmount.setMaxWidth(200);
        checkInputType(capacityAmount, Integer.class);

        gridPane.add(checkInLabel,0,0);
        gridPane.add(checkInPicker,1,0);
        gridPane.add(checkOutLabel,0,1);
        gridPane.add(checkOutPicker,1,1);
        gridPane.add(capacityFilterLabel,0,2);
        gridPane.add(capacityAmount,1,2);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        gridPane.getColumnConstraints().addAll(col1, col2);
        gridPane.setHgap(20);

        Button searchButton = new Button("Search");
        //filter capacity
        HBox filterBox = new HBox(30, gridPane, searchButton);

        Image progressBarImage = new Image("file:Images/System Logo/Process 1.png");
        ImageView progressView = new ImageView(progressBarImage);

        progressView.setPreserveRatio(true);
        progressView.fitWidthProperty().bind(stage.widthProperty().multiply(0.7));

        VBox topPane = new VBox(10, filterBox, progressView);
        filterBox.setPadding(new Insets(20));
        HBox.setMargin(searchButton, new Insets(25,0,0,0));

        searchButton.setOnAction(e -> {
            borderPane.setCenter(scrollPane);
            LocalDate CheckInDate = checkInPicker.getValue();
            LocalDate CheckOutDate = checkOutPicker.getValue();

            Image image;
            String picURL;
            boolean hasResults = false;
            VBox vBox = new VBox(10);

            String sqlForAvailableRooms;
            if (capacityAmount.getText().isEmpty()) {
                sqlForAvailableRooms = checkAvailableRooms(CheckInDate, CheckOutDate, null);
            } else {
                sqlForAvailableRooms = checkAvailableRooms(CheckInDate, CheckOutDate, Integer.valueOf(capacityAmount.getText()));
            }

            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlForAvailableRooms)
            ) {
                while (rs.next()) {
                    picURL = rs.getString("Pictures");
                    image = new Image("file:Images/Room/"+picURL);
                    ImageView imageView = new ImageView(image);
                    hasResults = true;

                    imageView.setFitWidth(250);
                    imageView.setFitHeight(200);

                    String id = String.valueOf(rs.getInt("RoomID"));
                    String description = "Capacity: " + String.valueOf(rs.getInt("Capacity")) +
                            "\nPricing (per night): RM" + String.valueOf(rs.getDouble("Pricing")) +
                            "\nRoom Type: " + rs.getString("Type");
                    Button button = new Button(description, imageView);
                    button.setContentDisplay(ContentDisplay.RIGHT);
                    button.setFont(new Font("Georgia", 40));
                    button.setGraphicTextGap(20);
                    button.setPrefSize(Double.MAX_VALUE, 200);
                    button.setOnAction(event -> booking(stage,id,imageView,description, CheckInDate, CheckOutDate));
                    vBox.getChildren().add(button);
                }
                vBox.setPadding(new Insets(20));
                vBox.setBackground(new Background(new BackgroundFill(Color.web("#D0EFFF"), null, null)));
                scrollPane.setContent(vBox);
                scrollPane.setFitToWidth(true);
                if (!hasResults) {
                    textPage("There is currently no room Available", "No Available rooms", true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }





        });

        //exitbox
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> exit(this.homePage, stage));
        VBox exitBox = new VBox(exitButton);
        exitBox.setStyle(
                "-fx-background-color: #FFF5EE;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 10px;" +
                        "-fx-padding: 10px;"
        );
        exitBox.setPadding(new Insets(50));
        exitBox.setAlignment(Pos.BOTTOM_RIGHT);
        //exitbox

        //user info side page
        MenuButton booking = new MenuButton("Booking Progress");
        booking.prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
        booking.prefHeightProperty().bind(stage.heightProperty().multiply(0.1));

        MenuButton booked = new MenuButton("Booked rooms");
        booked.prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
        booked.prefHeightProperty().bind(stage.heightProperty().multiply(0.1));

        Label feedback = new Label("Please give us some feedback: ");
        feedback.setWrapText(true);
        Button feedbackButton = new Button("Click here to provide feedback");

        VBox feedbackBox = new VBox(10,feedback, feedbackButton);

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
                try (Connection connection = DriverManager.getConnection(URL);
                     PreparedStatement pstmt2 = connection.prepareStatement("Insert Into feedback (GuestID, Feedback, Rating, created_at) values (?,?,?,?)")){
                    pstmt2.setString(1,String.valueOf(this.userID));
                    pstmt2.setString(2,feedbackTextArea.getText());
                    pstmt2.setString(3,ratingBox.getValue());
                    pstmt2.setDate(4,Date.valueOf(LocalDate.now()));
                    pstmt2.executeUpdate();
                } catch (SQLException exception){
                    exception.printStackTrace();
                }
                feedbackStage.close();
                textPage("Thank You for the feedback","Feedback Accepted",false);
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

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("select * from booking where GuestID = ?")) {
            pstmt.setString(1, String.valueOf(this.userID));
            ResultSet rs1 = pstmt.executeQuery();
            while (rs1.next()){
                String bookingID = String.valueOf(rs1.getInt("BookingID"));
                String roomID  = String.valueOf(rs1.getInt("RoomID"));
                String statusInfo =  rs1.getString("Status");
                Label infoLabel = new Label("Booking Details");
                infoLabel.setStyle("-fx-font-size: 24px;" +
                        "-fx-font-family: 'Lucida Handwriting';");
                String desc =
                        "Booking ID: " + bookingID +
                                "\nRoom ID: " + roomID +
                                "\nCheck In Date: " + rs1.getDate("CheckInDate").toLocalDate()+
                                "\nCheck Out Date: " + rs1.getDate("CheckOutDate").toLocalDate()+
                                "\nPayment Type: " + rs1.getString("PaymentType")+
                                "\nTotal Amount: " + String.valueOf(rs1.getDouble("TotalAmount")) +
                                "\nBooking Date: " + rs1.getDate("BookingDate").toLocalDate() +
                                "\nStatus: " + statusInfo;

                MenuItem menuItem = new MenuItem(desc);
                menuItem.setOnAction(e -> {
                    VBox information = new VBox(30);
                    Stage infoPage = new Stage();
                    infoPage.initModality(Modality.APPLICATION_MODAL);
                    Label text = new Label(desc);
                    Button closeButton = new Button("Close");
                    closeButton.setOnAction(e3 -> {
                        infoPage.close();
                    });
                    information.getChildren().addAll(infoLabel, text,closeButton);
                    Button cancelBooking = new Button("Cancel Booking");
                    if (statusInfo.equals("Pending")){
                        information.getChildren().add(cancelBooking);
                    }
                    cancelBooking.setAlignment(Pos.BOTTOM_RIGHT);
                    cancelBooking.setOnAction(event -> {
                        try (Connection connection = DriverManager.getConnection(URL);
                             PreparedStatement cancelQuery = connection.prepareStatement("Delete from booking where BookingID = ? AND Status = 'Pending'");
                             PreparedStatement alterQuery = connection.prepareStatement("Update room set status = 'available' where RoomID = ?")
                        ) {
                            cancelQuery.setString(1,bookingID);
                            alterQuery.setString(1,roomID);
                            cancelQuery.executeUpdate();
                            alterQuery.executeUpdate();
                            booking.getItems().remove(menuItem);
                            infoPage.close();
                            textPage("Booking was canceled","Cancel Booking",false);
                            if (booking.getItems().isEmpty()) {
                                booking.getItems().add(new MenuItem("No booking process in pending"));
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });

                    information.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(information,300,400);
                    scene.getStylesheets().add("file:Style.css");
                    infoPage.setScene(scene);
                    infoPage.setResizable(false);
                    infoPage.showAndWait();
                });

                if (booking.getItems().size() > 1 && booking.getItems().getFirst().getText().equals("No booking process in pending")) {
                    booking.getItems().removeFirst();
                }
                if (booked.getItems().size() > 1 && booked.getItems().getFirst().getText().equals("No Booked Rooms")){
                    booked.getItems().removeFirst();
                }

                if (statusInfo.equals("Pending")){
                    booking.getItems().add(menuItem);
                } else {
                    booked.getItems().add(menuItem);
                }
            }
            if (booking.getItems().isEmpty()) {
                booking.getItems().add(new MenuItem("No booking process in pending"));
            }
            if (booked.getItems().isEmpty()) {
                booked.getItems().add(new MenuItem("No Booked Rooms"));
            }
            rs1.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        //User Info Column
        Label welcomeText = new Label("Welcome:");

        ImageView imageView = new ImageView(this.profilePic);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        imageView.setClip(clip);
        Button profileButton = new Button();
        profileButton.setGraphic(imageView);
        profileButton.setOnAction(event -> {
            Stage changeProfileStage = new Stage();
            changeProfileStage.setResizable(false);
            changeProfileStage.initModality(Modality.APPLICATION_MODAL);
            ImageView imageView1 = new ImageView(imageView.getImage());
            imageView1.setFitWidth(300);
            imageView1.setFitHeight(300);
            Rectangle clip1 = new Rectangle(300, 300);
            clip1.setArcWidth(30);
            clip1.setArcHeight(30);
            imageView1.setClip(clip1);

            Button changeButton = new Button("Change Profile Pic");

            AtomicReference<String> filePath = new AtomicReference<>("");
            changeButton.setOnAction(changeProfileEvent -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose an Image");

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );

                File selectedFile = fileChooser.showOpenDialog(changeProfileStage);
                if (selectedFile != null) {
                    try {
                        String destDir = "Images/Profile";

                        Path targetPath = Paths.get(destDir, selectedFile.getName());

                        Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                        filePath.set(selectedFile.getName());
                        Image profileImage = new Image("file:Images/Profile/"+filePath.get());
                        updateGuestInDatabase(this.userID, "ProfilePicPath", filePath.get());
                        imageView1.setImage(profileImage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            });

            Button confirmButton = new Button("Confirm change");
            confirmButton.setOnAction(confirmEvent -> {
                if (!filePath.get().isEmpty()) {
                    imageView.setImage(new Image("file:Images/Profile/"+filePath.get()));
                    changeProfileStage.close();
                } else {
                    textPage("Please Pick an image", "ERROR: Invalid Image", true);
                }
            });

            VBox showProfile = new VBox(10, imageView1, changeButton, confirmButton);
            showProfile.setAlignment(Pos.CENTER);

            Scene scene = new Scene(showProfile, 500, 500);
            scene.getStylesheets().add("file:Style.css");
            changeProfileStage.setScene(scene);
            changeProfileStage.setTitle("Change Profile Picture");
            changeProfileStage.showAndWait();
        });

        Label userIdText = new Label("UserID: "+String.valueOf(userID));
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
                "-fx-background-color: #FFF5EE;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 10px;" +
                        "-fx-padding: 10px;"
        );

        //main page
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(exitBox);
        borderPane.setTop(topPane);
        borderPane2.setLeft(userInfo);
        borderPane2.setCenter(borderPane);
        Scene scene = new Scene(borderPane2, 800, 500);
        stage.setTitle("Rooms");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        oldstage.close();
        stage.showAndWait();
    }

    private String checkAvailableRooms(LocalDate CheckInDate, LocalDate CheckOutDate, Integer capacityAmount) {
        if (ChronoUnit.DAYS.between(CheckInDate, CheckOutDate) < 1) {
            textPage("Invalid dates", "ERROR: Invalid Input", true);
            return null;
        } else if (LocalDate.now().isAfter(CheckInDate)) {
            textPage("Check In Date Must Be After Today's Date", "ERROR: Invalid Input", true);
            return null;
        }
        String query = "SELECT RoomID From booking WHERE CheckOutDate > ?";
        String query2 = "SELECT RoomID from booking where CheckInDate < ?";
        String query3;
        ObservableList<Integer> invalidCheckInDate = FXCollections.observableArrayList();
        ObservableList<Integer> invalidCheckOutDate = FXCollections.observableArrayList();


        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             PreparedStatement pstmt2 = conn.prepareStatement(query2);
        ) {
            pstmt.setDate(1, Date.valueOf(CheckInDate));
            pstmt2.setDate(1, Date.valueOf(CheckOutDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    invalidCheckInDate.add(rs.getInt("RoomID"));
                }
            }

            try (ResultSet rs2 = pstmt2.executeQuery()) {
                while (rs2.next()) {
                    invalidCheckOutDate.add(rs2.getInt("RoomID"));
                }
            }

            ObservableList<Integer> commonRooms = FXCollections.observableArrayList(invalidCheckInDate);
            commonRooms.retainAll(invalidCheckOutDate);

            if (commonRooms.isEmpty()) {
                query3 = "Select * from room where Capacity >= ";
            } else {
                String idString = commonRooms.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                query3 = "SELECT * FROM room WHERE roomID not IN (" + idString + ") and Capacity >= ";
            }

            if (capacityAmount != null) {
                query3 += capacityAmount;
            } else {
                query3 += "1";
            }

            return query3;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}