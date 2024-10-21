package org.example.pendulumsin;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class StartingScreenHandler implements Initializable {
    @FXML
    StackPane pendulum;

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Arc patharc = new Arc();
        patharc.setType(ArcType.OPEN);
        patharc.setCenterX(0);
        patharc.setCenterY(0);
        patharc.setRadiusX(50);
        patharc.setRadiusY(50);
        patharc.setStartAngle(180);
        patharc.setLength(180);

        animate(patharc);
    }
    void animate(Arc patharc){
        PathTransition pt = new PathTransition(Duration.seconds(3), patharc, pendulum);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.setCycleCount(PathTransition.INDEFINITE);
        pt.interpolatorProperty().setValue(Interpolator.LINEAR);
        pt.setAutoReverse(true);



        pt.play();
    }
}
