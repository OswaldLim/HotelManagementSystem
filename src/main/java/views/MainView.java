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
import static services.BookingService.getBookingData;
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
        getBookingData();
        setBookingStatus();

        //preloads the images with the hotel logo for smoother transition
        Image image = new Image("file:Images/System Logo/LCHLOGINLOGO.png");
        Image secondImage = new Image("file:Images/System Logo/LCHSIGNUPLOGO.png");
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(stage.heightProperty());

        //Rectangle to block login page when users signing up and block sign up page when user logging in
        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(stage.widthProperty().multiply(0.5));
        rectangle.heightProperty().bind(stage.heightProperty());
        rectangle.setX(0);
        rectangle.setFill(Color.web("#1E3A8A"));

        //Makes sure that the image is always aligned to the center of the rectangle
        imageView.translateXProperty().bind(
                rectangle.translateXProperty()
                        .add(rectangle.widthProperty().divide(-2))
                        .subtract(imageView.fitWidthProperty().divide(2)).add(15)
        );

        //Creates Login Interface
        VBox logInInterface = getLoginInterface(stage, rectangle, imageView, secondImage);

        //Creates Sign Up Interface
        VBox signUpInterface = getSignUpView(stage, rectangle, imageView, secondImage);

        //BorderPane to hold both login and sign up interfaces
        BorderPane finalBorderPane = new BorderPane();
        finalBorderPane.setLeft(signUpInterface);
        finalBorderPane.setRight(logInInterface);

        //Stack Pane to put the rectangle and image that is covering the unused interface
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
