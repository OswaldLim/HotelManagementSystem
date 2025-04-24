package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Bookings;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static services.RoomService.updateRoomInDatabase;
import static utils.AlertUtils.textPage;

public class BookingService {
    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";
    private static ObservableList<Bookings> bookingDataList = FXCollections.observableArrayList();

    //used to update specific Booking data in the database
    public static void updateBookingInDatabase(int bookingID, String column, Object newValue){
        String sql = "update booking set " + column + " = ? WHERE BookingID = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,bookingID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //cancel bookings and set the rooms to available
    public static void cancelBooking(String bookingID, String roomID, MenuItem menuItem, MenuButton menuButton, Stage infoPage){
        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement cancelQuery = connection.prepareStatement("Update booking set Status = 'Pending' where BookingID = ?");
             PreparedStatement alterQuery = connection.prepareStatement("Update room set status = 'available' where RoomID = ?")
        ) {
            cancelQuery.setString(1,bookingID);
            alterQuery.setString(1,roomID);
            cancelQuery.executeUpdate();
            alterQuery.executeUpdate();
            menuButton.getItems().remove(menuItem);
            infoPage.close();
            textPage("Booking was canceled","Cancel Booking",false);
            if (menuButton.getItems().isEmpty()) {
                menuButton.getItems().add(new MenuItem("No booking process in pending"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    //Input menu items into the menu buttons to show booking in progress and booking history
    public static void showBookingProgress(Integer userID, MenuButton booking, MenuButton booked) {
        try (Connection conn = DriverManager.getConnection(URL);
             //get all booking details for the user
             PreparedStatement pstmt = conn.prepareStatement("select * from booking where GuestID = ?")) {
            pstmt.setString(1, String.valueOf(userID));
            ResultSet rs1 = pstmt.executeQuery();
            while (rs1.next()){
                String bookingID = String.valueOf(rs1.getInt("BookingID"));
                String roomID  = String.valueOf(rs1.getInt("RoomID"));
                String statusInfo =  rs1.getString("Status");
                Label infoLabel = new Label("Booking Details");
                infoLabel.setStyle("-fx-font-size: 24px;" +
                        "-fx-font-family: 'Lucida Handwriting';");
                String desc =
                        "Booking ID: " + bookingID +
                                "\nRoom ID: " + roomID +
                                "\nCheck In Date: " + rs1.getDate("CheckInDate").toLocalDate()+
                                "\nCheck Out Date: " + rs1.getDate("CheckOutDate").toLocalDate()+
                                "\nPayment Type: " + rs1.getString("PaymentType")+
                                "\nTotal Amount: " + String.valueOf(rs1.getDouble("TotalAmount")) +
                                "\nBooking Date: " + rs1.getDate("BookingDate").toLocalDate() +
                                "\nStatus: " + statusInfo;

                MenuItem menuItem = new MenuItem(desc);
                menuItem.setOnAction(e -> {
                    //get booking info
                    VBox information = new VBox(30);
                    Stage infoPage = new Stage();
                    infoPage.initModality(Modality.APPLICATION_MODAL);
                    Label text = new Label(desc);
                    Button closeButton = new Button("Close");
                    closeButton.setOnAction(e3 -> {
                        infoPage.close();
                    });
                    information.getChildren().addAll(infoLabel, text,closeButton);
                    Button cancelBooking = new Button("Cancel Booking");
                    //only allows cancel of bookings if the booking is not accepted
                    if (statusInfo.equals("Pending")){
                        information.getChildren().add(cancelBooking);
                    }

                    cancelBooking.setAlignment(Pos.BOTTOM_RIGHT);
                    cancelBooking.setOnAction(event -> { cancelBooking(bookingID, roomID, menuItem, booking, infoPage);});

                    information.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(information,300,400);
                    scene.getStylesheets().add("file:Style.css");
                    infoPage.setScene(scene);
                    infoPage.setResizable(false);
                    infoPage.showAndWait();
                });

                //Show text in both menu Buttons to notify users that there are no booking in progress or there is no booking history
                if (booking.getItems().size() > 1 && booking.getItems().getFirst().getText().equals("No booking process in pending")) {
                    booking.getItems().removeFirst();
                }
                if (booked.getItems().size() > 1 && booked.getItems().getFirst().getText().equals("No Booked Rooms")){
                    booked.getItems().removeFirst();
                }

                if (statusInfo.equals("Pending")){
                    booking.getItems().add(menuItem);
                } else {
                    booked.getItems().add(menuItem);
                }
            }
            if (booking.getItems().isEmpty()) {
                booking.getItems().add(new MenuItem("No booking process in pending"));
            }
            if (booked.getItems().isEmpty()) {
                booked.getItems().add(new MenuItem("No Booked Rooms"));
            }
            rs1.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    //Method used to insert new bookings into the database
    public static void insertNewBooking(Integer id, String roomID, LocalDate checkIn, LocalDate checkOut, String Amount, String payMethods, String usage){
        //Insertnew booking details into sql
        String insertQuery = "INSERT INTO booking (GuestID, RoomID, CheckInDate, CheckOutDate, TotalAmount, PaymentType, BookingDate, Status)" +
                " VALUES (?,?,?,?,?,?,?,'Pending')";
        //Set room status of booked rooms to occupied
        String setUnavailable = "UPDATE room SET Status = 'occupied' WHERE RoomID = ?";
        try (Connection connection = DriverManager.getConnection(URL)) {
            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                pstmt.setString(1, String.valueOf(id));
                pstmt.setString(2, roomID);
                pstmt.setDate(3, Date.valueOf(checkIn));
                pstmt.setDate(4, Date.valueOf(checkOut));
                pstmt.setString(5, Amount);
                pstmt.setString(6, payMethods);
                pstmt.setDate(7, Date.valueOf(LocalDate.now()));
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt2 = connection.prepareStatement(setUnavailable)) {
                pstmt2.setString(1, roomID);
                pstmt2.executeUpdate();
            }

            if (!usage.equals("Admin")){
                textPage("Thank You for booking at our hotel. \nEnjoy your stay :)", "Confirmation", false);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            textPage("Invalid Input INSERT", "ERROR: Invalid Input", true);
        }
    }

    //Used to initialize all booking data
    public static ObservableList<Bookings> getBookingData(){
        bookingDataList.clear();
        String bookingInfoQuery = "Select * from booking order by status";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(bookingInfoQuery)
        ) {
            while (rs.next()) {
                Bookings bookings = new Bookings(rs.getInt("BookingID"),
                        rs.getInt("GuestID"),
                        rs.getInt("RoomID"),
                        rs.getDate("CheckInDate").toLocalDate(),
                        rs.getDate("CheckOutDate").toLocalDate(),
                        rs.getDouble("TotalAmount"),
                        rs.getString("PaymentType"),
                        rs.getDate("BookingDate").toLocalDate(),
                        rs.getString("Status"));
                bookingDataList.add(bookings);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookingDataList;
    }

    //used to automatically set booking status depending on dates
    public static void setBookingStatus(){
        for (Bookings bookings : bookingDataList) {
            if (bookings.getCheckOutDate().isBefore(LocalDate.now()) && !bookings.getStatus().equals("Canceled")) { //if checkoutdate is before today and the status is  not canceled
                updateBookingInDatabase(bookings.getBookingID(), "Status", "Checked Out"); //auto check out the room
                updateRoomInDatabase(bookings.getRoomID(), "Status", "cleaning"); //auto sets the room to cleaning status
            } else if (bookings.getCheckInDate().isBefore(LocalDate.now())) { //if the checkindate is before today
                if (bookings.getStatus().equals("Success")) { // if booking status is success, auto check in the room
                    //Check in if booking is accepted
                    updateBookingInDatabase(bookings.getBookingID(), "Status", "Checked In");
                    updateRoomInDatabase(bookings.getRoomID(), "Status", "occupied");
                } else if (!bookings.getStatus().equals("Checked Out") || ! bookings.getStatus().equals("Checked In")){
                    //if the reservation is not checked out or checked in meaning(pending or canceled), the rooms are all canceled
                    updateBookingInDatabase(bookings.getBookingID(), "Status", "Canceled");
                    updateRoomInDatabase(bookings.getRoomID(), "Status", "available");
                }
            }
        }
    }

    //Used to check if the booking details are valid
    public static void checkValidBookingInput(LocalDate checkInDate, LocalDate checkOutDate, ChoiceBox<?>... choiceBoxes){
        if (ChronoUnit.DAYS.between(checkInDate,checkOutDate) < 1){
            textPage("Check In Date Must Be Before Check Out Date","ERROR: Invalid Input",true);
            return;
        } else if (LocalDate.now().isAfter(checkInDate)) {
            textPage("Check In Date Must Be After Today's Date", "ERROR: Invalid Input",true);
            return;
        }

        for (ChoiceBox<?> choiceBox : choiceBoxes){
            if (choiceBox.getValue() == null){
                textPage("No Input Fields Should be Left Empty", "ERROR: Invalid Input", true);
            }
        }
    }

    //Used to get price depending on room price per night and duration of stay
    public static Double getPricing(Integer roomId, LocalDate checkInDate, LocalDate checkOutDate){
        Double totalAmount = 0.0;
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("Select Pricing from room where RoomID = ?")
        ) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                totalAmount = ChronoUnit.DAYS.between(checkInDate, checkOutDate) * rs.getDouble("Pricing");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalAmount;
    }

    //delete selected booking from database
    public static void deleteBooking(TableView<?> tableView, Bookings bookings){
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("Delete from booking where BookingID = ?")) {
            pstmt.setInt(1, bookings.getBookingID());
            pstmt.executeUpdate();
            bookingDataList.remove(bookings);
            tableView.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
