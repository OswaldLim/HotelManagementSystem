package views;

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
        Stage homePage = getHomePage();
        Stage stage = new Stage();

        BorderPane borderPane2 = new BorderPane();

        BorderPane borderPane = showAvailableRooms(stage,userID,profilepic);


        VBox exitBox = showExitBox(stage);
        exitBox.prefWidthProperty().bind(stage.widthProperty().multiply(0.3));
        //exitbox

        //user info side page
        VBox userInfo = showUserInfoColumn(profilepic, userID, stage, lastName);
        //User Info Column


        //main page
        borderPane.setBottom(exitBox);
        borderPane2.setLeft(userInfo);
        borderPane2.setCenter(borderPane);
        Scene scene = new Scene(borderPane2, 1300, 700);
        stage.setTitle("Rooms");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        homePage.close();
        stage.showAndWait();
    }
}
