module org.example.pendulumsin {
    requires javafx.controls;
    requires javafx.fxml;


    opens pendulumsim to javafx.fxml;
    exports pendulumsim;
}