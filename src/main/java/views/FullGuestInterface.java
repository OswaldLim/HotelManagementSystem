package views;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static views.ExitBoxView.showExitBox;
import static views.RoomSelectionView.showAvailableRooms;
import static views.UserInfoColumnView.showUserInfoColumn;

public class FullGuestInterface {

    public static void showFullGuestUI(Integer userID, Image profilepic, Stage homePage){
        Stage stage = new Stage();

        BorderPane borderPane2 = new BorderPane();

        BorderPane borderPane = showAvailableRooms(stage,userID,profilepic,homePage);


        VBox exitBox = showExitBox(homePage, stage);
        //exitbox

        //user info side page
        VBox userInfo = showUserInfoColumn(profilepic, userID, stage);
        //User Info Column


        //main page
        borderPane.setBottom(exitBox);
        borderPane2.setLeft(userInfo);
        borderPane2.setCenter(borderPane);
        Scene scene = new Scene(borderPane2, 800, 500);
        stage.setTitle("Rooms");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        homePage.close();
        stage.showAndWait();
    }
}
