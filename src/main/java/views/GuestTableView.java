package views;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;
import models.Guest;
import models.Room;
import models.Staff;

import static services.GuestService.getAllGuestData;
import static services.GuestService.updateGuestInDatabase;
import static services.RoomService.updateRoomInDatabase;
import static services.StaffService.updateStaffInDatabase;
import static utils.TableUtils.formatTableColumnSize;

public class GuestTableView {
    public static TableView<Guest> getGuestTableView() {
        //view all Guests table layout
        TableView<Guest> tableView = new TableView<>();

        TableColumn<Guest, Integer> guestIDColumn = new TableColumn<>("Guest ID");
        guestIDColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));

        TableColumn<Guest, String> guestNameColumn = new TableColumn<>("Last Name");
        guestNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestLastName"));
        guestNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        guestNameColumn.setOnEditCommit(editLastName -> {
            Guest guest = editLastName.getRowValue();
            guest.setGuestLastName(editLastName.getNewValue());
            updateGuestInDatabase(guest.getGuestID(), "LastName", editLastName.getNewValue());
        });

        TableColumn<Guest, String> guestICColumn = new TableColumn<>("IC Number");
        guestICColumn.setCellValueFactory(new PropertyValueFactory<>("ICNum"));
        guestICColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        guestICColumn.setOnEditCommit(editIC -> {
            Guest guest = editIC.getRowValue();
            guest.setICNum(editIC.getNewValue());
            updateGuestInDatabase(guest.getGuestID(), "ICNum", editIC.getNewValue());
        });

        TableColumn<Guest, String> guestEmailColumn = new TableColumn<>("Email");
        guestEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        guestEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        guestEmailColumn.setOnEditCommit(editEmail -> {
            Guest guest = editEmail.getRowValue();
            guest.setEmail(editEmail.getNewValue());
            updateGuestInDatabase(guest.getGuestID(), "Email", editEmail.getNewValue());
        });

        TableColumn<Guest, String> guestPhoneNumberColumn = new TableColumn<>("Phone Number");
        guestPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        guestPhoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        guestPhoneNumberColumn.setOnEditCommit(editPhone -> {
            Guest guest = editPhone.getRowValue();
            guest.setPhoneNumber(editPhone.getNewValue());
            updateGuestInDatabase(guest.getGuestID(), "PhoneNumber", editPhone.getNewValue());
        });

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
        tableView.getColumns().addAll(guestIDColumn, guestNameColumn, guestICColumn, guestEmailColumn, guestPhoneNumberColumn, guestProfileColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (TableColumn<?, ?> column : tableView.getColumns()) {
            column.setMaxWidth(1f * Integer.MAX_VALUE); // allow it to expand fully
        }


        return tableView;
    }

}
