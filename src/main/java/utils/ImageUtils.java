package utils;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ImageUtils {

    public static void fadeImage(ImageView oldImageView, Image newImage) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), oldImageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            oldImageView.setImage(newImage);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), oldImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

}

