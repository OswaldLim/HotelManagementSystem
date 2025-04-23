package views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Guest;

import static views.GuestTableView.getGuestTableView;

public class GuestManagementPageView {
    public static VBox getGuestManagementPageView(Stage adminPage){
        VBox allGuestPage = new VBox(10);

        Label viewAllGuest = new Label("View All Guest Details");

        TableView<Guest> tableView = getGuestTableView();
        tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.9));

        allGuestPage.getChildren().addAll(viewAllGuest, tableView);
        allGuestPage.setPadding(new Insets(20));
        allGuestPage.setStyle("-fx-background-color: #FDFCE1");
        allGuestPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.87));
        allGuestPage.prefHeightProperty().bind(adminPage.heightProperty());

        return allGuestPage;
    }
}
