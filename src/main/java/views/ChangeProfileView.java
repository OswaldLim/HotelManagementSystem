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

    //Small window that handles profile changing
    public static String showChangeImageStage(ImageView initialImageView, Integer userID, String usage){
        Stage changeProfileStage = new Stage();
        changeProfileStage.setResizable(false);
        //Make sure users can only interact with this page if this page is present
        changeProfileStage.initModality(Modality.APPLICATION_MODAL);
        ImageView imageView1;

        //If no initial imageView is present, create a new imageview
        if (initialImageView == null){
            imageView1 = new ImageView();
        } else {
            imageView1 = new ImageView(initialImageView.getImage());
        }
        imageView1.setFitWidth(300);
        imageView1.setFitHeight(300);
        Rectangle clip1 = new Rectangle(300, 300);
        clip1.setArcWidth(30);
        clip1.setArcHeight(30);
        imageView1.setClip(clip1);

        //Button to upload Image
        Button changeButton = new Button();

        //Atomic reference to save file path of the image uploaded
        AtomicReference<String> filePath = new AtomicReference<>("");
        changeButton.setOnAction(changeProfileEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose an Image");

            //Makes sure the file chosen is an image file
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(changeProfileStage);
            if (selectedFile != null) {
                try {
                    //Destination directory is dependent on the usage being either "Profile Picture" or "Import"
                    String destDir;
                    if (usage.equals("Import")) {
                        destDir = "Images/Room";
                    } else {
                        destDir= "Images/Profile";
                    }

                    //Copies the filename of the chosen file into the directory
                    Path targetPath = Paths.get(destDir, selectedFile.getName());

                    Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                    //Saves the new filePath in the atomic reference above to be accessed elsewhere
                    filePath.set(selectedFile.getName());

                    //Sets the new Image in the ImageView inside the Page for users to check if the chosen image is satisfactory
                    Image profileImage = new Image("file:" + destDir + "/" + filePath.get());
                    imageView1.setImage(profileImage);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });

        //Button t confirm the change the changes the Image in the original page
        Button confirmButton = new Button("Confirm change");

        //Button prompts vary depending on the usage
        if (!usage.equals("Import")){
            confirmButton.setText("Confirm Change");
            changeButton.setText("Change Profile Picture");
        } else {
            confirmButton.setText("Confirm Import");
            changeButton.setText("Upload Image");
        }

        //if the confirmed, set the new Image in the original page and updates the new image path into the database
        confirmButton.setOnAction(confirmEvent -> {
            if (!filePath.get().isEmpty()) {
                if (!usage.equals("Import")){
                    initialImageView.setImage(new Image("file:Images/Profile/"+filePath.get()));
                    updateGuestInDatabase(userID, "ProfilePicPath", filePath.get());
                }
                changeProfileStage.hide();
            } else {
                //Make sure an image is chosen or else throw error
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
