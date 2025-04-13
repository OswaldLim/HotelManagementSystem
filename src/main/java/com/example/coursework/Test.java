package com.example.coursework;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pie Chart Example");

        // Data for the PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Java", 30),
                new PieChart.Data("Python", 40),
                new PieChart.Data("JavaScript", 20),
                new PieChart.Data("C++", 10)
        );

        // Create PieChart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Programming Language Usage");

        // Optional: show labels
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);

        // Layout
        StackPane root = new StackPane(pieChart);
        Scene scene = new Scene(root, 600, 400);

        // Set up and show stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
