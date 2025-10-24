module com.example {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.media;
    requires java.base;
    
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.view to javafx.fxml;
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.view;
}
