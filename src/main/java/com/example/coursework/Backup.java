package com.example.coursework;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.swing.undo.StateEdit;
import java.sql.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

public class Backup extends Application {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db"; // Database URL

    private int userID;

    private Stage homePage;
    private String action = "login";

    public static class Room {
        private Integer roomIdentificationNumber;
        private Integer roomCapacity;
        private Double roomPricing;
        private String roomType;
        private String picturePath;
        private String roomStatus;

        public Room(Integer roomIdentificationNumber, Integer roomCapacity, Double roomPricing, String roomType, String picturePath, String roomStatus) {
            this.roomIdentificationNumber = roomIdentificationNumber;
            this.roomCapacity = roomCapacity;
            this.roomPricing = roomPricing;
            this.roomType = roomType;
            this.picturePath = picturePath;
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

        public String getPicturePath() {
            return picturePath;
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

        public void setPicturePath(String picturePath) {
            this.picturePath = picturePath;
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
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt2 = conn.createStatement();
             ResultSet rs2 = stmt2.executeQuery(roomCount);) {
            int total = 0;
            while (rs2.next()) {

                String status = rs2.getString("Status");
                int count = rs2.getInt("Total");
                total += count;
                switch (status) {
                    case "available":
                        availabilityLabel.setText(String.valueOf(count));
                        available = true;
                        break;
                    case "cleaning":
                        cleaningRoomLabel.setText(String.valueOf(count));
                        cleaning = true;
                        break;
                    case "maintenance":
                        maintenenceLabel.setText(String.valueOf(count));
                        maintenance = true;
                        break;
                    default:
                        if (!available) {
                            availabilityLabel.setText("0");
                        }
                        if (!cleaning) {
                            cleaningRoomLabel.setText("0");
                        }
                        if (!maintenance) {
                            maintenenceLabel.setText("0");
                        }
                        break;
                }
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
                    System.out.println("Admin");
                    stage.close();
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
                        } else {
                            throw new SQLException("Invalid login credentials");
                        }
                        resultSet2.close();
                        rooms(stage);
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

        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(stage.widthProperty().multiply(0.5));
        rectangle.heightProperty().bind(stage.heightProperty());
        rectangle.setX(0);
        rectangle.setFill(Color.web("#1E3A8A"));

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
        //main admin page
        Stage adminPage = new Stage();
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));

        Button reportGeneration = new Button("Reports");
        reportGeneration.setOnAction(generateReport -> {
            TableView<RevenueData> tableView = new TableView<>();
            tableView.setPrefHeight(200);

            TableColumn<RevenueData, String> monthColumn = new TableColumn<>("Month");
            monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));

            TableColumn<RevenueData, Double> revenueColumn = new TableColumn<>("Total Revenue");
            revenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

            TableColumn<RevenueData, Double> occupancyColumn = new TableColumn<>("Occupancy Rate (%)");
            occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));

            tableView.getColumns().addAll(monthColumn, revenueColumn, occupancyColumn);
            ObservableList<RevenueData> data = FXCollections.observableArrayList();

            //Second table to show payment types
            TableView<RevenueData> tableView2 = new TableView<>();
            tableView2.setPrefHeight(200);

            TableColumn<RevenueData, String> paymentTypeColumn = new TableColumn<>("Payment Types");
            paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

            TableColumn<RevenueData, Integer> totalTransactionColumn = new TableColumn<>("Total Transactions");
            totalTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionAmount"));

            TableColumn<RevenueData, Double> paymentRevenueColumn = new TableColumn<>("Total Revenue");
            paymentRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

            tableView2.getColumns().addAll(paymentTypeColumn,paymentRevenueColumn,totalTransactionColumn);
            ObservableList<RevenueData> paymentData = FXCollections.observableArrayList();

            String getYear = """
                SELECT strftime('%Y', CheckinDate) AS year from booking group by year;
                """;
            ChoiceBox<String> yearChoice = new ChoiceBox<>();

            Label filterLabel = new Label("Filter by Year: ");
            HBox filterArea = new HBox(20, filterLabel, yearChoice);
            Text table1 = new Text("Revenue Summary: ");
            Text table2 = new Text("Payment Methods");

