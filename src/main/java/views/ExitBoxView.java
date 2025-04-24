package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static app.MainApp.getHomePage;
import static controllers.SceneController.exit;

public class ExitBoxView {
    //Used to create an exit box in the full Guest UI
    public static VBox showExitBox(Stage stage){
        Stage homePage = getHomePage();
        Button exitButton = new Button("Exit");

        //Exit button to return to homPage
        exitButton.setOnAction(e -> exit(homePage, stage));

        VBox exitBox = new VBox(exitButton);
        exitBox.setStyle(
                "-fx-background-color: #FDFCE1;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 10px;" +
                        "-fx-padding: 10px;"
        );
        exitBox.setPadding(new Insets(50));
        exitBox.setAlignment(Pos.BOTTOM_RIGHT);

        return exitBox;
    }
}
