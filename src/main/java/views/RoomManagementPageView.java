package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Room;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static services.RoomService.*;
import static utils.InputUtils.checkInputType;
import static utils.TableUtils.toggleTableEditing;
import static views.ChangeProfileView.showChangeImageStage;
import static views.LogoView.generateLogo;
import static views.RoomDashboard.generateRoomDashboard;
import static views.RoomDashboard.labelList;
import static views.RoomTableView.generateRoomTable;

public class RoomManagementPageView {
    public static VBox generateRoomManagementPage(PieChart pieChart, String role, Stage adminPage) {
        //view all rooms table layout
        VBox roomPanes = new VBox(generateRoomDashboard(), pieChart);

        //show labels
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(300, 300);
        pieChart.setMinSize(300, 300);
        pieChart.setMaxSize(300, 300);

        Label viewAllRooms = new Label("View All Rooms");

        //list to store all available room status
        ObservableList<String> roomStatus;
        //Room status available for editing differs based on role
        if (role.equals("Admin")) {
            roomStatus = FXCollections.observableArrayList("available","occupied","cleaning","maintenance");
        } else {
            roomStatus = FXCollections.observableArrayList("available","cleaning");
        }

        //get the tableview for the Room Data
        TableView<Room> tableView = generateRoomTable(pieChart,roomStatus,labelList(), role);

        HBox allRoomDataArea = new HBox(10, tableView, roomPanes);
        ObservableList<Room> roomDataList = FXCollections.observableArrayList();

        //Get all rooms available, the room status counts, and the pie chart
        getAllRooms(roomDataList,labelList(),pieChart);

        //Input fields
        TextField roomCapacityInfo = new TextField();
        checkInputType(roomCapacityInfo,Integer.class);
        roomCapacityInfo.setPromptText("Enter Room Capacity...");

        TextField roomPricingInfo = new TextField();
        checkInputType(roomPricingInfo,Double.class);
        roomPricingInfo.setPromptText("Enter Pricing/night");

        ChoiceBox<String> roomTypeInfo = new ChoiceBox<>();
        roomTypeInfo.getItems().addAll("Standard", "Deluxe", "Single", "Suite","Enter Room Type...");
        roomTypeInfo.setValue("Enter Room Type...");

        //button to import image
        Button importButton = new Button("Import Image");

        AtomicReference<String> filePath = new AtomicReference<>(""); // to save the filepath of the chosen image
        AtomicBoolean change = new AtomicBoolean(false);//to see if an image has already been chosen
        importButton.setOnAction(event -> {
            if (change.get() == false){
                filePath.set(showChangeImageStage(null, null, "Import"));
            } else {
                filePath.set(showChangeImageStage(new ImageView(new Image("file:Images/Room/"+filePath.get())), null, "Import"));
            }
            change.set(true);
        });

        //insert data into database
        Button submitButton = new Button("Insert Data");
        submitButton.setOnAction(submitEvent -> {
            insertNewRooms(roomCapacityInfo, roomPricingInfo, roomTypeInfo, filePath, roomDataList, labelList(), pieChart);
            filePath.set("");
            change.set(false);
        });

        //allows editing of the tableview
        Button editButton = new Button("Edit Data");
        editButton.setOnAction(editDataEvent -> {
            toggleTableEditing(tableView, editButton);
        });

        //deletes data from database
        Button deleteButton = new Button("Delete Data");
        deleteButton.setOnAction(deleteDataEvent -> {
            deleteRooms(tableView, roomDataList);
        });

        HBox roomDetailQuery = new HBox(10);
        HBox buttonArea = new HBox(20);

        // only add all the buttons and input fields if the user is an admin
        if (role.equals("Admin")) {
            roomDetailQuery.getChildren().addAll(roomCapacityInfo, roomPricingInfo, roomTypeInfo, importButton);
            buttonArea.getChildren().addAll(submitButton,editButton, deleteButton);
        }

        VBox inputFieldsAndButtons = new VBox(10, roomDetailQuery, buttonArea);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bottomArea = new HBox(10, inputFieldsAndButtons, spacer, generateLogo());
        //make sure the logo is not to far left
        bottomArea.maxWidthProperty().bind(allRoomDataArea.widthProperty());

        tableView.setItems(roomDataList);
        VBox roomManagementPage = new VBox(10, viewAllRooms,allRoomDataArea, bottomArea);
        roomManagementPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.86));
        roomManagementPage.prefHeightProperty().bind(adminPage.heightProperty());
        roomManagementPage.setStyle("-fx-background-color: #FDFCE1");
        roomManagementPage.setPadding(new Insets(10));
        return roomManagementPage;
    }
}