            String totalMoneyPerMonth = """
            WITH RECURSIVE DateSeries AS (
                SELECT CheckinDate AS stay_date, CheckoutDate, TotalAmount
                FROM booking
                WHERE Status = 'Success'
                AND strftime('%Y', CheckinDate) = ?
                UNION ALL
                SELECT DATE(stay_date, '+1 day'), CheckoutDate, TotalAmount
                FROM DateSeries
                WHERE stay_date < DATE(CheckoutDate, '-1 day')
              )
    
              SELECT\s
                CASE strftime('%m', stay_date)\s
                    WHEN '01' THEN 'January'\s
                    WHEN '02' THEN 'February'\s
                    WHEN '03' THEN 'March'\s
                    WHEN '04' THEN 'April'\s
                    WHEN '05' THEN 'May'\s
                    WHEN '06' THEN 'June'\s
                    WHEN '07' THEN 'July'\s
                    WHEN '08' THEN 'August'\s
                    WHEN '09' THEN 'September'\s
                    WHEN '10' THEN 'October'\s
                    WHEN '11' THEN 'November'\s
                    WHEN '12' THEN 'December'\s
                END AS month,\s
                IFNULL(SUM(TotalAmount), 0) AS total,
                COUNT(*) AS total_occupied_nights,\s
                ROUND((COUNT(*) * 100.0) /\s
                    (strftime('%d', DATE(stay_date, 'start of month', '+1 month', '-1 day'))\s
                    * (SELECT COUNT(DISTINCT RoomID) FROM booking)), 2) AS occupancy_rate
              FROM DateSeries
              GROUP BY strftime('%m', stay_date)
              ORDER BY strftime('%m', stay_date);
        """;

