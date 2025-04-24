package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Bookings;
import models.Room;

import java.time.LocalDate;

import static services.BookingService.*;
import static services.GuestService.getAllGuestID;
import static services.PaymentServices.getPaymentMethods;
import static services.RoomService.*;
import static utils.AlertUtils.textPage;
import static utils.TableUtils.toggleTableEditing;
import static views.LogoView.generateLogo;
import static views.ReservationTableView.getReservationTableView;

public class ReservationPageView {
    public static VBox getReservationPageView(Stage adminPage){
        //get all booking data from the database
        ObservableList<Bookings> bookingDataList = getBookingData();
        ObservableList<Integer> allRoomIDs = FXCollections.observableArrayList();

        //get all Guest data from the database
        ObservableList<Integer> allGuestIDs = getAllGuestID();
        ObservableList<String> allPaymentMethods = getPaymentMethods();

        VBox allReservationsPage = new VBox(10);
        Label viewAllReservation = new Label("View All Rooms");

        //get the reservation table vieww
        TableView<Bookings> tableView = getReservationTableView(allGuestIDs, allRoomIDs, allPaymentMethods, adminPage);

        //input boxes for inserting data
        Label insertGuestIdLabel = new Label("Insert Guest ID");
        ChoiceBox<Integer> insertGuestID = new ChoiceBox<>(allGuestIDs);

        Label insertRoomIdLabel = new Label("Insert Room ID: ");
        ChoiceBox<Integer> insertRoomID = new ChoiceBox<>(allRoomIDs);

        Label insertPaymentMethodLabel = new Label("Insert Payment Method: ");
        ChoiceBox<String> insertPaymentMethod = new ChoiceBox<>(allPaymentMethods);

        HBox inputBoxes = new HBox(10, insertGuestIdLabel ,insertGuestID, insertRoomIdLabel, insertRoomID, insertPaymentMethodLabel, insertPaymentMethod);

        Label insertCheckInDateLabel = new Label("Pick Check In Date: ");
        DatePicker insertCheckInDate = new DatePicker(LocalDate.now());
        insertCheckInDate.getEditor().setDisable(true);
        insertCheckInDate.getEditor().setOpacity(1);

        Label insertCheckOutDateLabel = new Label("Pick Check Out Date: ");
        DatePicker insertCheckOutDate = new DatePicker(LocalDate.now().plusDays(1));
        insertCheckOutDate.getEditor().setOpacity(1);
        insertCheckOutDate.getEditor().setDisable(true);

        HBox inputDatesBox = new HBox(10, insertCheckInDateLabel, insertCheckInDate, insertCheckOutDateLabel, insertCheckOutDate);
        HBox.setMargin(insertCheckInDate, new Insets(0, 50, 0, 0));
        //end of input boxes

        //Get all the room ids of available rooms
        for (Room room : getAvailableRooms(insertCheckInDate.getValue(), insertCheckOutDate.getValue(), null)){
            allRoomIDs.add(room.getRoomIdentificationNumber());
        }

        //used to Check for interactions with the check in date picker and checks if the date choice is valid
        insertCheckInDate.valueProperty().addListener((obs, oldValue, newValue) -> {
            allRoomIDs.clear();
            try {
                for (Room room : getAvailableRooms(insertCheckInDate.getValue(), insertCheckOutDate.getValue(), null)){
                    allRoomIDs.add(room.getRoomIdentificationNumber());
                }
            } catch (RuntimeException e){
                e.printStackTrace();
            }

            if (allRoomIDs.isEmpty()) {
                insertCheckInDate.setValue(oldValue);
            }
        });

        //used to Check for interactions with the check out date picker and checks if the date choice is valid
        insertCheckOutDate.valueProperty().addListener((obs, oldValue, newValue) -> {
            allRoomIDs.clear();
            try {
                for (Room room : getAvailableRooms(insertCheckInDate.getValue(), insertCheckOutDate.getValue(), null)){
                    allRoomIDs.add(room.getRoomIdentificationNumber());
                }
            } catch (RuntimeException e){
                e.printStackTrace();
            }

            if (allRoomIDs.isEmpty()) {
                insertCheckOutDate.setValue(oldValue);
            }
        });

        //Button Area
        Button insertDataButton = new Button("Add Reservations");
        //Insert Booking Data into the database
        insertDataButton.setOnAction(insertDataEvent -> {
            Integer roomId = insertRoomID.getValue();
            LocalDate checkInDate = insertCheckInDate.getValue();
            LocalDate checkOutDate = insertCheckOutDate.getValue();
            Double totalAmount = getPricing(roomId, checkInDate, checkOutDate);
            checkValidBookingInput(checkInDate, checkOutDate,insertRoomID, insertGuestID, insertPaymentMethod);

            Bookings newReservation = new Bookings(
                    bookingDataList.getLast().getBookingID()+1,
                    insertGuestID.getValue(),
                    insertRoomID.getValue(),
                    insertCheckInDate.getValue(),
                    insertCheckOutDate.getValue(),
                    totalAmount,
                    insertPaymentMethod.getValue(),
                    LocalDate.now(),
                    "Success"
            );
            bookingDataList.add(newReservation);
            tableView.refresh();

            insertNewBooking(insertGuestID.getValue(), String.valueOf(insertRoomID.getValue()), checkInDate, checkOutDate, String.valueOf(totalAmount), insertPaymentMethod.getValue(), "Admin");
        });

        //allows editing of table view
        Button editDataButton = new Button("Edit Reservations");
        editDataButton.setOnAction(editReservationEvent -> {
            toggleTableEditing(tableView, editDataButton);
        });

        //Button to delete data from table view and the database
        Button deleteDataButton = new Button("Delete Reservations");
        deleteDataButton.setOnAction(deleteReservation -> {
            Bookings bookings = tableView.getSelectionModel().getSelectedItem();

            if (bookings == null){
                textPage("Please Select a reservation to Delete", "ERROR: Invalid Input", true);
                return;
            }

            textPage("Are you sure you want to delete this Reservation?", "Confirmation", false, true, confirmed -> {
                if (confirmed) {
                    deleteBooking(tableView, bookings);
                }
            });
        });

        HBox buttonArea = new HBox(20, insertDataButton, editDataButton, deleteDataButton);

        VBox inputFieldsAndButtons = new VBox(10, inputBoxes, buttonArea);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bottomArea = new HBox(10, inputFieldsAndButtons, spacer, generateLogo());

        tableView.setItems(bookingDataList);
        allReservationsPage.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.84));
        allReservationsPage.prefHeightProperty().bind(adminPage.heightProperty());
        allReservationsPage.getChildren().addAll(viewAllReservation,tableView, inputDatesBox, bottomArea);
        allReservationsPage.setPadding(new Insets(20));
        allReservationsPage.setStyle("-fx-background-color: #FDFCE1");

        return allReservationsPage;
    }
}
