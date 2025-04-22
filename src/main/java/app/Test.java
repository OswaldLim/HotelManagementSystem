package app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage stage) {
        HBox hbox = new HBox();

        Rectangle left = new Rectangle(100, 100, Color.CORNFLOWERBLUE);
        Rectangle right = new Rectangle(100, 100, Color.SALMON);

        Region spacer = new Region();
        spacer.setStyle("-fx-background-color: lightgray;");
        spacer.setMinWidth(0);
        spacer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        spacer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        hbox.getChildren().addAll(left, spacer, right);

        BorderPane root = new BorderPane(hbox);
        Scene scene = new Scene(root, 500, 200);

        stage.setScene(scene);
        stage.setTitle("HBox Spacer Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
