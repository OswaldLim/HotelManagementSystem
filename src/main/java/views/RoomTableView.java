package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import models.Room;

import static controllers.RoomStatusController.updateRoomStatus;
import static services.RoomService.updateRoomInDatabase;
import static utils.TableUtils.formatTableColumnSize;

public class RoomTableView {
    public static TableView<Room> generateRoomTable(PieChart pieChart, ObservableList<String> roomStatusType, ObservableList<Label> labelList, String role) {
        //view all rooms table layout
        TableView<Room> tableView = new TableView<>();

        TableColumn<Room, Integer> roomIDColumn = new TableColumn<>("Room ID");
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomIdentificationNumber"));

        TableColumn<Room, Integer> roomCapacityColumn = new TableColumn<>("Room Capacity");
        roomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        roomCapacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        roomCapacityColumn.setOnEditCommit(editCapacity -> {
            Room room = editCapacity.getRowValue();
            room.setRoomCapacity(editCapacity.getNewValue());
            updateRoomInDatabase(room.getRoomIdentificationNumber(),"Capacity",editCapacity.getNewValue());
        });

        TableColumn<Room, Double> roomPricingColumn = new TableColumn<>("Room Pricing/night");
        roomPricingColumn.setCellValueFactory(new PropertyValueFactory<>("roomPricing"));
        roomPricingColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        roomPricingColumn.setOnEditCommit(editPrice -> {
            Room room = editPrice.getRowValue();
            room.setRoomPricing(editPrice.getNewValue());
            updateRoomInDatabase(room.getRoomIdentificationNumber(), "Price",editPrice.getNewValue());
        });

        TableColumn<Room, String> roomTypeColumn = new TableColumn<>("Room Type");
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        ObservableList<String> roomTypes = FXCollections.observableArrayList("Deluxe", "Suite", "Standard", "Single Room");
        roomTypeColumn.setCellFactory(tableCell -> new ChoiceBoxTableCell<>(roomTypes));
        roomTypeColumn.setOnEditCommit(editType -> {
            Room room = editType.getRowValue();
            room.setRoomType(editType.getNewValue());
            updateRoomInDatabase(room.getRoomIdentificationNumber(), "Type",editType.getNewValue());
        });

        TableColumn<Room, Image> roomPictureColumn = new TableColumn<>("Image");
        roomPictureColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        roomPictureColumn.setCellFactory(col -> new TableCell<>() {
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
        roomPictureColumn.setOnEditCommit(editPicture -> {
            Room room = editPicture.getRowValue();
            room.setPicturePath(editPicture.getNewValue());
            updateRoomInDatabase(room.getRoomIdentificationNumber(), "Pictures",editPicture.getNewValue());
        });

        TableColumn<Room, String> roomStatusColumn = new TableColumn<>("Room Availability");
        roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        roomStatusColumn.setCellFactory(tc -> new ChoiceBoxTableCell<>(roomStatusType));
        roomStatusColumn.setOnEditCommit(editStatus -> {
            Room room = editStatus.getRowValue();
            room.setRoomStatus(editStatus.getNewValue());
            updateRoomInDatabase(room.getRoomIdentificationNumber(),"Status", editStatus.getNewValue());
            updateRoomStatus(labelList.get(0), labelList.get(1), labelList.get(2), labelList.get(3), pieChart);
        });

        tableView.getColumns().addAll(roomIDColumn,roomCapacityColumn, roomPricingColumn, roomTypeColumn, roomPictureColumn, roomStatusColumn);
        formatTableColumnSize(tableView);

        //Only allow editing of the room status column if the user is not an admin
        if (!role.equals("Admin")) {
            tableView.setEditable(true);
            roomIDColumn.setEditable(false);
            roomCapacityColumn.setEditable(false);
            roomPricingColumn.setEditable(false);
            roomTypeColumn.setEditable(false);
            roomPictureColumn.setEditable(false);
            roomStatusColumn.setEditable(true);
        }

        return tableView;
    }

}
