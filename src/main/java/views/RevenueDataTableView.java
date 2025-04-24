package views;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.RevenueData;

public class RevenueDataTableView {

    //Create the table view to show revenue data and occupancy rate
    public static TableView<RevenueData> createRevenueTable(){
        TableView<RevenueData> tableView = new TableView<>();

        TableColumn<RevenueData, String> monthColumn = new TableColumn<>("Month");
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<RevenueData, Double> revenueColumn = new TableColumn<>("Total Revenue");
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        TableColumn<RevenueData, Double> occupancyColumn = new TableColumn<>("Occupancy Rate (%)");
        occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));

        tableView.getColumns().addAll(monthColumn, revenueColumn, occupancyColumn);
        return tableView;
    }

    //generate bar charts for above table
    public static BarChart<String, Number> barChartGeneration(){
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Revenue");

        BarChart<String, Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle("Monthly Revenue");
        barChart.setLegendVisible(false);

        return barChart;
    }
}
