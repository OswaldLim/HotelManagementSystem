package controllers;

// RoomStatusController.java (UI Layer)
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import models.RoomStatus;
import services.RoomService;

import java.util.Iterator;

import static services.RoomService.getRoomStatus;

public class RoomStatusController {

    private static PieChart.Data availableData;
    private static PieChart.Data cleaningData;
    private static PieChart.Data maintenenceData;
    private static PieChart.Data occupiedData;

    private static ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            availableData = new PieChart.Data("Available Rooms", 0),
            cleaningData = new PieChart.Data("Rooms that need Cleaning", 0),
            maintenenceData = new PieChart.Data("Rooms in maintenance", 0),
            occupiedData = new PieChart.Data("Occupied Rooms", 0)
    );

    public static ObservableList<PieChart.Data> getPieChartData() {
        return pieChartData;
    }

    public static void updateRoomStatus(Label availabilityLabel, Label cleaningRoomLabel,
                                        Label maintenenceLabel, Label totalRoomLabel, PieChart pieChart) {
        // Fetch room status from the service
        RoomStatus roomStatus = getRoomStatus();

        // Update the UI
        availabilityLabel.setText(String.valueOf(roomStatus.getAvailableCount()));
        cleaningRoomLabel.setText(String.valueOf(roomStatus.getCleaningCount()));
        maintenenceLabel.setText(String.valueOf(roomStatus.getMaintenanceCount()));
        totalRoomLabel.setText(String.valueOf(roomStatus.getTotalCount()));

        // Update PieChart Data
        addPieData(roomStatus);
        pieChart.setData(pieChartData);
    }

    private static void addPieData(RoomStatus roomStatus) {
        if (roomStatus.getAvailableCount() > 0) {
            availableData.setPieValue(roomStatus.getAvailableCount());
            if (!pieChartData.contains(availableData)) {
                pieChartData.add(availableData);
            }
        } else availableData.setPieValue(0);

        if (roomStatus.getCleaningCount() > 0) {
            cleaningData.setPieValue(roomStatus.getCleaningCount());
            if (!pieChartData.contains(cleaningData)) {
                pieChartData.add(cleaningData);
            }
        } else cleaningData.setPieValue(0);

        if (roomStatus.getMaintenanceCount() > 0) {
            maintenenceData.setPieValue(roomStatus.getMaintenanceCount());
            if (!pieChartData.contains(maintenenceData)) {
                pieChartData.add(maintenenceData);
            }
        } else maintenenceData.setPieValue(0);

        if (roomStatus.getOccupiedCount() > 0) {
            occupiedData.setPieValue(roomStatus.getOccupiedCount());
            if (!pieChartData.contains(occupiedData)) {
                pieChartData.add(occupiedData);
            }
        } else occupiedData.setPieValue(0);

        Iterator<PieChart.Data> iterator = pieChartData.iterator();

        while (iterator.hasNext()){
            PieChart.Data d = iterator.next();
            if (d.getPieValue() < 1) {
                iterator.remove();
            }
        }

    }
}
