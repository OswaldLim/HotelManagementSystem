package controllers;

import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

public class AnimationController {
    //Right movement
    public static void moveRightMovement(Stage stage, Rectangle rectangle){
        TranslateTransition moveRight = new TranslateTransition(Duration.seconds(1),rectangle);
        moveRight.setToX(stage.getWidth()*0.49);

        FillTransition blueToYellow = new FillTransition(Duration.seconds(1),rectangle);
        blueToYellow.setFromValue(Color.web("#1E3A8A"));
        blueToYellow.setToValue(Color.web("#EAB308"));

        moveRight.play();
        blueToYellow.play();
    }

    //Left movement
    public static void moveLeftMovement(Stage stage, Rectangle rectangle){
        TranslateTransition moveLeft = new TranslateTransition(Duration.seconds(1),rectangle);
        moveLeft.setFromX(stage.getWidth()*0.5);
        moveLeft.setToX(0);

        FillTransition yellowToBlue = new FillTransition(Duration.seconds(1),rectangle);
        yellowToBlue.setFromValue(Color.web("#EAB308"));
        yellowToBlue.setToValue(Color.web("#1E3A8A"));

        yellowToBlue.play();
        moveLeft.play();
    }

    //maintain the placement even when the stage is resized in main login Page
    public static void maintainPlacementLeft(Rectangle rectangle){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),rectangle);
        translateTransition.setToX(0);
        translateTransition.play();
    }

    //maintain the placement even when the stage is resized in main login Page
    public static void maintainPlacementRight(Stage stage, Rectangle rectangle){
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),rectangle);
        translateTransition.setToX(stage.getWidth()*0.49);
        translateTransition.play();
    }
}
