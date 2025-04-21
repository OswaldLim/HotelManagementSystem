package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static controllers.RoomStatusController.getPieChartData;
import static controllers.SceneController.exit;
import static controllers.SceneController.switchContent;
import static views.ReportGenerationView.generateReportView;
import static views.ReservationPageView.getReservationPageView;
import static views.RoomManagementPageView.generateRoomManagementPage;
import static views.StaffManagementPageView.getStaffManagementPageView;

public class MainAdminView {
    public static void showAdminUI(String role, Stage homePage){

        //main admin page
        Stage adminPage = new Stage();
        VBox vBox = new VBox(10);

        Label roleLabel = new Label("Role: "+role);

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
            HBox reportPage = generateReportView();
            switchContent(reportPage, scrollPane);
        });

        Button roomManagement = new Button("Room Management");
        roomManagement.setOnAction(manageRoom -> {
            PieChart pieChart = new PieChart(getPieChartData());
            VBox roomManagementPage = generateRoomManagementPage(pieChart,role,adminPage);
            switchContent(roomManagementPage, scrollPane);
        });

        Button reservationManagement = new Button("Reservations");
        reservationManagement.setOnAction(manageReservation -> {
            VBox allReservationsPage = getReservationPageView(adminPage);
            switchContent(allReservationsPage, scrollPane);
        });

        Button staffManagement = new Button("Staff Management");
        staffManagement.setOnAction(manageStaff -> {
            VBox allStaffPage = getStaffManagementPageView(adminPage);
            switchContent(allStaffPage, scrollPane);

        });

        Button exitButton = new Button("Exit Button");

        exitButton.setOnAction(exitAction -> {
            exit(homePage, adminPage);
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        if (role.equals("Admin")) {
            vBox.getChildren().addAll(roleLabel, reportGeneration, roomManagement, reservationManagement, staffManagement, spacer, exitButton);
        } else if (role.equals("Receptionist")) {
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










//    private void AdminPage(){
//        //main admin page
//        Stage adminPage = new Stage();
//        VBox vBox = new VBox(10);
//
//        Label roleLabel = new Label("Role: "+this.role);
//
//        vBox.setPadding(new Insets(15));
//        ScrollPane scrollPane = new ScrollPane();
//
//        Image image = new Image("file:logo_noBackground.png");
//
//        ImageView imageView = new ImageView(image);
//        imageView.fitWidthProperty().bind(adminPage.widthProperty().multiply(0.85));
//        imageView.setPreserveRatio(true);
//
//        StackPane stackPane = new StackPane(imageView, scrollPane);
//        stackPane.setStyle("-fx-background-color: #FFF5EE");
//
//        Button reportGeneration = new Button("Reports");
//        reportGeneration.setOnAction(generateReport -> {
//            // left side report generation
//            TableView<RevenueData> tableView = new TableView<>();
//
//            TableColumn<RevenueData, String> monthColumn = new TableColumn<>("Month");
//            monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
//
//            TableColumn<RevenueData, Double> revenueColumn = new TableColumn<>("Total Revenue");
//            revenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//
//            TableColumn<RevenueData, Double> occupancyColumn = new TableColumn<>("Occupancy Rate (%)");
//            occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));
//
//            tableView.getColumns().addAll(monthColumn, revenueColumn, occupancyColumn);
//            for (TableColumn<?,?> column : tableView.getColumns()) {
//                String headerText = column.getText();
//                double headerWidthEstimate = headerText.length() * 10; // rough width per character
//                column.setPrefWidth(headerWidthEstimate + 20); // add padding
//            }
//            ObservableList<RevenueData> data = FXCollections.observableArrayList();
//
//            //Bar chart generation
//            CategoryAxis xAxis = new CategoryAxis();
//            xAxis.setLabel("Month");
//
//            NumberAxis yAxis = new NumberAxis();
//            yAxis.setLabel("Total Revenue");
//
//            BarChart<String, Number> barChart = new BarChart<>(xAxis,yAxis);
//            barChart.setTitle("Monthly Revenue");
//            barChart.setLegendVisible(false);
//
//            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
//
//            //Second table to show payment types
//            TableView<RevenueData> tableView2 = new TableView<>();
//
//            TableColumn<RevenueData, String> paymentTypeColumn = new TableColumn<>("Payment Types");
//            paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
//
//            TableColumn<RevenueData, Integer> totalTransactionColumn = new TableColumn<>("Total Transactions");
//            totalTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionAmount"));
//
//            TableColumn<RevenueData, Double> paymentRevenueColumn = new TableColumn<>("Total Revenue");
//            paymentRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//
//            tableView2.getColumns().addAll(paymentTypeColumn,paymentRevenueColumn,totalTransactionColumn);
//
//            for (TableColumn<?,?> column : tableView2.getColumns()) {
//                String headerText = column.getText();
//                double headerWidthEstimate = headerText.length() * 10; // rough width per character
//                column.setPrefWidth(headerWidthEstimate + 20); // add padding
//            }
//
//            ObservableList<RevenueData> paymentData = FXCollections.observableArrayList();
//
//            PieChart pieChart1 = new PieChart();
//
//            pieChart1.setTitle("Payment Types");
//            pieChart1.setLegendVisible(false);
//            pieChart1.setLabelsVisible(true);
//
//            String getYear = """
//                SELECT strftime('%Y', CheckInDate / 1000, 'unixepoch') AS year
//                FROM booking group by year;
//                """;
//            ChoiceBox<String> yearChoice = new ChoiceBox<>();
//
//            Label filterLabel = new Label("Filter by Year: ");
//            HBox filterArea = new HBox(20, filterLabel, yearChoice);
//
//            String totalMoneyPerMonth = """
//                    WITH RECURSIVE DateSeries AS (
//                        -- Initial Select
//                        SELECT\s
//                            strftime('%Y-%m-%d', CheckInDate / 1000, 'unixepoch') AS stay_date,\s
//                            strftime('%Y-%m-%d', CheckoutDate / 1000, 'unixepoch') AS checkout_date,\s
//                            TotalAmount
//                        FROM booking
//                        WHERE Status not in ('Pending','Cancelled')
//                        AND strftime('%Y', CheckInDate / 1000, 'unixepoch') = ? \s
//                        UNION ALL
//                        SELECT\s
//                            DATE(stay_date, '+1 day'),\s
//                            checkout_date,\s
//                            TotalAmount
//                        FROM DateSeries
//                        WHERE stay_date < checkout_date
//                    )
//
//                    SELECT
//                        CASE strftime('%m', stay_date)
//                            WHEN '01' THEN 'January'
//                            WHEN '02' THEN 'February'
//                            WHEN '03' THEN 'March'
//                            WHEN '04' THEN 'April'
//                            WHEN '05' THEN 'May'
//                            WHEN '06' THEN 'June'
//                            WHEN '07' THEN 'July'
//                            WHEN '08' THEN 'August'
//                            WHEN '09' THEN 'September'
//                            WHEN '10' THEN 'October'
//                            WHEN '11' THEN 'November'
//                            WHEN '12' THEN 'December'
//                        END AS month,
//                        IFNULL(SUM(TotalAmount), 0) AS total,
//                        COUNT(*) AS total_occupied_nights,
//                        ROUND((COUNT(*) * 100.0) /\s
//                            (strftime('%d', DATE(stay_date, 'start of month', '+1 month', '-1 day')) *
//                            (SELECT COUNT(DISTINCT RoomID) FROM booking)), 2) AS occupancy_rate
//                    FROM DateSeries
//                    GROUP BY strftime('%m', stay_date)
//                    ORDER BY strftime('%m', stay_date);
//
//        """;
//
//            String paymentRevenue = """
//                Select PaymentType, strftime('%Y-%m-%d', BookingDate / 1000, 'unixepoch') AS formatted_date,
//                COUNT(*) AS total_transactions,
//                SUM(TotalAmount) AS total_revenue
//                from booking
//                where strftime('%Y', formatted_date) = ?
//                Group by PaymentType
//                """;
//
//            try (Connection conn = DriverManager.getConnection(URL);
//                 Statement stmt = conn.createStatement();
//                 ResultSet rs2 = stmt.executeQuery(getYear)) {
//                //Revenue Summary
//                while (rs2.next()) {
//                    yearChoice.getItems().add(rs2.getString("year"));
//                }
//
//                yearChoice.setOnAction(e -> {
//                    try (PreparedStatement pstmt = conn.prepareStatement(totalMoneyPerMonth)) {
//                        pstmt.setString(1,yearChoice.getValue());
//                        ResultSet rs = pstmt.executeQuery();
//                        while (rs.next()) {
//                            String month = rs.getString("month");
//                            double total = rs.getDouble("total");
//                            double occupancy = rs.getDouble("occupancy_rate");
//                            data.add(new RevenueData(month, total, occupancy));
//                            dataSeries.getData().add(new XYChart.Data<>(month, total));
//                        }
//                        rs.close();
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    }
//
//                    try (PreparedStatement pstmt2 = conn.prepareStatement(paymentRevenue)) {
//                        pstmt2.setString(1,yearChoice.getValue());
//                        ResultSet rs1 = pstmt2.executeQuery();
//                        while (rs1.next()) {
//                            paymentData.add(new RevenueData(rs1.getString("PaymentType"), rs1.getDouble("total_revenue"), rs1.getInt("total_transactions")));
//                            pieChart1.getData().add(new PieChart.Data(rs1.getString("PaymentType"),rs1.getDouble("total_revenue")));
//                        }
//                        rs1.close();
//                    } catch (SQLException e1) {
//                        e1.printStackTrace();
//                    }
//                });
//                barChart.getData().add(dataSeries);
//                yearChoice.setValue(yearChoice.getItems().getLast());
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//
//            //right side feedbacks
//            TableView<Feedback> tableView1 = new TableView<>();
//            ObservableList<Feedback> feedbackDataList = FXCollections.observableArrayList();
//
//            TableColumn<Feedback, Integer> feedbackIDColumn = new TableColumn<>("FeedBack ID");
//            feedbackIDColumn.setCellValueFactory(new PropertyValueFactory<>("feedbackID"));
//
//            TableColumn<Feedback, Integer> guestIDColumn = new TableColumn<>("Guest ID");
//            guestIDColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));
//
//            TableColumn<Feedback, String> feedbackColumn = new TableColumn<>("FeedBack");
//            feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));
//
//            TableColumn<Feedback, String> ratingColumn = new TableColumn<>("Rating");
//            ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
//
//            TableColumn<Feedback, LocalDate> created_atColumn = new TableColumn<>("Date Created");
//            created_atColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
//
//            tableView1.getColumns().addAll(feedbackIDColumn, guestIDColumn, feedbackColumn, ratingColumn, created_atColumn);
//            for (TableColumn<?,?> column : tableView1.getColumns()) {
//                String headerText = column.getText();
//                double headerWidthEstimate = headerText.length() * 10; // rough width per character
//                column.setPrefWidth(headerWidthEstimate + 20); // add padding
//            }
//
//            Label feedbackLabel = new Label("Feedback and Ratings");
//            VBox rightSidePane = new VBox(20, feedbackLabel, tableView1);
//
//            try (Connection conn = DriverManager.getConnection(URL);
//                 Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery("Select * from feedback")
//            ) {
//                while (rs.next()) {
//                    feedbackDataList.add(new Feedback(
//                            rs.getInt("FeedbackID"),
//                            rs.getInt("GuestID"),
//                            rs.getString("Feedback"),
//                            rs.getString("Rating"),
//                            rs.getDate("created_at").toLocalDate()
//                    ));
//                }
//            } catch (SQLException e2) {
//                e2.printStackTrace();
//            }
//
//            Label table1Text = new Label("Revenue Summary: ");
//            tableView.setItems(data);
//            tableView1.setItems(feedbackDataList);
//            tableView2.setItems(paymentData);
//            VBox table1 = new VBox(10, table1Text, tableView);
//            HBox table1Box = new HBox(20, table1, barChart);
//            table1Box.setStyle(
//                    "-fx-border-color: #8B5A2B; " +         // Wood-like border color
//                            "-fx-border-width: 2px; " +
//                            "-fx-border-radius: 10px; " +
//                            "-fx-padding: 10px; " +
//                            "-fx-background-color: #F5F5DC;"        // Optional warm neutral background (like beige)
//            );
//
//            Label table2Text = new Label("Payment Methods");
//            VBox table2 = new VBox(10, table2Text, tableView2);
//            HBox table2Box = new HBox(20, table2, pieChart1);
//            table2Box.setStyle(
//                    "-fx-border-color: #8B5A2B; " +         // Wood-like border color
//                            "-fx-border-width: 2px; " +
//                            "-fx-border-radius: 10px; " +
//                            "-fx-padding: 10px; " +
//                            "-fx-background-color: #F5F5DC;"        // Optional warm neutral background (like beige)
//            );
//            VBox leftSidePane = new VBox(10,filterArea,table1Box,table2Box);
//            HBox insideScrollPane = new HBox(10, leftSidePane, rightSidePane);
//            insideScrollPane.setStyle("-fx-background-color: #FFF5EE");
//            rightSidePane.setPadding(new Insets(20));
//            leftSidePane.setPadding(new Insets(20));
//            switchContent(insideScrollPane, scrollPane);
//            //end of report page
//        });
//
//        Button roomManagement = new Button("Room Management");
//        roomManagement.setOnAction(manageRoom -> {
//            //view all rooms table layout
//            Label label1 = new Label("Total Rooms: ");
//            Label totalRoomLabel = new Label();
//            VBox totalRoomPane = new VBox(10,label1, totalRoomLabel);
//
//            Label label2 = new Label("Rooms Available now: ");
//            Label availabilityLabel = new Label();
//            VBox availabilityPane = new VBox(10,label2,availabilityLabel);
//
//            Label label3 = new Label("Rooms that need Cleaning: ");
//            Label cleaningRoomLabel = new Label();
//            VBox cleaningRoomPane = new VBox(10,label3,cleaningRoomLabel);
//
//            Label label4 = new Label("Rooms that need Maintenance: ");
//            Label maintenenceLabel = new Label();
//            VBox maintenenceRoomPane = new VBox(10,label4, maintenenceLabel);
//
//            VBox roomPanes = new VBox(10,totalRoomPane,availabilityPane,cleaningRoomPane,maintenenceRoomPane, pieChart);
//            for (Node nodes : roomPanes.getChildren()) {
//                if (nodes instanceof VBox) {
//                    nodes.setStyle(
//                            "-fx-border-color: #8B5A2B; " +         // Wood-like border color
//                                    "-fx-border-width: 2px; " +
//                                    "-fx-border-radius: 10px; " +
//                                    "-fx-padding: 10px; " +
//                                    "-fx-background-color: #F5F5DC;"        // Optional warm neutral background (like beige)
//                    );
//                    ((VBox) nodes).setAlignment(Pos.CENTER);
//                    ((VBox) nodes).setPadding(new Insets(10));
//                }
//            }
//
//
//            Label viewAllRooms = new Label("View All Rooms");
//            TableView<Room> tableView = new TableView<>();
//
//            // Optional: show labels
//            pieChart.setLabelsVisible(false);
//            pieChart.setLegendVisible(true);
//            pieChart.setPrefSize(300, 300);
//            pieChart.setMinSize(300, 300);
//            pieChart.setMaxSize(300, 300);
//
//            HBox allRoomDataArea = new HBox(10, tableView, roomPanes);
//
//            TableColumn<Room, Integer> roomIDColumn = new TableColumn<>("Room ID");
//            roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomIdentificationNumber"));
//
//            TableColumn<Room, Integer> roomCapacityColumn = new TableColumn<>("Room Capacity");
//            roomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
//            roomCapacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//            roomCapacityColumn.setOnEditCommit(editCapacity -> {
//                Room room = editCapacity.getRowValue();
//                room.setRoomCapacity(editCapacity.getNewValue());
//                updateRoomInDatabase(room.getRoomIdentificationNumber(),"Capacity",editCapacity.getNewValue());
//            });
//
//            TableColumn<Room, Double> roomPricingColumn = new TableColumn<>("Room Pricing/night");
//            roomPricingColumn.setCellValueFactory(new PropertyValueFactory<>("roomPricing"));
//            roomPricingColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
//            roomPricingColumn.setOnEditCommit(editPrice -> {
//                Room room = editPrice.getRowValue();
//                room.setRoomPricing(editPrice.getNewValue());
//                updateRoomInDatabase(room.getRoomIdentificationNumber(), "Price",editPrice.getNewValue());
//            });
//
//            TableColumn<Room, String> roomTypeColumn = new TableColumn<>("Room Type");
//            roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
//            ObservableList<String> roomTypes = FXCollections.observableArrayList("Deluxe", "Suite", "Standard", "Single Room");
//            roomTypeColumn.setCellFactory(tableCell -> new ChoiceBoxTableCell<>(roomTypes));
//            roomTypeColumn.setOnEditCommit(editType -> {
//                Room room = editType.getRowValue();
//                room.setRoomType(editType.getNewValue());
//                updateRoomInDatabase(room.getRoomIdentificationNumber(), "Type",editType.getNewValue());
//            });
//
//            TableColumn<Room, Image> roomPictureColumn = new TableColumn<>("Image");
//            roomPictureColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
//            roomPictureColumn.setCellFactory(col -> new TableCell<>() {
//                private final ImageView imageView = new ImageView();
//
//                @Override
//                protected void updateItem(Image item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || item == null) {
//                        setGraphic(null);
//                    } else {
//                        imageView.setImage(item);
//                        imageView.setFitWidth(80);   // Optional: Resize the image
//                        imageView.setPreserveRatio(true);
//                        setGraphic(imageView);
//                    }
//                }
//            });
//            roomPictureColumn.setOnEditCommit(editPicture -> {
//                Room room = editPicture.getRowValue();
//                room.setPicturePath(editPicture.getNewValue());
//                updateRoomInDatabase(room.getRoomIdentificationNumber(), "Pictures",editPicture.getNewValue());
//            });
//
//            TableColumn<Room, String> roomStatusColumn = new TableColumn<>("Room Availability");
//            roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
//            ObservableList<String> roomStatus;
//            if (this.role.equals("Admin")) {
//                roomStatus = FXCollections.observableArrayList("available","occupied","cleaning","maintenance");
//            } else {
//                roomStatus = FXCollections.observableArrayList("available","cleaning");
//            }
//            roomStatusColumn.setCellFactory(tc -> new ChoiceBoxTableCell<>(roomStatus));
//            roomStatusColumn.setOnEditCommit(editStatus -> {
//                Room room = editStatus.getRowValue();
//                room.setRoomStatus(editStatus.getNewValue());
//                updateRoomInDatabase(room.getRoomIdentificationNumber(),"Status", editStatus.getNewValue());
//                getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);
//            });
//
//            tableView.getColumns().addAll(roomIDColumn,roomCapacityColumn, roomPricingColumn, roomTypeColumn, roomPictureColumn, roomStatusColumn);
//            for (TableColumn<?,?> column : tableView.getColumns()) {
//                String headerText = column.getText();
//                double headerWidthEstimate = headerText.length() * 10; // rough width per character
//                column.setPrefWidth(headerWidthEstimate + 20); // add padding
//            }
//            ObservableList<Room> roomDataList = FXCollections.observableArrayList();
//
//            TextField roomCapacityInfo = new TextField();
//            checkInputType(roomCapacityInfo,Integer.class);
//            roomCapacityInfo.setPromptText("Enter Room Capacity...");
//
//            TextField roomPricingInfo = new TextField();
//            checkInputType(roomPricingInfo,Double.class);
//            roomPricingInfo.setPromptText("Enter Pricing/night");
//
//            ChoiceBox<String> roomTypeInfo = new ChoiceBox<>();
//            roomTypeInfo.getItems().addAll("Standard", "Deluxe", "Single", "Suite","Enter Room Type...");
//            roomTypeInfo.setValue("Enter Room Type...");
//
//            Button importButton = new Button("Import Image");
//            ImageView roomImageView = new ImageView();
//            roomImageView.setPreserveRatio(true);
//            roomImageView.setFitWidth(200);
//
//            AtomicReference<String> filePath = new AtomicReference<>("");
//            importButton.setOnAction(event -> {
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.setTitle("Choose an Image");
//
//                fileChooser.getExtensionFilters().addAll(
//                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
//                );
//
//                File selectedFile = fileChooser.showOpenDialog(adminPage);
//                if (selectedFile != null) {
//                    try {
//                        String destDir = "Images/Room";
//
//                        Path targetPath = Paths.get(destDir, selectedFile.getName());
//
//                        Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//                        filePath.set(selectedFile.toString());
//                        Image roomImage = new Image("Images/Room/"+filePath.get());
//                        roomImageView.setImage(roomImage);
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            });
//
//            Button submitButton = new Button("Insert Data");
//            submitButton.setOnAction(submitEvent -> {
//                String insertQuery = "Insert into room (Capacity, Pricing, Type, Pictures) Values (?,?,?,?)";
//                if (roomCapacityInfo.getText().isEmpty() ||
//                        roomPricingInfo.getText().isEmpty() ||
//                        roomTypeInfo.getValue().equals("Enter Room Type...") ||
//                        filePath.get().isEmpty()
//                ) {
//                    textPage("No Input Fields Should be Left Empty", "ERROR: Invalid Input",true);
//                } else {
//                    try (Connection conn = DriverManager.getConnection(URL);
//                         PreparedStatement pstmt = conn.prepareStatement(insertQuery);
//                    ) {
//                        int insertCapacity = Integer.valueOf(roomCapacityInfo.getText());
//                        double insertPrice = Double.valueOf(roomPricingInfo.getText());
//                        String insertType = roomTypeInfo.getValue();
//                        String insertPicture = filePath.get();
//
//                        pstmt.setString(1,String.valueOf(insertCapacity));
//                        pstmt.setDouble(2,insertPrice);
//                        pstmt.setString(3,insertType);
//                        pstmt.setString(4,insertPicture);
//                        pstmt.executeUpdate();
//                        int id = roomDataList.getLast().getRoomIdentificationNumber() +1;
//                        roomDataList.add(new Room(id,insertCapacity, insertPrice, insertType, new Image("Images/Room/"+filePath.get()), "available"));
//                        getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);
//                    } catch (SQLException exception){
//                        exception.printStackTrace();
//                    }
//                }
//
//            });
//
//            Button editButton = new Button("Edit Data");
//            editButton.setOnAction(editDataEvent -> {
//                if (tableView.isEditable()) {
//                    tableView.setEditable(false);
//                    editButton.setText("Edit Data");
//                } else {
//                    tableView.setEditable(true);
//                    editButton.setText("Save Edit");
//                }
//            });
//
//            Button deleteButton = new Button("Delete Data");
//            deleteButton.setOnAction(deleteDataEvent -> {
//                Room selectedRoom = tableView.getSelectionModel().getSelectedItem();
//
//                if (selectedRoom == null){
//                    textPage("Please Select a room to Delete", "ERROR: Invalid Input", true);
//                    return;
//                }
//
//                textPage("Are you sure you want to delete this room?", "Confirmation", false, true, confirmed -> {
//                    if (confirmed) {
//                        try (Connection conn = DriverManager.getConnection(URL);
//                             PreparedStatement pstmt = conn.prepareStatement("Delete from room where RoomID = ?")) {
//                            pstmt.setInt(1, selectedRoom.getRoomIdentificationNumber());
//                            pstmt.executeUpdate();
//                            roomDataList.remove(selectedRoom);
//                            tableView.refresh();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            });
//
//            HBox roomDetailQuery = new HBox(10);
//            HBox buttonArea = new HBox(20);
//            if (this.role.equals("Admin")) {
//                roomDetailQuery.getChildren().addAll(roomCapacityInfo, roomPricingInfo, roomTypeInfo, importButton);
//                buttonArea.getChildren().addAll(submitButton,editButton, deleteButton);
//            } else {
//                tableView.setEditable(true);
//                roomIDColumn.setEditable(false);
//                roomCapacityColumn.setEditable(false);
//                roomPricingColumn.setEditable(false);
//                roomTypeColumn.setEditable(false);
//                roomPictureColumn.setEditable(false);
//                roomStatusColumn.setEditable(true);
//            }
//
//            //Available rooms
//            String availableRooms = "SELECT * from room";
//            try (Connection conn = DriverManager.getConnection(URL);
//                 Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery(availableRooms);
//            ) {
//                getRoomStatus(availabilityLabel,cleaningRoomLabel,maintenenceLabel,totalRoomLabel);
//
//                while (rs.next()) {
//                    Image newImage = new Image("file:Images/Room/"+rs.getString("Pictures"));
//                    Room roomData = new Room(rs.getInt("RoomID"),rs.getInt("Capacity"),
//                            rs.getDouble("Pricing"), rs.getString("Type"),
//                            newImage,rs.getString("Status"));
//                    roomDataList.add(roomData);
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//            tableView.setItems(roomDataList);
//            VBox roomManagementPage = new VBox(10, viewAllRooms,allRoomDataArea, roomDetailQuery, buttonArea);
//            roomManagementPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.86));
//            roomManagementPage.prefHeightProperty().bind(adminPage.heightProperty());
//            roomManagementPage.setStyle("-fx-background-color: #FFF5EE");
//            roomManagementPage.setPadding(new Insets(10));
//            switchContent(roomManagementPage, scrollPane);
//        });
//
//        Button reservationManagement = new Button("Reservations");
//        reservationManagement.setOnAction(manageReservation -> {
//            ObservableList<Integer> allRoomIDs = FXCollections.observableArrayList();
//            ObservableList<Integer> allGuestIDs = FXCollections.observableArrayList();
//            ObservableList<String> allPaymentMethods = FXCollections.observableArrayList();
//            try (Connection conn = DriverManager.getConnection(URL);
//                 Statement stmt2 = conn.createStatement();
//                 Statement stmt3 = conn.createStatement();
//                 ResultSet rs2 = stmt2.executeQuery("Select GuestID from guestinfo");
//                 ResultSet rs3 = stmt3.executeQuery("Select PaymentType from paymentstype")
//            ) {
//                while (rs2.next()) {
//                    allGuestIDs.add(rs2.getInt("GuestID"));
//                }
//                while (rs3.next()) {
//                    allPaymentMethods.add(rs3.getString("PaymentType"));
//                }
//
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//
//            VBox allReservationsPage = new VBox(10);
//
//            TableView<Bookings> tableView = new TableView<>();
//
//            TableColumn<Bookings, Integer> bookingIdColumn = new TableColumn<>("Booking ID");
//            bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
//
//            TableColumn<Bookings, Integer> guestIdColumn = new TableColumn<>("Guest ID");
//            guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));
//            guestIdColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allGuestIDs));
//            guestIdColumn.setOnEditCommit(editGuestId -> {
//                Bookings bookings = editGuestId.getRowValue();
//                bookings.setGuestID(editGuestId.getNewValue());
//                updateBookingInDatabase(bookings.getBookingID(), "GuestID", editGuestId.getNewValue());
//            });
//
//            TableColumn<Bookings, Integer> roomIdColumn = new TableColumn<>("Room ID");
//            roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
//            roomIdColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allRoomIDs));
//            roomIdColumn.setOnEditCommit(editRoomId -> {
//                Bookings bookings = editRoomId.getRowValue();
//                bookings.setRoomID(editRoomId.getNewValue());
//                updateBookingInDatabase(bookings.getBookingID(), "RoomID", editRoomId.getNewValue());
//            });
//
//            TableColumn<Bookings, LocalDate> checkInColumn = new TableColumn<>("Check In Date");
//            checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
//            checkInColumn.setCellFactory(tablecell -> new TableCell<>() {
//                private final DatePicker checkInDatePicker = new DatePicker();
//                {
//                    checkInDatePicker.setOnAction(event -> {
//                        Platform.runLater(() -> {
//                            commitEdit(checkInDatePicker.getValue());
//                        });
//                    });
//
//                }
//
//                public void startEdit() {
//                    super.startEdit();
//                    if (!isEmpty()) {
//                        checkInDatePicker.setValue(getItem());
//                        setText(null);
//                        setGraphic(checkInDatePicker);
//                    }
//                }
//
//                public void cancelEdit() {
//                    super.cancelEdit();
//                    LocalDate originalDate = getItem(); // This is the unedited value
//                    checkInDatePicker.setValue(originalDate); // Reset DatePicker UI
//
//                    setText(originalDate != null ? originalDate.format(formatter) : null);
//                    setGraphic(null);
//                }
//
//                public void updateItem(LocalDate date, boolean empty) {
//                    super.updateItem(date, empty);
//
//                    if (empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        if (isEditing()) {
//                            checkInDatePicker.setValue(date);
//                            setText(null);
//                            setGraphic(checkInDatePicker);
//                        } else {
//                            setText(date != null ? date.format(formatter) : null);
//                            setGraphic(null);
//                        }
//                    }
//                }
//
//                public void commitEdit(LocalDate newDate) {
//                    super.commitEdit(newDate);
//                    // Always access row item safely
//                    Bookings booking = getTableRow().getItem();
//                    if (booking != null) {
//                        LocalDate checkOutDate = booking.getCheckOutDate();
//                        if (newDate.isAfter(checkOutDate) || newDate.isBefore(LocalDate.now())) {
//                            textPage(
//                                    "Possible Errors: \n" +
//                                            "- New Check In Date is After Check Out Date\n" +
//                                            "- New Check In Date is Before today",
//                                    "ERROR: Invalid Date", true);
//                            cancelEdit();
//                        }
//                        else {
//                            booking.setCheckInDate(newDate);
//                            updateBookingInDatabase(booking.bookingID, "CheckInDate", newDate);
//                        }
//                    }
//                }
//            });
//
//            TableColumn<Bookings, LocalDate> checkOutColumn = new TableColumn<>("Check Out Column");
//            checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
//            checkOutColumn.setCellFactory(tablecell -> new TableCell<>() {
//                private final DatePicker checkOutDatePicker = new DatePicker();
//                {
//                    checkOutDatePicker.setOnAction(event -> {
//                        Platform.runLater(() -> {
//                            commitEdit(checkOutDatePicker.getValue());
//                        });
//                    });
//                }
//
//                public void startEdit() {
//                    super.startEdit();
//                    if (!isEmpty()) {
//                        checkOutDatePicker.setValue(getItem());
//                        setText(null);
//                        setGraphic(checkOutDatePicker);
//                    }
//                }
//
//                public void cancelEdit() {
//                    super.cancelEdit();
//                    LocalDate originalDate = getItem(); // This is the unedited value
//                    checkOutDatePicker.setValue(originalDate); // Reset DatePicker UI
//
//                    setText(originalDate != null ? originalDate.format(formatter) : null);
//                    setGraphic(null);
//                }
//
//                public void updateItem(LocalDate date, boolean empty) {
//                    super.updateItem(date, empty);
//
//                    if (empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        if (isEditing()) {
//                            checkOutDatePicker.setValue(date);
//                            setText(null);
//                            setGraphic(checkOutDatePicker);
//                        } else {
//                            setText(date != null ? date.format(formatter) : null);
//                            setGraphic(null);
//                        }
//                    }
//                }
//
//                public void commitEdit(LocalDate newDate) {
//                    super.commitEdit(newDate);
//                    // Always access row item safely
//                    Bookings booking = getTableRow().getItem();
//                    if (booking != null) {
//                        LocalDate checkInDate = booking.getCheckInDate();
//                        if (newDate.isBefore(checkInDate) || newDate.isBefore(LocalDate.now())) {
//                            textPage(
//                                    "Possible Errors: \n" +
//                                            "- New Check Out Date is Before Check In Date\n" +
//                                            "- New Check Out Date is Before today",
//                                    "ERROR: Invalid Date", true);
//                            cancelEdit();
//                        }
//                        else {
//                            booking.setCheckOutDate(newDate);
//                            updateBookingInDatabase(booking.bookingID, "CheckOutDate", newDate);
//                        }
//                    }
//                }
//            });
//
//            TableColumn<Bookings, Double> totalAmountColumn = new TableColumn<>("Payment Amount");
//            totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//            totalAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
//            totalAmountColumn.setOnEditCommit(editTotalAmount -> {
//                Bookings bookings = editTotalAmount.getRowValue();
//                bookings.setTotalAmount(editTotalAmount.getNewValue());
//                updateBookingInDatabase(bookings.getBookingID(), "TotalAmount", editTotalAmount.getNewValue());
//            });
//
//            TableColumn<Bookings, String> paymentTypeColumn = new TableColumn<>("Payment Type");
//            paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
//            paymentTypeColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allPaymentMethods));
//            paymentTypeColumn.setOnEditCommit(editPaymentType -> {
//                Bookings bookings = editPaymentType.getRowValue();
//                bookings.setPaymentType(editPaymentType.getNewValue());
//                updateBookingInDatabase(bookings.getBookingID(), "PaymentType", editPaymentType.getNewValue());
//            });
//
//            TableColumn<Bookings, LocalDate> bookingDateColumn = new TableColumn<>("Booking Date");
//            bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
//
//            TableColumn<Bookings, String> statusColumn = new TableColumn<>("Status");
//            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
//            ObservableList<String> statusType = FXCollections.observableArrayList("Success", "Pending", "Canceled");
//            statusColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(statusType));
//            statusColumn.setOnEditCommit(editStatusType -> {
//                Bookings bookings = editStatusType.getRowValue();
//                String newStatus = editStatusType.getNewValue();
//                String oldStatus = editStatusType.getOldValue();
//                textPage("Are You Sure you want to edit the Status?", "Confirmation", false, true, confirmed -> {
//                    if (confirmed) {
//                        bookings.setStatus(newStatus);
//                        updateBookingInDatabase(bookings.getBookingID(), "Status", newStatus);
//                        editStatusType.getTableView().refresh();
//                    } else {
//                        bookings.setStatus(oldStatus);
//                        editStatusType.getTableView().refresh();
//                    }
//                });
//            });
//
//            tableView.getColumns().addAll(bookingIdColumn, guestIdColumn, roomIdColumn, checkInColumn, checkOutColumn, totalAmountColumn, paymentTypeColumn, bookingDateColumn, statusColumn);
//            for (TableColumn<?,?> column : tableView.getColumns()) {
//                String headerText = column.getText();
//                double headerWidthEstimate = headerText.length() * 10; // rough width per character
//                column.setPrefWidth(headerWidthEstimate + 20); // add padding
//            }
//            tableView.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.7));
//            tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.7));
//
//            //input boxes for inserting data
//            Label insertGuestIdLabel = new Label("Insert Guest ID");
//            ChoiceBox<Integer> insertGuestID = new ChoiceBox<>(allGuestIDs);
//
//            Label insertRoomIdLabel = new Label("Insert Room ID: ");
//            ChoiceBox<Integer> insertRoomID = new ChoiceBox<>(allRoomIDs);
//
//            Label insertPaymentMethodLabel = new Label("Insert Payment Method: ");
//            ChoiceBox<String> insertPaymentMethod = new ChoiceBox<>(allPaymentMethods);
//
//            HBox inputBoxes = new HBox(10, insertGuestIdLabel ,insertGuestID, insertRoomIdLabel, insertRoomID, insertPaymentMethodLabel, insertPaymentMethod);
//
//            Label insertCheckInDateLabel = new Label("Pick Check In Date: ");
//            DatePicker insertCheckInDate = new DatePicker(LocalDate.now());
//            insertCheckInDate.getEditor().setDisable(true);
//            insertCheckInDate.getEditor().setOpacity(1);
//
//            Label insertCheckOutDateLabel = new Label("Pick Check Out Date: ");
//            DatePicker insertCheckOutDate = new DatePicker(LocalDate.now().plusDays(1));
//            insertCheckOutDate.getEditor().setOpacity(1);
//            insertCheckOutDate.getEditor().setDisable(true);
//
//            HBox inputDatesBox = new HBox(10, insertCheckInDateLabel, insertCheckInDate, insertCheckOutDateLabel, insertCheckOutDate);
//            HBox.setMargin(insertCheckInDate, new Insets(0, 50, 0, 0));
//            //end of input boxes
//
//            insertCheckInDate.valueProperty().addListener((obs, oldValue, newValue) -> {
//                allRoomIDs.clear();
//                String sql = checkAvailableRooms(insertCheckInDate.getValue(), insertCheckOutDate.getValue(), null);
//
//                if (sql == null) {
//                    insertCheckInDate.setValue(oldValue);
//                    return;
//                }
//
//                try (Connection conn = DriverManager.getConnection(URL);
//                     Statement stmt = conn.createStatement();
//                     ResultSet rs = stmt.executeQuery(sql)
//                ) {
//                    while (rs.next()) {
//                        allRoomIDs.add(rs.getInt("RoomID"));
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            });
//
//            insertCheckOutDate.valueProperty().addListener((obs, oldValue, newValue) -> {
//                String sql = checkAvailableRooms(insertCheckInDate.getValue(), insertCheckOutDate.getValue(), null);
//
//                if (sql == null) {
//                    insertCheckOutDate.setValue(oldValue);
//                    return;
//                }
//
//                allRoomIDs.clear();
//                try (Connection conn = DriverManager.getConnection(URL);
//                     Statement stmt = conn.createStatement();
//                     ResultSet rs = stmt.executeQuery(sql)
//                ) {
//                    while (rs.next()) {
//                        allRoomIDs.add(rs.getInt("RoomID"));
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            });
//
//            //Button Area
//            Button insertDataButton = new Button("Add Reservations");
//            insertDataButton.setOnAction(insertDataEvent -> {
//                Integer roomId = insertRoomID.getValue();
//                Double totalAmount =0.0;
//
//                LocalDate checkInDate = insertCheckInDate.getValue();
//                LocalDate checkOutDate = insertCheckOutDate.getValue();
//
//                if (ChronoUnit.DAYS.between(checkInDate,checkOutDate) < 1){
//                    textPage("Check In Date Must Be Before Check Out Date","ERROR: Invalid Input",true);
//                    return;
//                } else if (LocalDate.now().isAfter(checkInDate)) {
//                    textPage("Check In Date Must Be After Today's Date", "ERROR: Invalid Input",true);
//                    return;
//                }
//
//                if (insertGuestID.getValue()==null || insertRoomID.getValue()==null || insertPaymentMethod.getValue() == null) {
//                    textPage("No Input Fields Should be Left Empty", "ERROR: Invalid Input", true);
//                    return;
//                }
//
//                try (Connection conn = DriverManager.getConnection(URL);
//                     PreparedStatement pstmt = conn.prepareStatement("Select Pricing from room where RoomID = ?")
//                ) {
//                    pstmt.setInt(1, roomId);
//                    ResultSet rs = pstmt.executeQuery();
//
//                    if (rs.next()) {
//                        totalAmount = ChronoUnit.DAYS.between(checkInDate, checkOutDate) * rs.getDouble("Pricing");
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//
//
//                Bookings newReservation = new Bookings(
//                        this.bookingDataList.getLast().getBookingID()+1,
//                        insertGuestID.getValue(),
//                        insertRoomID.getValue(),
//                        insertCheckInDate.getValue(),
//                        insertCheckOutDate.getValue(),
//                        totalAmount,
//                        insertPaymentMethod.getValue(),
//                        LocalDate.now(),
//                        "Success"
//                );
//
//                try (Connection conn = DriverManager.getConnection(URL);
//                     PreparedStatement pstmt = conn.prepareStatement("Insert into booking (GuestID, RoomID, CheckInDate, CheckOutDate, TotalAmount, PaymentType , BookingDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//                ) {
//                    pstmt.setInt(1, newReservation.getGuestID());
//                    pstmt.setInt(2, newReservation.getRoomID());
//                    pstmt.setDate(3, Date.valueOf(newReservation.getCheckInDate()));
//                    pstmt.setDate(4, Date.valueOf(newReservation.getCheckOutDate()));
//                    pstmt.setDouble(5, newReservation.getTotalAmount());
//                    pstmt.setString(6, newReservation.getPaymentType());
//                    pstmt.setDate(7, Date.valueOf(newReservation.getBookingDate()));
//                    pstmt.setString(8, newReservation.getStatus());
//                    pstmt.executeUpdate();
//
//                    updateRoomInDatabase(newReservation.getRoomID(), "Status", "occupied");
//                    this.bookingDataList.add(newReservation);
//                    tableView.refresh();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            Button editDataButton = new Button("Edit Reservations");
//            editDataButton.setOnAction(editReservationEvent -> {
//                if (tableView.isEditable()) {
//                    editDataButton.setText("Edit Reservations");
//                    tableView.setEditable(false);
//                } else {
//                    editDataButton.setText("Save Edits");
//                    tableView.setEditable(true);
//                }
//            });
//
//            Button deleteDataButton = new Button("Delete Reservations");
//            deleteDataButton.setOnAction(deleteReservation -> {
//                Bookings bookings = tableView.getSelectionModel().getSelectedItem();
//
//                if (bookings == null){
//                    textPage("Please Select a reservation to Delete", "ERROR: Invalid Input", true);
//                    return;
//                }
//
//                textPage("Are you sure you want to delete this Reservation?", "Confirmation", false, true, confirmed -> {
//                    if (confirmed) {
//                        try (Connection conn = DriverManager.getConnection(URL);
//                             PreparedStatement pstmt = conn.prepareStatement("Delete from booking where BookingID = ?")) {
//                            pstmt.setInt(1, bookings.getBookingID());
//                            pstmt.executeUpdate();
//                            this.bookingDataList.remove(bookings);
//                            tableView.refresh();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            });
//
//            HBox buttonArea = new HBox(20, insertDataButton, editDataButton, deleteDataButton);
//            //End of Button Area
//
//            tableView.setItems(this.bookingDataList);
//            allReservationsPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.84));
//            allReservationsPage.prefHeightProperty().bind(adminPage.heightProperty());
//            allReservationsPage.getChildren().addAll(tableView, inputDatesBox, inputBoxes, buttonArea);
//            allReservationsPage.setPadding(new Insets(20));
//            allReservationsPage.setStyle("-fx-background-color: #FFF5EE");
//            switchContent(allReservationsPage, scrollPane);
//        });
//
//        Button staffManagement = new Button("Staff Management");
//        staffManagement.setOnAction(manageStaff -> {
//            ObservableList<Staff> staffDataList = FXCollections.observableArrayList();
//            try (Connection conn = DriverManager.getConnection(URL);
//                 Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery("Select * from Admin");
//            ){
//                while (rs.next()) {
//                    staffDataList.add(new Staff(
//                            rs.getInt("AdminID"),
//                            rs.getString("Username"),
//                            rs.getString("ICNum"),
//                            rs.getString("Password"),
//                            rs.getString("Role"),
//                            rs.getString("Email"),
//                            rs.getString("PhoneNumber")
//                    ));
//                }
//
//
//            } catch (SQLException e){
//                e.printStackTrace();
//            }
//
//            VBox allStaffPage = new VBox(10);
//
//            TableView<Staff> tableView = new TableView<>();
//
//            TableColumn<Staff, Integer> staffIDColumn = new TableColumn<>("Staff ID");
//            staffIDColumn.setCellValueFactory(new PropertyValueFactory<>("staffID"));
//
//            TableColumn<Staff, String> staffNameColumn = new TableColumn<>("Name");
//            staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
//            staffNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//            staffNameColumn.setOnEditCommit(editName -> {
//                Staff staff = editName.getRowValue();
//
//                staff.setStaffUsername(editName.getNewValue());
//                updateStaffInDatabase(staff.getStaffID(), "Username", editName.getNewValue());
//            });
//
//            TableColumn<Staff, String> staffICColumn = new TableColumn<>("IC Number");
//            staffICColumn.setCellValueFactory(new PropertyValueFactory<>("staffIC"));
//            staffICColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//            staffICColumn.setOnEditCommit(editIC -> {
//                Staff staff = editIC.getRowValue();
//                staff.setStaffIC(editIC.getNewValue());
//                updateStaffInDatabase(staff.getStaffID(), "ICNum", editIC.getNewValue());
//            });
//
//
//            TableColumn<Staff, String> staffPasswordColumn = new TableColumn<>("Password");
//            staffPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("staffPassword"));
//            staffICColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//            staffICColumn.setOnEditCommit(editPassword -> {
//                Staff staff = editPassword.getRowValue();
//
//                staff.setStaffPassword(editPassword.getNewValue());
//                updateStaffInDatabase(staff.getStaffID(), "Password", editPassword.getNewValue());
//            });
//
//            TableColumn<Staff, String> staffRoleColumn = new TableColumn<>("Role");
//            staffRoleColumn.setCellValueFactory(new PropertyValueFactory<>("staffRole"));
//            staffRoleColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn("Admin", "Cleaner", "Receptionist"));
//            staffRoleColumn.setOnEditCommit(editRole -> {
//                Staff staff = editRole.getRowValue();
//
//                staff.setStaffRole(editRole.getNewValue());
//                updateStaffInDatabase(staff.getStaffID(), "Role", editRole.getNewValue());
//            });
//
//            TableColumn<Staff, String> staffEmailColumn = new TableColumn<>("Email");
//            staffEmailColumn.setCellValueFactory(new PropertyValueFactory<>("staffEmail"));
//            staffEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//            staffEmailColumn.setOnEditCommit(editEmail -> {
//                Staff staff = editEmail.getRowValue();
//
//                staff.setStaffPassword(editEmail.getNewValue());
//                updateStaffInDatabase(staff.getStaffID(), "Email", editEmail.getNewValue());
//            });
//
//            TableColumn<Staff, String> staffPhoneNumberColumn = new TableColumn<>("Phone Number");
//            staffPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("staffPhoneNumber"));
//            staffPhoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//            staffPhoneNumberColumn.setOnEditCommit(editPhoneNumber -> {
//                Staff staff = editPhoneNumber.getRowValue();
//
//                staff.setStaffPassword(editPhoneNumber.getNewValue());
//                updateStaffInDatabase(staff.getStaffID(), "PhoneNumber", editPhoneNumber.getNewValue());
//            });
//
//            tableView.setItems(staffDataList);
//            //without password
//            tableView.getColumns().addAll(staffIDColumn, staffNameColumn, staffICColumn, staffRoleColumn, staffEmailColumn, staffPhoneNumberColumn);
//            //with password
////            tableView.getColumns().addAll(staffIDColumn, staffNameColumn, staffICColumn, staffPasswordColumn, staffRoleColumn, staffEmailColumn, staffPhoneNumberColumn);
//            for (TableColumn<?,?> column : tableView.getColumns()) {
//                String headerText = column.getText();
//                double headerWidthEstimate = headerText.length() * 20; // rough width per character
//                column.setPrefWidth(headerWidthEstimate + 20); // add padding
//            }
//            tableView.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.7));
//            tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.7));
//
//            TextField nameField = new TextField();
//            nameField.setPromptText("Enter Name...");
//
//            TextField ICField = new TextField();
//            ICField.setPromptText("Enter IC Number...");
//
//            TextField passwordField = new PasswordField();
//            passwordField.setPromptText("Enter Password...");
//
//            TextField emailField = new TextField();
//            emailField.setPromptText("Enter Email...");
//
//            TextField phoneNumberField = new TextField();
//            phoneNumberField.setPromptText("Enter Phone Number...");
//
//            ChoiceBox<String> roleBox = new ChoiceBox<>();
//            roleBox.getItems().addAll("Admin", "Receptionist", "Cleaner", "Select Role...");
//            roleBox.setValue("Select Role...");
//
//            Button addStaffButton = new Button("Add New Staff");
//            addStaffButton.setOnAction(addStaff -> {
//                if (!(nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || roleBox.getItems().equals("Select Role...") || passwordField.getText().isEmpty())) {
//                    String sqlQuery = "insert into Admin (Username, ICNum, Password, Email, PhoneNumber, Role) VALUES (?,?,?,?,?,?)";
//                    try (Connection conn = DriverManager.getConnection(URL);
//                         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)
//                    ) {
//                        Staff newStaff = new Staff(
//                                staffDataList.getLast().getStaffID()+1,
//                                nameField.getText(),
//                                ICField.getText(),
//                                passwordField.getText(),
//                                roleBox.getValue(),
//                                emailField.getText(),
//                                phoneNumberField.getText()
//                        );
//
//
//                        pstmt.setString(1, newStaff.getStaffUsername());
//                        pstmt.setString(2, newStaff.getStaffIC());
//                        pstmt.setString(3, newStaff.getStaffPassword());
//                        pstmt.setString(4, newStaff.getStaffEmail());
//                        pstmt.setString(5, newStaff.getStaffPhoneNumber());
//                        pstmt.setString(6, newStaff.getStaffRole());
//                        pstmt.executeUpdate();
//
//                        staffDataList.add(newStaff);
//
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    textPage("No Input Fields Should be Left Empty", "ERROR: Invalid Input", true);
//                }
//            });
//
//            Button removeStaffButton = new Button("Remove Staff");
//            removeStaffButton.setOnAction(removeStaff -> {
//                Staff staff = tableView.getSelectionModel().getSelectedItem();
//
//                if (staff == null) {
//                    textPage("Please Select a Staff to Remove", "ERROR: Invalid Input", true);
//                } else {
//
//                    textPage("Are you sure you want to remove this Staff Account?", "Confirmation", false, true, confirmed -> {
//                        if (confirmed) {
//                            try (Connection conn = DriverManager.getConnection(URL);
//                                 PreparedStatement pstmt = conn.prepareStatement("Delete from Admin where AdminID = ?")) {
//                                pstmt.setInt(1, staff.getStaffID());
//                                pstmt.executeUpdate();
//                                staffDataList.remove(staff);
//                                tableView.refresh();
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            });
//
//            Button editStaffButton = new Button("Edit Staff");
//            editStaffButton.setOnAction(editStaff -> {
//                if (tableView.isEditable()){
//                    editStaffButton.setText("Save Edit");
//                    tableView.setEditable(false);
//                } else  {
//                    editStaffButton.setText("Edit Staff");
//                    tableView.setEditable(true);
//                }
//            });
//
//            HBox inputFields = new HBox(10, nameField, ICField, passwordField, emailField, phoneNumberField, roleBox);
//
//            for (Node node : inputFields.getChildren()) {
//                if (node instanceof TextField) {
//                    ((TextField) node).prefWidthProperty().bind(tableView.widthProperty().divide(6));
//                    ((TextField) node).setPrefHeight(30);
//                }
//            }
//
//            HBox buttonArea = new HBox(10, addStaffButton, editStaffButton, removeStaffButton);
//
//            allStaffPage.getChildren().addAll(tableView, inputFields, buttonArea);
//            allStaffPage.setPadding(new Insets(20));
//            allStaffPage.setStyle("-fx-background-color: #FFF5EE");
//            allStaffPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.87));
//            allStaffPage.prefHeightProperty().bind(adminPage.heightProperty());
//            switchContent(allStaffPage, scrollPane);
//        });
//
//        Button exitButton = new Button("Exit Button");
//        exitButton.setOnAction(exitAction -> {
//            exit(this.homePage, adminPage);
//        });
//
//        Region spacer = new Region();
//        VBox.setVgrow(spacer, Priority.ALWAYS);
//        if (this.role.equals("Admin")) {
//            vBox.getChildren().addAll(roleLabel, reportGeneration, roomManagement, reservationManagement, staffManagement, spacer, exitButton);
//        } else if (this.role.equals("Receptionist")) {
//            vBox.getChildren().addAll(roleLabel, roomManagement, reservationManagement, spacer, exitButton);
//        } else {
//            vBox.getChildren().addAll(roleLabel, roomManagement, spacer, exitButton);
//        }
//
//        BorderPane borderPane = new BorderPane();
//        borderPane.setCenter(scrollPane);
//        scrollPane.setContent(stackPane);
//        borderPane.setLeft(vBox);
//        //end of main admin page
//
//        Scene scene = new Scene(borderPane,1100,500);
//        scene.getStylesheets().add("file:Style.css");
//        adminPage.setTitle("Admin page");
//        adminPage.setScene(scene);
//        adminPage.show();
//
//
//    }
}
