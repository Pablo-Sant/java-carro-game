module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.base;
    
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.view to javafx.fxml;
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.view;
}
