package com.example.coursework;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;




import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

public class Test extends Application {
    public static class Room {
        private String name;
        private double price;

        public Room(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    @Override
    public void start(Stage stage) {
        TableView<Room> tableView = new TableView<>();
        tableView.setEditable(true);

        TableColumn<Room, String> nameColumn = new TableColumn<>("Room Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));

        TableColumn<Room, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> event.getRowValue().setPrice(event.getNewValue()));

        tableView.getColumns().addAll(nameColumn, priceColumn);

        ObservableList<Room> rooms = FXCollections.observableArrayList(
                new Room("Deluxe", 200.00),
                new Room("Suite", 350.00)
        );

        tableView.setItems(rooms);

        Scene scene = new Scene(tableView, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Editable TableView");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
