package app;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static views.FullGuestInterface.showFullGuestUI;
import static views.MainAdminView.showAdminUI;
import static views.MainView.mainView;

public class MainApp extends Application {

    private static Stage homePage;

    public static Stage getHomePage() {
        return homePage;
    }

    public static void setHomePage(Stage homePage) {
        MainApp.homePage = homePage;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setHomePage(primaryStage);

        //Start the main view


        //Maybe check the error handling in editing dates in reservation in admin
        mainView(primaryStage);
    }
}
