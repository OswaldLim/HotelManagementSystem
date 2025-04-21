package views;

import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.RevenueData;

public class PaymentTypeTableView {
    public static TableView<RevenueData> generatePaymentTypeTable(){
        TableView<RevenueData> tableView = new TableView<>();

        TableColumn<RevenueData, String> paymentTypeColumn = new TableColumn<>("Payment Types");
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        TableColumn<RevenueData, Integer> totalTransactionColumn = new TableColumn<>("Total Transactions");
        totalTransactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionAmount"));

        TableColumn<RevenueData, Double> paymentRevenueColumn = new TableColumn<>("Total Revenue");
        paymentRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        tableView.getColumns().addAll(paymentTypeColumn,paymentRevenueColumn,totalTransactionColumn);
        return tableView;
    }

    public static PieChart generatePieChart(){
        PieChart pieChart = new PieChart();

        pieChart.setTitle("Payment Types");
        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(true);

        return pieChart;
    }
}
