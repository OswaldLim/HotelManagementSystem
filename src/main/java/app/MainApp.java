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

    //UPDATE sqlite_sequence SET seq = 4 WHERE name = 'your_table'; // code to reset primary key of table
    @Override
    public void start(Stage primaryStage) {
        String role = "Admin";
        Integer userID = 1;
        Image profilePic = new Image("file:logo.png");
        setHomePage(primaryStage);



//        showAdminUI(role, primaryStage);

//        showFullGuestUI(userID,profilePic,primaryStage);

        mainView(primaryStage);
    }
}
