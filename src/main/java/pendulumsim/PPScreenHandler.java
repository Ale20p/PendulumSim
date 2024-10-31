package pendulumsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PPScreenHandler implements Initializable {
    private Stage mstage;
    @FXML AnchorPane PPSpane;
        Line string = new Line();
    @FXML Circle ball;
    @FXML TextField lengthInput;
    @FXML TextField amplitudeInput;
    @FXML TextField gravityInput;
    @FXML TextField angularfrequencyInput;
    @FXML TextField massInput;
    @FXML Button backButton;

    private double length;
    private double gravity;
    private double amplitude;
    private double angularFrequency;
    private double time;
    private double mass;

    public void setStage(Stage stage) {
        mstage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        string.endXProperty().bind(ball.centerXProperty());
        string.endYProperty().bind(ball.centerYProperty());

        // Timeline animation which will update the pendulum's position every frame
        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(0.016), event -> {
            time += 0.016;

        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void updatePendulum() {
        time += 0.016;

        double theta = Equations.calculateDisplacement(amplitude, angularFrequency, time);
    }

    public void backToStartEvent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartingScreen.fxml"));
        Parent root = loader.load();
        StartingScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);
    }
    @FXML
    public void settingsevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsScreen.fxml"));
        Parent root = loader.load();
        SettingsScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void infoevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoScreen.fxml"));
        Parent root = loader.load();
        InfoScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }

    public void  startEvent() {
        if (lengthInput.getText().isEmpty()) {
            lengthInput.setText("1");
            length = Double.parseDouble(lengthInput.getText());
        } else {
            length = Double.parseDouble(lengthInput.getText());
            ball.setCenterY(string.getStartY() + length);
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

        if (angularfrequencyInput.getText().isEmpty()) {
            angularfrequencyInput.setText("100");
            angularFrequency = Double.parseDouble(angularfrequencyInput.getText());
        } else {
            angularFrequency = Double.parseDouble(angularfrequencyInput.getText());
        }

        if (massInput.getText().isEmpty()) {
            massInput.setText("0.5");
            mass = Double.parseDouble(massInput.getText());
        } else {
            mass = Double.parseDouble(massInput.getText());
        }


    }

    public void pauseEvent() throws IOException {

    }

    public void resetEvent() throws IOException {
        ball.setCenterX(0);
        ball.setCenterY(0);
    }
}
