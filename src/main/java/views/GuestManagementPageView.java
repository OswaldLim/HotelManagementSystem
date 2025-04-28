package views;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Guest;

import static services.GuestService.deleteGuest;
import static services.GuestService.getAllGuestData;
import static services.LoginService.getRole;
import static services.RoomService.deleteRooms;
import static utils.TableUtils.toggleTableEditing;
import static views.GuestTableView.getGuestTableView;

public class GuestManagementPageView {
    //A scrollPane that stores a read only table to view all guest details
    public static VBox getGuestManagementPageView(Stage adminPage){
        ObservableList<Guest> allGuestDataList = getAllGuestData();

        VBox allGuestPage = new VBox(10);
        Label viewAllGuest = new Label("View All Guest Details");
        TableView<Guest> tableView = getGuestTableView();
        tableView.setItems(allGuestDataList);
        tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.9));

        Button editButton = new Button("Edit Data");
        editButton.setOnAction(editDataEvent -> {
            toggleTableEditing(tableView, editButton);
        });

        //deletes data from database
        Button deleteButton = new Button("Delete Data");
        deleteButton.setOnAction(deleteDataEvent -> {
            deleteGuest(tableView, allGuestDataList);
        });


        //only admins can edit and delete data
        if (getRole().equals("Admin")){
            HBox buttonArea = new HBox(10, editButton, deleteButton);
            allGuestPage.getChildren().addAll(viewAllGuest, tableView, buttonArea);
        } else if(getRole().equals("Receptionist")) { //receptionist can only edit guest data
            HBox buttonArea = new HBox(10, editButton);
            allGuestPage.getChildren().addAll(viewAllGuest, tableView, buttonArea);
        } else {//cleaners can only view
            allGuestPage.getChildren().addAll(viewAllGuest, tableView);
        }

        allGuestPage.setPadding(new Insets(20));
        allGuestPage.setStyle("-fx-background-color: #FDFCE1");
        allGuestPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.87));
        allGuestPage.prefHeightProperty().bind(adminPage.heightProperty());

        return allGuestPage;
    }
}
