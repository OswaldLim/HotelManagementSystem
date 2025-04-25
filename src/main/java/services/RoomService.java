package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Bookings;
import models.Room;
import models.RoomStatus;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static controllers.RoomStatusController.updateRoomStatus;
import static utils.AlertUtils.textPage;
import static views.BookingDetailsView.showBookingDetails;

public class RoomService {

    private static final String URL = "jdbc:sqlite:hotelManagementSystem.db";

    //gets count of each room status
    public static RoomStatus getRoomStatus() {
        String query = "SELECT Status, COUNT(*) AS Total FROM room GROUP BY Status";
        RoomStatus roomStatus = new RoomStatus();

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int total = 0;

            while (rs.next()) {
                String status = rs.getString("Status");
                int count = rs.getInt("Total");
                total += count;

                switch (status) {
                    case "occupied":
                        roomStatus.setOccupiedCount(count);
                        break;
                    case "available":
                        roomStatus.setAvailableCount(count);
                        break;
                    case "cleaning":
                        roomStatus.setCleaningCount(count);
                        break;
                    case "maintenance":
                        roomStatus.setMaintenanceCount(count);
                        break;
                    default:
                        break;
                }
            }

            roomStatus.setTotalCount(total);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomStatus;
    }

    //Update specific data of Rooms in the database
    public static void updateRoomInDatabase(int roomId, String column, Object newValue) {
        String sql = "update room set " + column + " = ? WHERE RoomID = ?";
        try (Connection conn =DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2,roomId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Check available rooms depending on the date chosen and return an SQL query for filtering data
    private static String checkAvailableRooms(LocalDate CheckInDate, LocalDate CheckOutDate, Integer capacityAmount) {
        if (ChronoUnit.DAYS.between(CheckInDate, CheckOutDate) < 1) {
            textPage("Invalid dates", "ERROR: Invalid Input", true);
            return null;
        } else if (LocalDate.now().isAfter(CheckInDate)) {
            textPage("Check In Date Must Be After Today's Date", "ERROR: Invalid Input", true);
            return null;
        }

        String query = "SELECT RoomID From booking WHERE CheckOutDate > ?";
        String query2 = "SELECT RoomID from booking where CheckInDate < ?";
        String query3;
        //list of rooms that are occupied when the user wants to check in
        ObservableList<Integer> invalidCheckInDate = FXCollections.observableArrayList();
        //list of rooms that are occupied when the user wants to check out
        ObservableList<Integer> invalidCheckOutDate = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             PreparedStatement pstmt2 = conn.prepareStatement(query2);
        ) {
            pstmt.setDate(1, Date.valueOf(CheckInDate));
            pstmt2.setDate(1, Date.valueOf(CheckOutDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    invalidCheckInDate.add(rs.getInt("RoomID"));
                }
            }

            try (ResultSet rs2 = pstmt2.executeQuery()) {
                while (rs2.next()) {
                    invalidCheckOutDate.add(rs2.getInt("RoomID"));
                }
            }

            ObservableList<Integer> commonRooms = FXCollections.observableArrayList(invalidCheckInDate);
            //Filters common rooms that are occupied
            commonRooms.retainAll(invalidCheckOutDate);

            if (commonRooms.isEmpty()) {
                query3 = "Select * from room where Capacity >= ";
            } else {
                String idString = commonRooms.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                //Filter out rooms that are available
                query3 = "SELECT * FROM room WHERE roomID not IN (" + idString + ") and Capacity >= ";
            }

            //Filters the room according to capacity
            if (capacityAmount != null) {
                query3 += capacityAmount;
            } else {
                query3 += "1";
            }

            return query3;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Integer> getAvailableRoomIds(Bookings booking, LocalDate date, String dateType){
        ObservableList<Integer> availableRoomIds = FXCollections.observableArrayList();

        if (dateType.equals("Check In")) {
            for (Room room: getAvailableRooms(date,booking.getCheckOutDate(), null)) {
                availableRoomIds.add(room.getRoomIdentificationNumber());
            }
        } else {
            for (Room room: getAvailableRooms(booking.getCheckInDate(),date, null)) {
                availableRoomIds.add(room.getRoomIdentificationNumber());
            }
        }
        return availableRoomIds;
    }

    //Get a list of available rooms
    public static ObservableList<Room> getAvailableRooms(LocalDate CheckInDate, LocalDate CheckOutDate, Integer capacityAmount){
        ObservableList<Room> availableRooms = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkAvailableRooms(CheckInDate, CheckOutDate, capacityAmount))
        ) {
            while (rs.next()) {
                //save the profile pic of users
                Image newImage = new Image("file:Images/Room/"+rs.getString("Pictures"));
                //create a data of Class Room for better data handling
                Room roomData = new Room(rs.getInt("RoomID"),rs.getInt("Capacity"),
                        rs.getDouble("Pricing"), rs.getString("Type"),
                        newImage,rs.getString("Status"));
                availableRooms.add(roomData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableRooms;
    }

    //filters the rooms and only show available rooms in the UI
    public static void filterRooms(VBox vBox, TextField capacityAmount, LocalDate CheckInDate, LocalDate CheckOutDate, Stage stage, Integer userID, Image profilepic){
        Image image;
        String picURL;
        boolean hasResults = false;

        String sqlForAvailableRooms;
        if (capacityAmount.getText().isEmpty()) {
            sqlForAvailableRooms = checkAvailableRooms(CheckInDate, CheckOutDate, null);
        } else {
            sqlForAvailableRooms = checkAvailableRooms(CheckInDate, CheckOutDate, Integer.valueOf(capacityAmount.getText()));
        }

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlForAvailableRooms)
        ) {
            while (rs.next()) {
                picURL = rs.getString("Pictures");
                image = new Image("file:Images/Room/"+picURL);
                ImageView imageView = new ImageView(image);
                hasResults = true;

                imageView.setFitWidth(250);
                imageView.setFitHeight(200);

                String id = String.valueOf(rs.getInt("RoomID"));
                //Save room data into a string of room description
                String description = "Capacity: " + String.valueOf(rs.getInt("Capacity")) +
                        "\nPricing (per night): RM" + String.valueOf(rs.getDouble("Pricing")) +
                        "\nRoom Type: " + rs.getString("Type");
                Button button = new Button(description, imageView);
                button.setContentDisplay(ContentDisplay.RIGHT);
                button.setFont(new Font("Georgia", 40));
                button.setGraphicTextGap(20);
                button.setPrefSize(Double.MAX_VALUE, 200);
                button.setOnAction(event -> showBookingDetails(stage,id,imageView,description, CheckInDate, CheckOutDate,userID, profilepic));
                vBox.getChildren().add(button);
            }
            if (!hasResults) {
                //if no rooms are available, show error
                textPage("There is currently no room Available", "No Available rooms", true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //get all rooms in the hotel regardless of availability
    public static void getAllRooms(ObservableList<Room> roomDataList, ObservableList<Label> labels, PieChart pieChart){
        //Available rooms
        String availableRooms = "SELECT * from room";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(availableRooms);
        ) {
            updateRoomStatus(labels.get(0),labels.get(1),labels.get(2),labels.get(3),pieChart);

            while (rs.next()) {
                Image newImage = new Image("file:Images/Room/"+rs.getString("Pictures"));
                Room roomData = new Room(rs.getInt("RoomID"),rs.getInt("Capacity"),
                        rs.getDouble("Pricing"), rs.getString("Type"),
                        newImage,rs.getString("Status"));
                roomDataList.add(roomData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    //insert new rooms into the database
    public static void insertNewRooms(TextField roomCapacityInfo, TextField roomPricingInfo, ChoiceBox<String> roomTypeInfo, AtomicReference<String> filePath, ObservableList<Room> roomDataList, ObservableList<Label> labels, PieChart pieChart){
        String insertQuery = "Insert into room (Capacity, Pricing, Type, Pictures) Values (?,?,?,?)";
        if (roomCapacityInfo.getText().isEmpty() ||
                roomPricingInfo.getText().isEmpty() ||
                roomTypeInfo.getValue().equals("Enter Room Type...") ||
                filePath.get().isEmpty()
        ) {
            textPage("No Input Fields Should be Left Empty", "ERROR: Invalid Input",true);
        } else {
            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            ) {
                int insertCapacity = Integer.valueOf(roomCapacityInfo.getText());
                double insertPrice = Double.valueOf(roomPricingInfo.getText());
                String insertType = roomTypeInfo.getValue();
                String insertPicture = filePath.get();

                pstmt.setString(1,String.valueOf(insertCapacity));
                pstmt.setDouble(2,insertPrice);
                pstmt.setString(3,insertType);
                pstmt.setString(4,insertPicture);

                int id = roomDataList.getLast().getRoomIdentificationNumber() +1;
                roomDataList.add(new Room(id,insertCapacity, insertPrice, insertType, new Image("file:Images/Room/"+filePath.get()), "available"));
                updateRoomStatus(labels.get(0),labels.get(1),labels.get(2),labels.get(3),pieChart);
            } catch (SQLException exception){
                exception.printStackTrace();
            }
        }

    }

    //delete rooms from the database
    public static void deleteRooms(TableView<Room> tableView, ObservableList<Room> roomDataList){
        Room selectedRoom = tableView.getSelectionModel().getSelectedItem();

        if (selectedRoom == null){
            textPage("Please Select a room to Delete", "ERROR: Invalid Input", true);
            return;
        }

        textPage("Are you sure you want to delete this room?", "Confirmation", false, true, confirmed -> {
            if (confirmed) {
                try (Connection conn = DriverManager.getConnection(URL);
                     PreparedStatement pstmt = conn.prepareStatement("Delete from room where RoomID = ?")) {
                    pstmt.setInt(1, selectedRoom.getRoomIdentificationNumber());
                    pstmt.executeUpdate();
                    roomDataList.remove(selectedRoom);
                    tableView.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
