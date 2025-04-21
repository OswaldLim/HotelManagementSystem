package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static controllers.SceneController.exit;

public class ExitBoxView {
    public static VBox showExitBox(Stage homePage, Stage stage){
        Button exitButton = new Button("Exit");

        exitButton.setOnAction(e -> exit(homePage, stage));

        VBox exitBox = new VBox(exitButton);
        exitBox.setStyle(
                "-fx-background-color: #FFF5EE;" +
                        "-fx-border-color: #8B5A2B;" +
                        "-fx-border-width: 10px;" +
                        "-fx-padding: 10px;"
        );
        exitBox.setPadding(new Insets(50));
        exitBox.setAlignment(Pos.BOTTOM_RIGHT);

        return exitBox;
    }
}
