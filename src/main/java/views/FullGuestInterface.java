package views;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static app.MainApp.getHomePage;
import static views.ExitBoxView.showExitBox;
import static views.RoomSelectionView.showAvailableRooms;
import static views.UserInfoColumnView.showUserInfoColumn;

public class FullGuestInterface {

    public static void showFullGuestUI(Integer userID, Image profilepic, String lastName){
        Stage stage = new Stage();
        Stage homePage = getHomePage();

        BorderPane borderPane2 = new BorderPane();

        //Shows available rooms and Date filters
        BorderPane borderPane = showAvailableRooms(stage,userID,profilepic);

        //show exitBox
        VBox exitBox = showExitBox(stage);
        exitBox.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));

        //user info side page
        VBox userInfo = showUserInfoColumn(profilepic, userID, stage, lastName);

        //main page
        borderPane.setBottom(exitBox);
        borderPane2.setLeft(userInfo);
        borderPane2.setCenter(borderPane);
        Scene scene = new Scene(borderPane2);
        stage.setTitle("Rooms");
        stage.setScene(scene);

        //Maximize the stage
        if (!stage.isMaximized()){
            stage.setMaximized(true);
        }

        scene.getStylesheets().add("file:Style.css");
        homePage.close();
        stage.showAndWait();
    }
}
