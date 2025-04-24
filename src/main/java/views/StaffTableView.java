package views;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import models.Staff;

import static services.StaffService.updateStaffInDatabase;
import static utils.TableUtils.formatTableColumnSize;

public class StaffTableView {
    public static TableView<Staff> getStaffTableView(ObservableList<Staff> staffDataList, Stage adminPage){
        TableView<Staff> tableView = new TableView<>();

        // Staff ID column - non-editable
        TableColumn<Staff, Integer> staffIDColumn = new TableColumn<>("Staff ID");
        staffIDColumn.setCellValueFactory(new PropertyValueFactory<>("staffID"));

        // Staff Name column - editable
        TableColumn<Staff, String> staffNameColumn = new TableColumn<>("Name");
        staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        staffNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        staffNameColumn.setOnEditCommit(editName -> {
            Staff staff = editName.getRowValue();
            staff.setStaffUsername(editName.getNewValue());
            updateStaffInDatabase(staff.getStaffID(), "Username", editName.getNewValue());
        });

        // IC Number column - editable
        TableColumn<Staff, String> staffICColumn = new TableColumn<>("IC Number");
        staffICColumn.setCellValueFactory(new PropertyValueFactory<>("staffIC"));
        staffICColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        staffICColumn.setOnEditCommit(editIC -> {
            Staff staff = editIC.getRowValue();
            staff.setStaffIC(editIC.getNewValue());
            updateStaffInDatabase(staff.getStaffID(), "ICNum", editIC.getNewValue());
        });

        // Password column - editable (not currently added to table below)
        TableColumn<Staff, String> staffPasswordColumn = new TableColumn<>("Password");
        staffPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("staffPassword"));
        staffPasswordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        staffPasswordColumn.setOnEditCommit(editPassword -> {
            Staff staff = editPassword.getRowValue();
            staff.setStaffPassword(editPassword.getNewValue());
            updateStaffInDatabase(staff.getStaffID(), "Password", editPassword.getNewValue());
        });

        // Role column - editable via dropdown
        TableColumn<Staff, String> staffRoleColumn = new TableColumn<>("Role");
        staffRoleColumn.setCellValueFactory(new PropertyValueFactory<>("staffRole"));
        staffRoleColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn("Admin", "Cleaner", "Receptionist"));
        staffRoleColumn.setOnEditCommit(editRole -> {
            Staff staff = editRole.getRowValue();
            staff.setStaffRole(editRole.getNewValue());
            updateStaffInDatabase(staff.getStaffID(), "Role", editRole.getNewValue());
        });

        // Email column - editable
        TableColumn<Staff, String> staffEmailColumn = new TableColumn<>("Email");
        staffEmailColumn.setCellValueFactory(new PropertyValueFactory<>("staffEmail"));
        staffEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        staffEmailColumn.setOnEditCommit(editEmail -> {
            Staff staff = editEmail.getRowValue();
            staff.setStaffEmail(editEmail.getNewValue());
            updateStaffInDatabase(staff.getStaffID(), "Email", editEmail.getNewValue());
        });

        // Phone Number column - editable
        TableColumn<Staff, String> staffPhoneNumberColumn = new TableColumn<>("Phone Number");
        staffPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("staffPhoneNumber"));
        staffPhoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        staffPhoneNumberColumn.setOnEditCommit(editPhoneNumber -> {
            Staff staff = editPhoneNumber.getRowValue();
            staff.setStaffPhoneNumber(editPhoneNumber.getNewValue());
            updateStaffInDatabase(staff.getStaffID(), "PhoneNumber", editPhoneNumber.getNewValue());
        });

        // Bind data to table
        tableView.setItems(staffDataList);

        // Add desired columns (excluding password column by default)
        tableView.getColumns().addAll(staffIDColumn, staffNameColumn, staffICColumn, staffRoleColumn, staffEmailColumn, staffPhoneNumberColumn);

        // tableView.getColumns().addAll(staffIDColumn, staffNameColumn, staffICColumn, staffPasswordColumn, staffRoleColumn, staffEmailColumn, staffPhoneNumberColumn);

        // Set column resize policy to fill available space
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Expand each column proportionally
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            column.setMaxWidth(1f * Integer.MAX_VALUE);
        }

        // Bind table dimensions to admin page window
        tableView.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.7));
        tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.7));

        return tableView;
    }
}
