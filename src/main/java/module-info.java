module com.ct.cacatrackerproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;
    requires com.google.protobuf;
    requires jakarta.mail;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires java.prefs;

    opens com.ct.cacatrackerproject to javafx.fxml;
    exports com.ct.cacatrackerproject;
    exports com.ct.cacatrackerproject.controllers;
    opens com.ct.cacatrackerproject.controllers to javafx.fxml;
}