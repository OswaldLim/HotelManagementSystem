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
import javafx.scene.control.cell.PropertyValueFactory;
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
        //main admin page
        Stage adminPage = new Stage();
        VBox vBox = new VBox(10);
        VBox insideScrollPane = new VBox(10);
        vBox.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane(insideScrollPane);
        scrollPane.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scrollPane);
        borderPane.setLeft(vBox);

        //end of main admin page

        //Report Page
        //first table to show total booking and revenue data
        TableView<RevenueData> tableView = new TableView<>();

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
        insideScrollPane.getChildren().addAll(yearChoice,tableView,tableView2);
        //end of report page

        Scene scene = new Scene(borderPane,500,500);
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