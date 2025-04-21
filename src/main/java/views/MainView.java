package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import static controllers.AnimationController.*;
import static services.BookingService.setBookingStatus;
import static views.LoginView.getLoginInterface;
import static views.SignUpView.getSignUpView;

public class MainView {
    private static String action = "login";

    public static void setAction(String act){
        action = act;
    }

    public static void mainView(Stage stage) {
        //Change the status of bookings automatically to check in or check out
        setBookingStatus();

        Image image = new Image("file:logo_noBackground.png");
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(stage.heightProperty().multiply(0.9));

        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(stage.widthProperty().multiply(0.5));
        rectangle.heightProperty().bind(stage.heightProperty());
        rectangle.setX(0);
        rectangle.setFill(Color.web("#1E3A8A"));

        imageView.translateXProperty().bind(
                rectangle.translateXProperty()
                        .add(rectangle.widthProperty().divide(-2))
                        .subtract(imageView.fitWidthProperty().divide(2))
        );

        //Interface
        VBox logInInterface = getLoginInterface(stage, rectangle);

        //SignUpPage start
        VBox signUpInterface = getSignUpView(stage, rectangle);
        //SIgnUpPage Ending

        BorderPane finalBorderPane = new BorderPane();
        finalBorderPane.setLeft(signUpInterface);
        finalBorderPane.setRight(logInInterface);

        StackPane finalPane = new StackPane(finalBorderPane,rectangle,imageView);
        StackPane.setAlignment(rectangle,Pos.CENTER_LEFT);

        stage.widthProperty().addListener((obs,oldWidth,newWidth) -> {
            if (action.equals("signUp")){
                maintainPlacementRight(stage, rectangle);
            } else {
                maintainPlacementLeft(rectangle);
            }
        });

        Scene scene = new Scene(finalPane,1000,500);
        stage.setTitle("Login Page");
        stage.setScene(scene);
        scene.getStylesheets().add("file:Style.css");
        stage.show();
    }
}
