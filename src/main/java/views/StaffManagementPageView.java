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
import static utils.InputUtils.checkIfInputEmpty;
import static utils.TableUtils.toggleTableEditing;
import static views.LogoView.generateLogo;
import static views.StaffTableView.getStaffTableView;

public class StaffManagementPageView {
    public static VBox getStaffManagementPageView(Stage adminPage){
        ObservableList<Staff> staffDataList = getStaffList();
        VBox allStaffPage = new VBox(10);

        Label viewAllStaff = new Label("View All Staff");

        TableView<Staff> tableView = getStaffTableView(staffDataList, adminPage);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Name...");

        TextField ICField = new TextField();
        ICField.setPromptText("Enter IC Number...");

        TextField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password...");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email...");

        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Enter Phone Number...");

        ChoiceBox<String> roleBox = new ChoiceBox<>();
        roleBox.getItems().addAll("Admin", "Receptionist", "Cleaner", "Select Role...");
        roleBox.setValue("Select Role...");

        Button addStaffButton = new Button("Add New Staff");
        addStaffButton.setOnAction(addStaff -> {
//            if (!(nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || roleBox.getItems().equals("Select Role...") || passwordField.getText().isEmpty())) {
            if (checkIfInputEmpty(nameField, emailField, phoneNumberField, roleBox, passwordField)) {
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
            }
        });

        Button removeStaffButton = new Button("Remove Staff");
        removeStaffButton.setOnAction(removeStaff -> {
            Staff staff = tableView.getSelectionModel().getSelectedItem();

            if (staff == null) {
                textPage("Please Select a Staff to Remove", "ERROR: Invalid Input", true);
            } else {
                textPage("Are you sure you want to remove this Staff Account?", "Confirmation", false, true, confirmed -> {
                    if (confirmed) {
                        deleteStaff(staff, tableView);
                    }
                });
            }
        });

        Button editStaffButton = new Button("Edit Staff");
        editStaffButton.setOnAction(editStaff -> {
            toggleTableEditing(tableView, editStaffButton);
        });

        HBox inputFields = new HBox(10, nameField, ICField, passwordField, emailField, phoneNumberField, roleBox);

        for (Node node : inputFields.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).prefWidthProperty().bind(tableView.widthProperty().divide(6));
                ((TextField) node).setPrefHeight(30);
            }
        }

        HBox buttonArea = new HBox(10, addStaffButton, editStaffButton, removeStaffButton);

        VBox inputFieldsAndButtons = new VBox(10, inputFields, buttonArea);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bottomArea = new HBox(10, inputFieldsAndButtons, spacer, generateLogo());

        allStaffPage.getChildren().addAll(viewAllStaff, tableView, bottomArea);
        allStaffPage.setPadding(new Insets(20));
        allStaffPage.setStyle("-fx-background-color: #FDFCE1");
        allStaffPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.87));
        allStaffPage.prefHeightProperty().bind(adminPage.heightProperty());

        return allStaffPage;
    }
}
