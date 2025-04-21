package controllers;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.GuestService;

public class RegisterController {
    private GuestService guestService = new GuestService();

    public void onRegisterButtonClick(Stage stage, TextField... creden) {
        boolean userCreated = guestService.checkUserExists(creden);
        if (userCreated) {
            // User successfully registered, proceed to the next steps
        }
    }
}
