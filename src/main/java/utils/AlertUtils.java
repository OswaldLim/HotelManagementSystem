package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertUtils {

    //Custom error popup
    public static void textPage(String text, String title, boolean err) {
        textPage(text, title, err, false, confirmed -> {
            // Default behavior: do nothing on confirmation
        });
    }

    //Popup to show text, show error, and get user confirmation actions
    public static void textPage(String text, String title, boolean err, boolean conf, ConfirmationCallback callBack) {
        Stage error = new Stage();
        error.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = createTextContent(text, err);
        HBox hBox = new HBox();
        setErrorImage(err, hBox, vBox);

        if (conf) {
            addConfirmationButtons(hBox, vBox, callBack, error);
        }

        Scene scene = new Scene(hBox, 400, 150);
        scene.getStylesheets().add("file:Style.css");
        error.setResizable(false);
        error.setTitle(title);
        error.setScene(scene);
        if (conf){
            error.showAndWait();
        } else {
            error.show();
        }
    }

    //create text format
    private static VBox createTextContent(String text, boolean err) {
        Text info = new Text(text);

        if (!err){
            info.setWrappingWidth(400);
        } else {
            info.setWrappingWidth(250);
        }

        info.setFont(new Font("Georgia", 14));
        VBox vBox = new VBox(30, info);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        return vBox;
    }

    //Adds an error image if the textPage() is used for an error
    private static void setErrorImage(boolean err, HBox hBox, VBox vBox) {
        if (err) {
            Image image = new Image("file:Images/System Logo/Error.jpeg");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            hBox.getChildren().addAll(imageView, vBox);
        } else {
            hBox.getChildren().add(vBox);
        }
    }

    //Add confirmation buttons when textPage is used for confirmation
    private static void addConfirmationButtons(HBox hBox, VBox vBox, ConfirmationCallback callBack, Stage error) {
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(yesEvent -> {
            callBack.onConfirmed(true);
            error.close();
        });
        Button noButton = new Button("No");
        noButton.setOnAction(noEvent -> {
            callBack.onConfirmed(false);
            error.close();
        });
        HBox confirmationArea = new HBox(20, yesButton, noButton);
        vBox.getChildren().addAll(confirmationArea);
    }

    public interface ConfirmationCallback {
        void onConfirmed(boolean confirmed);
    }
}