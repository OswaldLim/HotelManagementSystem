module com.example.coursework {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires jdk.xml.dom;
    requires java.desktop;

    opens com.example.coursework to javafx.fxml;
    opens models to javafx.base;
    exports com.example.coursework;
    exports app;
}