module com.ct.cacatrackerproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;
    requires java.sql;
    requires com.google.protobuf;
    requires jakarta.mail;

    opens com.ct.cacatrackerproject to javafx.fxml;
    exports com.ct.cacatrackerproject;
    exports com.ct.cacatrackerproject.controllers;
    opens com.ct.cacatrackerproject.controllers to javafx.fxml;
}