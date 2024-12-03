module pendulumsin {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.media;


    opens pendulumsim to javafx.fxml;
    exports pendulumsim;
    exports pendulumsim.Controller;
    opens pendulumsim.Controller to javafx.fxml;
    exports pendulumsim.Model;
    opens pendulumsim.Model to javafx.fxml;
}