package controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneController {
    //custom Exit function for exit button to switch scenes
    public static void exit(Stage oldstage, Stage current) {
        current.close();
        oldstage.show();
    }

    //Animation between content switch
    public static void switchContent(Node newContent, ScrollPane scrollPane) {
        Node oldContent = scrollPane.getContent();

        if (oldContent != null) {
            // Animate old content out
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), oldContent);
            slideOut.setFromX(0);
            slideOut.setToX(-scrollPane.getWidth());

            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), oldContent);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            ParallelTransition exit = new ParallelTransition(slideOut, fadeOut);
            exit.setOnFinished(e -> {
                // Set new content after old one slides out
                scrollPane.setContent(newContent);

                // Animate new content in
                newContent.setTranslateX(scrollPane.getWidth());
                newContent.setOpacity(0.0);

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newContent);
                slideIn.setFromX(scrollPane.getWidth());
                slideIn.setToX(0);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);

                new ParallelTransition(slideIn, fadeIn).play();
            });

            exit.play();
        } else {
            // No current content, just show with animation
            scrollPane.setContent(newContent);
            newContent.setTranslateX(scrollPane.getWidth());
            newContent.setOpacity(0.0);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newContent);
            slideIn.setFromX(scrollPane.getWidth());
            slideIn.setToX(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newContent);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            new ParallelTransition(slideIn, fadeIn).play();
        }
    }

}
