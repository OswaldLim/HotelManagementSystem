package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static app.MainApp.getHomePage;
import static controllers.RoomStatusController.getPieChartData;
import static controllers.SceneController.exit;
import static controllers.SceneController.switchContent;
import static views.GuestManagementPageView.getGuestManagementPageView;
import static views.ReportGenerationView.generateReportView;
import static views.ReservationPageView.getReservationPageView;
import static views.RoomManagementPageView.generateRoomManagementPage;
import static views.StaffManagementPageView.getStaffManagementPageView;

public class MainAdminView {
    public static void showAdminUI(String role){
        Stage homePage = getHomePage();

        //main admin page
        Stage adminPage = new Stage();
        VBox vBox = new VBox(10);

        Label roleLabel = new Label("Role: "+role);

        vBox.setPadding(new Insets(15));
        ScrollPane scrollPane = new ScrollPane();

        //Waiting page when no buttons are pressed
        Image image = new Image("file:Images/System Logo/LCHLOGO.png");
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(adminPage.widthProperty().multiply(0.85));
        imageView.setPreserveRatio(true);

        //Stack Pane to stack the image below the scrollPane as background
        StackPane stackPane = new StackPane(imageView, scrollPane);
        stackPane.prefHeightProperty().bind(adminPage.heightProperty());
        stackPane.setStyle("-fx-background-color: #FDFCE1");

        //Button area to generate all Admin related reports
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

        Button guestManagement = new Button("Guest Management");
        guestManagement.setOnAction(manageGuest -> {
            VBox allGuestPage = getGuestManagementPageView(adminPage);
            switchContent(allGuestPage, scrollPane);
        });

        //Button to exit back to login page
        Button exitButton = new Button("Exit Button");

        exitButton.setOnAction(exitAction -> {
            exit(homePage, adminPage);
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        //Buttons or accessible tables are assigned based on roles
        if (role.equals("Admin")) {
            vBox.getChildren().addAll(roleLabel, reportGeneration, roomManagement, reservationManagement, staffManagement, guestManagement, spacer, exitButton);
        } else if (role.equals("Receptionist")) {
            vBox.getChildren().addAll(roleLabel, roomManagement, reservationManagement, guestManagement, spacer, exitButton);
        } else {
            vBox.getChildren().addAll(roleLabel, roomManagement, guestManagement, spacer, exitButton);
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
}
