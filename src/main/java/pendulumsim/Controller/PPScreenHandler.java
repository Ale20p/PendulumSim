package pendulumsim.Controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import pendulumsim.Model.Equations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PPScreenHandler implements Initializable {
    private Stage mstage;
    @FXML AnchorPane PPSpane;
    @FXML Rectangle string;
    @FXML Circle ball;
    @FXML TextField lengthInput;
    @FXML Slider lengthSlider;
    @FXML TextField amplitudeInput;
    @FXML TextField gravityInput;
    @FXML TextField massInput;
    @FXML TextField periodInput;
    @FXML Button backButton;
    @FXML Pane pendulumHolder;

    private double length;
    private double gravity;
    private double amplitude;
    private double mass;
    private Timeline animation;

    public void setStage(Stage stage) {
        mstage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int positonX = (int) (pendulumHolder.getPrefWidth()/2);

        Rotate rotation = new Rotate(75, positonX, 0);
        pendulumHolder.getTransforms().add(rotation);

        KeyValue initkeyvalue = new KeyValue(rotation.angleProperty(),-75 , Interpolator.EASE_BOTH);
        KeyFrame initkeyframe = new KeyFrame(Duration.seconds(6), initkeyvalue);
        animation = new Timeline(initkeyframe);

        animation.setAutoReverse(true);
        animation.setCycleCount(Timeline.INDEFINITE);
    }

    public void backToStartEvent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/StartingScreen.fxml"));
        Parent root = loader.load();
        StartingScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);
    }

    public void  startEvent() {
        if (lengthInput.getText().isEmpty() && lengthSlider.getValue() == 0.0) {
            lengthInput.setText("240");
            length = Double.parseDouble(lengthInput.getText());
            string.setHeight(length);
            ball.setLayoutY(length);
        } else if (lengthInput.getText().isEmpty()) {
            lengthInput.setText(String.valueOf(lengthSlider.getValue()));
            length = lengthSlider.getValue();
            string.setHeight(length);
            ball.setLayoutY(length);
        } else {
            length = Double.parseDouble(lengthInput.getText());
            string.setHeight(length);
            ball.setLayoutY(length);
        }

        if (gravityInput.getText().isEmpty()) {
            gravityInput.setText("9.8");
            gravity = Double.parseDouble(gravityInput.getText());
        } else {
            gravity = Double.parseDouble(gravityInput.getText());
        }

        if (amplitudeInput.getText().isEmpty()) {
            amplitudeInput.setText("0.1");
            amplitude = Double.parseDouble(amplitudeInput.getText());
        } else {
            amplitude = Double.parseDouble(amplitudeInput.getText());
        }

        if (massInput.getText().isEmpty()) {
            massInput.setText("45");
            mass = Double.parseDouble(massInput.getText());
            ball.setRadius(mass);
        } else {
            mass = Double.parseDouble(massInput.getText());
            if (mass > 70.0) {
                ball.setRadius(70.0);
            } else if (mass < 25.0) {
                ball.setRadius(25.0);
            } else {
                ball.setRadius(mass);
            }
        }

        periodInput.setText(String.valueOf(Equations.calculatePeriod(length, gravity)));

        animation.play();
    }

    public void pauseEvent() throws IOException {
        animation.pause();
    }

    public void resetEvent() throws IOException {

    }

    @FXML
    public void settingsevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/SettingsScreen.fxml"));
        Parent root = loader.load();
        SettingsScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void infoevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/InfoScreen.fxml"));
        Parent root = loader.load();
        InfoScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);
    }
}