            String paymentRevenue = """
                Select PaymentType,
                COUNT(*) AS total_transactions,
                SUM(TotalAmount) AS total_revenue
                from booking
                where strftime('%Y', BookingDate) = ?
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
                        }
                        rs1.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });
                yearChoice.setValue(yearChoice.getItems().getLast());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.setItems(data);
            tableView2.setItems(paymentData);
            VBox insideScrollPane1 = new VBox(10,filterArea,table1,tableView,table2,tableView2);
            insideScrollPane1.setPadding(new Insets(20));
            scrollPane.setContent(insideScrollPane1);
            //end of report page
        });

        Button roomManagement = new Button("Room Management");
        roomManagement.setOnAction(event -> {
            //view all rooms table layout
            Label label1 = new Label("Total Rooms: ");
            Label totalRoomLabel = new Label();
            VBox totalRoomPane = new VBox(10,label1, totalRoomLabel);
            totalRoomPane.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN,null,null)));

            Label label2 = new Label("Rooms Available now: ");
            Label availabilityLabel = new Label();
            VBox availabilityPane = new VBox(10,label2,availabilityLabel);
            availabilityPane.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN,null,null)));


            Label label3 = new Label("Rooms that need Cleaning: ");
            Label cleaningRoomLabel = new Label();
            VBox cleaningRoomPane = new VBox(10,label3,cleaningRoomLabel);
            cleaningRoomPane.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN,null,null)));


            Label label4 = new Label("Rooms that need Maintenance: ");
            Label maintenenceLabel = new Label();
            VBox maintenenceRoomPane = new VBox(10,label4, maintenenceLabel);
            maintenenceRoomPane.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN,null,null)));

            VBox roomPanes = new VBox(10,totalRoomPane,availabilityPane,cleaningRoomPane,maintenenceRoomPane);

            Text viewAllRooms = new Text("View All Rooms");
            TableView<Room> tableView = new TableView<>();

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

            TableColumn<Room, String> roomPictureColumn = new TableColumn<>("Picture Path");
            roomPictureColumn.setCellValueFactory(new PropertyValueFactory<>("picturePath"));
            roomPictureColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            roomPictureColumn.setOnEditCommit(editPicture -> {
                Room room = editPicture.getRowValue();
                room.setPicturePath(editPicture.getNewValue());
                updateRoomInDatabase(room.getRoomIdentificationNumber(), "Pictures",editPicture.getNewValue());
            });

            TableColumn<Room, String> roomStatusColumn = new TableColumn<>("Room Availability");
            roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
            ObservableList<String> roomStatus = FXCollections.observableArrayList("available","occupied","cleaning","maintenance");
            roomStatusColumn.setCellFactory(tc -> new ChoiceBoxTableCell<>(roomStatus));
            roomStatusColumn.setOnEditCommit(editStatus -> {
                Room room = editStatus.getRowValue();
                room.setRoomStatus(editStatus.getNewValue());
                updateRoomInDatabase(room.getRoomIdentificationNumber(),"Status", editStatus.getNewValue());
                getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);
            });


            tableView.getColumns().addAll(roomIDColumn,roomCapacityColumn, roomPricingColumn, roomTypeColumn, roomPictureColumn, roomStatusColumn);
            ObservableList<Room> roomDataList = FXCollections.observableArrayList();

            TextField roomCapacityInfo = new TextField();
            checkInputType(roomCapacityInfo,Integer.class);
            roomCapacityInfo.setPromptText("Enter Room Capacity...");

            TextField roomPricingInfo = new TextField();
            checkInputType(roomPricingInfo,Double.class);
            roomPricingInfo.setPromptText("Enter Pricing/night");

            TextField roomTypeInfo = new TextField();
            roomTypeInfo.setPromptText("Enter Room Type...");

            TextField roomPictureInfo = new TextField();
            roomPictureInfo.setPromptText("Enter Room Picture Filename...");

            Button submitButton = new Button("Insert Data");
            submitButton.setOnAction(submitEvent -> {
                String insertQuery = "Insert into room (Capacity, Pricing, Type, Pictures) Values (?,?,?,?)";
                if (roomCapacityInfo.getText().isEmpty() ||
                        roomPricingInfo.getText().isEmpty() ||
                        roomTypeInfo.getText().isEmpty() ||
                        roomPictureInfo.getText().isEmpty()
                ) {
                    textPage("Input Text Cannot be Empty", "ERROR: Invalid Input",true);
                } else {
                    try (Connection conn = DriverManager.getConnection(URL);
                         PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                    ) {
                        int insertCapacity = Integer.valueOf(roomCapacityInfo.getText());
                        double insertPrice = Double.valueOf(roomPricingInfo.getText());
                        String insertType = roomTypeInfo.getText();
                        String insertPicture = roomPictureInfo.getText();

                        pstmt.setString(1,String.valueOf(insertCapacity));
                        pstmt.setString(2,insertPicture);
                        pstmt.setString(3,insertType);
                        pstmt.setString(4,insertPicture);
                        pstmt.executeUpdate();
                        int id = roomDataList.getLast().getRoomIdentificationNumber() +1;
                        roomDataList.add(new Room(id,insertCapacity, insertPrice, insertType, insertPicture, "available"));
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
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a room to delete");
                    alert.showAndWait();
                    return;
                }

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this room?", ButtonType.YES, ButtonType.NO);
                confirmAlert.showAndWait();

                if (confirmAlert.getResult() == ButtonType.YES) {
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

            HBox buttonArea = new HBox(20,submitButton,editButton, deleteButton);

            HBox roomDetailQuery = new HBox(10, roomCapacityInfo, roomPricingInfo, roomTypeInfo, roomPictureInfo);


            //Available rooms
            String availableRooms = "SELECT * from room";
            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(availableRooms);
            ) {
                getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);
                while (rs.next()) {
                    Room roomData = new Room(rs.getInt("RoomID"),rs.getInt("Capacity"),
                            rs.getDouble("Pricing"), rs.getString("Type"),
                            rs.getString("Pictures"),rs.getString("Status"));
                    roomDataList.add(roomData);
                }
                tableView.setItems(roomDataList);
                VBox insideScrollPane2 = new VBox(10, viewAllRooms,allRoomDataArea, roomDetailQuery, buttonArea);
                scrollPane.setContent(insideScrollPane2);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        vBox.getChildren().addAll(reportGeneration, roomManagement);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scrollPane);
        borderPane.setLeft(vBox);

        //end of main admin page

        //Report Page
        //first table to show total booking and revenue data




        Scene scene = new Scene(borderPane,800,500);
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

    private void rooms(Stage oldstage){
        Stage stage = new Stage();
        String query = "SELECT * From room WHERE Status = 'available'";
        VBox vBox = new VBox(10);
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
                            "-fx-border-radius: 15; ");
            exitBox.setPadding(new Insets(50));
            exitBox.setAlignment(Pos.BOTTOM_RIGHT);

            MenuButton booking = new MenuButton("Booking Progress");

            MenuButton booked = new MenuButton("Booked rooms");

            Text feedback = new Text("Please give us some feedback: ");
            Button feedbackButton = new Button("Click here to provide feedback");

            feedbackButton.setOnAction(e -> {
                Stage feedbackStage = new Stage();
                VBox feedbackPage = new VBox(10);
                feedbackPage.setPadding(new Insets(15));

                TextArea feedbackTextArea = new TextArea();
                feedbackTextArea.setPromptText("Enter your feedback...");

                ChoiceBox<String> ratingBox = new ChoiceBox<>();
                ratingBox.setValue("Rate Us...");
                ratingBox.getItems().addAll("Rate Us","1","2","3","4","5");

                Button submitButton = new Button("Submit");
                submitButton.setOnAction(e1 -> {
                    try (Connection connection = DriverManager.getConnection(URL);
                         PreparedStatement pstmt2 = connection.prepareStatement("Insert Into feedback (GuestID, Feedback, Rating, created_at) values (?,?,?,?)")){
                        pstmt2.setString(1,String.valueOf(this.userID));
                        pstmt2.setString(2,feedbackTextArea.getText());
                        pstmt2.setString(3,ratingBox.getValue());
                        pstmt2.setString(4,LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        pstmt2.executeUpdate();
                    } catch (SQLException exception){
                        exception.printStackTrace();
                    }
                    feedbackStage.close();
                    textPage("Thank You for the feedback","Feedback Accepted",false);
                });
                feedbackPage.getChildren().addAll(feedbackTextArea,ratingBox,submitButton);
                Scene scene = new Scene(feedbackPage,300,500);
                feedbackStage.setScene(scene);
                feedbackStage.setTitle("FeedBack");
                feedbackStage.show();
            });

            try (PreparedStatement pstmt = conn.prepareStatement("select * from booking where GuestID = ?")) {
                if (booking.getItems().isEmpty()) {
                    booking.getItems().add(new MenuItem("No booking process in pending"));
                }
                if (booked.getItems().isEmpty()) {
                    booked.getItems().add(new MenuItem("No Booked Rooms"));
                }
                pstmt.setString(1, String.valueOf(this.userID));
                ResultSet rs1 = pstmt.executeQuery();
                while (rs1.next()){
                    String bookingID = String.valueOf(rs1.getInt("BookingID"));
                    String roomID  = String.valueOf(rs1.getInt("RoomID"));
                    String statusInfo =  rs1.getString("Status");
                    String desc =
                            "Booking ID: " + bookingID +
                                    "\nRoom ID: " + roomID +
                                    "\nCheck In Date: " + rs1.getString("CheckInDate")+
                                    "\nCheck Out Date: " + rs1.getString("CheckOutDate")+
                                    "\nPayment Type: " + rs1.getString("PaymentType")+
                                    "\nTotal Amount: " + String.valueOf(rs1.getDouble("TotalAmount")) +
                                    "\nBooking Date: " + rs1.getString("BookingDate") +
                                    "\nStatus: " + statusInfo;

                    MenuItem menuItem = new MenuItem(desc);
                    menuItem.setOnAction(e -> {
                        VBox information = new VBox(10);
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
                        infoPage.setScene(scene);
                        infoPage.setResizable(false);
                        infoPage.show();
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
                rs1.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            Text text = new Text(String.valueOf(userID));
            VBox userInfo = new VBox(10, text,booking,booked,feedback,feedbackButton);
            feedbackButton.setAlignment(Pos.BOTTOM_LEFT);
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
            oldstage.close();
            stage.show();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Payment(Stage oldstage, int id, LocalDate checkIn, LocalDate checkOut, long days, String roomID) {
        GridPane gridPane = new GridPane();
        Stage stage = new Stage();
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
            String insertQuery = "INSERT INTO booking (GuestID, RoomID, CheckInDate, CheckOutDate,TotalAmount, PaymentType, BookingDate, Status)" +
                    " VALUES (?,?,?,?,?,?,?,'Pending')";
            String setUnavailable = "UPDATE room SET Status = 'occupied' WHERE room.RoomID = ?";
            try (Connection connection = DriverManager.getConnection(URL)) {
                try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                    pstmt.setString(1, String.valueOf(id));
                    pstmt.setString(2, roomID);
                    pstmt.setString(3, String.valueOf(checkIn));
                    pstmt.setString(4, String.valueOf(checkOut));
                    pstmt.setString(5, Amount.getText());
                    pstmt.setString(6, payMethods.getValue());
                    pstmt.setString(7, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
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

        VBox vBox = new VBox(10, gridPane, confirmButton, exit);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        stage.setTitle("Booking");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        stage.show();
        oldstage.close();
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