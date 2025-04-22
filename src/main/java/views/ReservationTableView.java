package views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import models.Bookings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static services.BookingService.updateBookingInDatabase;
import static utils.AlertUtils.textPage;
import static utils.TableUtils.formatTableColumnSize;

public class ReservationTableView {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static TableView<Bookings> getReservationTableView(ObservableList<Integer> allGuestIDs, ObservableList<Integer> allRoomIDs, ObservableList<String> allPaymentMethods, Stage adminPage){
        TableView<Bookings> tableView = new TableView<>();

        TableColumn<Bookings, Integer> bookingIdColumn = new TableColumn<>("Booking ID");
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingID"));

        TableColumn<Bookings, Integer> guestIdColumn = new TableColumn<>("Guest ID");
        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestID"));
        guestIdColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allGuestIDs));
        guestIdColumn.setOnEditCommit(editGuestId -> {
            Bookings bookings = editGuestId.getRowValue();
            bookings.setGuestID(editGuestId.getNewValue());
            updateBookingInDatabase(bookings.getBookingID(), "GuestID", editGuestId.getNewValue());
        });

        TableColumn<Bookings, Integer> roomIdColumn = new TableColumn<>("Room ID");
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        roomIdColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allRoomIDs));
        roomIdColumn.setOnEditCommit(editRoomId -> {
            Bookings bookings = editRoomId.getRowValue();
            bookings.setRoomID(editRoomId.getNewValue());
            updateBookingInDatabase(bookings.getBookingID(), "RoomID", editRoomId.getNewValue());
        });

        TableColumn<Bookings, LocalDate> checkInColumn = new TableColumn<>("Check In Date");
        checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkInColumn.setCellFactory(tablecell -> new TableCell<>() {
            private final DatePicker checkInDatePicker = new DatePicker();
            {
                checkInDatePicker.setOnAction(event -> {
                    Platform.runLater(() -> {
                        commitEdit(checkInDatePicker.getValue());
                    });
                });

            }

            public void startEdit() {
                super.startEdit();
                if (!isEmpty()) {
                    checkInDatePicker.setValue(getItem());
                    setText(null);
                    setGraphic(checkInDatePicker);
                }
            }

            public void cancelEdit() {
                super.cancelEdit();
                LocalDate originalDate = getItem(); // This is the unedited value
                checkInDatePicker.setValue(originalDate); // Reset DatePicker UI

                setText(originalDate != null ? originalDate.format(formatter) : null);
                setGraphic(null);
            }

            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        checkInDatePicker.setValue(date);
                        setText(null);
                        setGraphic(checkInDatePicker);
                    } else {
                        setText(date != null ? date.format(formatter) : null);
                        setGraphic(null);
                    }
                }
            }

            public void commitEdit(LocalDate newDate) {
                super.commitEdit(newDate);
                // Always access row item safely
                Bookings booking = getTableRow().getItem();
                if (booking != null) {
                    LocalDate checkOutDate = booking.getCheckOutDate();
                    if (newDate.isAfter(checkOutDate) || newDate.isBefore(LocalDate.now())) {
                        textPage(
                                "Possible Errors: \n" +
                                        "- New Check In Date is After Check Out Date\n" +
                                        "- New Check In Date is Before today",
                                "ERROR: Invalid Date", true);
                        cancelEdit();
                    }
                    else {
                        booking.setCheckInDate(newDate);
                        updateBookingInDatabase(booking.getBookingID(), "CheckInDate", newDate);
                    }
                }
            }
        });

        TableColumn<Bookings, LocalDate> checkOutColumn = new TableColumn<>("Check Out Column");
        checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        checkOutColumn.setCellFactory(tablecell -> new TableCell<>() {
            private final DatePicker checkOutDatePicker = new DatePicker();
            {
                checkOutDatePicker.setOnAction(event -> {
                    Platform.runLater(() -> {
                        commitEdit(checkOutDatePicker.getValue());
                    });
                });
            }

            public void startEdit() {
                super.startEdit();
                if (!isEmpty()) {
                    checkOutDatePicker.setValue(getItem());
                    setText(null);
                    setGraphic(checkOutDatePicker);
                }
            }

            public void cancelEdit() {
                super.cancelEdit();
                LocalDate originalDate = getItem(); // This is the unedited value
                checkOutDatePicker.setValue(originalDate); // Reset DatePicker UI

                setText(originalDate != null ? originalDate.format(formatter) : null);
                setGraphic(null);
            }

            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        checkOutDatePicker.setValue(date);
                        setText(null);
                        setGraphic(checkOutDatePicker);
                    } else {
                        setText(date != null ? date.format(formatter) : null);
                        setGraphic(null);
                    }
                }
            }

            public void commitEdit(LocalDate newDate) {
                super.commitEdit(newDate);
                // Always access row item safely
                Bookings booking = getTableRow().getItem();
                if (booking != null) {
                    LocalDate checkInDate = booking.getCheckInDate();
                    if (newDate.isBefore(checkInDate) || newDate.isBefore(LocalDate.now())) {
                        textPage(
                                "Possible Errors: \n" +
                                        "- New Check Out Date is Before Check In Date\n" +
                                        "- New Check Out Date is Before today",
                                "ERROR: Invalid Date", true);
                        cancelEdit();
                    }
                    else {
                        booking.setCheckOutDate(newDate);
                        updateBookingInDatabase(booking.getBookingID(), "CheckOutDate", newDate);
                    }
                }
            }
        });

        TableColumn<Bookings, Double> totalAmountColumn = new TableColumn<>("Payment Amount");
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        totalAmountColumn.setOnEditCommit(editTotalAmount -> {
            Bookings bookings = editTotalAmount.getRowValue();
            bookings.setTotalAmount(editTotalAmount.getNewValue());
            updateBookingInDatabase(bookings.getBookingID(), "TotalAmount", editTotalAmount.getNewValue());
        });

        TableColumn<Bookings, String> paymentTypeColumn = new TableColumn<>("Payment Type");
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        paymentTypeColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(allPaymentMethods));
        paymentTypeColumn.setOnEditCommit(editPaymentType -> {
            Bookings bookings = editPaymentType.getRowValue();
            bookings.setPaymentType(editPaymentType.getNewValue());
            updateBookingInDatabase(bookings.getBookingID(), "PaymentType", editPaymentType.getNewValue());
        });

        TableColumn<Bookings, LocalDate> bookingDateColumn = new TableColumn<>("Booking Date");
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));

        TableColumn<Bookings, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<String> statusType = FXCollections.observableArrayList("Success", "Pending", "Canceled");
        statusColumn.setCellFactory(tablecell -> new ChoiceBoxTableCell<>(statusType));
        statusColumn.setOnEditCommit(editStatusType -> {
            Bookings bookings = editStatusType.getRowValue();
            String newStatus = editStatusType.getNewValue();
            String oldStatus = editStatusType.getOldValue();
            textPage("Are You Sure you want to edit the Status?", "Confirmation", false, true, confirmed -> {
                if (confirmed) {
                    bookings.setStatus(newStatus);
                    updateBookingInDatabase(bookings.getBookingID(), "Status", newStatus);
                    editStatusType.getTableView().refresh();
                } else {
                    bookings.setStatus(oldStatus);
                    editStatusType.getTableView().refresh();
                }
            });
        });

        tableView.getColumns().addAll(bookingIdColumn, guestIdColumn, roomIdColumn, checkInColumn, checkOutColumn, totalAmountColumn, paymentTypeColumn, bookingDateColumn, statusColumn);
        formatTableColumnSize(tableView);
        tableView.prefWidthProperty().bind(adminPage.widthProperty().multiply(0.7));
        tableView.prefHeightProperty().bind(adminPage.heightProperty().multiply(0.7));

        return tableView;
    }
}
