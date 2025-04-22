module com.example.coursework {
    requires javafx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires jdk.xml.dom;
    requires java.desktop;

    opens models to javafx.base;
    exports app;
}