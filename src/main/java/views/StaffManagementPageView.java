package views;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Staff;

import static controllers.SceneController.switchContent;
import static services.StaffService.*;
import static utils.AlertUtils.textPage;
import static utils.InputUtils.*;
import static utils.TableUtils.toggleTableEditing;
import static views.LogoView.generateLogo;
import static views.StaffTableView.getStaffTableView;

public class StaffManagementPageView {

    // Main method to create the Staff Management page
    public static VBox getStaffManagementPageView(Stage adminPage){
        // Get list of staff from service
        ObservableList<Staff> staffDataList = getStaffList();
        VBox allStaffPage = new VBox(10);

        Label viewAllStaff = new Label("View All Staff");

        // Set up table view to display staff
        TableView<Staff> tableView = getStaffTableView(staffDataList, adminPage);

        // Input fields for new staff information
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Name...");

        TextField ICField = new TextField();
        ICField.setPromptText("Enter IC Number...");
        checkInputType(ICField, Integer.class);

        TextField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password...");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email...");

        TextField phoneNumberField = new TextField();
        checkInputType(phoneNumberField, Integer.class);
        phoneNumberField.setPromptText("Enter Phone Number...");

        // ChoiceBox for staff role selection
        ChoiceBox<String> roleBox = new ChoiceBox<>();
        roleBox.getItems().addAll("Admin", "Receptionist", "Cleaner", "Select Role...");
        roleBox.setValue("Select Role...");

        // Button to add a new staff member
        Button addStaffButton = new Button("Add New Staff");
        addStaffButton.setOnAction(addStaff -> {
            // Check for empty fields
            if (checkIfInputEmpty(nameField, emailField, ICField, phoneNumberField, roleBox, passwordField)) {
                if (isValidPhone(phoneNumberField.getText())) {
                    if (isValidIC(ICField.getText())) {
                        if (isValidEmail(emailField.getText())){
                            // Create and insert new staff
                            Staff newStaff = new Staff(
                                    staffDataList.getLast().getStaffID()+1,
                                    nameField.getText(),
                                    ICField.getText(),
                                    passwordField.getText(),
                                    roleBox.getValue(),
                                    emailField.getText(),
                                    phoneNumberField.getText()
                            );
                            insertNewStaff(newStaff);
                        } else {
                            textPage("Invalid email format", "ERROR: Invalid Input", true);
                        }
                    } else {
                        textPage("Invalid IC format", "ERROR: Invalid Input", true);
                    }
                } else {
                    textPage("Invalid phone Number format", "ERROR: Invalid Input", true);
                }
            }
        });

        // Button to remove selected staff from the table
        Button removeStaffButton = new Button("Remove Staff");
        removeStaffButton.setOnAction(removeStaff -> {
            Staff staff = tableView.getSelectionModel().getSelectedItem();

            if (staff == null) {
                textPage("Please Select a Staff to Remove", "ERROR: Invalid Input", true);
            } else {
                // Confirmation before deletion
                textPage("Are you sure you want to remove this Staff Account?", "Confirmation", false, true, confirmed -> {
                    if (confirmed) {
                        deleteStaff(staff, tableView);
                    }
                });
            }
        });

        // Button to toggle editing mode in the table
        Button editStaffButton = new Button("Edit Staff");
        editStaffButton.setOnAction(editStaff -> {
            toggleTableEditing(tableView, editStaffButton);
        });

        // HBox for input fields
        HBox inputFields = new HBox(10, nameField, ICField, passwordField, emailField, phoneNumberField, roleBox);

        // Adjust input field sizing relative to table view
        for (Node node : inputFields.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).prefWidthProperty().bind(tableView.widthProperty().divide(6));
                ((TextField) node).setPrefHeight(30);
            }
        }

        // HBox to hold the add/edit/remove buttons
        HBox buttonArea = new HBox(10, addStaffButton, editStaffButton, removeStaffButton);

        // VBox that groups input fields and buttons
        VBox inputFieldsAndButtons = new VBox(10, inputFields, buttonArea);

        // Spacer to separate form area from logo
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bottomArea = new HBox(10, inputFieldsAndButtons, spacer, generateLogo());

        // Assemble all components into the main layout
        allStaffPage.getChildren().addAll(viewAllStaff, tableView, bottomArea);
        allStaffPage.setPadding(new Insets(20));
        allStaffPage.setStyle("-fx-background-color: #FDFCE1");
        allStaffPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.87));
        allStaffPage.prefHeightProperty().bind(adminPage.heightProperty());

        return allStaffPage;
    }
}
