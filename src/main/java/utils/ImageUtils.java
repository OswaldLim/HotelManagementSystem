package utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ImageUtils {

    public static void invertImageColour(ImageView imageView) {

        Image image = imageView.getImage();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage animatedImage = new WritableImage(width, height);

        PixelReader reader = image.getPixelReader();
        PixelWriter writer = animatedImage.getPixelWriter();

        Color[][] originalPixels = new Color[width][height];
        Color[][] invertedPixels = new Color[width][height];

        // Cache original and inverted colors
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = reader.getColor(x, y);
                originalPixels[x][y] = c;
                invertedPixels[x][y] = new Color(1 - c.getRed(), 1 - c.getGreen(), 1 - c.getBlue(), c.getOpacity());
            }
        }

        imageView.setImage(animatedImage);

        Timeline timeline = new Timeline();
        int frames = 5;
        double duration = 1.0; // in seconds

        for (int i = 0; i <= frames; i++) {
            double progress = i / (double) frames;
            KeyFrame frame = new KeyFrame(Duration.seconds(duration * progress), e -> {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color original = originalPixels[x][y];
                        Color inverted = invertedPixels[x][y];

                        // Interpolate between original and inverted
                        Color blended = new Color(
                                interpolate(original.getRed(), inverted.getRed(), progress),
                                interpolate(original.getGreen(), inverted.getGreen(), progress),
                                interpolate(original.getBlue(), inverted.getBlue(), progress),
                                original.getOpacity()
                        );
                        writer.setColor(x, y, blended);
                    }
                }
            });
            timeline.getKeyFrames().add(frame);
        }

        timeline.setCycleCount(1);
        timeline.play();


    }

    private static double interpolate(double start, double end, double progress) {
        return start + (end - start) * progress;
    }

}

