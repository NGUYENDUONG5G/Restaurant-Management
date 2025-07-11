module org.example.restaurantmangement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.management;
    requires java.desktop;

    opens org.example.restaurantmangement to javafx.fxml;
    opens org.example.restaurantmangement.Model to javafx.base;
    exports org.example.restaurantmangement;
    exports org.example.restaurantmangement.Controller;
    opens org.example.restaurantmangement.Controller to javafx.fxml;
}