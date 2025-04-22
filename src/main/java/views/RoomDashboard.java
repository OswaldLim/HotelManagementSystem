package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RoomDashboard {

    private static Label totalRoomLabel = new Label();
    private static Label availabilityLabel = new Label();
    private static Label cleaningRoomLabel = new Label();
    private static Label maintenenceLabel = new Label();

    public static ObservableList<Label> labelList(){
        ObservableList<Label> listOfLabels = FXCollections.observableArrayList(
                availabilityLabel, cleaningRoomLabel, maintenenceLabel, totalRoomLabel
        );
        return listOfLabels;
    }

    public static VBox generateRoomDashboard(){
        Label label1 = new Label("Total Rooms: ");
        VBox totalRoomPane = new VBox(10,label1, totalRoomLabel);

        Label label2 = new Label("Rooms Available now: ");
        VBox availabilityPane = new VBox(10,label2,availabilityLabel);

        Label label3 = new Label("Rooms that need Cleaning: ");
        VBox cleaningRoomPane = new VBox(10,label3,cleaningRoomLabel);

        Label label4 = new Label("Rooms that need Maintenance: ");
        VBox maintenenceRoomPane = new VBox(10,label4, maintenenceLabel);

        VBox roomPanes = new VBox(10,totalRoomPane,availabilityPane,cleaningRoomPane,maintenenceRoomPane);
        formatDashboard(roomPanes);
        return roomPanes;
    }

    private static void formatDashboard(VBox roomPanes){
        for (Node nodes : roomPanes.getChildren()) {
            if (nodes instanceof VBox) {
                nodes.setStyle(
                        "-fx-border-color: #8B5A2B; " +         // Wood-like border color
                                "-fx-border-width: 2px; " +
                                "-fx-border-radius: 10px; " +
                                "-fx-padding: 10px; " +
                                "-fx-background-color: #FFF5EE;"        // Optional warm neutral background (like beige)
                );
                ((VBox) nodes).setAlignment(Pos.CENTER);
                ((VBox) nodes).setPadding(new Insets(10));
            }
        }
    }

}
