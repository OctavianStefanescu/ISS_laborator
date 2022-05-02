module com.example.semesterprojectiss {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.semesterprojectiss to javafx.fxml;

    exports domain;
    exports repository;
    exports service;
    exports com.example.semesterprojectiss;
}