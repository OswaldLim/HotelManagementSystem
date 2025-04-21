package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static services.GuestService.updateGuestInDatabase;
import static services.RoomService.updateRoomInDatabase;
import static utils.AlertUtils.textPage;

public class ChangeProfileView {

    public static String showChangeImageStage(ImageView imageView, Integer userID, String usage){
        Stage changeProfileStage = new Stage();
        changeProfileStage.setResizable(false);
        changeProfileStage.initModality(Modality.APPLICATION_MODAL);
        ImageView imageView1;
        if (imageView == null){
            imageView1 = new ImageView();
        } else {
            imageView1 = new ImageView(imageView.getImage());
        }
        imageView1.setFitWidth(300);
        imageView1.setFitHeight(300);
        Rectangle clip1 = new Rectangle(300, 300);
        clip1.setArcWidth(30);
        clip1.setArcHeight(30);
        imageView1.setClip(clip1);

        Button changeButton = new Button();

        AtomicReference<String> filePath = new AtomicReference<>("");
        changeButton.setOnAction(changeProfileEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose an Image");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(changeProfileStage);
            if (selectedFile != null) {
                try {
                    String destDir;
                    if (usage.equals("Import")) {
                        destDir = "Images/Room";
                    } else {
                        destDir= "Images/Profile";
                    }

                    Path targetPath = Paths.get(destDir, selectedFile.getName());

                    Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                    filePath.set(selectedFile.getName());

                    Image profileImage = new Image("file:" + destDir + "/" + filePath.get());

                    imageView1.setImage(profileImage);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });

        Button confirmButton = new Button("Confirm change");
        if (!usage.equals("Import")){
            confirmButton.setText("Confirm Change");
            changeButton.setText("Change Profile Picture");
        } else {
            confirmButton.setText("Confirm Import");
            changeButton.setText("Upload Image");
        }

        confirmButton.setOnAction(confirmEvent -> {
            if (!filePath.get().isEmpty()) {
                if (!usage.equals("Import")){
                    imageView.setImage(new Image("file:Images/Profile/"+filePath.get()));
                    updateGuestInDatabase(userID, "ProfilePicPath", filePath.get());
                }
                changeProfileStage.hide();
            } else {
                textPage("Please Pick an image", "ERROR: Invalid Image", true);
            }
        });

        VBox showProfile = new VBox(10, imageView1, changeButton, confirmButton);


        showProfile.setAlignment(Pos.CENTER);

        Scene scene = new Scene(showProfile, 500, 500);
        scene.getStylesheets().add("file:Style.css");
        changeProfileStage.setScene(scene);
        changeProfileStage.setTitle("Change Profile Picture");
        changeProfileStage.showAndWait();
        return filePath.get();
    }

}
