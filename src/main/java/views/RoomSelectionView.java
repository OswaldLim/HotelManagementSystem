package views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;

import static services.RoomService.filterRooms;
import static utils.InputUtils.checkInputType;
import static views.LogoView.generateLogo;

public class RoomSelectionView {

    public static BorderPane showAvailableRooms(Stage stage, Integer userID, Image profilepic){
        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();

        //Date filters
        Label checkInLabel = new Label("Check In Date: ");
        DatePicker checkInPicker = new DatePicker(LocalDate.now());
        //don't allow users to type in the date picker
        checkInPicker.getEditor().setDisable(true);
        checkInPicker.getEditor().setOpacity(1);
        Label checkOutLabel = new Label("Check Out Date: ");
        DatePicker checkOutPicker = new DatePicker(LocalDate.now().plusDays(1));
        //don't allow users to type in the date picker
        checkOutPicker.getEditor().setDisable(true);
        checkOutPicker.getEditor().setOpacity(1);

        Label capacityFilterLabel = new Label("Capacity: ");
        TextField capacityAmount = new TextField();
        capacityAmount.setPromptText("Input Capacity....");
        capacityAmount.setMaxWidth(200);
        checkInputType(capacityAmount, Integer.class);

        gridPane.add(checkInLabel,0,0);
        gridPane.add(checkInPicker,1,0);
        gridPane.add(checkOutLabel,0,1);
        gridPane.add(checkOutPicker,1,1);
        gridPane.add(capacityFilterLabel,0,2);
        gridPane.add(capacityAmount,1,2);

        //format sizing of columns
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);

        gridPane.getColumnConstraints().addAll(col1, col2);

        Button searchButton = new Button("Search Rooms");

        //Makes sure the spacer is resizable and shrinkable
        Region spacer = new Region();
        spacer.setMinWidth(0);
        spacer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        spacer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //filter box area
        HBox filterBox = new HBox(30, gridPane, searchButton, spacer, generateLogo());
        filterBox.setStyle("-fx-background-color: #FDFCE1");

        //Image to show the user booking progress stage
        Image progressBarImage = new Image("file:Images/System Logo/Process 1.png");
        ImageView progressView = new ImageView(progressBarImage);

        progressView.setPreserveRatio(true);
        progressView.fitWidthProperty().bind(stage.widthProperty().multiply(0.75));


        VBox topPane = new VBox(10, filterBox, progressView);
        topPane.maxWidthProperty().bind(stage.widthProperty().multiply(0.75));
        filterBox.setPadding(new Insets(20));
        HBox.setMargin(searchButton, new Insets(25,0,0,0));

        //Search button used to apply filter chosen (Dates and capacity) and find suitable rooms available
        searchButton.setOnAction(e -> {
            borderPane.setCenter(scrollPane);
            LocalDate CheckInDate = checkInPicker.getValue();
            LocalDate CheckOutDate = checkOutPicker.getValue();

            VBox vBox = new VBox(10);

            filterRooms(vBox, capacityAmount, CheckInDate, CheckOutDate, stage, userID, profilepic);

            vBox.setPadding(new Insets(20));
            vBox.setBackground(new Background(new BackgroundFill(Color.web("#D0EFFF"), null, null)));
            scrollPane.setContent(vBox);
            scrollPane.setFitToWidth(true);

        });

        borderPane.setCenter(scrollPane);
        borderPane.setTop(topPane);
        return borderPane;
    }



}
