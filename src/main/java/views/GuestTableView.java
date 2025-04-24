package views;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Guest;

import static services.GuestService.getAllGuestData;
import static utils.TableUtils.formatTableColumnSize;

public class GuestTableView {
    public static TableView<Guest> getGuestTableView() {
        //view all Guests table layout
        ObservableList<Guest> allGuestDataList = getAllGuestData();
        TableView<Guest> tableView = new TableView<>();

        TableColumn<Guest, Integer> guestIDColumn = new TableColumn<>("Guest ID");
        guestIDColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));

        TableColumn<Guest, String> guestNameColumn = new TableColumn<>("Last Name");
        guestNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestLastName"));

        TableColumn<Guest, String> guestICColumn = new TableColumn<>("IC Number");
        guestICColumn.setCellValueFactory(new PropertyValueFactory<>("ICNum"));

        TableColumn<Guest, String> guestEmailColumn = new TableColumn<>("Email");
        guestEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        //Change the cell of the image column to imageView
        TableColumn<Guest, Image> guestProfileColumn = new TableColumn<>("Profile Picture");
        guestProfileColumn.setCellValueFactory(new PropertyValueFactory<>("profilePic"));
        guestProfileColumn.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    imageView.setFitWidth(80);   // Optional: Resize the image
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        //Makes the column to expand equally to fill all space in the tableView
        tableView.getColumns().addAll(guestIDColumn, guestNameColumn, guestICColumn, guestEmailColumn, guestProfileColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (TableColumn<?, ?> column : tableView.getColumns()) {
            column.setMaxWidth(1f * Integer.MAX_VALUE); // allow it to expand fully
        }



        tableView.setItems(allGuestDataList);

        return tableView;
    }

}
