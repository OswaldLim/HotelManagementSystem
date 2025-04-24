package utils;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableUtils {

    //format all column size to not truncate any column header
    public static void formatTableColumnSize(TableView<?> tableView){
        for (TableColumn<?,?> column : tableView.getColumns()) {
            String headerText = column.getText();
            double headerWidthEstimate = headerText.length() * 10; // rough width per character
            column.setPrefWidth(headerWidthEstimate + 20); // add padding
        }
    }

    //enable editing of tables
    public static void toggleTableEditing(TableView<?> tableView, Button editButton) {
        boolean isEditing = tableView.isEditable();
        tableView.setEditable(!isEditing);
        editButton.setText(isEditing ? "Edit Reservations" : "Save Edits");
    }
}
