module org.example.pendulumsin {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pendulumsin to javafx.fxml;
    exports org.example.pendulumsin;
}