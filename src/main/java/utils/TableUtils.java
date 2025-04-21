package utils;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableUtils {

    public static void formatTableColumnSize(TableView<?> tableView){
        for (TableColumn<?,?> column : tableView.getColumns()) {
            String headerText = column.getText();
            double headerWidthEstimate = headerText.length() * 10; // rough width per character
            column.setPrefWidth(headerWidthEstimate + 20); // add padding
        }
    }

    public static void toggleTableEditing(TableView<?> tableView, Button editButton) {
        boolean isEditing = tableView.isEditable();
        tableView.setEditable(!isEditing);
        editButton.setText(isEditing ? "Edit Reservations" : "Save Edits");
    }
}
