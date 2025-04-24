package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Feedback;
import models.RevenueData;

import static services.FeedbackService.getFeedback;
import static services.ReportService.*;
import static utils.TableUtils.formatTableColumnSize;
import static views.FeedbackView.getFeedbackTable;
import static views.LogoView.generateLogo;
import static views.PaymentTypeTableView.generatePaymentTypeTable;
import static views.PaymentTypeTableView.generatePieChart;
import static views.RevenueDataTableView.barChartGeneration;
import static views.RevenueDataTableView.createRevenueTable;

public class ReportGenerationView {
    public static HBox generateReportView() {
        //Generate Payment Table
        TableView<RevenueData> revenueTableView = createRevenueTable();
        formatTableColumnSize(revenueTableView);
        ObservableList<RevenueData> revenueData = FXCollections.observableArrayList();

        //Bar chart generation
        BarChart<String, Number> barChart = barChartGeneration();

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();

        //Second table to show payment types
        TableView<RevenueData> paymentTypeTableView = generatePaymentTypeTable();
        formatTableColumnSize(paymentTypeTableView);

        ObservableList<RevenueData> paymentData = FXCollections.observableArrayList();

        //Pie chart for payment data
        PieChart pieChart = generatePieChart();

        //choice box to filter data to be shown
        ChoiceBox<String> yearChoice = new ChoiceBox<>();
        getYearForFilter(yearChoice);

        Label filterLabel = new Label("Filter by Year: ");
        HBox filterArea = new HBox(20, filterLabel, yearChoice);

        //feedback Table
        ObservableList<Feedback> feedbackDataList = FXCollections.observableArrayList();

        TableView<Feedback> feedbackTableView = getFeedbackTable();
        formatTableColumnSize(feedbackTableView);

        Label feedbackLabel = new Label("Feedback and Ratings");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox rightSidePane = new VBox(20, feedbackLabel, feedbackTableView, spacer, generateLogo());
        rightSidePane.setAlignment(Pos.BOTTOM_RIGHT);


        //setting data to tableview
        generateAllReportData(yearChoice,paymentData, pieChart,barChart,dataSeries,revenueData);
        getFeedback(feedbackDataList);

        revenueTableView.setItems(revenueData);
        paymentTypeTableView.setItems(paymentData);
        feedbackTableView.setItems(feedbackDataList);

        //Building Page
        Label table1Text = new Label("Revenue Summary: ");
        revenueTableView.setItems(revenueData);
        feedbackTableView.setItems(feedbackDataList);
        paymentTypeTableView.setItems(paymentData);
        VBox table1 = new VBox(10, table1Text, revenueTableView);

        HBox table1Box = new HBox(20, table1, barChart);
        table1Box.setStyle(
                "-fx-border-color: #8B5A2B; " +         // Wood-like border color
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-color: #FFF5EE;"        // Optional warm neutral background (like beige)
        );

        Label table2Text = new Label("Payment Methods");
        VBox table2 = new VBox(10, table2Text, paymentTypeTableView);
        HBox table2Box = new HBox(20, table2, pieChart);
        table2Box.setStyle(
                "-fx-border-color: #8B5A2B; " +         // Wood-like border color
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-color: #FFF5EE;"        // Optional warm neutral background (like beige)
        );

        VBox leftSidePane = new VBox(10,filterArea,table1Box,table2Box);
        HBox insideScrollPane = new HBox(10, leftSidePane, rightSidePane);
        insideScrollPane.setStyle("-fx-background-color: #FDFCE1");
        rightSidePane.setPadding(new Insets(20));
        leftSidePane.setPadding(new Insets(20));
        return insideScrollPane;
        //end of report page
    }


}
