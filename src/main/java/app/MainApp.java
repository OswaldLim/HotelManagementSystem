package app;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static views.MainView.mainView;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainView(primaryStage);
    }
}
