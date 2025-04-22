package views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LogoView {
    private static final Image logoImage = new Image("file:Images/System Logo/LCHLOGO.png");

    public static ImageView generateLogo(){
        ImageView imageView = new ImageView(logoImage);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setPickOnBounds(true);

        return imageView;
    }
}
